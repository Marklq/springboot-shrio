package com.dd.shiro.pojo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String Id;
    private String username;
    private String password;
    private String perms;

}
