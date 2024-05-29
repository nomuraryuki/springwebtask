package com.example.springwebtask.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginUserForm {

    @NotEmpty(message = "{User.Id.NotEmpty}")
    private String loginUserId;

    @NotEmpty(message = "{User.Password.NotEmpty}")
    private String loginUserPassword;
}
