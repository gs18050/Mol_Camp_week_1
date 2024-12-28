package com.example.tabapplication.ui.contact

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

class ContactAdapter(private var dataset: List<ContactInfo>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.name_text)
        val phoneText: TextView = view.findViewById(R.id.phone_text)
        val addressText: TextView = view.findViewById(R.id.info_text)
        val button: Button = view.findViewById(R.id.contact_button)
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
        holder.button.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    fun getDataset(): List<ContactInfo> {
        return dataset
    }

    fun updateList(filteredList: List<ContactInfo>) {
        dataset = filteredList
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
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

class ContactFragment : Fragment(), ContactAdapter.OnItemClickListener {

    private var _binding: FragmentContactBinding? = null
    private lateinit var searchEditText: EditText
    private lateinit var dataset: List<ContactInfo>
    private lateinit var adapter: ContactAdapter
    private lateinit var recyclerView: RecyclerView
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
        dataset = gson.fromJson(json, dataListType)
        searchEditText = root.findViewById(R.id.searchEditText)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ContactAdapter(dataset, this)
        recyclerView.adapter=adapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return root
    }

    override fun onItemClick(position: Int) {
        val ds=adapter.getDataset()
        val phone_number=ds[position].PhoneNumber
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone_number"))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun filterList(query: String) {
        val filteredList = dataset.filter { item ->
            item.Name.contains(query, ignoreCase = true)
        }
        adapter.updateList(filteredList)
    }
}