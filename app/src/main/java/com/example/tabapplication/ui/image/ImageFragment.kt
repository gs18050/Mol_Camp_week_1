package com.example.tabapplication.ui.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tabapplication.databinding.FragmentImageBinding
import com.example.tabapplication.R
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class ImageAdapter(private val imagePaths: List<String>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: FragmentImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imagePath: String) {
            Glide.with(binding.imageView.context)
                .load(imagePath)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = FragmentImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imagePaths[position])
    }

    override fun getItemCount() = imagePaths.size
}

fun getGalleryImages(context: Context): List<String> {
    val imageList = mutableListOf<String>()

    val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val projection = arrayOf(
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.DISPLAY_NAME
    )

    val cursor: Cursor? = context.contentResolver.query(
        uri,
        projection,
        null,
        null,
        MediaStore.Images.Media.DATE_ADDED + " DESC"
    )

    cursor?.use {
        val columnIndexData = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        while (it.moveToNext()) {
            val imagePath = it.getString(columnIndexData)
            imageList.add(imagePath)
        }
    }

    return imageList
}

class ImageFragment : Fragment() {

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!

    private fun setupRecyclerView(imagePaths: List<String>) {
        val imageAdapter = ImageAdapter(imagePaths)
        val numColumns: Int = 3
        val recyclerView = binding.imageRecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), numColumns)
        recyclerView.adapter = imageAdapter
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val imageResStrs = getGalleryImages(requireContext())
                setupRecyclerView(imageResStrs)
            }else {
                Toast.makeText(requireContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val imageViewModel =
        //    ViewModelProvider(this).get(ImageViewModel::class.java)

        _binding = FragmentImageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val imageResStrs = getGalleryImages(requireContext())
            setupRecyclerView(imageResStrs)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}