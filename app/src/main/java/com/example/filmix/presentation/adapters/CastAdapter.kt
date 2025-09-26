package com.example.filmix.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.filmix.R
import com.example.filmix.databinding.ItemVerticalCastBinding
import com.example.filmix.domain.models.CastMember

class CastAdapter(
    private var castList: List<CastMember>,
    private val onCastClick: ((CastMember) -> Unit)? = null
) : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = ItemVerticalCastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CastViewHolder(binding)
    }

    override fun getItemCount(): Int = castList.size.coerceAtMost(15)

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(castList[position])
    }

    fun updateCast(newCastList: List<CastMember>) {
        castList = newCastList
        notifyDataSetChanged()
    }

    inner class CastViewHolder(
        private val binding: ItemVerticalCastBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cast: CastMember) {
            binding.tvActorName.text = cast.name
            binding.tvCharacterName.text = cast.character

            val imageUrl = if (cast.profilePath.isNotEmpty()) {
                "https://image.tmdb.org/t/p/w185${cast.profilePath}"
            } else null

            Glide.with(binding.root.context)
                .load(imageUrl)
                .apply(RequestOptions()
                    .transform(RoundedCorners(12))
                    .placeholder(R.drawable.ic_person_placeholder)
                    .error(R.drawable.ic_person_placeholder)
                )
                .into(binding.ivActorPhoto)

            binding.root.setOnClickListener {
                onCastClick?.invoke(cast)
            }
        }
    }
}
