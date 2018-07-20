package com.trevorjd.rwplugin;

import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerCommandEvent;
import net.risingworld.api.events.player.PlayerKeyEvent;
import net.risingworld.api.events.player.gui.PlayerGuiElementClickEvent;
import net.risingworld.api.gui.GuiElement;
import net.risingworld.api.gui.GuiImage;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.KeyInput;

import static com.trevorjd.rwplugin.mainGUI.toggleFrontPage;
import static com.trevorjd.rwplugin.mainGUI.toggleGUI;
import static com.trevorjd.rwplugin.rwdoc.plugin;
import static com.trevorjd.rwplugin.rwdocUtils.*;

public class rwdocCmdListener implements Listener
{
    @EventMethod
    public static void onPlayerCommand(PlayerCommandEvent event)
    {
        String[] cmd = event.getCommand().split(" ");
        Player player = event.getPlayer();
        if (cmd[0].equals(rwdoc.c.getProperty("rwdocCommand")))
        {
            System.out.println("rwdoc Debug: command entered. Displaying GUI");
            toggleGUI(player, true);

            String eol = System.getProperty("line.separator");
            updateGUI.setMenuListItems(plugin, player, "Server Rules" + eol + "Land Protection" + eol + "Building" + eol + "Animal Management" + eol + "Transportation" + eol + "Web Forum");

            String s = "Welcome to our server!";
            updateGUI.setPageBodyRight(plugin, player, s);

            player.setMouseCursorVisible(true);
        }
    }

    @EventMethod
    public static void onGuiClick(PlayerGuiElementClickEvent event)
    {
        Player player = event.getPlayer();
        GuiElement element = event.getGuiElement();
        System.out.println("rwdoc Debug: I got a click!");
        if (element == (GuiImage) player.getAttribute("iButtonClose"))
        {
            System.out.println("rwdoc Debug: I got a Close click!");
            toggleGUI(player,false);
            player.setMouseCursorVisible(false);
        }

        if (element == (GuiImage) player.getAttribute("iNavUp"))
        {
            System.out.println("rwdoc Debug: Page turn - up.");
            updateGUI.setPageBodyRight(plugin, player, getDefaultWelcome(), 0.65f, 0.5f);
            updateGUI.updatePageTitleRight(plugin, player, "");
            toggleFrontPage(player,true);
        }

        if (element == (GuiImage) player.getAttribute("iNavLeft"))
        {
            System.out.println("rwdoc Debug: Page turn - left.");
        }

        if (element == (GuiImage) player.getAttribute("iNavRight"))
        {
            System.out.println("rwdoc Debug: Page turn - right.");
            updateGUI.setPageBodyLeft(plugin, player, getBluePrintInfo());
            updateGUI.setPageBodyRight(plugin, player,getLoremIpsum());
            updateGUI.updatePageTitleRight(plugin, player, "Lorem to the Ipsum!");
            updateGUI.updatePageTitleLeft(plugin, player, getDefaultTitle());
            toggleFrontPage(player,false);
        }
    }

    @EventMethod
    public static void onPlayerKeyEvent(PlayerKeyEvent event)
    {
        Player player = event.getPlayer();
        System.out.println("rwdoc Debug: I got a key!");
        if(event.getKeyCode() == KeyInput.KEY_Y && event.isPressed())
        {

        }
    }
}
