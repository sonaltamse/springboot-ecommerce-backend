package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotfoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.repositories.CartItemRepository;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDTO addPRoductToCart(Long productId, Integer quantity) {
       Cart cart = createCart();

       Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotfoundException("Product", "productId", productId));

       CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

       if(cartItem!=null) throw new APIException("Product"+product.getProductName()+" already exists in the cart");
       if(product.getQuantity() == 0) throw new APIException(product.getProductName()+" is not available");
       if(product.getQuantity() < quantity) throw new APIException("Please, make an order of the "+product.getProductName()+" less than or equal to the quantity"+product.getQuantity()+".");

       CartItem newCartItem = new CartItem();
       newCartItem.setCart(cart);
       newCartItem.setProduct(product);
       newCartItem.setQuantity(quantity);
       newCartItem.setDiscount(product.getDiscount());
       newCartItem.setProductPrice(product.getSpecialPrice());

       cartItemRepository.save(newCartItem);
       product.setQuantity(product.getQuantity());
       cart.setTotalPrice(cart.getTotalPrice()+(product.getSpecialPrice()*quantity));
       cartRepository.save(cart);
       CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
       List<CartItem> cartItems = cart.getCartItems();
       Stream<ProductDTO> productDTOStream = cartItems.stream().map(item -> {
           ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
           map.setQuantity(item.getQuantity());
           return map;
       });
       cartDTO.setProducts(productDTOStream.toList());
       return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts =  cartRepository.findAll();
        if(carts.size()==0) throw new APIException("No carts found");

        List<CartDTO> cartDTOs = carts.stream().map(cart-> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> products = cart.getCartItems().stream().map(
                    p -> modelMapper.map(p.getProduct(), ProductDTO.class)).toList();
            cartDTO.setProducts(products);
            return cartDTO;
        }).collect(Collectors.toList());

        return cartDTOs;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if (userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());

        return cartRepository.save(cart);
    }
}
