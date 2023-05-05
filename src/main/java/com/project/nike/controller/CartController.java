package com.project.nike.controller;

import com.project.nike.dto.AddToCartDto;
import com.project.nike.dto.CartDto;
import com.project.nike.exception.CartItemNotExistException;
import com.project.nike.model.*;
import com.project.nike.repository.CartItemRepository;
import com.project.nike.repository.CartRepository;
import com.project.nike.repository.ProductRepository;
import com.project.nike.repository.UserRepository;
import com.project.nike.response.ResponseHandler;
import com.project.nike.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private VoucherService voucherService;

    @GetMapping("/")
    public ResponseEntity<Object> cartPage() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(username);
        Cart cart = cartRepository.findCartByUser(user.get());
        if(cart==null) {
            cart = new Cart();
            cart.setUser(userRepository.getUsersByEmail(username));
            cartRepository.save(cart);
        }
        CartDto cartDto = cartService.listCartItems(cart);
        if (cart.getVoucher() == null){
            return ResponseHandler.responseEntity("success", HttpStatus.ACCEPTED, cartDto);
        }
        cartDto.setVoucherId(cart.getVoucher().getId());
        cartDto.setTotalMoney(cartDto.getTotalMoney() - cartDto.getTotalMoney() * voucherService.getDiscountById(cart.getVoucher().getId()));
        //oke done
        return ResponseHandler.responseEntity("success", HttpStatus.ACCEPTED, cartDto);
    }

    @PostMapping("/voucher/select/{id}")
    public ResponseEntity<Object> selectVoucher(@PathVariable long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(username);
        Cart cart = cartRepository.findCartByUser(user.get());
        if(cart==null) {
            cart = new Cart();
            cart.setUser(userRepository.getUsersByEmail(username));
            cartRepository.save(cart);
        }
        Voucher voucher = voucherService.findVoucherById(id);
        cart.setVoucher(voucher);
        cartRepository.save(cart);

        return ResponseHandler.responseEntity("oke", HttpStatus.OK, "voucher has been selected");
    }

    @GetMapping("/addToCart")
    public ResponseEntity<Object> addToCart(@RequestBody AddToCartDto addToCartDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser  = userRepository.getUsersByEmail(username);

        Cart cart = cartRepository.findCartByUser(currentUser);
        if(cart==null) {
            cart = new Cart();
            cart.setUser(userRepository.getUsersByEmail(username));
            cartRepository.save(cart);
        }

        Optional<Product> product = Optional.ofNullable(productRepository.findById(addToCartDto.getProductId())).orElseThrow(() -> new RuntimeException("Something error! "));
        if (product.isEmpty()){
            return ResponseHandler.errorResponseEntity("failure", HttpStatus.BAD_REQUEST, "not found product");
        }
        //handle if product has been in cart already
        CartItem cartItem = cartItemRepository.findByProductAndCart(product.get(), cart);
        if (cartItem == null){
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product.get());
            cartItem.setQuantity(1);
            cartItemRepository.save(cartItem);
            return ResponseHandler.responseEntity("success", HttpStatus.CREATED, "added to cart");
        }else {
            cartItem.setQuantity(cartItem.getQuantity()+1);
            cartItemRepository.save(cartItem);
            return ResponseHandler.responseEntity("success", HttpStatus.CREATED, "added to cart one more");
        }
    }

    @PutMapping("/changCartItemQuanity/{id}")
    public ResponseEntity<Object> changeQuanity(@PathVariable String id,@RequestParam String quantity) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser  = userRepository.getUsersByEmail(username);
        Cart cart = cartRepository.getCartByUser(currentUser);
        Product product = productRepository.findById(Long.parseLong(id)).orElseThrow(() -> new RuntimeException("Something error! "));
        CartItem cartItem = cartItemRepository.findByProductAndCart(product, cart);
        if (cartItem == null){
            throw new CartItemNotExistException("this is not found");
        }
        cartItem.setQuantity(Integer.parseInt(quantity));
        cartItemRepository.save(cartItem);
        return ResponseHandler.responseEntity("success", HttpStatus.ACCEPTED, "this quantity has been changed successfully");
    }

    @PostMapping("/deleteCartItem/{id}")
    public ResponseEntity<Object> deleteCartItem(@PathVariable String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser  = userRepository.getUsersByEmail(username);

        Cart cart = cartRepository.getCartByUser(currentUser);
        Product product = productRepository.findById(Long.parseLong(id)).orElseThrow(() -> new RuntimeException("Something error! "));
        CartItem cartItem = cartItemRepository.findByProductAndCart(product,cart);

        if (cartItem == null){
            throw new CartItemNotExistException("this is not found");
        }

        cartItemRepository.delete(cartItem);

        return ResponseHandler.responseEntity("success", HttpStatus.ACCEPTED, "cartItem"+ cartItem + "is deleted from cart");
    }
}

