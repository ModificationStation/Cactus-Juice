/*
 * Copyright (C) 2016, 2018 Player, asie
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/* This is a modified class from https://github.com/FabricMC/tiny-remapper */

package net.fabricmc.tinymapper;

import net.glasslauncher.legacy.jsontemplate.Member;
import net.glasslauncher.legacy.jsontemplate.Mappings;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TinyUtils {

    public static void readV2(BufferedReader reader, String from, String to,
                               String headerLine,
                               Mappings out)
            throws IOException {
        String[] parts;

        if (!headerLine.startsWith("tiny\t2\t")
                || (parts = splitAtTab(headerLine, 0, 5)).length < 5) { //min. tiny + major version + minor version + 2 name spaces
            throw new IOException("invalid/unsupported tiny file (incorrect header)");
        }

        List<String> namespaces = Arrays.asList(parts).subList(3, parts.length);
        int nsA = namespaces.indexOf(from);
        int nsB = namespaces.indexOf(to);
        Map<String, String> obfFrom = nsA != 0 ? new HashMap<>() : null;
        int partCountHint = 2 + namespaces.size(); // suitable for members, which should be the majority
        int lineNumber = 1;

        boolean inHeader = true;
        boolean inClass = false;
        boolean inMethod = false;

        boolean escapedNames = false;

        String className = null;
        String methodName = null;
        Member member = null;
        int varLvIndex = 0;
        int varStartOpIdx = 0;
        int varLvtIndex = 0;
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("\r", "").replaceAll("\n", "");
            lineNumber++;
            if (line.isEmpty()) continue;

            int indent = 0;

            while (indent < line.length() && line.charAt(indent) == '\t') {
                indent++;
            }

            parts = splitAtTab(line, indent, partCountHint);
            String section = parts[0];

            if (indent == 0) {
                inHeader = inClass = inMethod = false;

                if (section.equals("c")) { // class: c <names>...
                    if (parts.length != namespaces.size() + 1) throw new IOException("invalid class decl in line "+lineNumber);

                    className = unescapeOpt(parts[1 + nsA], escapedNames);
                    String mappedName = unescapeOpt(parts[1 + nsB], escapedNames);

                    if (!mappedName.isEmpty()) {
                        out.addClass(className, new Member(mappedName, null, "c"));
                    }

                    inClass = true;
                }
            } else if (indent == 1) {
                inMethod = false;

                if (inHeader) { // header k/v
                    if (section.equals("escaped-names")) {
                        escapedNames = true;
                    }
                } else if (inClass && (section.equals("m") || section.equals("f"))) { // method/field: m/f <descA> <names>...
                    boolean isMethod = section.equals("m");
                    if (parts.length != namespaces.size() + 2) throw new IOException("invalid "+(isMethod ? "method" : "field")+" decl in line "+lineNumber);

                    String memberDesc = unescapeOpt(parts[1], escapedNames);
                    String memberName = unescapeOpt(parts[2 + nsA], escapedNames);
                    String mappedName = unescapeOpt(parts[2 + nsB], escapedNames);
                    inMethod = isMethod;

                    if (!mappedName.isEmpty()) {
                        if (isMethod) {
                            member = new Member(mappedName, memberDesc, "m");
                            methodName = memberName;
                        } else {
                            member = new Member(mappedName, memberDesc, "f");
                        }
                        out.addFieldMethod(className, memberName, member);
                    }
                }
            } else if (indent == 2) {
                if (inMethod && section.equals("p")) { // method parameter: p <lv-index> <names>...
                    if (parts.length != namespaces.size() + 2) throw new IOException("invalid method parameter decl in line "+lineNumber);

                    varLvIndex = Integer.parseInt(parts[1]);
                    String mappedName = unescapeOpt(parts[2 + nsB], escapedNames);
                    if (!mappedName.isEmpty()) out.addMethodArg(className, methodName, parts[2 + nsA], new Member(mappedName, String.valueOf(varLvIndex), "p"));
                } else if (inMethod && section.equals("v")) { // method variable: v <lv-index> <lv-start-offset> <optional-lvt-index> <names>...
                    throw new IOException("invalid method variable decl in line "+lineNumber);
                }
            }
        }
    }

    private static String[] splitAtTab(String s, int offset, int partCountHint) {
        String[] ret = new String[Math.max(1, partCountHint)];
        int partCount = 0;
        int pos;

        while ((pos = s.indexOf('\t', offset)) >= 0) {
            if (partCount == ret.length) ret = Arrays.copyOf(ret, ret.length * 2);
            ret[partCount++] = s.substring(offset, pos);
            offset = pos + 1;
        }

        if (partCount == ret.length) ret = Arrays.copyOf(ret, ret.length + 1);
        ret[partCount++] = s.substring(offset);

        return partCount == ret.length ? ret : Arrays.copyOf(ret, partCount);
    }

    private static String unescapeOpt(String str, boolean escapedNames) {
        return escapedNames ? unescape(str) : str;
    }

    private static String unescape(String str) {
        int pos = str.indexOf('\\');
        if (pos < 0) return str;

        StringBuilder ret = new StringBuilder(str.length() - 1);
        int start = 0;

        do {
            ret.append(str, start, pos);
            pos++;
            int type;

            if (pos >= str.length()) {
                throw new RuntimeException("incomplete escape sequence at the end");
            } else if ((type = escaped.indexOf(str.charAt(pos))) < 0) {
                throw new RuntimeException("invalid escape character: \\"+str.charAt(pos));
            } else {
                ret.append(toEscape.charAt(type));
            }

            start = pos + 1;
        } while ((pos = str.indexOf('\\', start)) >= 0);

        ret.append(str, start, str.length());

        return ret.toString();
    }

    private static final String toEscape = "\\\n\r\0\t";
    private static final String escaped = "\\nr0t";
}
