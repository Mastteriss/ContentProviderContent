package com.example.contentprovider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactsAdapter(private val contacts: List<Contact>, private val onCallClick: (String) -> Unit, private val onMessageClick: (String) -> Unit) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.contact_name)
        val callImageView: ImageView = view.findViewById(R.id.call_image)
        val messageImageView: ImageView = view.findViewById(R.id.message_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.nameTextView.text = contact.name
        holder.callImageView.setOnClickListener { onCallClick(contact.phoneNumber) }
        holder.messageImageView.setOnClickListener { onMessageClick(contact.phoneNumber) }
    }

    override fun getItemCount() = contacts.size
}