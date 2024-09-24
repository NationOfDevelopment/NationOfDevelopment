package com.sparta.nationofdevelopment.domain.store.entity;

import com.sparta.nationofdevelopment.domain.menu.entity.Menu;
import com.sparta.nationofdevelopment.domain.order.entity.Orders;
import com.sparta.nationofdevelopment.domain.review.entity.Review;
import com.sparta.nationofdevelopment.domain.store.dto.request.StoreRequestDto;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private int minOrderMount;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    // 유저와의 다대일 관계
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    // 메뉴와의 일대다 관계
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Menu> menuList = new ArrayList<>();

    // 주문과의 일대다 관계
    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Orders> orderList = new ArrayList<>();

    // 리뷰와의 일대다 관계
    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviewList = new ArrayList<>();


    public Store(StoreRequestDto requestDto, Users user) {
        this.storeName = requestDto.getStoreName();
        this.openTime = requestDto.getOpenTime();
        this.closeTime = requestDto.getCloseTime();
        this.minOrderMount = requestDto.getMinOrderMount();
        this.user = user;
        this.status = StoreStatus.OPEN;
    }

    public Store(String storeName, int minOrderMount, LocalTime openTime, LocalTime closeTime, StoreStatus status, Users user) {
        this.storeName = storeName;
        this.minOrderMount = minOrderMount;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = status;
        this.user = user;
    }



    public void closeStore() {
        this.status = StoreStatus.CLOSED;
    }

    public void update(StoreRequestDto requestDto) {
        this.storeName = requestDto.getStoreName();
        this.openTime = requestDto.getOpenTime();
        this.closeTime = requestDto.getCloseTime();
        this.minOrderMount = requestDto.getMinOrderMount();
    }

    public List<Menu> getMenus() {
        return menuList;
    }
}
