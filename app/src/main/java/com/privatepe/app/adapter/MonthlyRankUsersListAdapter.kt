package com.privatepe.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.privatepe.app.R
import com.privatepe.app.model.InvitationRewardReponse
import java.util.Locale

class MonthlyRankUsersListAdapter(
    var context: Context,
    var list: List<InvitationRewardReponse.Result.Data>
) : RecyclerView.Adapter<MonthlyRankUsersListAdapter.myViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.invitation_rewards_list_item, parent, false)
        return myViewHolder(v)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {

        if (position == 0) {
            holder.ivPosition.setBackgroundResource(R.drawable.daily_first)
            holder.ivPosition.visibility = View.VISIBLE
            holder.tvPosition.visibility = View.GONE
        } else if (position == 1) {
            holder.ivPosition.setBackgroundResource(R.drawable.daily_second)
            holder.ivPosition.visibility = View.VISIBLE
            holder.tvPosition.visibility = View.GONE
        } else if (position == 2) {
            holder.ivPosition.setBackgroundResource(R.drawable.daily_third)
            holder.ivPosition.visibility = View.VISIBLE
            holder.tvPosition.visibility = View.GONE
        } else {
            holder.ivPosition.visibility = View.GONE
            holder.tvPosition.visibility = View.VISIBLE
            holder.tvPosition.text = (position + 1).toString()
        }
        holder.tvCount.text = list[position].monthlyReferalEarn?.toString()
        holder.tvUserName.text = list[position].name?.lowercase(Locale.getDefault())
        holder.tvCharmLevel.text = "Lvl. "+list[position].richLevel.toString()
       // holder.rlBg.background = context.resources.getDrawable(getMaleLevelImage(list[position].richLevel))
        try {
            Glide.with(context)
                .load(list[position].profileImagesDefault?.imageName)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.fake_user_icon)
                .placeholder(R.drawable.fake_user_icon)
                .dontTransform()
                .override(200, 200)
                .into(holder.ivUserImage)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvPosition: TextView
        var tvUserName: TextView
        var tvCount: TextView
        var tvCharmLevel: TextView
        var llParent: LinearLayout
        var ivPosition: ImageView
        var ivUserImage: ImageView
        var rlBg: RelativeLayout

        init {
            ivPosition = itemView.findViewById(R.id.ivPosition)
            ivUserImage = itemView.findViewById(R.id.ivUserImage)
            tvPosition = itemView.findViewById(R.id.tvPosition)
            tvUserName = itemView.findViewById(R.id.tvUserName)
            tvCount = itemView.findViewById(R.id.tvCount)
            tvCharmLevel = itemView.findViewById(R.id.tvCharmLevel)
            llParent = itemView.findViewById(R.id.llParent)
            rlBg = itemView.findViewById(R.id.rl_bg)
        }
    }
}
