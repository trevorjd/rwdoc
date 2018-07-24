package com.trevorjd.rwplugin;

import net.risingworld.api.gui.*;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.ImageInformation;

import java.util.*;

import static com.trevorjd.rwplugin.rwdoc.*;
import static com.trevorjd.rwplugin.rwdocUtils.rwdebug;
import static com.trevorjd.rwplugin.rwdocUtils.wordWrap;

public class rwdocGUI
{
    private static Float padding = 0.005f;
    private static int pageNumBuffer = 0;  // used to buffer the page number from addLabelAttributes()
                            // to the Map since the GuiLabel element can't hold it

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
        } else
        {
            mainPanel.setBorderColor(0x00000000);
        } //invisible


        GuiImage bgImage = new GuiImage(BGIMAGE, 0f, 0f, true, 1080, 720, false);

        // child of bgImage
        GuiPanel pageLeftPanel = new GuiPanel(0.07f, 0.07f, true, 0.4f, 0.9f, true);
        pageLeftPanel.setColor(0x00000000);
        if (EDITOR)
        {
            pageLeftPanel.setBorderColor(0xFF0000FF); //red
            pageLeftPanel.setBorderThickness(1f, false);
        } else
        {
            pageLeftPanel.setBorderColor(0x000000FF);
        }//invisible

        // child of bgImage
        GuiPanel pageRightPanel = new GuiPanel(0.53f, 0.07f, true, 0.4f, 0.9f, true);
        pageRightPanel.setColor(0x00000000);
        if (EDITOR)
        {
            pageRightPanel.setBorderColor(0x00FF00FF); //green
            pageRightPanel.setBorderThickness(1f, false);
        } else
        {
            pageRightPanel.setBorderColor(0x00000000);
        } //invisible

        // child of bgImage

        GuiImage button_close = new GuiImage(HITBOXIMAGE, 0.96f, 0.94f, true, 38, 38, false);
        button_close.setClickable(true);
        GuiImage button_left = new GuiImage(HITBOXIMAGE, 0.0f, 0.05f, true, 64, 38, false);
        button_left.setClickable(true);
        GuiImage button_right = new GuiImage(HITBOXIMAGE, 0.95f, 0.05f, true, 64, 38, false);
        button_right.setClickable(true);
        GuiImage button_up = new GuiImage(HITBOXIMAGE, 0.495f, 0.05f, true, 38, 64, false);
        button_up.setClickable(true);

        mainPanel.addChild(bgImage);
        bgImage.addChild(pageLeftPanel);
        bgImage.addChild(pageRightPanel);
        bgImage.addChild(button_close);
        bgImage.addChild(button_left);
        bgImage.addChild(button_right);
        bgImage.addChild(button_up);

        mainPanel.setVisible(false);
        bgImage.setVisible(false);
        pageLeftPanel.setVisible(false);
        pageRightPanel.setVisible(false);
        button_close.setVisible(false);
        button_left.setVisible(false);
        button_right.setVisible(false);
        button_up.setVisible(false);

        player.setAttribute("rwdoc_mainPanel", mainPanel);
        player.setAttribute("rwdoc_bgImage", bgImage);
        player.setAttribute("rwdoc_pageLeftPanel", pageLeftPanel);
        player.setAttribute("rwdoc_pageRightPanel", pageRightPanel);
        player.setAttribute("rwdoc_button_close", button_close);
        player.setAttribute("rwdoc_button_left", button_left);
        player.setAttribute("rwdoc_button_right", button_right);
        player.setAttribute("rwdoc_button_up", button_up);
    }

    public static void addCommonGuiToPlayer(Player player)
    {
        player.addGuiElement((GuiPanel) player.getAttribute("rwdoc_mainPanel"));
        player.addGuiElement((GuiImage) player.getAttribute("rwdoc_bgImage"));
        player.addGuiElement((GuiPanel) player.getAttribute("rwdoc_pageLeftPanel"));
        player.addGuiElement((GuiPanel) player.getAttribute("rwdoc_pageRightPanel"));
        player.addGuiElement((GuiImage) player.getAttribute("rwdoc_button_close"));
        player.addGuiElement((GuiImage) player.getAttribute("rwdoc_button_left"));
        player.addGuiElement((GuiImage) player.getAttribute("rwdoc_button_right"));
        player.addGuiElement((GuiImage) player.getAttribute("rwdoc_button_up"));
    }

    private static String cleanText(String inputString)
    {
        String trimmed = "";
        if(null != inputString)
        {
            String pass1 = inputString.trim();
            String newline = System.getProperty("line.separator");
            // String pass1 = inputString.replace(newline, "");
            String pass2 = pass1.replace("\t", "");
            String pass3 = pass2.replace("\n", "");
            trimmed = pass2.trim();
        } else rwdebug(2, "cleanText received a null value!.");

        return trimmed;
    }

    public static void buildPage(GuiPanel panel, Player player, RwdocDocument document, int pageNumber)
    {
        rwdebug(3, "Building document: " +document.getDocumentTitle() + "; page: " + pageNumber + ".");
        DocumentPage page = document.getPagebyIndex(pageNumber);
        //Array to hold list of GUI elements to be added
        ArrayList<DocumentElement> pageElements = page.getElementList();
        Float current_Y_pos = 1.0f;
        // Array to track gui elements added to the player's HUD. Used in Utils to clear the HUD.
        ArrayList<GuiElement> guiItems = new ArrayList<GuiElement>();

        // loop through elements list, creating elements as needed
        for (int count = 0; count < pageElements.size(); count++)
        {
            String newline = System.getProperty("line.separator");
            DocumentElement element = pageElements.get(count);
            String elementType = element.getElementType();
            if (elementType.equals("title"))
            {
                if (!element.getTextString().equals("default"))
                {
                    rwdebug(4, "title element - not default doc");
                    GuiLabel label = new GuiLabel(cleanText(element.getTextString()), 0f, current_Y_pos, true);
                    GuiLabel attribLabel = addLabelAttributes(element, label);
                    if (label.getPositionY() == attribLabel.getPositionY())
                    {
                        current_Y_pos = current_Y_pos - calcHeight(attribLabel) - padding;
                        attribLabel.setPosition(attribLabel.getPositionX(), current_Y_pos, true);
                    }

                    panel.addChild(attribLabel);
                    player.addGuiElement(attribLabel);
                    guiItems.add(attribLabel);
                    attribLabel.setVisible(true);
                } else
                {
                    rwdebug(4, "title element - default doc - ignored");
                }
            }
            if (elementType.equals("menuitem"))
            {
                rwdebug(4, "found a menu item");
                // Map to track auto-generated menu items; used in Listener for document/page management
                Map<Integer, MenuElement> menuitems = (HashMap<Integer, MenuElement>) player.getAttribute("rwdoc_menu_elements");
                rwdebug(4, "got hashmap");
                GuiLabel label = new GuiLabel(wordWrap(cleanText(element.getTextString()), 30), 0f, current_Y_pos, true);
                GuiLabel attribLabel = addLabelAttributes(element, label);
                MenuItem menuItem = (MenuItem) attribLabel;
                if (null != element.getPageNumber()) { menuItem.setPageNum(Integer.parseInt(element.getPageNumber())); }
                if (null != element.getTextString()) { menuItem.setLinkDocTitle(element.getTextString()); }
                if (label.getPositionY() == attribLabel.getPositionY())
                {
                    current_Y_pos = current_Y_pos - calcHeight(attribLabel) - padding;
                    attribLabel.setPosition(attribLabel.getPositionX(), current_Y_pos, true);
                }
                panel.addChild(attribLabel);
                player.addGuiElement(attribLabel);
                rwdebug(4, "adding menuitem to hashmap");
                MenuElement menuElement = new MenuElement();
                menuElement.setTitle(attribLabel.getText());
                menuElement.setPageNum(pageNumBuffer);
                pageNumBuffer = 0;
                menuitems.put(attribLabel.getID(), menuElement);
                attribLabel.setClickable(true);
                guiItems.add(attribLabel);
                attribLabel.setVisible(true);
            }
            if (elementType.equals("headline"))
            {
                GuiLabel label = new GuiLabel(wordWrap(cleanText(element.getTextString()), 30), 0f, current_Y_pos, true);
                GuiLabel attribLabel = addLabelAttributes(element, label);
                if (label.getPositionY() == attribLabel.getPositionY())
                {
                    current_Y_pos = current_Y_pos - calcHeight(attribLabel) - padding;
                    attribLabel.setPosition(attribLabel.getPositionX(), current_Y_pos, true);
                }
                panel.addChild(attribLabel);
                player.addGuiElement(attribLabel);
                guiItems.add(attribLabel);
                attribLabel.setVisible(true);
            }
            if (elementType.equals("text"))
            {
                String clean = cleanText(element.getTextString());
                String wrapped = wordWrap(clean, 20);

                GuiLabel label = new GuiLabel(wrapped, 0f, current_Y_pos, true);
                GuiLabel attribLabel = addLabelAttributes(element, label);
                if (label.getPositionY() == attribLabel.getPositionY())
                {
                    current_Y_pos = current_Y_pos - calcHeight(attribLabel) - padding;
                    attribLabel.setPosition(attribLabel.getPositionX(), current_Y_pos, true);
                }
                panel.addChild(attribLabel);
                player.addGuiElement(attribLabel);
                guiItems.add(attribLabel);
                attribLabel.setVisible(true);
            }
            if (elementType.equals("image"))
            {
                rwdebug(4, "docpath = " + document.getDocumentPath());
                String imagepath = document.getDocumentPath() + IMGDIR + element.getFileName();
                ImageInformation imageinfo = new ImageInformation(imagepath);
                GuiImage image = new GuiImage(imageinfo, 0.0f, current_Y_pos, true, Float.parseFloat(element.getImageWidth()), Float.parseFloat(element.getImageHeight()), true);
                GuiImage attribImage = addImageAttributes(element, image);
                if (image.getPositionY() == attribImage.getPositionY())
                {
                    current_Y_pos = current_Y_pos - attribImage.getHeight() - padding;
                    attribImage.setPosition(attribImage.getPositionX(), current_Y_pos, true);
                }
                panel.addChild(attribImage);
                player.addGuiElement(attribImage);
                guiItems.add(attribImage);
                attribImage.setVisible(true);
            }
        }
        rwdebug(4, "playerGuiItem size = " + guiItems.size());
        for (GuiElement item : guiItems)
        {
            rwdebug(4, "guiItems contains item: " + item.getID());
        }
        rwdebug(3, "Page done. Attribname: " + "rwdoc_gui_elements_" + panel.getID());
        player.setAttribute("rwdoc_gui_elements_" + panel.getID(), guiItems);
    }

    public static GuiLabel addLabelAttributes(DocumentElement edoc, GuiLabel egui)
    {
        if (null != edoc.getTextSize())
        {
            egui.setFontSize(Integer.parseInt(edoc.getTextSize()));
        } else
            {
                if(edoc.getElementType().equals("menuitem")) { egui.setFontSize(40);}
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

        if (null != edoc.getTabstop())
        {
            float tabstop = Float.parseFloat(edoc.getTabstop());
            egui.setPosition(0f + tabstop * 0.1f, egui.getPositionY(), true);
        }

        if (null != edoc.getPageNumber())
        {
            int pageNumber = Integer.parseInt(edoc.getPageNumber());
            pageNumBuffer = pageNumber;
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
        if (null != edoc.getAlignment())
        {
            switch (edoc.getAlignment()){
                case "left" :
                    eimage.setPosition(0f, eimage.getPositionY(), true);
                    break;
                case "center" :
                    eimage.setPosition(0.5f - (eimage.getWidth()/2), eimage.getPositionY(), true);
                    break;
                case "right" :
                    eimage.setPosition(1.0f - eimage.getWidth(), eimage.getPositionY(), true);
                    break;
            }
        }
        return eimage;
    }

    public static float calcHeight(GuiLabel label)
    {
        String sep = System.getProperty("line.separator");
        float result = 0f;
        String input = label.getText();
        int count = input.split(sep,-1).length-1 + 1;
        rwdebug(4,"calcHeight found \" + count + \" newlines.\"");
        result = (float) count * label.getFontSize() / 675;
        return result;
    }

    public static void setVisibility(Player player, boolean value)
    {
        GuiPanel mainPanel = (GuiPanel) player.getAttribute("rwdoc_mainPanel");
        GuiImage bgImage = (GuiImage) player.getAttribute("rwdoc_bgImage");
        GuiPanel pageLeftPanel = (GuiPanel) player.getAttribute("rwdoc_pageLeftPanel");
        GuiPanel pageRightPanel = (GuiPanel) player.getAttribute("rwdoc_pageRightPanel");
        GuiImage button_close = (GuiImage) player.getAttribute("rwdoc_button_close");
        GuiImage button_up = (GuiImage) player.getAttribute("rwdoc_button_up");
        GuiImage button_left = (GuiImage) player.getAttribute("rwdoc_button_left");
        GuiImage button_right = (GuiImage) player.getAttribute("rwdoc_button_right");
        mainPanel.setVisible(value);
        bgImage.setVisible(value);
        pageLeftPanel.setVisible(value);
        pageRightPanel.setVisible(value);
        button_close.setVisible(value);
        button_up.setVisible(value);
        button_left.setVisible(value);
        button_right.setVisible(value);
    }

}
