package com.klive.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.klive.app.Interface.LanguageSelect;
import com.klive.app.R;
import com.klive.app.model.language.LanguageData;
import com.klive.app.model.language.LanguageJsonModel;

import java.util.List;

public class LangAdapter extends RecyclerView.Adapter<LangAdapter.LanguageAdapterHolder> {
    public static String language = "";
    public static String id = "";
    private int row_index = -1;
    private int pos = 0;
    List<LanguageJsonModel> arrayList;
    int MAX_TAGS_ALLOWED = 3;
    Context context;
    LanguageSelect languageSelect;
    public LangAdapter(Context context, List<LanguageJsonModel> arrayList, LanguageSelect languageSelect) {
        this.arrayList = arrayList;
        this.context = context;
        this.languageSelect = languageSelect;
    }

    @NonNull
    @Override
    public LangAdapter.LanguageAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_item, parent, false);
        return new LangAdapter.LanguageAdapterHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final LangAdapter.LanguageAdapterHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.language.setText(arrayList.get(position).getLanguageName());

        if (row_index == position)
            holder.itemView.setBackgroundResource(R.drawable.langselect);
        else
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));

        holder.language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language = arrayList.get(position).getLanguageName();
                id = String.valueOf(arrayList.get(position).getLanguageId());
                row_index = position;
                languageSelect.languageClick(true);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class LanguageAdapterHolder extends RecyclerView.ViewHolder {
       TextView language;
        public LanguageAdapterHolder(View view) {
            super(view);
            language = view.findViewById(R.id.language);
        }
    }
}
