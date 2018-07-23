package com.trevorjd.rwplugin;

import net.risingworld.api.gui.*;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.ImageInformation;

import java.time.temporal.ValueRange;
import java.util.*;

import static com.trevorjd.rwplugin.RwdocLibrary.getTitleList;
import static com.trevorjd.rwplugin.rwdoc.*;
import static com.trevorjd.rwplugin.rwdocUtils.wordWrap;

public class rwdocGUI
{
    private static Float padding = 0.01f;

    /*
    give an array of document elements
    for each element:
    - create a corresponding gui element
    - calculate the vertical height (position of element above + getHeight()
    - add as a child of the relevant panel

    Player attributes:
    - mainPanel (full size of gui)
    - bgImage (journal.png overlay) - parent of left/right sides
    - pageLeftPanel
    - pageRightPanel
    - array (pageLeftElements)
    - array (pageRightElements)

    Refresh GUI
    - iterate *current* pageLeft and pageRight elements, removing each element from player's GUI
    - get new arrays of left and right elements
    - add them
    - make it visible
     */

    protected static void setupMainGUI(Player player)
    {
        // prepare main GUI window and sections
        GuiPanel mainPanel = new GuiPanel(0.5f, 0.5f, true, 1080, 720, false);
        mainPanel.setPivot(PivotPosition.Center);
        mainPanel.setColor(0x00000000);
        if (EDITOR)
        {
            mainPanel.setBorderColor(0xFFFFFFFF); //white
            mainPanel.setBorderThickness(1f, false);
        } else { mainPanel.setBorderColor(0x00000000); } //invisible

        ImageInformation imageinfo = new ImageInformation(plugin.getPath() + IMGDIR + "bgImage.png");
        GuiImage bgImage = new GuiImage(imageinfo,0f,0f,true,1080, 720, false);

        // child of bgImage
        GuiPanel pageLeftPanel = new GuiPanel(0.07f,0.07f,true, 0.4f, 0.9f, true);
        pageLeftPanel.setColor(0x00000000);
        if (EDITOR)
        {
            pageLeftPanel.setBorderColor(0xFF0000FF); //red
            pageLeftPanel.setBorderThickness(1f, false);
        } else { pageLeftPanel.setBorderColor(0x000000FF); }//invisible

        // child of bgImage
        GuiPanel pageRightPanel = new GuiPanel(0.53f,0.07f,true, 0.4f, 0.9f, true);
        pageRightPanel.setColor(0x00000000);
        if (EDITOR)
        {
            pageRightPanel.setBorderColor(0x00FF00FF); //green
            pageRightPanel.setBorderThickness(1f, false);
        } else { pageRightPanel.setBorderColor(0x00000000); } //invisible

        mainPanel.addChild(bgImage);
        bgImage.addChild(pageLeftPanel);
        bgImage.addChild(pageRightPanel);

        mainPanel.setVisible(false);
        bgImage.setVisible(false);
        pageLeftPanel.setVisible(false);
        pageRightPanel.setVisible(false);

        player.setAttribute("rwdoc_mainPanel", mainPanel);
        player.setAttribute("rwdoc_bgImage", bgImage);
        player.setAttribute("rwdoc_pageLeftPanel", pageLeftPanel);
        player.setAttribute("rwdoc_pageRightPanel", pageRightPanel);

        player.addGuiElement((GuiPanel) player.getAttribute("rwdoc_mainPanel"));
        player.addGuiElement((GuiImage) player.getAttribute("rwdoc_bgImage"));
        player.addGuiElement((GuiPanel) player.getAttribute("rwdoc_pageLeftPanel"));
        player.addGuiElement((GuiPanel) player.getAttribute("rwdoc_pageRightPanel"));
    }

    public static void addnewbits(Player player)
    {

        //player.setAttribute("rwdoc_PageLeft", 0);
        //player.setAttribute("rwdoc_PageRight", 1);

        // build front page menu (left = menu items;  right = welcome message / graphic)

        //  create list of elements, add to player
        /*ArrayList<String> titleList = getTitleList();
        player.setAttribute("titleList", titleList);
        GuiPanel p_pageLeftPanel = (GuiPanel) player.getAttribute("rwdoc_pageLeftPanel");
        Float current_Y_pos = 0.9f;
        for (int count = 0; count < titleList.size(); count++)
        {
            String labelName = "rwd_t_" + titleList.get(count);
            GuiLabel label = createHeading(labelName);
            p_pageLeftPanel.addChild(label);
            label.setPosition(0.5f, current_Y_pos, true);
            current_Y_pos = current_Y_pos - calcHeight(label);
            player.setAttribute(labelName, label);
        }

        //  retrieve list of elements, add to player GUI
        titleList = (ArrayList<String>) player.getAttribute("titleList");
        for (int count = 0; count < titleList.size(); count++)
        {
            String currentElement = titleList.get(count);
            player.addGuiElement((GuiLabel) player.getAttribute(currentElement));
        }*/
    }

