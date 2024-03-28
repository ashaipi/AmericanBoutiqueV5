package com.AmericanBoutique.controller;

import com.AmericanBoutique.model.Product;
import com.AmericanBoutique.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        super();
        this.productService = productService;
    }

    // handler method to handle list products and return mode and view
    @GetMapping("/products")
    public String listProducts(Model model) {
        System.out.println("-[1]---> ProductController class - listProducts() method - Endpoint(/products) - HTML(products)");
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    // Add new product
    @GetMapping("/products/new")
    public String createProductForm(Model model) {
        System.out.println("-[2]---> ProductController class - createProductForm() method - Endpoint(/products/new) - HTML(create_product)");
        // create a product object to hold product form data
        Product product = new Product();
        model.addAttribute("product", product);
        return "create_product";
    }

    // Save product to database
    @PostMapping("/products")
    public String saveProduct(@ModelAttribute("product") Product product) {
        System.out.println("-[3]---> ProductController class - saveProduct() method - Endpoint(/products) - EndPoint(redirect:/products)");
        productService.saveProduct(product);
        return "redirect:/products";
    }

    // Retrieve product from a database by id
    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        System.out.println("-[4]---> ProductController class - editProductForm() method - Endpoint(/products/edit/{id}) - HTML(edit_product)");
        model.addAttribute("product", productService.getProductById(id));
        return "edit_product";
    }

    // Update the product and save it to a database
    @PostMapping("/products/{id}")
    public String updateProduct(Model model,
                                @PathVariable Long id,
                                @ModelAttribute("product") Product product){
        System.out.println("-[5]---> ProductController class - updateProduct() method - Endpoint(/products/{id}) - Endpoint(redirect:/products)");

        System.out.println("---------------> IMAGE: "+product.getImg());

        // get product from a database by id
        Product existingProduct = productService.getProductById(id);
        existingProduct.setId(id);
        existingProduct.setProductName(product.getProductName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStockQuantity(product.getStockQuantity());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setImg(product.getImg());

        // save updated product object
        productService.updateProduct(existingProduct);
        return "redirect:/products";
    }

    // handler method to handle delete product request
    @GetMapping("/products/{id}")
    public String deleteProduct(@PathVariable Long id) {
        System.out.println("-[6]---> ProductController class - deleteProduct() method - Endpoint(/products/{id}) - EndPoint(redirect:/products)");
        productService.deleteProductById(id);
        return "redirect:/products";
    }
}