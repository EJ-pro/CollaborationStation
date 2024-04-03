package com.example.collaborationstation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage

class ContestAdapter(private val contestList: List<Contest>) : RecyclerView.Adapter<ContestAdapter.ContestViewHolder>() {

    inner class ContestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val applicationDeadlineTextView: TextView = itemView.findViewById(R.id.applicationDeadlineTextView)
        val scheduleDeadlineTextView: TextView = itemView.findViewById(R.id.scheduleDeadlineTextView)
        val scheduleStartTextView: TextView = itemView.findViewById(R.id.scheduleStartTextView)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        val eligibilityTextView: TextView = itemView.findViewById(R.id.eligibilityTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
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

        val imageReference = currentContest.imageReference
        if (!imageReference.isNullOrEmpty()) {
            // Firebase Storage에서 이미지 다운로드 URL을 가져오고 Glide를 사용하여 이미지뷰에 이미지 설정
            val storageReference = FirebaseStorage.getInstance().reference.child(imageReference)
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()

                // Glide를 사용하여 이미지뷰에 이미지 설정
                Glide.with(holder.itemView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_item1) // 이미지를 로드하는 동안 보여줄 이미지
                    .error(R.drawable.ic_launcher_bear) // 이미지 로드 실패 시 보여줄 이미지
                    .into(holder.imageView)
            }.addOnFailureListener { exception ->
                // 이미지 다운로드 URL을 가져오는데 실패한 경우 처리
            }
        } else {
            // 이미지 참조가 없는 경우 기본 이미지 설정
            Glide.with(holder.itemView.context)
                .load(R.drawable.logo)
                .placeholder(R.drawable.ic_item1)
                .error(R.drawable.logo)
                .into(holder.imageView)
        }
    }


    override fun getItemCount() = contestList.size
}