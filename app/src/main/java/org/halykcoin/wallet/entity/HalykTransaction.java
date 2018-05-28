package org.halykcoin.wallet.entity;

import java.math.BigInteger;

/**
 * Created by Maestro on 14.01.2018.
 */

public class HalykTransaction {

    public enum TransactionType {
        IN,
        OUT,
        PENDING
    }

    private BigInteger amount;
    private BigInteger fee;
    private BigInteger height;
    private String note;
    private String payment_id;
    private Object subaddr_index;
    private long timestamp;
    private String txid;
    private String type;
    private long unlock_time;
    private String address;


    public HalykTransaction(BigInteger amount, BigInteger fee, BigInteger height, String note, String payment_id, Object subaddr_index, long timestamp, String txid, String type, long unlock_time, String address) {
        this.amount = amount;
        this.fee = fee;
        this.height = height;
        this.note = note;
        this.payment_id = payment_id;
        this.subaddr_index = subaddr_index;
        this.timestamp = timestamp;
        this.txid = txid;
        this.type = type;
        this.unlock_time = unlock_time;
        this.address = address;
    }

    public HalykTransaction(BigInteger amount, BigInteger fee, BigInteger height, String note, String payment_id, Object subaddr_index, long timestamp, String txid, String type, long unlock_time) {
        this(amount, fee, height, note, payment_id, subaddr_index, timestamp, txid, type, unlock_time, "");
    }

    public HalykTransaction(BigInteger amount, String address, String payment_id) {
        this(amount, new BigInteger("0"), new BigInteger("0"), "", payment_id, null, 0, "", "", 0,address);
    }

    public TransactionType getTransactionType() {
        switch (type) {
            case "in": return TransactionType.IN;
            case "out": return TransactionType.OUT;
            case "pending": return TransactionType.PENDING;
            default: return TransactionType.IN;
        }
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public BigInteger getFee() {
        return fee;
    }

    public void setFee(BigInteger fee) {
        this.fee = fee;
    }

    public BigInteger getHeight() {
        return height;
    }

    public void setHeight(BigInteger height) {
        this.height = height;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public Object getSubaddr_index() {
        return subaddr_index;
    }

    public void setSubaddr_index(Object subaddr_index) {
        this.subaddr_index = subaddr_index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getUnlock_time() {
        return unlock_time;
    }

    public void setUnlock_time(long unlock_time) {
        this.unlock_time = unlock_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
