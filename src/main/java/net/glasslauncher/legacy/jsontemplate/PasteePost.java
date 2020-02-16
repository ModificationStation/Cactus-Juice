package net.glasslauncher.legacy.jsontemplate;

import lombok.Setter;

@Setter
public class PasteePost {
    private PasteePostSection[] sections;
    private String description;
}
