package com.example.springwebtask.controller;

import com.example.springwebtask.Exception.ProductNotFoundException;
import com.example.springwebtask.form.AddProductForm;
import com.example.springwebtask.form.LoginUserForm;
import com.example.springwebtask.record.InsertProduct;
import com.example.springwebtask.record.SessionUserRecord;
import com.example.springwebtask.record.UserRecord;
import com.example.springwebtask.service.PgCategoriesService;
import com.example.springwebtask.service.PgProductService;
import com.example.springwebtask.service.PgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {
    @Autowired
    PgUserService pgUserService;

    @Autowired
    PgProductService pgProductService;

    @Autowired
    PgCategoriesService pgCategoriesService;
    @Autowired
    private HttpSession session;

    @GetMapping("/index")
    public String index(@ModelAttribute("loginUserForm") LoginUserForm loginUserForm){
//        System.out.println(pgUserService.findUser(loginUserForm.getLoginUserId(), loginUserForm.getLoginUserPassword()));
        return "index";
    }

    @PostMapping("/index")
    public String lopin_pass(@Validated @ModelAttribute("loginUserForm") LoginUserForm loginUserForm, BindingResult bindingResult, Model model){
        try {
            var userList = pgUserService.findUser(loginUserForm.getLoginUserId(), loginUserForm.getLoginUserPassword());
//            System.out.println(userList.password());
            if (bindingResult.hasErrors()) {
                System.out.println(0);
                return "index";

            } else if (loginUserForm.getLoginUserId().equals(userList.login_id()) && loginUserForm.getLoginUserPassword().equals(userList.password())) {
                System.out.println(1);
                var user = new SessionUserRecord(1, userList.login_id(), userList.name());
                session.setAttribute("user", user);
//            var login_user = new UserRecord(loginUserForm.setLoginUserId(),loginUserForm.getLoginUserPassword());
                return "redirect:/menu";
            }
        }catch (ProductNotFoundException e){
            System.out.println(2);
            return "index";
        }
        System.out.println(3);
            return "index";
    }

    @GetMapping("/menu")
    public String login(@ModelAttribute("loginUserForm") LoginUserForm loginUserForm, Model model){

        model.addAttribute("productList", pgProductService.findAll());
        return "menu";
    }

    @GetMapping("/insert")
    public String insert_product(@ModelAttribute("addProductForm") AddProductForm addProductForm, Model model){

        model.addAttribute("categoriesList", pgCategoriesService.findAll());
        return "insert";
    }

    @PostMapping("/insert")
    public String addProduct(@Validated @ModelAttribute("addProductForm") AddProductForm addProductForm, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "insert";
        }
        else{
            var product_id = addProductForm.getAddProductPid();
            var name = addProductForm.getAddProductName();
            var price = Integer.parseInt(addProductForm.getAddProductPrice());

            var insertProduct = new InsertProduct(product_id,1,name,price);
            pgProductService.insert(insertProduct);
            return "redirect:/insert";
        }
    }

}
