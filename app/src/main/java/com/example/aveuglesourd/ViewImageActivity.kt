package com.example.aveuglesourd


import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*

class ViewImageActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView
    private var mAdapter: ImageAdapter? = null
    private lateinit var progressBar: ProgressBar
    private var mDatabaseRef: DatabaseReference? = null
    private var mUploads: MutableList<Upload>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)
        mRecyclerView = findViewById(R.id.recycler_view)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        progressBar = findViewById(R.id.progress_circular)
        mUploads = ArrayList()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads")
        mDatabaseRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val upload = postSnapshot.getValue(Upload::class.java)!!
                    (mUploads as ArrayList<Upload>).add(upload)
                }
                mAdapter = ImageAdapter(this@ViewImageActivity, mUploads as ArrayList<Upload>)
                mRecyclerView.setAdapter(mAdapter)
                progressBar.setVisibility(View.INVISIBLE)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@ViewImageActivity,
                    databaseError.message,
                    Toast.LENGTH_LONG
                ).show()
                progressBar.setVisibility(View.INVISIBLE)
            }
        })
    }
}