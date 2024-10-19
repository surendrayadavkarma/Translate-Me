package com.example.translateme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.translateme.model.Translation;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<Translation> {
    private final Context context;
    private List<Translation> translations;

    public HistoryAdapter(Context context, List<Translation> translations) {
        super(context, 0, translations);
        this.context = context;
        this.translations = translations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_translation, parent, false);
        }

        Translation translation = translations.get(position);
        TextView sourceTextView = convertView.findViewById(R.id.sourceText);
        TextView translatedTextView = convertView.findViewById(R.id.translatedText);

        // Use getter methods
        sourceTextView.setText(translation.getSourceText());
        translatedTextView.setText(translation.getTranslatedText());

        // Set text color using colors from colors.xml
        sourceTextView.setTextColor(context.getResources().getColor(R.color.white)); // Set to black
        translatedTextView.setTextColor(context.getResources().getColor(R.color.colorPrimary)); // Set to your primary color

        return convertView;
    }

    public void updateData(List<Translation> newTranslations) {
        translations.clear();
        translations.addAll(newTranslations);
        notifyDataSetChanged();
    }
}
