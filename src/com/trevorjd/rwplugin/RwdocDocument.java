package com.trevorjd.rwplugin;

import java.util.ArrayList;

public class RwdocDocument
{
    ArrayList<DocumentPage> pageList = new ArrayList<DocumentPage>();
    String documentTitle;

    public RwdocDocument(String title, ArrayList<DocumentPage> pages)
    {
        documentTitle = title;
        pageList = pages;
    }

    public RwdocDocument()
    {

    }

    public void setPageList(ArrayList<DocumentPage> pageList)
    {
        this.pageList = pageList;
    }

    public void setDocumentTitle(String documentTitle)
    {
        this.documentTitle = documentTitle;
    }

    public String getDocumentTitle()
    {
        return documentTitle;
    }

    public ArrayList<DocumentPage> getPageList()
    {
        return pageList;
    }

    public void addPage(DocumentPage page)
    {
        pageList.add(page);
    }

    public DocumentPage getPagebyIndex(int pageIndex)
    {
        return pageList.get(pageIndex);
    }
}
