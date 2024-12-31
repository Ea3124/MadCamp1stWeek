//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.view.View
//import android.view.animation.AlphaAnimation
//import android.view.animation.Animation
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.RecyclerView
//import com.example.madcamp1stweek.FilterOption
//import com.example.madcamp1stweek.FilterOptionsRVAdapter
//
//class FirstActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityFilterBinding
//
//    private val viewModel: FilterViewModel by viewModels()
//    private lateinit var adapterList: List<FilterOptionsRVAdapter>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter)
//
//        binding.apply {
//            viewModel = this@FilterActivity.viewModel
//            lifecycleOwner = this@FilterActivity
//        }
//
//        setFilterOptions()
//    }
//
//    private fun setFilterOptions() {
//        val filterOptionList = FilterOption.getOptionsSortedByFilterType() // 필터 유형마다의 옵션 목록을 리스트에 저장
//        adapterList = List(filterOptionList.size) { FilterOptionsRVAdapter() } // 리사이클러뷰와 연결할 어댑터 리스트 정의
//        binding.apply {
//            val recyclerViewList = listOf<RecyclerView>(
//                filterQuestion1WithWhomRv,
//                filterQuestion2HowManyRv,
//                filterQuestion3HowLongRv,
//                filterQuestion4RouteStyleRv,
//                filterQuestion5MeansOfTransportationRv
//            )
//            // 리사이클러뷰에 어댑터 연결
//            recyclerViewList.forEachIndexed { index, rv ->
//                rv.apply {
//                    adapter = adapterList[index]
//                    layoutManager = FlexboxLayoutManager(context).apply {
//                        flexWrap = FlexWrap.WRAP
//                        flexDirection = FlexDirection.ROW
//                    }
//                }
//            }
//        }
//        // FilterType 순서대로 필터 옵션을 어댑터에 추가
//        adapterList.forEachIndexed { index, adapter ->
//            adapter.addOption(filterOptionList[index])
//        }
//    }
//}