    private static String cleanText(String inputString)
    {
        String newline = System.getProperty("line.separator");
        String pass1 = inputString.replace(newline, "");
        String pass2 = pass1.replace("\t", "");
        String pass3 = pass2.replace("\n", "");
        String trimmed = pass3.trim();
        return trimmed;
    }

    public static void buildPage(GuiPanel panel, Player player, RwdocDocument document, int pageNumber)
    {
        System.out.println("rwdoc Debug: document - " + document.getDocumentTitle() + " page - " + pageNumber);
        DocumentPage page = document.getPagebyIndex(pageNumber);
        ArrayList<DocumentElement> pageElements = page.getElementList();
        Float current_Y_pos = 1.0f;

        for (int count = 0; count < pageElements.size(); count++)
        {
            String newline = System.getProperty("line.separator");
            DocumentElement element = pageElements.get(count);
            String elementType = element.getElementType();
            if (elementType.equals("title"))
            {
                GuiLabel label = new GuiLabel(cleanText(element.getTextString()), 0f, current_Y_pos, true);
                GuiLabel attribLabel = addLabelAttributes(element, label);
                if(label.getPositionY() == attribLabel.getPositionY())
                {
                    current_Y_pos = current_Y_pos - calcHeight(attribLabel) - padding;
                    attribLabel.setPosition(attribLabel.getPositionX(), current_Y_pos, true);
                }

                panel.addChild(attribLabel);
                player.addGuiElement(attribLabel);
                attribLabel.setVisible(true);
            }
            if (elementType.equals("menuitem"))
            {
                GuiLabel label = new GuiLabel(wordWrap(cleanText(element.getTextString()), 30), 0f, current_Y_pos, true);
                GuiLabel attribLabel = addLabelAttributes(element, label);
                if(label.getPositionY() == attribLabel.getPositionY())
                {
                    current_Y_pos = current_Y_pos - calcHeight(attribLabel) - padding;
                    attribLabel.setPosition(attribLabel.getPositionX(), current_Y_pos, true);
                }
                panel.addChild(attribLabel);
                player.addGuiElement(attribLabel);
                attribLabel.setVisible(true);
            }
            if (elementType.equals("headline"))
            {
                GuiLabel label = new GuiLabel(wordWrap(cleanText(element.getTextString()), 30), 0f, current_Y_pos, true);
                GuiLabel attribLabel = addLabelAttributes(element, label);
                if(label.getPositionY() == attribLabel.getPositionY())
                {
                    current_Y_pos = current_Y_pos - calcHeight(attribLabel) - padding;
                    attribLabel.setPosition(attribLabel.getPositionX(), current_Y_pos, true);
                }
                panel.addChild(attribLabel);
                player.addGuiElement(attribLabel);
                attribLabel.setVisible(true);
                System.out.println("rwdoc_debug: Headline YPos = " + attribLabel.getPositionY());
            }
            if (elementType.equals("text"))
            {
                // System.out.println("rwdoc_debug pre-clean: >>"+element.getTextString()+"<<");
                String clean = cleanText(element.getTextString());
                String wrapped = wordWrap(clean, 20);

                // System.out.println("rwdoc_debug clean: >>"+clean+"<<");
                // System.out.println("rwdoc_debug wrapped: >>"+wrapped.replace("\n", "\\n")+"<<");
                GuiLabel label = new GuiLabel(wrapped, 0f, current_Y_pos, true);
                System.out.println("rwdoc_debug got element");
                GuiLabel attribLabel = addLabelAttributes(element, label);
                System.out.println("rwdoc_debug attribted element");
                if(label.getPositionY() == attribLabel.getPositionY())
                {
                    current_Y_pos = current_Y_pos - calcHeight(attribLabel) - padding;
                    attribLabel.setPosition(attribLabel.getPositionX(), current_Y_pos, true);
                }
                System.out.println("rwdoc_debug calced height");
                panel.addChild(attribLabel);
                System.out.println("rwdoc_debug added element");
                player.addGuiElement(attribLabel);
                System.out.println("rwdoc_debug added2 element");

                attribLabel.setVisible(true);
                System.out.println("rwdoc_debug: Text YPos = " + attribLabel.getPositionY());
            }
            if (elementType.equals("image"))
            {
                System.out.println("rwdoc_debug IMGPATHBUFFER = >" + IMGPATHBUFFER + "<");
                System.out.println("rwdoc_debug element.getFileName() = >" + element.getFileName() + "<");
                String imagepath = IMGPATHBUFFER + IMGDIR +element.getFileName();
                System.out.println("redoc_debug_imagepath=" + imagepath);
                ImageInformation imageinfo = new ImageInformation(imagepath);
                GuiImage image = new GuiImage(imageinfo, 0.5f - (Float.parseFloat(element.getImageWidth()) /2), current_Y_pos, true, Float.parseFloat(element.getImageWidth()), Float.parseFloat(element.getImageHeight()), true);
                GuiImage attribImage = addImageAttributes(element, image);
                if(image.getPositionY() == attribImage.getPositionY())
                {
                    current_Y_pos = current_Y_pos - attribImage.getHeight() - padding;
                    attribImage.setPosition(attribImage.getPositionX(), current_Y_pos, true);
                }
                panel.addChild(attribImage);
                player.addGuiElement(attribImage);
                attribImage.setVisible(true);
            }
        }
    }

