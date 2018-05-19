import javax.swing.*;
import java.awt.*;
class Ant
{
    int px,py,ppx,ppy,dx,dy,r;
    int bold;   //prevents clustering
    int k=0;
    int brainx[]=new int[10];
    int brainy[]=new int[10];
    int bc=0;  //barrier counter
    Coods hillP,foodP,presentP;
    Color cl;
    byte chro[];
    static Ant hAnt=null;
    Ant link;
    int fitness;
    int c,limit;
    byte rtst[]=new byte[4];
    boolean food_present;
    boolean bar_encounter;
    boolean naive=true;
    
    public Ant(int x,int y,Color clr,int f)
    {
        c=0;
        limit=0;
        r=5;
        px=x;
        py=y;
        cl=clr;
        fitness=f;
        bold=-1;
        
        hillP=new Coods(x,y);
        hillP.link=Coods.head;
        Coods.head=hillP;
        
        presentP=hillP;
        //initialize chromosome
        chro=new byte[4];
        int rn=new Random().rand(16);
        
        for(int i=0;i<4;i++)
        {
            chro[i]=(byte)(rn%2);
            rn=rn/2;
        }
    }
    public Ant(int x,int y,Color clr,int f,byte ch[])
    {
        c=0;
        limit=0;
        r=5;
        px=x;
        py=y;
        cl=clr;
        if(f>15)
        f=15;
        fitness=f;
        bold=-1;
        
        hillP=new Coods(x,y);
        hillP.link=Coods.head;
        Coods.head=hillP;
        
        presentP=hillP;
        //initialize chromosome
        chro=new byte[4];
        for(int i=0;i<ch.length;i++)
        {
            chro[i]=ch[i];
        }
        
    }
    public byte[] free_roam(byte st[])
    {
        c++;
        if(c>=limit)
        {
            c=0;
            limit=new Random().rand(10);
        
            for(int i=0;i<4;i++)
            {
                rtst[3-i]=(byte)(st[i]^chro[i]); 
            }
        }
        return rtst;
    }
    public void food_duty(int lx[],int ly[])
    {
       if(food_present==true)
       {
            if(dist(presentP.px,presentP.py,px,py)<1.5&&presentP.Hlink!=null)
            presentP=presentP.Hlink;
        }
        else 
        {
           if(dist(presentP.px,presentP.py,px,py)<1.5&&presentP.Flink!=null)
              presentP=presentP.Flink;
         }
        move(presentP.px,presentP.py,lx,ly);
    }
    private void move(int x,int y,int lx[],int ly[])
    {
        if(bar_encounter==false)
        {
            while(true)
            {
                if(bar_encounter==false)
                {
                    int xx=x-px;
                    int yy=y-py;
                    
                    if(Math.abs(xx)>Math.abs(yy))
                    {
                        if(xx>0)
                        dx=3;
                        else 
                        dx=-3;
                        dy=0;
                    }
                    else if(Math.abs(xx)<Math.abs(yy))
                    {
                        dx=0;
                        if(yy>0)
                        dy=3;
                        else 
                        dy=-3;
                    }
                    else
                    {
                        if(yy>0)
                        dy=2;
                        else 
                        dy=-2;
            
                        if(x>0)
                        dx=2;
                        else 
                        dx=-2;
                    }
                }
                if(bar_encounter==true)
                {
                    int rn=new Random().rand(16);
                    switch(rn)
                        {
                            case 0:dx=0;dy=-3;break;
                            case 1:dx=-1;dy=-3;break;
                            case 2:dx=-2;dy=-2;break;
                            case 3:dx=-3;dy=-1;break;
                            case 4:dx=3;dy=0;break;
                            case 5:dx=3;dy=-1;break;
                            case 6:dx=2;dy=-2;break;
                            case 7:dx=1;dy=-3;break;
                            case 8:dx=0;dy=3;break;
                            case 9:dx=1;dy=3;break;
                            case 10:dx=2;dy=2;break;
                            case 11:dx=3;dy=1;break;
                            case 12:dx=-3;dy=0;break;
                            case 13:dx=-3;dy=1;break;
                            case 14:dx=-2;dy=2;break;
                            case 15:dx=-1;dy=3;break;
                        }
                }
                
                int tpx=px+dx;
                int tpy=py+dy;
                if(tpx<lx[2]&&tpx>lx[0]&&tpy<ly[1]&&tpy>ly[0])
                {
                     bar_encounter=true;
                     bc=0;
                     continue;
                }
                else
                break;
            }
      }
      if(bar_encounter==true)
      {
          bc++;
          if(bc>20)
          {
              bc=0;
              bar_encounter=false;
              Coods cptr=new Coods((px+dx),(py+dy));
             cptr.link=Coods.head;
              Coods.head=cptr;
              if(naive==true)
                {
                    cptr.Hlink=presentP;
                    cptr.Flink=presentP.Flink;
                    presentP.Flink.Hlink=cptr;
                    presentP.Flink=cptr;
                }
                if(naive==false)
                {
                    if(food_present==false)
                    {
                        cptr.Hlink=presentP.Hlink;
                        cptr.Flink=presentP;
                        cptr.Hlink.Flink=cptr;
                        presentP.Hlink=cptr;
                    }
                }
              
            }
        }
            for(int i=1;i<10;i++)
            {
                brainx[i]=brainx[i-1];
                brainy[i]=brainy[i-1];
            }
            brainx[0]=px;
            brainy[0]=py;
            int tpx=px+dx;
            int tpy=py+dy;
            if(!(tpx<lx[2]&&tpx>lx[0]&&tpy<ly[1]&&tpy>ly[0]))
            {
                px=px+dx;
                py=py+dy;
            }
    }
    
    private double dist(int x,int y,int xx,int yy)
    {
        return Math.sqrt(Math.pow(x-xx,2)+Math.pow(y-yy,2));
    }
}