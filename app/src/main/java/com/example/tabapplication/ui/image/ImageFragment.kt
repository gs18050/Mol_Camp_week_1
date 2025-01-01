package com.example.tabapplication.ui.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tabapplication.databinding.FragmentImageBinding
import com.example.tabapplication.R
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.util.DisplayMetrics
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.example.tabapplication.MainActivity
import com.example.tabapplication.SharedViewModel
import com.example.tabapplication.ui.contact.ContactAdapter

class ImageAdapter(
    private val imageResIds: List<Int>,
    private val onItemClick: (Int, Int) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.itemImageView)

        fun bind(pos: Int, imageResId: Int) {
            Glide.with(imageView.context)
                .load(imageResId) // Load drawable resource ID
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView)

            itemView.setOnClickListener {
                onItemClick(pos, imageResId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(position, imageResIds[position])
    }

    override fun getItemCount() = imageResIds.size
}


/*fun getGalleryImages(context: Context): MutableList<String> {
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
        MediaStore.Images.Media.DISPLAY_NAME + " ASC"
    )

    cursor?.use {
        val columnIndexData = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        while (it.moveToNext()) {
            val imagePath = it.getString(columnIndexData)
            imageList.add(imagePath)
        }
    }

    return imageList
}*/

fun getDrawableImages(): MutableList<Int> {
    return mutableListOf(
        R.drawable.foodimage1,
        R.drawable.foodimage2,
        R.drawable.foodimage3,
        R.drawable.foodimage4,
        R.drawable.foodimage5,
        R.drawable.foodimage6,
        R.drawable.foodimage7,
        R.drawable.foodimage8,
        R.drawable.foodimage9,
        R.drawable.foodimage10,
        R.drawable.foodimage11,
        R.drawable.foodimage12,
        R.drawable.foodimage13,
        R.drawable.foodimage14,
        R.drawable.foodimage15,
        R.drawable.foodimage16,
        R.drawable.foodimage17,
        R.drawable.foodimage18,
        R.drawable.foodimage19,
        R.drawable.foodimage20
    )
}

class ImageFragment : Fragment() {

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!
    val sharedViewModel: SharedViewModel by activityViewModels()

    private fun setupRecyclerView(imagePaths: List<Int>) {
        val imageAdapter = ImageAdapter(imagePaths) { pos, imagePath ->
            //val mainActivity = requireActivity() as MainActivity
            sharedViewModel.updateData(pos)
            sharedViewModel.setTabDir(1)
            sharedViewModel.updateTab(2)
        }
        val numColumns = 2
        val recyclerView = binding.imageRecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), numColumns)
        recyclerView.adapter = imageAdapter
    }

    /*private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val imageResStrs = getDrawableImages()
                setupRecyclerView(imageResStrs)
            }else {
                Toast.makeText(requireContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentImageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedViewModel.pingSelect.observe(viewLifecycleOwner) { ind ->
            if (ind != null) {
                sharedViewModel.isTabChanging.observe(viewLifecycleOwner) { flag ->
                    Log.d("isTabChanging", flag.toString())
                    if (flag) {
                        sharedViewModel.updateData(ind)
                        sharedViewModel.setTabChanging(false)
                        sharedViewModel.setTabDir(0)
                        sharedViewModel.updateTab(2)
                    }
                }
            }
        }

        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val characterImage: ImageView = binding.characterImage
        val scale = 0.1f
        characterImage.scaleType = ImageView.ScaleType.MATRIX
        val matrix = Matrix()
        val drawable = resources.getDrawable(R.drawable.menu_nupjuk, null)
        val intrinsicWidth = drawable.intrinsicWidth
        val intrinsicHeight = drawable.intrinsicHeight
        val imageViewWidth = displayMetrics.widthPixels
        val imageViewHeight = (displayMetrics.heightPixels*0.2).toInt()

        val layoutParams = characterImage.layoutParams
        layoutParams.width = imageViewWidth
        layoutParams.height = imageViewHeight
        characterImage.layoutParams = layoutParams

        val scaleX = scale
        val scaleY = scale
        val dx = (imageViewWidth - intrinsicWidth * scaleX) / 2
        val dy = (imageViewHeight - intrinsicHeight * scaleY) / 2

        matrix.setScale(scaleX, scaleY)
        matrix.postTranslate(dx, dy)
        characterImage.imageMatrix = matrix
        characterImage.setImageDrawable(drawable)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val imageResStrs = getDrawableImages()
            setupRecyclerView(imageResStrs)
        } else {
            //requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            val imageResStrs = getDrawableImages()
            setupRecyclerView(imageResStrs)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
    }
}