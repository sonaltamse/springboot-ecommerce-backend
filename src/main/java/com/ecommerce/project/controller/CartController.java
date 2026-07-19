package com.ecommerce.project.controller;

import com.ecommerce.project.model.Cart;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    CartRepository cartRepository;

    @Tag(name = "Cart Controller", description = "APIs for managing shopping carts")
    @Operation(summary = "Add products to cart", description = "Adds a specified quantity of a product to the user's shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added to cart successfully"),
    })
    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductsToCart(@PathVariable Long productId, @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.addPRoductToCart(productId, quantity);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);
    }

    @Tag(name = "Cart Controller", description = "APIs for managing shopping carts")
    @Operation(summary = "Get all carts", description = "Retrieves a list of all shopping carts in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Carts retrieved successfully"),
    })
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts() {
        List<CartDTO> cartDTOs = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOs, HttpStatus.FOUND);
    }

    @Tag(name = "Cart Controller", description = "APIs for managing shopping carts")
    @Operation(summary = "Get cart by user", description = "Retrieves the shopping cart associated with the currently logged-in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Cart retrieved successfully"),
    })
    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById(){
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);
        Long cartId = cart.getCartId();
        CartDTO cartDTO = cartService.getCart(emailId, cartId);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.FOUND);
    }

    @Tag(name = "Cart Controller", description = "APIs for managing shopping carts")
    @Operation(summary = "Update product quantity in cart", description = "Updates the quantity of a specified product in the user's shopping cart. The operation can be either 'add' or 'delete'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product quantity updated successfully"),
    })
    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId, @PathVariable String operation){
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId, operation.equalsIgnoreCase("delete")?-1:1);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }

    @Tag(name = "Cart Controller", description = "APIs for managing shopping carts")
    @Operation(summary = "Delete product from cart", description = "Removes a specified product from the user's shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed from cart successfully"),
    })
    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId){
        String status =  cartService.deleteProductFromCart(cartId,productId);
        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
