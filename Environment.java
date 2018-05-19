import java.awt.*;
import javax.swing.*;
import java.io.*;
class Environment extends JPanel implements Runnable
{
    JFrame j;
    int X,Y;
    byte chro[];
    Color cl;
    boolean need;
    Thread t;
    int inin;
    int max_fit=0;
    int c=0;
    
    //Coods of Lake (Rectangle)
    int lx[],ly[],lr;
    public Environment()
    {
        X=Toolkit.getDefaultToolkit().getScreenSize().width;
        Y=Toolkit.getDefaultToolkit().getScreenSize().height;
        j=new JFrame("ANT SIMULATION");
        j.setVisible(true);
        j.setSize(X,Y);
        j.add(this);
	j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setIconImage(new ImageIcon("./antIcon.jpg").getImage());
        lx=new int[4];
        ly=new int[4];
        lr=100;
        lx[0]=X/2-lr/2;
        ly[0]=Y/2-lr/2;
        lx[1]=X/2-lr/2;
        ly[1]=Y/2+lr/2;
        lx[2]=X/2+lr/2;
        ly[2]=Y/2-lr/2;
        lx[3]=X/2+lr/2;
        ly[3]=Y/2+lr/2;
        
        cl=new Color(100,120,50);
        
        chro=new byte[4];
        need=false;
        
        int x=X/4;
        int y=Y/4;
        Color red=new Color(100,10,10);
        inin=20;    //initial number
        Hill.hHill=new Hill(-500,-500,Color.yellow);
        Hill hptr=new Hill(x,y,red);
        hptr.link=Hill.hHill;
        Hill.hHill=hptr;
        hptr=new Hill(X-x,Y-y,Color.white);
        hptr.link=Hill.hHill;
        Hill.hHill=hptr;
        
        Ant.hAnt=new Ant(-500,-500,Color.yellow,3);
        Ant ptr=Ant.hAnt;
        for(int i=0;i<inin;i++)
        {
            ptr=new Ant(x,y,red,2);
            ptr.link=Ant.hAnt;
            Ant.hAnt=ptr;
        }
        for(int i=0;i<inin;i++)
        {
            ptr=new Ant(X-x,Y-y,Color.white,2);
            ptr.link=Ant.hAnt;
            Ant.hAnt=ptr;
        }
        
        t=new Thread(this);
        t.start();
        
    }
    public void run()
    {
        /*
         *  Call every ants move()
         *    Move for food or freely
         *  check this by seeing if ant has food or not
         *      if it has food cood then food_duty().
         *      else free_roam().
         *  Check each ants proximity for Food and Enemies
         *      if ant is 10px from food then change food_present to true
         *      if ant is 10px from an enemy then start a fight. the stronger group wins.
         *      form groups of ants within a radius of 20px.
         *  if ants of the same type collide:
         *      if there is a naive ant then it receive's info from the other in food_duty
         */
        int f=900;
        int p=0;
        while(true)
        {
            try
            {
                Thread.sleep(100);
                repaint();
                
                //Food appearance
                Food fptr;
                f++;
                if(f>150)
                {
                    fptr=Food.hFood;
                    while(fptr!=null)
                    {
                        fptr.fbits=fptr.fbits-1;
                        fptr.red=fptr.red+10;
                                if(fptr.red<255)
                                fptr.cl=new Color(fptr.red,255,0);
                                else
                                fptr.cl=new Color(255,255,0);
                        fptr=fptr.link;
                    }
                    fptr=new Food(new Random().rand(X),new Random().rand(Y));
                    fptr.link=Food.hFood;
                    Food.hFood=fptr;
                    f=0;
                }
                p++;
                if(p>400)
                {
                    int kk=0;
                    Coods cptr=Coods.head;
                    while(kk++<30)
                    {
                        cptr=cptr.link;
                    }
                    cptr.link=null;
                    p=0;
                }
                //movement
                Ant aptr=Ant.hAnt;
                while(aptr!=null)
                {
                    byte st[]=new byte[4];
                    int rn=new Random().rand(16);
                    for(int i=0;i<4;i++)
                    {
                        st[3-i]=(byte)(rn%2);
                        rn=rn/2;
                    }
                    if(aptr.foodP==null)    //condition for free roam
                    {
                        String ss="";
                        byte s[]=aptr.free_roam(st);
                        aptr.bold--;
                        for(int i=0;i<4;i++)
                            ss=ss+s[i];
                        
                        switch(Integer.parseInt(ss))
                        {
                            case 0000:aptr.dx=0;aptr.dy=-3;break;
                            case 0001:aptr.dx=-1;aptr.dy=-3;break;
                            case 0010:aptr.dx=-2;aptr.dy=-2;break;
                            case 0011:aptr.dx=-3;aptr.dy=-1;break;
                            case 0100:aptr.dx=3;aptr.dy=0;break;
                            case 0101:aptr.dx=3;aptr.dy=-1;break;
                            case 0110:aptr.dx=2;aptr.dy=-2;break;
                            case 0111:aptr.dx=1;aptr.dy=-3;break;
                            case 1000:aptr.dx=0;aptr.dy=3;break;
                            case 1001:aptr.dx=1;aptr.dy=3;break;
                            case 1010:aptr.dx=2;aptr.dy=2;break;
                            case 1011:aptr.dx=3;aptr.dy=1;break;
                            case 1100:aptr.dx=-3;aptr.dy=0;break;
                            case 1101:aptr.dx=-3;aptr.dy=1;break;
                            case 1110:aptr.dx=-2;aptr.dy=2;break;
                            case 1111:aptr.dx=-1;aptr.dy=3;break;
                        }
                        
                        for(int i=1;i<5;i++)
                        {
                                aptr.brainx[i]=aptr.brainx[i-1];
                                aptr.brainy[i]=aptr.brainy[i-1];
                        }
                            aptr.brainx[0]=aptr.px;
                            aptr.brainy[0]=aptr.py;
                        //Check if new position is safe or not. if not dont move.
                        int tpx=aptr.px+aptr.dx;
                        int tpy=aptr.py+aptr.dy;
                        if(!(tpx<lx[2]&&tpx>lx[0]&&tpy<ly[1]&&tpy>ly[0]))
                        {
                            aptr.px=aptr.px+aptr.dx;
                            aptr.py=aptr.py+aptr.dy;
                        }
                    }
                    else
                    {
                        aptr.food_duty(lx,ly);
                    }
                     
                    //increase physical strength
                    if(aptr.fitness>10)
                    aptr.r=6;
                    if(aptr.fitness>20)
                    aptr.r=7;
                    if(aptr.fitness>30)
                    aptr.r=8;
                     if(aptr.fitness>40)
                    aptr.r=9;
                     if(aptr.fitness>50)
                    aptr.r=10;
                    aptr=aptr.link;
                }
                
                aptr=Ant.hAnt;
                while(aptr!=null)
                {
                    //Check in ants proximity
                    Ant ptr=Ant.hAnt;
                    while(ptr.link!=null)      
                    {
                        double d=dist(ptr,aptr);
                        if(d<10&&ptr.cl!=aptr.cl)   //start fight
                        {
                            Group friend=new Group(aptr);
                            Group foe=new Group(ptr);
                            if(foe.fitness>friend.fitness)
                            {
                                
                                for(int i=0;i<friend.k;i++)
                                {
                                    
                                    kill(friend.list[i]);
                                    
                                }
                            }
                            else if(foe.fitness<friend.fitness)
                            {
                                
                                for(int i=0;i<foe.k;i++)
                                {
                                    kill(foe.list[i]);
                                    
                                }
                                increase_fitness(friend);
                            }
                            else
                            {   reduce_fitness(foe);
                                reduce_fitness(friend);
                            }
                        }
                        if(d<7&&ptr.cl==aptr.cl)  //Info Exchange
                        {
                            if(ptr.foodP==null&&ptr.bold<0)
                            {
                                ptr.foodP=aptr.foodP;
                                ptr.hillP=aptr.hillP;
                                ptr.presentP=aptr.foodP;
                                ptr.food_present=false;
                            }
                        }
                        ptr=ptr.link;
                    } //check ants proxmity loop end    
                    
                        //check for food
                         fptr=Food.hFood;
                        while(fptr!=null)
                        {
                            if(dist(aptr,fptr)<7&&aptr.food_present==false)
                            {
                                if(aptr.foodP==null)
                                {
                                    aptr.foodP=new Coods(fptr.px,fptr.py);
                                    aptr.foodP.link=Coods.head;
                                    Coods.head=aptr.foodP;
                                    aptr.foodP.Hlink=aptr.hillP;
                                    aptr.hillP.Flink=aptr.foodP;
                                    aptr.presentP=aptr.foodP;
                                }
                                //change present pointer
                                aptr.presentP=aptr.foodP.Hlink;
                                
                                aptr.food_present=true;
                                
                                fptr.fbits=fptr.fbits-1;
                                fptr.red=fptr.red+10;
                                if(fptr.red<255)
                                fptr.cl=new Color(fptr.red,255,0);
                                else
                                fptr.cl=new Color(255,255,0);
                            }
                            fptr=fptr.link;
                        }
                        //code to remove food
                         fptr=Food.hFood;
                        while(fptr.link!=null)
                        {
                            if(fptr.link.fbits<0)
                                {
                                    fptr.link=fptr.link.link;
                                }
                            fptr=fptr.link;
                        }
                        
                        //check for hill
                         Hill hptr=Hill.hHill;
                    while(hptr!=null)
                    {
                        //condition to find the hill
                       if(Math.sqrt(Math.pow(aptr.px-hptr.px,2)+Math.pow(aptr.py-hptr.py,2))<3)
                            {
                                if(hptr.cl==aptr.cl)
                                {
                                    if(aptr.food_present==true)
                                    {
                                        aptr.food_present=false;
                                        aptr.fitness=aptr.fitness+1;
                                        hptr.fpoints++;
                                        
                                        
                                        //change present_pointer position
                                        aptr.presentP=aptr.hillP.Flink;
                                        
                                        //spawn ants
                                        if(max_fit<=aptr.fitness)
                                        max_fit=aptr.fitness;
                                        if((++c)%2==0)
                                        {
                                            c=0;
                                            ptr=new Ant(hptr.px,hptr.py,hptr.cl,max_fit,aptr.chro);
                                            ptr.link=Ant.hAnt;
                                            Ant.hAnt=ptr;
                                            max_fit=0;
                                        
                                        }
                                    }
                                }
                                else if(hptr.fpoints>0&&aptr.food_present==false/*&&aptr.foodP==null*/)
                                {
                                    hptr.fpoints--;
                                    aptr.food_present=true;
                                    if(aptr.foodP==null)
                                    {
                                    aptr.foodP=new Coods(hptr.px,hptr.py);
                                     aptr.foodP.link=Coods.head;
                                    Coods.head=aptr.foodP;
                                    aptr.foodP.Hlink=aptr.hillP;
                                    aptr.hillP.Flink=aptr.foodP;
                                }
                                    aptr.presentP=aptr.hillP;
                                }
                            }
                            
                       hptr=hptr.link;
                    }
                        
                      
                    if(aptr.px>X)
                        aptr.px=0;
                    if(aptr.px<0)
                        aptr.px=X;
                    if(aptr.py>Y)
                        aptr.py=0;
                    if(aptr.py<0)
                        aptr.py=Y;
                        
                    if(aptr.fitness==0)
                        kill(aptr);
                    
                    //check for ignorance of food-absence
                    if(dist(aptr.brainx[9],aptr.brainy[9],aptr.px,aptr.py)<2.0&&dist(aptr.brainx[4],aptr.brainy[4],aptr.px,aptr.py)<2.0&&aptr.food_present==false)
                         {
                             if(aptr.foodP!=null)
                             {
                                 aptr.bold=5;
                                 aptr.food_present=false;
                                 aptr.presentP=aptr.hillP;
                                 aptr.foodP=null;
                                 aptr.naive=true;
                             }
                         }
                    aptr=aptr.link;
                    repaint();
                }
            }
            catch(Exception e){}
        }
    }
     
