package com.example.aveuglesourd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.aveuglesourd.fragment.MyAccountFragment
import com.example.aveuglesourd.fragment.PeopleFragment
import kotlinx.android.synthetic.main.activity_communication.*


class communication : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(PeopleFragment())
        nav_view.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                //it c'est la reference de menu item
                R.id.navigation_amis -> {
                    //si itemId est navigation_amis ouvrir fragment amis
                    replaceFragment(PeopleFragment())
                    true
                }
                R.id.navigation_account -> {
                    //si itemId est navigation_amis ouvrir fragment mon compte
                    replaceFragment(MyAccountFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}