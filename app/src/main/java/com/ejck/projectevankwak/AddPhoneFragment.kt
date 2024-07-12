// AddPhoneFragment.kt
package com.ejck.projectevankwak

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

// Fragment for adding a phone contact
class AddPhoneFragment : Fragment() {

    private var addContactListener: AddContactListener? = null

    // Called when the fragment attaches to the context (activity)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the context implements AddContactListener interface
        if (context is AddContactListener) {
            addContactListener = context // Set the listener
        }
    }

    // Called to create and return the view hierarchy associated with the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_phone, container, false)

        // Initialize views from the inflated layout
        val editTextContactName = view.findViewById<EditText>(R.id.editTextContactName)
        val editTextPhoneNumber = view.findViewById<EditText>(R.id.editTextPhoneNumber)
        val btnSubmitPhone = view.findViewById<Button>(R.id.btnSubmitPhone)

        // Set click listener for the submit button
        btnSubmitPhone.setOnClickListener {
            // Retrieve text from EditText fields
            val contactName = editTextContactName.text.toString()
            val phoneNumber = editTextPhoneNumber.text.toString()

            // Create a Contact object with the entered data
            val contact = Contact(contactName, phoneNumber)

            // Notify the listener (hosting activity) about the new contact
            addContactListener?.onAddContact(contact)

            // Clear EditText fields after submission
            editTextContactName.text.clear()
            editTextPhoneNumber.text.clear()
        }

        return view // Return the prepared view hierarchy
    }

    // Called when the fragment is no longer attached to its activity
    override fun onDetach() {
        super.onDetach()
        addContactListener = null // Clear the listener reference to avoid leaks
    }
}
