package com.example.translateme.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.translateme.model.Translation;

import java.util.List;

@Dao
public interface TranslationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Translation translation);

    @Query("SELECT * FROM Translation")
    List<Translation> getAllTranslations();
}
