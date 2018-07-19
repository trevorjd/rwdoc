package com.trevorjd.rwplugin;

import net.risingworld.api.Plugin;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerConnectEvent;
import net.risingworld.api.events.player.PlayerSpawnEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.KeyInput;

import java.util.Properties;

import static com.trevorjd.rwplugin.mainGUI.setupMainGUI;

/**
 * @author trevorjd
 */

public class rwdoc extends Plugin implements Listener
{
    // Globals
    public static Properties c = null; // config options in here

    // RETURN CODES
    public static final int ERR_SUCCESS = 0;
    public static final int ERR_FAILURE = -1;

    static rwdoc plugin;
    rwdocCmdListener cmdListener = new rwdocCmdListener();

    @Override
    public void onEnable(){
        plugin = this;
        boolean initSuccess = rwdocUtils.initPlugin(this);
        if (initSuccess)
        {
            registerEventListener(this);
            registerEventListener(cmdListener);
            System.out.println("rwdoc: Enabled.");
        } else
            System.out.println("rwdoc: Failed to initialise. Check log for details.");
    }

    @Override
    public void onDisable(){
        unregisterEventListener(this);
        System.out.println("rwdoc: Disabled.");
    }

    @EventMethod
    public void onPlayerSpawnEvent(PlayerSpawnEvent event)
    {
        Player player = event.getPlayer();
        setupMainGUI(player);
    }

    @EventMethod
    public void onPlayerConnect(PlayerConnectEvent event)
    {
        Player player = event.getPlayer();
        player.setAttribute("guiVisible", false);
        player.setListenForKeyInput(true);
        player.registerKeys(KeyInput.KEY_Y);
    }

}
