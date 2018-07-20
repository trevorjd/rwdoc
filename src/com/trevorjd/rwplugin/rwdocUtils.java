package com.trevorjd.rwplugin;

import net.risingworld.api.Plugin;

public class rwdocUtils
{


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



    public static String getBluePrintInfo()
    {
        String string = "To use a blueprint on Rosalia, you must have permission of the creator. If you don't, our server may be liable to real-life legal troubles.\n" +
                "\n" +
                "* Blueprints of your own work are OK, even if the work was created on another server.\n" +
                "* Blueprints downloaded from the internet are usually OK.\n" +
                "* Admins will remove any work created from a blueprint if we get a complaint about it.\n" +
                "* Players violating these rules multiple times may get banned.\n" +
                "\n" +
                "Valve's Notice of Copyright Infringement (which we are subject to)\n" +
                "\n" +
                "Valve respects the intellectual property rights of others, and we ask that everyone using our internet sites and services do the same. Anyone who believes that their work has been reproduced in one of our internet sites or services in a way that constitutes copyright infringement may notify Valve's copyright agent using the form below.\n" +
                "Submitting a claim of copyright infringement is a serious legal matter. Before you proceed, you might consider contacting the individual directly to address the complaint. It might be a simple misunderstanding and possible to address without involving proper legal process.\n" +
                "\n" +
                "Be aware that under Section 512(f) of the Digital Millennium Copyright Act, any person who knowingly materially misrepresents that material or activity is infringing may be liable for damages.\n" +
                "\n" +
                "Please also note that the information provided in this legal notice may be shared with third-parties or made public.";
        return string;
    }

    public static String getLoremIpsum()
    {
        String s = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.";
        return s;
    }

    public static String getDefaultWelcome()
    {
        String s = "Welcome to our server!";
        return s;
    }

    public static String getDefaultTitle()
    {
        String s = "rwDoc: Documentation for a Rising World";
        return s;
    }
}
