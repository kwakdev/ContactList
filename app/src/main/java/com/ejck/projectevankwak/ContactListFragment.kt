package com.ejck.projectevankwak

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Fragment for displaying a list of contacts
class ContactListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactListAdapter: ContactListAdapter

    // Called to create and return the view hierarchy associated with the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)

        // Initialize RecyclerView and set layout manager
        recyclerView = view.findViewById(R.id.recyclerViewContacts)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Get contacts from the hosting activity (MainActivity)
        val contacts = (activity as MainActivity).getContacts()

        // Initialize ContactListAdapter with the retrieved contacts and click listeners
        contactListAdapter = ContactListAdapter(requireContext(), contacts, object : ContactListAdapter.ContactItemClickListener {
            override fun onCallClick(contact: Contact) {
                // Handle call button click
                Log.d("ContactListAdapter", "in phone")

                // Check for CALL_PHONE permission
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // Request the permission if not granted
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PHONE)
                } else {
                    // Create intent to make a call
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${contact.info}"))
                    startActivity(intent)
                }
            }

            override fun onEmailClick(contact: Contact) {
                // Handle email button click
                Log.d("ContactListAdapter", "in email")

                // Create intent to send an email
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(contact.info))
                    putExtra(Intent.EXTRA_SUBJECT, "Subject")
                    putExtra(Intent.EXTRA_TEXT, "Body")
                }

                // Verify if there's an app to handle the intent
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                } else {
                    // Display a toast if no email app is found
                    Toast.makeText(requireContext(), "No email app found", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // Set the adapter for the RecyclerView
        recyclerView.adapter = contactListAdapter

        return view // Return the prepared view hierarchy
    }

    // Method to update the contact list in the RecyclerView
    fun updateContacts(newContacts: List<Contact>) {
        contactListAdapter.updateContacts(newContacts)
    }

    companion object {
        private const val REQUEST_CALL_PHONE = 1
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CALL_PHONE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, proceed with the call
                    Toast.makeText(requireContext(), "Permission granted, please try again", Toast.LENGTH_SHORT).show()
                } else {
                    // Permission denied, show a message to the user
                    Toast.makeText(requireContext(), "Permission denied to make calls", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}
