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
        public static final String PATCHER = Dirs.RUNTIME + "/bin/applydiff.exe";
        public static final String JAD_RETRO = Dirs.RUNTIME + "/bin/jadretro.jar";
        public static final String FERNFLOWER = Dirs.RUNTIME + "/bin/fernflower.jar";
        public static final String JAD_WIN = Dirs.RUNTIME + "/bin/jad.exe";
        public static final String JAD_LINUX = Dirs.RUNTIME + "/bin/jad.exe";
        public static final String JAD_OSX = Dirs.RUNTIME + "/bin/jad-osx";
        public static final String EXCEPTOR = Dirs.RUNTIME + "/bin/exceptor.jar";
        public static final String SPECIAL_SOURCE = Dirs.RUNTIME + "/bin/SpecialSource-1.8.5.jar";
        public static final String CMD_RG = "%S -CP {classpath} RetroGuard -searge {conffile}";
        public static final String CMD_RG_REOBF = "%S -cp {classpath} RetroGuard -notch  {conffile}";
        public static final String CMD_JAD_RETRO = "%s -jar {jarjr} {targetdir}";
        public static final String CMD_RECOMP_CLT = "%s -g -verbose -classpath \"" + Recomp.CLASSPATH_CLIENT + "\" -sourcepath {sourcepath} -d {outpath} {pkgs} {fixes}/*.java";
        public static final String CMD_RECOMP_SRV = "%s -g -verbose -classpath \"" + Recomp.CLASSPATH_SERVER + "\" -sourcepath {sourcepath} -d {outpath} {pkgs}";
        public static final String CMD_START_CLT = "%s -Xincgc -Xms1024M -Xmx1024M -cp \"" + Recomp.CLASSPATH_CLIENT + "\" net.minecraft.server.MinecraftServer";
        public static final String CMD_START_SRV = "%s -Xincgc -Xms1024M -Xmx1024M -cp \"" + Recomp.CLASSPATH_SERVER + "\" -Djava.library.path={natives} Start";
        public static final String CMD_PATCH_WIN = "./%(patcher)s --binary -p1 -u -i ../../{patchfile} -d {srcdir}";
        public static final String CMD_PATCH_LINUX = "patch --binary -p1 -u -i ../../{patchfile} -d {srcdir}";
        public static final String CMD_PATCH_OSX = "Patch --binary -p1 -u -i ../../{patchfile} -d {srcdir}";
        public static final String CMD_FERNFLOWER = "%s -jar {jarff} {conf} {jarin} {jarout}";
        public static final String CMD_EXCEPTOR = "%s -jar {jarexc} {input} {output} {conf} {log}";
        public static final String CMD_SPECIAL_SOURCE = "%s -jar {jarexc} -i {input} -o {output} -m {srg}";

        public String parseCmd(String command, Map<String, String> replaceMap) {
            for (String key : replaceMap.keySet()) {
                command = command.replace("{" + key + "}", replaceMap.get(key));
            }
            return command;
        }
    }

    public static class ConfFiles {
        public static final String CLASSES = Dirs.CONF + "/classes.csv";
        public static final String METHODS = Dirs.CONF + "/methods.csv";
        public static final String FIELDS = Dirs.CONF + "/fields.csv";
        public static final String RG_CLIENT = Dirs.TEMP + "/client_rg.srg";
        public static final String RG_SERVER = Dirs.TEMP + "/server_rg.srg";
        public static final String RO_CLIENT = Dirs.TEMP + "/client_ro.srg";
        public static final String RO_SERVER = Dirs.TEMP + "/server_ro.srg";
    }

    public static class Decomp {
        public static final String CLIENT_CONF = "-rbr=0 -dgs=1 -asc=1";
        public static final String SERVER_CONF = "-rbr=0 -dgs=1 -asc=1";
        public static final String CLIENT_OUT = Dirs.OUT + "/client";
        public static final String SERVER_OUT = Dirs.OUT + "/server";
        public static final String CLIENT_SRC = Dirs.TEMP + "/minecraft_exc.jar";
        public static final String SERVER_SRC = Dirs.TEMP + "/minecraft_server_exc.jar";
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
        public static final String OUT = ROOT + "/temp/out";
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
        public static final String CLIENT = Dirs.JARS + "bin/minecraft.jar";
        public static final String SERVER = Dirs.JARS + "minecraft_server.jar";
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
