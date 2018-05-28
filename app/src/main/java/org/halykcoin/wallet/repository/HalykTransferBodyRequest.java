package org.halykcoin.wallet.repository;

import java.math.BigInteger;

/**
 * Created by Maestro on 15.01.2018.
 */

public class HalykTransferBodyRequest {
    private String id;
    private String address;
    private BigInteger amount;
    private String paymentId;
    private int mixIn = 1;

    public HalykTransferBodyRequest(String id, String address, BigInteger amount, String paymentId) {
        this.id = id;
        this.address = address;
        this.amount = amount;
        this.paymentId = paymentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public int getMixIn() {
        return mixIn;
    }

    public void setMixIn(int mixIn) {
        this.mixIn = mixIn;
    }
}
