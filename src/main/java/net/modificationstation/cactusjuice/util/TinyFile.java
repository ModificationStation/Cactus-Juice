package net.modificationstation.cactusjuice.util;

import com.google.gson.Gson;
import net.fabricmc.tinymapper.TinyUtils;
import net.glasslauncher.legacy.jsontemplate.Mappings;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class TinyFile {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("out.tinyfilev2"));
        String header = bufferedReader.readLine().replace("\n", "").replace("\r", "");
        Mappings mappingAcceptor = new Mappings();
        TinyUtils.readV2(bufferedReader, "intermediary", "named", header, mappingAcceptor);

        new FileOutputStream("test.json").write((new Gson()).toJson(mappingAcceptor).getBytes());
    }
}
