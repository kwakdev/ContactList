package com.ejck.projectevankwak

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter for managing contact list RecyclerView
class ContactListAdapter(
    private val context: Context,
    private var contacts: List<Contact>,
    private val listener: ContactItemClickListener
) : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    // Interface for handling click events on contact items
    interface ContactItemClickListener {
        fun onCallClick(contact: Contact)
        fun onEmailClick(contact: Contact)
    }

    // ViewHolder for individual contact items
    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contactName: TextView = itemView.findViewById(R.id.textViewContactName)
        val contactInfo: TextView = itemView.findViewById(R.id.textViewContactInfo)
        val btnCall: ImageButton = itemView.findViewById(R.id.btnCall)
        val btnEmail: ImageButton = itemView.findViewById(R.id.btnEmail)

        // Bind data to the ViewHolder
        fun bind(contact: Contact) {
            contactName.text = contact.name
            contactInfo.text = contact.info

            // Show appropriate button based on contact information type
            if (contact.info.contains("@")) {
                btnEmail.visibility = View.VISIBLE
                btnCall.visibility = View.GONE
                btnEmail.setOnClickListener {
                    listener.onEmailClick(contact)
                }
            } else {
                btnCall.visibility = View.VISIBLE
                btnEmail.visibility = View.GONE
                btnCall.setOnClickListener {
                    listener.onCallClick(contact)
                }
            }
        }
    }

    // Create new ViewHolders (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        // Inflate the item layout and create ViewHolder
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(itemView)
    }

    // Replace the contents of a ViewHolder (invoked by the layout manager)
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return contacts.size
    }

    // Update the dataset and refresh the RecyclerView
    fun updateContacts(newContacts: List<Contact>) {
        contacts = newContacts
        notifyDataSetChanged()
    }
}
