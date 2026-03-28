package com.aranae.api.search.controller;

import com.aranae.api.search.controller.dto.SearchResponse;
import com.aranae.api.search.service.SearchService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    // GET /search?q=민지
    // GET /search?q=만약에+우리
    @GetMapping
    public SearchResponse search(
            @RequestParam String q,
            @PageableDefault(size = 10) Pageable pageable) {
        return searchService.search(q, pageable);
    }
}
