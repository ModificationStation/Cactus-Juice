package net.glasslauncher.legacy.util;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import net.modificationstation.cactusjuice.config.Config;
import net.modificationstation.cactusjuice.Main;
import net.modificationstation.cactusjuice.jsontemplate.PasteePost;
import net.modificationstation.cactusjuice.jsontemplate.PasteePostSection;
import net.modificationstation.cactusjuice.jsontemplate.PasteeResponse;

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
            Gson gson = Config.gsonBuilder.create();
            req.setRequestMethod("POST");
            req.setRequestProperty("Content-Type", "application/json");
            req.setRequestProperty("X-Auth-Token", "ak4XFTvAbNJvaEIoycGzOhCYeLkd7JFpZLVtUgutM");
            req.setDoOutput(true);
            req.setDoInput(true);
            PasteePost pasteePost = new PasteePost();
            pasteePost.description = name;
            pasteePost.sections = new PasteePostSection[1];
            pasteePost.sections[0] = new PasteePostSection();
            pasteePost.sections[0].contents = text;

            OutputStreamWriter wr = new OutputStreamWriter(req.getOutputStream());
            wr.write(gson.toJson(pasteePost));
            wr.flush();

            BufferedReader res = new BufferedReader(new InputStreamReader(req.getInputStream()));
            StringBuilder resj = new StringBuilder();
            for (String strline = ""; strline != null; strline = res.readLine()) {
                resj.append(strline);
            }
            PasteeResponse resp = gson.fromJson(resj.toString(), PasteeResponse.class);

            if (req.getResponseCode() != 201) {
                Main.logger.severe("Error sending request!");
                Main.logger.severe("Code: " + req.getResponseCode());
                throw new HTTPException(req.getResponseCode());
            }
            Main.logger.info(resj.toString());
            return resp.link;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
