package com.trevorjd.rwplugin;

import net.risingworld.api.Plugin;
import net.risingworld.api.gui.GuiLabel;
import net.risingworld.api.objects.Player;

public class updateGUI
{

    private static final String newline = System.getProperty("line.separator");

    public static void setMenuListItems(Plugin plugin, Player player, String text)
    {
        GuiLabel lMenuLabel = (GuiLabel) player.getAttribute("lMenuList");
        lMenuLabel.setText(wordWrap(text));
    }

    public static void setPageBodyRight(Plugin plugin, Player player, String text)
    {
        GuiLabel lPageBodyR = (GuiLabel) player.getAttribute("lPageBodyR");
        lPageBodyR.setText(wordWrap(text));
        lPageBodyR.setPosition  (0.55f, lPageBodyR.getPositionY(), true);
    }

    public static void setPageBodyRight(Plugin plugin, Player player, String text, Float x_pos, Float y_pos )
    {
        GuiLabel lPageBodyR = (GuiLabel) player.getAttribute("lPageBodyR");
        lPageBodyR.setText(wordWrap(text));
        lPageBodyR.setPosition  (x_pos, y_pos, true);
    }

    public static void setPageBodyLeft(Plugin plugin, Player player, String text)
    {
        GuiLabel lPageBodyL = (GuiLabel) player.getAttribute("lPageBodyL");
        lPageBodyL.setText(wordWrap(text));
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

    protected static int calcWrapLength()
    {
        int result = 54;
        return result;
    }

    public static String wordWrap(String in) {
        //:: Trim
        int length = calcWrapLength();
        while(in.length() > 0 && (in.charAt(0) == '\t' || in.charAt(0) == ' '))
            in = in.substring(1);

        //:: If Small Enough Already, Return Original
        if(in.length() < length)
            return in;

        //:: If Next length Contains Newline, Split There
        if(in.substring(0, length).contains(newline))
            return in.substring(0, in.indexOf(newline)).trim() + newline +
                    wordWrap(in.substring(in.indexOf(newline) + 1));

        //:: Otherwise, Split Along Nearest Previous Space/Tab/Dash
        int spaceIndex = Math.max(Math.max( in.lastIndexOf(" ",  length),
                in.lastIndexOf("\t", length)),
                in.lastIndexOf("-",  length));

        //:: If No Nearest Space, Split At length
        if(spaceIndex == -1)
            spaceIndex = length;

        //:: Split
        return in.substring(0, spaceIndex).trim() + newline + wordWrap(in.substring(spaceIndex));
    }
}
