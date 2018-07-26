package com.trevorjd.rwplugin;

import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerCommandEvent;
import net.risingworld.api.events.player.PlayerKeyEvent;
import net.risingworld.api.events.player.gui.PlayerGuiElementClickEvent;
import net.risingworld.api.gui.GuiElement;
import net.risingworld.api.gui.GuiImage;
import net.risingworld.api.gui.GuiLabel;
import net.risingworld.api.gui.GuiPanel;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.KeyInput;

import java.util.ArrayList;

import static java.lang.Math.max;
import static com.trevorjd.rwplugin.RwdocLibrary.buildLibrary;
import static com.trevorjd.rwplugin.rwdoc.REFRESH_CMD;
import static com.trevorjd.rwplugin.rwdoc.ACTIVATION_CMD;
import static com.trevorjd.rwplugin.rwdoc.RWDOC_LIBRARY;
import static com.trevorjd.rwplugin.rwdocGUI.buildPage;
import static com.trevorjd.rwplugin.rwdocGUI.setVisibility;
import static com.trevorjd.rwplugin.rwdocGUI.addCommonGuiToPlayer;
import static com.trevorjd.rwplugin.rwdocUtils.rwdebug;

public class rwdocCmdListener implements Listener
{
    @EventMethod
    public static void onPlayerCommand(PlayerCommandEvent event)
    {
        String[] cmd = event.getCommand().split(" ");
        Player player = event.getPlayer();
        if (cmd[0].equals(ACTIVATION_CMD))
        {
            rwdebug(3, "Activation command received; Displaying GUI");
            addCommonGuiToPlayer(player);
            refreshGUI(player);
            player.setMouseCursorVisible(true);
        }

        if (cmd[0].equals(REFRESH_CMD))
        {
            rwdebug(3, "Refresh command received; Reloading library");
            clearGUI(player);
            buildLibrary();
            player.sendTextMessage("rwdoc: Library reloaded.");
        }
    }

    public static void refreshGUI(Player player)
    {
        rwdebug(3, "Refreshing GUI");
        clearGUI(player);
        ArrayList<GuiElement> guiItems = new ArrayList<GuiElement>();
        MenuItemHandler mih = new MenuItemHandler();
        player.setAttribute("rwdoc_mih", mih);
        player.setAttribute("rwdoc_gui_elements", guiItems);
        RwdocDocument document = RWDOC_LIBRARY.getDocumentbyTitle(String.valueOf(player.getAttribute("rwdoc_current_document")));
        rwdebug(4, "Got document: " + document.getDocumentTitle());
        int pagenum = Integer.parseInt(String.valueOf(player.getAttribute("rwdoc_current_page")));
        GuiPanel panelLeft = (GuiPanel) player.getAttribute("rwdoc_pageLeftPanel");
        GuiPanel panelRight = (GuiPanel) player.getAttribute("rwdoc_pageRightPanel");
        rwdebug(3, "building left page");
        buildPage(panelLeft, player, document, pagenum);
        rwdebug(3, "building right page");
        buildPage(panelRight, player, document, pagenum + 1);
        setVisibility(player, true);
    }

    public static void clearGUI(Player player)
    {
        rwdebug(3, "Trying to remove items from player GUI left panel.");
        GuiPanel leftPanel = (GuiPanel) player.getAttribute("rwdoc_pageLeftPanel");
        int leftPanelNum = leftPanel.getID();
        rwdebug(4, "Clearing left. Attribname: " + "rwdoc_gui_elements_" + leftPanel.getID());
        ArrayList<GuiElement> guiItems = (ArrayList<GuiElement>) player.getAttribute("rwdoc_gui_elements_" + leftPanelNum);
        if(null != guiItems)
        {
            rwdebug(4, "playerGuiItem size = " + guiItems.size());
            for (GuiElement item : guiItems)
            {
                rwdebug(4, "removing item: " + item.getID());
                player.removeGuiElement(item);
                item.destroy();
            }
            player.deleteAttribute("rwdoc_gui_elements");
            rwdebug(4, "rwdoc_gui_elements removed");
        } else rwdebug(4, "left panel is empty. Nothing to do.");

        guiItems = null;
        rwdebug(3, "Trying to remove items from player GUI right panel.");
        GuiPanel rightPanel = (GuiPanel) player.getAttribute("rwdoc_pageRightPanel");
        int rightPanelNum = rightPanel.getID();
        rwdebug(4, "Clearing right. Attribname: " + "rwdoc_gui_elements_" + rightPanel.getID());
        guiItems = (ArrayList<GuiElement>) player.getAttribute("rwdoc_gui_elements_" + rightPanelNum);
        if(null != guiItems)
        {
            rwdebug(4, "playerGuiItem size = " + guiItems.size());
            for (GuiElement item : guiItems)
            {
                rwdebug(4, "removing item: " + item.getID());
                player.removeGuiElement(item);
            }
            player.deleteAttribute("rwdoc_gui_elements");
            rwdebug(4, "rwdoc_gui_elements removed");
        } else rwdebug(4, "right panel is empty. Nothing to do.");

    }

