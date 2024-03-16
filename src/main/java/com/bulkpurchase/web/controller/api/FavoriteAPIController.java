package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.user.FavoriteDeleteListDTO;
import com.bulkpurchase.domain.dto.user.FavoriteProductDTO;
import com.bulkpurchase.domain.dto.user.FavoriteResponse;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.FavoriteProduct;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.FavoriteProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
public class FavoriteAPIController {

    private final UserAuthValidator userAuthValidator;
    private final FavoriteProductService favoriteProductService;
    private final ProductService productService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> toggleFavorite(@RequestParam(value = "productID") Long productID, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        Optional<Product> productOpt = productService.findById(productID);
        if (productOpt.isPresent()) {
            boolean isFavorited = favoriteProductService.toggleFavorite(user, productOpt.get());
            return ResponseEntity.ok(new FavoriteResponse(isFavorited));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "정상적인 요청이 아닙니다."));
        }
    }

    @DeleteMapping("/list")
    @ResponseBody
    public ResponseEntity<?> deleteFavoriteList(@RequestBody FavoriteDeleteListDTO favoriteDeleteListDTO, Principal principal) {
        for (Long productID : favoriteDeleteListDTO.getProductIDs()) {
            Product product = productService.findById(productID).orElse(null);
            User user = userAuthValidator.getCurrentUser(principal);
            favoriteProductService.toggleFavorite(user, product);
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "선택한 찜 상품이 삭제되었습니다."));
    }

    @GetMapping("/list")
    public ResponseEntity<?> favoriteList(@RequestParam(value = "page", defaultValue = "1") Integer page, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page - 1, 10, sort);

        Page<FavoriteProduct> favoriteProductsPage = favoriteProductService.findByUser(user, pageable);
        List<FavoriteProductDTO> favoriteProductDTOs = favoriteProductsPage.getContent().stream()
                .map(FavoriteProductDTO::new)
                .collect(Collectors.toList());

        Map<String, Object> responsePayload = Map.of(
                "totalPages", favoriteProductsPage.getTotalPages(),
                "favoriteProductDTOs", favoriteProductDTOs
        );

        return ResponseEntity.ok(responsePayload);
    }


    @GetMapping
    public ResponseEntity<?> favoriteOne(@RequestParam(value = "id") Long id, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        Optional<FavoriteProduct> favoriteProductOpt = favoriteProductService.findByUserAndId(user, id);
        if (favoriteProductOpt.isPresent()) {
            FavoriteProductDTO favoriteProductDTO = new FavoriteProductDTO(favoriteProductOpt.get());
            return ResponseEntity.status(HttpStatus.OK).body(favoriteProductDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "존재하지 않는 찜 상품입니다."));
        }
    }
}
