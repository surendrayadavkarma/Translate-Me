package com.example.translateme.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.translateme.model.Translation;

@Database(entities = {Translation.class}, version = 1, exportSchema = false)
public abstract class TranslationDatabase extends RoomDatabase {

    public abstract TranslationDao translationDao();

    private static volatile TranslationDatabase INSTANCE;

    public static TranslationDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TranslationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TranslationDatabase.class, "translation_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
