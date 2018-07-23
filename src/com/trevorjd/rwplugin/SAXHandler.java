package com.trevorjd.rwplugin;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {

    // OK, so this is tricky.
    // Each DocumentPage consists of multiple DocumentElement: title, menuitem, headline, image, text
    // Most elements are optional
    // Pages can have multiple text elements
    // Elements must be accessed in order as written in the file
    // Pages will be sequential, but the order of elements may vary

    private ArrayList<DocumentPage> pageList = null;
    private DocumentPage page;

    private boolean bTitle = false;
    private boolean bMenuItem = false;
    private boolean bHeadline = false;
    private boolean bText = false;
    private boolean bImage = false;

    // buffer variables
    private String posx = null;
    private String posy = null;
    private String textColor = null;
    private String textSize = null;
    private String pageNum = null;
    private String imageFilename = null;
    private String title;
    private String imageWidth;
    private String imageHeight;

    public ArrayList<DocumentPage> getDocument()
    {
        return pageList;
    }

    public String getTitle()
    {
        return title;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
    {
        if (qName.equalsIgnoreCase("page"))
        {
            String index = attributes.getValue("index");
            page = new DocumentPage();
            page.setPageNumber(Integer.parseInt(index));
            if (pageList == null)
                pageList = new ArrayList<>();
        } else if (qName.equalsIgnoreCase("title"))
        {
            //set boolean values for fields, will be used in setting page variables
            bTitle = true;
        } else if (qName.equalsIgnoreCase("menuitem"))
        {
            pageNum = attributes.getValue("page");
            bMenuItem = true;
        } else if (qName.equalsIgnoreCase("headline"))
        {
            posx = attributes.getValue("posx");
            posy = attributes.getValue("posy");
            if (((posx != null) && (posy == null)) || ((posy != null) && (posx == null)))
            {System.out.println("rwdoc ERROR - posx/posy pair incomplete in: " + title);};
            textColor = attributes.getValue("color");
            textSize = attributes.getValue("size");
            bHeadline = true;
        } else if (qName.equalsIgnoreCase("text"))
        {
            posx = attributes.getValue("posx");
            posy = attributes.getValue("posy");
            if (((posx != null) && (posy == null)) || ((posy != null) && (posx == null)))
            {System.out.println("rwdoc ERROR - posx/posy pair incomplete in: " + title);};
            textColor = attributes.getValue("color");
            textSize = attributes.getValue("size");
            bText = true;
        } else if (qName.equalsIgnoreCase("image"))
        {
            posx = attributes.getValue("posx");
            posy = attributes.getValue("posy");
            if (((posx != null) && (posy == null)) || ((posy != null) && (posx == null)))
            {System.out.println("rwdoc ERROR - posx/posy pair incomplete in: " + title);};
            imageWidth = attributes.getValue("width");
            imageHeight = attributes.getValue("height");
            if (((imageWidth != null) && (imageHeight == null)) || ((imageHeight != null) && (imageWidth == null)))
            {System.out.println("rwdoc ERROR - width/height pair incomplete in: " + title);};
            imageFilename = attributes.getValue("filename");
            bImage = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("page")) {
            //add Employee object to list
            pageList.add(page);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        if (bTitle) {
            DocumentElement element = new DocumentElement();
            element.setElementType("title");
            if(null != posx)
                element.setxPosition(posx);
            if(null != posy)
                element.setyPosition(posy);
            if(null != textColor)
                element.setTextColor(textColor);
            if(null != textSize)
                element.setTextSize(textSize);
            element.setTextString(new String(ch, start, length));
            System.out.println("rwdoc Debug: elementType = " + element.getElementType());
            System.out.println("rwdoc Debug: elementText = " + element.getTextString());
            System.out.println("rwdoc Debug: adding title element");
            page.addElement(element);
            title = new String(ch, start, length);
            bTitle = false;
            clearVars();
        }
        else if (bMenuItem) {
            DocumentElement element = new DocumentElement();
            element.setElementType("menuitem");
            if(null != posx)
                element.setxPosition(posx);
            if(null != posy)
                element.setyPosition(posy);
            if(null != textColor)
                element.setTextColor(textColor);
            if(null != textSize)
                element.setTextSize(textSize);
            element.setPageNumber(pageNum);
            element.setTextString(new String(ch, start, length));
            System.out.println("rwdoc Debug: adding menuitem element");
            page.addElement(element);
            bMenuItem = false;
            clearVars();
        }
        else if (bHeadline) {
            DocumentElement element = new DocumentElement();
            element.setElementType("headline");
            if(null != posx)
                element.setxPosition(posx);
            if(null != posy)
                element.setyPosition(posy);
            if(null != textColor)
                element.setTextColor(textColor);
            if(null != textSize)
                element.setTextSize(textSize);
            element.setTextString(new String(ch, start, length));
            System.out.println("rwdoc Debug: adding headline element");
            page.addElement(element);
            bHeadline = false;
            clearVars();
        }
        else if (bText) {
            DocumentElement element = new DocumentElement();
            element.setElementType("text");
            if(null != posx)
                element.setxPosition(posx);
            if(null != posy)
                element.setyPosition(posy);
            if(null != textColor)
                element.setTextColor(textColor);
            if(null != textSize)
                element.setTextSize(textSize);
            element.setTextString(new String(ch, start, length));
            System.out.println("rwdoc Debug: adding text element");
            page.addElement(element);
            bText = false;
            clearVars();
            }
        else if (bImage) {
            DocumentElement element = new DocumentElement();
            element.setElementType("image");
            if(null != posx)
                element.setxPosition(posx);
            if(null != posy)
                element.setyPosition(posy);
            if(null != textColor)
                element.setTextColor(textColor);
            if(null != textSize)
                element.setTextSize(textSize);
            if(null != imageFilename)
                element.setFileName(imageFilename);
            if(null != imageWidth)
                element.setImageWidth(imageWidth);
            if(null != imageHeight)
                element.setImageHeight(imageHeight);
            System.out.println("rwdoc Debug: adding image element");
            page.addElement(element);
            bImage = false;
            clearVars();
        }
    }

    private void clearVars()
    {
        posx = null;
        posy = null;
        textColor = null;
        textSize = null;
        pageNum = null;
        imageFilename = null;
    }
}

