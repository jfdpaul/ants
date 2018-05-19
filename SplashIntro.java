import javax.swing.*;
import java.awt.*;

class SplashIntro extends JWindow implements Runnable
{
    Ant list,head;
    int X,Y;
    Thread t;
    public SplashIntro()
    {
        super();
        X=Toolkit.getDefaultToolkit().getScreenSize().width;
        Y=Toolkit.getDefaultToolkit().getScreenSize().height;
        head=null;
        this.setSize(300,200);
        this.setLocation(X/2-150,Y/2-100);
        this.setVisible(true);
        for(int i=0;i<1000;i++)
        {
            list=new Ant(0,50,Color.red,1);
            list.link=head;
            head=list;
        }
        this.toFront();
        t=new Thread(this);
        t.start();
    }
    public void run()
    {
        int c=0;
        while(c<170)
        {
            try{
                
            Thread.sleep(50);
            c++;
            move();
            repaint();
        }
        catch(Exception e){}
        }
         this.dispose();
        new Environment();
       
    }
    private void move()
    {
        list=head;
        while(list.link!=null)
        {
            list.py=list.py+(int)new Random().rand(8);
            list.px=list.px+(int)new Random().rand(8);
            if(list.px>300)
            list.px=0;
            if(list.py>200)
            list.py=0;
            list=list.link;
        }
    }
    public void paint(Graphics g)
    {
        //super.paint(g);
        list=head;
        
        setBackground(Color.green);
        while(list!=null)
        {
            g.setColor(list.cl);
            g.fillArc(list.px,list.py,list.r,list.r,0,360);
            
            list=list.link;
        }
        String s="ANTS";
           g.setColor(Color.black); 
        g.drawString(s,20,100);
        
        setFont(new Font("Serif", Font.BOLD, 100));
    }
    
}