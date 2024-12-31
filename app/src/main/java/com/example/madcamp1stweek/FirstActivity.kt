package com.example.madcamp1stweek

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_first)

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
                        setFilterOptions()
                    }
                    override fun onAnimationRepeat(animation: Animation?) {}
                })
            }
            binding.welcomeText.startAnimation(fadeOut)
        }, 3000) // 3초 동안 유지
    }

    /** 질문/옵션 리사이클러뷰 설정 */
    private fun setFilterOptions() {
        // 1) GENDER, STYLE, SERVICE, ETC 순서대로 필터 옵션 리스트화
        val filterOptionList = FilterOption.getOptionsSortedByFilterType()
        // -> [ [MAN, WOMAN], [TRENDY, LUXURIOUS, ...], [GGOMGGOM, KIND, ...], [CLEAN_ROOM, GOOD_PRICE, ...] ]

        // 2) 각 질문마다 FilterOptionsRVAdapter 할당
        adapterList = List(filterOptionList.size) { FilterOptionsRVAdapter() }

        // 3) activity_first.xml 에 있는 4개의 RecyclerView
        //    (성별, 스타일, 서비스, 기타) 에 순서대로 할당
        val recyclerViewList = listOf<RecyclerView>(
            binding.filterQuestion0,
            binding.filterQuestion1,
            binding.filterQuestion2,
            binding.filterQuestion3
        )

        // 4) 리사이클러뷰마다 Adapter & LayoutManager 세팅
        recyclerViewList.forEachIndexed { index, rv ->
            rv.adapter = adapterList[index]
            rv.layoutManager = FlexboxLayoutManager(this).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
            }
        }

        // 5) 실제 옵션 데이터를 adapter 에 추가
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
            // “사용자 선택 옵션이 전부 shop.providedOptions에 들어있는가?”
            selectedFilters.all { it in shop.providedOptions }
        }

        // 2) 하단 resultLayout 영역 보이기
        binding.resultLayout.visibility = View.VISIBLE
        binding.hairShopListRv.visibility = View.VISIBLE
        binding.btnGoMain.visibility = View.VISIBLE

        // 3) hairShopListRv에 어댑터 연결 (별도 어댑터 필요)
        val hairShopAdapter = HairShopAdapter(filteredHairShops.toMutableList(), this) // 예시용. 직접 구현 필요
        binding.hairShopListRv.adapter = hairShopAdapter
        hairShopAdapter.updateShops(filteredHairShops.toMutableList())
    }
}
