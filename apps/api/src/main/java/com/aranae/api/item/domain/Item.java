package com.aranae.api.item.domain;

import com.aranae.api.common.domain.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(
        name = "items",
        uniqueConstraints = @UniqueConstraint(columnNames = "uniqueKey")
)
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String brand;
    private String category;
    private Integer price;
    private String imageKey;
    private String purchaseUrl;

    @Column(nullable = false, unique = true)
    private String uniqueKey;

    protected Item() {
    }

    // 생성 시 uniqueKey를 강제하기 위한 팩토리 메서드(초안)
    public static Item create(String name, String brand, String category) {
        Item item = new Item();
        item.name = name;
        item.brand = brand;
        item.category = category;
        item.uniqueKey = generateUniqueKey(name, brand, category);
        return item;
    }

    private static String generateUniqueKey(String name, String brand, String category) {
        return normalize(brand) + "|" + normalize(name) + "|" + normalize(category);
    }

    private static String normalize(String v) {
        if (v == null) return "";
        return v.trim().toLowerCase().replaceAll("\\s+", "-");
    }
}
