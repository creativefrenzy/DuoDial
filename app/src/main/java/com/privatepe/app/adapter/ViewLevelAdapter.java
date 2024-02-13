package com.privatepe.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.app.R;
import com.privatepe.app.model.LevelData.Level;

import java.util.List;

public class ViewLevelAdapter extends RecyclerView.Adapter<ViewLevelAdapter.myViewHolder> {

    Context context;
    List<Level> list;

    public ViewLevelAdapter(Context context, List<Level> list) {

        this.context = context;
        this.list = list;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view_level, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        holder.tv_leveldata.setText("Lv"+list.get(position).getLevel());
        holder.rl_backchange.setBackground(context.getResources().getDrawable(R.drawable.level3_trans_relative));

        /*switch (position) {
            case 0:
                holder.tv_leveldata.setText("Lv1");
                holder.rl_backchange.setBackground(context.getResources().getDrawable(R.drawable.level10_trans_relative));
                break;
            case 1:
                holder.tv_leveldata.setText("Lv2");
                holder.rl_backchange.setBackground(context.getResources().getDrawable(R.drawable.level9_trans_relative));
                break;
            case 2:
                holder.tv_leveldata.setText("Lv3");
                holder.rl_backchange.setBackground(context.getResources().getDrawable(R.drawable.level8_trans_relative));
                break;
            case 3:
                holder.tv_leveldata.setText("Lv4");
                holder.rl_backchange.setBackground(context.getResources().getDrawable(R.drawable.level7_trans_relative));
                break;
            case 4:
                holder.tv_leveldata.setText("Lv5");
                holder.rl_backchange.setBackground(context.getResources().getDrawable(R.drawable.level6_trans_relative));
                break;
            case 5:
                holder.tv_leveldata.setText("Lv6");
                holder.rl_backchange.setBackground(context.getResources().getDrawable(R.drawable.level5_trans_relative));
                break;
            case 6:
                holder.tv_leveldata.setText("Lv7");
                holder.rl_backchange.setBackground(context.getResources().getDrawable(R.drawable.level4_trans_relative));
                break;
            case 7:
                holder.tv_leveldata.setText("Lv8");
                holder.rl_backchange.setBackground(context.getResources().getDrawable(R.drawable.level3_trans_relative));
                break;
            case 8:
                holder.tv_leveldata.setText("Lv9");
                holder.rl_backchange.setBackground(context.getResources().getDrawable(R.drawable.level2_trans_relative));
                break;
            case 9:
                holder.tv_leveldata.setText("Lv10");
                holder.rl_backchange.setBackground(context.getResources().getDrawable(R.drawable.level1_trans_relative));
                break;
        }*/
        //  holder.tv_leveldata.setText(list.get(position).getLevel() + "");
        holder.tv_incomedata.setText(list.get(position).getAmount() + "");
        holder.tv_beans.setText(list.get(position).getLevelBeans() + "");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView tv_leveldata, tv_incomedata,tv_beans;
        RelativeLayout container, rl_backchange;

        public myViewHolder(View itemView) {
            super(itemView);

            tv_leveldata = itemView.findViewById(R.id.tv_leveldata);
            tv_beans = itemView.findViewById(R.id.tv_beans);
            tv_incomedata = itemView.findViewById(R.id.tv_incomedata);
            container = itemView.findViewById(R.id.container);
            rl_backchange = itemView.findViewById(R.id.rl_backchange);

        }
    }
}