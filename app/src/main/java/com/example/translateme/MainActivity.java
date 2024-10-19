package com.example.translateme;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.translateme.database.TranslationDatabase;  // Import statement added
import com.example.translateme.model.Translation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Spinner fromSpinner, toSpinner;
    private TextInputEditText sourceText;
    private ImageView micTV;
    private MaterialButton translateBtn, historyBtn, favoriteBtn;
    private TextView translateTV;

    String[] fromLanguage = {
            "From", "English", "Afrikaans", "Arabic", "Belarusian", "Bengali",
            "Catalan", "Czech", "Welsh", "Hindi", "Urdu", "Spanish", "French",
            "German", "Italian", "Japanese", "Korean", "Dutch", "Turkish", "Russian", "Portuguese"
    };

    String[] toLanguage = {
            "To", "English", "Afrikaans", "Arabic", "Belarusian", "Bengali",
            "Catalan", "Czech", "Welsh", "Hindi", "Urdu", "Spanish", "French",
            "German", "Italian", "Japanese", "Korean", "Dutch", "Turkish", "Russian", "Portuguese"
    };

    private static final int REQUEST_PERMISSION_CODE = 1;
    int fromLanguageCode, toLanguageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);
        sourceText = findViewById(R.id.idEditSource);
        micTV = findViewById(R.id.idTvMic);
        translateBtn = findViewById(R.id.idBtnTranslation);
        translateTV = findViewById(R.id.idTranslatedTv);
        historyBtn = findViewById(R.id.idBtnHistory);
        favoriteBtn = findViewById(R.id.idBtnFavorites);

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, fromLanguage);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromLanguageCode = getLanguageCode(fromLanguage[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, toLanguage);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLanguageCode = getLanguageCode(toLanguage[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        micTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something to translate");

                try {
                    startActivityForResult(intent, REQUEST_PERMISSION_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateTV.setVisibility(View.VISIBLE);
                translateTV.setText("");

                if (sourceText.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter text to translate", Toast.LENGTH_SHORT).show();
                } else if (fromLanguageCode == 0) {
                    Toast.makeText(MainActivity.this, "Please select source language", Toast.LENGTH_SHORT).show();
                } else if (toLanguageCode == 0) {
                    Toast.makeText(MainActivity.this, "Please select target language", Toast.LENGTH_SHORT).show();
                } else {
                    translateText(fromLanguageCode, toLanguageCode, sourceText.getText().toString());
                }
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String translatedText = translateTV.getText().toString();
                if (!translatedText.isEmpty()) {
                    addTranslationToFavorites(sourceText.getText().toString(), translatedText);
                }
            }
        });
    }

    private void translateText(int fromLanguageCode, int toLanguageCode, String source) {
        translateTV.setText("Downloading model, please wait...");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();
        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

        translator.downloadModelIfNeeded()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        translateTV.setText("Translating...");
                        translator.translate(source)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String translatedText) {
                                        translateTV.setText(translatedText);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "Failed to translate! Try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to download model! Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addTranslationToFavorites(String sourceText, String translatedText) {
        Translation translation = new Translation(0, sourceText, translatedText);
        // Storing in database (adjust the database access accordingly)
        new Thread(new Runnable() {
            @Override
            public void run() {
                TranslationDatabase db = TranslationDatabase.getDatabase(getApplicationContext());
                db.translationDao().insert(translation);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null) {
                sourceText.setText(result.get(0));
            }
        }
    }

    private int getLanguageCode(String language) {
        switch (language) {
            case "English":
                return FirebaseTranslateLanguage.EN;
            case "Afrikaans":
                return FirebaseTranslateLanguage.AF;
            case "Arabic":
                return FirebaseTranslateLanguage.AR;
            case "Belarusian":
                return FirebaseTranslateLanguage.BE;
            case "Bengali":
                return FirebaseTranslateLanguage.BN;
            case "Catalan":
                return FirebaseTranslateLanguage.CA;
            case "Czech":
                return FirebaseTranslateLanguage.CS;
            case "Welsh":
                return FirebaseTranslateLanguage.CY;
            case "Hindi":
                return FirebaseTranslateLanguage.HI;
            case "Urdu":
                return FirebaseTranslateLanguage.UR;
            case "Spanish":
                return FirebaseTranslateLanguage.ES;
            case "French":
                return FirebaseTranslateLanguage.FR;
            case "German":
                return FirebaseTranslateLanguage.DE;
            case "Italian":
                return FirebaseTranslateLanguage.IT;
            case "Japanese":
                return FirebaseTranslateLanguage.JA;
            case "Korean":
                return FirebaseTranslateLanguage.KO;
            case "Dutch":
                return FirebaseTranslateLanguage.NL;
            case "Turkish":
                return FirebaseTranslateLanguage.TR;
            case "Russian":
                return FirebaseTranslateLanguage.RU;
            case "Portuguese":
                return FirebaseTranslateLanguage.PT;
            default:
                return 0;
        }
    }
}
