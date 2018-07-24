package com.trevorjd.rwplugin;

import net.risingworld.api.gui.GuiLabel;

public class MenuItem extends GuiLabel
{
    private static String linkDocTitle;
    private static int pageNum;

    public MenuItem(float x, float y, boolean relativeposition, String linkDocTitle, int pageNum)
    {
        super(x, y, relativeposition);
        this.linkDocTitle = linkDocTitle;
        this.pageNum = pageNum;
    }

    public MenuItem(java.lang.String text, float x, float y, boolean relativeposition, String linkDocTitle, int pageNum)
    {
        super(text, x, y, relativeposition);
        this.linkDocTitle = linkDocTitle;
        this.pageNum = pageNum;
    }

    //setters

    public void setPageNum(int pageNum)
    {
        this.pageNum = pageNum;
    }

    public void setLinkDocTitle(String docTitle)
    {
        this.linkDocTitle = docTitle;
    }

    //getters

    public int getPageNum()
    {
        return pageNum;
    }

    public String getLinkDocTitle()
    {
        return linkDocTitle;
    }
}
