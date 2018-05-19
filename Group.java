class Group
{
    Ant list[];
    int k;
    int fitness;
    public Group(Ant a)
    {
        list=new Ant[10];
        list[0]=a;
        k=1;
        fitness=a.fitness;
        search();
    }
    public void search()
    {
        Ant ptr=Ant.hAnt;
        while(ptr!=null)
        {
            if(ptr.cl==list[0].cl)
            {
                if(Math.sqrt(Math.pow(ptr.px-list[0].px,2)+Math.pow(ptr.py-list[0].py,2))<20)
                {
                    list[k++]=ptr;
                    fitness+=ptr.fitness;
                }
            }
            ptr=ptr.link;
        }
    }
}