package com.trevorjd.rwplugin;

public class MenuElement
{
    private static String title;
    private static int pageNum;

    public MenuElement()
    {
    }

    //setters

    public static void setPageNum(int pageNum)
    {
        MenuElement.pageNum = pageNum;
    }

    public static void setTitle(String title)
    {
        MenuElement.title = title;
    }

    //getters


    public static String getTitle()
    {
        return title;
    }

    public static int getPageNum()
    {
        return pageNum;
    }
}
