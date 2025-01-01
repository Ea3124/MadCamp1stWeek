package com.example.madcamp1stweek

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import android.widget.SearchView
import android.widget.Toast

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SearchView 참조
        searchView = view.findViewById(R.id.searchView)

        // SharedPreferences 참조
        sharedPreferences = requireContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val likedShops = sharedPreferences.getStringSet(likedShopsKey, mutableSetOf())

        // RecyclerView 참조
        recyclerViewShops = view.findViewById(R.id.recyclerViewShops)

        // 데이터 초기화
        shopList = generateDummyShops()

        // SharedPreferences에서 불러온 즐겨찾기 목록을 기반으로 myshop 설정
        for (shop in shopList) {
            shop.myshop = likedShops?.contains(shop.phoneNumber) ?: false
        }

        // Adapter 설정
        shopAdapter = HairShopAdapter(shopList, requireContext())

        // LayoutManager 설정 (LinearLayoutManager 사용)
        recyclerViewShops.layoutManager = LinearLayoutManager(requireContext())

        // Adapter 연결
        recyclerViewShops.adapter = shopAdapter

        // 스와이프 기능 설정
        setupSwipe()

        // SearchView 리스너 설정
        setupSearchView()
    }

    private fun setupSwipe() {
        // 스와이프 콜백 정의
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // 이동 동작은 지원하지 않음
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.RIGHT) {
                    val position = viewHolder.adapterPosition
                    val shop = shopAdapter.filteredShopList[position]

                    // 전화 걸기 인텐트 실행
                    val dialIntent = Intent(Intent.ACTION_DIAL)
                    dialIntent.data = Uri.parse("tel:${shop.phoneNumber}")
                    try {
                        startActivity(dialIntent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "전화 기능을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }

                    // 스와이프 상태 초기화 및 투명도 복원
                    shopAdapter.notifyItemChanged(position)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX > 0) {
                    val itemView = viewHolder.itemView

                    // 그라데이션 페인트 설정
                    val paint = Paint()
                    val gradient = LinearGradient(
                        itemView.left.toFloat(),
                        itemView.top.toFloat(),
                        itemView.left + dX,
                        itemView.top.toFloat(),
                        ContextCompat.getColor(requireContext(), R.color.green),
                        ContextCompat.getColor(requireContext(), R.color.background_yellow),
                        Shader.TileMode.CLAMP
                    )
                    paint.shader = gradient

                    // 그라데이션 배경 그리기
                    c.drawRect(
                        itemView.left.toFloat(),
                        itemView.top.toFloat(),
                        itemView.left + dX,
                        itemView.bottom.toFloat(),
                        paint
                    )

                    // 전화 아이콘 그리기
                    val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_call)
                    icon?.let {
                        val intrinsicWidth = it.intrinsicWidth
                        val intrinsicHeight = it.intrinsicHeight
                        val iconMargin = (itemView.height - intrinsicHeight) / 2
                        val iconLeft = itemView.left + iconMargin
                        val iconTop = itemView.top + (itemView.height - intrinsicHeight) / 2
                        val iconRight = iconLeft + intrinsicWidth
                        val iconBottom = iconTop + intrinsicHeight
                        it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        it.draw(c)
                    }

                    // 항목을 오른쪽으로 이동
                    viewHolder.itemView.translationX = dX
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.5f // 스와이프 임계값을 절반으로 설정
            }
        }

        // ItemTouchHelper를 RecyclerView에 연결
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewShops)
    }

    private fun setupSearchView() {
        // 검색 버튼 활성화
        searchView.isSubmitButtonEnabled = true

        // 검색어 입력 리스너 설정
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색어 제출 시 필터링
                shopAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색어 변경 시 필터링
                shopAdapter.filter.filter(newText)
                return false
            }
        })
    }

    // 더미 데이터 생성 함수
    private fun generateDummyShops(): MutableList<HairShop> {
        return mutableListOf(
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
}
