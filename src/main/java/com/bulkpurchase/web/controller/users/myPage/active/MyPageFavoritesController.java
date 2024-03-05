package com.bulkpurchase.web.controller.users.myPage.active;

import com.bulkpurchase.domain.dto.review.ReviewDetailDTO;
import com.bulkpurchase.domain.entity.user.FavoriteProduct;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.FavoriteProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class MyPageFavoritesController {


    private final UserAuthValidator userAuthValidator;
    private final FavoriteProductService favoriteProductService;

    @GetMapping
    public String myPageReviews(Model model, Principal principal, @RequestParam(value = "page", required = false) Integer pageNum) {
        if (pageNum == null) {
            pageNum = 0;
        }
        User user = userAuthValidator.getCurrentUser(principal);
        Pageable page = PageRequest.of(pageNum, 3);
        Page<FavoriteProduct> favoriteProducts = favoriteProductService.findByUser(user, page);

        model.addAttribute("favoriteProducts", favoriteProducts);
        model.addAttribute("totalPage", favoriteProducts.getTotalPages());
        return "users/myPage/active/favorites";
    }

}
