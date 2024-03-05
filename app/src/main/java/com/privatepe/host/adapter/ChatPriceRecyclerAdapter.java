package com.privatepe.host.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.host.R;
import com.privatepe.host.model.PriceList.PriceDataModel;
import com.privatepe.host.utils.SessionManager;

import java.util.ArrayList;

public class ChatPriceRecyclerAdapter extends RecyclerView.Adapter<ChatPriceRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PriceDataModel> priceDataModelArrayList = new ArrayList<>();
    private int SelectedChatPrice, SelectedLevel;
    private int selectedPosition = -1;
    PriceCallbackInterface priceCallbackInterface;
    private LinearLayout toast;


    public ChatPriceRecyclerAdapter(Context context, ArrayList<PriceDataModel> priceDataModelArrayList, int selectedChatPrice, int selectedLevel, int selPosition , PriceCallbackInterface priceCallbackInterface ) {
        this.context = context;
        this.priceDataModelArrayList = priceDataModelArrayList;
        SelectedChatPrice = selectedChatPrice;
        SelectedLevel = selectedLevel;
        selectedPosition = selPosition;
        this.priceCallbackInterface=priceCallbackInterface;

        //  Toast.makeText(context,""+selPosition,Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.chat_price_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String PriceString = "" + priceDataModelArrayList.get(position).getAmount() + "/min " + priceDataModelArrayList.get(position).getLevel();


     //   String ss=priceDataModelArrayList.get(position).getLevel();
     //   String[] ss1=ss.trim().split(" ");
     //   String[] ss2=ss1[1].trim().split("-");
//
     //  Log.i("ss2",""+ss2[0]+"    "+ss2[1]);
//

       String userlevel=new SessionManager(context).getUserLevel();


        holder.PriceText.setText(PriceString);
        if (position > 2) {
           // Log.i("levellll",""+new SessionManager(context).getUserLevel()+"    "+getLevelFromApi(priceDataModelArrayList.get(position).getLevel()));

           // new SessionManager(context).getUserLevel()

            if(levelisbigger(Integer.parseInt(userlevel),getLevelFromApi(priceDataModelArrayList.get(position).getLevel())))
            {
                holder.PriceText.setTextColor(context.getResources().getColor(R.color.black));
                Log.i("isLevelbigger","yes");
               // holder.checkBox.setEnabled(true);
            }
            else {
                holder.PriceText.setTextColor(context.getResources().getColor(R.color.disable_price));
              //  holder.checkBox.setEnabled(false);

                Log.i("isLevelbigger","no");
            }


            //;

        }

        holder.checkBox.setOnClickListener(view -> {


           if(position > 2)
           {

               if(levelisbigger(Integer.parseInt(userlevel),getLevelFromApi(priceDataModelArrayList.get(position).getLevel())))
               {
                   selectedPosition = holder.getAdapterPosition();
                   priceCallbackInterface.getSelectedPrice(String.valueOf(priceDataModelArrayList.get(position).getAmount()));
                   updateDataOnServer(priceDataModelArrayList.get(position).getAmount());
                   notifyDataSetChanged();

               }
               else {
                   customToastForPrice(context);
                   holder.checkBox.setChecked(false);
               }


           }
           else {

               selectedPosition = holder.getAdapterPosition();
               priceCallbackInterface.getSelectedPrice(String.valueOf(priceDataModelArrayList.get(position).getAmount()));
               updateDataOnServer(priceDataModelArrayList.get(position).getAmount());
               notifyDataSetChanged();
           }

        });

        if (selectedPosition == position) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

    }

    private boolean levelisbigger(int userLevel, int[] levelFromApi) {

        boolean islevelbigger=false;

        //  if(userLevel>=levelFromApi[0]&&userLevel<=levelFromApi[1])

        if(userLevel>=levelFromApi[0])
        {
            islevelbigger=true;

        }
        else {

            islevelbigger=false;
        }


        return islevelbigger;

    }

    private int[] getLevelFromApi(String level) {

        int[] lev=new int[2];

        if(!level.equals("All"))
        {
            String ss=level.trim();
            String[] ss1=ss.trim().split(" ");
            String[] ss2=ss1[1].trim().split("-");

            lev[0]=Integer.parseInt(ss2[0].trim());
            lev[1]=Integer.parseInt(ss2[1].trim());
        }
        return lev;


    }

    private void updateDataOnServer(String amount) {
      //  new ApiManager(context, (ApiResponseInterface) context).updateCallPrice(new priceupdateModel(amount));


    }

    @Override
    public int getItemCount() {
        return priceDataModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView PriceText;
        CheckBox checkBox;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            PriceText = itemView.findViewById(R.id.pricetxt);
            checkBox = itemView.findViewById(R.id.checkbox);


        }
    }

    private void customToastForPrice(Context context) {
        LayoutInflater li =LayoutInflater.from(context);
        View layout = li.inflate(R.layout.your_level_lower, (ViewGroup) toast);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setView(layout);
        toast.show();
    }


    public interface PriceCallbackInterface
    {
        public void getSelectedPrice(String Price);
    }


}
