package com.example.springwebtask.record;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UpdateProductRecord(int id, String product_id, int category_id, String name,int price,String description) {
}
