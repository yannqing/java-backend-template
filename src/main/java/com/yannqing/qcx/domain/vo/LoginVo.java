package com.yannqing.qcx.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo {

    private Integer userId;
    private String userName;
    private String token;
    private List<Integer> authList;

    public LoginVo(User user, String token, List<Integer> authList) {
        this.userId = user.getUserId();
        this.userName = user.getUsername();
        this.token = token;
        this.authList = authList;
    }

}
