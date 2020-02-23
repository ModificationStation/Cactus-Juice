package net.glasslauncher.legacy.jsontemplate;

import java.util.HashMap;

public class Mappings {
    private HashMap<String, Member> memberMap = new HashMap<>();

    public void addClass(String obfName, Member member) {
        memberMap.put(obfName, member);
    }

    public void addFieldMethod(String className, String obfName, Member member) {
        memberMap.get(className).addSubMember(obfName, member);
    }

    public void addMethodArg(String className, String methodName, String obfName, Member member) {
        memberMap.get(className).getSubMember(methodName).addSubMember(obfName, member);
    }
}
