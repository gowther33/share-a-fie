package com.example.share_a_file.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.share_a_file.PeersModel
import com.example.test.databinding.ItemViewBinding

class PeersRecyclerViewAdapter(
    private val peersList:ArrayList<PeersModel>,
    val onItemClick: (PeersModel) -> Unit
):RecyclerView.Adapter<PeersRecyclerViewAdapter.PeersViewHolder>() {


    inner class PeersViewHolder(
        private val binding:ItemViewBinding,
        itemClick: (Int) -> Unit
        ):RecyclerView.ViewHolder(binding.root){

            init {
                binding.itemLayout.setOnClickListener {
                    itemClick(adapterPosition)
                }
            }

        private val tvId = binding.tvDeviceId

        fun bind(item:PeersModel){
            tvId.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeersViewHolder {
        val binding = ItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PeersViewHolder(binding){
            onItemClick(peersList[it])
        }
    }

    override fun onBindViewHolder(holder: PeersViewHolder, position: Int) {
        val item = peersList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return peersList.size
    }
}