package com.mrcaracal.firebasefirestoredatabase2.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mrcaracal.firebasefirestoredatabase2.R
import com.mrcaracal.firebasefirestoredatabase2.adapter.UserAdapter
import com.mrcaracal.firebasefirestoredatabase2.databinding.ActivityHomeBinding
import com.mrcaracal.firebasefirestoredatabase2.model.UserData
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

private const val TAG = "HomeActivity"

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var documentReference: DocumentReference
    private lateinit var collectionReference: CollectionReference
    private lateinit var firestore: FirebaseFirestore
    private lateinit var datas: HashMap<String, String>
    val userDataArrayList: ArrayList<UserData> = ArrayList()
    private lateinit var userAdapter: UserAdapter
    private val COLLECTION_NAME = "EmployeeInformation"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        documentReference = firestore.collection(COLLECTION_NAME).document()
        collectionReference = firestore.collection(COLLECTION_NAME)
        userAdapter = UserAdapter(userDataArrayList)
        datas = HashMap()

        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = userAdapter

        binding.btnSaveUpdateByName.setOnClickListener {
            userDataArrayList.clear()
            val name = binding.edtSaveUpdateName.text.toString().lowercase(Locale.getDefault())
            val surname = binding.edtSaveUpdateSurname.text.toString().lowercase(Locale.getDefault())
            val age = binding.edtSaveUpdateAge.text.toString()

            if (name.equals("") || surname.equals("") || age.equals("")) {
                Log.i(TAG, "onCreate: Empty")
            } else {
                datas.put("name", name)
                datas.put("surname", surname)
                datas.put("age", age)

                documentReference = firestore.collection(COLLECTION_NAME).document(name + surname)
                saveAndUpdate()
            }
            binding.edtSaveUpdateName.setText("")
            binding.edtSaveUpdateSurname.setText("")
            binding.edtSaveUpdateAge.setText("")
        }

        binding.btnDeleteByName.setOnClickListener {
            userDataArrayList.clear()
            val nameSurname = binding.edtDeleteName.text.toString().lowercase(Locale.getDefault())
            if (nameSurname.equals("")) {
                Log.i(TAG, "onCreate: Empty")
            } else {
                delete(nameSurname)
            }
            binding.edtDeleteName.setText("")
        }

        binding.btnFilterName.setOnClickListener {
            val namefilt = binding.edtFilterName.text.toString().lowercase(Locale.getDefault())
            if (namefilt.equals("")) {
                Log.i(TAG, "onCreate: Empty")
            } else{
                filterName(namefilt)
            }
        }

        binding.btnFilterAge.setOnClickListener {
            val ageFilt = binding.edtFilterAge.text.toString()
            if (ageFilt.equals("")) {
                Log.i(TAG, "onCreate: Empty")
            } else{
                filterAge(ageFilt)
            }
        }
    }

    private fun filterAge(ageFilt: String) {
        collectionReference.whereEqualTo("age", ageFilt).addSnapshotListener { value, error ->
            userDataArrayList.clear()
            if (error != null){
                Log.i(TAG, "filterAge: " + error)
            }else{
                if(value != null){
                    if (!value.isEmpty){
                        val documents = value.documents
                        for (doc in documents){
                            val str_name = doc.get("name") as String
                            val str_surname = doc.get("surname") as String
                            val str_age = doc.get("age") as String

                            Log.i(TAG, "filterName: STR_NAME: "+str_name+ ": "+str_age)
                            val udata = UserData(str_name, str_surname, str_age)
                            userDataArrayList.add(udata)
                        }
                        userAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun filterName(namefilt: String) {
        collectionReference.whereEqualTo("name", namefilt).addSnapshotListener { value, error ->
            userDataArrayList.clear()
            if(error != null){
                Log.i(TAG, "filterName: " + error)
            }else{
                if (value != null){
                    if (!value.isEmpty){
                        val documents = value.documents
                        for (doc in documents){
                            val str_name = doc.get("name") as String
                            val str_surname = doc.get("surname") as String
                            val str_age = doc.get("age") as String

                            Log.i(TAG, "filterName: STR_NAME: "+str_name)
                            val udata = UserData(str_name, str_surname, str_age)
                            userDataArrayList.add(udata)
                        }
                        userAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun delete(nameSurname: String) {
        firestore.collection(COLLECTION_NAME).document(nameSurname).delete()
            .addOnSuccessListener {
                Log.i(TAG, "delete: Deleted")
            }
            .addOnFailureListener {
                Log.i(TAG, "delete: Failure")
            }
    }

    private fun saveAndUpdate() {
        documentReference.set(datas)
            .addOnSuccessListener {
                Log.i(TAG, "saveAndUpdate: Saved")
            }
            .addOnFailureListener {
                Log.i(TAG, "saveAndUpdate: Failure")
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            auth.signOut()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}