package com.example.madcamp1stweek

data class GalleryItem(
    var index: Int,
    val imageUrl: String,
    var description: String,
    var rating: Float ,// 별점 추가
    var hairshopName: String // 미용실 이름 추가
)
