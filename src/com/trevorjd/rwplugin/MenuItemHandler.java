package com.trevorjd.rwplugin;

import net.risingworld.api.gui.GuiLabel;
import net.risingworld.api.objects.Player;

import java.util.ArrayList;

import static com.trevorjd.rwplugin.rwdocUtils.rwdebug;

public class MenuItemHandler
{
    private static ArrayList<String> docLinks;
    private static ArrayList<Integer> pageNums;
    private static ArrayList<GuiLabel> guiLabels;

    public MenuItemHandler()
    {
        docLinks = new ArrayList<String>();
        pageNums = new ArrayList<Integer>();
        guiLabels = new ArrayList<GuiLabel>();
    }

    public static void addMenuItem(String docLink, int pageNum, GuiLabel guiLabel)
    {
        docLinks.add(docLink);
        pageNums.add(pageNum);
        guiLabels.add(guiLabel);

        queryMenuItems();
    }

    public static void queryMenuItems()
    {
        rwdebug(3, "menuitemhandler: summoned, I come");

        for (int count = 0; count < docLinks.size(); count++)
        {
            rwdebug(3, String.format("------MIH: queryMenuItems------ link: %s page: %d id: %d\n",
                    docLinks.get(count),
                    pageNums.get(count),
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
        player.setAttribute("rwdoc_MIH_pagenums", pageNums);
        player.setAttribute("rwdoc_MIH_guilabels", guiLabels);
    }
    //getters
    public static ArrayList<GuiLabel> getGuiLabels()
    {
        return guiLabels;
    }

    public static ArrayList<Integer> getPageNums()
    {
        return pageNums;
    }

    public static ArrayList<String> getDocLinks()
    {
        return docLinks;
    }
}
