package com.example.madcamp1stweek

// DashboardItem.kt (새 파일 추가 가능)
sealed class DashboardItem {
    // 상단 왼쪽만 차지할 SNIP 헤더
    object Header : DashboardItem()

    // 기존의 GalleryItem을 포함한 일반 카드
    data class GalleryCard(val galleryItem: GalleryItem) : DashboardItem()
}
