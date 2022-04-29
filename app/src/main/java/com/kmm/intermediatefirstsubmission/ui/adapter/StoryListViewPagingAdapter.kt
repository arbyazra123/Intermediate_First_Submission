package com.kmm.intermediatefirstsubmission.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
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

class StoryListViewPagingAdapter(private val iOnStoryItemClick: IOnStoryItemClick) :
    PagingDataAdapter<ListStoryResponseItem, StoryListViewPagingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.binding(data)

        }
    }

    inner class MyViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(story: ListStoryResponseItem) {
            itemView.setOnClickListener {
                iOnStoryItemClick.onClick(binding, story)
            }
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
                .load(story.photoUrl)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(15)))
                .into(binding.storyImageView)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryResponseItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryResponseItem,
                newItem: ListStoryResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryResponseItem,
                newItem: ListStoryResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}