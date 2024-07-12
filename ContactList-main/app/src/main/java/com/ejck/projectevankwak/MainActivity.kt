package com.ejck.projectevankwak

import android.content.Context
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Main activity that manages fragments and contacts
class MainActivity : AppCompatActivity(), AddContactListener {

    private lateinit var titleTextView: TextView
    private var contacts = mutableListOf<Contact>()
    private lateinit var contactListAdapter: ContactListAdapter

    // Called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        titleTextView = findViewById(R.id.titleTextView)

        // Load saved contacts from SharedPreferences
        contacts = loadContacts()

        // Replace initial fragment with ContactListFragment
        replaceFragment(ContactListFragment())

        // Set click listeners for navigation buttons
        findViewById<ImageButton>(R.id.btnAddEmail)?.setOnClickListener {
            titleTextView.text = "Add Email"
            replaceFragment(AddEmailFragment())
        }

        findViewById<ImageButton>(R.id.btnContactList)?.setOnClickListener {
            titleTextView.text = "Contact List"
            replaceFragment(ContactListFragment())
        }

        findViewById<ImageButton>(R.id.btnAddPhone)?.setOnClickListener {
            titleTextView.text = "Add Phone"
            replaceFragment(AddPhoneFragment())
        }
    }

    // Method to replace fragments in the container
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    // Listener method called when a contact is added
    override fun onAddContact(contact: Contact) {
        contacts.add(contact) // Add the contact to the list
        saveContacts(contacts) // Save the updated list to SharedPreferences
        // Update the contact list in the ContactListFragment
        (supportFragmentManager.findFragmentById(R.id.container) as? ContactListFragment)?.updateContacts(contacts)
    }

    // Method to save contacts to SharedPreferences using Gson
    private fun saveContacts(contacts: List<Contact>) {
        val json = Gson().toJson(contacts) // Convert contacts list to JSON string
        val prefs = getSharedPreferences("list", Context.MODE_PRIVATE)
        prefs.edit().putString("contacts", json).apply() // Save JSON string to SharedPreferences
    }

    // Method to load contacts from SharedPreferences using Gson
    private fun loadContacts(): MutableList<Contact> {
        val prefs = getSharedPreferences("list", Context.MODE_PRIVATE)
        val json = prefs.getString("contacts", null) // Retrieve JSON string from SharedPreferences
        return if (json != null) {
            Gson().fromJson(json, object : TypeToken<MutableList<Contact>>() {}.type) // Convert JSON string back to contacts list
        } else {
            mutableListOf() // Return empty list if no contacts found in SharedPreferences
        }
    }

    // Method to provide access to contacts from other components
    fun getContacts(): List<Contact> = contacts
}

// Interface for handling contact addition
interface AddContactListener {
    fun onAddContact(contact: Contact)
}
