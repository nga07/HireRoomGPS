package com.example.finalapplication.screen

import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.finalapplication.R
import com.example.finalapplication.data.model.Contact
import com.example.finalapplication.data.model.Message
import com.example.finalapplication.data.model.User
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.getNewid
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TestActivity : AppCompatActivity() {
    private var lastdoc: DocumentSnapshot? = null
    private val db = Firebase.firestore
    private lateinit var button: Button
    private lateinit var dialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        button = findViewById(R.id.button)
        dialog = ProgressDialog(this)
        button.setOnClickListener {
            dialog.show()
            getData()

        }
    }

    fun getData(){
        db.collection(Contact.contacts)
            .whereEqualTo("uid","5k2ASy2jjPefqMptArYdfhlefh72")
            .orderBy("message.time", Query.Direction.DESCENDING)
            .limit(10L)
            .addSnapshotListener { value, error ->
                if(error!=null){
                    Log.v("aaaa", error.toString())
                    dialog.cancel()
                    return@addSnapshotListener
                }
                if (value != null) {
                    for(doc in value.documents){
                        val contact = doc.toObject(Contact::class.java)
                        Log.v("aaaa", contact?.message?.text.toString())
                    }
                    dialog.cancel()
                    Log.v("aaaa", "have data : ${value.documents.size}")
                }
            }
    }

    fun addData() {

        val user = User()
        user.id = "0PF7t6MD57TOYiUGhJdwMt6ouC72"

        val message = Message()
        message.id = getNewid().toString()
        message.text = "hellllllo"
        message.chatType = "chatbox"
        message.time = System.currentTimeMillis()
        message.reciverId = "5k2ASy2jjPefqMptArYdfhlefh72"
        message.senderId ="0PF7t6MD57TOYiUGhJdwMt6ouC72"

        val contact = Contact(
            "5k2ASy2jjPefqMptArYdfhlefh720PF7t6MD57TOYiUGhJdwMt6ouC72",
            "5k2ASy2jjPefqMptArYdfhlefh72",user,message
        )

        db.collection(Contact.contacts)
            .document(contact.id.toString())
            .set(contact)
    }
}