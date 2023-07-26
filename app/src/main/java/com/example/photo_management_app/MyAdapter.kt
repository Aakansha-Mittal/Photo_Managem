package com.example.photo_management_app

import android.app.Activity
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.photo_management_app.MyAdapter.MyViewHolder

class MyAdapter(val context: Activity, val imagePaths: ArrayList<Image>)
    : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val viewHolder=holder as RecyclerView.ViewHolder
        val Image=imagePaths.get(position)

        val bitmap = MediaStore.Images.Thumbnails.getThumbnail(holder.img.context.contentResolver,
            Image.id, 2, null)
        holder.img.setImageBitmap(bitmap)
    }


    fun submitList(list: ArrayList<com.example.photo_management_app.Image>) {
            imagePaths.clear()
            imagePaths.addAll(list)
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return imagePaths.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val img : ImageView
        init {
            img = itemView.findViewById(R.id.img)
        }
    }

}