package com.example.tabapplication.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tabapplication.BuildConfig
import com.example.tabapplication.databinding.FragmentMapBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView : MapView
    private var kakaoMap : KakaoMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
                showMapView(latitude, longitude)
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
                val currentLocation = LatLng.from(latitude, longitude)
                val cameraUpdate: CameraUpdate = CameraUpdateFactory.newCenterPosition(currentLocation)
                kakaoMap!!.moveCamera(cameraUpdate);
                /*val marker = Marker().apply {
                    position = currentLocation
                    map = kakaoMap
                    isDraggable = false
                    captionText = "현재 위치"
                }*/
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}