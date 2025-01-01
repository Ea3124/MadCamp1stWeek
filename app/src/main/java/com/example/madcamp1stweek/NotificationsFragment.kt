package com.example.madcamp1stweek

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
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
import com.google.android.gms.maps.model.LatLngBounds


class NotificationsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var recyclerViewFavorites: RecyclerView
    private lateinit var favoriteAdapter: FavoriteShopAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private val likedShopsKey = "liked_shops"
    private var mMap: GoogleMap? = null  // Google Map 변수 추가
    // 즐겨찾기된 미용실 객체 목록 생성
    private lateinit var favoriteShops: List<HairShop>

    //nickname
    private lateinit var textViewWelcome: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewWelcome = view.findViewById(R.id.textViewWelcome)

        // InfoActivity에서 전달한 NickName 받기
        val nickName = requireActivity().intent.getStringExtra("NICK_NAME") ?: "사용자"
        textViewWelcome.text = "$nickName 님, 반가워요"

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        // SharedPreferences 참조
        sharedPreferences = requireContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val likedShops = sharedPreferences.getStringSet(likedShopsKey, mutableSetOf())

        // RecyclerView 참조
        recyclerViewFavorites = view.findViewById(R.id.recyclerViewFavorites)
        recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // 전체 미용실 목록 생성
        val allShops = generateDummyShops()

        // favoriteShops 초기화: likedShops에 포함된 phoneNumber를 가진 HairShop 객체들 필터링
        favoriteShops = allShops.filter { it.phoneNumber in (likedShops ?: emptySet()) }

        // 어댑터 설정
        favoriteAdapter = FavoriteShopAdapter(favoriteShops, object : FavoriteShopAdapter.OnItemClickListener {
            override fun onItemClick(hairShop: HairShop) {
                // 지도에서 해당 미용실 위치로 이동
                moveMapToLocation(hairShop.latitude, hairShop.longitude, hairShop.name)
            }
        })
        recyclerViewFavorites.adapter = favoriteAdapter

        // 지도에 모든 즐겨찾기된 미용실 마커 추가
        addMarkersToMap()
    }

    // HomeFragment에서 사용한 동일한 더미 데이터 생성 함수
    private fun generateDummyShops(): List<HairShop> {
        return listOf(
            HairShop("킷키헤어 대전봉명점", "0507-1427-0953", R.drawable.kitk_hair_logo, 36.358, 127.353,
                providedOptions = setOf(
                    FilterOption.MAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.CLEAN_ROOM  // 리뷰가 많음
                )),
            HairShop("니니티헤어 궁동점", "0507-1410-5856", R.drawable.ninity_logo, 36.363, 127.353,
                providedOptions = setOf(
                    FilterOption.MAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.CLEAN_ROOM  // 리뷰가 많음
                )),
            HairShop("LSJ뷰티헤어 유성본점", "0507-1435-2330", R.drawable.lsj_logo, 36.362, 127.350,
                providedOptions = setOf(
                    FilterOption.MAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.CLEAN_ROOM  // 리뷰가 많음
                )),
            HairShop("야도헤어 봉명점", "0507-1407-8963", R.drawable.yaddo_logo, 36.353, 127.377,
                providedOptions = setOf(
                    FilterOption.MAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.MANY_REVIEW  // 리뷰가 많음
                )),
            HairShop("해크니헤어 대전유성본점", "0507-1443-2322", R.drawable.hakni_logo, 36.359, 127.351,
                providedOptions = setOf(
                    FilterOption.WOMAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.NO_TALK,    // 꼼꼼한 시술
                    FilterOption.CLEAN_ROOM  // 리뷰가 많음
                )),
            HairShop("리소헤어 충남대점", "0507-1331-2465", R.drawable.liso_logo, 36.362, 127.350,
                providedOptions = setOf(
                    FilterOption.WOMAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.MANY_REVIEW  // 리뷰가 많음
                )),
            HairShop("SJ뷰티헤어 봉명점", "0507-1336-9083", R.drawable.sj_beauty_logo, 36.359, 127.346,
                providedOptions = setOf(
                    FilterOption.MAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.NO_TALK,    // 꼼꼼한 시술
                    FilterOption.MANY_REVIEW  // 리뷰가 많음
                )),
            HairShop("프랫헤어", "042-487-4918", R.drawable.pratt_logo, 36.359, 127.346,
                providedOptions = setOf(
                    FilterOption.WOMAN,         // 남성 고객 가능
                    FilterOption.LUXURIOUS,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.MANY_REVIEW  // 리뷰가 많음
                )),
            HairShop("CM3헤어모드 궁동 점", "0507-1408-4149", R.drawable.cm3_logo, 36.362,127.350,
                providedOptions = setOf(
                    FilterOption.MAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.CLEAN_ROOM  // 리뷰가 많음
                )),
            HairShop("에이프린 헤어 봉명점", "0507-1360-1054", R.drawable.aprin_logo, 36.359, 127.346,
                providedOptions = setOf(
                    FilterOption.WOMAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.NO_TALK,    // 꼼꼼한 시술
                    FilterOption.CLEAN_ROOM  // 리뷰가 많음
                )),
            HairShop("피즈피헤어 봉명점", "0507-1379-8084", R.drawable.fizfi_logo, 36.359, 127.346,
                providedOptions = setOf(
                    FilterOption.MAN,         // 남성 고객 가능
                    FilterOption.LUXURIOUS,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.MANY_REVIEW  // 리뷰가 많음
                )),
            HairShop("살롱드비바체 유성봉명점", "0507-1446-0139", R.drawable.salon_de_logo, 36.359, 127.346,
                providedOptions = setOf(
                    FilterOption.WOMAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.MANY_REVIEW  // 리뷰가 많음
                )),
            HairShop("라라몽", "0507-1370-0025", R.drawable.raramong_logo, 36.359, 127.346,
                providedOptions = setOf(
                    FilterOption.MAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.MANY_REVIEW  // 리뷰가 많음
                )),
            HairShop("고요아틀리에 대전궁동점", "0507-1370-8755", R.drawable.goyo_logo, 36.359, 127.346,
                providedOptions = setOf(
                    FilterOption.WOMAN,         // 남성 고객 가능
                    FilterOption.LUXURIOUS,      // 트렌디한 스타일
                    FilterOption.NO_TALK,    // 꼼꼼한 시술
                    FilterOption.MANY_REVIEW  // 리뷰가 많음
                )),
            HairShop("아논헤어 대전봉명점", "0507-1474-9339", R.drawable.anon_logo, 36.359, 127.346,
                providedOptions = setOf(
                    FilterOption.MAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.CLEAN_ROOM  // 리뷰가 많음
                )),
            HairShop("영롱헤어 충대점", "042-826-1666", R.drawable.younglong_logo, 36.359, 127.346,
                providedOptions = setOf(
                    FilterOption.WOMAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.MANY_REVIEW  // 리뷰가 많음
                )),
            HairShop("지아트원헤어 대전유성본점", "0507-1321-7437", R.drawable.ji_art_logo, 36.359, 127.346,
                providedOptions = setOf(
                    FilterOption.MAN,         // 남성 고객 가능
                    FilterOption.LUXURIOUS,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.MANY_REVIEW  // 리뷰가 많음
                )),
            HairShop("디어준헤어", "010-7925-1870", R.drawable.dearjun_logo, 36.359, 127.346,
                providedOptions = setOf(
                    FilterOption.WOMAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.NO_TALK,    // 꼼꼼한 시술
                    FilterOption.MANY_REVIEW  // 리뷰가 많음
                )),
            HairShop("제이블리붙임머리", "0507-1336-6621", R.drawable.j_vely_logo, 36.359, 127.346,
                providedOptions = setOf(
                    FilterOption.MAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.MANY_REVIEW  // 리뷰가 많음
                )),
            HairShop("쏠르씨엘헤어 유성점", "0507-1378-3322", R.drawable.souls_logo, 36.359, 127.346,
                providedOptions = setOf(
                    FilterOption.WOMAN,         // 남성 고객 가능
                    FilterOption.LUXURIOUS,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.CLEAN_ROOM  // 리뷰가 많음
                ))
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // 초기 카메라 위치 설정 (대전 중심지)
        val initialLocation = LatLng(36.350, 127.384) // 대전 중심지
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 12f)) // 12f는 줌 레벨


        // 모든 즐겨찾기된 미용실에 마커 추가
        addMarkersToMap()
    }

    // 모든 즐겨찾기된 미용실에 마커 추가하는 메서드
    private fun addMarkersToMap() {
        mMap?.clear() // 기존 마커 삭제

        if (::favoriteShops.isInitialized && favoriteShops.isNotEmpty()) {
            val builder = LatLngBounds.Builder()

            for (shop in favoriteShops) {
                val location = LatLng(shop.latitude, shop.longitude)
                val markerOptions = MarkerOptions()
                    .position(location)
                    .title(shop.name)
                    .snippet(shop.phoneNumber)

                mMap?.addMarker(markerOptions)
                builder.include(location)
            }

            // 모든 마커가 포함되도록 카메라 범위 설정
            val bounds = builder.build()
            val padding = 100 // 화면 가장자리에서의 여백 (픽셀 단위)

            mMap?.setOnMapLoadedCallback {
                mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
            }
        }
    }

    // 특정 위치로 카메라 이동하는 메서드
    private fun moveMapToLocation(latitude: Double, longitude: Double, title: String) {
        val location = LatLng(latitude, longitude)
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

        // 마커 클릭 시 정보 표시
        mMap?.addMarker(MarkerOptions().position(location).title(title))?.showInfoWindow()
    }
}
