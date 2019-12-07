package net.modificationstation.cactusjuice;

import java.io.File;
import java.util.HashMap;

public class Config {

    public static String cactusjuicePath = getCactusjuicePath();

    public static String os = getOS();

    /**
     * Used by JSON-IO to make output JSON strings look like actual JSON.
     */
    public static HashMap prettyprint = new HashMap() {{
        put("PRETTY_PRINT", true);
        put("TYPE", false);
    }};

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
