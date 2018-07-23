package com.trevorjd.rwplugin;

import net.risingworld.api.Plugin;

import java.util.ArrayList;
import java.util.Iterator;

public class rwdocUtils
{

    private static RwdocLibrary library;

    protected static boolean initPlugin(Plugin plugin)
    {
        System.out.println("rwdoc Debug: initialising plugin.");
        boolean success = false;
        rwdoc.c = handlerProperties.getConfig();
        rwdoc.rwdocLibrary = new RwdocLibrary();
        GUICMS.debug();
        // writeXMLTest();
        // readXMLTest();

        if (rwdoc.c != null)
        {
            success = true;
        }
        else {
            System.out.println("rwdoc: Failed to load config & failed to create a new config file. Expect errors!");
        }
        return success;
    }


    public static String getDefaultWelcome()
    {
        String s = "Welcome to our server!";
        return s;
    }

    public static String getDefaultTitle()
    {
        String s = "rwDoc: Documentation for a Rising World";
        return s;
    }

    public static void writeXMLTest()
    {
        System.out.println("rwdoc Debug: writeXMLTest");
        // Testing library XML structure
        library = new RwdocLibrary();
        System.out.println("rwdoc Debug: created library");
        RwdocDocument document = new RwdocDocument();
        System.out.println("rwdoc Debug: created document");
        ArrayList<DocumentPage> pages = new ArrayList<DocumentPage>();
        System.out.println("rwdoc Debug: created pages array");
        DocumentPage page = new DocumentPage();
        System.out.println("rwdoc Debug: created new page");
        page.setPageNumber(1);
        System.out.println("rwdoc Debug: set page number");

        DocumentElement element = new DocumentElement();
        element.setElementType("title");
        element.setTextString("Trevor's Fishy Business");
        System.out.println("rwdoc Debug: setting title element");
        page.addElement(element);

        element = new DocumentElement();
        element.setElementType("headline");
        element.setTextString("How to Catch Fish");
        System.out.println("rwdoc Debug: setting headline element 1");
        page.addElement(element);

        element = new DocumentElement();
        element.setElementType("headline");
        element.setTextString("How to Buy Fish");
        System.out.println("rwdoc Debug: setting headline element 2");
        page.addElement(element);

        element = new DocumentElement();
        element.setElementType("headline");
        element.setTextString("How to Breed Fish");
        System.out.println("rwdoc Debug: setting headline element 3");
        page.addElement(element);

        element = new DocumentElement();
        element.setElementType("test");
        element.setTextString("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        System.out.println("rwdoc Debug: setting text element");
        page.addElement(element);

        System.out.println("rwdoc Debug: adding page");
        pages.add(page);

        System.out.println("rwdoc Debug: getting pagenum");
        if (page.getTitle() != null)
        {
            System.out.println("rwdoc Debug: getting title");
            document.setDocumentTitle(page.getTitle());
        }
        document.addPage(page);
        library.addDocument(document);

    }

    public static void readXMLTest()
    {
        System.out.println("rwdoc Debug: readXMLTest");
        ArrayList<RwdocDocument> documents = library.getLibrary();
        System.out.println("rwdoc Debug: have document arraylist");
        System.out.println("rwdoc Debug: test liblength = " + documents.size());
        RwdocDocument document = documents.get(0);
        System.out.println("rwdoc Debug: have document");
        String title = document.getDocumentTitle();
        System.out.println("rwdoc Debug: title = " + title);

        ArrayList<DocumentPage> pages = document.getPageList();
        System.out.println("rwdoc Debug: pages = " + pages.size());

        DocumentPage page = pages.get(0);
        System.out.println("rwdoc Debug: have page");
        ArrayList<DocumentElement> elements = page.getElementList();
        System.out.println("rwdoc Debug: elements = " + elements.size());

    }

    private static final String newline = System.getProperty("line.separator");
    // private static final String newline = "\n";

    public static String wordWrap(String text, int fontSize) {
        // calculate wrapping width based on font size
        int linelength;
        if (fontSize <= 10) { linelength = 112; } else
        if (fontSize <= 15) { linelength = 76; } else
        if (fontSize <= 20) { linelength = 50; } else
        if (fontSize <= 25) { linelength = 40; } else
        if (fontSize <= 30) { linelength = 38; } else
        if (fontSize <= 35) { linelength = 33; } else
        if (fontSize <= 40) { linelength = 29; } else
        if (fontSize <= 45) { linelength = 26; } else
        if (fontSize <= 50) { linelength = 23; } else
            linelength = 20;

        //:: Trim
        while(text.length() > 0 && (text.charAt(0) == '\t' || text.charAt(0) == ' '))
            text = text.substring(1);

        //:: If Small Enough Already, Return Original
        if(text.length() < linelength)
            return text;

        //:: If Next length Contains Newline, Split There
        if(text.substring(0, linelength).contains(newline))
            return text.substring(0, text.indexOf(newline)).trim() + newline +
                    wordWrap(text.substring(text.indexOf(newline) + 1), fontSize);

        //:: Otherwise, Split Along Nearest Previous Space/Tab/Dash
        int spaceIndex = Math.max(Math.max( text.lastIndexOf(" ",  linelength),
                text.lastIndexOf("\t", linelength)),
                text.lastIndexOf("-",  linelength));

        //:: If No Nearest Space, Split At length
        if(spaceIndex == -1)
            spaceIndex = linelength;

        //:: Split
        return text.substring(0, spaceIndex).trim() + newline + wordWrap(text.substring(spaceIndex), fontSize);
    }
}
