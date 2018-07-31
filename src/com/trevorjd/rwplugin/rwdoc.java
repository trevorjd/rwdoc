package com.trevorjd.rwplugin;

import net.risingworld.api.Plugin;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerConnectEvent;
import net.risingworld.api.events.player.PlayerSpawnEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.ImageInformation;
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
 * - optional frames around images
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
 * - In-text color codes (e.g. [#7055ed]) behave strangely so are not enabled. (Trevor or API limitation? Not sure.)
 */

public class rwdoc extends Plugin implements Listener
{
    // Globals - hardwired
    protected static String PLUGIN_VER = "0.1.3";
    protected static String REQUIRED_GAME_VER = "0.9.3";
    public static Properties c = null; // config options in here
    protected static rwdoc plugin;
    protected static boolean EDITOR; //plugin is in editor mode - show label borders
    protected static final String IMGDIR = "/images/";
    protected static final double THRESHOLD = .0001;
    protected static ImageInformation BGIMAGE;
    protected static ImageInformation HITBOXIMAGE;
    protected static ImageInformation BULLET;
    protected static ImageInformation IMAGE_FRAME_1;
    protected static ImageInformation IMAGE_FRAME_2;
    protected static ImageInformation IMAGE_FRAME_3;
    protected static ImageInformation IMAGE_FRAME_4;
    protected static ImageInformation HRULE;
    protected static ImageInformation VRULE;
    protected static ImageInformation TICK;
    protected static ImageInformation CROSS;
    protected static String PLUGINSFOLDER; // location of RW plugins folder for searching other plugins
    protected static String DEFAULT_TITLE = "rwDoc - Documentation for a Rising World";

    // Globals - init with config file
    protected static String EDITOR_CMD; // activation command for editor mode
    protected static boolean EXTSEARCH; //search 3rd party plugin folders for rwdoc/rwdoc.xml
    protected static int FONTCOLOR;
    protected static int LOGLEVEL = 3; //give it a default value so that it works when loading properties
    protected static String ACTIVATION_CMD; // activation command for plugin
    protected static String REFRESH_CMD; // activation command for library refresh
    protected static int DEFAULT_HEADLINE_SIZE = 30;
    protected static int DEFAULT_MENUITEM_SIZE = 40;
    protected static int DEFAULT_TEXT_SIZE = 20;
    protected static boolean MENUITEM_BULLETS;
    protected static boolean EDITOR_ENABLED;
    protected static boolean REFRESH_ENABLED;
    protected static boolean VERSION_CHECKING;
    protected static boolean VERSION_ANNOUNCEMENT;

    // Shared resources
    protected static RwdocLibrary RWDOC_LIBRARY;
    private static rwdocCmdListener CMD_LISTENER = new rwdocCmdListener();
    protected static boolean UPTODATE = false;
    protected static boolean COMPATIBLE = false;

    @Override
    public void onEnable(){
        plugin = this;
        boolean initSuccess = rwdocUtils.initPlugin(this);
        if (initSuccess)
        {
            registerEventListener(this);
            registerEventListener(CMD_LISTENER);
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
        //version announcements
        if(VERSION_CHECKING && VERSION_ANNOUNCEMENT)
        {
            if(!UPTODATE) { player.sendTextMessage(c.getProperty("msg_plugin_outdated")); }
            if(!COMPATIBLE) { player.sendTextMessage(c.getProperty("msg_server_outdated")); }
        }
    }

    @EventMethod
    public void onPlayerConnect(PlayerConnectEvent event)
    {
        Player player = event.getPlayer();
        player.setAttribute("guiVisible", false);
        player.registerKeys(KeyInput.KEY_ESCAPE);
        player.setListenForKeyInput(true);
    }

}