    public static GuiLabel addLabelAttributes(DocumentElement edoc, GuiLabel egui)
    {
        if (null != edoc.getTextSize())
        {
            egui.setFontSize(Integer.parseInt(edoc.getTextSize()));
        } else
            {
            if(edoc.getElementType().equals("headline")) { egui.setFontSize(30);}
            if(edoc.getElementType().equals("text")) { egui.setFontSize(20);}
            }

        if (null != edoc.getTextColor())
        {
            egui.setFontColor((int) Long.parseLong(edoc.getTextSize()));
        }else { egui.setFontColor(FONTCOLOR); }

        if (null != edoc.getxPosition() && null != edoc.getyPosition())
        {
            egui.setPosition(Float.parseFloat(edoc.getxPosition()), Float.parseFloat(edoc.getyPosition()), true);
        }

        if (EDITOR)
        {
            egui.setBorderColor(0x0000FFFF);
            egui.setBorderThickness(1f, false);
        }

        return egui;
    }

    public static GuiImage addImageAttributes(DocumentElement edoc, GuiImage eimage)
    {
        if (null != edoc.getxPosition() && null != edoc.getyPosition())
        {
            eimage.setPosition(Float.parseFloat(edoc.getxPosition()), Float.parseFloat(edoc.getyPosition()), true);
        }
        return eimage;
    }

