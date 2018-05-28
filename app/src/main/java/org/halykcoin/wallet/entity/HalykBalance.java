package org.halykcoin.wallet.entity;

import org.halykcoin.wallet.C;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * Created by Maestro on 11.01.2018.
 */

public class HalykBalance {
    private BigInteger balance;
    private BigInteger unlocked_balance;

    public HalykBalance(BigInteger balance, BigInteger unlocked_balance) {
        this.balance = balance;
        this.unlocked_balance = unlocked_balance;
    }

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    public BigInteger getUnlocked_balance() {
        return unlocked_balance;
    }

    public void setUnlocked_balance(BigInteger unlocked_balance) {
        this.unlocked_balance = unlocked_balance;
    }

    private String getFormattedValue(BigInteger value){
//        BigInteger[] divident = value.divideAndRemainder(new BigInteger(C.HLC_DIVIDER));
//        return divident[0].toString() + "." + divident[1].toString();
        BigDecimal b1 = new BigDecimal(value.toString());
        BigDecimal b2 = new BigDecimal(C.HLC_DIVIDER);
        return b1.divide(b2, MathContext.DECIMAL32).toPlainString();
    }

    public String getFormattedBalance(){
        return getFormattedValue(balance);
    }

    public String getFormattedUnlockedBalance(){
        return getFormattedValue(unlocked_balance);
    }
}
