package net.glasslauncher.legacy.util;

import com.cedarsoftware.util.io.JsonObject;
import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import net.modificationstation.cactusjuice.Config;
import net.modificationstation.cactusjuice.Main;

import javax.xml.ws.http.HTTPException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Pastee {
    private HttpURLConnection req;
    private String text;

    public Pastee(String text) {
        try {
            this.req = (HttpURLConnection) new URL("https://api.paste.ee/v1/pastes").openConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        this.text = text;
    }

    public String post() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return post("Cactus Juice Log at " + dateFormat.format(date));
    }

    public String post(String name) {
        try {
            req.setRequestMethod("POST");
            req.setRequestProperty("Content-Type", "application/json");
            req.setRequestProperty("X-Auth-Token", "ak4XFTvAbNJvaEIoycGzOhCYeLkd7JFpZLVtUgutM");
            req.setDoOutput(true);
            req.setDoInput(true);
            JsonObject paste = new JsonObject();
            paste.put("description", name);
            JsonObject section = new JsonObject();
            section.put("contents", text);
            ArrayList sections = new ArrayList();
            sections.add(section);
            paste.put("sections", sections);
            OutputStreamWriter wr = new OutputStreamWriter(req.getOutputStream());
            wr.write(JsonWriter.objectToJson(paste, Config.prettyprint));
            wr.flush();

            BufferedReader res = new BufferedReader(new InputStreamReader(req.getInputStream()));
            StringBuilder resj = new StringBuilder();
            for (String strline = ""; strline != null; strline = res.readLine()) {
                resj.append(strline);
            }
            JsonObject resp = (JsonObject) JsonReader.jsonToJava(resj.toString());

            if (req.getResponseCode() != 201) {
                Main.logger.severe("Error sending request!");
                Main.logger.severe("Code: " + req.getResponseCode());
                throw new HTTPException(req.getResponseCode());
            }
            Main.logger.info(resj.toString());
            return (String) resp.get("link");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
