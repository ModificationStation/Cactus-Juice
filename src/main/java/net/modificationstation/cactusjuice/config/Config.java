package net.modificationstation.cactusjuice.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.modificationstation.cactusjuice.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Set;

public class Config {

    public static GsonBuilder gsonBuilder;

    public static Directories directories = new Directories();

    public static final boolean hasConsole = System.console() != null;

    public static String cactusjuicePath = getCactusjuicePath();

    public static String os = getOS();

    /**
     * Used by JSON-IO to make output JSON strings look like actual JSON.
     */
    public static HashMap prettyprint = new HashMap() {{
        put("PRETTY_PRINT", true);
        put("TYPE", false);
    }};

    public static void loadCJConfigFile() throws FileNotFoundException {
        gsonBuilder = (new GsonBuilder()).excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
        Gson gson = gsonBuilder.create();
        File file = new File("cactusjuice.json");
        Reader reader = new FileReader(file);
        JsonObject configObj = (JsonObject) JsonParser.parseReader(reader);
        Set keys = configObj.keySet();
        for (Object key : keys) {
            try {
                Class cls = Class.forName("net.modificationstation.cactusjuice.config." + key);
                gson.fromJson(configObj.get((String) key), cls);
            } catch (Exception e) {
                Main.logger.info("Config class \"" + key + "\" not found!");
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * Gets the OS of the user.
     * @return "windows" | "osx" | "unix"
     */
    private static String getOS() {
        String os = (System.getProperty("os.name")).toLowerCase();
        if (os.contains("win")) {
            return "windows";
        } else if (os.contains("mac")) {
            return "osx";
        } else {
            return "linux";
        }
    }

    private static String getCactusjuicePath() {
        try {
            return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().replace("\\", "/");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}