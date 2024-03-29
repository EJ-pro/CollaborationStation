package com.example.collaborationstation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collaborationstation.CommonUtil.userName
import com.example.collaborationstation.databinding.ItemChatListBinding
import com.bumptech.glide.Glide


class ChatListAdapter : RecyclerView.Adapter<ChatListAdapter.ListViewHolder>() {
    companion object {
        const val TAG = "ChatListAdapter"
    }

    var chatItems: List<ChatMessageEntity> = listOf()


    inner class ListViewHolder(private val viewBinding: ItemChatListBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: ChatMessageEntity) = if (item.name.equals(userName)) {  // 나의 msg
            viewBinding.llChatCard.setBackgroundResource(R.color.teal_100)  // 카드뷰 배경색
            viewBinding.llChatLeft.visibility = View.GONE
            viewBinding.llChatRight.visibility = View.VISIBLE
            viewBinding.clChatLeft.visibility = View.GONE
            viewBinding.clChatRight.visibility = View.VISIBLE
            Glide.with(viewBinding.root)
                .load(R.drawable.ic_launcher_bear)
                .circleCrop()
                .into(viewBinding.ivProfileRight)  // imageView 둥글게
            viewBinding.tvUserRight.text = item.name
            viewBinding.tvMsgRight.text = item.content
            viewBinding.tvTimeRight.text = item.timestamp
        } else {  // 다른사람의 msg
            viewBinding.llChatCard.setBackgroundResource(R.color.deep_orange_100)  // 카드뷰 배경색
            viewBinding.llChatLeft.visibility = View.VISIBLE
            viewBinding.llChatRight.visibility = View.GONE
            viewBinding.clChatLeft.visibility = View.VISIBLE
            viewBinding.clChatRight.visibility = View.GONE
            Glide.with(viewBinding.root)
                .load(R.drawable.ic_launcher_bear)
                .circleCrop()
                .into(viewBinding.ivProfile)  // imageView 둥글게
            viewBinding.tvUser.text = item.name
            viewBinding.tvMsg.text = item.content
            viewBinding.tvTime.text = item.timestamp
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ItemChatListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(chatItems[position])
    }

    override fun getItemCount(): Int {
        return chatItems.size
    }

    fun setChatItems(list: Any) {
        this.chatItems = list as List<ChatMessageEntity>
        notifyDataSetChanged()
    }
}
