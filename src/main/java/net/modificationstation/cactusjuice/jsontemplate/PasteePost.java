package net.modificationstation.cactusjuice.jsontemplate;

import lombok.Setter;

@Setter
public class PasteePost {
    private PasteePostSection[] sections;
    private String description;
}
