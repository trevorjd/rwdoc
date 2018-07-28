package com.trevorjd.rwplugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.trevorjd.rwplugin.XMLParser.parseXMLFile;
import static com.trevorjd.rwplugin.rwdoc.EXTSEARCH;
import static com.trevorjd.rwplugin.rwdoc.PLUGINSFOLDER;
import static com.trevorjd.rwplugin.rwdoc.c;
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
        rwdebug(3, "addDocument() received: " + document.getDocumentTitle() + " with " + document.getNumberofPages() + " pages.");
        library.add(document);
        rwdebug(3, "Added doc to library: " + document.getDocumentTitle() + "; Library now contains " + library.size() + " documents.");
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
        library = new ArrayList<RwdocDocument>();
        ArrayList<File> files = new ArrayList<File>();
        files = getFiles();
        parseFileList(files);
        setupDefaultDocument();
    }

    private static ArrayList<File> getFiles()
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

    private static boolean parseFileList(ArrayList<File> files)
    {
        boolean success = false;

        for (File file : files)
        try
        {
            rwdebug(3, "Parsing file: " + file.getPath());
            RwdocDocument document = new RwdocDocument();
            document = parseXMLFile(file.getPath());
            addDocument(document);
            success = true;
        } catch (Exception e)
        {
            rwdebug(2, "Error processing file: " + file.getPath());
            rwdebug(2, e.getMessage());
            e.printStackTrace();
        }
        return success;
    }

    private static void setupDefaultDocument()
    {
        // by this point, we have all documents loaded
        // now we need to get a list of titles and build the the menu list and default front page
        RwdocDocument document = new RwdocDocument();
        RwdocDocument newDoc = new RwdocDocument();
        ArrayList<String> titleList = getTitleList();
        boolean foundDefaultDoc = false;
        for (String s : titleList)
        {
            if(s.toLowerCase().equals("default"))
            {
                foundDefaultDoc = true;
            }
        }
        if (!foundDefaultDoc)
        {
            rwdebug(2, "No default document found!");
            document.setDocumentTitle("default");
            DocumentPage page0 = new DocumentPage();
            DocumentPage page1 = new DocumentPage();
            page0.setPageNumber(0);
            newDoc.addPage(page0);
            DocumentElement element = new DocumentElement();
            element.setElementType("headline");
            element.setPivot("center");
            element.setxPosition("0.5");
            element.setyPosition("0.5");
            element.setTextString(c.getProperty("msg_no_default_document"));
            page1.addElement(element);
            page1.setPageNumber(1);
            newDoc.addPage(page1);
        } else
        {
            document = getDocumentbyTitle("default");
            DocumentPage page0 = document.getPagebyNumber(0);
            if(page0 == null)
            {
                rwdebug(2, "Default document incomplete. Missing Page 0.");
                page0 = new DocumentPage();
                DocumentElement element = new DocumentElement();
                element.setElementType("title");
                element.setDocTitle("default");
                page0.addElement(element);
                page0.setPageNumber(0);
                newDoc.addPage(page0);
            } else
                if(page0.getTitle() == null)
                {
                    // page 0 exists but can't provide a title. Nuke it and build a clean one.
                    rwdebug(2, "Default document incomplete. Page 0 exists but missing <title> tag.");
                    page0 = new DocumentPage();
                    DocumentElement element = new DocumentElement();
                    element.setElementType("title");
                    element.setDocTitle("default");
                    element.setTextString("default");
                    page0.addElement(element);
                    page0.setPageNumber(0);
                    newDoc.addPage(page0);
                } else { newDoc.addPage(page0); }

            DocumentPage page1 = document.getPagebyNumber(1);
            if(null == page1)
            {
                rwdebug(2, "Default document incomplete. Page 1 does not exist.");
                page1 = new DocumentPage();
                DocumentElement element = new DocumentElement();
                element.setElementType("headline");
                element.setTextString("Default document is incomplete.");
                element.setPivot("centrebottom");
                page1.addElement(element);
                element = new DocumentElement();
                element.setElementType("headline");
                element.setTextString("Page 1 does not exist.");
                element.setPivot("centrebottom");
                page1.addElement(element);
                page1.setPageNumber(1);
                newDoc.addPage(page1);
            } else { newDoc.addPage(page1); }
        }
        // By this point we (should) have assured the existence of a document that contains
        // two pages. Page 0 has a title element and the document has a title.
        newDoc.setDocumentTitle("default");
        newDoc.setDocumentPath(document.getDocumentPath());
        // remove the old default doc and replace it with the new one
        removeDocumentByTitle("default");
        addDocument(newDoc);

        // Now we'll retrieve whatever's in memory, build a version with menu items, and replace with that.
        document = getDocumentbyTitle("default");
        newDoc = new RwdocDocument();
        DocumentPage page0 = document.getPagebyNumber(0);
        DocumentPage page1 = document.getPagebyNumber(1);

        for (int count = 0; count < titleList.size(); count++)
        {
            // iterate titleList; create a menu item for each; add it to page0
            DocumentElement element = new DocumentElement();
            element.setElementType("menuitem");
            if(!titleList.get(count).equals("default")) // exclude itself
            {
                element.setTextString(titleList.get(count));
                element.setPageIndex("start");
            } else { rwdebug(3, "exluding default doc from menulist"); }
            page0.addElement(element);
        }
        newDoc.addPage(page0);
        newDoc.addPage(page1);
        newDoc.setDocumentTitle("default");
        newDoc.setDocumentPath(document.getDocumentPath());
        // remove the old default doc and replace it with the new one
        removeDocumentByTitle("default");
        addDocument(newDoc);
    }

    private static void removeDocumentByTitle(String titleToRemove)
    {
        // if a specified document exists, remove it.
        for (int count = 0; count < library.size(); count++)
        {
            if (library.get(count).getDocumentTitle().equals(titleToRemove))
            {
                library.remove(count);
            }
        }

    }


}