package com.example.madcamp1stweek

data class GalleryItem(
    val index: Int,
    val imageUrl: String,
    val description: String,
    val rating: Float ,// 별점 추가
    val hairshopName: String // 미용실 이름 추가
)
