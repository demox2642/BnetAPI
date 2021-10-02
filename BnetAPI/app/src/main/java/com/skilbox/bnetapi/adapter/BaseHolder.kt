package com.skilbox.bnetapi.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.note_item_list_view.view.*
import java.text.SimpleDateFormat
import java.util.*

abstract
class BaseHolder(
    view: View,
    private val onItemCkick: (noteId: String) -> Unit
) : RecyclerView.ViewHolder(view), LayoutContainer {

    protected fun bindMainInfo(
        id: String,
        body: String,
        da: Long,
        dm: Long
    ) {
        itemView.da.text = "DA: $da"
        itemView.dm.text = if (dm == da) {
            ""
        } else { "DM:${getDateTime(dm)}" }
        itemView.text_note.text = body
        itemView.setOnClickListener {
            onItemCkick(id)
        }
    }

    private fun getDateTime(s: Long): String {
        try {
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            val netDate = Date(s * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
}
