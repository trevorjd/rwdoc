package com.trevorjd.rwplugin;

import net.risingworld.api.Plugin;
import net.risingworld.api.utils.ImageInformation;

import java.io.File;

import static com.trevorjd.rwplugin.rwdoc.*;

public class rwdocUtils
{

    // private static RwdocLibrary library;

    protected static boolean initPlugin(Plugin plugin)
    {
        rwdebug(3, "Initialising plugin.");
        boolean success = false;
        rwdoc.c = handlerProperties.getConfig();

        BGIMAGE = new ImageInformation(plugin.getPath() + IMGDIR + "bgImage.png");
        HITBOXIMAGE = new ImageInformation(plugin.getPath() + IMGDIR + "hitbox.png");
        rwdebug(3, "pluginfolder = " + plugin.getPath());
        PLUGINSFOLDER = plugin.getPath().substring(0,plugin.getPath().lastIndexOf(File.separator));
        rwdoc.rwdocLibrary = new RwdocLibrary();

        if (rwdoc.c != null)
        {
            success = true;
        }
        else {
            rwdebug(2, "Failed to load config & failed to create a new config file.");
        }
        return success;
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
    // private static final String newline = "\n";

    public static String wordWrap(String text, int fontSize) {
        // calculate wrapping width based on font size
        int linelength;
        if (fontSize <= 10) { linelength = 112; } else
        if (fontSize <= 15) { linelength = 76; } else
        if (fontSize <= 20) { linelength = 55; } else
        if (fontSize <= 25) { linelength = 40; } else
        if (fontSize <= 30) { linelength = 38; } else
        if (fontSize <= 35) { linelength = 33; } else
        if (fontSize <= 40) { linelength = 29; } else
        if (fontSize <= 45) { linelength = 26; } else
        if (fontSize <= 50) { linelength = 23; } else
            linelength = 20;

        //:: Trim
        while(text.length() > 0 && (text.charAt(0) == '\t' || text.charAt(0) == ' '))
            text = text.substring(1);

        //:: If Small Enough Already, Return Original
        if(text.length() < linelength)
            return text;

        //:: If Next length Contains Newline, Split There
        if(text.substring(0, linelength).contains(newline))
            return text.substring(0, text.indexOf(newline)).trim() + newline +
                    wordWrap(text.substring(text.indexOf(newline) + 1), fontSize);

        //:: Otherwise, Split Along Nearest Previous Space/Tab/Dash
        int spaceIndex = Math.max(Math.max( text.lastIndexOf(" ",  linelength),
                text.lastIndexOf("\t", linelength)),
                text.lastIndexOf("-",  linelength));

        //:: If No Nearest Space, Split At length
        if(spaceIndex == -1)
            spaceIndex = linelength;

        //:: Split
        return text.substring(0, spaceIndex).trim() + newline + wordWrap(text.substring(spaceIndex), fontSize);
    }
}