    public static void showTestGui(Player player)
    {
        // prepare example left page
        GuiLabel label1 = createHeading("Welcome to Trevoria");
        label1.setFontSize(40);
        label1.setBorderColor(0xFF0000FF); //red
        label1.setBorderThickness(1f, false);
        label1.setPosition(0.5f, 1.0f - calcHeight(label1), true);
        label1.setPivot(PivotPosition.CenterBottom);

        GuiImage image = createTitleImage();
        image.setBorderColor(0x00FF00FF); //green
        image.setBorderThickness(1f, false);
        image.setPosition(0.5f - image.getWidth() / 2, label1.getPositionY() - image.getHeight(), true);

        GuiLabel label2 = createHeading("");
        label2.setFontSize(40);
        label2.setText(wordWrap("0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9", label2.getFontSize()));
        label2.setBorderColor(0x0000FFFF); //blue
        label2.setBorderThickness(1f, false);
        label2.setPosition(padding, image.getPositionY() - calcHeight(label2) - padding, true);

        GuiLabel label3 = createHeading("");
        label3.setFontSize(10);
        label3.setText(wordWrap("0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9", label3.getFontSize()));
        label3.setBorderColor(0xFF0000FF); //red
        label3.setBorderThickness(1f, false);
        label3.setPosition(padding, label2.getPositionY() - calcHeight(label3) - padding, true);

        GuiLabel label4 = createHeading("");
        label4.setFontSize(20);
        label4.setText(wordWrap("0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9",label4.getFontSize()));
        label4.setBorderColor(0x00FF00FF); //green
        label4.setBorderThickness(1f, false);
        label4.setPosition(padding, label3.getPositionY() - calcHeight(label4) - padding, true);

        //prepare example right page
        GuiLabel label5 = createHeading("");
        label5.setFontSize(15);
        label5.setText(wordWrap("0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9",label5.getFontSize()));
        label5.setBorderColor(0x00FF00FF); //green
        label5.setBorderThickness(1f, false);
        label5.setPosition(padding, 1.0f - calcHeight(label4) - padding, true);

        GuiLabel label6 = createHeading("");
        label6.setFontSize(30);
        label6.setText(wordWrap("0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9",label6.getFontSize()));
        label6.setBorderColor(0x00FF00FF); //green
        label6.setBorderThickness(1f, false);
        label6.setPosition(padding, label5.getPositionY() - calcHeight(label6) - padding, true);

        GuiLabel label7 = createHeading("");
        label7.setFontSize(35);
        label7.setText(wordWrap("0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9",label7.getFontSize()));
        label7.setBorderColor(0x00FF00FF); //green
        label7.setBorderThickness(1f, false);
        label7.setPosition(padding, label6.getPositionY() - calcHeight(label7) - padding, true);

        GuiLabel label8 = createHeading("");
        label8.setFontSize(45);
        label8.setText(wordWrap("0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9",label8.getFontSize()));
        label8.setBorderColor(0x00FF00FF); //green
        label8.setBorderThickness(1f, false);
        label8.setPosition(padding, label7.getPositionY() - calcHeight(label8) - padding, true);

        GuiLabel label9 = createHeading("");
        label9.setFontSize(50);
        label9.setText(wordWrap("0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9",label9.getFontSize()));
        label9.setBorderColor(0x00FF00FF); //green
        label9.setBorderThickness(1f, false);
        label9.setPosition(padding, label4.getPositionY() - calcHeight(label9) - padding, true);

/*        pageLeftPanel.addChild(label1);
        pageLeftPanel.addChild(image);
        pageLeftPanel.addChild(label2);
        pageLeftPanel.addChild(label3);
        pageLeftPanel.addChild(label4);
        pageLeftPanel.addChild(label9);
        pageRightPanel.addChild(label5);
        pageRightPanel.addChild(label6);
        pageRightPanel.addChild(label7);
        pageRightPanel.addChild(label8);


        label1.setVisible(false);
        image.setVisible(false);
        label2.setVisible(false);
        label3.setVisible(false);
        label4.setVisible(false);
        label5.setVisible(false);
        label6.setVisible(false);
        label7.setVisible(false);
        label8.setVisible(false);
        label9.setVisible(false);

        player.setAttribute("label1", label1);
        player.setAttribute("label2", label2);
        player.setAttribute("label3", label3);
        player.setAttribute("label4", label4);
        player.setAttribute("label5", label5);
        player.setAttribute("label6", label6);
        player.setAttribute("label7", label7);
        player.setAttribute("label8", label8);
        player.setAttribute("label9", label9);
        player.setAttribute("image", image);

        player.addGuiElement((GuiLabel) player.getAttribute("label1"));
        player.addGuiElement((GuiLabel) player.getAttribute("label2"));
        player.addGuiElement((GuiLabel) player.getAttribute("label3"));
        player.addGuiElement((GuiLabel) player.getAttribute("label4"));
        player.addGuiElement((GuiLabel) player.getAttribute("label5"));
        player.addGuiElement((GuiLabel) player.getAttribute("label6"));
        player.addGuiElement((GuiLabel) player.getAttribute("label7"));
        player.addGuiElement((GuiLabel) player.getAttribute("label8"));
        player.addGuiElement((GuiLabel) player.getAttribute("label9"));
        player.addGuiElement((GuiImage) player.getAttribute("image"));*/
    }

    public static float calcHeight(GuiLabel label)
    {
        String sep = System.getProperty("line.separator");
        float result = 0f;
        String input = label.getText();
        int count = input.split(sep,-1).length-1 + 1;
        System.out.println("rwdoc_debug: found " + count + " newlines.");
        result = (float) count * label.getFontSize() / 675;
        return result;
    }

    public static void setVisibility(Player player, boolean value)
    {
        GuiPanel mainPanel = (GuiPanel) player.getAttribute("rwdoc_mainPanel");
        GuiImage bgImage = (GuiImage) player.getAttribute("rwdoc_bgImage");
        GuiPanel pageLeftPanel = (GuiPanel) player.getAttribute("rwdoc_pageLeftPanel");
        GuiPanel pageRightPanel = (GuiPanel) player.getAttribute("rwdoc_pageRightPanel");
        mainPanel.setVisible(value);
        bgImage.setVisible(value);
        pageLeftPanel.setVisible(value);
        pageRightPanel.setVisible(value);

        //ArrayList leftPage = player.getAttribute("rwdoc_leftPage");
        //ArrayList rightPage = player.getAttribute("rwdoc_rightPage");
    }

    private static GuiLabel createHeading(String s){
        GuiLabel element = new GuiLabel(
                s,
                0.5f,
                0.92f,
                true);
        element.setFontSize(45);
        element.setFontColor(0x302013FF);
        element.setColor(0x00000000);
        element.setBorderColor(0xFFFFFFFF);
        element.setBorderThickness(1f, false);
        element.setVisible(false);
        return element;
    }

    private static GuiImage createTitleImage(){
        ImageInformation ii = new ImageInformation(c.getProperty("rwdocImgLoc") + "trevoria.png");
        GuiImage element = new GuiImage(ii,
                0.5f,
                0.00f,
                true,
                0.5f,
                0.15f,
                true);
        element.setBorderColor(0xFFFFFFFF);
        element.setBorderThickness(1f, false);
        element.setVisible(false);
        element.setClickable(true);
        return element;
    }
}
