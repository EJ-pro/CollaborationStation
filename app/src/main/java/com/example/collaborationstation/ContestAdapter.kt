package com.example.collaborationstation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContestAdapter(private val contestList: List<Contest>) : RecyclerView.Adapter<ContestAdapter.ContestViewHolder>() {

    inner class ContestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val applicationDeadlineTextView: TextView = itemView.findViewById(R.id.applicationDeadlineTextView)
        val scheduleDeadlineTextView: TextView = itemView.findViewById(R.id.scheduleDeadlineTextView)
        val scheduleStartTextView: TextView = itemView.findViewById(R.id.scheduleStartTextView)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        val eligibilityTextView: TextView = itemView.findViewById(R.id.eligibilityTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContestViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_team_matching_tab1_item, parent, false)
        return ContestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContestViewHolder, position: Int) {
        val currentContest = contestList[position]
        holder.nameTextView.text = currentContest.name
        holder.descriptionTextView.text = currentContest.description
        holder.applicationDeadlineTextView.text = currentContest.applicationDeadline
        holder.scheduleDeadlineTextView.text = currentContest.scheduleDeadline
        holder.scheduleStartTextView.text = currentContest.scheduleStart
        holder.locationTextView.text = currentContest.location
        holder.eligibilityTextView.text = currentContest.eligibility
    }

    override fun getItemCount() = contestList.size
}