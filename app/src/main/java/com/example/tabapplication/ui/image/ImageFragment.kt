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
import com.bumptech.glide.Glide

class ImageAdapter(private val imagePaths: List<String>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: FragmentImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imagePath: String) {
            Glide.with(binding.imageView.context)
                .load(imagePath) // 로컬 이미지 파일 경로
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
        MediaStore.Images.Media.DATA, // Path to the image file
        MediaStore.Images.Media.DISPLAY_NAME // Optional: File name
    )

    val cursor: Cursor? = context.contentResolver.query(
        uri,
        projection,
        null, // Selection (WHERE clause)
        null, // Selection arguments
        MediaStore.Images.Media.DATE_ADDED + " DESC" // Sort order
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

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val imageViewModel =
            ViewModelProvider(this).get(ImageViewModel::class.java)

        _binding = FragmentImageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val imageResStrs = getGalleryImages(requireContext())
        /*listOf(
            R.drawable.testimg,
            R.drawable.testimg,
            R.drawable.testimg,
            R.drawable.testimg,
            R.drawable.testimg
        )*/

        val imageAdapter = ImageAdapter(imageResStrs)
        val num_colum: Int=3
        val recyclerView = binding.imageRecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(),num_colum)
        recyclerView.adapter = imageAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}