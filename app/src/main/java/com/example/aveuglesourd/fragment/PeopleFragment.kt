package com.example.aveuglesourd.fragment

import android.os.Bundle

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aveuglesourd.R
import com.example.aveuglesourd.Util.FirestoreUtil
import com.example.aveuglesourd.recycleview.PersonItem


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aveuglesourd.AppConstants
import com.example.aveuglesourd.ChatActivity
import com.google.firebase.firestore.ListenerRegistration


import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_people.*
import org.jetbrains.anko.startActivity


class PeopleFragment : Fragment() {

    private lateinit var userListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var peopleSection: Section


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        userListenerRegistration =
            FirestoreUtil.addUsersListener(this.requireActivity(), this::updateRecyclerView)

        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FirestoreUtil.removeListener(userListenerRegistration)
        shouldInitRecyclerView = true
    }
//ajout personne(item)
    private fun updateRecyclerView(items: List<Item>) {

        fun init() {
            recycler_view_people.apply {
                layoutManager = LinearLayoutManager(this@PeopleFragment.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = peopleSection.update(items)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

    }

    private val onItemClick = OnItemClickListener { item, view ->
        if (item is PersonItem) {

            context!!.startActivity<ChatActivity>(
                AppConstants.USER_NAME to item.person.name,
                AppConstants.USER_ID to item.userId
            )
        }
    }

}