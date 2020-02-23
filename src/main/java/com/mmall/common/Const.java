package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @Author: Junrui Gong
 * @Date: 2/14/20
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public interface Role {
        int role_customer = 0; // 消费者
        int role_admin = 1;  // 管理员

    }

    public enum ProductStatusEnum {
        ON_SALE(1, "在线"),
        NON_SALE(2, "下架");
        private String value;
        private int code;

        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }


}
