package com.trevorjd.rwplugin;

import net.risingworld.api.Plugin;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerConnectEvent;
import net.risingworld.api.events.player.PlayerSpawnEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.KeyInput;

import java.util.Properties;

import static com.trevorjd.rwplugin.rwdocGUI.setupMainGUI;

/**
 * @author trevorjd
 * LICENCE: MIT https://en.wikipedia.org/wiki/MIT_License
 *
 * rwDoc is an enhancement of the Rising World journal system
 * Default activation chat command: /help
 *
 * Plugin/Mod authors and server administrators can create attractive
 * documentation for their server or mod.
 *
 * rwDoc features:
 * - remembers current reader position
 * - self-documented!
 * - custom menu
 * - configurable
 * - simple navigation
 * - auto-formatting (~ish)
 * - - no need to specify page left/right; rwdoc will automatically sequence pages
 * - - no need to specify text position except after embedding an image
 * - remembers current reader position
 * - close button
 * - word wrapping
 * - image embedding
 * - multiple documents
 * - refresh button (for ease of editing, save file in-place and hit refresh)
 * - journal XML format (extended)
 *     <title> (for main menu items)
 *     <menuitem> (for section links within document, use <page index="?"></page>)
 *     <headline> (puts a linebreak immediately after)
 *     <page> (for pages of your document)
 *     <text> (will use default or most recent values if colour/size/etc. are not specified)
 *     <image> (does this need explanation?)
 * - searches other plugins for doc files (optional config)
 * limitations:
 * - images must be positioned and sized manually (API limitation)
 * - pages do not scroll, authors need to test layout before publishing (API limitation)
 * - does not support permissions (Maybe later)
 * - word wrap width must be specified in config file (API limitation)
 * - log messages are English only (May add localized error messages if there's a huge demand)
 * - config file is English only (Not sure if I can localize the config)
 * - cannot handle non-ASCII (Game limitation)
 */

public class rwdoc extends Plugin implements Listener
{
    // Globals
    public static Properties c = null; // config options in here

    protected static final String IMGDIR = "/images/";
    protected static boolean EDITOR = true; //user is in editor mode - show label borders
    protected static boolean EXTSEARCH = true; //search 3rd party plugin folders for rwdoc/rwdoc.xml
    protected static String IMGPATHBUFFER;

    protected static rwdoc plugin;
    protected static RwdocLibrary rwdocLibrary;
    rwdocCmdListener cmdListener = new rwdocCmdListener();
    protected static int FONTCOLOR = 0x30201370;

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
        //showTestGui(player);
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
