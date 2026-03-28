package com.aranae.api.item.domain;

import com.aranae.api.common.domain.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "items")
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

    // 일반 구매 링크 (브랜드 공식몰, 무신사 등)
    private String purchaseUrl;

    // 어필리에이트 링크 (쿠팡파트너스, 네이버 쇼핑파트너 등 수익 추적용)
    private String affiliateUrl;

    @Column(nullable = false, unique = true)
    private String uniqueKey;

    protected Item() {
    }

    public static Item create(String name, String brand, String category,
                              Integer price, String imageKey,
                              String purchaseUrl, String affiliateUrl) {
        Item item = new Item();
        item.name = name;
        item.brand = brand;
        item.category = category;
        item.price = price;
        item.imageKey = imageKey;
        item.purchaseUrl = purchaseUrl;
        item.affiliateUrl = affiliateUrl;
        item.uniqueKey = generateUniqueKey(name, brand, category);
        return item;
    }

    public void update(String name, String brand, String category,
                       Integer price, String imageKey,
                       String purchaseUrl, String affiliateUrl) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.imageKey = imageKey;
        this.purchaseUrl = purchaseUrl;
        this.affiliateUrl = affiliateUrl;
        this.uniqueKey = generateUniqueKey(name, brand, category);
    }

    private static String generateUniqueKey(String name, String brand, String category) {
        return normalize(brand) + "|" + normalize(name) + "|" + normalize(category);
    }

    private static String normalize(String v) {
        if (v == null) return "";
        return v.trim().toLowerCase().replaceAll("\\s+", "-");
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getCategory() { return category; }
    public Integer getPrice() { return price; }
    public String getImageKey() { return imageKey; }
    public String getPurchaseUrl() { return purchaseUrl; }
    public String getAffiliateUrl() { return affiliateUrl; }
    public String getUniqueKey() { return uniqueKey; }
}
