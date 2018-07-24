package com.trevorjd.rwplugin;

import java.util.ArrayList;

public class RwdocDocument
{
    ArrayList<DocumentPage> pageList = new ArrayList<DocumentPage>();
    String documentTitle;
    String documentPath;

    public RwdocDocument(String title, ArrayList<DocumentPage> pages)
    {
        documentTitle = title;
        pageList = pages;
    }

    public RwdocDocument()
    {

    }

    public void addPage(DocumentPage page)
    {
        pageList.add(page);
    }

    public DocumentPage getPagebyIndex(int pageIndex)
    {
        return pageList.get(pageIndex);
    }

    public void setPageList(ArrayList<DocumentPage> pageList)
    {
        this.pageList = pageList;
    }

    public ArrayList<DocumentPage> getPageList()
    {
        return pageList;
    }

    public int getNumberofPages() { return pageList.size(); }

    // setters

    public void setDocumentTitle(String documentTitle)
    {
        this.documentTitle = documentTitle;
    }

    public void setDocumentPath(String documentPath)
    {
        this.documentPath = documentPath;
    }

    // getters

    public String getDocumentTitle()
    {
        return documentTitle;
    }

    public String getDocumentPath()
    {
        return documentPath;
    }
}
