package com.project.shopapi.service.inf;

public interface CartService {
    public int addProductToCart(int productId, int quantity, int customerId);
    public void updateQuantity(int productId, int quantity, int customerId, float priceProduct);
    public void removeCart(int productId, int customerId);
}
