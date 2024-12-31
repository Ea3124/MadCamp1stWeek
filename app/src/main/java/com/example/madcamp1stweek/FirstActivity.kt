package com.example.madcamp1stweek

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp1stweek.databinding.ActivityFirstBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding

    // 질문 별로 사용할 RecyclerView의 Adapter들
    private lateinit var adapterList: List<FilterOptionsRVAdapter>

    // HairShop 전체 리스트 (예시)
    private lateinit var allHairShops: MutableList<HairShop>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DataBindingUtil을 통해 binding 초기화
        binding = DataBindingUtil.setContentView(this, R.layout.activity_first) as ActivityFirstBinding

        allHairShops = mutableListOf(
            HairShop("킷키헤어 대전봉명점", "0507-1427-0953", R.drawable.kitk_hair_logo, 36.358, 127.353,
                providedOptions = setOf(
                    FilterOption.MAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.CLEAN_ROOM  // 리뷰가 많음
                )),
            HairShop("LSJ뷰티헤어 유성본점", "0507-1435-2330", R.drawable.lsj_logo, 36.362, 127.350,
                providedOptions = setOf(
                    FilterOption.WOMAN,         // 남성 고객 가능
                    FilterOption.LUXURIOUS,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.MANY_REVIEW  // 리뷰가 많음
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
            HairShop("니니티헤어 궁동점", "0507-1410-5856", R.drawable.ninity_logo, 36.363, 127.353,
                providedOptions = setOf(
                    FilterOption.MAN,         // 남성 고객 가능
                    FilterOption.LUXURIOUS,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.MANY_REVIEW  // 리뷰가 많음
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
            HairShop("CM3헤어모드 궁동 점", "0507-1408-4149", R.drawable.cm3_logo, 36.359, 127.346,
                providedOptions = setOf(
                    FilterOption.MAN,         // 남성 고객 가능
                    FilterOption.TRENDY,      // 트렌디한 스타일
                    FilterOption.GGOMGGOM,    // 꼼꼼한 시술
                    FilterOption.MANY_REVIEW  // 리뷰가 많음
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

        // 환영문구 애니메이션 시작
        startWelcomeAnimation()

        // “다음으로” 버튼 클릭 리스너
        binding.btnShowResult.setOnClickListener {
            val selectedSet = collectAllSelectedOptions()
            showHairShopList(selectedSet)
        }

        // “메인화면으로” 버튼 클릭 리스너
        binding.btnGoMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun startWelcomeAnimation() {
        // (1) 페이드인
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 1000 // 1초
            fillAfter = true
        }
        binding.welcomeText.startAnimation(fadeIn)

        // (2) 3초 후 페이드아웃
        Handler(Looper.getMainLooper()).postDelayed({
            val fadeOut = AlphaAnimation(1f, 0f).apply {
                duration = 1000 // 1초
                fillAfter = true
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        // 환영문구 안보이게
                        binding.welcomeText.visibility = View.GONE
                        // 필터 질문 레이아웃 VISIBLE
                        binding.filterQuestionsLayout.visibility = View.VISIBLE
                        // 필터 RecyclerView들 셋업
                        binding.btnShowResult.visibility = View.VISIBLE // 버튼 표시
                        setFilterOptions()
                    }
                    override fun onAnimationRepeat(animation: Animation?) {}
                })
            }
            binding.welcomeText.startAnimation(fadeOut)
        }, 2000) // 2초 동안 유지
    }

    /** 질문/옵션 리사이클러뷰 설정 */
    private fun setFilterOptions() {
        // 1) GENDER, STYLE, SERVICE, ETC 순서대로 필터 옵션 리스트화
        val filterOptionList = FilterOption.getOptionsSortedByFilterType()

        // 2) 각 질문마다 FilterOptionsRVAdapter 할당
        adapterList = List(filterOptionList.size) { FilterOptionsRVAdapter() }

        // 3) activity_first.xml에 정의된 RecyclerView 리스트
        val recyclerViewList = listOf(
            binding.filterQuestion0,
            binding.filterQuestion1,
            binding.filterQuestion2,
            binding.filterQuestion3
        )

        // 4) RecyclerView에 Adapter 및 LayoutManager 연결
        recyclerViewList.forEachIndexed { index, rv ->
            rv.layoutManager = FlexboxLayoutManager(this).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
            }
            rv.adapter = adapterList[index]
        }

        // 5) Adapter에 데이터 추가
        adapterList.forEachIndexed { index, adapter ->
            adapter.addOption(filterOptionList[index])
        }
    }


    /** 모든 질문(RecyclerView)에서 사용자가 선택한 옵션들을 하나로 모아서 Set으로 반환 */
    private fun collectAllSelectedOptions(): Set<FilterOption> {
        val selectedSet = mutableSetOf<FilterOption>()
        adapterList.forEach { adapter ->
            selectedSet.addAll(adapter.getSelectedOptions())
        }
        return selectedSet
    }

    /** HairShop 리스트 필터링 후 화면에 보여주기 */
    private fun showHairShopList(selectedFilters: Set<FilterOption>) {
        // 1) 필터 로직
        val filteredHairShops = allHairShops.filter { shop ->
            // 선택된 필터 옵션이 shop 제공 옵션에 모두 포함되는지 확인
            selectedFilters.all { it in shop.providedOptions }
        }

        // 필터 결과가 비어 있더라도 레이아웃을 보여줘야 함
        if (filteredHairShops.isEmpty()) {
            Toast.makeText(this, "필터에 맞는 결과가 없습니다.", Toast.LENGTH_SHORT).show()
        }

        // 2) 하단 resultLayout 영역 보이기
        binding.resultLayout.visibility = View.VISIBLE
        binding.hairShopListRv.visibility = View.VISIBLE
        binding.btnGoMain.visibility = View.VISIBLE

        binding.hairShopListRv.layoutManager = LinearLayoutManager(this) // LayoutManager 설정
        // 3) hairShopListRv에 어댑터 연결
        val hairShopAdapter = HairShopAdapter(filteredHairShops.toMutableList(), this)
        binding.hairShopListRv.adapter = hairShopAdapter

        // 어댑터에 데이터 업데이트
        hairShopAdapter.updateShops(filteredHairShops.toMutableList())
    }

}
