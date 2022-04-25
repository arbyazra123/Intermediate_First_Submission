package com.kmm.intermediatefirstsubmission.ui.pages.landing.story

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.ChangeBounds
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.kmm.intermediatefirstsubmission.R
import com.kmm.intermediatefirstsubmission.data.story.model.ListStoryResponseItem
import com.kmm.intermediatefirstsubmission.databinding.FragmentDetailStoryPageBinding
import java.text.SimpleDateFormat
import java.util.*

class DetailStoryPage : Fragment() {
    companion object {
        const val DATA = "data"
    }

    private lateinit var binding: FragmentDetailStoryPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 500
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = 500
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailStoryPageBinding.inflate(layoutInflater)
        arguments?.getParcelable<ListStoryResponseItem>(DATA)?.let {
            binding.imageView.transitionName = "${it.id}image"
            binding.nameTextView.transitionName = "${it.id}name"
            binding.createdDate.transitionName = "${it.id}date"
            binding.descTextView.transitionName = "${it.id}desc"
            binding.nameTextView.text = it.name
            binding.descTextView.text = it.description
            binding.createdDate.text =
                    it.createdAt?.let { it1 ->
                        SimpleDateFormat(getString(R.string.isoFormat), Locale.getDefault()).parse(
                            it1
                        )?.let { it2 ->
                            SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault()).format(it2)
                        }
                    }
            Glide.with(binding.root).load(it.photoUrl)
                .apply(RequestOptions().transform(RoundedCorners(15)))
                .into(binding.imageView)
        }
        return binding.root
    }

}