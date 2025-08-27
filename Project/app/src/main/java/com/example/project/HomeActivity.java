package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.project.utils.HistoryManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the home page searching activity of the application.
 * @author u6393399 Xuan Liu, u7580335 Shiying Cai
 */
public class HomeActivity extends AppCompatActivity {
    private EditText editText;
    private Button button, btnHistory1, btnHistory2, btnHistory3;
    private Intent intent;
    List<String> histories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent fromLoginIntent = getIntent();
        String username = fromLoginIntent.getStringExtra("Username");
        editText = findViewById(R.id.HomeSearch);
        button = findViewById(R.id.HomeButton);
        intent = new Intent(this, MainActivity.class);

        btnHistory1 = findViewById(R.id.historyButton1);
        btnHistory2 = findViewById(R.id.historyButton2);
        btnHistory3 = findViewById(R.id.historyButton3);

        HistoryManager historyManager = new HistoryManager(this);
        histories = historyManager.getHistory(username);
        Collections.reverse(histories);

        List<String> displayedHistories = new ArrayList<>();
        setupHistoryButton(btnHistory1, 0, displayedHistories);
        setupHistoryButton(btnHistory2, 1, displayedHistories);
        setupHistoryButton(btnHistory3, 2, displayedHistories);

        button.setOnClickListener(v -> {
            String course = editText.getText().toString();
            intent.putExtra("Course", course);
            intent.putExtra("Username", username);
            editText.setText("");
            startActivity(intent);

            historyManager.updateHistory(username,course);
        });
    }

    /**
     * Sets up a button to display and select a unique history item.
     * @param button
     * @param historyIndex
     * @param displayedHistories
     */
    private void setupHistoryButton(Button button, int historyIndex, List<String> displayedHistories) {
        while (historyIndex < histories.size()) {
            String history = histories.get(historyIndex).trim();
            // Check if the history item is non-empty and has not been displayed yet.
            if (!history.isEmpty() && !displayedHistories.contains(history)) {
                button.setText(history);
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(v -> editText.setText(history));
                displayedHistories.add(history);
                break;
            } else {
                historyIndex++;
            }
        }
        if (historyIndex >= histories.size()) {
            button.setVisibility(View.GONE);
        }
    }
}