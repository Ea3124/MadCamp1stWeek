package com.example.madcamp1stweek

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp1stweek.databinding.ActivityFirstBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding

    // 질문 별로 사용할 RecyclerView의 Adapter들
    private lateinit var adapterList: List<FilterOptionsRVAdapter>

    // HairShop 전체 리스트
    private lateinit var allHairShops: MutableList<HairShop>

    private lateinit var sharedPreferences: SharedPreferences
    private val likedShopsKey = "liked_shops"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DataBindingUtil을 통해 binding 초기화
        binding = DataBindingUtil.setContentView(this, R.layout.activity_first)

        // 닉네임 받기
        val nickName = intent.getStringExtra("NICK_NAME") ?: "사용자"
        binding.welcomeText.text = "$nickName 님, 반가워요"
        binding.q0text.text = "$nickName 님의 성별은 무엇인가요?"

        // 로그 출력
        Log.d("FirstActivity", "Received NickName: $nickName")

        // (1) 질문 레이아웃을 처음엔 GONE 처리
        binding.filterQuestionsLayout.visibility = View.GONE

        // “결과보기” 버튼도 처음에는 숨김
        binding.btnShowResult.visibility = View.GONE
        binding.btnShowResultC.visibility = View.GONE

        // 샘플로 사용할 allHairShops 초기화
        allHairShops = mutableListOf(
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

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        // 저장된 '좋아요' 상태 불러오기
        val likedShops = sharedPreferences.getStringSet(likedShopsKey, mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        // (2) onCreate()에서 바로 setFilterOptions() 호출해 adapterList 초기화
        setFilterOptions()

        // 환영문구 애니메이션 시작
        startWelcomeAnimation()

        // “결과보기” 버튼 클릭 리스너
        binding.btnShowResult.setOnClickListener {
            showLoadingAndThenShowResult()
        }

        // “메인화면으로” 버튼 클릭 리스너
        binding.btnGoMain.setOnClickListener {
            val intent = Intent(this@FirstActivity, MainActivity::class.java)
            intent.putExtra("NICK_NAME", nickName)  // 필요한 데이터 전달
            startActivity(intent)
            finish()
        }
    }

    fun updateLikedShops(hairShopId: String, isLiked: Boolean) {
        val editor = sharedPreferences.edit()
        val likedShops = sharedPreferences.getStringSet(likedShopsKey, mutableSetOf()) ?: mutableSetOf()

        if (isLiked) {
            likedShops.add(hairShopId)
        } else {
            likedShops.remove(hairShopId)
        }

        editor.putStringSet(likedShopsKey, likedShops)
        editor.apply() // 비동기로 데이터 저장
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

                        // (3) 여기서 필터 질문 레이아웃을 VISIBLE로 변경
                        binding.filterQuestionsLayout.visibility = View.VISIBLE

                        // 마지막 질문까지 한 번도 선택되지 않았을 수도 있으므로,
                        // "결과보기" 버튼은 여기서가 아니라 '순차질문 로직'에서 제어해도 됨.
                    }
                    override fun onAnimationRepeat(animation: Animation?) {}
                })
            }
            binding.welcomeText.startAnimation(fadeOut)
        }, 2000) // 2초 동안 유지
    }

    private fun showViewWithSlideIn(view: View) {
        if (view.visibility == View.VISIBLE) return

        val slideIn = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 1f,
            Animation.RELATIVE_TO_PARENT, 0f
        ).apply {
            duration = 500 // 애니메이션 지속 시간 (밀리초 단위)
            fillAfter = true // 애니메이션 종료 후 상태 유지
        }

        // 애니메이션 리스너 추가
        slideIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // 애니메이션이 끝난 후 스크롤 수행
                binding.filterQuestionsLayout.post {
                    // 새로 표시된 뷰의 위치로 스크롤
                    binding.filterQuestionsLayout.smoothScrollTo(0, view.top)
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        // 뷰를 보이도록 설정하고 애니메이션 시작
        view.visibility = View.VISIBLE
        view.startAnimation(slideIn)
    }



    /** 질문/옵션 리사이클러뷰 설정 */
    private fun setFilterOptions() {
        // 필터 타입별로 그룹화된 옵션 리스트 가져오기
        val filterOptionGroups = FilterOption.getOptionsSortedByFilterType()

        // 각 그룹별로 Adapter 설정 (예시)
        adapterList = filterOptionGroups.mapIndexed { index, groupOptions ->
            val selectionMode = groupOptions.firstOrNull()?.selectionMode ?: SelectionMode.MULTIPLE
            FilterOptionsRVAdapter(selectionMode, index).apply {
                addOption(groupOptions)

                // 필터 선택 콜백 → 다음 질문 or 결과버튼 노출
                onFilterSelectionListener = object : FilterOptionsRVAdapter.OnFilterSelectionListener {
                    override fun onFilterSelected(questionIndex: Int, hasSelection: Boolean) {
                        if (!hasSelection) return

                        // 질문별 다음 UI 오픈
                        when (questionIndex) {
                            0 -> {showViewWithSlideIn(binding.filterQuestion1)
                                showViewWithSlideIn(binding.q1text)}
                            1 -> {showViewWithSlideIn(binding.filterQuestion2)
                                showViewWithSlideIn(binding.q2text)}
                            2 -> {showViewWithSlideIn(binding.filterQuestion3)
                                showViewWithSlideIn(binding.q3text)}
                            3 -> {
                                showViewWithSlideIn(binding.btnShowResult)
                                showViewWithSlideIn(binding.btnShowResultC)
                            }

                        }
                    }
                }
            }
        }

        // 질문별 RecyclerView
        binding.filterQuestion0.apply {
            layoutManager = FlexboxLayoutManager(context).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
            }
            adapter = adapterList[0]
            visibility = View.VISIBLE // 첫 번째 질문은 처음부터 보여주고 싶다면 VISIBLE
        }

        binding.filterQuestion1.apply {
            layoutManager = FlexboxLayoutManager(context).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
            }
            adapter = adapterList[1]
            visibility = View.GONE
        }

        binding.filterQuestion2.apply {
            layoutManager = FlexboxLayoutManager(context).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
            }
            adapter = adapterList[2]
            visibility = View.GONE
        }

        binding.filterQuestion3.apply {
            layoutManager = FlexboxLayoutManager(context).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
            }
            adapter = adapterList[3]
            visibility = View.GONE
        }
    }

    private fun showLoadingAndThenShowResult() {
        // 로딩 레이아웃 표시
        binding.loadingLayout.visibility = View.VISIBLE

        // 2초 후에 로딩 레이아웃 숨기고 결과 표시
        Handler(Looper.getMainLooper()).postDelayed({
            binding.loadingLayout.visibility = View.GONE
            val selectedSet = collectAllSelectedOptions()
            showHairShopList(selectedSet)
        }, 2000)
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

        // 필터 질문 레이아웃 숨기기
        binding.filterQuestionsLayout.visibility = View.GONE
        binding.btnShowResult.visibility = View.GONE

        // (1) 필터 로직
        val filteredHairShops = allHairShops.filter { shop ->
            // 선택된 필터 옵션이 shop 제공 옵션에 모두 포함되는지 확인
            selectedFilters.all { it in shop.providedOptions }
        }

        // 필터 결과가 비어 있더라도 레이아웃을 보여줘야 함
        if (filteredHairShops.isEmpty()) {
            Toast.makeText(this, "필터에 맞는 결과가 없습니다.", Toast.LENGTH_SHORT).show()
            binding.hairShopListRv.visibility = View.GONE
            binding.ResultList.visibility = View.GONE
            showViewWithSlideIn(binding.noResultsText)
        } else {
//            binding.hairShopListRv.visibility = View.VISIBLE
            showViewWithSlideIn(binding.hairShopListRv)
//            binding.ResultList.visibility = View.VISIBLE
            showViewWithSlideIn(binding.ResultList)
            binding.noResultsText.visibility = View.GONE

            binding.hairShopListRv.layoutManager = LinearLayoutManager(this)
            val hairShopAdapter = HairShopAdapter(filteredHairShops.toMutableList(), this)
            binding.hairShopListRv.adapter = hairShopAdapter
        }

        // 하단 resultLayout 영역 보이기
//        binding.resultLayout.visibility = View.VISIBLE
        showViewWithSlideIn(binding.resultLayout)
//        btnShowResultC
//        binding.btnGoMain.visibility = View.VISIBLE
        showViewWithSlideIn(binding.btnGoMain)
        showViewWithSlideIn(binding.btnGoMainC)

        // 어댑터에 데이터 업데이트
        val hairShopAdapter = HairShopAdapter(filteredHairShops.toMutableList(), this)
        binding.hairShopListRv.layoutManager = LinearLayoutManager(this)
        binding.hairShopListRv.adapter = hairShopAdapter
        hairShopAdapter.updateShops(filteredHairShops.toMutableList())
    }
}
