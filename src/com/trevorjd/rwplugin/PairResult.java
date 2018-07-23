package com.trevorjd.rwplugin;

public class PairResult
{
    private Object a;
    private Object b;

    public PairResult(Object a, Object b)
    {
        this.a = a;
        this.b = b;
    }

    public Object getA()
    {
        return a;
    }

    public Object getB()
    {
        return b;
    }

    public void setA(Object a)
    {
        this.a = a;
    }

    public void setB(Object b)
    {
        this.b = b;
    }
}
