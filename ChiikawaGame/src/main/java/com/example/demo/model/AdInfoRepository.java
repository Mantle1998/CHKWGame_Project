package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdInfoRepository extends JpaRepository<AdInfo, Integer>{ //繼承JpaRepository的屬性和方法，根據adId來操作class AdInfo的資料
}
