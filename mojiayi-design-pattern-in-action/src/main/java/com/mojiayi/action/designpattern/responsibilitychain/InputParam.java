package com.mojiayi.action.designpattern.responsibilitychain;

/**
 * @author mojiayi
 */
public class InputParam {
    private Long userId;
    private Long amount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
