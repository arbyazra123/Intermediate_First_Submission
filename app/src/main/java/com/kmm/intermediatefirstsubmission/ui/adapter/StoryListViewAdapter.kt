package com.kmm.intermediatefirstsubmission.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.kmm.intermediatefirstsubmission.R
import com.kmm.intermediatefirstsubmission.data.story.model.ListStoryResponseItem
import com.kmm.intermediatefirstsubmission.databinding.StoryItemBinding
import com.kmm.intermediatefirstsubmission.utility.limitWords
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StoryListViewAdapter internal constructor(
    private val itemClickListener: IOnStoryItemClick
) :
    RecyclerView.Adapter<StoryListViewAdapter.StoryViewHolder>() {
    private var stories: ArrayList<ListStoryResponseItem> = arrayListOf()
    fun setData(stories: ArrayList<ListStoryResponseItem>) {
        this.stories.clear()
        this.stories = stories
    }

    inner class StoryViewHolder(val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(position: Int) {
            val story = stories[position]
            binding.storyImageView.transitionName = "${story.id}image"
            binding.nameTextView.transitionName = "${story.id}name"
            binding.descTextView.transitionName = "${story.id}desc"
            binding.createdDate.transitionName = "${story.id}date"
            binding.nameTextView.text = story.name
            binding.descTextView.text = story.description?.limitWords()
            binding.createdDate.text = story.createdAt?.let { it1 ->
                SimpleDateFormat(
                    binding.root.context.getString(R.string.isoFormat),
                    Locale.getDefault()
                ).parse(
                    it1
                )?.let { it2 ->
                    SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault()).format(it2)
                }
            }
            Glide.with(itemView)
                .load(stories[position].photoUrl)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(15)))
                .into(binding.storyImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(
            StoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(holder.binding, stories[position])
        }
        holder.binding(position)
    }

    override fun getItemCount(): Int {
        return stories.size
    }
}