package com.example.madcamp1stweek

/** 필터 유형 */
enum class FilterType(val order: Int) {
    GENDER(0),
    STYLE(1),
    SERVICE(2),
    ETC(3),
}

enum class SelectionMode {
    SINGLE,
    MULTIPLE
}

/** 필터 옵션 */
enum class FilterOption(val filterType: FilterType, val optionName: String, val selectionMode: SelectionMode) {
    // 성별
    MAN(FilterType.GENDER, "남성", SelectionMode.SINGLE),
    WOMAN(FilterType.GENDER, "여성", SelectionMode.SINGLE),

    // 스타일
    TRENDY(FilterType.STYLE, "트렌디한", SelectionMode.MULTIPLE),
    LUXURIOUS(FilterType.STYLE, "고급스러운", SelectionMode.MULTIPLE),
    MY_STYLE(FilterType.STYLE, "개성있는", SelectionMode.MULTIPLE),
    LESS_DAMAGE(FilterType.STYLE, "손상이 적은", SelectionMode.MULTIPLE),
    STAYING_POWER(FilterType.STYLE, "유지력이 좋은", SelectionMode.MULTIPLE),
    NATURAL(FilterType.STYLE, "자연스러운", SelectionMode.MULTIPLE),

    // 서비스
    GGOMGGOM(FilterType.SERVICE, "꼼꼼한 시술", SelectionMode.MULTIPLE),
    KIND(FilterType.SERVICE, "친절은 생명", SelectionMode.MULTIPLE),
    COUNSEL(FilterType.SERVICE, "자세한 상담", SelectionMode.MULTIPLE),
    A_S(FilterType.SERVICE, "세심한 A/S", SelectionMode.MULTIPLE),
    FUN_TALK(FilterType.SERVICE, "즐거운 대화", SelectionMode.MULTIPLE),
    NO_TALK(FilterType.SERVICE, "필요한 대화만", SelectionMode.MULTIPLE),

    // 기타
    CLEAN_ROOM(FilterType.ETC, "청결한 매장", SelectionMode.MULTIPLE),
    GOOD_PRICE(FilterType.ETC, "합리적인 가격", SelectionMode.MULTIPLE),
    GOOD_PARKING(FilterType.ETC, "편리한 주차", SelectionMode.MULTIPLE),
    MANY_MONEY(FilterType.ETC, "비싼만큼 좋아요", SelectionMode.MULTIPLE),
    MANY_REVIEW(FilterType.ETC, "리뷰가 많아요", SelectionMode.MULTIPLE);

    companion object {
        // 필터 유형에 해당하는 선택지 리스트 반환
        fun findOptionsByFilterType(type: FilterType): List<FilterOption> {
            return entries.filter { it.filterType == type }
        }

        // 필터 유형 순서에 따라 정렬된 필터 옵션 리스트 반환
        fun getOptionsSortedByFilterType(): List<List<FilterOption>> {
            return FilterType.values().sortedBy { it.order }.map { type ->
                findOptionsByFilterType(type)
            }
        }
    }
}
