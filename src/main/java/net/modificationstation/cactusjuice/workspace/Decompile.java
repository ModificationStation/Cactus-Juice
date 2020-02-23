package net.modificationstation.cactusjuice.workspace;

import net.glasslauncher.legacy.util.FileUtils;
import net.modificationstation.cactusjuice.Config;
import net.modificationstation.cactusjuice.Main;
import net.modificationstation.cactusjuice.util.Command;

import java.io.File;
import java.util.HashMap;

public class Decompile {

    public static void decompile() {
        decompileSide(0);
    }

    public static void decompileSide(int side) {
        File[] srcDir = new File[]{
                new File(Config.Output.SRC_CLIENT),
                new File(Config.Output.SRC_SERVER),
        };
        File[] jars = new File[]{
                new File(Config.Jar.CLIENT),
                new File(Config.Jar.SERVER),
        };
        File[] mappingOutput = new File[]{
                new File(Config.Decomp.CLIENT_DEOBF),
                new File(Config.Decomp.SERVER_DEOBF)
        };
        String[] sideName = new String[]{
                "Client",
                "Server",
        };

        if (!(new File(Config.Dirs.SRC).exists()) && (new File(Config.Dirs.JARS)).exists()) {
            Main.getLogger().info("Decompiling " + sideName[side]);
            if (jars[side].exists()) {
                Main.getLogger().info("Running FernFlower...");
                runFF(jars[side], new File(Config.Dirs.TEMP));
                //applyTinyMap(jars[side], mappingOutput[side]);
                Main.getLogger().info("Done!");
            }
        }
    }

    public static void applyTinyMap(File inputJar, File outputJar) {

        String command = Config.Cmds.parseCmd(Config.Cmds.CMD_TINY_MAPPER, new HashMap<String, String>(){{
            put("jarin", inputJar.getPath());
            put("jarout", outputJar.getPath());
        }});

        Command process = new Command(command);
        try {
            process.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runFF(File inputJar, File outputDir) {

        String command = Config.Cmds.parseCmd(Config.Cmds.CMD_FERNFLOWER, new HashMap<String, String>(){{
            put("jarin", inputJar.getPath());
            put("jarout", outputDir.getPath());
        }});

        Command process = new Command(command);
        try {
            process.run();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileUtils.extractZip(outputDir + "/" + inputJar.getName(), outputDir + "/" + inputJar.getName().replaceFirst("[.][^.]+$", "") + "_decomp");
    }
}
