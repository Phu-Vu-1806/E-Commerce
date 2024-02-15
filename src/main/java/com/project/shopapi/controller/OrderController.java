package com.project.shopapi.controller;

import com.project.shopapi.entity.Order;
import com.project.shopapi.model.dto.OrderDetailsDto;
import com.project.shopapi.model.dto.OrderInfoDto;
import com.project.shopapi.model.request.CreateOrderReq;
import com.project.shopapi.security.service.UserDetailsImpl;
import com.project.shopapi.service.inf.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/order")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderReq orderReq, Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        System.out.println("User: "+user);

        Order order = orderService.createOrder(orderReq, user.getId());

        return ResponseEntity.ok(order.getId());
    }

    @GetMapping(value = "/api/order/")
    public ResponseEntity<?> getListOrderOfPersonByStatus(@RequestParam(name = "status") int status,
                                                          Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        List<OrderInfoDto> dtos = orderService.getListOrderOfPersonByStatus(status, user.getId());
        return ResponseEntity.ok(dtos);

    }

    @GetMapping(value = "/api/order/{orderId}")
    public ResponseEntity<?> getOrderDetailsByUser(@PathVariable(name = "orderId") Integer orderId,
                                                   Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        OrderDetailsDto dto = orderService.getOrderDetailsByUser(orderId, user.getId());
        return ResponseEntity.ok(dto);

    }

    @GetMapping(value = "/api/order/{orderId}/cancelOrder")
    public ResponseEntity<?> userCancelOrder(@PathVariable(name = "orderId") Integer orderId,
                                             Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        orderService.userCancelOrder(orderId, user.getId());
        return (ResponseEntity<?>) ResponseEntity.ok();

    }
}
