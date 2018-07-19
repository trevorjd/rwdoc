package com.trevorjd.rwplugin;

import net.risingworld.api.Plugin;

public class rwdocUtils
{
    private static final String newline = System.getProperty("line.separator");

    protected static boolean initPlugin(Plugin plugin)
    {
        System.out.println("rwdoc Debug: initialising plugin.");
        boolean success = false;
        rwdoc.c = handlerProperties.getConfig();

        if (rwdoc.c != null)
        {
            success = true;
        }
        else {
            System.out.println("rwdoc: Failed to load config & failed to create a new config file. Expect errors!");
        }
        return success;
    }

    protected static int calcWrapLength()
    {
        int result = 48;
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

    public static String stripNewlines(String in)
    {
        String result = null;
        result = in.replace(newline, "");
        return result;
    }
}
