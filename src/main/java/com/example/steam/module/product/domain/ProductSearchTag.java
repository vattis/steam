package com.example.steam.module.product.domain;

import lombok.Getter;

@Getter
public enum ProductSearchTag {
    ALL("ALL"), NAME("NAME"), COMPANY("COMPANY");

    String label;

    ProductSearchTag(String name) {

    }
}
