package net.glasslauncher.legacy.jsontemplate;

import lombok.Getter;

import java.security.InvalidParameterException;
import java.util.HashMap;

public class Member {
    @Getter private String name;
    @Getter private String signature;
    @Getter private String type;
    @Getter private HashMap<String, Member> subMembers;

    public Member(String name, String signature, String type) {
        this.name = name;
        this.signature = signature;
        this.type = type;
    }

    public void addSubMember(String name, Member member) throws InvalidParameterException {
        if (subMembers == null && !this.type.equals("v") && !this.type.equals("p")) {
            subMembers = new HashMap<String, Member>(){{
                put(name, member);
            }};
        }
        else if (subMembers != null) {
            subMembers.put(name, member);
        }
    }

    public Member getSubMember(String name) {
        if (subMembers == null || this.type.equals("v") || this.type.equals("p")) {
            throw new RuntimeException("Can't get sub member if none exist!");
        }
        return subMembers.get(name);
    }
}
