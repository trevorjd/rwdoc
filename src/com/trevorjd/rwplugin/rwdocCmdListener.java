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

import java.io.InputStream;
import java.util.ArrayList;

import static com.trevorjd.rwplugin.RwdocLibrary.getTitleList;
import static com.trevorjd.rwplugin.rwdoc.*;
import static java.lang.Math.max;
import static com.trevorjd.rwplugin.RwdocLibrary.buildLibrary;
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
            addCommonGuiToPlayer(player);
            refreshGUI(player);
            player.setMouseCursorVisible(true);
            player.disableClientsideKeys(KeyInput.KEY_ESCAPE);
        }

        if (cmd[0].equals(REFRESH_CMD))
        {
            if(REFRESH_ENABLED)
            {
                clearGUI(player);
                buildLibrary();
                player.sendTextMessage("rwdoc: Library reloaded.");
            }
        }

        if (cmd[0].equals(EDITOR_CMD))
        {
            if(EDITOR_ENABLED)
            {

                if(EDITOR)
                {
                    EDITOR = false;
                } else
                {
                    EDITOR = true;
                }
                player.sendTextMessage(c.getProperty("msg_editor_mode"));
            }
        }

        if (cmd[0].equals("/rwdocloglevel"))
        {
            player.sendTextMessage("changing log level");
            LOGLEVEL = Integer.parseInt(cmd [1]);
        }

        if (cmd[0].equals("/rwdoclist"))
        {
            player.sendTextMessage("Showing doc list");
            ArrayList<String> titleList = getTitleList();
            for (String s : titleList)
            {
                rwdebug(3, s);
            }
        }
    }

    public static void refreshGUI(Player player)
    {
        clearGUI(player);
        ArrayList<GuiElement> guiItems = new ArrayList<GuiElement>();
        MenuItemHandler mih = new MenuItemHandler();
        player.setAttribute("rwdoc_mih", mih);
        player.setAttribute("rwdoc_gui_elements", guiItems);
        RwdocDocument document = RWDOC_LIBRARY.getDocumentbyTitle(String.valueOf(player.getAttribute("rwdoc_current_document")));
        int pagenum = Integer.parseInt(String.valueOf(player.getAttribute("rwdoc_current_page")));
        GuiPanel panelLeft = (GuiPanel) player.getAttribute("rwdoc_pageLeftPanel");
        GuiPanel panelRight = (GuiPanel) player.getAttribute("rwdoc_pageRightPanel");
        buildPage(panelLeft, player, document, pagenum);
        buildPage(panelRight, player, document, pagenum + 1);
        setVisibility(player, true);
    }

    public static void clearGUI(Player player)
    {
        GuiPanel leftPanel = (GuiPanel) player.getAttribute("rwdoc_pageLeftPanel");
        int leftPanelNum = leftPanel.getID();
        ArrayList<GuiElement> guiItems = (ArrayList<GuiElement>) player.getAttribute("rwdoc_gui_elements_" + leftPanelNum);
        if(null != guiItems)
        {
            for (GuiElement item : guiItems)
            {
                player.removeGuiElement(item);
                item.destroy();
            }
            player.deleteAttribute("rwdoc_gui_elements");
        } else rwdebug(4, "left panel is empty. Nothing to do.");

        guiItems = null;
        GuiPanel rightPanel = (GuiPanel) player.getAttribute("rwdoc_pageRightPanel");
        int rightPanelNum = rightPanel.getID();
        guiItems = (ArrayList<GuiElement>) player.getAttribute("rwdoc_gui_elements_" + rightPanelNum);
        if(null != guiItems)
        {
            for (GuiElement item : guiItems)
            {
                player.removeGuiElement(item);
            }
            player.deleteAttribute("rwdoc_gui_elements");
        } else rwdebug(4, "right panel is empty. Nothing to do.");

    }

    @EventMethod
    public static void onGuiClick(PlayerGuiElementClickEvent event)
    {
        Player player = event.getPlayer();
        GuiElement element = event.getGuiElement();
        RwdocDocument document = RWDOC_LIBRARY.getDocumentbyTitle(String.valueOf(player.getAttribute("rwdoc_current_document")));
        if (element == (GuiImage) player.getAttribute("rwdoc_button_close"))
        {
            setVisibility(player,false);
            clearGUI(player);
            player.setMouseCursorVisible(false);
            player.enableClientsideKeys(KeyInput.KEY_ESCAPE);
        } else

        if (element == (GuiImage) player.getAttribute("rwdoc_button_up"))
        {
            int pagenum = Integer.parseInt(String.valueOf(player.getAttribute("rwdoc_current_page")));
            if (pagenum != 0)
            {
                player.setAttribute("rwdoc_current_page", "0");
                refreshGUI(player);
            } else
            {
                player.setAttribute("rwdoc_current_document", "default");
                player.setAttribute("rwdoc_current_page", "0");
                refreshGUI(player);
            }
        } else

        if (element == (GuiImage) player.getAttribute("rwdoc_button_left"))
        {
            // RwdocDocument document = RWDOC_LIBRARY.getDocumentbyTitle(String.valueOf(player.getAttribute("rwdoc_current_document")));
            if (document != null)
            {
                int pagenum = Integer.parseInt(String.valueOf(player.getAttribute("rwdoc_current_page")));
                if (pagenum != 0)
                {
                    int requestedPage = pagenum - 2;
                    player.setAttribute("rwdoc_current_page", max(0, requestedPage));
                    refreshGUI(player);
                } else { rwdebug(3, "Nothing to do. Already at page zero."); }
            } else { rwdebug(2, "button_right: current document is null! This shouldn't happen."); }
        } else

        if (element == (GuiImage) player.getAttribute("rwdoc_button_right"))
        {
            //RwdocDocument document = RWDOC_LIBRARY.getDocumentbyTitle(String.valueOf(player.getAttribute("rwdoc_current_document")));
            if (document != null)
            {
                int numberofPages = document.getNumberofPages();
                if (numberofPages != 0)
                {
                    int pageNum = Integer.parseInt(String.valueOf(player.getAttribute("rwdoc_current_page")));
                    int requestedPage = pageNum + 2;
                    if (requestedPage < numberofPages)
                    {
                        player.setAttribute("rwdoc_current_page", requestedPage);
                        refreshGUI(player);
                    } else { rwdebug(3, "Already at page limit.");  }

                } else {rwdebug(2, "button_right: Zero pages in document! This shouldn't happen.");}
            } else { rwdebug(2, "button_right: current document is null! This shouldn't happen."); }
        } else

        // mouseclick didn't match any of the button elements, so it must be a menuitem GuiLabel
        {
            rwdebug(3, "Player clicked a menuitem.");
            ArrayList<GuiLabel> guiLabelsMIH = (ArrayList<GuiLabel>) player.getAttribute("rwdoc_MIH_guilabels");
            ArrayList<String> docLinksMIH = (ArrayList<String>) player.getAttribute("rwdoc_MIH_doclinks");
            ArrayList<String> pageIndexMIH = (ArrayList<String>) player.getAttribute("rwdoc_MIH_pageindices");
            // OK let's process them
            boolean foundMatch = false;
            for (int count = 0; count < guiLabelsMIH.size(); count++)
            {
                if(element == guiLabelsMIH.get(count))
                {
                    // a menuitem contains a docTitle and a pageIndex
                    String currDoc = String.valueOf(player.getAttribute("rwdoc_current_document"));
                    String newDocTitle = docLinksMIH.get(count);
                    if (newDocTitle != "")
                    {
                        RwdocDocument requestedDocument = RWDOC_LIBRARY.getDocumentbyTitle(newDocTitle);
                        if (requestedDocument != null)
                        {
                            rwdebug(3, "Requested document: " + requestedDocument);
                            //find the linked page and get its pagenum
                            String pageIndex = pageIndexMIH.get(count);
                            for (DocumentPage page : requestedDocument.getPageList())
                            {
                                if(pageIndex.equals(page.getPageIndex()))
                                {
                                    // some jiggery pokery to keep page numbers correctly aligned
                                    int pageNum = page.getPageNumber();
                                    if(pageNum % 2 == 0)
                                    {
                                        // if the pagenumber is even, start there
                                        player.setAttribute("rwdoc_current_document", newDocTitle);
                                        player.setAttribute("rwdoc_current_page", page.getPageNumber());
                                    } else
                                    {
                                        // if the page number is odd, we start on the previous page
                                        player.setAttribute("rwdoc_current_document", newDocTitle);
                                        player.setAttribute("rwdoc_current_page", page.getPageNumber()-1);
                                    }
                                    foundMatch = true;
                                    refreshGUI(player);
                                }
                            }
                        } else
                        {
                            player.sendTextMessage("Sorry, the linked page does not exist.");
                            rwdebug(2, "Linked page does not exist: " + newDocTitle
                                    + " index: " + pageIndexMIH.get(count)
                                    + " This shouldn't happen for the front page menu links."
                            );
                        }
                    } else
                    {
                        rwdebug(2, "Null value in menu button checking. This should never happen!");
                    }
                } else {
                    // nothing else
                }
            }
            if(!foundMatch)
            {
                rwdebug(2, "Invalid menuitem in document: " +
                        String.valueOf(player.getAttribute("rwdoc_current_document"))
                        + " Page: " + String.valueOf(player.getAttribute("rwdoc_current_page")));
            }

        }
    }

    @EventMethod
    public static void onPlayerKeyEvent(PlayerKeyEvent event)
    {
        Player player = event.getPlayer();
        if(event.getKeyCode() == KeyInput.KEY_ESCAPE)
        {
            setVisibility(player,false);
            clearGUI(player);
            player.setMouseCursorVisible(false);
            player.enableClientsideKeys(KeyInput.KEY_ESCAPE);
        }
    }
}
