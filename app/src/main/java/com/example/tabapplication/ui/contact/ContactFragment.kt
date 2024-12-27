package com.example.tabapplication.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.tabapplication.databinding.FragmentContactBinding
import com.example.tabapplication.R
import androidx.recyclerview.widget.LinearLayoutManager

class ContactInfo(var Name: String, var PhoneNumber: String, var Info: String) {}

class ContactAdapter(private val dataset: Array<ContactInfo>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.name_text)
        val phoneText: TextView = view.findViewById(R.id.phone_text)
        val infoText: TextView = view.findViewById(R.id.info_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = dataset[position]
        holder.nameText.text = contact.Name
        holder.phoneText.text = contact.PhoneNumber
        holder.infoText.text = contact.Info
    }

    override fun getItemCount() = dataset.size
}

class ContactFragment : Fragment() {

    private var _binding: FragmentContactBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val contactViewModel =
        //    ViewModelProvider(this).get(ContactViewModel::class.java)

        _binding = FragmentContactBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dataset = arrayOf(
            ContactInfo("JunHo", "010-6889-4833", "Me"),
            ContactInfo("Alice", "010-1234-5678", "Friend"),
            ContactInfo("Bob", "010-8765-4321", "Colleague")
        )
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ContactAdapter(dataset)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}