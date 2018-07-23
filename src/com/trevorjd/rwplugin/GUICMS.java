package com.trevorjd.rwplugin;

import net.risingworld.api.gui.GuiPanel;

import java.util.ArrayList;

public class GUICMS
{
    // GUI Content Managment System
    // Builds a library of GUI elements from the RwdocLibrary
    private static ArrayList<RwdocDocument> library = rwdoc.rwdocLibrary.getLibrary();

    public static void debug()
    {
        System.out.println("rwdoc Debug: library contains " + library + " objects.");
    }

    public void buildFrontPage()
    {
        // build the front page
        // header image (optional)
        // no page title
        // main menu
        // -
    }
}
