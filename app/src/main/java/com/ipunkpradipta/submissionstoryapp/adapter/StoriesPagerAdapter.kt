package com.ipunkpradipta.submissionstoryapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ipunkpradipta.submissionstoryapp.ui.stories.DetailStoryActivity
import com.ipunkpradipta.submissionstoryapp.R
import com.ipunkpradipta.submissionstoryapp.network.StoryItem
import com.ipunkpradipta.submissionstoryapp.utils.loadImage

class StoriesPagerAdapter: PagingDataAdapter<StoryItem,StoriesPagerAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.card_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }

    }


    class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        private val ivPhotoUrl: ImageView = view.findViewById(R.id.iv_item_photo)
        private val tvName: TextView = view.findViewById(R.id.tv_item_name)

        fun bind(data:StoryItem){
            ivPhotoUrl.loadImage(data.photoUrl)
            tvName.text = data.name
            itemView.setOnClickListener{
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_PHOTO_URL,data.photoUrl)
                intent.putExtra(DetailStoryActivity.EXTRA_NAME,data.name)
                intent.putExtra(DetailStoryActivity.EXTRA_DESCRIPTION,data.description)
                intent.putExtra(DetailStoryActivity.EXTRA_CREATED_AT,data.createdAt)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity, Pair(ivPhotoUrl,"PhotoUrl"),Pair(tvName,"name"))
                itemView.context.startActivity(intent,options.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}