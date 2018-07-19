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

import static com.trevorjd.rwplugin.mainGUI.showHideGUI;

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
            showHideGUI(player, true);

            String eol = System.getProperty("line.separator");
            menuGui.setMenuLabelText(rwdoc.plugin, player, "Server Rules" + eol + "Land Protection" + eol + "Building" + eol + "Animal Management" + eol + "Transportation" + eol + "Web Forum");

            String s = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.";
            menuGui.setBodyTextLabel(rwdoc.plugin, player,rwdocUtils.wordWrap(s));
            player.setMouseCursorVisible(true);
        }
    }

    @EventMethod
    public static void onGuiClick(PlayerGuiElementClickEvent event)
    {
        Player player = event.getPlayer();
        GuiElement element = event.getGuiElement();
        System.out.println("rwdoc Debug: I got a click!");
        GuiPanel panel = (GuiPanel) player.getAttribute("pTextPanel");
        System.out.println("rwdoc Debug: panelWidth = " + panel.getWidth());
        if (element == (GuiImage) player.getAttribute("pButtonClose"))
        {
            showHideGUI(player,false);
            player.setMouseCursorVisible(false);
        }

        if (element == (GuiImage) player.getAttribute("pButtonFontScaleUp"))
        {
            int fontSize;
            System.out.println("rwdoc Debug: Scaling Up");
            if (player.hasAttribute("FontSize"))
            {
                fontSize = (int) player.getAttribute("FontSize");
            } else fontSize = 20;
            player.setAttribute("FontSize", fontSize + 1 );
            mainGUI.refreshDisplay(player);
        }

        if (element == (GuiImage) player.getAttribute("pButtonFontScaleDown"))
        {
            int fontSize;
            System.out.println("rwdoc Debug: Scaling Down");
            if (player.hasAttribute("FontSize"))
            {
                fontSize = (int) player.getAttribute("FontSize");
            } else fontSize = 20;
            player.setAttribute("FontSize", fontSize - 1 );
            mainGUI.refreshDisplay(player);
        }
    }

    @EventMethod
    public static void onPlayerKeyEvent(PlayerKeyEvent event)
    {
        Player player = event.getPlayer();
        System.out.println("rwdoc Debug: I got a key!");
        if(event.getKeyCode() == KeyInput.KEY_Y && event.isPressed())
        {
            if ((boolean) player.getAttribute("guiVisible"))
            {
                System.out.println("rwdoc Debug: Keypress - Hiding GUI");
                showHideGUI(player,false);
                player.setMouseCursorVisible(false);
            }
            else
            {
                System.out.println("rwdoc Debug: Keypress - Showing GUI");
                showHideGUI(player,true);
                player.setMouseCursorVisible(true);
            }
        }
    }
}
