package com.example.bikerent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.bikerent.mapper")
public class BikeRentBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikeRentBackendApplication.class, args);
    }

}
