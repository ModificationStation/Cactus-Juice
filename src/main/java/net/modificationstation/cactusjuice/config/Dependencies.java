package net.modificationstation.cactusjuice.config;

import lombok.Getter;
import net.modificationstation.cactusjuice.jsontemplate.DependencyNatives;

public class Dependencies {
    @Getter private static DependencyNatives windows;
    @Getter private static DependencyNatives osx;
    @Getter private static DependencyNatives linux;
    @Getter private static String lwjgl;
    @Getter private static String lwjglHash;
    @Getter private static String lwjglUtil;
    @Getter private static String lwjglUtilHash;
    @Getter private static String jinput;
    @Getter private static String jinputHash;
}
