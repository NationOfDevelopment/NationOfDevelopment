package com.sparta.nationofdevelopment.domain.store.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Store {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "min_order_amount")
    private int minOrderAmount;
    @Column(name = "store_name")
    private String storeName;
    @Column(name = "store_status")
    private boolean storeStatus;

}
