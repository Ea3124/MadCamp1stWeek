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

class HomeFragment : Fragment() {

    private lateinit var recyclerViewShops: RecyclerView
    private lateinit var shopAdapter: HairShopAdapter
    private lateinit var shopList: List<HairShop>
    private lateinit var sharedPreferences: SharedPreferences
    private val likedShopsKey = "liked_shops"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SharedPreferences 참조
        sharedPreferences = requireContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val likedShops = sharedPreferences.getStringSet(likedShopsKey, mutableSetOf())

        // RecyclerView 참조
        recyclerViewShops = view.findViewById(R.id.recyclerViewShops)

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
    }

    // 더미 데이터 생성 함수
    private fun generateDummyShops(): List<HairShop> {
        return listOf(
            HairShop("킷키헤어 대전봉명점", "0507-1427-0953", R.drawable.kitk_hair_logo),
            HairShop("LSJ뷰티헤어 유성본점", "0507-1435-2330", R.drawable.lsj_logo),
            HairShop("야도헤어 봉명점", "0507-1407-8963", R.drawable.yaddo_logo),
            HairShop("해크니헤어 대전유성본점", "0507-1443-2322", R.drawable.hakni_logo),
            HairShop("니니티헤어 궁동점", "0507-1410-5856", R.drawable.ninity_logo),
            HairShop("리소헤어 충남대점", "0507-1331-2465", R.drawable.liso_logo),
            HairShop("SJ뷰티헤어 봉명점", "0507-1336-9083", R.drawable.sj_beauty_logo),
            HairShop("프랫헤어", "042-487-4918", R.drawable.pratt_logo),
            HairShop("CM3헤어모드 궁동 점", "0507-1408-4149", R.drawable.cm3_logo),
            HairShop("에이프린 헤어 봉명점", "0507-1360-1054", R.drawable.aprin_logo),
            HairShop("피즈피헤어 봉명점", "0507-1379-8084", R.drawable.fizfi_logo),
            HairShop("살롱드비바체 유성봉명점", "0507-1446-0139", R.drawable.salon_de_logo),
            HairShop("라라몽", "0507-1370-0025", R.drawable.raramong_logo),
            HairShop("고요아틀리에 대전궁동점", "0507-1370-8755", R.drawable.goyo_logo),
            HairShop("아논헤어 대전봉명점", "0507-1474-9339", R.drawable.anon_logo),
            HairShop("영롱헤어 충대점", "042-826-1666", R.drawable.younglong_logo),
            HairShop("지아트원헤어 대전유성본점", "0507-1321-7437", R.drawable.ji_art_logo),
            HairShop("디어준헤어", "010-7925-1870", R.drawable.dearjun_logo),
            HairShop("제이블리붙임머리", "0507-1336-6621", R.drawable.j_vely_logo),
            HairShop("쏠르씨엘헤어 유성점", "0507-1378-3322", R.drawable.souls_logo)
        )
    }

}