package com.example.madcamp1stweek

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp1stweek.R.layout.activity_first
import com.example.madcamp1stweek.databinding.ActivityFirstBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding

    // 뷰모델 사용 (필요하다면)
    private val viewModel: FilterViewModel by viewModels()

    private lateinit var adapterList: List<FilterOptionsRVAdapter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // activity_first.xml 과 연결
        binding = DataBindingUtil.setContentView(this, activity_first)

        // 1) 환영문구 애니메이션 처리
        startWelcomeAnimation()

        // 2) 질문/옵션 세팅은 환영문구가 사라진 뒤(setFilterOptions())에 진행
        //    -> 애니메이션 리스너 안에서 호출할 예정

        // 3) 혹시나 메인화면으로 가기 버튼 리스너
        binding.btnGoMain.setOnClickListener {
            // MainActivity로 이동
            // startActivity(Intent(this, MainActivity::class.java))
            // finish() // 필요한 경우
        }
    }

    /** 환영 문구 애니메이션: 페이드인 → 3초 유지 → 페이드아웃 후 질문 영역 노출 */
    private fun startWelcomeAnimation() {
        // (1) 페이드인 애니메이션
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 1000
            fillAfter = true
        }
        binding.welcomeText.startAnimation(fadeIn)

        // (2) 3초 후에 페이드아웃
        Handler(Looper.getMainLooper()).postDelayed({
            val fadeOut = AlphaAnimation(1f, 0f).apply {
                duration = 1000
                fillAfter = true
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        // 환영문구 가리기
                        binding.welcomeText.visibility = View.GONE
                        // 필터 옵션 질문 영역 노출
                        binding.filterQuestionsLayout.visibility = View.VISIBLE
                        // 이제 필터 옵션 리사이클러뷰 설정
                        setFilterOptions()
                    }
                    override fun onAnimationRepeat(animation: Animation?) {}
                })
            }
            binding.welcomeText.startAnimation(fadeOut)
        }, 3000)
    }

    /** RecyclerView들 설정 */
    private fun setFilterOptions() {
        // FilterType 순서대로 필터 옵션들 가져오기
        val filterOptionList = FilterOption.getOptionsSortedByFilterType()

        // 리사이클러뷰와 연결할 어댑터 리스트
        adapterList = List(filterOptionList.size) { FilterOptionsRVAdapter() }

        // XML에 있는 4개의 RecyclerView
        val recyclerViewList = listOf<RecyclerView>(
            binding.filterQuestion0,
            binding.filterQuestion1,
            binding.filterQuestion2,
            binding.filterQuestion3
        )

        // 리사이클러뷰에 어댑터 연결 + Flexbox 설정
        recyclerViewList.forEachIndexed { index, rv ->
            rv.apply {
                adapter = adapterList[index]
                layoutManager = FlexboxLayoutManager(context).apply {
                    flexWrap = FlexWrap.WRAP
                    flexDirection = FlexDirection.ROW
                }
            }
        }

        // FilterType 순서대로 필터 옵션을 각 어댑터에 전달
        adapterList.forEachIndexed { index, adapter ->
            adapter.addOption(filterOptionList[index])
        }
    }

    /**
     *  (선택 예시) 모든 필터가 선택되었다고 판단 시,
     *  HairShop 리스트 세팅 + resultLayout VISIBLE
     */
    private fun showHairShopList(selectedFilters: List<FilterOption>) {
        // 1) HairShop 리스트를 필터 조건에 맞춰 가져온다고 가정
        val filteredHairShops = getFilteredHairShops(selectedFilters)

        // 2) resultLayout 보이기
        binding.resultLayout.visibility = View.VISIBLE
        // HairShop을 보여주는 RecyclerView도 보이게
        binding.hairShopListRv.visibility = View.VISIBLE
        binding.btnGoMain.visibility = View.VISIBLE

        // 3) HairShop 리스트 RecyclerView에 어댑터 연결 (별도의 어댑터 필요)
        //    여기서는 간단히 예시만
        // binding.hairShopListRv.adapter = HairShopAdapter().apply {
        //     submitList(filteredHairShops)
        // }

    }

    /** 예시. 실제로는 DB나 서버에서 필터 조건에 맞는 리스트를 가져올 수 있음 */
    private fun getFilteredHairShops(selectedFilters: List<FilterOption>): List<HairShop> {
        // 임시 예시(전체 HairShop 리스트)
        val allShops = listOf(
            HairShop("킷키헤어 대전봉명점", "0507-1427-0953", R.drawable.kitk_hair_logo, 36.358, 127.353),
            HairShop("LSJ뷰티헤어 유성본점", "0507-1435-2330", R.drawable.lsj_logo, 36.362, 127.350),
            HairShop("야도헤어 봉명점", "0507-1407-8963", R.drawable.yaddo_logo, 36.353, 127.377),
            HairShop("해크니헤어 대전유성본점", "0507-1443-2322", R.drawable.hakni_logo, 36.359, 127.351),
            HairShop("니니티헤어 궁동점", "0507-1410-5856", R.drawable.ninity_logo, 36.363, 127.353),
            HairShop("리소헤어 충남대점", "0507-1331-2465", R.drawable.liso_logo, 36.362, 127.350),
            HairShop("SJ뷰티헤어 봉명점", "0507-1336-9083", R.drawable.sj_beauty_logo, 36.359, 127.346),
            HairShop("프랫헤어", "042-487-4918", R.drawable.pratt_logo, 36.359, 127.346),
            HairShop("CM3헤어모드 궁동 점", "0507-1408-4149", R.drawable.cm3_logo, 36.359, 127.346),
            HairShop("에이프린 헤어 봉명점", "0507-1360-1054", R.drawable.aprin_logo, 36.359, 127.346),
            HairShop("피즈피헤어 봉명점", "0507-1379-8084", R.drawable.fizfi_logo, 36.359, 127.346),
            HairShop("살롱드비바체 유성봉명점", "0507-1446-0139", R.drawable.salon_de_logo, 36.359, 127.346),
            HairShop("라라몽", "0507-1370-0025", R.drawable.raramong_logo, 36.359, 127.346),
            HairShop("고요아틀리에 대전궁동점", "0507-1370-8755", R.drawable.goyo_logo, 36.359, 127.346),
            HairShop("아논헤어 대전봉명점", "0507-1474-9339", R.drawable.anon_logo, 36.359, 127.346),
            HairShop("영롱헤어 충대점", "042-826-1666", R.drawable.younglong_logo, 36.359, 127.346),
            HairShop("지아트원헤어 대전유성본점", "0507-1321-7437", R.drawable.ji_art_logo, 36.359, 127.346),
            HairShop("디어준헤어", "010-7925-1870", R.drawable.dearjun_logo, 36.359, 127.346),
            HairShop("제이블리붙임머리", "0507-1336-6621", R.drawable.j_vely_logo, 36.359, 127.346),
            HairShop("쏠르씨엘헤어 유성점", "0507-1378-3322", R.drawable.souls_logo, 36.359, 127.346)
        )
        // selectedFilters에 해당하는 미용실만 filtering 로직 구현
        // ...
        return allShops // 예시: 일단 전체 리턴
    }
}
