//package com.example.madcamp1stweek
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//
//class NotificationsFragment : Fragment() {
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_notifications, container, false)
//    }
//}
// NotificationsFragment.kt
package com.example.madcamp1stweek

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

class NotificationsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var recyclerViewFavorites: RecyclerView
    private lateinit var favoriteAdapter: FavoriteShopAdapter
    private lateinit var favoriteNames: List<String>
    private lateinit var sharedPreferences: SharedPreferences
    private val likedShopsKey = "liked_shops"
    private var mMap: GoogleMap? = null  // Google Map 변수 추가



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        // SharedPreferences 참조
        sharedPreferences = requireContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val likedShops = sharedPreferences.getStringSet(likedShopsKey, mutableSetOf())

        // RecyclerView 참조
        recyclerViewFavorites = view.findViewById(R.id.recyclerViewFavorites)
        recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // 즐겨찾기된 미용실 이름 목록 생성
        favoriteNames = getFavoriteShopNames(likedShops)

        // 어댑터 설정
        favoriteAdapter = FavoriteShopAdapter(favoriteNames)
        recyclerViewFavorites.adapter = favoriteAdapter
    }

    // SharedPreferences의 likedShops를 기반으로 즐겨찾기된 미용실 이름 목록을 반환
    private fun getFavoriteShopNames(likedShops: Set<String>?): List<String> {
        val favoriteList = mutableListOf<String>()

        // 전체 미용실 목록 생성 (HomeFragment와 동일)
        val allShops = generateDummyShops()

        // likedShops에 포함된 phoneNumber를 가진 미용실의 이름을 추가
        likedShops?.forEach { phoneNumber ->
            val shop = allShops.find { it.phoneNumber == phoneNumber }
            shop?.let {
                favoriteList.add(it.name)
            }
        }

        return favoriteList
    }

    // HomeFragment에서 사용한 동일한 더미 데이터 생성 함수
    private fun generateDummyShops(): List<HairShop> {
        return listOf(
            HairShop("킷키헤어", "0507-1427-0953", R.drawable.kitk_hair_logo),
            HairShop("LSJ뷰티헤어", "0507-1435-2330", R.drawable.lsj_logo),
            HairShop("야도헤어", "0507-1407-8963", R.drawable.yaddo_logo),
            HairShop("해크니헤어", "0507-1443-2322", R.drawable.hakni_logo),
            HairShop("니니티헤어", "0507-1410-5856", R.drawable.ninity_logo),
            HairShop("리소헤어", "0507-1331-2465", R.drawable.liso_logo),
            HairShop("SJ뷰티헤어", "0507-1336-9083", R.drawable.sj_beauty_logo),
            HairShop("프랫헤어", "042-487-4918", R.drawable.pratt_logo),
            HairShop("CM3헤어모드", "0507-1408-4149", R.drawable.cm3_logo),
            HairShop("에이프린헤어", "0507-1360-1054", R.drawable.aprin_logo),
            HairShop("피즈피헤어", "0507-1379-8084", R.drawable.fizfi_logo),
            HairShop("살롱드비바체", "0507-1446-0139", R.drawable.salon_de_logo),
            HairShop("라라몽", "0507-1370-0025", R.drawable.raramong_logo),
            HairShop("고요아틀리에", "0507-1370-8755", R.drawable.goyo_logo),
            HairShop("아논헤어", "0507-1474-9339", R.drawable.anon_logo),
            HairShop("영롱헤어", "042-826-1666", R.drawable.younglong_logo),
            HairShop("지아트원헤어", "0507-1321-7437", R.drawable.ji_art_logo),
            HairShop("디어준헤어", "010-7925-1870", R.drawable.dearjun_logo),
            HairShop("제이블리", "0507-1336-6621", R.drawable.j_vely_logo),
            HairShop("쏠르씨엘헤어", "0507-1378-3322", R.drawable.souls_logo)
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val location = LatLng(36.350, 127.384)  // 대전 중심 위치

        val markerOptions = MarkerOptions()
        markerOptions.position(location)
        markerOptions.title("대전")
        markerOptions.snippet("샵 위치")

        mMap?.addMarker(markerOptions)
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
    }
}
