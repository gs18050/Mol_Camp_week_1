package com.example.tabapplication.ui.contact

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.tabapplication.databinding.FragmentContactBinding
import com.example.tabapplication.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tabapplication.SharedViewModel
import com.example.tabapplication.ui.image.getGalleryImages
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.nio.charset.Charset
import java.util.Random

data class ContactInfo(
    val Name: String,
    val PhoneNumber: String,
    val Address: String,
    val latitude: Double,
    val longitude: Double,
    var imagePath: String)

class ContactAdapter(private var dataset: List<ContactInfo>,
                     private val listener: OnItemClickListener,
                     private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.name_text)
        val addressText: TextView = view.findViewById(R.id.info_text)
        val image: ImageView = view.findViewById(R.id.item_image)
        val button: ImageButton = view.findViewById(R.id.contact_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = dataset[position]
        holder.nameText.text = contact.Name
        holder.addressText.text = contact.Address

        //val imagePath = imagePaths.getOrNull(position)
        val imagePath = contact.imagePath
        Glide.with(holder.image.context)
            .load(imagePath)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .into(holder.image)

        holder.button.setOnClickListener {
            listener.onItemClick(position)
        }

        holder.image.setOnClickListener {
            onItemClick(position)
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

public fun readJsonFromAssets(context: Context, fileName: String): String? {
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
    val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentContactBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val json = readJsonFromAssets(requireContext(), "contact_data.json")
        val gson=Gson()
        val dataListType = object : TypeToken<List<ContactInfo>>() {}.type

        dataset = gson.fromJson(json, dataListType)
        searchEditText = root.findViewById(R.id.searchEditText)

        searchEditText.clearFocus()

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        var imageResStrs: MutableList<String> = mutableListOf()
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    imageResStrs = getGalleryImages(requireContext())
                }else {
                    Toast.makeText(requireContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            imageResStrs = getGalleryImages(requireContext())
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        for (i in 0 until imageResStrs.size) {
            dataset[i].imagePath = imageResStrs[i]
        }

        adapter = ContactAdapter(dataset, this) { pos, ->
            //val mainActivity = requireActivity() as MainActivity
            sharedViewModel.updateData(pos)
            sharedViewModel.updateTab(2)
        }

        recyclerView.adapter=adapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.clearFocus()

        searchEditText.setOnFocusChangeListener { v, hasFocus ->
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (hasFocus) {
                imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
            }
            else {
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }

        val imageView3 = root.findViewById<Button>(R.id.imageView3)
        imageView3.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_layout, null)

            val imageView = dialogView.findViewById<ImageView>(R.id.popup_image)
            val nameText = dialogView.findViewById<TextView>(R.id.popup_name)
            val addressText = dialogView.findViewById<TextView>(R.id.popup_address)
            val closeButton = dialogView.findViewById<Button>(R.id.popup_close_button)
            val callButton = dialogView.findViewById<Button>(R.id.popup_call_button)

            val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            builder.setView(dialogView)
            val dialog = builder.create()

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val handler = android.os.Handler()
            val interval = 100L
            val duration = 3000L
            val endTime = System.currentTimeMillis() + duration
            lateinit var phoneText: String
            var currentIndex = -1

            val rouletteTask = object : Runnable {
                override fun run() {
                    if (System.currentTimeMillis() < endTime) {
                        val randomIndex = Random().nextInt(dataset.size)
                        currentIndex=randomIndex
                        val randomContact = dataset[randomIndex]

                        Glide.with(requireContext())
                            .load(randomContact.imagePath)
                            .override(900,900)
                            .error(R.drawable.error)
                            .into(imageView)

                        nameText.text = randomContact.Name.take(5)
                        addressText.text = randomContact.Address

                        handler.postDelayed(this, interval)
                    } else {
                        val finalIndex = Random().nextInt(dataset.size)
                        currentIndex=finalIndex
                        val finalContact = dataset[finalIndex]

                        Glide.with(requireContext())
                            .load(finalContact.imagePath)
                            .override(900,900)
                            .error(R.drawable.error)
                            .into(imageView)

                        nameText.text = finalContact.Name.take(5)
                        phoneText = finalContact.PhoneNumber
                        addressText.text = finalContact.Address
                    }
                }
            }

            imageView.setOnClickListener {
                sharedViewModel.updateData(currentIndex)
                sharedViewModel.updateTab(2)
            }

            handler.post(rouletteTask)

            closeButton.setOnClickListener {
                dialog.dismiss()
            }
            callButton.setOnClickListener {
                val phone_number = phoneText
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone_number"))
                startActivity(intent)
                dialog.dismiss()
            }

            dialog.show()
        }

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

    override fun onResume() {
        super.onResume()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun filterList(query: String) {
        val filteredList = dataset.filter { item ->
            item.Name.contains(query, ignoreCase = true)
        }
        adapter.updateList(filteredList)
    }
}