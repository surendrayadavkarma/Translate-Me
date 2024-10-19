package com.example.translateme.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Translation {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String sourceText;
    private String translatedText;

    // Constructor
    public Translation(int id, String sourceText, String translatedText) {
        this.id = id;
        this.sourceText = sourceText;
        this.translatedText = translatedText;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getSourceText() {
        return sourceText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }
}
