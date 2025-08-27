package com.example.project.utils;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * Manages the user history stored in a JSON file.
 * @author u6393399 Xuan Liu
 */
public class HistoryManager {

    private String filename = "history.json";
    private Context context;

    public HistoryManager(Context context) {
        this.context = context;
    }

    private JSONObject readHistoryFile() {
        File file = new File(context.getFilesDir(), filename);
        if (!file.exists()) {
            return new JSONObject();
        }

        StringBuilder content = new StringBuilder();
        try (FileInputStream fis = context.openFileInput(filename);
             InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            return new JSONObject(content.toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    private void writeToFile(JSONObject jsonObject) {
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
             OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8")) {

            osw.write(jsonObject.toString());
            osw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateHistory(String username, String history) {
        JSONObject jsonObject = readHistoryFile();

        try {
            JSONArray userHistory;
            if (jsonObject.has(username)) {
                userHistory = jsonObject.getJSONArray(username);
                for (int i = 0; i < userHistory.length(); i++) {
                    if (userHistory.getString(i).equals(history)) {
                        userHistory.remove(i);
                        break;
                    }
                }
                userHistory.put(history);
            } else {
                userHistory = new JSONArray();
                userHistory.put(history);
                jsonObject.put(username, userHistory);
            }

            writeToFile(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<String> getHistory(String username) {
        List<String> historyList = new ArrayList<>();
        JSONObject jsonObject = readHistoryFile();

        try {
            if (jsonObject.has(username)) {
                JSONArray userHistory = jsonObject.getJSONArray(username);
                for (int i = 0; i < userHistory.length(); i++) {
                    historyList.add(userHistory.getString(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return historyList;
    }
}