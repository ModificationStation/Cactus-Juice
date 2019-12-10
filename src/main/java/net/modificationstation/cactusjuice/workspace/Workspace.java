package net.modificationstation.cactusjuice.workspace;

import net.modificationstation.cactusjuice.config.Directories;

import java.io.File;

public class Workspace {

    public static void setupWorkspace() {
        File workspaceDir = new File(Directories.getDirWorkspace());
        workspaceDir.mkdirs();
        try {
            SetupWorkspace.create();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
