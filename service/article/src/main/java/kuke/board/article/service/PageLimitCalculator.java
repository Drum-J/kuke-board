package kuke.board.article.service;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class PageLimitCalculator {

    public static Long calculatePageLimit(Long page, Long pageSize, Long moveablePageCount) {
        return (((page - 1) / moveablePageCount) + 1) * pageSize * moveablePageCount + 1;
    }
}
