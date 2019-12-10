package net.modificationstation.cactusjuice.workspace;

import net.modificationstation.cactusjuice.config.Directories;

import java.io.File;

public class Workspace {
    static File workspaceDir;

    public Workspace() {
        workspaceDir = new File(Directories.dirWorkspace);
    }

    public static void setupWorkspace() {
        workspaceDir.mkdirs();
        try {
            SetupWorkspace.create();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
