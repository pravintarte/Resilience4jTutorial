package com.javafun.resilience4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.stream.IntStream;

@SpringBootApplication
public class Resilience4jMain  {

    public static void main(String[] args) {
        SpringApplication.run(Resilience4jMain.class,args);
        Inventory inventory = new Inventory();
        inventory.productName="Soap";
        inventory.unit=9;
        IntStream.rangeClosed(1,100).parallel().forEach(t->{
            ResponseEntity<String> result = new RestTemplate().exchange(new RequestEntity(inventory, HttpMethod.POST, URI.create("http://localhost:8080/order/check")),String.class);
        });


    }


}
