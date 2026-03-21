package vasyurin.work.entities;

import vasyurin.work.enums.CategoriesTypes;
import vasyurin.work.enums.TransactionTypes;

import java.sql.Timestamp;

public class Transaction {

    private final int id, walletId;
    private final float amount;
    private final TransactionTypes type;
    private final CategoriesTypes category;
    private final Timestamp created;

    public Transaction(int id, int walletId, float amount, TransactionTypes type) {
        this.id = id;
        this.walletId = walletId;
        this.amount = amount;
        this.type = type;
        this.category = CategoriesTypes.OTHER;
        this.created = new Timestamp(System.currentTimeMillis());
    }

    public Transaction(int id, int walletId, float amount, TransactionTypes type, CategoriesTypes category) {
        this.id = id;
        this.walletId = walletId;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.created = new Timestamp(System.currentTimeMillis());
    }

    public Transaction(int id, int walletId, float amount, TransactionTypes type, CategoriesTypes category, Timestamp created) {
        this.id = id;
        this.walletId = walletId;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public int getWalletId() {
        return walletId;
    }

    public float getAmount() {
        return amount;
    }

    public TransactionTypes getType() {
        return type;
    }

    public CategoriesTypes getCategory() {
        return category;
    }

    public Timestamp getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return "Transaction{" +
               "id=" + id +
               ", walletId=" + walletId +
               ", amount=" + amount +
               ", type=" + type +
               ", category=" + category +
               ", created=" + created +
               '}';
    }
}