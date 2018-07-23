package com.trevorjd.rwplugin;

import net.risingworld.api.Plugin;
import net.risingworld.api.gui.GuiLabel;
import net.risingworld.api.objects.Player;

public class updateGUI
{
    public static void setMenuListItems(Plugin plugin, Player player, String text)
    {
        GuiLabel lMenuLabel = (GuiLabel) player.getAttribute("lMenuList");
        //lMenuLabel.setText(wordWrap(text));
    }

    public static void setPageBodyRight(Plugin plugin, Player player, String text)
    {
        GuiLabel lPageBodyR = (GuiLabel) player.getAttribute("lPageBodyR");
        //lPageBodyR.setText(wordWrap(text));
        lPageBodyR.setPosition  (0.55f, lPageBodyR.getPositionY(), true);
    }

    public static void setPageBodyRight(Plugin plugin, Player player, String text, Float x_pos, Float y_pos )
    {
        GuiLabel lPageBodyR = (GuiLabel) player.getAttribute("lPageBodyR");
        //lPageBodyR.setText(wordWrap(text));
        lPageBodyR.setPosition  (x_pos, y_pos, true);
    }

    public static void setPageBodyLeft(Plugin plugin, Player player, String text)
    {
        GuiLabel lPageBodyL = (GuiLabel) player.getAttribute("lPageBodyL");
        //lPageBodyL.setText(wordWrap(text));
        lPageBodyL.setPosition  (0.1f, lPageBodyL.getPositionY(), true);
    }

    public static void updatePageTitleLeft(Plugin plugin, Player player, String text)
    {
        GuiLabel lPageTitleL = (GuiLabel) player.getAttribute("lPageTitleL");
        lPageTitleL.setText(text);
        lPageTitleL.setPosition  (0.2f, lPageTitleL.getPositionY(), true);
    }

    public static void updatePageTitleRight(Plugin plugin, Player player, String text)
    {
        GuiLabel lPageTitleR = (GuiLabel) player.getAttribute("lPageTitleR");
        lPageTitleR.setText(text);
        lPageTitleR.setPosition  (0.6f, lPageTitleR.getPositionY(), true);
    }


}
