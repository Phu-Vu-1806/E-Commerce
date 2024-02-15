package com.project.shopapi.service.inf;

import com.project.shopapi.entity.Order;
import com.project.shopapi.model.dto.OrderDetailsDto;
import com.project.shopapi.model.dto.OrderInfoDto;
import com.project.shopapi.model.request.CreateOrderReq;

import java.util.List;

public interface OrderService {
    Order createOrder(CreateOrderReq req, int  userId);
    List<OrderInfoDto> getListOrderOfPersonByStatus(int status, Integer userId);
    OrderDetailsDto getOrderDetailsByUser(Integer id, Integer userId);
    void userCancelOrder(Integer id, Integer userId);
}
