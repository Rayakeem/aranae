package com.aranae.api.item.service;

import com.aranae.api.item.controller.dto.ItemCreateRequest;
import com.aranae.api.item.controller.dto.ItemResponse;
import com.aranae.api.item.controller.dto.ItemUpdateRequest;
import com.aranae.api.item.domain.Item;
import com.aranae.api.common.exception.NotFoundException;
import com.aranae.api.item.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Page<ItemResponse> findAll(String query, Pageable pageable) {
        if (query != null && !query.isBlank()) {
            // 이름 또는 브랜드 모두 검색
            return itemRepository
                    .findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(query, query, pageable)
                    .map(ItemResponse::from);
        }
        return itemRepository.findAll(pageable).map(ItemResponse::from);
    }

    public ItemResponse findById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 아이템입니다. id=" + id));
        return ItemResponse.from(item);
    }

    @Transactional
    public ItemResponse create(ItemCreateRequest request) {
        // uniqueKey 중복 확인 (같은 브랜드+이름+카테고리 조합이 이미 존재하는지)
        String uniqueKey = buildUniqueKey(request.name(), request.brand(), request.category());
        itemRepository.findByUniqueKey(uniqueKey).ifPresent(existing -> {
            throw new IllegalStateException("이미 등록된 아이템입니다. id=" + existing.getId());
        });

        Item item = Item.create(
                request.name(), request.brand(), request.category(),
                request.price(), request.imageKey(),
                request.purchaseUrl(), request.affiliateUrl()
        );
        return ItemResponse.from(itemRepository.save(item));
    }

    @Transactional
    public ItemResponse update(Long id, ItemUpdateRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 아이템입니다. id=" + id));
        item.update(request.name(), request.brand(), request.category(),
                request.price(), request.imageKey(),
                request.purchaseUrl(), request.affiliateUrl());
        return ItemResponse.from(item);
    }

    @Transactional
    public void delete(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new NotFoundException("존재하지 않는 아이템입니다. id=" + id);
        }
        itemRepository.deleteById(id);
    }

    private String buildUniqueKey(String name, String brand, String category) {
        return normalize(brand) + "|" + normalize(name) + "|" + normalize(category);
    }

    private String normalize(String v) {
        if (v == null) return "";
        return v.trim().toLowerCase().replaceAll("\\s+", "-");
    }
}
