package com.studio.king.demomovie.adapter.holder

import com.studio.king.demomovie.R
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.databinding.ViewItemGenreHomeHolderBinding
import com.studio.king.demomovie.model.GenresModel
import com.studio.king.demomovie.utils.LogUtil
import org.koin.core.component.KoinComponent
import java.security.SecureRandom
import kotlin.random.Random

class GenreHomeHolder(itemView: ViewItemGenreHomeHolderBinding) : LifecycleViewHolder(itemView.root),
    KoinComponent {

    private val binding: ViewItemGenreHomeHolderBinding = itemView
    private var mGenresModel: GenresModel? = null
    override fun setData(
        t: Any?,
        position: Int,
        adapter: BaseAdapterAny
    ) {
        if (t is GenresModel) {
            mGenresModel = t
            buildUITitle()
            buildUIBackground()
        }

    }

    private fun buildUITitle() {
        binding.textTitleGenreHome.text = mGenresModel?.name ?: ""
    }

    private fun buildUIBackground() {
        when (SecureRandom().nextInt(3)) {
            0 -> {
                binding.imageGenreHome.setBackgroundResource(R.drawable.gradient_type_1)
            }
            1 -> {
                binding.imageGenreHome.setBackgroundResource(R.drawable.gradient_type_2)
            }
            2 -> {
                binding.imageGenreHome.setBackgroundResource(R.drawable.gradient_type_3)
            }
        }
    }
}