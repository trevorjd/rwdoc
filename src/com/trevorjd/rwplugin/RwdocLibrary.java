package com.trevorjd.rwplugin;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;

import static com.trevorjd.rwplugin.rwdoc.IMGPATHBUFFER;
import static com.trevorjd.rwplugin.rwdoc.c;

public class RwdocLibrary
{
    // This class collects any XML files present in the docs directory and turns them into an RwdocLibrary
    private static SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

    private static ArrayList<RwdocDocument> library = new ArrayList<RwdocDocument>();

    public RwdocLibrary()
    {
        buildLibrary();
    }

    public static void addDocument(RwdocDocument document)
    {
        library.add(document);
        System.out.println("rwdoc Debug: added document to library." + document.getDocumentTitle());
        System.out.println("rwdoc Debug: liblength = " + library.size());
    }

    public static ArrayList<RwdocDocument> getLibrary()
    {
        return library;
    }

    public static RwdocDocument getDocumentbyTitle(String title)
    {
        RwdocDocument document = null;
        for (int count = 0; count < library.size() ; count++)
        {
            if (library.get(count).getDocumentTitle().equals(title))
            {
                document = library.get(count);
            }
        }
        return document;
    }

    public static ArrayList<String> getTitleList()
    {
        ArrayList<String> result = new ArrayList<String>();

        for (int count = 0; count < library.size(); count++)
        {
            result.add(library.get(count).getDocumentTitle());
        }
        return result;
    }

    public static void buildLibrary()
    {
        File dir = new File(c.getProperty("rwdocDocsDir"));
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null)
        {
            for (File child : directoryListing)
            {
                String filename = child.getName();
                if (isXMLFile(filename))
                {
                    try
                    {
                        IMGPATHBUFFER = child.getPath().substring(0,child.getPath().lastIndexOf(File.separator));
                        System.out.println("rwdoc Debug: Parsing file - " + child.getPath() + child.getName());
                        SAXParser saxParser = saxParserFactory.newSAXParser();
                        SAXHandler handler = new SAXHandler();
                        saxParser.parse(child, handler);
                        RwdocDocument document = new RwdocDocument(handler.getTitle(), handler.getDocument());
                        library.add(document);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        } else
        {
            System.out.println("rwdoc: Unable to find any documents in " + c.getProperty("rwdocDocsDir"));
        }
    }

    private static boolean isXMLFile(String filename)
    {
        boolean success = false;
        String validExtension = ".xml";
        if (filename.length() > 3)
        {
            String testString = filename.substring(filename.length() - 4);
            if (testString.equals(validExtension))
            {
                success = true;
            }
        } else
        {
            // ignore non-XML files
        }
        return success;
    }

}