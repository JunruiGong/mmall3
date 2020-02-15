package com.mmall.common;

/**
 * @Author: Junrui Gong
 * @Date: 2/14/20
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public interface Role {
        int role_customer = 0; // 消费者
        int role_admin = 1;  // 管理员

    }


}
