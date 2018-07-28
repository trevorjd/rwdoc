package com.trevorjd.rwplugin;

import javax.print.Doc;
import java.util.ArrayList;

import static com.trevorjd.rwplugin.rwdocUtils.rwdebug;

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

    public DocumentPage getPagebyIndex(String pageIndex)
    {
        DocumentPage result = null;
        for (int count = 0; count < pageList.size(); count++)
        {
            if(pageList.get(count).getPageIndex().equals(pageIndex))
            {
                result = pageList.get(count);
                result.setPageNumber(count);
            }
        }
        return result;
    }

    public DocumentPage getPagebyNumber(int pageNumber)
    {
        return pageList.get(pageNumber);
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
