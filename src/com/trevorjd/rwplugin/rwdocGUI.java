package com.trevorjd.rwplugin;

import net.risingworld.api.gui.*;
import net.risingworld.api.gui.Font;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.ImageInformation;

import java.io.File;
import java.util.*;

import static com.trevorjd.rwplugin.MenuItemHandler.assignToPlayer;
import static com.trevorjd.rwplugin.rwdoc.*;
import static com.trevorjd.rwplugin.rwdocUtils.cmpF;
import static com.trevorjd.rwplugin.rwdocUtils.rwdebug;
import static com.trevorjd.rwplugin.rwdocUtils.wordWrap;

public class rwdocGUI
{
    private static Float padding = 0.02f;
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
        GuiPanel pageLeftPanel = new GuiPanel(0.07f, 0.07f, true, 0.4f, 0.88f, true);
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
        GuiPanel pageRightPanel = new GuiPanel(0.53f, 0.07f, true, 0.4f, 0.88f, true);
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
        GuiLabel titleLeft = new GuiLabel(DEFAULT_TITLE, 0.27f, 0.95f, true);
        titleLeft.setFontSize(16);
        titleLeft.setFontColor(FONTCOLOR);
        titleLeft.setPivot(PivotPosition.CenterBottom);

        GuiLabel titleRight = new GuiLabel(DEFAULT_TITLE, 0.75f, 0.95f, true);
        titleRight.setFontSize(16);
        titleRight.setFontColor(FONTCOLOR);
        titleRight.setPivot(PivotPosition.CenterBottom);

        GuiImage button_close = new GuiImage(HITBOXIMAGE, 0.96f, 0.94f, true, 38, 38, false);
        button_close.setClickable(true);
        GuiImage button_left = new GuiImage(HITBOXIMAGE, 0.0f, 0.05f, true, 64, 38, false);
        button_left.setClickable(true);
        GuiImage button_right = new GuiImage(HITBOXIMAGE, 0.93f, 0.05f, true, 64, 38, false);
        button_right.setClickable(true);
        GuiImage button_up = new GuiImage(HITBOXIMAGE, 0.495f, 0.05f, true, 38, 64, false);
        button_up.setClickable(true);

        GuiLabel pageNumLabelLeft = new GuiLabel("", 0.1f, 0.05f, true);
        pageNumLabelLeft.setFontSize(60);
        pageNumLabelLeft.setFontColor(FONTCOLOR);
        pageNumLabelLeft.setPivot(PivotPosition.CenterBottom);
        pageNumLabelLeft.setFont(Font.Handdrawn);

        GuiLabel pageNumLabelRight = new GuiLabel("", 0.90f, 0.05f, true);
        pageNumLabelRight.setFontSize(60);
        pageNumLabelRight.setFontColor(FONTCOLOR);
        pageNumLabelRight.setPivot(PivotPosition.CenterBottom);
        pageNumLabelRight.setFont(Font.Handdrawn);

        mainPanel.addChild(bgImage);
        bgImage.addChild(pageLeftPanel);
        bgImage.addChild(pageRightPanel);
        bgImage.addChild(button_close);
        bgImage.addChild(button_left);
        bgImage.addChild(button_right);
        bgImage.addChild(button_up);
        bgImage.addChild(titleLeft);
        bgImage.addChild(titleRight);
        bgImage.addChild(pageNumLabelLeft);
        bgImage.addChild(pageNumLabelRight);

        mainPanel.setVisible(false);
        bgImage.setVisible(false);
        pageLeftPanel.setVisible(false);
        pageRightPanel.setVisible(false);
        button_close.setVisible(false);
        button_left.setVisible(false);
        button_right.setVisible(false);
        button_up.setVisible(false);
        titleLeft.setVisible(false);
        titleRight.setVisible(false);
        pageNumLabelLeft.setVisible(false);
        pageNumLabelRight.setVisible(false);

