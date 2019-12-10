package net.modificationstation.cactusjuice.workspace;

import net.modificationstation.cactusjuice.Main;
import net.modificationstation.cactusjuice.config.Directories;

import java.io.File;
import java.io.FileNotFoundException;

public class SetupWorkspace {
    public static void create() throws FileNotFoundException {
        Main.getLogger().info("Creating workspace...");
        if ((new File(Directories.getDirWorkspace())).exists()) {
            Main.getLogger().info("Exists!");
        }
    }

    private static void makeConfig() {

    }
}
