package com.klive.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.klive.app.Inbox.ChatAdapter;
import com.klive.app.Inbox.ChatBean;
import com.klive.app.Interface.CitySelect;
import com.klive.app.R;
import com.klive.app.dialogs.cityDialog;
import com.klive.app.model.CountryCodes.CountryCodeModel;
import com.klive.app.model.city.CityResult;

import org.w3c.dom.Text;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityAdapterHolder> {
    public static String city = "";
    private int row_index = -1;
    private int pos = 0;
    List<CountryCodeModel> arrayList;
    Context context;
    CitySelect citySelect;
    cityDialog.OnMyDialogResult myDialogResult;

    public CityAdapter(Context context, List<CountryCodeModel> arrayList, CitySelect citySelect) {
        this.arrayList = arrayList;
        this.context = context;
        this.citySelect = citySelect;
    }

    @NonNull
    @Override
    public CityAdapter.CityAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        return new CityAdapter.CityAdapterHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CityAdapter.CityAdapterHolder holder, @SuppressLint("RecyclerView") final int position) {
        //holder.tagText.setText(arrayList.get(position));
        holder.city.setText(arrayList.get(position).getCountry());
        final CountryCodeModel team = arrayList.get(position);

        if (team.isSelected()) {
            holder.checkBox.setChecked(true);
            row_index = position;
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    citySelect.cityClick(true);
                    team.setSelected(true);
                    city = arrayList.get(position).getCountry();
                    if (row_index >= 0) {
                        arrayList.get(row_index).setSelected(false);
                        notifyItemChanged(row_index);
                    }
                    row_index = position;
                } else {
                    team.setSelected(false);
                }
            }
        });

        holder.city_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.checkBox.isChecked()){
                    holder.checkBox.setChecked(false);
                }else {

                    citySelect.cityClick(true);
                    holder.checkBox.setChecked(true);
                    city = arrayList.get(position).getCountry();
                   /* if (row_index >= 0) {
                        arrayList.get(row_index).setSelected(false);
                        notifyItemChanged(row_index);
                    }*/

                    row_index = position;
                }

            }
        });

        //  holder.city_tag.setOnClickListener(new View.OnClickListener() {
        //      @Override
        //      public void onClick(View v) {
        //          holder.checkBox.setChecked(true);


        //          if (!team.isSelected()) {
        //              citySelect.cityClick(true);
        //              team.setSelected(true);
        //              city = arrayList.get(position).getCity();
        //              if (row_index >= 0) {
        //                  arrayList.get(row_index).setSelected(false);
        //                  notifyItemChanged(row_index);
        //              }
        //              row_index = position;
        //          } else {
        //              team.setSelected(false);
        //          }

        //      }
        //  });






       /* holder.city_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
                //listener.onItemClick(position);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CityAdapterHolder extends RecyclerView.ViewHolder {
        RelativeLayout city_tag;
        CheckBox checkBox;
        TextView city;

        public CityAdapterHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.city_box);
            city_tag = view.findViewById(R.id.city_tag);
            city = view.findViewById(R.id.city);
        }
    }


    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(int position, String city);
    }

}
