package com.example.aveuglesourd.recycleview

import android.content.Context
import com.bumptech.glide.Glide
import com.example.aveuglesourd.R
import com.example.aveuglesourd.Util.StorageUtil
import com.example.aveuglesourd.model.User

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_person.*
// contient les données des personnes

class PersonItem(val person: User,
                 val userId: String,
                 private val context: Context)
    : Item() {

    //get les text view(bio,name)

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView_name.text = person.name
        viewHolder.textView_bio.text = person.bio

        // si le chemain de l'image existe l'image doit se déplacer en image view
        if (person.profilePicturePath != null)

        // téléchrger la photo
            Glide.with(context)
                .load(StorageUtil.pathToReference(person.profilePicturePath))
                .placeholder(R.drawable.peoples)
                .into(viewHolder.imageView_profile_picture)
    }

    override fun getLayout() = R.layout.item_person
}