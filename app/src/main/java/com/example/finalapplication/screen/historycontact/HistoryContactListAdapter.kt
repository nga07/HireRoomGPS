package com.example.finalapplication.screen.historycontact

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.finalapplication.data.model.Contact
import com.example.finalapplication.data.model.Message
import com.example.finalapplication.data.model.User
import com.example.finalapplication.databinding.ItemContactBinding
import com.example.finalapplication.databinding.ItemLoadMoreBinding
import com.example.finalapplication.screen.search.SearchListAdapter
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseViewHolder
import com.example.finalapplication.utils.loadImageByUrl
import com.example.finalapplication.utils.toDateString

class HistoryContactListAdapter(
    private val itemClickListener: ItemRecyclerViewListenner<Contact>
) :
    ListAdapter<Contact, BaseViewHolder<Any>>(Contact.diffCallback) {

    var haveNextPage = true

    override fun getItemViewType(position: Int): Int {
        if (currentList.size > 0 && position == currentList.size && haveNextPage) return TYPE_LOAD
        return TYPE_DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Any> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_LOAD ->
                LoadViewHolder(ItemLoadMoreBinding.inflate(layoutInflater, parent, false))
            else ->
                ContactViewHolder(ItemContactBinding.inflate(layoutInflater, parent, false))
        }
    }

    override fun getItemCount(): Int {
        if (haveNextPage && currentList.size > 0) return currentList.size + 1
        return currentList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Any>, position: Int) {
        if (currentList.size <= position) return
        holder.bind(getItem(position))
    }


    inner class LoadViewHolder(val binding: ItemLoadMoreBinding) : BaseViewHolder<Any>(binding) {

        override fun bind(item: Any) {
            // TODO("Not yet implemented")
        }
    }

    inner class ContactViewHolder(val binding: ItemContactBinding) : BaseViewHolder<Any>(binding) {

        override fun bind(item: Any) {
            val contact = item as Contact
            binding.apply {
                root.setOnClickListener {
                    itemClickListener.onItemClick(contact)
                }
                circleAvatar.loadImageByUrl(contact.adversaryUser?.avatar, binding.root.context)
                if (contact.message?.seen == false && contact.uid == contact.message?.reciverId) {
                    textUsername.setTextColor(Color.BLACK)
                    textUsername.setTypeface(null, Typeface.BOLD)
                    textMessage.setTextColor(Color.BLACK)
                    textMessage.setTypeface(null, Typeface.BOLD)
                    textTime.setTypeface(null, Typeface.BOLD)
                } else {
                    textUsername.setTextColor(Color.GRAY)
                    textUsername.setTypeface(null, Typeface.NORMAL)
                    textMessage.setTextColor(Color.GRAY)
                    textMessage.setTypeface(null, Typeface.NORMAL)
                    textTime.setTypeface(null, Typeface.NORMAL)
                }
                textUsername.text = contact.adversaryUser?.name
                textTime.text = contact.message?.time?.toDateString().toString()
                when (contact.message?.chatType) {
                    Message.chatbox -> {
                        textMessage.text = if (contact.message?.text.isNullOrEmpty()) Message.image
                        else contact.message?.text
                    }
                    Message.call -> textMessage.text = Message.call
                    else -> textMessage.text = Message.video
                }
            }
        }
    }

    companion object {
        const val TYPE_DATA = 1
        const val TYPE_LOAD = 2
    }
}
