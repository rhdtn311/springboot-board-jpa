package com.devcourse.springbootboardjpa.common.dto.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PageDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @Range(min = 0, max = Integer.MAX_VALUE)
        private int page;

        @Range(min = 1, max = Integer.MAX_VALUE)
        private int size;

        @Range(min = 1, max = Integer.MAX_VALUE)
        private int totalPage;

        public Pageable makePageable() {
            return PageRequest.of(page, size, Sort.by("createAt"));
        }
    }

    @AllArgsConstructor
    @Getter
    public static class Response<E, DTO> {
        private List<DTO> data;
        private boolean isPrev;
        private boolean isNext;
        private List<Integer> pages;
        private int nowPage;

        public Response(Page<E> page, Function<E, DTO> entityToDtoFunction, int totalPage) {
            int startPageNumber = totalPage * ((page.getNumber()) / totalPage) + 1;
            int endPageNumber = Math.min(page.getTotalPages(), startPageNumber + totalPage - 1);

            this.nowPage = page.getNumber() + 1;
            this.data = page.getContent()
                    .stream().map(entityToDtoFunction)
                    .collect(Collectors.toList());
            this.isNext = startPageNumber + totalPage - 2 < page.getTotalPages();
            this.isPrev = startPageNumber > 1;
            this.pages = IntStream.rangeClosed(startPageNumber, endPageNumber)
                    .boxed()
                    .collect(Collectors.toList());
        }
    }
}
