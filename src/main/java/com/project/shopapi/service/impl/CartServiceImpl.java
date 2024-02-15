package com.project.shopapi.service.impl;

import com.project.shopapi.entity.Cart;
import com.project.shopapi.entity.Product;
import com.project.shopapi.entity.User;
import com.project.shopapi.repository.CartRepository;
import com.project.shopapi.repository.ProductRepository;
import com.project.shopapi.repository.UserRepository;
import com.project.shopapi.service.inf.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public int addProductToCart(int productId, int quantity, int customerId) {

        Product product = productRepository.findById(productId).get();
        User user = userRepository.findById(customerId).get();

        Cart cart = cartRepository.findByProductAndUser(productId, customerId);

        if (cart != null) {
            int updateQuantity = cart.getQuantity() + quantity;

            cart.setQuantity(updateQuantity);
        } else {
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setQuantity(quantity);
        }

        float amount = cart.getQuantity() * product.getPrice();
        cart.setAmount(amount);
        cartRepository.save(cart);

        return quantity;

    }

    public void updateQuantity(int productId, int quantity, int customerId, float priceProduct) {

        cartRepository.updateQuantity(productId, quantity, customerId, priceProduct);
    }

    public void removeCart(int productId, int customerId) {
        cartRepository.removeCart(productId, customerId);
    }
}
