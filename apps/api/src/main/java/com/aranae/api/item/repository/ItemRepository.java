package com.aranae.api.item.repository;

import com.aranae.api.item.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // 이름 또는 브랜드로 검색
    Page<Item> findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(
            String name, String brand, Pageable pageable);

    // 중복 등록 방지 확인용
    Optional<Item> findByUniqueKey(String uniqueKey);
}
