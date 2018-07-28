package com.trevorjd.rwplugin;

import java.util.ArrayList;

import static com.trevorjd.rwplugin.rwdocUtils.rwdebug;

public class DocumentPage
{
    private int pageNumber = -1;
    private String pageIndex;
    private ArrayList<DocumentElement> elementList = new ArrayList<>();

    public DocumentPage(int pageNum, ArrayList<DocumentElement> elements)
    {
        pageNumber = pageNum;
        elementList = elements;
    }

    public DocumentPage()
    {
    }

    public void insertTitle(DocumentElement element) { elementList.add(0, element); }

    public void addElement(DocumentElement element)
    {
        elementList.add(element);
    }

    //setters
    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public void setElementList(ArrayList<DocumentElement> elementList)
    {
        elementList = elementList;
    }

    public void setPageIndex(String pageIndex)
    {
        this.pageIndex = pageIndex;
    }

    //getters
    public int getPageNumber()
    {
        return pageNumber;
    }

    public String getTitle()
    {
        String s = null;
        if (pageNumber == 0)
        {
            s = elementList.get(0).getTextString();
        }
        return s;
    }

    public ArrayList<DocumentElement> getElementList()
    {
        return elementList;
    }

    public int getNumberofElements() { return elementList.size(); }

    public String getPageIndex()
    {
        return pageIndex;
    }
}
