package net.modificationstation.cactusjuice.workspace;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import net.glasslauncher.legacy.util.FileUtils;
import net.modificationstation.cactusjuice.Config;
import net.modificationstation.cactusjuice.Main;
import net.modificationstation.cactusjuice.jsontemplate.DependencyNatives;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Workspace {

    public static void setupWorkspace() {
        Main.getLogger().info("Java version: " + System.getProperty("java.version"));
        if (new File(Config.Dirs.SRC).exists()) {
            Main.getLogger().severe("Directory \"src\" already exists! Run cleanup if you want a new workspace!");
            return;
        }
        Main.getLogger().info("Setting up your workspace...");
        try {
            makeDirs();
            getJars();
            setMCVersion();
            Main.getLogger().info("Done!");
        } catch (Exception e) {
            Main.getLogger().info("Setup encountered an exception! Aborting.");
            cleanup();
            e.printStackTrace();
        }
    }

    private static void makeDirs() {
        ArrayList<String> dirs = new ArrayList<String>(){{
            add(Config.Dirs.TEMP);
            add(Config.Dirs.NATIVES);
        }};

        for (String dir : dirs) {
            new File(dir).mkdirs();
        }
    }

    private static void getJars() throws NoSuchFieldException, IllegalAccessException {
        FileUtils.downloadFile(Config.Deps.JINPUT, Config.Dirs.JARS + "/bin", Config.Deps.JINPUT_HASH, "jinput.jar");
        FileUtils.downloadFile(Config.Deps.LWJGL, Config.Dirs.JARS + "/bin", Config.Deps.LWJGL_HASH, "lwjgl.jar");
        FileUtils.downloadFile(Config.Deps.LWJGL_UTIL, Config.Dirs.JARS + "/bin", Config.Deps.LWJGL_UTIL_HASH, "lwjgl_util.jar");

        DependencyNatives natives = (DependencyNatives) Config.Deps.class.getField(Config.OS.toUpperCase()).get(null);
        FileUtils.downloadFile(natives.getJinputNatives(), Config.Dirs.TEMP + "/natives", natives.getJinputNativesHash(), "jinput.zip");
        FileUtils.downloadFile(natives.getLwjglNatives(), Config.Dirs.TEMP + "/natives", natives.getLwjglNativesHash(), "lwjgl.zip");

        FileUtils.extractZip(Config.Dirs.TEMP + "/natives/jinput.zip", Config.Dirs.NATIVES);
        FileUtils.extractZip(Config.Dirs.TEMP + "/natives/lwjgl.zip", Config.Dirs.NATIVES);

        FileUtils.delete((new File(Config.Dirs.TEMP + "/natives")));
    }

    public static void cleanup() {
        ArrayList<String> dirs = new ArrayList<String>(){{
            add(Config.Dirs.TEMP);
            add(Config.Dirs.NATIVES);
            add(Config.Dirs.JARS);
            add(Config.Dirs.LOGS);
            add(Config.Dirs.REOBF);
            add(Config.Dirs.SRC);
            add(Config.Dirs.CONF);
            add(Config.Dirs.BIN);
        }};

        for (String dir : dirs) {
            File dirFile = new File(dir);
            if (dirFile.exists()) {
                FileUtils.delete(dirFile);
            }
        }
    }

    private static void setMCVersion() throws Exception {
        CodeSource src = Main.class.getProtectionDomain().getCodeSource();

        HashMap<String, LinkedTreeMap<String, LinkedTreeMap<String, String>>> mcVersions = (new Gson()).fromJson(FileUtils.readFile(src.getLocation() + "!/workspace/conf/versions.json", true), HashMap.class);
        ArrayList<String> versions = new ArrayList<>();

        URL jar = src.getLocation();
        ZipInputStream zip = new ZipInputStream( jar.openStream());
        ZipEntry ze = null;

        while((ze = zip.getNextEntry()) != null) {
            String entryName = ze.getName();
            if(ze.isDirectory() && entryName.startsWith("workspace/conf") && !ze.getName().contains("patches")) {
                versions.add((new File(entryName).getName()));
            }
        }

        StringBuilder versionStringBuilder = new StringBuilder();
        for (String version : versions) {
            versionStringBuilder.append(version).append(", ");
        }
        String versionString = versionStringBuilder.toString().substring(0, versionStringBuilder.length() - 2);

        Main.getLogger().info("If you wish to supply your own configuration, type \"none\".");
        Main.getLogger().info("Any two versions joined by a comma (b1.5_01,1.5_02) are client vs server version.");
        Main.getLogger().info("Only b1.7.3 is \"officially\" supported as of now.");

        String input = "";
        while (!versions.contains(input)) {
            Main.getLogger().info("Current versions are: " + versionString);
            Main.getLogger().info("What version would you like to install?");
            if (Config.HAS_CONSOLE) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                input = reader.readLine();
            } else {
                input = JOptionPane.showInputDialog(null, "Enter the version you would like to use. See console for more information.", "Select a Version", JOptionPane.QUESTION_MESSAGE);
            }
        }

        Main.getLogger().info("Downloading client and server jars for \"" + input + "\"...");
        FileUtils.downloadFile(mcVersions.get("client").get(input).get("url"), Config.Dirs.JARS + "/bin", null, "minecraft.jar");
        FileUtils.downloadFile(mcVersions.get("server").get(input).get("url"), Config.Dirs.JARS, null, "minecraft_server.jar");
        Files.copy(Main.class.getResourceAsStream("/workspace/jars/server.properties"), Paths.get(Config.Dirs.JARS + "/server.properties"), StandardCopyOption.REPLACE_EXISTING);

        FileUtils.copyResourcesRecursively(Main.class.getResource("/workspace/conf/" + input), new File(Config.Dirs.CONF));
    }

}
