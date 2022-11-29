package com.klive.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.klive.app.R;
import com.klive.app.model.language.LanguageData;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumAdapterHolder> {
    public static String language = "";
    public static String id = "";
    private int row_index = -1;
    private int pos = 0;
    List<String> arrayList;
    int MAX_TAGS_ALLOWED = 3;
    Context context;
    public AlbumAdapter(Context context, List<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AlbumAdapter.AlbumAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
        return new AlbumAdapter.AlbumAdapterHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AlbumAdapter.AlbumAdapterHolder holder, @SuppressLint("RecyclerView") final int position) {
        //holder.language.setText(arrayList.get(position));
        holder.userImage1.setImageURI(Uri.parse(arrayList.get(position)));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AlbumAdapterHolder extends RecyclerView.ViewHolder {
       ImageView userImage1;
        public AlbumAdapterHolder(View view) {
            super(view);
            userImage1 = view.findViewById(R.id.userImage1);
        }
    }
}
