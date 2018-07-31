package com.trevorjd.rwplugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import static com.trevorjd.rwplugin.rwdocUtils.rwdebug;
import static net.risingworld.api.utils.Utils.StringUtils.isHex;

public class XMLParser {

    private static DocumentPage page;
    private static DocumentPage bufferPage = new DocumentPage();

    // bools for flagging during parsing
    private static boolean bTitle = false;
    private static boolean bMenuItem = false;
    private static boolean bHeadline = false;
    private static boolean bText = false;
    private static boolean bImage = false;

    // buffer variables
    private static String posx = null;
    private static String posy = null;
    private static String textColor = null;
    private static String textSize = null;
    private static String pageIndex = null;
    private static String imageFilename = null;
    private static String title = null;
    private static String imageWidth = null;
    private static String imageHeight = null;
    private static String alignment = null;
    private static String indent = null;
    private static String imageFrame = null;
    private static String wrapText = "true";
    private static String pivot = null;
    private static String charBuffer = "";
    private static DocumentElement elementBuffer = new DocumentElement();
    private static String legacyElement = null; // if a text or headline node contains a page attribute
    // we need to build a second page and insert it


    public static RwdocDocument parseXMLFile(String fileName)
    {
        ArrayList<DocumentPage> pageList = new ArrayList<DocumentPage>();

        rwdebug(3, "parseXMLFile Received: " + fileName);
        RwdocDocument document = new RwdocDocument();
        int pageNum = 0;
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty("javax.xml.stream.isCoalescing", true);
            InputStream inputStream = new FileInputStream(fileName);
            XMLEventReader eventReader =
                    factory.createXMLEventReader(inputStream, "UTF-8");

            while(eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                switch(event.getEventType()) {

                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();

                        if (qName.equalsIgnoreCase("page")) {
                            page = new DocumentPage();
                            Iterator<Attribute> attributes = startElement.getAttributes();
                            while(attributes.hasNext())
                            {
                                Attribute attribute = attributes.next();
                                switch (attribute.getName().toString().toLowerCase()) {
                                    case "index" :
                                        pageIndex = attribute.getValue();
                                        page.setPageIndex(pageIndex);
                                        break;
                                }
                            }
                            // handle documents missing an index, or having an invalid index, on page 0
                            rwdebug(3, "XMLParser: page=" + pageNum + " index=" + pageIndex);
                            if (pageNum == 0)
                            {
                                if (pageIndex == null || (!pageIndex.equals("start") && !pageIndex.equals("")))
                                {
                                    rwdebug(2, "Page 0 index is not 'start' in document " + fileName);
                                    rwdebug(2, "Overriding so things don't break.");
                                    page.setPageIndex("start");
                                }
                            }
                        } else if (qName.equalsIgnoreCase("title")) {
                            // titles don't have attributes
                            bTitle = true;
                        } else if (qName.equalsIgnoreCase("menuitem")) {
                            Iterator<Attribute> attributes = startElement.getAttributes();
                            while(attributes.hasNext())
                            {
                                Attribute attribute = attributes.next();
                                switch (attribute.getName().toString().toLowerCase()) {
                                    case "indent" :
                                        indent = attribute.getValue();
                                        break;
                                    case "align" :
                                        alignment = attribute.getValue();
                                        break;
                                    case "index" :
                                        pageIndex = attribute.getValue();
                                        break;
                                    case "wrap" :
                                        wrapText = attribute.getValue();
                                        break;
                                    case "color" :
                                        textColor = attribute.getValue();
                                        break;
                                    case "size" :
                                        textSize = attribute.getValue();
                                        break;
                                    case "pivot" :
                                        pivot = attribute.getValue();
                                        break;
                                }
                            }
                            bMenuItem = true;
                        } else if (qName.equalsIgnoreCase("headline")) {
                            Iterator<Attribute> attributes = startElement.getAttributes();
                            while(attributes.hasNext())
                            {
                                Attribute attribute = attributes.next();
                                switch (attribute.getName().toString().toLowerCase()) {
                                    case "indent" :
                                        indent = attribute.getValue();
                                        break;
                                    case "align" :
                                        alignment = attribute.getValue();
                                        break;
                                    case "wrap" :
                                        wrapText = attribute.getValue();
                                        break;
                                    case "posx" :
                                        posx = attribute.getValue();
                                        break;
                                    case "posy" :
                                        posy = attribute.getValue();
                                        break;
                                    case "color" :
                                        textColor = attribute.getValue();
                                        break;
                                    case "size" :
                                        textSize = attribute.getValue();
                                        break;
                                    case "pivot" :
                                        pivot = attribute.getValue();
                                        break;
                                    case "page" :
                                        legacyElement = attribute.getValue();
                                        break;
                                }
                            }
                            bHeadline = true;
                        } else if (qName.equalsIgnoreCase("text")) {
                            Iterator<Attribute> attributes = startElement.getAttributes();
                            while(attributes.hasNext())
                            {
                                Attribute attribute = attributes.next();
                                switch (attribute.getName().toString().toLowerCase()) {
                                    case "indent" :
                                        indent = attribute.getValue();
                                        break;
                                    case "align" :
                                        alignment = attribute.getValue();
                                        break;
                                    case "wrap" :
                                        wrapText = attribute.getValue();
                                        break;
                                    case "posx" :
                                        posx = attribute.getValue();
                                        break;
                                    case "posy" :
                                        posy = attribute.getValue();
                                        break;
                                    case "color" :
                                        textColor = attribute.getValue();
                                        break;
                                    case "size" :
                                        textSize = attribute.getValue();
                                        break;
                                    case "pivot" :
                                        pivot = attribute.getValue();
                                        break;
                                    case "page" :
                                        legacyElement = attribute.getValue();
                                        break;
                                }
                            }
                            bText = true;
                        } else if (qName.equalsIgnoreCase("image")) {
                            Iterator<Attribute> attributes = startElement.getAttributes();
                            while(attributes.hasNext())
                            {
                                Attribute attribute = attributes.next();
                                switch (attribute.getName().toString().toLowerCase()) {
                                    case "indent" :
                                        indent = attribute.getValue();
                                        break;
                                    case "align" :
                                        alignment = attribute.getValue();
                                        break;
                                    case "wrap" :
                                        wrapText = attribute.getValue();
                                        break;
                                    case "posx" :
                                        posx = attribute.getValue();
                                        break;
                                    case "posy" :
                                        posy = attribute.getValue();
                                        break;
                                    case "width" :
                                        imageWidth = attribute.getValue();
                                        break;
                                    case "height" :
                                        imageHeight = attribute.getValue();
                                        break;
                                    case "pivot" :
                                        pivot = attribute.getValue();
                                        break;
                                    case "filename" :
                                        imageFilename = attribute.getValue();
                                        break;
                                    case "frame" :
                                        imageFrame = attribute.getValue();
                                        break;
                                }
                            }
                            bImage = true;
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();
                        if(bTitle) {
                            DocumentElement element = new DocumentElement();
                            element.setElementType("title");
                            element.setTextString(characters.getData());
                            page.addElement(element);
                            document.setDocumentTitle(element.getTextString());
                            bTitle = false;
                            clearVars();
                        }
                        if(bHeadline) {
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
                            if(null != pivot)
                                element.setPivot(pivot);
                            element.setTextString(characters.getData());
                            if(null != legacyElement)
                            {
                                if(legacyElement.equalsIgnoreCase("right"))
                                {
                                    bufferPage.addElement(element);
                                    bufferPage.setLegacyPage(true);
                                } else
                                if(legacyElement.equalsIgnoreCase("left"))
                                {
                                    page.addElement(element);
                                    page.setLegacyPage(true);
                                } else { rwdebug(3, "unknown legacyHeadline: " + legacyElement); }
                            } else {
                                // Not a legacy element; no special treatment required
                                page.addElement(element);
                            }

                            bHeadline = false;
                            clearVars();
                        }
                        if(bMenuItem) {
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
                            if(null != pivot)
                                element.setPivot(pivot);
                            element.setDocTitle(document.getDocumentTitle());
                            element.setPageIndex(pageIndex);
                            element.setTextString(characters.getData());
                            page.addElement(element);
                            bMenuItem = false;
                            clearVars();
                        }
                        if(bText) {
                            //DocumentElement elementBuffer = new DocumentElement();
                            elementBuffer.setElementType("text");
                            if(null != posx)
                                elementBuffer.setxPosition(posx);
                            if(null != posy)
                                elementBuffer.setyPosition(posy);
                            if(null != textColor)
                                if(isHex(textColor)) { elementBuffer.setTextColor(textColor); }
                                else { rwdebug(2, "Invalid color attribute: " + textColor); }
                            if(null != textSize)
                                elementBuffer.setTextSize(textSize);
                            if(null != alignment)
                                elementBuffer.setAlignment(alignment);
                            if(null != wrapText)
                                elementBuffer.setWrapText(wrapText);
                            if(null != pivot)
                                elementBuffer.setPivot(pivot);
                            if(null != indent)
                                elementBuffer.setIndent(indent);
                            charBuffer = charBuffer + characters.getData();
                        }
                        if(bImage) {
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
                            if(null != pivot)
                                element.setPivot(pivot);
                            page.addElement(element);
                            bImage = false;
                            clearVars();
                        }
                        break;

                    case XMLStreamConstants.COMMENT:
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        EndElement endElement = event.asEndElement();
                        if(endElement.getName().getLocalPart().equalsIgnoreCase("text")) {
                            elementBuffer.setTextString(charBuffer);
                            if(legacyElement != null)
                            {
                                if(legacyElement.equalsIgnoreCase("right"))
                                {
                                    bufferPage.addElement(elementBuffer);
                                    bufferPage.setLegacyPage(true);
                                } else
                                if(legacyElement.equalsIgnoreCase("left"))
                                {
                                    page.addElement(elementBuffer);
                                    page.setLegacyPage(true);
                                } else { rwdebug(3, "unknown legacyText " + legacyElement); }
                            } else {
                                //not a legacy element; no special treatment required
                                page.addElement(elementBuffer);
                            }
                            bText = false;
                            charBuffer = "";
                            legacyElement = null;
                            elementBuffer = new DocumentElement();
                            clearVars();
                        }


                        if(endElement.getName().getLocalPart().equalsIgnoreCase("page")) {
                            rwdebug(3, "Setting pageNum: " + pageNum);
                            if(bufferPage.isLegacyPage())
                            {
                                // insert both the page (left side page) and the bufferPage (right side page)
                                page.setPageNumber(pageNum++);
                                pageList.add(page);
                                bufferPage.setPageNumber(pageNum++);
                                pageList.add(bufferPage);
                                bufferPage = new DocumentPage();
                            } else
                            {
                                page.setPageNumber(pageNum++);
                                pageList.add(page);
                            }

                        }

                        break;

                }

            }

        } catch (FileNotFoundException e) {
            rwdebug(2, "XML Parser, File not Found");
            e.printStackTrace();
        } catch (XMLStreamException e) {
            rwdebug(2, "XML Parser: Stream Exception.");
            e.printStackTrace();
        }
        document.setPageList(pageList);
        document.setDocumentPath(fileName.substring(0,fileName.lastIndexOf(File.separator)));
        // handle documents w/o a title element, or where title is empty
        if(document.getDocumentTitle() == null || document.getDocumentTitle().equals(""))
        {
            rwdebug(2, "File has no valid <title> element! " + fileName);
            File file = new File(fileName);
            document.setDocumentTitle(rwdoc.c.getProperty("msg_untitled") + ": " + file.getParentFile().getName() );
        }
        return document;
    }

    private static void clearVars()
    {
        posx = null;
        posy = null;
        textColor = null;
        textSize = null;
        pageIndex = null;
        imageFilename = null;
        imageWidth = null;
        imageHeight = null;
        alignment = null;
        indent = null;
        imageFrame = null;
        wrapText = "true";
        pivot = null;
    }
}