package com.javafun.resilience4j;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/order")
public class OrderController {


    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/check")
    //@CircuitBreaker(name = "inventoryBreak", fallbackMethod = "inventoryFallback")
    @Bulkhead(name = "inventoryBulkHead", fallbackMethod = "inventoryFallback")
    @ResponseBody
    public String checkOrder  (@RequestBody  Inventory inventory){

        System.out.println("Inside check order , for product "+inventory.productName);

            ResponseEntity<Boolean> result = restTemplate.exchange(new RequestEntity(inventory, HttpMethod.POST, URI.create("http://localhost:9090/inventory/check")),Boolean.class);

            if(result.getBody()){
                System.out.println("Order Placed Successfully");
                return  "Order Placed Successfully";
            }
            System.out.println("Order failed");
            return "Order failed.";

    }



    public String inventoryFallback(Inventory inventory,Throwable t){
        System.out.println(t);
        System.out.println("Inside fallback , for product "+inventory.productName);
        return "Product out of Stock";
    }

}
