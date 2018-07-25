package com.trevorjd.rwplugin;

import net.risingworld.api.gui.GuiLabel;

public class MenuItem
{
    private static String linkDocTitle;
    private static int pageNum;
    private static GuiLabel guiLabel;

    public MenuItem()
    {
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

    public static void setGuiLabel(GuiLabel guiLabel)
    {
        MenuItem.guiLabel = guiLabel;
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

    public static GuiLabel getGuiLabel()
    {
        return guiLabel;
    }
}
