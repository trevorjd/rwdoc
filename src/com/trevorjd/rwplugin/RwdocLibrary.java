package com.trevorjd.rwplugin;

import jdk.nashorn.internal.ir.ReturnNode;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.trevorjd.rwplugin.rwdoc.PLUGINSFOLDER;
import static com.trevorjd.rwplugin.rwdocUtils.rwdebug;

public class RwdocLibrary
{
    // This class searches the Rising World plugins folder for rwdoc.xml files
    // and builds them into an RwdocLibrary object
    private static SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

    private static ArrayList<RwdocDocument> library = new ArrayList<RwdocDocument>();

    public RwdocLibrary()
    {
        buildLibrary();
    }

    public static void addDocument(RwdocDocument document)
    {
        library.add(document);
        rwdebug(3, "Added doc to library: " + document.getDocumentTitle() + " Now contains " + library.size() + " documents.");
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
        ArrayList<File> files = new ArrayList<File>();
        files = getFiles();
        parseFileList(files);
        setupDefaultDocument();
    }

    public static ArrayList<File> getFiles()
    {
        ArrayList<File> files = new ArrayList<File>();

        //String filename = child.getName();
        rwdebug(3, "Initialising library");
        try
        {
            rwdebug(3, "Searching for files in: " + PLUGINSFOLDER);
            Files.walk(Paths.get(PLUGINSFOLDER))
                    .filter(Files::isRegularFile)
                    .forEach((f) -> {
                        String file = f.toString();
                        if (file.endsWith("rwdoc.xml"))
                        {
                            try
                            {
                                rwdebug(3, "Found: " + file);
                                files.add(new File(file));
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return files;
    }


    public static void parseFileList(ArrayList<File> files)
    {
        for (File file : files)
        try
        {
            rwdebug(3, "Parsing file - " + file.getPath());
            SAXParser saxParser = saxParserFactory.newSAXParser();
            SAXHandler handler = new SAXHandler();
            saxParser.parse(file, handler);
            RwdocDocument document = new RwdocDocument(handler.getTitle(), handler.getDocument());
            rwdebug(3, "docpathfull=" + file.getPath());
            rwdebug(3, "docpath=" + file.getPath().substring(0,file.getPath().lastIndexOf(File.separator)));
            document.setDocumentPath(file.getPath().substring(0,file.getPath().lastIndexOf(File.separator)));

            addDocument(document);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void setupDefaultDocument()
    {
        // by this point, we have all documents loaded
        // now we need to get a list of titles and build the the menu list and default front page
        ArrayList<String> titleList = getTitleList();
        for (String s : titleList)
        {
            rwdebug(3, "Library contains: " + s);
        }
        RwdocDocument document = getDocumentbyTitle("default");
        String filepath = document.getDocumentPath();
        DocumentPage page0;
        DocumentPage page1;
        if (null == document)
        {
            rwdebug(2, "No default document found!");
            page0 = new DocumentPage();
            page1 = new DocumentPage();
            DocumentElement element = new DocumentElement();
            element.setElementType("headline");
            element.setTabstop("3");
            element.setTextString("No default document found!");

        } else
        {
            page0 = document.getPagebyIndex(0);
            if(null == page0)
            {
                rwdebug(2, "Default document incomplete. Missing Page 1");
                page0 = new DocumentPage();
            }
            page1 = document.getPagebyIndex(1);
            if(null == page1)
            {
                rwdebug(2, "Default document incomplete. Missing Page 2");
                page1 = new DocumentPage();
            }
        }

        for (int count = 0; count < titleList.size(); count++)
        {
            DocumentElement element = new DocumentElement();
            element.setElementType("menuitem");
            if(!titleList.get(count).equals("default")) // exclude itself
            {
                element.setTextString(titleList.get(count));
                element.setPageNumber("0");
            }
            page0.addElement(element);
        }
        RwdocDocument newDoc = new RwdocDocument();
        newDoc.setDocumentTitle("default");
        newDoc.addPage(page0);
        newDoc.addPage(page1);
        removeDocumentByTitle("default");
        newDoc.setDocumentPath(filepath);
        addDocument(newDoc);
        for (String s : titleList)
        {
            rwdebug(3, "Library contains: " + s);
        }
    }

    private static void removeDocumentByTitle(String titleToRemove)
    {
        rwdebug(3, "Removing document by title: " + titleToRemove);
        for (int count = 0; count < library.size(); count++)
        {
            if (library.get(count).getDocumentTitle().equals(titleToRemove))
            {
                library.remove(count);
            }
        }

    }


}