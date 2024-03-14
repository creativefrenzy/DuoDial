package com.privatepe.app.adapter.metend;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;
import com.privatepe.app.Inbox.MessageBean;
import com.privatepe.app.R;

import java.util.List;


public class MessageAdapterVDO extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MessageBean> messageBeanList;
    protected final LayoutInflater inflater;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private Context context;

    public MessageAdapterVDO(Context context, List<MessageBean> messageBeanList) {
        inflater = ((Activity) context).getLayoutInflater();
        this.messageBeanList = messageBeanList;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.msginvdo, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        throw new RuntimeException("there is no type");
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {
            setupView((HeaderViewHolder) holder, position);
        } else if (holder instanceof MyViewHolder) {
            setupView((MyViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {

        return messageBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }


    private boolean isPositionHeader(int position) {
        String date = messageBeanList.get(position).getMsgDate();
        if (TextUtils.isEmpty(date)) {
            return false;
        } else {
            return true;
        }
    }

    private void setupView(MyViewHolder holder, int position) {

        MessageBean bean = messageBeanList.get(position);
        //Log.e("ChatList", "position="+position+" "+bean.getMessage().getType() +" "+bean.getMessage().getMessage());
        holder.setIsRecyclable(false);
        if (bean.isBeSelf()) {

            try {

                if (bean.getMessage().getType().equals("text")) {
                    holder.tv_msg.setText(Html.fromHtml(
                            getColoredSpanned(bean.getMessage().getFromName(), "#FFD700") + getColoredSpanned(" : " + bean.getMessage().getMessage(), "#FFFFFF")));
                } else if (bean.getMessage().getType().equals("gift")) {
                    holder.cv_image.setVisibility(View.VISIBLE);
                    holder.img_image.setImageResource(getGiftImage((bean.getMessage().getMessage())));
                    holder.tv_msg.setVisibility(View.GONE);


                } else if (bean.getMessage().getType().equals("ss")) {

                }
            } catch (Exception e) {
            }
        } else {
            try {

                if (bean.getMessage().getType().equals("text")) {
                    holder.tv_msg.setText(Html.fromHtml(
                            getColoredSpanned(bean.getMessage().getFromName(), "#FFD700") + getColoredSpanned(" : " + bean.getMessage().getMessage(), "#FFFFFF")));
                } else if (bean.getMessage().getType().equals("gift")) {
                    holder.cv_image.setVisibility(View.VISIBLE);
                    holder.img_image.setImageResource(getGiftImage((bean.getMessage().getMessage())));
                    holder.tv_msg.setVisibility(View.GONE);


                } else if (bean.getMessage().getType().equals("ss")) {

                }
            } catch (Exception e) {

            }
        }

  /*      holder.layoutRight.setVisibility(bean.isBeSelf() ? View.VISIBLE : View.GONE);
        holder.layoutLeft.setVisibility(bean.isBeSelf() ? View.GONE : View.VISIBLE);
  */
    }


    private void setupView(HeaderViewHolder holder, int position) {

        MessageBean bean = messageBeanList.get(position);
        holder.headerTV.setText(bean.getMsgDate());

        /*if (position > 0) {
            String dateCurr = getDateByTimestamp(messageBeanList.get(position).getTimestamp());
            String dateprev = getDateByTimestamp(messageBeanList.get(position - 1).getTimestamp());
            if (dateCurr.equalsIgnoreCase(dateprev)) {
                holder.headerTV.setVisibility(View.GONE);
            } else {
                holder.headerTV.setVisibility(View.VISIBLE);
            }
        } else {
            holder.headerTV.setVisibility(View.VISIBLE);
        }*/
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

    /*    private final CircleImageView iconOtherName;
        private final TextView textViewOtherMsg;
        private final CircleImageView iconSelfName;
        private final TextView textViewSelfMsg;
        private final RelativeLayout layoutLeft;
        private final RelativeLayout layoutRight;
        private final TextView timeLeft;
        private final TextView timeRight;


        private CardView cv_l, cv_r, cv_ss_r;
        private LinearLayout ll_l, ll_r;
        private ImageView img_r, img_ss_r, img_l;*/

        TextView tv_msg;
        ImageView img_profilepic, img_image;
        CardView cv_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_msg = ((TextView) itemView.findViewById(R.id.tv_msg));
            img_profilepic = ((ImageView) itemView.findViewById(R.id.img_profilepic));
            img_image = ((ImageView) itemView.findViewById(R.id.img_image));
            cv_image = ((CardView) itemView.findViewById(R.id.cv_image));

        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView headerTV;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTV = itemView.findViewById(R.id.headerTV);
        }
    }

    public static String getTimeByTimestamp(String dateInMilliseconds) {
        String dateFormat = "hh:mm a";
        try {
            return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateByTimestamp(String dateInMilliseconds) {
        String dateFormat = "dd/MM/yyyy";
        try {
            return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    int getGiftImage(String id) {
        int imgResource = 0;
        if (id.equals("18")) {
            imgResource = R.drawable.heart;

        } else if (id.equals("21")) {
            imgResource = R.drawable.lips;

        } else if (id.equals("22")) {
            imgResource = R.drawable.bunny;

        } else if (id.equals("23")) {
            imgResource = R.drawable.rose;

        } else if (id.equals("24")) {
            imgResource = R.drawable.boygirl;

        } else if (id.equals("25")) {
            imgResource = R.drawable.sandle;

        } else if (id.equals("26")) {
            imgResource = R.drawable.frock;

        } else if (id.equals("27")) {
            imgResource = R.drawable.car;

        } else if (id.equals("28")) {
            imgResource = R.drawable.ship;

        } else if (id.equals("29")) {
            imgResource = R.drawable.tajmahal;
        } else if (id.equals("30")) {
            imgResource = R.drawable.crown;
        } else if (id.equals("31")) {
            imgResource = R.drawable.bracket;
        } else if (id.equals("32")) {
            imgResource = R.drawable.diamondgift;
        } else if (id.equals("33")) {
            imgResource = R.drawable.lovegift;
        }
        return imgResource;
    }
    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}
