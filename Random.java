class Random
{
    
    public int rand(int n)//n provides a range (0-n)
    {
        double f=Math.random();
        int t=(int)(f*100000000)%n;
        return t;
    }
    
}