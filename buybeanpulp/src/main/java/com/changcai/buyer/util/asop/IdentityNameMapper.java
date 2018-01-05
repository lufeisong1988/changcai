package com.changcai.buyer.util.asop;



/**
 * Created by liuxingwei on 2017/1/24.
 */

final class IdentityNameMapper implements NameMapper{
    public static final NameMapper INSTANCE = new IdentityNameMapper();

    private IdentityNameMapper() {
    }

    public String map(String name) {
        return name;
    }
}
