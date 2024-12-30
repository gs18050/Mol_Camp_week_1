package com.example.tabapplication.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.tabapplication.BuildConfig
import com.example.tabapplication.R
import com.example.tabapplication.SharedViewModel
import com.example.tabapplication.databinding.FragmentMapBinding
import com.example.tabapplication.ui.contact.ContactInfo
import com.example.tabapplication.ui.contact.readJsonFromAssets
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView : MapView
    private var kakaoMap : KakaoMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var dataset: List<ContactInfo>
    val sharedViewModel: SharedViewModel by activityViewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
            }else {
                Toast.makeText(requireContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }

    private fun getPermission(permissionList: List<String>) {
        if (permissionList.size==0) {
            getCurrentLocation()
            return
        }
        val nextList: List<String> = permissionList.subList(1,permissionList.size)
        if (ContextCompat.checkSelfPermission(requireContext(), permissionList[0])==PackageManager.PERMISSION_GRANTED) {
            getPermission(nextList)
        } else {
            requestPermissionLauncher.launch(permissionList[0])
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val json = readJsonFromAssets(requireContext(), "contact_data.json")
        val gson= Gson()
        val dataListType = object : TypeToken<List<ContactInfo>>() {}.type
        dataset = gson.fromJson(json, dataListType)

        val permissionList: List<String> = listOf(Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getPermission(permissionList)

        return root
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                sharedViewModel.sharedData.observe(viewLifecycleOwner) { data ->
                    Log.d("Debug", data.toString())
                    if (data==-1) {
                        showMapView(latitude, longitude)
                    }
                    else {
                        showMapView(dataset[data].latitude, dataset[data].longitude)
                        //sharedViewModel.updateData(-1)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "현재 위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showMapView(latitude: Double, longitude: Double) {
        mapView = binding.mapView
        KakaoMapSdk.init(requireContext(), BuildConfig.MAP_KEY)

        mapView.start(object : MapLifeCycleCallback() {

            override fun onMapDestroy() {
                Log.d("KakaoMap", "onMapDestroy")
            }

            override fun onMapError(p0: Exception?) {
                Log.e("KakaoMap", "onMapError")
            }
        }, object : KakaoMapReadyCallback(){
            override fun onMapReady(kakaomap: KakaoMap) {
                kakaoMap = kakaomap
                if (latitude!=0.0 || longitude!=0.0) {
                    val currentLocation = LatLng.from(latitude, longitude)
                    val cameraUpdate: CameraUpdate =
                        CameraUpdateFactory.newCenterPosition(currentLocation,18)
                    kakaoMap!!.moveCamera(cameraUpdate)
                }

                for ((ind,data) in dataset.withIndex()) {
                    val location = LatLng.from(data.latitude, data.longitude)
                    val styles = kakaomap.labelManager?.addLabelStyles(
                        LabelStyles.from(
                            LabelStyle.from(
                                R.drawable.ping_image
                            ).setZoomLevel(18)
                        )
                    )
                    val options = LabelOptions.from(location).setStyles(styles).setTag(ind)
                    val layer = kakaomap.labelManager?.layer
                    layer?.addLabel(options)
                }
                kakaomap.setOnLabelClickListener { map, layer, label ->
                    val tag = label.tag as? Int
                    if (tag != null && tag in dataset.indices) {
                        val selectedData = dataset[tag]
                        showBottomSheet(selectedData)
                    }
                }
            }
        })
    }

    private fun showBottomSheet(data: ContactInfo) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.map_label_info, null)

        view.findViewById<TextView>(R.id.nameTextView).text = data.Name

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}