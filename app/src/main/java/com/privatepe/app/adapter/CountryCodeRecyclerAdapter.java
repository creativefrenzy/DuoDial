package com.privatepe.app.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.app.R;
import com.privatepe.app.activity.CountryCodeActivity;
import com.privatepe.app.login.OTPVerify;
import com.privatepe.app.model.CountryCodes.CountryCodeModel;

import java.util.ArrayList;
import java.util.List;

public class CountryCodeRecyclerAdapter extends RecyclerView.Adapter<CountryCodeRecyclerAdapter.ViewHolder> {

    CountryCodeActivity countryCodeActivity;
    List<CountryCodeModel> countryCodeList = new ArrayList<>();

    List<Integer> flagdrawbleList = new ArrayList<>();


    public CountryCodeRecyclerAdapter(CountryCodeActivity countryCodeActivity, List<CountryCodeModel> countryCodeList, List<Integer> flagdrawbleList) {
        this.countryCodeActivity = countryCodeActivity;
        this.countryCodeList = countryCodeList;
        this.flagdrawbleList = flagdrawbleList;
    }


    @NonNull
    @Override
    public CountryCodeRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.countrycode_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryCodeRecyclerAdapter.ViewHolder holder, int position) {
        holder.CoutryName.setText(countryCodeList.get(position).getCountry());
        holder.CountryCode.setText(countryCodeList.get(position).getCountryCode());


        holder.flagImg.setImageResource(flagdrawbleList.get(position));

        Log.i("imgId", flagdrawbleList.get(0).toString());

        //Log.i("imgurl",countryCodeList.get(position).getCountryImg());


        //   Glide.with(countryCodeActivity)
        //           .load(countryCodeList.get(position).getCountryImg()) // image url
        //           .placeholder(R.drawable.img_file) // any placeholder to load at start
        //           .centerCrop()
        //           .into(holder.flagImg);


    }

    @Override
    public int getItemCount() {
        return countryCodeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView flagImg;
        private TextView CoutryName;
        private TextView CountryCode;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            flagImg = itemView.findViewById(R.id.flagimg);
            CoutryName = itemView.findViewById(R.id.countryName);
            CountryCode = itemView.findViewById(R.id.countryCode);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(countryCodeActivity, OTPVerify.class);
                    intent.putExtra("countryCode", countryCodeList.get(getAdapterPosition()).getCountryCode());
                    intent.putExtra("phonenum", countryCodeActivity.phonenum);
                    countryCodeActivity.startActivity(intent);
                    countryCodeActivity.finish();

                }
            });


        }
    }
}
