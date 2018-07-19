package com.trevorjd.rwplugin;

import net.risingworld.api.Plugin;
import net.risingworld.api.gui.GuiLabel;
import net.risingworld.api.objects.Player;

public class menuGui
{
    public menuGui()
    {
    }

    public static void setMenuLabelText(Plugin plugin, Player player, String text)
    {
        GuiLabel pMenuLabel = (GuiLabel) player.getAttribute("pMenuLabel");
        pMenuLabel.setText(text);
    }

    public static void setBodyTextLabel(Plugin plugin, Player player, String text)
    {
        GuiLabel pMenuLabel = (GuiLabel) player.getAttribute("pTextLabel");
        pMenuLabel.setText(text);
    }
}
