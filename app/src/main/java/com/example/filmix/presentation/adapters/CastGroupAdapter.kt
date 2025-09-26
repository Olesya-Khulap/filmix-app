package com.example.filmix.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.filmix.R
import com.example.filmix.databinding.ItemVerticalCastGroupBinding
import com.example.filmix.domain.models.CastMember

class CastGroupAdapter(
    private var castList: List<CastMember>,
    private val onCastClick: ((CastMember) -> Unit)? = null
) : RecyclerView.Adapter<CastGroupAdapter.CastGroupViewHolder>() {

    private val groupedCast get() = castList.chunked(3)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastGroupViewHolder {
        val binding = ItemVerticalCastGroupBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CastGroupViewHolder(binding)
    }

    override fun getItemCount(): Int = groupedCast.size

    override fun onBindViewHolder(holder: CastGroupViewHolder, position: Int) {
        val castGroup = groupedCast[position]
        holder.bind(castGroup, onCastClick)
    }

    fun updateCast(newCastList: List<CastMember>) {
        castList = newCastList.take(15)
        notifyDataSetChanged()
    }

    inner class CastGroupViewHolder(
        private val binding: ItemVerticalCastGroupBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(castGroup: List<CastMember>, onCastClick: ((CastMember) -> Unit)?) {
            bindCastMember(castGroup.getOrNull(0), binding.cast1.root, onCastClick)
            bindCastMember(castGroup.getOrNull(1), binding.cast2.root, onCastClick)
            bindCastMember(castGroup.getOrNull(2), binding.cast3.root, onCastClick)
        }

        private fun bindCastMember(
            castMember: CastMember?,
            castView: View,
            onCastClick: ((CastMember) -> Unit)?
        ) {
            if (castMember != null) {
                castView.visibility = View.VISIBLE

                val photo = castView.findViewById<ImageView>(R.id.iv_actor_photo)
                val name = castView.findViewById<TextView>(R.id.tv_actor_name)
                val character = castView.findViewById<TextView>(R.id.tv_character_name)

                name.text = castMember.name
                character.text = castMember.character

                val imageUrl = if (castMember.profilePath.isNotEmpty()) {
                    "https://image.tmdb.org/t/p/w185${castMember.profilePath}"
                } else null

                Glide.with(itemView.context)
                    .load(imageUrl)
                    .apply(RequestOptions()
                        .transform(RoundedCorners(12))
                        .placeholder(R.drawable.ic_person_placeholder)
                        .error(R.drawable.ic_person_placeholder)
                    )
                    .into(photo)

                castView.setOnClickListener {
                    onCastClick?.invoke(castMember)
                }
            } else {
                castView.visibility = View.GONE
            }
        }
    }
}
