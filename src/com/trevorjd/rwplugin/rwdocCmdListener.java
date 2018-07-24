package com.trevorjd.rwplugin;

import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerCommandEvent;
import net.risingworld.api.events.player.PlayerKeyEvent;
import net.risingworld.api.events.player.gui.PlayerGuiElementClickEvent;
import net.risingworld.api.gui.GuiElement;
import net.risingworld.api.gui.GuiImage;
import net.risingworld.api.gui.GuiPanel;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.KeyInput;

import java.util.ArrayList;
import java.util.HashMap;
import static java.lang.Math.max;
import static com.trevorjd.rwplugin.rwdoc.rwdocLibrary;
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
        if (cmd[0].equals(rwdoc.c.getProperty("rwdocCommand")))
        {
            rwdebug(3, "Activation command received; Displaying GUI");
            addCommonGuiToPlayer(player);
            refreshGUI(player);
            player.setMouseCursorVisible(true);
        }
    }

    public static void refreshGUI(Player player)
    {
        rwdebug(3, "Refreshing GUI");
        clearGUI(player);
        ArrayList<GuiElement> guiItems = new ArrayList<GuiElement>();
        player.setAttribute("rwdoc_gui_elements", guiItems);
        RwdocDocument document = rwdocLibrary.getDocumentbyTitle(String.valueOf(player.getAttribute("rwdoc_current_document")));
        player.deleteAttribute("rwdoc_menu_elements");
        player.setAttribute("rwdoc_menu_elements", new HashMap<Integer, String>());
        rwdebug(3, "Got document: " + document.getDocumentTitle());
        int pagenum = Integer.parseInt(String.valueOf(player.getAttribute("rwdoc_current_page")));
        GuiPanel panelLeft = (GuiPanel) player.getAttribute("rwdoc_pageLeftPanel");
        GuiPanel panelRight = (GuiPanel) player.getAttribute("rwdoc_pageRightPanel");
        buildPage(panelLeft, player, document, pagenum);
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
            RwdocDocument document = rwdocLibrary.getDocumentbyTitle(String.valueOf(player.getAttribute("rwdoc_current_document")));
            int pagenum = Integer.parseInt(String.valueOf(player.getAttribute("rwdoc_current_page")));
            if (pagenum != 0)
            {
                pagenum = 0;
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
            rwdebug(3, "button_left clicked");
            RwdocDocument document = rwdocLibrary.getDocumentbyTitle(String.valueOf(player.getAttribute("rwdoc_current_document")));
            int pagenum = Integer.parseInt(String.valueOf(player.getAttribute("rwdoc_current_page")));
            player.setAttribute("rwdoc_current_page", max(0, pagenum - 2));
            refreshGUI(player);
        } else

        if (element == (GuiImage) player.getAttribute("rwdoc_button_right"))
        {
            rwdebug(3, "button_right clicked");
            RwdocDocument document = rwdocLibrary.getDocumentbyTitle(String.valueOf(player.getAttribute("rwdoc_current_document")));
            int numberofPages = document.getNumberofPages();
            int pagenum = Integer.parseInt(String.valueOf(player.getAttribute("rwdoc_current_page")));
            if (pagenum + 1 > numberofPages)
            {
                player.setAttribute("rwdoc_current_page", pagenum + 2);
            }
            refreshGUI(player);
        } else
        {
            rwdebug(3, "Click didn't match anything above. Checking menu buttons.");
            HashMap<Integer, MenuElement> menuElements = (HashMap<Integer, MenuElement>) player.getAttribute("rwdoc_menu_elements");
            rwdebug(3, "Have ID list; setting title");
            MenuElement menuElement = menuElements.get(element.getID());
            if (menuElement != null)
            {
                String newDocTitle = menuElement.getTitle();
                if(newDocTitle != "")
                {
                    rwdebug(4, "newdoctitle = " + newDocTitle);
                    player.setAttribute("rwdoc_current_document", newDocTitle);
                    RwdocDocument document = rwdocLibrary.getDocumentbyTitle(newDocTitle);
                    if (menuElement.getPageNum() <= document.getNumberofPages())
                    {
                        player.setAttribute("rwdoc_current_page", menuElement.getPageNum());
                        refreshGUI(player);
                    } else { rwdebug(2, "Invalid page number requested in: " + newDocTitle); }
                } else { rwdebug(2, "Invalid document requested! This shouldn't happen."); }
            } else { rwdebug(2, "Invalid menuitem near: " +
                    rwdocLibrary.getDocumentbyTitle(String.valueOf(player.getAttribute("rwdoc_current_document")))
                    + " Page: " + String.valueOf(player.getAttribute("rwdoc_current_page"))); }


        }
    }

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
