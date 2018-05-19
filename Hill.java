import java.awt.*;
import javax.swing.*;
class Hill
{
    int px,py,r;
    Color cl;
    static Hill hHill;
    Hill link;
    int fpoints;
    public Hill(int x,int y,Color clr)
    {
        fpoints=0;
        px=x;
        py=y;
        r=30;
        cl=clr;
    }
}