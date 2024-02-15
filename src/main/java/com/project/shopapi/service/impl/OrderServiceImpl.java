package com.project.shopapi.service.impl;

import com.project.shopapi.entity.Order;
import com.project.shopapi.entity.Product;
import com.project.shopapi.entity.Sold;
import com.project.shopapi.entity.User;
import com.project.shopapi.exception.BadRequestException;
import com.project.shopapi.exception.NotFoundException;
import com.project.shopapi.model.dto.OrderDetailsDto;
import com.project.shopapi.model.dto.OrderInfoDto;
import com.project.shopapi.model.request.CreateOrderReq;
import com.project.shopapi.repository.OrderRepository;
import com.project.shopapi.repository.ProductRepository;
import com.project.shopapi.repository.SoldRepository;
import com.project.shopapi.service.inf.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.shopapi.utils.Constant.*;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final SoldRepository soldRepository;

    public Order createOrder(CreateOrderReq req, int userId) {

        Order order = new Order();

        Optional<Product> product = productRepository.findById(req.getProductId());

        if (product.isEmpty()) {
            throw new NotFoundException("Sản phẩm không tồn tại");
        }

        order.setCreatedAt(new Date());

        User createdBy = new User(userId);
        order.setCreatedBy(createdBy);
        order.setReceiverAddress(req.getReceiverAddress());
        order.setReceiverName(req.getReceiverName());
        order.setReceiverPhone(req.getReceiverPhone());

        order.setProduct(product.get());
        order.setSize(req.getSize());
        order.setQuantity(req.getQuantity());
//        order.setProductPrice(req.getProductPrice());
        order.setProductPrice((long) product.get().getPrice());
        order.setProductName(product.get().getName());
        order.setStatus(ORDER_STATUS);

        orderRepository.save(order);

        Sold sold = soldRepository.findByProduct(product.get());
        sold.setQuantity(sold.getQuantity() + req.getQuantity());
        soldRepository.save(sold);

        return order;

    }

    public List<OrderInfoDto> getListOrderOfPersonByStatus(int status, Integer userId) {

        List<Order> order = orderRepository.getListOrderOfPersonByStatus(status, userId);

        List<OrderInfoDto> dtos = order.stream().map(o -> OrderInfoDto.builder()
                .id(o.getId())
                .productName(o.getProductName())
                .productPrice(o.getProductPrice())
                .size(o.getSize())
                .build()).collect(Collectors.toList());

        return dtos;

    }

    public OrderDetailsDto getOrderDetailsByUser(Integer id, Integer userId) {

        Order order = orderRepository.getOrderDetailByUser(id, userId);

        OrderDetailsDto dto = OrderDetailsDto.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .productPrice(order.getProductPrice())
                .receiverName(order.getReceiverName())
                .receiverPhone(order.getReceiverPhone())
                .quantity(order.getQuantity())
                .receiverAddress(order.getReceiverAddress())
                .status(order.getStatus())
                .productName(order.getProductName())
                .build();

        if (order.getStatus() == ORDER_STATUS) {
            dto.setStatusText("Cho lay hang");
        } else if (order.getStatus() == DELIVERY_STATUS) {
            dto.setStatusText("Dang giao hang");
        } else if (order.getStatus() == CANCELED_STATUS) {
            dto.setStatusText("Da Huy");
        } else if (order.getStatus() == RETURNED_STATUS) {
            dto.setStatusText("Da tra hang");
        }

        return dto;

    }

    public void userCancelOrder(Integer id, Integer userId) {

        Optional<Order> rs = orderRepository.findById(id);

        if (rs.isEmpty()) {
            throw new NotFoundException("Đơn hàng không tồn tại");

        }

        Order order = rs.get();

        if (order.getCreatedBy().getId() != userId) {
            throw new BadRequestException("Bạn không phải chủ nhân đơn hàng");

        }

        if (order.getStatus() != ORDER_STATUS) {
            throw new BadRequestException("Trạng thái đơn hàng không phù hợp để hủy");

        }

        order.setStatus(CANCELED_STATUS);

        orderRepository.save(order);

    }
}
