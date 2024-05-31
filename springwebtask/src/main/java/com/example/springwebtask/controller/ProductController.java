package com.example.springwebtask.controller;

import com.example.springwebtask.Exception.ProductDeleteException;
import com.example.springwebtask.Exception.ProductDuplicationIdException;
import com.example.springwebtask.Exception.ProductNotFoundException;
import com.example.springwebtask.form.AddProductForm;
import com.example.springwebtask.form.LoginUserForm;

import com.example.springwebtask.form.SearchProductNameFrom;
import com.example.springwebtask.form.UpdateProductForm;
import com.example.springwebtask.record.InsertProduct;
import com.example.springwebtask.record.ProductRecord;
import com.example.springwebtask.record.SessionUserRecord;
import com.example.springwebtask.record.UserRecord;
import com.example.springwebtask.service.PgCategoriesService;
import com.example.springwebtask.service.PgProductService;
import com.example.springwebtask.service.PgUserService;
import com.sun.net.httpserver.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.Locale;

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

    @Autowired
    private MessageSource messageSource;

    private  String successMessage;

    @GetMapping("/index")
    public String index(@ModelAttribute("loginUserForm") LoginUserForm loginUserForm){

        return "index";
    }

    @PostMapping("/index")
    public String lopin_pass(@Validated @ModelAttribute("loginUserForm") LoginUserForm loginUserForm, BindingResult bindingResult, Model model){
        try {
            var userList = pgUserService.findUser(loginUserForm.getLoginUserId(), loginUserForm.getLoginUserPassword());

            if (bindingResult.hasErrors()) {
                return "index";

            } else if (loginUserForm.getLoginUserId().equals(userList.login_id()) && loginUserForm.getLoginUserPassword().equals(userList.password())) {
                var user = new SessionUserRecord(1, userList.login_id(), userList.name(),userList.role());
                session.setAttribute("user", user);
                return "redirect:/menu";
            }

        }catch (ProductNotFoundException e){

            String errorLogin = messageSource.getMessage("login.error.message",null, Locale.JAPAN);
            model.addAttribute("errorLogin", errorLogin);
            return "index";
        }
        return "index";
    }

    @GetMapping("/menu")
    public String login(@ModelAttribute("SearchProductNameFrom")SearchProductNameFrom searchProductNameFrom,@ModelAttribute("loginUserForm") LoginUserForm loginUserForm, Model model){
        if(session.getAttribute("user")==null) return "redirect:/index";

        else {
        model.addAttribute("MenuMessage", successMessage);
        model.addAttribute("SearchProductNameFrom", searchProductNameFrom);
        model.addAttribute("productList", pgProductService.findAll());
        model.addAttribute("countDB", pgProductService.findAll().size());
        return "menu";
        }
    }

    @GetMapping("/menu/search")
    public String searchName(@RequestParam(name="searchName") String name,@ModelAttribute("SearchProductNameFrom") SearchProductNameFrom searchProductNameFrom, Model model){

        if(session.getAttribute("user")==null) return "redirect:/index";

        else {

            name = searchProductNameFrom.getSearchName();

            model.addAttribute("productList", pgProductService.searchName(name));
            model.addAttribute("countDB", pgProductService.searchName(name).size());
            return "/menu";
        }

    }

    @GetMapping("/insert")
    public String insert_product(@ModelAttribute("addProductForm") AddProductForm addProductForm, Model model){
        if(session.getAttribute("user")==null) return "redirect:/index";
        else {
            model.addAttribute("categoriesList", pgCategoriesService.findAll());
            return "insert";
        }
    }

    @PostMapping("/insert")
    public String addProduct(@Validated @ModelAttribute("addProductForm") AddProductForm addProductForm, BindingResult bindingResult, Model model) {
        if(session.getAttribute("user")==null) return "redirect:/index";
        model.addAttribute("categoriesList", pgCategoriesService.findAll());

        if(bindingResult.hasErrors()) {
            return "insert";
        }
        else{

            var product_id = addProductForm.getAddProductPid();
            var name = addProductForm.getAddProductName();
            var price = Integer.parseInt(addProductForm.getAddProductPrice());
            var description = addProductForm.getAddProductDescription();
            var Cid = Integer.parseInt(addProductForm.getAddProductCid());
            var insertProduct = new InsertProduct(product_id,Cid,name,price,description);

            try {
                pgProductService.insert(insertProduct);
                successMessage = "登録が完了しました";
//                System.out.println(successMessage);
//                model.addAttribute("MenuMessage", successMessage);

                return "redirect:/menu";
            }catch (ProductDuplicationIdException e){
                String errorMessage = messageSource.getMessage("productid.error.message",null, Locale.JAPAN);
                model.addAttribute("errorMessage", errorMessage);
                return "insert";
            }
        }
    }

    @GetMapping("/logout")
    public String logoutget(@ModelAttribute("loginForm") LoginUserForm loginUserForm) {
        session.invalidate();
        return "logout";
    }

    @GetMapping("/detail/{pId}")
    public  String detail(@PathVariable("pId") int productId, Model model ){
        if(session.getAttribute("user")==null) return "redirect:/index";
        model.addAttribute("productId", pgProductService.findById(productId));
        return "detail";
    }

    @PostMapping("/delete/{pId}")
    public String delete(@PathVariable("pId") int productId, Model model ){
        if(session.getAttribute("user")==null) return "redirect:/index";

        try {
            model.addAttribute("productId", pgProductService.findById(productId));
            pgProductService.delete(productId);
            successMessage = "削除に成功しました";
            return "redirect:/menu";
        }catch (ProductNotFoundException e){
            String deleteError = "削除に失敗しました";
            model.addAttribute("deleteError", deleteError);
            return "redirect:/detail/{pId}";
        }
    }

    @GetMapping("/updateInput/{pId}")
    public  String update(@PathVariable("pId") int productId, @ModelAttribute("updateProductForm") UpdateProductForm updateProductForm, Model model ){
        try {
            if (session.getAttribute("user") == null) return "redirect:/index";
            model.addAttribute("categoriesList", pgCategoriesService.findAll());
            model.addAttribute("productId", pgProductService.findById(productId));
            ProductRecord productRecord = pgProductService.findById(productId);
            int category_id = pgCategoriesService.findIdByName(productRecord.name()).id();
            model.addAttribute("category_id", category_id);
            updateProductForm.setUpdateProductId(productRecord.product_id());
            updateProductForm.setUpdateProductName(productRecord.name());
            updateProductForm.setUpdateProductPrice(String.valueOf(productRecord.price()));
            updateProductForm.setUpdateDescription(productRecord.description());
            return "updateInput";
        }catch (RuntimeException e){
            return "detail";
        }
    }

//    @GetMapping("/updateInput")
//    public  String update( Model model ){
//        if(session.getAttribute("user")==null) return "redirect:/index";
//
//        return "updateInput";
//    }

}
