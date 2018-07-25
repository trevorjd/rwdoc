package com.trevorjd.rwplugin;

import java.util.ArrayList;

public class DocumentPage
{
    private int pageNumber;
    private ArrayList<DocumentElement> elementList = new ArrayList<>();

    public DocumentPage(int pageNum, ArrayList<DocumentElement> elements)
    {
        pageNumber = pageNum;
        elementList = elements;
    }

    public DocumentPage()
    {
    }

    public void addElement(DocumentElement element)
    {
        elementList.add(element);
    }

    //setters
    public void setPageNumber(int pageNumber)
    {
        pageNumber = pageNumber;
    }

    public void setElementList(ArrayList<DocumentElement> elementList)
    {
        elementList = elementList;
    }

    //getters
    public int getPageNumber()
    {
        return pageNumber;
    }

    public String getTitle()
    {
        String s = null;
        if (pageNumber == 1)
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
}
