package com.apnacolor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.apnacolor.entity.CartItem;
import com.apnacolor.services.CartItemService;

@RestController
@RequestMapping("/api/cart")
//@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/add")
    public CartItem addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        return cartItemService.addToCart(userId, productId, quantity);
    }

    @GetMapping("/items")
    public List<CartItem> getCartItems(@RequestParam Long userId) {
        return cartItemService.getCartItems(userId);
    }

    @PutMapping("/update")
    public CartItem updateQuantity(@RequestParam Long cartItemId, @RequestParam int quantity) {
        return cartItemService.updateQuantity(cartItemId, quantity);
    }

    @DeleteMapping("/remove")
    public String removeFromCart(@RequestParam Long cartItemId) {
        boolean removed = cartItemService.removeFromCart(cartItemId);
        return removed ? "Item removed" : "Item not found";
    }
}
