package com.example.springwebtask.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
public class AddProductForm {

    @NotEmpty
    private String addProductPid;

    @NotEmpty(message = "{Product.Name.Null}")
    @Length(min = 1, max = 255 ,message="{Product.Name.length}")
    private String addProductName;


//    @Digits(integer = 2147483647, fraction = 0)
    @NotEmpty(message = "{Product.Price.Null}")
    @Range(min=0, max=2147483647, message = "{Product.Price.Range}")
    @Positive(message = "{Product.Price.Positive}")
    private String addProductPrice;

    @Range(max=2000)
    private String addProductDescription;


}
