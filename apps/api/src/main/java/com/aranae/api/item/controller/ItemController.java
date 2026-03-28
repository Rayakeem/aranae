package com.aranae.api.item.controller;

import com.aranae.api.common.response.PageResponse;
import com.aranae.api.item.controller.dto.ItemCreateRequest;
import com.aranae.api.item.controller.dto.ItemResponse;
import com.aranae.api.item.controller.dto.ItemUpdateRequest;
import com.aranae.api.item.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public PageResponse<ItemResponse> findAll(
            @RequestParam(required = false) String query,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return PageResponse.from(itemService.findAll(query, pageable));
    }

    @GetMapping("/{id}")
    public ItemResponse findById(@PathVariable Long id) {
        return itemService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse create(@RequestBody @Valid ItemCreateRequest request) {
        return itemService.create(request);
    }

    @PutMapping("/{id}")
    public ItemResponse update(@PathVariable Long id,
                               @RequestBody @Valid ItemUpdateRequest request) {
        return itemService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }
}
