package com.trevorjd.rwplugin;

import net.risingworld.api.Plugin;
import net.risingworld.api.utils.ImageInformation;

import java.io.File;

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

        rwdoc.RWDOC_LIBRARY = new RwdocLibrary();

        if (rwdoc.c != null)
        {
            success = true;
        }
        else {
            rwdebug(2, "Failed to load config & failed to create a new config file.");
        }
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
