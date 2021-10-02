package com.skilbox.bnetapi.adapter


import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.skilbox.bnetapi.Note

class NoteAdapter(
    val onItemClick: (noteId: String) -> Unit
) : AsyncListDifferDelegationAdapter<Note>(PokemonDiffUtilCallBack()) {

    init {
        delegatesManager.addDelegate(NoteAdapterDelegate(onItemClick))
    }

    class PokemonDiffUtilCallBack : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
}
