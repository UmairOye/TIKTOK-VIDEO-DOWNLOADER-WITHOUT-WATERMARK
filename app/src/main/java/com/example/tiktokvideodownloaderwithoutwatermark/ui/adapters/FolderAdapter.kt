package com.example.tiktokvideodownloaderwithoutwatermark.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktokvideodownloaderwithoutwatermark.databinding.ListFoldersBinding
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.FolderModel

class FolderAdapter : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    private var downloadList: ArrayList<FolderModel> = ArrayList()
    private var listener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = ListFoldersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return downloadList.size
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        holder.bind(downloadList[position])
        holder.cdMain.setOnClickListener {
            listener?.onItemClick(downloadList[position])
        }

    }

    class FolderViewHolder(private val binding: ListFoldersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val cdMain = binding.cdMain
        fun bind(item: FolderModel) {
           binding.tvFolderName.text = item.name
            binding.tvNoOfItems.text = "(${item.noOfItems})"
        }
    }

    fun submitList(list: ArrayList<FolderModel>) {
        this.downloadList = list
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onItemClick(item: FolderModel)
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

}