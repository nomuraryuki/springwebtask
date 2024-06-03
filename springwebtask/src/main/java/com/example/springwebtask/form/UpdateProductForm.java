package com.example.springwebtask.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
public class UpdateProductForm {

    @NotEmpty(message = "{Product.ProductId.Null}")
    @Pattern(regexp="[0-9]{5}", message = "{Product.Pid.pattern}")
    private String updateProductId;

    @NotEmpty(message = "{Product.Name.Null}")
    @Length(min = 1, max = 255 ,message="{Product.Name.length}")
    private String updateProductName;

    @NotEmpty(message = "{Product.Price.Null}")
    @Range(min=0, max=2147483647, message = "{Product.Price.Range}")
    @Positive(message = "{Product.Price.Positive}")
    private String updateProductPrice;

    private String updateCategoryId;

    private String updateCategoryName;

    @Length(min=0, max=2000)
    private String updateDescription;

    private Integer id;
}