        player.setAttribute("rwdoc_mainPanel", mainPanel);
        player.setAttribute("rwdoc_bgImage", bgImage);
        player.setAttribute("rwdoc_pageLeftPanel", pageLeftPanel);
        player.setAttribute("rwdoc_pageRightPanel", pageRightPanel);
        player.setAttribute("rwdoc_button_close", button_close);
        player.setAttribute("rwdoc_button_left", button_left);
        player.setAttribute("rwdoc_button_right", button_right);
        player.setAttribute("rwdoc_button_up", button_up);
        player.setAttribute("rwdoc_titleLeft", titleLeft);
        player.setAttribute("rwdoc_titleRight", titleRight);
        player.setAttribute("rwdoc_pageNumLabelLeft", pageNumLabelLeft);
        player.setAttribute("rwdoc_pageNumLabelRight", pageNumLabelRight);
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
        player.addGuiElement((GuiLabel) player.getAttribute("rwdoc_titleLeft"));
        player.addGuiElement((GuiLabel) player.getAttribute("rwdoc_titleRight"));
        player.addGuiElement((GuiLabel) player.getAttribute("rwdoc_pageNumLabelLeft"));
        player.addGuiElement((GuiLabel) player.getAttribute("rwdoc_pageNumLabelRight"));
    }


    private static String cleanText(String inputString)
    {
        String cleaned = "";
        if(null != inputString)
        {
            //rwdebug(3, "inputString: " + inputString);
            String pass1 = inputString.trim(); //remove leading and trailing spaces
            String pass2 = pass1.replace("\t", " ");
            cleaned = pass2.trim().replaceAll(" +", " ");
        } else rwdebug(2, "cleanText received a null value! This shouldn't happen.");
        //rwdebug(3, "outputString: " + cleaned);
        return cleaned;
    }


    public static void buildPage(GuiPanel panel, Player player, RwdocDocument document, int pageNumber)
    {
        boolean firstItem = true;
        // if the right side page is empty, create a blank one.
        DocumentPage page;
        if(pageNumber > (document.getNumberofPages()-1))
        {
            page = new DocumentPage();
            DocumentElement elementToInsert = new DocumentElement();
            elementToInsert.setElementType("text");
            elementToInsert.setTextString(c.getProperty("msg_no_content"));
            elementToInsert.setyPosition("0.5");
            elementToInsert.setAlignment("center");
            page.addElement(elementToInsert);
        } else { page = document.getPagebyNumber(pageNumber); }

        GuiLabel pageNumLabelLeft = (GuiLabel) player.getAttribute("rwdoc_pageNumLabelLeft");
        GuiLabel pageNumLabelRight = (GuiLabel) player.getAttribute("rwdoc_pageNumLabelRight");
        int pageNum = page.getPageNumber();
        if(pageNum % 2 == 0)
        {
            pageNumLabelLeft.setText(String.valueOf(pageNum));
        } else {
            // in the case that we're inserting a dummy page on the right, don't print a pagenum
            if(pageNum == -1)
            {
                pageNumLabelRight.setText("");
            } else { pageNumLabelRight.setText(String.valueOf(pageNum)); }
        }


        // insert a doc title element if the page has none
        DocumentElement titleToInsert = new DocumentElement();
        titleToInsert.setTextString(document.getDocumentTitle());
        titleToInsert.setElementType("title");
        page.insertTitle(titleToInsert);
        //Array to hold list of GUI elements to be added
        ArrayList<DocumentElement> pageElements = page.getElementList();
        // Float used to set vertical alignment of each new element
        Float current_Y_pos = 1.0f;
        // Array to track gui elements added to the player's HUD. Used in Utils to clear the HUD.
        ArrayList<GuiElement> guiItems = new ArrayList<GuiElement>();
        // Track auto-generated menu items; used in Listener for document/page management
        MenuItemHandler mih = (MenuItemHandler) player.getAttribute("rwdoc_mih");
        // RwdocDocuments contain an array of pages.
        // each DocumentPage has an array of DocumentElements.
        // loop through elements list, creating GUIElements as specified
        // GUIElements are stored in pageElemnents for display AND guiItems for later removal
        for (int count = 0; count < pageElements.size(); count++)
        {
            DocumentElement element = pageElements.get(count);
            String elementType = element.getElementType();
            if (elementType != null) {
                if (elementType.equals("title"))
                {
                    // titles are a special case because they're not on the left/right panels
                    // don't process with attributeLabel(), adjust Y values, etc.
                    GuiLabel titleLeft = (GuiLabel) player.getAttribute("rwdoc_titleLeft");
                    GuiLabel titleRight = (GuiLabel) player.getAttribute("rwdoc_titleRight");
                    if (element.getTextString().equals("default"))
                    {
                        titleLeft.setText(DEFAULT_TITLE);
                        titleRight.setText(DEFAULT_TITLE);
                    } else
                    if (page.isLegacyPage())
                    {
                        //create a notification if the document is untitled
                        titleLeft.setText("This is a legacy journal.xml file.");
                        titleRight.setText("Please update to use rwDoc format.");
                    } else
                    {
                        titleLeft.setText(cleanText(element.getTextString()));
                        titleRight.setText(cleanText(element.getTextString()));
                    }

                }

                if (elementType.equals("menuitem"))
                {
                    if(element.getTextString() != null && !element.getTextString().isEmpty())
                    {
                        GuiLabel label = new GuiLabel("", 0f, current_Y_pos, true);
                        GuiLabel attribLabel = addLabelAttributes(element, label);
                        String cleanText = cleanText(element.getTextString());
                        if (element.getWrapText() != null && !Boolean.valueOf(element.getWrapText()))
                        {
                            attribLabel.setText(cleanText);
                        } else { attribLabel.setText(wordWrap(cleanText, attribLabel.getFontSize())); }

                        if (cmpF(current_Y_pos, attribLabel.getPositionY()))
                        {
                            current_Y_pos = current_Y_pos - calcHeight(attribLabel) - padding;
                            attribLabel.setPosition(attribLabel.getPositionX(), current_Y_pos, true);
                        }
                        if (MENUITEM_BULLETS)
                        {
                            // create new GuiImage for bullet; shift the label across
                            GuiImage imgBullet = new GuiImage(
                                    BULLET,
                                    attribLabel.getPositionX(),
                                    attribLabel.getPositionY() + calcHeight(attribLabel) / 2,
                                    true,
                                    32,
                                    32,
                                    false
                            );
                            attribLabel.setPosition(attribLabel.getPositionX() + 0.075f, attribLabel.getPositionY(), true);
                            imgBullet.setPivot(PivotPosition.CenterLeft);
                            panel.addChild(imgBullet);
                            player.addGuiElement(imgBullet);
                            imgBullet.setClickable(true);
                            imgBullet.setVisible(true);
                            guiItems.add(imgBullet);
                        }
                        panel.addChild(attribLabel);
                        player.addGuiElement(attribLabel);
                        attribLabel.setClickable(true);
                        attribLabel.setVisible(true);
                        guiItems.add(attribLabel);
                        // MIH is only needed for menu items
                        if(!element.getTextString().isEmpty())
                        {
                            if (document.getDocumentTitle().equals("default"))
                            {
                                mih.addMenuItem(attribLabel.getText(), "start", attribLabel);
                            } else
                            {
                                mih.addMenuItem(document.getDocumentTitle(), element.getPageIndex(), attribLabel);
                            }
                        }
                    }

                }
                if (elementType.equals("headline"))
                {
                    GuiLabel label = new GuiLabel("", 0f, current_Y_pos, true);
                    GuiLabel attribLabel = addLabelAttributes(element, label);
                    String cleanText = cleanText(element.getTextString());
                    if (element.getWrapText() != null && !Boolean.valueOf(element.getWrapText()))
                    {
                        attribLabel.setText(cleanText);
                    } else { attribLabel.setText(wordWrap(cleanText, attribLabel.getFontSize())); }
                    if (cmpF(current_Y_pos, attribLabel.getPositionY()))
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
                    GuiLabel label = new GuiLabel("", 0f, current_Y_pos, true);
                    GuiLabel attribLabel = addLabelAttributes(element, label);
                    String cleanText = cleanText(element.getTextString());
                    // if there are newlines in the text, consider it pre-formatted and do NOT wrap
                    if(cleanText.contains(System.getProperty("line.separator")) || cleanText.contains("\n"))
                    {
                        rwdebug(3, "Text contains newlines. Assuming pre-formatted text; turning off wrap.");
                        element.setWrapText("false");
                    }

                    if (element.getWrapText() != null && !Boolean.valueOf(element.getWrapText()))
                    {
                        attribLabel.setText(cleanText);
                    } else { attribLabel.setText(wordWrap(cleanText, attribLabel.getFontSize())); }
                    if (cmpF(current_Y_pos, attribLabel.getPositionY()))
                    {
                        current_Y_pos = current_Y_pos - calcHeight(attribLabel) - padding;
                        attribLabel.setPosition(attribLabel.getPositionX(), current_Y_pos, true);
                    }
                    panel.addChild(attribLabel);
                    player.addGuiElement(attribLabel);
                    guiItems.add(attribLabel);
                    attribLabel.setVisible(true);
                    // default journal sets a TopLeft pivot for text items; we must accommodate this
                    // XML parser sets isLegacyPage() = true if the elements contain page="left" or page="right"
                    if(page.isLegacyPage() && element.getPivot() == null)
                    {
                        attribLabel.setPivot(PivotPosition.TopLeft);
                    }
                }
                if (elementType.equals("image"))
                {
                    // clean the image filename in case someone tries to sneak in something nasty
                    File file = new File(element.getFileName());
                    String fileName = file.getName();
                    // provide some common graphics for authors to use (saving memory)
                    GuiImage image;
                    switch (fileName.toLowerCase())
                    {
                        case "hrule" :
                            image = new GuiImage(HRULE, 0.0f, current_Y_pos, true, Float.parseFloat(element.getImageWidth()), Float.parseFloat(element.getImageHeight()), true);
                            break;
                        case "vrule" :
                            image = new GuiImage(VRULE, 0.0f, current_Y_pos, true, Float.parseFloat(element.getImageWidth()), Float.parseFloat(element.getImageHeight()), true);
                            break;
                        case "tick" :
                            image = new GuiImage(TICK, 0.0f, current_Y_pos, true, Float.parseFloat(element.getImageWidth()), Float.parseFloat(element.getImageHeight()), true);
                            break;
                        case "cross" :
                            image = new GuiImage(CROSS, 0.0f, current_Y_pos, true, Float.parseFloat(element.getImageWidth()), Float.parseFloat(element.getImageHeight()), true);
                            break;
                        case "bullet" :
                            image = new GuiImage(BULLET, 0.0f, current_Y_pos, true, Float.parseFloat(element.getImageWidth()), Float.parseFloat(element.getImageHeight()), true);
                            break;
                        default:
                            String imagepath = document.getDocumentPath() + IMGDIR + fileName;
                            ImageInformation imageinfo = new ImageInformation(imagepath);
                            image = new GuiImage(imageinfo, 0.0f, current_Y_pos, true, Float.parseFloat(element.getImageWidth()), Float.parseFloat(element.getImageHeight()), true);
                    }
                    GuiImage attribImage = addImageAttributes(element, image);
                    if (cmpF(current_Y_pos, attribImage.getPositionY()))
                    {
                        current_Y_pos = current_Y_pos - attribImage.getHeight() - padding;
                        attribImage.setPosition(attribImage.getPositionX(), current_Y_pos, true);
                    }
                    if (element.getImageFrame() != null)
                    {
                        // frame needed. Create the frame; make it a little bigger
                        // dummy init that will be overwritten by the switch
                        GuiImage imageFrame = new GuiImage(IMAGE_FRAME_1, 0f, 0f, true, 0f, 0f,false);
                        switch (element.getImageFrame()) {
                            case "1" :
                                imageFrame = new GuiImage(IMAGE_FRAME_1, 0f, 0f, true, 0f, 0f,false);
                                break;
                            case "2" :
                                imageFrame = new GuiImage(IMAGE_FRAME_2, 0f, 0f, true, 0f, 0f,false);
                                break;
                            case "3" :
                                imageFrame = new GuiImage(IMAGE_FRAME_3, 0f, 0f, true, 0f, 0f,false);
                                break;
                            case "4" :
                                imageFrame = new GuiImage(IMAGE_FRAME_4, 0f, 0f, true, 0f, 0f,false);
                                break;
                        }
                        imageFrame.setPosition(attribImage.getPositionX(), attribImage.getPositionY(), true);
                        imageFrame.setSize(attribImage.getWidth(), attribImage.getHeight(), true);
                        panel.addChild(imageFrame);
                        player.addGuiElement(imageFrame);
                        guiItems.add(imageFrame);
                        imageFrame.setVisible(true);
                        // stick the image on top of the frame, resize it to fit the frame, then the usual stuff
                        imageFrame.addChild(attribImage);
                        attribImage.setSize(0.90f, 0.90f, true);
                        attribImage.setPosition(0.05f, 0.05f, true);
                        player.addGuiElement(attribImage);
                        guiItems.add(attribImage);
                        attribImage.setVisible(true);
                    } else
                    {
                        // no frame needed, business as usual
                        panel.addChild(attribImage);
                        player.addGuiElement(attribImage);
                        guiItems.add(attribImage);
                        attribImage.setVisible(true);
                    }

                }
            } else { rwdebug(3, "Element type is null. This is unusual."); }

        }
        player.setAttribute("rwdoc_gui_elements_" + panel.getID(), guiItems);
        assignToPlayer(player);

    }

    public static GuiLabel addLabelAttributes(DocumentElement edoc, GuiLabel egui)
    {
        if (null != edoc.getTextSize())
        {
            egui.setFontSize(Integer.parseInt(edoc.getTextSize()));
        } else
            {
                if(edoc.getElementType().equals("menuitem")) { egui.setFontSize(DEFAULT_MENUITEM_SIZE);}
                if(edoc.getElementType().equals("headline")) { egui.setFontSize(DEFAULT_HEADLINE_SIZE);}
                if(edoc.getElementType().equals("text")) { egui.setFontSize(DEFAULT_TEXT_SIZE);}
            }

        if (null != edoc.getTextColor())
        {
            if(edoc.getTextColor().substring(0,2).toLowerCase().equals("0x"))
            {

                if(edoc.getTextColor().length() == 8)
                {
                    int red = Integer.parseInt(edoc.getTextColor().substring(2,4), 16);
                    int green = Integer.parseInt(edoc.getTextColor().substring(4,6), 16);
                    int blue = Integer.parseInt(edoc.getTextColor().substring(6,8), 16);
                    egui.setFontColor((float) red/255, (float) green/255, (float) blue/255, 1f);
                } else { rwdebug(2, "Invalid color attribute (short!): " + edoc.getTextColor()); }
            } else { rwdebug(2, "Invalid color attribute (no 0x): " + edoc.getTextColor()); }
        } else
            {
                // no valid override; using default colour
                egui.setFontColor(FONTCOLOR); }

        if (null != edoc.getAlignment())
        {
            if(edoc.getAlignment().toLowerCase().equals("center"))
            {
                egui.setPosition(0.5f, egui.getPositionY(), true);
                egui.setPivot(PivotPosition.CenterBottom);
            }
        }

        if (null != edoc.getIndent())
        {
            float indent = Float.parseFloat(edoc.getIndent());
            egui.setPosition(0f + indent * 0.1f, egui.getPositionY(), true);
        }

        if (edoc.getxPosition() != null)
        {
            egui.setPosition(Float.parseFloat(edoc.getxPosition()), egui.getPositionY(), true);
        }

        if (edoc.getyPosition() != null)
        {
            egui.setPosition(egui.getPositionX(), Float.parseFloat(edoc.getyPosition()), true);
        }

        if (EDITOR)
        {
            egui.setBorderColor(0x0000FFFF);
            egui.setBorderThickness(1f, false);
        }

        if (null != edoc.getPivot())
        {
            switch (edoc.getPivot().toLowerCase()) {
                case "topleft" :
                    egui.setPivot(PivotPosition.TopLeft);
                    break;
                case "centertop" :
                    egui.setPivot(PivotPosition.CenterTop);
                    break;
                case "topright" :
                    egui.setPivot(PivotPosition.TopRight);
                    break;
                case "centerleft" :
                    egui.setPivot(PivotPosition.CenterLeft);
                    break;
                case "center" :
                    egui.setPivot(PivotPosition.Center);
                    break;
                case "centerright" :
                    egui.setPivot(PivotPosition.CenterRight);
                    break;
                case "bottomleft" :
                    egui.setPivot(PivotPosition.BottomLeft);
                    break;
                case "centerbottom" :
                    egui.setPivot(PivotPosition.CenterBottom);
                    break;
                case "bottomright" :
                    egui.setPivot(PivotPosition.BottomRight);
                    break;
            }
        }

        return egui;
    }

    public static GuiImage addImageAttributes(DocumentElement edoc, GuiImage eimage)
    {
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

        if (null != edoc.getIndent())
        {
            float indent = Float.parseFloat(edoc.getIndent());
            eimage.setPosition(0f + indent * 0.1f, eimage.getPositionY(), true);
        }

        if (edoc.getxPosition() != null)
        {
            eimage.setPosition(Float.parseFloat(edoc.getxPosition()), eimage.getPositionY(), true);
        }

        if (edoc.getyPosition() != null)
        {
            eimage.setPosition(eimage.getPositionX(), Float.parseFloat(edoc.getyPosition()), true);
        }

        if (null != edoc.getPivot())
        {
            switch (edoc.getPivot().toLowerCase()) {
                case "topleft" :
                    eimage.setPivot(PivotPosition.TopLeft);
                    break;
                case "centertop" :
                    eimage.setPivot(PivotPosition.CenterTop);
                    break;
                case "topright" :
                    eimage.setPivot(PivotPosition.TopRight);
                    break;
                case "centerleft" :
                    eimage.setPivot(PivotPosition.CenterLeft);
                    break;
                case "center" :
                    eimage.setPivot(PivotPosition.Center);
                    break;
                case "centerright" :
                    eimage.setPivot(PivotPosition.CenterRight);
                    break;
                case "bottomleft" :
                    eimage.setPivot(PivotPosition.BottomLeft);
                    break;
                case "centerbottom" :
                    eimage.setPivot(PivotPosition.CenterBottom);
                    break;
                case "bottomright" :
                    eimage.setPivot(PivotPosition.BottomRight);
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
        if ( count == 1)
        {
            count = input.split("\n",-1).length-1 + 1;
        }

        rwdebug(4,"calcHeight found " + count + " newlines in " + label.getText());
        result = (float) count * label.getFontSize() / 660;
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
        GuiLabel titleLeft = (GuiLabel) player.getAttribute("rwdoc_titleLeft");
        GuiLabel titleRight = (GuiLabel) player.getAttribute("rwdoc_titleRight");
        GuiLabel pageNumLabelLeft = (GuiLabel) player.getAttribute("rwdoc_pageNumLabelLeft");
        GuiLabel pageNumLabelRight = (GuiLabel) player.getAttribute("rwdoc_pageNumLabelRight");
        mainPanel.setVisible(value);
        bgImage.setVisible(value);
        pageLeftPanel.setVisible(value);
        pageRightPanel.setVisible(value);
        button_close.setVisible(value);
        button_up.setVisible(value);
        button_left.setVisible(value);
        button_right.setVisible(value);
        titleLeft.setVisible(value);
        titleRight.setVisible(value);
        pageNumLabelLeft.setVisible(value);
        pageNumLabelRight.setVisible(value);
    }

}
