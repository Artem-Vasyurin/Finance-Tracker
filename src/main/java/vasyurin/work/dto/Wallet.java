package vasyurin.work.dto;

import java.util.ArrayList;
import java.util.List;

public class Wallet {

    private final int id;
    private List<Transaction> transactions = new ArrayList<Transaction>();
    private float balance = 1000;

    public Wallet(int id) {
        this.id = id;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }


    public List<Transaction> getTransactions() {
        return transactions;
    }

    public int getId() {
        return id;
    }
}
