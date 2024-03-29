package com.privatepe.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.app.R;
import com.privatepe.app.model.UserListResponseNew.GetRatingTag;

import java.util.Arrays;
import java.util.List;

public class RateCountDisplayAdapter extends RecyclerView.Adapter<RateCountDisplayAdapter.ViewHolder> {
    Context context;
    List<GetRatingTag> list;

    public RateCountDisplayAdapter(Context context, List<GetRatingTag> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ratecountdisplay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            String tag = list.get(position).getTag();
            List<String> tagList = Arrays.asList(tag.split("\\,"));

            //String[] parts = tag.split("\\,"); // escape.
            //String document = parts[parts.length - 1];
            /*String part1 = parts[0];
            String part2 = parts[1];
            String part3 = parts[2];*/

            for (int i = 0; i <= tagList.size(); i++) {
                holder.tv_tag_show.setText(tagList.get(i));
                holder.tv_tag_count.setText(" " + list.get(position).getTotalCount());
            }

            // holder.tv_tag_show.setText(tagList.get(0));
            // holder.tv_tag_count.setText(" " + list.get(position).getTotalCount());

        } catch (Exception e) {
        }
    }


    @Override
    public int getItemViewType(int position) {
        return list.size();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_tag_show;
        TextView tv_tag_count;


        public ViewHolder(View view) {
            super(view);

            tv_tag_show = view.findViewById(R.id.tv_tag_show);
            tv_tag_count = view.findViewById(R.id.tv_tag_count);
        }


    }
}