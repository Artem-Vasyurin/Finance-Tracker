package vasyurin.work.entities;

import vasyurin.work.enums.TransactionTypes;

import java.util.ArrayList;
import java.util.List;

public class Wallet {

    private final int id;
    private final List<Transaction> transactions = new ArrayList<>();

    public Wallet(int id) {
        this.id = id;
        Transaction firstMoney = new Transaction(1, id, 1000, TransactionTypes.INCOME);
        transactions.add(firstMoney);
    }

    public float getBalance() {
        float balance = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getType().equals(TransactionTypes.INCOME)) {
                balance += transaction.getAmount();
            } else if (transaction.getType().equals(TransactionTypes.EXPENSE)) {
                balance -= transaction.getAmount();
            }
        }
        return balance;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public int getId() {
        return id;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}