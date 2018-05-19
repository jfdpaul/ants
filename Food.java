import java.awt.*;
class Food 
{
    int px,py,r;
    Color cl;
    static Food hFood=new Food(-99,-99);
    Food link;
    int fbits;
    int red;
    public Food(int x,int y)
    {
        red=0;
        px=x;
        py=y;
        r=10;
        cl=Color.green;
        fbits=20;//new Random().rand(20);
    }
}