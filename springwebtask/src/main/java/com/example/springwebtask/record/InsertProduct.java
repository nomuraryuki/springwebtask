package com.example.springwebtask.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public record InsertProduct(

        String product_id,

        int category_id,

        @NotBlank(message = "{Product.Name.Null}")
        @Length(min = 1, max = 50 ,message="{Product.Name.length}")
        String name,

//        @NotNull(message = "Product.Price.Null")
        int price,
        String description
){
}
