package com.example.springwebtask.controller;

import com.example.springwebtask.Exception.ProductDeleteException;
import com.example.springwebtask.Exception.ProductDuplicationIdException;
import com.example.springwebtask.Exception.ProductNotFoundException;
import com.example.springwebtask.form.AddProductForm;
import com.example.springwebtask.form.LoginUserForm;

import com.example.springwebtask.form.SearchProductNameFrom;
import com.example.springwebtask.form.UpdateProductForm;
import com.example.springwebtask.record.*;
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

    private String updateProductId;

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
        successMessage="";
        if(session.getAttribute("user")==null) return "redirect:/index";

        else {
            try {
                name = searchProductNameFrom.getSearchName();

                model.addAttribute("productList", pgProductService.searchName(name));
                model.addAttribute("countDB", pgProductService.searchName(name).size());
                return "/menu";
            }catch (NullPointerException e){
                successMessage="'"+name+"'"+"に該当する検索結果がありませんでした";
                return "redirect:/menu";
            }

        }

    }

    @GetMapping("/insert")
    public String insert_product(@ModelAttribute("addProductForm") AddProductForm addProductForm, Model model){
        successMessage="";
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

            var name = addProductForm.getAddProductName();
            var price = Integer.parseInt(addProductForm.getAddProductPrice());
            var description = addProductForm.getAddProductDescription();
            var Cid = Integer.parseInt(addProductForm.getAddProductCid());
            var product_id = addProductForm.getAddProductPid();


            try {
                var insertProduct = new InsertProduct(product_id,Cid,name,price,description);
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
    public  String detail(@PathVariable("pId") int pId, Model model ){
        successMessage="";
        if(session.getAttribute("user")==null) return "redirect:/index";
        model.addAttribute("productId", pgProductService.findById(pId));
        return "detail";
    }

    @PostMapping("/delete/{pId}")
    public String delete(@PathVariable("pId") int pId, Model model ){
        if(session.getAttribute("user")==null) return "redirect:/index";

        try {
            model.addAttribute("productId", pgProductService.findById(pId));
            pgProductService.delete(pId);
            successMessage = "削除に成功しました";
            return "redirect:/menu";
        }catch (Exception e){
            successMessage = "削除に失敗しました";
//            return "redirect:/detail/{pId}";
            return "redirect:/menu";
        }
    }

    @GetMapping("/updateInput/{pId}")
    public  String updateGet(@PathVariable("pId") int pId, @ModelAttribute("updateProductForm") UpdateProductForm updateProductForm, Model model ){
        try {
            updateProductId="";
            if (session.getAttribute("user") == null) return "redirect:/index";
            model.addAttribute("categoriesList", pgCategoriesService.findAll());
            model.addAttribute("productId", pgProductService.findById(pId));
            ProductRecord productRecord = pgProductService.findById(pId);
            CategoriesRecord category_id = pgCategoriesService.findIdByName(productRecord.category());
            updateProductForm.setUpdateCategoryId(String.valueOf(category_id.id()));
            updateProductForm.setUpdateProductId(productRecord.product_id());
            updateProductForm.setUpdateProductName(productRecord.name());
            updateProductForm.setUpdateProductPrice(String.valueOf(productRecord.price()));
            updateProductForm.setUpdateDescription(productRecord.description());
            updateProductId=productRecord.product_id();
            return "updateInput";
        }catch (RuntimeException e){
            return "updateInput";
        }
    }

    @PostMapping("/updateInput/{pId}")
    public String updatePost(@Validated @ModelAttribute("updateProductForm") UpdateProductForm updateProductForm,BindingResult bindingResult,@PathVariable("pId") int pId, Model model){
        if(bindingResult.hasErrors()) {
            model.addAttribute("categoriesList", pgCategoriesService.findAll());
            model.addAttribute("productId", pgProductService.findById(pId));
            String errorUpdateMessage = messageSource.getMessage("update.product.error.message",null, Locale.JAPAN);
            model.addAttribute("errorUpdateMessage", errorUpdateMessage);
            return "updateInput";
        }

        else if(session.getAttribute("user")==null) return "redirect:/index";

        else{
            model.addAttribute("categoriesList", pgCategoriesService.findAll());
            var product_id = updateProductForm.getUpdateProductId();
            var name = updateProductForm.getUpdateProductName();
            var price = Integer.parseInt(updateProductForm.getUpdateProductPrice());
            var description = updateProductForm.getUpdateDescription();
            var Cid = Integer.parseInt(updateProductForm.getUpdateCategoryId());
            var updateProduct = new UpdateProductRecord(pId,product_id,Cid,name,price,description);
            var sufferProductId = pgProductService.findProductIdSuffer(product_id);

                if (sufferProductId==null || updateProductId.equals(product_id)) {
                    pgProductService.update(updateProduct);
                    successMessage = "更新が完了しました";
                    return "redirect:/menu";
                }else {
                    model.addAttribute("productId", pgProductService.findById(pId));
                    String errorMessage = messageSource.getMessage("productid.error.message",null, Locale.JAPAN);
                    model.addAttribute("errorMessage", errorMessage);
                    String errorUpdateMessage = messageSource.getMessage("update.product.error.message",null, Locale.JAPAN);
                    model.addAttribute("errorUpdateMessage", errorUpdateMessage);
                    return "updateInput";
                }
        }
    }
//    @GetMapping("success")
//    public String success(@ModelAttribute("loginForm") LoginUserForm loginUserForm){
//        return "success";
//    }
}
