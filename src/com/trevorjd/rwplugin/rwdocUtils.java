package com.trevorjd.rwplugin;

import net.risingworld.api.Plugin;
import net.risingworld.api.utils.ImageInformation;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

import static com.trevorjd.rwplugin.rwdoc.*;

public class rwdocUtils
{

    protected static boolean initPlugin(Plugin plugin)
    {
        rwdebug(3, "Initialising plugin.");
        boolean success = false;
        rwdoc.c = propertiesHandler.getConfig();

        rwdebug(4, "pluginfolder = " + plugin.getPath());
        BGIMAGE = new ImageInformation(plugin, IMGDIR + "bgImage.png");
        HITBOXIMAGE = new ImageInformation(plugin, IMGDIR + "hitbox.png");
        BULLET = new ImageInformation(plugin, IMGDIR + "bullet.png");
        IMAGE_FRAME_1 = new ImageInformation(plugin, IMGDIR + "frame_1.png");
        IMAGE_FRAME_2 = new ImageInformation(plugin, IMGDIR + "frame_2.png");
        IMAGE_FRAME_3 = new ImageInformation(plugin, IMGDIR + "frame_3.png");
        IMAGE_FRAME_4 = new ImageInformation(plugin, IMGDIR + "frame_4.png");
        TICK = new ImageInformation(plugin, IMGDIR + "greentick.png");
        CROSS = new ImageInformation(plugin, IMGDIR + "redx.png");
        HRULE = new ImageInformation(plugin, IMGDIR + "hrule.png");
        VRULE = new ImageInformation(plugin, IMGDIR + "vrule.png");
        FONTCOLOR = 0x30201370;
        PLUGINSFOLDER = plugin.getPath().substring(0,plugin.getPath().lastIndexOf(File.separator));

        // get values from properties
        EXTSEARCH = Boolean.valueOf(c.getProperty("search_all_plugins"));
        LOGLEVEL = Integer.parseInt(c.getProperty("log_level"));
        ACTIVATION_CMD = String.valueOf(c.getProperty("activation_command"));
        REFRESH_ENABLED = Boolean.valueOf(c.getProperty("refresh_enabled"));
        EDITOR_ENABLED = Boolean.valueOf(c.getProperty("editor_enabled"));
        REFRESH_CMD = String.valueOf(c.getProperty("refresh_command"));
        EDITOR_CMD = String.valueOf(c.getProperty("editor_command"));
        DEFAULT_HEADLINE_SIZE = Integer.parseInt(c.getProperty("text_size_headline"));
        DEFAULT_MENUITEM_SIZE = Integer.parseInt(c.getProperty("text_size_menuitem"));
        DEFAULT_TEXT_SIZE = Integer.parseInt(c.getProperty("text_size_text"));
        MENUITEM_BULLETS = Boolean.valueOf(c.getProperty("menuitem_bullets"));
        VERSION_CHECKING = Boolean.valueOf(c.getProperty("version_check_enabled"));
        VERSION_ANNOUNCEMENT = Boolean.valueOf(c.getProperty("version_announcement_enabled"));

        rwdoc.RWDOC_LIBRARY = new RwdocLibrary();

        if (rwdoc.c != null)
        {
            success = true;
        }
        else {
            rwdebug(2, "Failed to load config & failed to create a new config file.");
        }

        COMPATIBLE = compatibilityCheck();
        if(!COMPATIBLE)
        {
            rwdebug(2, "rwDoc requires your server software to be updated. Things may break!");
        }

        UPTODATE = versionCheck(); // players will be notified on login if notifications enabled
        if(!UPTODATE)
        {
            rwdebug(2, "A newer version of rwDoc is available!");
        } else { rwdebug(3, "rwDoc is up to date."); }

        return success;
    }

    public static boolean cmpF(Float float1, Float float2)
    {
        boolean result;
        if (Math.abs(float1 - float2) < THRESHOLD)
            result = true;
        else
            result = false;
        return result;
    }

    private static boolean versionCheck()
    {
        boolean upToDate = false;
        Properties p = getVersionFile();
        int test = versionCompare(p.getProperty("plugin_version"), PLUGIN_VER);
        if(test > 0)
        {
            // a newer version of this plugin is available
            upToDate = false;
        } else { upToDate = true; }
        return upToDate;
    }

    private static boolean compatibilityCheck()
    {
        boolean compatible = false;
        String gameVersion = plugin.getServer().getVersion();
        int test = versionCompare(REQUIRED_GAME_VER, gameVersion);
        if (test >= 0)
        {
            // RequiredGameVersion is newer than the current server version
            compatible = false;
        } else { compatible = true; }
        return compatible;
    }

