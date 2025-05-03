package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.controller.response.CartResponse;
import com.shose.shoseshop.entity.*;
import com.shose.shoseshop.exception.AppException;
import com.shose.shoseshop.exception.ErrorResponse;
import com.shose.shoseshop.repository.*;
import com.shose.shoseshop.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductDetailRepository productDetailRepository;
    private final UserRepository userRepository;
    private final CartDetailRepository cartDetailRepository;
    ProductRepository productRepository;

    @Override
    public void create(User user) {
        cartRepository.save(new Cart(user));
    }

    @Override
    public void addToCart(Long productDetailId, Long quantity, Long actionType) {
        UserDetails loginUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(loginUser.getUsername()).orElseThrow(EntityNotFoundException::new);
        Cart cart = cartRepository.findByUser_Id(user.getId()).orElseThrow(EntityNotFoundException::new);
        ProductDetail productDetail = productDetailRepository.findById(productDetailId).orElseThrow(EntityNotFoundException::new);

        // Kiểm tra số lượng yêu cầu có hợp lệ không
        if (quantity <= 0) {
            throw new AppException(ErrorResponse.OUT_OF_STOCK);
        }

        List<CartDetail> cartDetails = cart.getCartDetails();
        Optional<CartDetail> existingCartDetail = cartDetails.stream()
                .filter(cd -> cd.getProductDetail().getId().equals(productDetailId))
                .findFirst();

        if (existingCartDetail.isPresent()) {
            CartDetail cartDetail = existingCartDetail.get();
            long newQuantity;

            if (quantity == 1 && actionType != null && actionType == 1) {
                newQuantity = 1;
            } else if (quantity == 1) {
                newQuantity = cartDetail.getQuantity() + 1;
            } else {
                newQuantity = quantity;
            }

            if (newQuantity > productDetail.getQuantity()) {
                throw new AppException(ErrorResponse.OUT_OF_STOCK);
            }

            cartDetail.setQuantity(newQuantity);
            cartDetailRepository.save(cartDetail);
        } else {
            if (quantity > productDetail.getQuantity()) {
                throw new AppException(ErrorResponse.OUT_OF_STOCK);
            }

            CartDetail newCartDetail = new CartDetail();
            newCartDetail.setProductDetail(productDetail);
            newCartDetail.setQuantity(quantity);
            newCartDetail.setCart(cart);
            newCartDetail.setProductName(productDetail.getProduct().getName());
            cartDetailRepository.save(newCartDetail);
        }
    }



    @Override
    public CartResponse getCart() {
        UserDetails loginUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Login user: {}", loginUser);
        User user = userRepository.findByUsername(loginUser.getUsername()).orElseThrow(EntityNotFoundException::new);
        Cart cart = cartRepository.findByUser_Id(user.getId()).orElseThrow(EntityNotFoundException::new);
        return new ModelMapper().map(cart, CartResponse.class);
    }

    @Override
    public void deleteCartDetails(Set<Long> ids) {
        List<CartDetail> cartDetails = cartDetailRepository.findByIdIn(ids);
        if (!CollectionUtils.isEmpty(cartDetails)) {
            cartDetails.forEach(BaseEntity::markAsDelete);
        }
        cartDetailRepository.saveAll(cartDetails);
    }
}
