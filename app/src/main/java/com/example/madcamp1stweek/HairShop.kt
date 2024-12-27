package com.example.madcamp1stweek

data class HairShop(
    val name: String,
    val phoneNumber: String,
    val imageResId: Int, // 로컬 drawable 리소스 ID
    var myshop: Boolean = false // 즐겨찾기 상태 추가
)
