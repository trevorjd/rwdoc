package com.trevorjd.rwplugin;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import static com.trevorjd.rwplugin.rwdocUtils.rwdebug;
import static net.risingworld.api.utils.Utils.StringUtils.isHex;

public class saxHandler extends DefaultHandler {

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
    private String title = null;
    private String imageWidth = null;
    private String imageHeight = null;
    private String alignment = null;
    private String indent = null;
    private String imageFrame = null;
    private String wrapText = "true";

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
            //indent = attributes.getValue("tab");
            bTitle = true;
        } else if (qName.equalsIgnoreCase("menuitem"))
        {
            indent = attributes.getValue("tab");
            alignment = attributes.getValue("align");
            pageNum = attributes.getValue("page");
            wrapText = attributes.getValue("wrap");
            bMenuItem = true;
        } else if (qName.equalsIgnoreCase("headline"))
        {
            indent = attributes.getValue("tab");
            alignment = attributes.getValue("align");
            posx = attributes.getValue("posx");
            posy = attributes.getValue("posy");
            if (((posx != null) && (posy == null)) || ((posy != null) && (posx == null)))
            {rwdebug(2, "posx/posy pair incomplete in: \" + title");};
            textColor = attributes.getValue("color");
            textSize = attributes.getValue("size");
            wrapText = attributes.getValue("wrap");
            bHeadline = true;
        } else if (qName.equalsIgnoreCase("text"))
        {
            alignment = attributes.getValue("align");
            posx = attributes.getValue("posx");
            posy = attributes.getValue("posy");
            if (((posx != null) && (posy == null)) || ((posy != null) && (posx == null)))
            {rwdebug(2, "posx/posy pair incomplete in: \" + title");};
            textColor = attributes.getValue("color");
            textSize = attributes.getValue("size");
            wrapText = attributes.getValue("wrap");
            bText = true;
        } else if (qName.equalsIgnoreCase("image"))
        {
            alignment = attributes.getValue("align");
            posx = attributes.getValue("posx");
            posy = attributes.getValue("posy");
            if (((posx != null) && (posy == null)) || ((posy != null) && (posx == null)))
            {rwdebug(2, "posx/posy pair incomplete in: \" + title");};
            imageWidth = attributes.getValue("width");
            imageHeight = attributes.getValue("height");
            imageFrame = attributes.getValue("frame");
            if (((imageWidth != null) && (imageHeight == null)) || ((imageHeight != null) && (imageWidth == null)))
            {rwdebug(2, "width/height pair incomplete in: " + title);};
            imageFilename = attributes.getValue("filename");
            bImage = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("page")) {
            pageList.add(page);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        if (bTitle) {
            DocumentElement element = new DocumentElement();
            element.setElementType("title");
            element.setTextString(new String(ch, start, length));
            rwdebug(4, "SAX Add type: " + element.getElementType() + " text: " + element.getTextString());
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
                if(isHex(textColor)) { element.setTextColor(textColor); }
                else { rwdebug(2, "Invalid color attribute: " + textColor); }
            if(null != textSize)
                element.setTextSize(textSize);
            if(null != indent)
                element.setIndent(indent);
            if(null != wrapText)
                element.setWrapText(wrapText);
            element.setPageNumber(pageNum);
            element.setTextString(new String(ch, start, length));
            rwdebug(1, new String(ch, start, length));
            rwdebug(4, "SAX Add type: " + element.getElementType() + " text: " + element.getTextString() + " indent: " + element.getIndent());
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
                if(isHex(textColor)) { element.setTextColor(textColor); }
                    else { rwdebug(2, "Invalid color attribute: " + textColor); }
            if(null != textSize)
                element.setTextSize(textSize);
            if(null != indent)
                element.setIndent(indent);
            if(null != alignment)
                element.setAlignment(alignment);
            if(null != wrapText)
                element.setWrapText(wrapText);
            element.setTextString(new String(ch, start, length));
            rwdebug(4, "SAX Add type: " + element.getElementType() + " text: " + element.getTextString() + " indent: " + element.getIndent());
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
                if(isHex(textColor)) { element.setTextColor(textColor); }
                else { rwdebug(2, "Invalid color attribute: " + textColor); }
            if(null != textSize)
                element.setTextSize(textSize);
            if(null != alignment)
                element.setAlignment(alignment);
            if(null != wrapText)
                element.setWrapText(wrapText);
            element.setTextString(new String(ch, start, length));
            rwdebug(4, "SAX Add type: " + element.getElementType() + " text: " + element.getTextString());
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
            if(null != alignment)
                element.setAlignment(alignment);
            if(null != imageFrame)
                element.setImageFrame(imageFrame);
            rwdebug(4, "SAX Add type: " + element.getElementType() + " filename: " + element.getFileName());
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
        imageWidth = null;
        imageHeight = null;
        alignment = null;
        pageNum = null;
        indent = null;
        imageFrame = null;
        wrapText = "true";
    }

}

