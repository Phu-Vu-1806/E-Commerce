package com.project.shopapi.controller;

import com.project.shopapi.entity.Product;
import com.project.shopapi.security.service.UserDetailsImpl;
import com.project.shopapi.service.impl.CartServiceImpl;
import com.project.shopapi.service.impl.ProductServiceImpl;
import com.project.shopapi.utils.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {
    private final CartServiceImpl cartService;
    private final ProductServiceImpl productService;

    @PostMapping("/add/{productId}/{quantity}")
    public ResponseEntity<?> addProductToCart(Authentication authentication,
                                              @PathVariable("productId") int productId,
                                              @PathVariable("quantity") int quantity) {

        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        Integer result = cartService.addProductToCart(productId, quantity, user.getId());

        return new ResponseEntity<MessageResponse>(
                new MessageResponse(result + " item(s) of this product were added to your shopping cart."),
                HttpStatus.OK);

    }

    @PutMapping("/update/{productId}/{quantity}")
    public ResponseEntity<?> updateQuantity(Authentication authentication, @PathVariable("productId") int productId,
                                            @PathVariable("quantity") int quantity) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        Product product = productService.getById(productId);
        float priceProduct = product.getPrice();

        cartService.updateQuantity(productId, quantity, user.getId(), priceProduct);

        MessageResponse msg = new MessageResponse("Updated quantity sucessfully");

        return new ResponseEntity<MessageResponse>(msg, HttpStatus.OK);

    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeCart(Authentication authentication, @PathVariable("productId") int productId) {

        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        cartService.removeCart(productId, user.getId());

        MessageResponse msg = new MessageResponse("Deleted cart sucessfully");

        return new ResponseEntity<MessageResponse>(msg, HttpStatus.OK);

    }
}
