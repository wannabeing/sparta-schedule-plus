package org.example.spartascheduleplus.dto.common;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageInfo {
    private final int totalCount; // 전체 항목 수
    private final int pages; // 전체 페이지 개수
    private final int number; // 현재 페이지 번호
    private final int size; // 한 페이지당 보여주는 항목 수

    private final boolean hasNext; // 다음 페이지 존재여부
    private final boolean hasPrevious; // 이전 페이지 존재여부
    private final boolean isLast; // 마지막 페이지 여부

    // ✅ 생성자 (페이징 객체를 받아 생성)
    public PageInfo(Page<?> page)
    {
        this.totalCount = (int) page.getTotalElements();
        this.pages = page.getTotalPages();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
        this.isLast = page.isLast();
    }
}
