package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartVo {
    /**
     * 商品集合
     */
    private List<CartProductVo> cartProductVos;
    private BigDecimal cartTotalPrice;
    /**
     * 是否全部勾选
     */
    private boolean allChecked;
    private String imageHost;

    public List<CartProductVo> getCartProductVos() {
        return cartProductVos;
    }

    public void setCartProductVos(List<CartProductVo> cartProductVos) {
        this.cartProductVos = cartProductVos;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public boolean isAllChecked() {
        return allChecked;
    }

    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
