package com.example.madcamp1stweek

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.SearchView

class HomeFragment : Fragment() {

    private lateinit var recyclerViewShops: RecyclerView
    private lateinit var shopAdapter: HairShopAdapter
    private lateinit var shopList: MutableList<HairShop>
    private lateinit var sharedPreferences: SharedPreferences
    private val likedShopsKey = "liked_shops"
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
        view?.setBackgroundColor(Color.TRANSPARENT) // 루트 뷰 배경을 투명하게 설정
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Reference SearchView
        searchView = view.findViewById(R.id.searchView)

        // SharedPreferences 참조
        sharedPreferences = requireContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val likedShops = sharedPreferences.getStringSet(likedShopsKey, mutableSetOf())

        // RecyclerView 참조
        recyclerViewShops = view.findViewById(R.id.recyclerViewShops)

        // RecyclerView 배경 투명 설정
        recyclerViewShops.setBackgroundColor(Color.TRANSPARENT)

        // 데이터 초기화
        shopList = generateDummyShops()

        // SharedPreferences에서 불러온 즐겨찾기 목록을 기반으로 myshop 설정
        for (shop in shopList) {
            if (likedShops?.contains(shop.phoneNumber) == true) {
                shop.myshop = true
            } else {
                shop.myshop = false
            }
        }

        // Adapter 설정
        shopAdapter = HairShopAdapter(shopList, requireContext())

        // LayoutManager 설정 (LinearLayoutManager 사용)
        recyclerViewShops.layoutManager = LinearLayoutManager(requireContext())

        // Adapter 연결
        recyclerViewShops.adapter = shopAdapter
        // Inflate the layout for this fragment

        // Set up SearchView listener
        setupSearchView()
    }

    private fun setupSearchView() {
        // Enable submitting queries
        searchView.isSubmitButtonEnabled = true

        // Set listeners for query text changes
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Optional: Handle query submission
                shopAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text changes in the search view
                shopAdapter.filter.filter(newText)
                return false
            }
        })
    }






//    HairShop("SJ뷰티헤어", "0507-1336-9083", R.drawable.sj_beauty_logo, 36.359,127.346),
//    HairShop("프랫헤어", "042-487-4918", R.drawable.pratt_logo, 36.359,127.346),
//    HairShop("CM3헤어모드", "0507-1408-4149", R.drawable.cm3_logo, 36.359,127.346),
//    HairShop("에이프린헤어", "0507-1360-1054", R.drawable.aprin_logo, 36.359,127.346),
//    HairShop("피즈피헤어", "0507-1379-8084", R.drawable.fizfi_logo, 36.359,127.346),
//    HairShop("살롱드비바체", "0507-1446-0139", R.drawable.salon_de_logo, 36.359,127.346),
//    HairShop("라라몽", "0507-1370-0025", R.drawable.raramong_logo, 36.359,127.346),
//    HairShop("고요아틀리에", "0507-1370-8755", R.drawable.goyo_logo, 36.359,127.346),
//    HairShop("아논헤어", "0507-1474-9339", R.drawable.anon_logo, 36.359,127.346),
//    HairShop("영롱헤어", "042-826-1666", R.drawable.younglong_logo, 36.359,127.346),
//    HairShop("지아트원헤어", "0507-1321-7437", R.drawable.ji_art_logo, 36.359,127.346),
//    HairShop("디어준헤어", "010-7925-1870", R.drawable.dearjun_logo, 36.359,127.346),
//    HairShop("제이블리", "0507-1336-6621", R.drawable.j_vely_logo, 36.359,127.346),
//    HairShop("쏠르씨엘헤어", "0507-1378-3322", R.drawable.souls_logo, 36.359,127.346)
//    )


    // 더미 데이터 생성 함수
    private fun generateDummyShops(): MutableList<HairShop> {
        return mutableListOf(
            HairShop("킷키헤어 대전봉명점", "0507-1427-0953", R.drawable.kitk_hair_logo,36.358,127.353),
            HairShop("LSJ뷰티헤어 유성본점", "0507-1435-2330", R.drawable.lsj_logo,36.362,127.350),
            HairShop("야도헤어 봉명점", "0507-1407-8963", R.drawable.yaddo_logo,36.353,127.377),
            HairShop("해크니헤어 대전유성본점", "0507-1443-2322", R.drawable.hakni_logo,36.359,127.351),
            HairShop("니니티헤어 궁동점", "0507-1410-5856", R.drawable.ninity_logo,36.363,127.353),
            HairShop("리소헤어 충남대점", "0507-1331-2465", R.drawable.liso_logo,36.362,127.350),
            HairShop("SJ뷰티헤어 봉명점", "0507-1336-9083", R.drawable.sj_beauty_logo, 36.359,127.346),
            HairShop("프랫헤어", "042-487-4918", R.drawable.pratt_logo, 36.359,127.346),
            HairShop("CM3헤어모드 궁동 점", "0507-1408-4149", R.drawable.cm3_logo, 36.359,127.346),
            HairShop("에이프린 헤어 봉명점", "0507-1360-1054", R.drawable.aprin_logo, 36.359,127.346),
            HairShop("피즈피헤어 봉명점", "0507-1379-8084", R.drawable.fizfi_logo, 36.359,127.346),
            HairShop("살롱드비바체 유성봉명점", "0507-1446-0139", R.drawable.salon_de_logo, 36.359,127.346),
            HairShop("라라몽", "0507-1370-0025", R.drawable.raramong_logo, 36.359,127.346),
            HairShop("고요아틀리에 대전궁동점", "0507-1370-8755", R.drawable.goyo_logo, 36.359,127.346),
            HairShop("아논헤어 대전봉명점", "0507-1474-9339", R.drawable.anon_logo, 36.359,127.346),
            HairShop("영롱헤어 충대점", "042-826-1666", R.drawable.younglong_logo, 36.359,127.346),
            HairShop("지아트원헤어 대전유성본점", "0507-1321-7437", R.drawable.ji_art_logo, 36.359,127.346),
            HairShop("디어준헤어", "010-7925-1870", R.drawable.dearjun_logo, 36.359,127.346),
            HairShop("제이블리붙임머리", "0507-1336-6621", R.drawable.j_vely_logo, 36.359,127.346),
            HairShop("쏠르씨엘헤어 유성점", "0507-1378-3322", R.drawable.souls_logo, 36.359,127.346)
        )
    }

}
