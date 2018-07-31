package com.trevorjd.rwplugin;

public class DocumentElement
{
    private String elementType;
    private String textString;
    private String xPosition;
    private String yPosition;
    private String textColor;
    private String textSize;
    // private String pageNumber;
    private String fileName;
    private String imageWidth;
    private String imageHeight;
    private String alignment;
    private String indent;
    private String docTitle;
    private String imageFrame;
    private String wrapText;
    private String pivot;
    private String pageIndex;
    private boolean rightSide;

    public DocumentElement()
    {
    }

    // setters
    public void setElementType(String elementType)
    {
        this.elementType = elementType;
    }

    public void setTextString(String textString)
    {
        this.textString = textString;
    }

/*    public void setPageNumber(String pageNumber)
    {
        this.pageNumber = pageNumber;
    }*/

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

    public void setAlignment(String alignment) { this.alignment = alignment; }

    public void setImageHeight(String imageHeight)
    {
        this.imageHeight = imageHeight;
    }

    public void setImageWidth(String imageWidth)
    {
        this.imageWidth = imageWidth;
    }

    public void setIndent(String indent)
    {
        this.indent = indent;
    }

    public void setDocTitle(String docTitle)
    {
        this.docTitle = docTitle;
    }

    public void setImageFrame(String imageFrame)
    {
        this.imageFrame = imageFrame;
    }

    public void setWrapText(String wrapText)
    {
        this.wrapText = wrapText;
    }

    public void setPivot(String pivot)
    {
        this.pivot = pivot;
}

    public void setPageIndex(String pageIndex)
    {
        this.pageIndex = pageIndex;
    }

    public void setRightSide(boolean rightSide)
    {
        this.rightSide = rightSide;
    }

    // getters

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

    /*public String getPageNumber() { return pageNumber; }*/

    public String getFileName()
    {
        return fileName;
    }

    public String getAlignment() {return alignment; }

    public String getImageHeight()
    {
        return imageHeight;
    }

    public String getImageWidth()
    {
        return imageWidth;
    }

    public String getIndent() { return indent; }

    public String getDocTitle()
    {
        return docTitle;
    }

    public String getImageFrame()
    {
        return imageFrame;
    }

    public String getWrapText()
    {
        return wrapText;
    }

    public String getPivot()
    {
        return pivot;
    }

    public String getPageIndex()
    {
        return pageIndex;
    }

    public boolean isRightSide()
    {
        return rightSide;
    }
}
