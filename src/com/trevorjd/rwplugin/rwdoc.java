package com.trevorjd.rwplugin;

import net.risingworld.api.Plugin;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerConnectEvent;
import net.risingworld.api.events.player.PlayerSpawnEvent;
import net.risingworld.api.gui.GuiElement;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.ImageInformation;
import net.risingworld.api.utils.KeyInput;

import java.util.ArrayList;
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
 * - custom front page menu
 * - configurable
 * - simple navigation
 * - auto-formatting (~ish)
 * - - no need to specify page left/right; rwdoc will automatically sequence pages
 * - - no need to specify text position; rwdoc handles vertical alignment (mostly)
 * - close button
 * - word wrapping
 * - tab stops for headings (indentation) using XML tab= attribute. (Nine steps of 10% each; headings only because word-wrapping is unaware of tab stops)
 * - image embedding!
 * - multiple documents
 * - refresh button (for ease of editing, save file in-place and hit refresh)
 * - journal XML format (extended)
 *     <title> (for main menu items)
 *     <menuitem> (for section links within document, use <page index="?"></page>)
 *     <headline> (puts a linebreak immediately after)
 *     <page> (for pages of your document)
 *     <text> (will use default or most recent values if colour/size/etc. are not specified)
 *     <image> (does this need explanation?)
 * - searches other plugins for doc files! (optional config)
 * - most elements can be left on auto-formatting or have their position manually set (caution: manually set elements are ignored by auto-formatting)
 * limitations:
 * - pages do not scroll, authors need to test layout before publishing (API limitation)
 * - does not support permissions (Maybe later)
 * - word wrap width must be specified in config file (API limitation)
 * - log messages are English only
 * - config file is English only
 * - cannot handle non-ASCII (Game limitation)
 * - images can be centered with the XML align= attribute. Text cannot. (API limitation)
 * - NavButtons and CloseButton won't work if you cover them! (API limitation)
 * - It's necessary for the author to ensure all pages are numbered
 * - It's necessary for the author to ensure all page numbers are consecutive
 */

public class rwdoc extends Plugin implements Listener
{
    // Globals
    public static Properties c = null; // config options in here
    protected static rwdoc plugin;
    protected static final String IMGDIR = "/images/";
    protected static boolean EDITOR = true; //user is in editor mode - show label borders
    protected static boolean EXTSEARCH = true; //search 3rd party plugin folders for rwdoc/rwdoc.xml
    protected static ImageInformation BGIMAGE;
    protected static ImageInformation HITBOXIMAGE;
    protected static String PLUGINSFOLDER;
    protected static RwdocLibrary rwdocLibrary;
    private static rwdocCmdListener cmdListener = new rwdocCmdListener();
    protected static int FONTCOLOR = 0x30201370;
    protected static int LOGLEVEL = 3;

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
        player.setAttribute("rwdoc_current_document", "default");
        player.setAttribute("rwdoc_current_page", "0");
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