    @EventMethod
    public static void onGuiClick(PlayerGuiElementClickEvent event)
    {
        Player player = event.getPlayer();
        GuiElement element = event.getGuiElement();
        rwdebug(4, "mouseclick received");
        RwdocDocument document = RWDOC_LIBRARY.getDocumentbyTitle(String.valueOf(player.getAttribute("rwdoc_current_document")));
        if (element == (GuiImage) player.getAttribute("rwdoc_button_close"))
        {
            rwdebug(3, "button_close clicked");
            setVisibility(player,false);
            clearGUI(player);
            player.setMouseCursorVisible(false);
        } else

        if (element == (GuiImage) player.getAttribute("rwdoc_button_up"))
        {
            rwdebug(3, "button_up clicked");
            int pagenum = Integer.parseInt(String.valueOf(player.getAttribute("rwdoc_current_page")));
            if (pagenum != 0)
            {
                rwdebug(4, "Returning to top of current document.");
                player.setAttribute("rwdoc_current_page", "0");
                refreshGUI(player);
            } else
            {
                rwdebug(4, "Returning to default document.");
                player.setAttribute("rwdoc_current_document", "default");
                player.setAttribute("rwdoc_current_page", "0");
                refreshGUI(player);
            }
        } else

        if (element == (GuiImage) player.getAttribute("rwdoc_button_left"))
        {
            rwdebug(3, "button_left clicked");
            rwdebug(4, "current document: " + String.valueOf((player.getAttribute("rwdoc_current_document"))));
            // RwdocDocument document = RWDOC_LIBRARY.getDocumentbyTitle(String.valueOf(player.getAttribute("rwdoc_current_document")));
            if (document != null)
            {
                rwdebug(4, "document is not null");
                int pagenum = Integer.parseInt(String.valueOf(player.getAttribute("rwdoc_current_page")));
                if (pagenum != 0)
                {
                    int requestedPage = pagenum - 2;
                    rwdebug(4, "button_left page: " + pagenum + " reqpage: " + requestedPage);
                    player.setAttribute("rwdoc_current_page", max(0, requestedPage));
                    refreshGUI(player);
                } else { rwdebug(4, "Nothing to do. Already at page zero."); }
            } else { rwdebug(2, "button_right: current document is null! This shouldn't happen."); }
        } else

        if (element == (GuiImage) player.getAttribute("rwdoc_button_right"))
        {
            rwdebug(3, "button_right clicked");
            rwdebug(4, "current document: " + String.valueOf((player.getAttribute("rwdoc_current_document"))));
            //RwdocDocument document = RWDOC_LIBRARY.getDocumentbyTitle(String.valueOf(player.getAttribute("rwdoc_current_document")));
            if (document != null)
            {
                rwdebug(4, "document is not null");
                int numberofPages = document.getNumberofPages();
                if (numberofPages != 0)
                {
                    rwdebug(4, "numberofPages > 0");
                    int pageNum = Integer.parseInt(String.valueOf(player.getAttribute("rwdoc_current_page")));
                    int requestedPage = pageNum + 2;
                    rwdebug(4, "I am at page: " +
                            Integer.parseInt(String.valueOf(player.getAttribute("rwdoc_current_page"))) +
                            " I want to go to page: " + (pageNum + 2));
                    if (requestedPage < numberofPages)
                    {
                        rwdebug(4, "Page: " + requestedPage + "is < " + numberofPages);
                        player.setAttribute("rwdoc_current_page", requestedPage);
                        refreshGUI(player);
                    } else { rwdebug(4, "Sorry, p: " + requestedPage  + " is >= " + numberofPages);  }

                } else {rwdebug(2, "button_right: Zero pages in document! This shouldn't happen.");}
            } else { rwdebug(2, "button_right: current document is null! This shouldn't happen."); }
        } else
        {
            rwdebug(3, "Click didn't match anything above. Checking menu buttons.");
            ArrayList<GuiLabel> guiLabelsMIH = (ArrayList<GuiLabel>) player.getAttribute("rwdoc_MIH_guilabels");
            ArrayList<String> docLinksMIH = (ArrayList<String>) player.getAttribute("rwdoc_MIH_doclinks");
            ArrayList<Integer> pageNumMIH = (ArrayList<Integer>) player.getAttribute("rwdoc_MIH_pagenums");
            for (int count = 0; count < guiLabelsMIH.size(); count++)
            {
                rwdebug(4, "Listener: Have title:" + docLinksMIH.get(count));
            }
            // OK let's process them
            boolean foundMatch = false;
            for (int count = 0; count < guiLabelsMIH.size(); count++)
            {
                if(element == guiLabelsMIH.get(count))
                {
                    rwdebug(3, "Listener: found matching GuiLabel");
                    String currDoc = String.valueOf(player.getAttribute("rwdoc_current_document"));
                    if (currDoc.equals("default"))
                    {
                        rwdebug(4, "Listener: linking from default document");
                        String newDocTitle = docLinksMIH.get(count);
                        if (newDocTitle != "")
                        {
                            rwdebug(4, "newdoctitle = " + newDocTitle);
                            player.setAttribute("rwdoc_current_document", newDocTitle);
                            RwdocDocument newDocument = RWDOC_LIBRARY.getDocumentbyTitle(newDocTitle);
                            if (newDocument != null)
                            {
                                if (pageNumMIH.get(count) <= newDocument.getNumberofPages())
                                {
                                    player.setAttribute("rwdoc_current_page", pageNumMIH.get(count));
                                    refreshGUI(player);
                                } else
                                {
                                    rwdebug(2, "Invalid page number requested in: " + newDocTitle);
                                }
                            } else
                            {
                                rwdebug(2, "Link to non-existent document: " + newDocTitle
                                        + " in " + String.valueOf((player.getAttribute("rwdoc_current_document")))
                                        + " on " + String.valueOf((player.getAttribute("rwdoc_current_page")))
                                        + " This shouldn't happen for the default document."
                                );
                            }
                        } else
                        {
                            rwdebug(2, "Null value in menu button checking. This should never happen!");
                        }
                    } else
                        {
                            if (pageNumMIH.get(count) <= document.getNumberofPages())
                            {
                                player.setAttribute("rwdoc_current_page", pageNumMIH.get(count));
                                refreshGUI(player);
                            } else
                            {
                                rwdebug(2, "Invalid page number requested in menuitem: "
                                        + " in " + String.valueOf((player.getAttribute("rwdoc_current_document")))
                                        + " on " + String.valueOf((player.getAttribute("rwdoc_current_page")))
                                        );
                            }
                        }
                } else { rwdebug(4, "Listener - Not this one. Keep searching..."); }
            }
            if(!foundMatch)
            {
                rwdebug(2, "Invalid menuitem near: " +
                        String.valueOf(player.getAttribute("rwdoc_current_document"))
                        + " Page: " + String.valueOf(player.getAttribute("rwdoc_current_page")));
            }

        }
    }

    // currently not used
    @EventMethod
    public static void onPlayerKeyEvent(PlayerKeyEvent event)
    {
        Player player = event.getPlayer();
        rwdebug(4, "key pressed");
        if(event.getKeyCode() == KeyInput.KEY_Y && event.isPressed())
        {

        }
    }
}
