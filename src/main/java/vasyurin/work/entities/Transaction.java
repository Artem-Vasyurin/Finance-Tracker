package vasyurin.work.entities;

import vasyurin.work.enums.TransactionType;

public class Transaction {

    private final int id;
    private final float amount;
    private final TransactionType type;

    public Transaction(int id, float amount, TransactionType type) {
        this.id = id;
        this.amount = amount;
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }
}
