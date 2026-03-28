package com.aranae.api.celebrity.repository;

import com.aranae.api.celebrity.domain.Celebrity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CelebrityRepository extends JpaRepository<Celebrity, Long> {

    Page<Celebrity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // findOrCreate용 정확한 이름 검색 (그룹명 포함)
    Optional<Celebrity> findFirstByNameIgnoreCaseAndGroupNameIgnoreCase(String name, String groupName);

    // 그룹명 없이 이름만으로 검색
    Optional<Celebrity> findFirstByNameIgnoreCase(String name);
}
