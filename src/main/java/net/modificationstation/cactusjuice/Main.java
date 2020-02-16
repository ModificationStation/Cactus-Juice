package net.modificationstation.cactusjuice;

import lombok.Getter;
import net.glasslauncher.legacy.ConsoleWindow;
import net.glasslauncher.legacy.util.Classpath;
import net.glasslauncher.legacy.util.FileUtils;
import net.modificationstation.cactusjuice.workspace.Workspace;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.*;

public class Main {
    @Getter private static Logger logger = Logger.getLogger("cactusjuice");

    private static ArrayList<String> libs = new ArrayList<>();

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!Config.HAS_CONSOLE) {
            ConsoleWindow console = new ConsoleWindow();
        }
        makeLogger();
        getDeps();

        for (Object lib : libs.toArray()) {
            try {
                Classpath.addFile("lib/" + lib);
            } catch (Exception e) {
                getLogger().info("Failed to load \"" + lib + "\".");
                e.printStackTrace();
            }
        }

        if (args.length == 0 && !Config.HAS_CONSOLE) {
            String input = JOptionPane.showInputDialog(null, "Enter your arguments. Arguments must be separated by a space.", "Confirmation", JOptionPane.WARNING_MESSAGE);
            if (input == null) {
                getLogger().info("No arguments provided. Aborting.");
                return;
            }
            args = input.split(" ");
        }

        start(args);
    }

    private static void makeLogger() {
        try {
            System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tT] [CactJ] [%4$s] %5$s %n");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            LocalDateTime now = LocalDateTime.now();
            String time = dtf.format(now);
            File logdir = new File(Config.Dirs.ROOT + "/cactj-logs");
            logdir.mkdirs();
            Handler file_handler = new FileHandler(Config.Dirs.ROOT + "/cactj-logs/" + time + ".log");
            SimpleFormatter format = new SimpleFormatter();
            getLogger().addHandler(file_handler);
            file_handler.setFormatter(format);
            getLogger().setLevel(Level.ALL);
            file_handler.setLevel(Level.ALL);
            getLogger().info("Logging to " + logdir.toString());
            getLogger().info("Java version: " + System.getProperty("java.version"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Downloads all dependencies listed in Config.Deps.CACTUS_DEPS.
     */
    private static void getDeps() {
        getLogger().info("Checking dependencies...");
        HashMap<String, String> deps = Config.Deps.CACTUS_DEPS;

        for (String dep : deps.keySet()) {
            try {
                FileUtils.downloadFile(dep, Config.Dirs.LIB, deps.get(dep));
                libs.add(dep.substring(dep.lastIndexOf('/') + 1));
            } catch (Exception e) {
                getLogger().info("Failed to download dependency: " + dep);
                e.printStackTrace();
            }
        }
    }

    private static void start(String[] a) {
        getLogger().info("Welcome to Cactus Juice!");
        try {
            getLogger().info(Config.Dirs.ROOT);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        getLogger().info(Arrays.toString(a));
        List args = Arrays.asList(a);
        if (args.contains("setupcj")) {
            Workspace.setupWorkspace();
        }
        else if (args.contains("cleanup")) {
            String input = "";
            getLogger().info("This script will delete your workspace and set most of it to factory defaults.");
            getLogger().info("Are you sure you want to clean up your workspace? [y/N]");
            if (Config.HAS_CONSOLE) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                try {
                    input = reader.readLine();
                } catch (Exception e) {
                    getLogger().info("An exception has occurred. Exiting.");
                    e.printStackTrace();
                    System.exit(1);
                }
            } else {
                input = JOptionPane.showInputDialog(null, "Are you sure? See console for more information.", "Confirmation", JOptionPane.WARNING_MESSAGE);
            }
            if (input.toLowerCase().equals("yes") || input.toLowerCase().equals("y")) {
                Workspace.cleanup();
            } else {
                getLogger().info("Aborting.");
                System.exit(0);
            }
            getLogger().info("Done!");
        } else {
            getLogger().info("No valid launch args. Exiting.");
        }
        System.exit(0);
    }
}