    private void kill(Ant a)
    {
        Ant ptr=Ant.hAnt;
        while(ptr.link!=null)
        {
            if(ptr.link==a)
            {
                ptr.link=ptr.link.link;
                break;
            }
            ptr=ptr.link;
        }
    }
    private void increase_fitness(Group g)
    {
        for(int i=0;i<g.k;i++)
        {
            Ant ptr=Ant.hAnt;
            while(ptr!=null)
            {
                if(ptr==g.list[i])
                {
                    ptr.fitness=ptr.fitness+1;
                    
                    break;
                }
                ptr=ptr.link;
            }
        }
    }
    private void reduce_fitness(Group g)
    {
        for(int i=0;i<g.k;i++)
        {
            Ant ptr=Ant.hAnt;
            while(ptr!=null)
            {
                if(ptr==g.list[i])
                {
                    ptr.fitness=ptr.fitness-1;
                    
                    break;
                }
                ptr=ptr.link;
            }
        }
    }
    public double dist(int x,int y,int xx,int yy)
    {
        return Math.sqrt(Math.pow(x-xx,2)+Math.pow(y-yy,2));
    }
    public double dist(Ant a,Ant b)
    {
        return Math.sqrt(Math.pow(a.px-b.px,2)+Math.pow(a.py-b.py,2));
    }
     public double dist(Ant a,Food f)
    {
        return Math.sqrt(Math.pow(a.px-f.px,2)+Math.pow(a.py-f.py,2));
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        setBackground(cl);
        
        g.setColor(Color.blue);
        g.fillRect(lx[1],ly[2],lr,lr);
        
        Hill hptr=Hill.hHill;
        while(hptr.link!=null)
        {
            g.setColor(hptr.cl);
            g.fillArc(hptr.px-hptr.r/2,hptr.py-hptr.r/2,hptr.r,hptr.r,0,360);
            
            g.setColor(Color.black);
            if(hptr.fpoints>0)
            g.setColor(Color.green);
            g.fillArc(hptr.px-hptr.r/4,hptr.py-hptr.r/4,hptr.r/2,hptr.r/2,0,360);
            hptr=hptr.link;    
        }
        
        Ant aptr=Ant.hAnt;
        while(aptr.link!=null)
        {
            if(aptr.food_present==true)
            {
                g.setColor(Color.green);
                g.fillArc(aptr.px-aptr.r/2,aptr.py-aptr.r/2,4,4,0,360);
            }
            g.setColor(aptr.cl);
            g.fillArc(aptr.px-aptr.r/2,aptr.py-aptr.r/2,aptr.r,aptr.r,0,360);
            aptr=aptr.link;    
        }
        Food fptr=Food.hFood;
        while(fptr.link!=null)
        {
            g.setColor(fptr.cl);
            g.fillRect(fptr.px-fptr.r/2,fptr.py-fptr.r/2,fptr.r,fptr.r);
            fptr=fptr.link;    
        }
        Coods cptr=Coods.head;
        while(cptr!=null)
        {
            g.setColor(cptr.cl);
            g.fillArc(cptr.px,cptr.py,3,3,0,360);
            cptr=cptr.link;
        }
    }
}