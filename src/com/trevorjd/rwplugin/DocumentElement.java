package com.trevorjd.rwplugin;

public class DocumentElement
{
    private String elementType;
    private String textString;
    private String xPosition;
    private String yPosition;
    private String textColor;
    private String textSize;
    private String pageNumber;
    private String fileName;
    private String imageWidth;
    private String imageHeight;

    public DocumentElement()
    {
    }

    public String getxPosition()
    {
        return xPosition;
    }

    public String getyPosition()
    {
        return yPosition;
    }

    public String getTextColor()
    {
        return textColor;
    }

    public String getTextSize()
    {
        return textSize;
    }

    public String getElementType()
    {
        return elementType;
    }

    public String getTextString()
    {
        return textString;
    }

    public String getPageNumber() { return pageNumber; }

    public String getFileName()
    {
        return fileName;
    }

    public String getImageHeight()
    {
        return imageHeight;
    }

    public String getImageWidth()
    {
        return imageWidth;
    }

    public void setElementType(String elementType)
    {
        this.elementType = elementType;
    }

    public void setTextString(String textString)
    {
        this.textString = textString;
    }

    public void setPageNumber(String pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public void setTextColor(String textColor)
    {
        this.textColor = textColor;
    }

    public void setTextSize(String textSize)
    {
        this.textSize = textSize;
    }

    public void setxPosition(String xPosition)
    {
        this.xPosition = xPosition;
    }

    public void setyPosition(String yPosition)
    {
        this.yPosition = yPosition;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public void setImageHeight(String imageHeight)
    {
        this.imageHeight = imageHeight;
    }

    public void setImageWidth(String imageWidth)
    {
        this.imageWidth = imageWidth;
    }
}