    public static int versionCompare(String str1, String str2) {
        // returns 0 if input is the same
        // returns 1 if str1 is higher
        // returns -1 is str2 is higher
        try (Scanner s1 = new Scanner(str1);
             Scanner s2 = new Scanner(str2);) {
            s1.useDelimiter("\\.");
            s2.useDelimiter("\\.");

            while (s1.hasNextInt() && s2.hasNextInt()) {
                int v1 = s1.nextInt();
                int v2 = s2.nextInt();
                if (v1 < v2) {
                    return -1;
                } else if (v1 > v2) {
                    return 1;
                }
            }

            if (s1.hasNextInt() && s1.nextInt() != 0)
                return 1; //str1 has an additional lower-level version number
            if (s2.hasNextInt() && s2.nextInt() != 0)
                return -1; //str2 has an additional lower-level version

            return 0;
        } // end of try-with-resources
    }

    private static Properties getVersionFile()
    {
        Properties p = new Properties();
        try {
            URL url = new URL("https://raw.githubusercontent.com/trevorjd/rwdoc/master/src/version.properties?raw=true");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestMethod("GET");
            int status = con.getResponseCode();

            if (con != null)
            {
                Reader reader = new InputStreamReader(con.getInputStream(), "UTF-8"); // for example
                try {
                    p.load(reader);
                } finally {
                    reader.close();
                }
            }
            con.disconnect();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return p;
    }

    public static void rwdebug(int logLevel, String message)
    {
        // Log levels
        // 1 - fatal errors
        // 2 - general errors
        // 3 - debugging
        // 4 - fine debugging
        String LOG_LEVEL = "";
        switch (logLevel)
        {
            case 1 :
                LOG_LEVEL = "FATAL";
                break;
            case 2 :
                LOG_LEVEL = "ERROR";
                break;
            case 3 :
                LOG_LEVEL = "DEBUG";
                break;
            case 4 :
                LOG_LEVEL = "FINE";
                break;
        }

        if (logLevel <= LOGLEVEL) { System.out.println("rwdoc " + LOG_LEVEL + " " + message);}
    }

    private static final String newline = System.getProperty("line.separator");

    public static String wordWrap(String text, int fontSize) {
        // calculate wrapping width based on font size
        int linelength;
        if (fontSize <= 10) { linelength = Integer.parseInt(c.getProperty("font_10pt_wrap_length")); } else
        if (fontSize <= 15) { linelength = Integer.parseInt(c.getProperty("font_15pt_wrap_length")); } else
        if (fontSize <= 20) { linelength = Integer.parseInt(c.getProperty("font_20pt_wrap_length")); } else
        if (fontSize <= 25) { linelength = Integer.parseInt(c.getProperty("font_25pt_wrap_length")); } else
        if (fontSize <= 30) { linelength = Integer.parseInt(c.getProperty("font_30pt_wrap_length")); } else
        if (fontSize <= 35) { linelength = Integer.parseInt(c.getProperty("font_35pt_wrap_length")); } else
        if (fontSize <= 40) { linelength = Integer.parseInt(c.getProperty("font_40pt_wrap_length")); } else
        if (fontSize <= 45) { linelength = Integer.parseInt(c.getProperty("font_45pt_wrap_length")); } else
        if (fontSize <= 50) { linelength = Integer.parseInt(c.getProperty("font_50pt_wrap_length")); } else
        if (fontSize <= 55) { linelength = Integer.parseInt(c.getProperty("font_55pt_wrap_length")); } else
        if (fontSize <= 60) { linelength = Integer.parseInt(c.getProperty("font_60pt_wrap_length")); } else
        if (fontSize <= 65) { linelength = Integer.parseInt(c.getProperty("font_65pt_wrap_length")); } else
        if (fontSize <= 70) { linelength = Integer.parseInt(c.getProperty("font_70pt_wrap_length")); } else
            linelength = 5;
        // Replace tabs with newlines because tabs mess up GuiLabels
        if(text.length() > 0)
        {
            text.replace('\t', '\n');
        }
        // Trim leading newlines and spaces
        while((text.length() > 0) && (text.charAt(0) == ' ' || text.charAt(0) == '\n'))
            text = text.substring(1);

        // If Small Enough Already, Return Original
        if(text.length() < linelength)
            return text;

        // If Next length Contains Newline, Split There
        if(text.substring(0, linelength).contains(newline))
            return text.substring(0, text.indexOf(newline)).trim() + newline +
                    wordWrap(text.substring(text.indexOf(newline) + 1), fontSize);

        // Otherwise, Split Along Nearest Previous Space/Tab/Dash
        int spaceIndex = Math.max(Math.max( text.lastIndexOf(" ",  linelength),
                text.lastIndexOf("\t", linelength)),
                text.lastIndexOf("-",  linelength));

        // If No Nearest Space, Split At length
        if(spaceIndex == -1)
            spaceIndex = linelength;

        // Split
        return text.substring(0, spaceIndex).trim() + newline + wordWrap(text.substring(spaceIndex), fontSize);
    }
}
