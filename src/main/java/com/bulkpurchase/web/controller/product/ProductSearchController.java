package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.dto.product.ProductForSalesVolumeSortDTO;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductService productService;

    @GetMapping("/product/search")
    public String productSearchView(@RequestParam(value = "q", required = false) String productName,
                                    @RequestParam(value = "p", required = false) Integer page,
                                    @RequestParam(value = "sortField", required = false) String sortField,
                                    @RequestParam(value = "sortDir", required = false) String sortDir ,
                                    @RequestParam(value = "size", required = false) Integer size,
                                    Model model) {
        if (size == null) {
            size = 12;
        }
        if (page==null) {
            page = 1;
        }

        if (sortDir == null) {
            sortDir = "desc";
        }
        if (productName == null) {
            return "redirect:/";
        }


        if (sortField == null) {
            sortField = "productID";
        }

        if (sortField.equals("salesVolume")) {
            Page<ProductForSalesVolumeSortDTO> productPage = productService.findProductsBySearchTermAndSortBySalesVolume(productName, PageRequest.of(page - 1, size));
            model.addAttribute("productPage", productPage);
            model.addAttribute("totalPages", productPage.getTotalPages());
            System.out.println("productPage = " + productPage);
        } else {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField);
            Pageable pageable = PageRequest.of(page-1, size, sort);
            Page<Product> productPage = productService.findPageByProductNameContaining(pageable, productName);
            model.addAttribute("productPage", productPage);
            model.addAttribute("totalPages", productPage.getTotalPages());
        }

        model.addAttribute("page", page);
        model.addAttribute("q", productName);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "product/productSearchView";
    }
}
