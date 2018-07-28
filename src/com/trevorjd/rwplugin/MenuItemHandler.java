package com.trevorjd.rwplugin;

import net.risingworld.api.gui.GuiLabel;
import net.risingworld.api.objects.Player;

import java.util.ArrayList;

import static com.trevorjd.rwplugin.rwdocUtils.rwdebug;

public class MenuItemHandler
{
    private static ArrayList<String> docLinks;
    private static ArrayList<String> pageIndices;
    private static ArrayList<GuiLabel> guiLabels;

    public MenuItemHandler()
    {
        docLinks = new ArrayList<String>();
        pageIndices = new ArrayList<String>();
        guiLabels = new ArrayList<GuiLabel>();
    }

    public static void addMenuItem(String docLink, String pageIndex, GuiLabel guiLabel)
    {
        docLinks.add(docLink);
        pageIndices.add(pageIndex);
        guiLabels.add(guiLabel);

        queryMenuItems();
    }

    public static void queryMenuItems()
    {
        rwdebug(4, "MIH: querying contents");

        for (int count = 0; count < docLinks.size(); count++)
        {
            rwdebug(4, String.format("MIH: link: %s page: %s id: %d\n",
                    docLinks.get(count),
                    pageIndices.get(count),
                    guiLabels.get(count).getID()
                    ));
        }
    }

    public static void assignToPlayer(Player player)
    {
        player.deleteAttribute("rwdoc_MIH_doclinks");
        player.deleteAttribute("rwdoc_MIH_pagenums");
        player.deleteAttribute("rwdoc_MIH_guilabels");
        player.setAttribute("rwdoc_MIH_doclinks", docLinks);
        player.setAttribute("rwdoc_MIH_pageindices", pageIndices);
        player.setAttribute("rwdoc_MIH_guilabels", guiLabels);
    }
    //getters
    public static ArrayList<GuiLabel> getGuiLabels()
    {
        return guiLabels;
    }

    public static ArrayList<String> getPageNums()
    {
        return pageIndices;
    }

    public static ArrayList<String> getDocLinks()
    {
        return docLinks;
    }
}
