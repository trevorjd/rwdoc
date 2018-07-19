package com.trevorjd.rwplugin;

import java.io.*;
import java.util.Properties;

import static com.trevorjd.rwplugin.rwdoc.plugin;

public class handlerProperties
{

    protected static Properties getConfig()
    {
        System.out.println("rwdoc Debug: Getting config.");
        Properties config = new Properties();
        Properties p = loadConfig();

        config.setProperty("rwdocDocsDir", "/" + p.getProperty("rwdocDocsDir"));
        config.setProperty("rwdocCommand", "/" + p.getProperty("rwdocCommand"));
        config.setProperty("rwdocImgLoc", plugin.getPath() + "/" + p.getProperty("rwdocImgLoc") + "/");
        config.setProperty("guiPOS_X", p.getProperty("guiPOS_X"));
        config.setProperty("guiPOS_Y", p.getProperty("guiPOS_Y"));
        config.setProperty("guiSIZE_X", p.getProperty("guiSIZE_X"));
        config.setProperty("guiSIZE_Y", p.getProperty("guiSIZE_Y"));
        config.setProperty("guiTitle", p.getProperty("guiTitle"));
        config.setProperty("guiFontColor", p.getProperty("guiFontColor"));
        config.setProperty("guiBorderColor", p.getProperty("guiBorderColor"));
        config.setProperty("guiLabelColor", p.getProperty("guiLabelColor"));
        config.setProperty("imageNavLeft", p.getProperty("imageNavLeft"));
        config.setProperty("imageNavRight", p.getProperty("imageNavRight"));
        config.setProperty("imageNavUp", p.getProperty("imageNavUp"));
        config.setProperty("imageButtonUp", p.getProperty("imageButtonUp"));
        config.setProperty("imageButtonDown", p.getProperty("imageButtonDown"));
        config.setProperty("imageButtonClose", p.getProperty("imageButtonClose"));
        config.setProperty("imageButtonFontScaleUp", p.getProperty("imageButtonFontScaleUp"));
        config.setProperty("imageButtonFontScaleDown", p.getProperty("imageButtonFontScaleDown"));
        return config;
    }

    protected static boolean writeConfigFile(Properties properties, String filename, String comment)
    {
        boolean success = false;
        System.out.println("rwdoc Debug: Writing config.");
        try {
            File file = new File(plugin.getPath() + "/" + filename);
            FileOutputStream out = new FileOutputStream(file);
            properties.store(out,comment);
            out.close();
            success = true;
        } catch (IOException e) { e.printStackTrace(); }

        return success;
    }

    private static Properties getDefaultConfig()
    {
        System.out.println("rwdoc Debug: Creating default config.");
        Properties p = new Properties();
        p.setProperty("rwdocDocsDir", "docs");
        p.setProperty("rwdocCommand", "help");
        p.setProperty("guiPOS_X", "0.5");
        p.setProperty("guiPOS_Y", "0.5");
        p.setProperty("guiSIZE_X", "0.5");
        p.setProperty("guiSIZE_Y", "0.5");
        p.setProperty("guiTitle", "rwdoc - Documentation for a Rising World");
        p.setProperty("guiFontColor", "0xffffffff");
        p.setProperty("guiBorderColor", "0x07070710");
        p.setProperty("guiLabelColor", "0x07FF0710");
        p.setProperty("rwdocImgLoc", "images");
        p.setProperty("imageNavLeft", "arrow_left.png");
        p.setProperty("imageNavRight", "arrow_right.png");
        p.setProperty("imageNavUp", "arrow_up.png");
        p.setProperty("imageButtonUp", "arrow_up_small.png");
        p.setProperty("imageButtonDown", "arrow_down_small.png");
        p.setProperty("imageButtonClose", "image_close.png");
        p.setProperty("imageButtonFontScaleUp", "fontScaleUp.png");
        p.setProperty("imageButtonFontScaleDown", "fontScaleDown.png");

        return p;
    }

    protected static Properties loadConfig()
    {
        // test for existence of setting.properties file, write a new one if needed
        Properties p = new Properties();
        File configFile = new File(plugin.getPath() + "/settings.properties");
        if (!configFile.exists())
        {
            System.out.println("rwdoc: Config file not present. Writing a new one with default values.");
            writeConfigFile(getDefaultConfig(), "settings.properties", "rwdoc config");
        }

        // load in the new config
        InputStream input = null;
        FileInputStream in;
        try {
            input = new FileInputStream(configFile);
            p.load(input);
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally
        {
            if (input != null)
            {
                System.out.println("rwdoc Debug: We're good. We have a file.");
                try
                {
                    input.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return p;
    }
}
