package org.halykcoin.wallet.entity;

import java.math.BigInteger;

/**
 * Created by Maestro on 15.01.2018.
 */

public class HalykTransferDetails {

    private BigInteger[] amount_list;
    private BigInteger[] fee_list;
    private String[] tx_hash_list;

    public HalykTransferDetails(BigInteger[] amount_list, BigInteger[] fee_list, String[] tx_hash_list) {
        this.amount_list = amount_list;
        this.fee_list = fee_list;
        this.tx_hash_list = tx_hash_list;
    }

    public BigInteger[] getAmount_list() {
        return amount_list;
    }

    public void setAmount_list(BigInteger[] amount_list) {
        this.amount_list = amount_list;
    }

    public BigInteger[] getFee_list() {
        return fee_list;
    }

    public void setFee_list(BigInteger[] fee_list) {
        this.fee_list = fee_list;
    }

    public String[] getTx_hash_list() {
        return tx_hash_list;
    }

    public void setTx_hash_list(String[] tx_hash_list) {
        this.tx_hash_list = tx_hash_list;
    }
}
