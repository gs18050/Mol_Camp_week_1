package com.example.tabapplication.ui.contact

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tabapplication.databinding.FragmentContactBinding
import com.example.tabapplication.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.nio.charset.Charset

data class ContactInfo(
    val Name: String,
    val PhoneNumber: String,
    val Address: String)

class ContactAdapter(private val dataset: List<ContactInfo>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.name_text)
        val phoneText: TextView = view.findViewById(R.id.phone_text)
        val addressText: TextView = view.findViewById(R.id.info_text)
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
        holder.addressText.text = contact.Address
    }

    override fun getItemCount() = dataset.size
}

fun readJsonFromAssets(context: Context, fileName: String): String? {
    return try {
        val inputStream = context.assets.open(fileName)
        val json = inputStream.bufferedReader(Charset.defaultCharset()).use { it.readText() }
        json
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
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

        val json = readJsonFromAssets(requireContext(), "contact_data.json")
        val gson=Gson()
        val dataListType = object : TypeToken<List<ContactInfo>>() {}.type
        val dataset: List<ContactInfo> = gson.fromJson(json, dataListType)
        /*val dataset = arrayOf(
            ContactInfo("JunHo", "010-6889-4833", "Me"),
            ContactInfo("Alice", "010-1234-5678", "Friend"),
            ContactInfo("Bob", "010-8765-4321", "Colleague")
        )*/
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