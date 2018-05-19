import java.awt.*;
class Coods
{
    int px,py;
    Coods Hlink,Flink,link;
    Color cl;
    static Coods head;
    public Coods(int x,int y)
    {
        cl=Color.yellow;
        px=x;
        py=y;
        Hlink=null;
        Flink=null;
        link=null;
    }
}