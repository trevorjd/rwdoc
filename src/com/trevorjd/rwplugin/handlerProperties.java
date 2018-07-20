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
        config.setProperty("guiSIZE_X", "0.85");
        config.setProperty("guiSIZE_Y", "0.85");
        config.setProperty("guiFontColor", p.getProperty("guiFontColor"));
        config.setProperty("guiBorderColor", p.getProperty("guiBorderColor"));
        config.setProperty("guiLabelColor", p.getProperty("guiLabelColor"));
        config.setProperty("imageNavLeft", p.getProperty("imageNavLeft"));
        config.setProperty("imageNavRight", p.getProperty("imageNavRight"));
        config.setProperty("imageNavUp", p.getProperty("imageNavUp"));
        config.setProperty("imageButtonClose", p.getProperty("imageButtonClose"));
        config.setProperty("bgImageJournal", p.getProperty("bgImageJournal"));
        config.setProperty("bgImageJournal_Left", p.getProperty("bgImageJournal_Left"));
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
        p.setProperty("rwdocCommand", "help");
        p.setProperty("guiFontColor", "0xffffffff");
        p.setProperty("guiBorderColor", "0x07070710");
        p.setProperty("guiLabelColor", "0x07FF0710");
        p.setProperty("rwdocImgLoc", "images");
        p.setProperty("imageNavLeft", "hitbox.png");
        p.setProperty("imageNavRight", "hitbox.png");
        p.setProperty("imageNavUp", "hitbox.png");
        p.setProperty("imageButtonClose", "hitbox.png");
        p.setProperty("bgImageJournal", "journal.png");
        p.setProperty("bgImageJournal_Left", "journal_left.png");

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
