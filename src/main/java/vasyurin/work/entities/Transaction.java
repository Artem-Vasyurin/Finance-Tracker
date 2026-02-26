package vasyurin.work.entities;

import vasyurin.work.enums.TransactionType;

public class Transaction {

    private final int id,walletId;
    private final float amount;
    private final TransactionType type;

    public Transaction(int id, int walletId, float amount, TransactionType type) {
        this.id = id;
        this.walletId = walletId;
        this.amount = amount;
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Transaction{" +
               "id=" + id +
               ", walletId=" + walletId +
               ", amount=" + amount +
               ", type=" + type +
               '}';
    }
}
