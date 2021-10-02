package com.skilbox.bnetapi.adapter

import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.skilbox.bnetapi.Note
import com.skilbox.bnetapi.R
import com.skilbox.bnetapi.plugins.inflate
import java.util.*

class NoteAdapterDelegate(
    val onItemClick: (item: String) -> Unit
) : AbsListItemAdapterDelegate<Note, Note, NoteAdapterDelegate.NoteClassHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): NoteClassHolder {
        return NoteClassHolder(parent.inflate(R.layout.note_item_list_view), onItemClick)
    }

    class NoteClassHolder(
        view: View,
        onItemClick: (noteId: String) -> Unit
    ) : BaseHolder(view, onItemClick) {

        fun bind(data: Note) {
            bindMainInfo(
                id = data.id,
                dm = data.dm,
                da = data.da,
                body = data.body
            )
        }

        override val containerView: View
            get() = itemView
    }

    override fun isForViewType(
        item: Note,
        items: MutableList<Note>,
        position: Int
    ): Boolean {
        return items[position] is Note
    }

    override fun onBindViewHolder(
        item: Note,
        holder: NoteClassHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }
}
