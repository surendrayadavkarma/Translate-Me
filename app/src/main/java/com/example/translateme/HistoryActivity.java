package com.example.translateme;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.translateme.database.TranslationDatabase;  // Import statement added
import com.example.translateme.model.Translation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryActivity extends AppCompatActivity {

    private ListView historyListView;
    private HistoryAdapter historyAdapter;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyListView = findViewById(R.id.historyListView);
        historyAdapter = new HistoryAdapter(this, new ArrayList<>());
        historyListView.setAdapter(historyAdapter);

        executorService = Executors.newSingleThreadExecutor();
        loadHistory();
    }

    private void loadHistory() {
        executorService.execute(() -> {
            List<Translation> translations;
            try {
                // Access the database through TranslationDao
                TranslationDatabase db = TranslationDatabase.getDatabase(getApplicationContext());
                translations = db.translationDao().getAllTranslations();
                runOnUiThread(() -> historyAdapter.updateData(translations));
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(HistoryActivity.this, "Failed to load history", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
