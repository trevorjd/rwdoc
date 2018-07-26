package com.trevorjd.rwplugin;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.trevorjd.rwplugin.XMLParser.parseXMLFile;
import static com.trevorjd.rwplugin.rwdoc.EXTSEARCH;
import static com.trevorjd.rwplugin.rwdoc.PLUGINSFOLDER;
import static com.trevorjd.rwplugin.rwdocUtils.rwdebug;

public class RwdocLibrary
{
    // This class searches the Rising World plugins folder for rwdoc.xml files
    // and builds them into an RwdocLibrary object
    //private static SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

    private static ArrayList<RwdocDocument> library;

    public RwdocLibrary()
    {
        buildLibrary();
    }

    public static void addDocument(RwdocDocument document)
    {
        library.add(document);
        rwdebug(3, "Added doc to library: " + document.getDocumentTitle() + "; Now contains " + library.size() + " documents.");
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
        ArrayList<RwdocDocument> backup = new ArrayList<RwdocDocument>();
        if (library != null)
        {
             backup = (ArrayList<RwdocDocument>) library.clone();
        }
        library = new ArrayList<RwdocDocument>();
        ArrayList<File> files = new ArrayList<File>();
        files = getFiles();
        if (!parseFileList(files))
        {
            rwdebug(2, "Failed to load all XML files. See log for details. Reverting to old library.");
            library.clear();
            library = (ArrayList<RwdocDocument>) backup.clone();
        } else {
            RwdocDocument doc0 = library.get(0);
            if(doc0 != null)
            {
                rwdebug(1, "doc0: " + doc0.getDocumentTitle());
            }
            RwdocDocument doc1 = library.get(1);
            if(doc1 != null)
            {
                rwdebug(1, "doc1: " + doc1.getDocumentTitle());
            }
            RwdocDocument doc2 = library.get(2);
            if(doc2 != null)
            {
                rwdebug(1, "doc2: " + doc2.getDocumentTitle());
            }
            RwdocDocument doc3 = library.get(3);
            if(doc3 != null)
            {
                rwdebug(1, "doc3: " + doc3.getDocumentTitle());
            }

            setupDefaultDocument(); }

    }

    public static ArrayList<File> getFiles()
    {
        ArrayList<File> files = new ArrayList<File>();

        //String filename = child.getName();
        rwdebug(3, "Initialising library");
        try
        {
            // respect 'search other plugins' setting
            String filePath;
            if (EXTSEARCH)
            {
                filePath = PLUGINSFOLDER;
            } else { filePath = PLUGINSFOLDER + "/rwdoc"; }

            rwdebug(3, "Searching for files in: " + filePath);
            Files.walk(Paths.get(filePath))
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

    public static boolean parseFileList(ArrayList<File> files)
    {
        boolean success = false;
        for (File file : files)
        try
        {
            /*
            rwdebug(3, "Parsing file - " + file.getPath());
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxHandler handler = new saxHandler();
            saxParser.parse(file, handler);
            RwdocDocument document = new RwdocDocument(handler.getTitle(), handler.getDocument());
            rwdebug(4, "docpathfull=" + file.getPath());
            rwdebug(4, "docpath=" + file.getPath().substring(0,file.getPath().lastIndexOf(File.separator)));
            document.setDocumentPath(file.getPath().substring(0,file.getPath().lastIndexOf(File.separator)));
            if (document.getDocumentTitle() == null)
            {
                rwdebug(2, "Document is missing a title tag! File: " + file.getPath());
            } else { addDocument(document); }
            */
            rwdebug(1, "Sending a file to get parsed.");
            RwdocDocument document = parseXMLFile(file.getPath());
            rwdebug(1, "Document title is... " + document.getDocumentTitle());
            library.add(document);
            rwdebug(3,"No fatal errors loading: " + file.getName());
            success = true;
        } catch (Exception e)
        {
            rwdebug(2, "Error processing file: " + file.getPath());
            rwdebug(2, e.getMessage());
            e.printStackTrace();
        }
        return success;

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
            element.setIndent("3");
            element.setTextString("No default document found!");

        } else
        {
            page0 = document.getPagebyIndex(0);
            if(null == page0)
            {
                rwdebug(2, "Default document incomplete. Missing Page 0");
                page0 = new DocumentPage();
            }
            page1 = document.getPagebyIndex(1);
            if(null == page1)
            {
                rwdebug(2, "Default document incomplete. Missing Page 1");
                page1 = new DocumentPage();
            }
        }

        for (int count = 0; count < titleList.size(); count++)
        {
            rwdebug(3, "page 0 size: " + page0.getNumberofElements());
            DocumentElement element = new DocumentElement();
            element.setElementType("menuitem");
            if(!titleList.get(count).equals("default")) // exclude itself
            {
                element.setTextString(titleList.get(count));
                element.setPageNumber("0");
                rwdebug(3, "Added menuitem to default document: " + element.getTextString() + " Page: " + element.getPageNumber());
            } else { rwdebug(3, "exluding default doc from menulist"); }
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