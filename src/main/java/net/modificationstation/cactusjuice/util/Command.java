package net.modificationstation.cactusjuice.util;

import lombok.Getter;
import net.modificationstation.cactusjuice.Config;
import net.modificationstation.cactusjuice.Main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Command {
    @Getter private ProcessBuilder processBuilder = new ProcessBuilder();
    @Getter private Process process;

    public Command(String command) {
        if (command.isEmpty()) {
            Main.getLogger().severe("Given command was empty!");
            return;
        }
        Main.getLogger().info(Arrays.toString(command.split("\\b(?!\\\\ ) ")));
        processBuilder.command(command.split("\\b(?!\\\\ ) "));
        processBuilder.inheritIO();
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(new File(Config.Dirs.ROOT));
    }

    public void run() throws IOException {
        process = processBuilder.start();
    }

    public void stop() {
        process.destroy();
    }

    public void waitFor() throws InterruptedException {
        process.waitFor();
    }
}
