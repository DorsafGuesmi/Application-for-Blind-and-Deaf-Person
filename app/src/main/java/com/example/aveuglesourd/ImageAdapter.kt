package com.example.aveuglesourd


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aveuglesourd.ImageAdapter.ImageViewHolder
import com.squareup.picasso.Picasso

class ImageAdapter(
    private val mContext: Context,
    private val mUploads: List<Upload>
) : RecyclerView.Adapter<ImageViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ImageViewHolder {
        val v =
            LayoutInflater.from(mContext).inflate(R.layout.image_item, viewGroup, false)
        return ImageViewHolder(v)
    }

    override fun onBindViewHolder(imageViewHolder: ImageViewHolder, i: Int) {
        val uploadCur = mUploads[i]
        imageViewHolder.img_description.text = uploadCur.imgName
        Picasso.get()
            .load(uploadCur.imgUrl)
            .placeholder(R.drawable.upp)
            .fit()
            .centerCrop()
            .into(imageViewHolder.image_view)
    }

    override fun getItemCount(): Int {
        return mUploads.size
    }

    inner class ImageViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var img_description: TextView
        var image_view: ImageView

        init {
            img_description = itemView.findViewById(R.id.img_description)
            image_view = itemView.findViewById(R.id.image_view)
        }
    }

}