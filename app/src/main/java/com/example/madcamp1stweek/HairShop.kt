package com.example.madcamp1stweek

data class HairShop(
    val name: String,
    val phoneNumber: String,
    val imageResId: Int, // 로컬 drawable 리소스 ID
    val latitude: Double,
    val longitude: Double,
    // 이 미용실이 제공하는 FilterOption들을 명시 (Set or List)
    val providedOptions: Set<FilterOption>,
    var myshop: Boolean = false // 즐겨찾기 상태 추가
)
