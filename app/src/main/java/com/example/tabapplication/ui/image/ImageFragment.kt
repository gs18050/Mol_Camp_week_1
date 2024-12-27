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

        val imageView: ImageView = binding.imageView
        imageViewModel.setImage(R.drawable.testimg)
        imageViewModel.imageResId.observe(viewLifecycleOwner) { resId ->
            imageView.setImageResource(resId)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}