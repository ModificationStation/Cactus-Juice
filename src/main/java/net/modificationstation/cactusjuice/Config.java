package net.modificationstation.cactusjuice;

import net.modificationstation.cactusjuice.jsontemplate.DependencyNatives;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public static final boolean HAS_CONSOLE = System.console() != null;
    public static final String OS = getOSString();

    /**
     * Gets the OS of the user.
     * @return "windows" | "osx" | "linux"
     */
    private static String getOSString() {
        String os = (System.getProperty("os.name")).toLowerCase();
        if (os.contains("win")) {
            return "windows";
        } else if (os.contains("mac")) {
            return "osx";
        } else {
            return "linux";
        }
    }

    public static class Cmds {
        public static final String FERNFLOWER = Dirs.RUNTIME + "/fernflower.jar";
        public static final String TINY_MAPPER = Dirs.RUNTIME + "/tiny-mapper-0.2.1-fat.jar";
        public static final String CMD_RECOMP_CLT = "%s -g -verbose -classpath \"" + Recomp.CLASSPATH_CLIENT + "\" -sourcepath {sourcepath} -d {outpath} {pkgs} {fixes}/*.java";
        public static final String CMD_RECOMP_SRV = "%s -g -verbose -classpath \"" + Recomp.CLASSPATH_SERVER + "\" -sourcepath {sourcepath} -d {outpath} {pkgs}";
        public static final String CMD_START_CLT = "%s -Xincgc -Xms1024M -Xmx1024M -cp \"" + Recomp.CLASSPATH_CLIENT + "\" -Djava.library.path={natives} Start";
        public static final String CMD_START_SRV = "%s -Xincgc -Xms1024M -Xmx1024M -cp \"" + Recomp.CLASSPATH_SERVER + "\" net.minecraft.server.MinecraftServer";
        public static final String CMD_FERNFLOWER = "%s -jar " + FERNFLOWER + " " + Decomp.FF_CONF + " {jarin} {jarout}";
        public static final String CMD_TINY_MAPPER = "%s -jar " + TINY_MAPPER + " {jarin} {jarout} {mappings} {frommap} {tomap}";

        public static String parseCmd(String command, Map<String, String> replaceMap) {
            command = command.replace("%s", System.getProperty("java.home") + "/bin/java");
            for (String key : replaceMap.keySet()) {
                command = command.replace("{" + key + "}", replaceMap.get(key).replaceAll(" ", "\\ "));
            }
            return command;
        }
    }

    public static class ConfFiles {
        public static final String MAPPINGS = Dirs.CONF + "/mappings.tinyfilev2";
        public static final String PATCH = Dirs.CONF + "/patches/vanilla.patch";
        public static final String PATCHES = Dirs.CONF + "/patches/optional";
    }

    public static class Decomp {
        public static final String FF_CONF = "-rbr=0 -dgs=1 -asc=1";
        public static final String CLIENT_SRC = Dirs.TEMP + "/minecraft_decomp";
        public static final String SERVER_SRC = Dirs.TEMP + "/minecraft_server_decomp";
        public static final String CLIENT_DEOBF = Dirs.TEMP + "/minecraft_deobf";
        public static final String SERVER_DEOBF = Dirs.TEMP + "/minecraft_server_deobf";
        public static final String SOURCE = "net";
    }

    public static class Deps {
        public static final DependencyNatives WINDOWS = new DependencyNatives(
                "https://repo1.maven.org/maven2/org/lwjgl/lwjgl/lwjgl-platform/2.9.0/lwjgl-platform-2.9.0-natives-windows.jar",
                "30e99b9386040f387fd94c26c1ac64d3",
                "https://repo1.maven.org/maven2/net/java/jinput/jinput-platform/2.0.5/jinput-platform-2.0.5-natives-windows.jar",
                "b168b014be0186d9e95bf3d263e3a129"
        );
        public static final DependencyNatives OSX = new DependencyNatives(
                "https://repo1.maven.org/maven2/org/lwjgl/lwjgl/lwjgl-platform/2.9.0/lwjgl-platform-2.9.0-natives-osx.jar",
                "722da64d6286a030e5e60d7678c27edc",
                "https://repo1.maven.org/maven2/net/java/jinput/jinput-platform/2.0.5/jinput-platform-2.0.5-natives-osx.jar",
                "32fea5fd88f91b9dd335839aca06f3d1"
        );

        public static final DependencyNatives LINUX = new DependencyNatives(
                "https://repo1.maven.org/maven2/org/lwjgl/lwjgl/lwjgl-platform/2.9.0/lwjgl-platform-2.9.0-natives-linux.jar",
                "8bf181ad1340d45e3505f267a5d33cc7",
                "https://repo1.maven.org/maven2/net/java/jinput/jinput-platform/2.0.5/jinput-platform-2.0.5-natives-linux.jar",
                "12f3021e9cab2beece3c191138955a56"
        );
        public static final String LWJGL = "https://repo1.maven.org/maven2/org/lwjgl/lwjgl/lwjgl/2.9.0/lwjgl-2.9.0.jar";
        public static final String LWJGL_HASH = "ce74486a7687ad7ea91dcc1fcd6977b8";
        public static final String LWJGL_UTIL = "https://repo1.maven.org/maven2/org/lwjgl/lwjgl/lwjgl_util/2.9.0/lwjgl_util-2.9.0.jar";
        public static final String LWJGL_UTIL_HASH = "6a0eeaf3451ed9646b7d61a9dd8b86cc";
        public static final String JINPUT = "https://repo1.maven.org/maven2/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar";
        public static final String JINPUT_HASH = "cc07d371f79dc4ed2239e1101ae06313";
        public static final HashMap<String, String> CACTUS_DEPS = new HashMap<String, String>() {{
            put("https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.6/gson-2.8.6.jar", "310f5841387183aca7900fead98d4858");
            put("http://dist.wso2.org/maven2/com/google/common/google-collect/1.0-rc1/google-collect-1.0-rc1.jar", "3fbdd4b6803f1e33b84b89b9f11719b4");
        }};
    }

    public static class Dirs {
        public static final String ROOT = ".";
        public static final String TEMP = ROOT + "/temp";
        public static final String SRC = ROOT + "/src";
        public static final String LOGS = ROOT + "/cactj-logs";
        public static final String BIN = ROOT + "/bin";
        public static final String JARS = ROOT + "/jars";
        public static final String REOBF = ROOT + "/reobf";
        public static final String CONF = ROOT + "/conf";
        public static final String RUNTIME = ROOT + "/runtime";
        public static final String LIB = ROOT + "/lib";
        public static final String NATIVES = Dirs.JARS + "/bin/natives";
        public static final String CACTJ_LIB = Dirs.LIB + "/cactj-lib";
    }

    public static class Exc {
        public static final String CLIENT_CFG = Dirs.CONF + "/client.exc";
        public static final String SERVER_CFG = Dirs.CONF + "/server.exc";
        public static final String CLIENT_OUT = Dirs.TEMP + "/minecraft_exc.jar";
        public static final String SERVER_OUT = Dirs.TEMP + "/minecraft_server_exc.jar";
        public static final String CLIENT_LOG = Dirs.LOGS + "/client_exc.log";
        public static final String SERVER_LOG = Dirs.TEMP + "/server_exc.log";
    }

    public static class Jar {
        public static final String CLIENT = Dirs.JARS + "/bin/minecraft.jar";
        public static final String SERVER = Dirs.JARS + "/minecraft_server.jar";
    }

    public static class Obf {
        public static final String CLIENT_OUT = Dirs.TEMP + "/minecraft_obf.jar";
        public static final String SERVER_OUT = Dirs.TEMP + "/minecraft_server_obf.jar";
    }

    public static class Output {
        public static final String BIN_OUT = Dirs.TEMP + "/bin";
        public static final String BIN_CLIENT = Dirs.TEMP + "/bin/minecraft";
        public static final String BIN_SERVER = Dirs.TEMP + "/bin/minecraft_server";
        public static final String SRC_CLIENT = Dirs.SRC + "/minecraft";
        public static final String SRC_SERVER = Dirs.SRC + "/minecraft_server";
    }

    public static class Patches {
        public static final String PATCH_CLIENT = Dirs.CONF + "/patches/minecraft.patch";
        public static final String PATCH_SERVER = Dirs.CONF + "/patches/minecraft_server.patch";
        public static final String PATCH_TEMP = Dirs.TEMP + "/temp.patch";
        public static final String FF_PATCH_CLIENT = Dirs.CONF + "/patches/minecraft_ff.patch";
        public static final String FF_PATCH_SERVER = Dirs.CONF + "/patches/minecraft_server_ff.patch";
    }

    public static class Recomp {
        public static final String BIN_CLIENT = Dirs.BIN + "/minecraft";
        public static final String BIN_SERVER = Dirs.BIN + "/minecraft_server";
        public static final String CLASSPATH_CLIENT = Dirs.LIB + "/," + Dirs.LIB + "/*," + Dirs.JARS + "/bin/minecraft.jar," + Dirs.JARS + "/bin/jinput.jar," + Dirs.JARS + "/bin/lwjgl.jar," + Dirs.JARS + "/bin/lwjgl_util.jar";
        public static final String CLASSPATH_SERVER = Dirs.LIB + "/," + Dirs.LIB + "/*," + Dirs.JARS + "/minecraft_server.jar";
        public static final String CLIENT_FIXES = Dirs.CONF + "/patches";
    }

    public static class Reobf {
        public static final String MD5_CLIENT = Dirs.TEMP + "/client.md5";
        public static final String MD5_SERVER = Dirs.TEMP + "/server.md5";
        public static final String MD5_PRE_REOBF_CLIENT = Dirs.TEMP + "/client_reobf.md5";
        public static final String MD5_PRE_REOBF_SERVER = Dirs.TEMP + "/server_reobf.md5";
        public static final String OBF_SRG_CLIENT = Dirs.TEMP + "/client_ro.srg";
        public static final String OBF_SRG_SERVER = Dirs.TEMP + "/server_ro.srg";
        public static final String RECOMP_JAR_CLIENT = Dirs.TEMP + "/client_recomp.jar";
        public static final String RECOMP_JAR_SERVER = Dirs.TEMP + "/server_recomp.jar";
        public static final String OBF_JAR_CLIENT = Dirs.TEMP + "/client_reobf.jar";
        public static final String OBF_JAR_SERVER = Dirs.TEMP + "/server_reobf.jar";
        public static final String REOBF_DIR_CLIENT = Dirs.REOBF + "/minecraft";
        public static final String REOBF_DIR_SERVER = Dirs.REOBF + "/minecraft_server";
        public static final String FIX_START = "minecraft/Start.class";
        public static final String NULLPKG = "net/minecraft/src";
        public static final String IGNORED_PKGS = "paulscode,com/jcraft,ibxm,de/matthiasmann/twl,org/xmlpull,javax/xml";
    }
}
