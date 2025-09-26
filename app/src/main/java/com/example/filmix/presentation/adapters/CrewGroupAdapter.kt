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
import com.example.filmix.databinding.ItemVerticalCrewGroupBinding
import com.example.filmix.domain.models.CrewMember

class CrewGroupAdapter(
    private var crewList: List<CrewMember>,
    private val onCrewClick: ((CrewMember) -> Unit)? = null
) : RecyclerView.Adapter<CrewGroupAdapter.CrewGroupViewHolder>() {

    private val groupedCrew get() = crewList.chunked(3)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewGroupViewHolder {
        val binding = ItemVerticalCrewGroupBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CrewGroupViewHolder(binding)
    }

    override fun getItemCount(): Int = groupedCrew.size

    override fun onBindViewHolder(holder: CrewGroupViewHolder, position: Int) {
        val crewGroup = groupedCrew[position]
        holder.bind(crewGroup, onCrewClick)
    }

    fun updateCrew(newCrewList: List<CrewMember>) {
        crewList = newCrewList.take(15)
        notifyDataSetChanged()
    }

    inner class CrewGroupViewHolder(
        private val binding: ItemVerticalCrewGroupBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(crewGroup: List<CrewMember>, onCrewClick: ((CrewMember) -> Unit)?) {
            bindCrewMember(crewGroup.getOrNull(0), binding.crew1.root, onCrewClick)
            bindCrewMember(crewGroup.getOrNull(1), binding.crew2.root, onCrewClick)
            bindCrewMember(crewGroup.getOrNull(2), binding.crew3.root, onCrewClick)
        }

        private fun bindCrewMember(
            crewMember: CrewMember?,
            crewView: View,
            onCrewClick: ((CrewMember) -> Unit)?
        ) {
            if (crewMember != null) {
                crewView.visibility = View.VISIBLE

                val photo = crewView.findViewById<ImageView>(R.id.iv_crew_photo)
                val name = crewView.findViewById<TextView>(R.id.tv_crew_name)
                val job = crewView.findViewById<TextView>(R.id.tv_crew_job)

                name.text = crewMember.name
                job.text = crewMember.job

                val imageUrl = if (crewMember.profilePath.isNotEmpty()) {
                    "https://image.tmdb.org/t/p/w185${crewMember.profilePath}"
                } else null

                Glide.with(itemView.context)
                    .load(imageUrl)
                    .apply(RequestOptions()
                        .transform(RoundedCorners(12))
                        .placeholder(R.drawable.ic_person_placeholder)
                        .error(R.drawable.ic_person_placeholder)
                    )
                    .into(photo)

                crewView.setOnClickListener {
                    onCrewClick?.invoke(crewMember)
                }
            } else {
                crewView.visibility = View.GONE
            }
        }
    }
}
