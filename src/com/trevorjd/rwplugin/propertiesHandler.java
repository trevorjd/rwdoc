package com.trevorjd.rwplugin;

import java.io.*;
import java.util.Properties;

import static com.trevorjd.rwplugin.rwdoc.plugin;
import static com.trevorjd.rwplugin.rwdocUtils.rwdebug;

public class propertiesHandler
{

    protected static Properties getConfig()
    {
        rwdebug(3, "initialising config");
        Properties config = new Properties();
        Properties p = loadConfig();
        config.setProperty("activation_command", "/" + cleanProperty(p,"activation_command"));
        config.setProperty("refresh_command", "/" + cleanProperty(p,"refresh_command"));
        config.setProperty("editor_command", "/" + cleanProperty(p, "editor_command"));
        config.setProperty("refresh_enabled", cleanProperty(p,"refresh_enabled"));
        config.setProperty("editor_enabled", cleanProperty(p, "editor_enabled"));
        config.setProperty("search_all_plugins", cleanProperty(p,"search_all_plugins"));
        config.setProperty("log_level", cleanProperty(p,"log_level"));
        config.setProperty("msg_no_content", cleanProperty(p,"msg_no_content"));
        config.setProperty("msg_no_default_document", cleanProperty(p,"msg_no_default_document"));
        config.setProperty("msg_editor_mode", cleanProperty(p,"msg_editor_mode"));
        config.setProperty("msg_server_outdated", cleanProperty(p,"msg_server_outdated"));
        config.setProperty("msg_plugin_outdated", cleanProperty(p,"msg_plugin_outdated"));
        config.setProperty("msg_library_refreshed", cleanProperty(p,"msg_library_refreshed"));
        config.setProperty("msg_untitled", cleanProperty(p,"msg_untitled"));
        config.setProperty("version_check_enabled", cleanProperty(p,"version_check_enabled"));
        config.setProperty("version_announcement_enabled", cleanProperty(p,"version_announcement_enabled"));
        config.setProperty("font_10pt_wrap_length", cleanProperty(p,"font_10pt_wrap_length"));
        config.setProperty("font_15pt_wrap_length", cleanProperty(p,"font_15pt_wrap_length"));
        config.setProperty("font_20pt_wrap_length", cleanProperty(p,"font_20pt_wrap_length"));
        config.setProperty("font_25pt_wrap_length", cleanProperty(p,"font_25pt_wrap_length"));
        config.setProperty("font_30pt_wrap_length", cleanProperty(p,"font_30pt_wrap_length"));
        config.setProperty("font_35pt_wrap_length", cleanProperty(p,"font_35pt_wrap_length"));
        config.setProperty("font_40pt_wrap_length", cleanProperty(p,"font_40pt_wrap_length"));
        config.setProperty("font_45pt_wrap_length", cleanProperty(p,"font_45pt_wrap_length"));
        config.setProperty("font_50pt_wrap_length", cleanProperty(p,"font_50pt_wrap_length"));
        config.setProperty("font_55pt_wrap_length", cleanProperty(p,"font_55pt_wrap_length"));
        config.setProperty("font_60pt_wrap_length", cleanProperty(p,"font_60pt_wrap_length"));
        config.setProperty("font_65pt_wrap_length", cleanProperty(p,"font_65pt_wrap_length"));
        config.setProperty("font_70pt_wrap_length", cleanProperty(p,"font_70pt_wrap_length"));
        config.setProperty("text_size_headline", cleanProperty(p,"text_size_headline"));
        config.setProperty("text_size_menuitem", cleanProperty(p,"text_size_menuitem"));
        config.setProperty("text_size_text", cleanProperty(p,"text_size_text"));
        config.setProperty("menuitem_bullets", cleanProperty(p,"menuitem_bullets"));

        return config;
    }

    protected static boolean writeConfigFile(Properties properties, String filename, String comment)
    {
        boolean success = false;
        try {
            File file = new File(plugin.getPath() + "/" + filename);
            FileOutputStream out = new FileOutputStream(file);
            properties.store(out,comment);
            out.close();
            success = true;
        } catch (IOException e) { e.printStackTrace(); }

        return success;
    }

    private static String cleanProperty(Properties properties, String property)
    {
        String result;
        Properties defProp = getDefaultConfig();
        if (properties.getProperty(property) != null)
        {
            result = properties.getProperty(property);
        } else
            {
                rwdebug(2,"settings.properties is missing tag: " + property);
                result = defProp.getProperty(property); }
        return result;
    }

    private static Properties getDefaultConfig()
    {
        Properties p = new Properties();
        p.setProperty("activation_command", "help");
        p.setProperty("refresh_command", "rwdocrefresh");
        p.setProperty("search_all_plugins", "true");
        p.setProperty("log_level", "2");

        p.setProperty("editor_command", "rwdoceditor");
        p.setProperty("editor_enabled", "true");
        p.setProperty("refresh_enabled", "true");
        p.setProperty("msg_no_content", "This page has no content.");
        p.setProperty("msg_no_default_document", "No default document found.");
        p.setProperty("msg_editor_mode", "Toggled editor highlighting.");
        p.setProperty("msg_library_refreshed", "rwDoc - Library refreshed.");
        p.setProperty("msg_server_outdated", "rwDoc requires your server software to be updated. Things may break.");
        p.setProperty("msg_plugin_outdated", "A newer version of rwDoc is available.");
        p.setProperty("msg_untitled", "Untitled");

        p.setProperty("version_check_enabled", "true");
        p.setProperty("version_announcement_enabled", "true");

        p.setProperty("font_10pt_wrap_length", "112");
        p.setProperty("font_15pt_wrap_length", "76");
        p.setProperty("font_20pt_wrap_length", "55");
        p.setProperty("font_25pt_wrap_length", "40");
        p.setProperty("font_30pt_wrap_length", "38");
        p.setProperty("font_35pt_wrap_length", "33");
        p.setProperty("font_40pt_wrap_length", "29");
        p.setProperty("font_45pt_wrap_length", "26");
        p.setProperty("font_50pt_wrap_length", "23");
        p.setProperty("font_55pt_wrap_length", "18");
        p.setProperty("font_60pt_wrap_length", "15");
        p.setProperty("font_65pt_wrap_length", "12");
        p.setProperty("font_70pt_wrap_length", "9");
        p.setProperty("text_size_headline", "30");
        p.setProperty("text_size_menuitem", "40");
        p.setProperty("text_size_text", "20");

        p.setProperty("menuitem_bullets", "true");

        return p;
    }

    protected static Properties loadConfig()
    {
        rwdebug(3, "loading settings.properties");
        // test for existence of setting.properties file, write a new one if needed
        Properties p = new Properties();
        File configFile = new File(plugin.getPath() + "/settings.properties");
        if (!configFile.exists())
        {
            rwdebug(2, "rwdoc: Config file not present. Writing a new one with default values.");
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
