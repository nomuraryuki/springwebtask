package com.example.springwebtask.controller;

import com.example.springwebtask.Exception.ProductDuplicationIdException;
import com.example.springwebtask.Exception.ProductNotFoundException;
import com.example.springwebtask.form.AddProductForm;
import com.example.springwebtask.form.LoginUserForm;
import com.example.springwebtask.form.SearchProductNameFrom;
import com.example.springwebtask.record.InsertProduct;
import com.example.springwebtask.record.SessionUserRecord;
import com.example.springwebtask.record.UserRecord;
import com.example.springwebtask.service.PgCategoriesService;
import com.example.springwebtask.service.PgProductService;
import com.example.springwebtask.service.PgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/index")
    public String index(@ModelAttribute("loginUserForm") LoginUserForm loginUserForm){

        return "index";
    }

    @PostMapping("/index")
    public String lopin_pass(@Validated @ModelAttribute("loginUserForm") LoginUserForm loginUserForm, BindingResult bindingResult, Model model){
        try {
            var userList = pgUserService.findUser(loginUserForm.getLoginUserId(), loginUserForm.getLoginUserPassword());
//            System.out.println(userList.password());
            if (bindingResult.hasErrors()) {
                return "index";

            } else if (loginUserForm.getLoginUserId().equals(userList.login_id()) && loginUserForm.getLoginUserPassword().equals(userList.password())) {
                var user = new SessionUserRecord(1, userList.login_id(), userList.name());
                session.setAttribute("user", user);
//            var login_user = new UserRecord(loginUserForm.setLoginUserId(),loginUserForm.getLoginUserPassword());
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
        model.addAttribute("SearchProductNameFrom", searchProductNameFrom);
        model.addAttribute("productList", pgProductService.findAll());
            model.addAttribute("countDB", pgProductService.findAll().size());
        return "menu";
        }
    }

    @PostMapping("/menu")
    public String searchName(@ModelAttribute("SearchProductNameFrom") SearchProductNameFrom searchProductNameFrom, Model model){

        if(session.getAttribute("user")==null) return "redirect:/index";

        else {

            var name = searchProductNameFrom.getSearchName();
            model.addAttribute("productList", pgProductService.searchName(name));
            model.addAttribute("countDB", pgProductService.searchName(name).size());
            return "menu";
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
    public String addProduct(@Validated @ModelAttribute("addProductForm") AddProductForm addProductForm, BindingResult bindingResult,Model model) {
        model.addAttribute("categoriesList", pgCategoriesService.findAll());

        var category_id = addProductForm.getAddProductCid();
        System.out.println(category_id);
        if(bindingResult.hasErrors()) {
//            System.out.println(0);
            return "insert";
        }
        else{

            var product_id = addProductForm.getAddProductPid();
            var name = addProductForm.getAddProductName();
            var price = Integer.parseInt(addProductForm.getAddProductPrice());
            var description = addProductForm.getAddProductDescription();
            var Cid = Integer.parseInt(addProductForm.getAddProductCid());
            var insertProduct = new InsertProduct(product_id,Cid,name,price,description);

//            System.out.println(1);
            try {
                pgProductService.insert(insertProduct);
//                System.out.println(2);
                return "redirect:/insert";
            }catch (ProductDuplicationIdException e){
                String errorMessage = messageSource.getMessage("productid.error.message",null, Locale.JAPAN);
                model.addAttribute("errorMessage", errorMessage);
//                System.out.println(3);
                return "insert";
            }
        }
    }

    @GetMapping("/logout")
    public String logoutget(@ModelAttribute("loginForm") LoginUserForm loginUserForm) {
        session.invalidate();
        return "logout";
    }


}
