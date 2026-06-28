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
import jakarta.transaction.Transactional;
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

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if(createCart()==null) throw  new ResourceNotfoundException("Cart", "cartId", cartId);
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(c->c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> products = cart.getCartItems().stream().map(
                p -> modelMapper.map(p.getProduct(), ProductDTO.class)).toList();
        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Long cartId = userCart.getCartId();

        Cart cart = cartRepository.findById(cartId).
                orElseThrow(()-> new ResourceNotfoundException("Cart", "cartId", cartId));
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotfoundException("Product", "productId", productId));

        if(product.getQuantity() == 0) throw new APIException(product.getProductName()+" is not available");
        if(product.getQuantity() < quantity) throw new APIException("Please, make an order of the "+product.getProductName()+" less than or equal to the quantity"+product.getQuantity()+".");

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if(cartItem==null) throw new APIException("Product"+product.getProductName()+" does not exist in the cart");

        int newQuantity = cartItem.getQuantity()+quantity;
        if(newQuantity<0) throw new APIException("Product quantity cannot be negative. Current quantity: "+cartItem.getQuantity());
        if(newQuantity==0) deleteProductFromCart(cartId,productId);
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());
            cartItem.setProductPrice(product.getSpecialPrice());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
            cartItemRepository.save(cartItem);
        }
        CartItem updatedItem = cartItemRepository.save(cartItem);
        if(updatedItem.getQuantity() == 0){
            cartItemRepository.deleteById(updatedItem.getCartItemId());
        }
        CartDTO  cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item->{
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    @Override
    @Transactional
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(()->new ResourceNotfoundException("Cart","cartId",cartId));
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if(cartItem==null) throw new ResourceNotfoundException("Product","productId",productId);

        cart.setTotalPrice(cart.getTotalPrice()-(cartItem.getProductPrice()*cartItem.getQuantity()));
        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId,productId);
        return "Product"+cartItem.getProduct().getProductName()+" has been removed from the cart !!!";
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(()->new ResourceNotfoundException("Cart","cartId",cartId));
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if(cartItem==null) throw new APIException("Product with id "+productId+" does not exist in the cart with id "+cartId);

        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotfoundException("Product", "productId", productId));

        double cartPrice = cart.getTotalPrice()-(cartItem.getProductPrice()*cartItem.getQuantity());
        cartItem.setProductPrice(product.getSpecialPrice());
        cart.setTotalPrice(cartPrice+(cartItem.getProductPrice()*cartItem.getQuantity()));
        cartItem = cartItemRepository.save(cartItem);
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
