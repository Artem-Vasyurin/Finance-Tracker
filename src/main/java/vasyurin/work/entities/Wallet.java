package vasyurin.work.entities;

import vasyurin.work.enums.TransactionType;

import java.util.ArrayList;
import java.util.List;

public class Wallet {

    private final int id;
    private final List<Transaction> transactions = new ArrayList<>();

    public Wallet(int id) {
        this.id = id;
        Transaction firstMoney = new Transaction(1,1000, TransactionType.INCOME);
        transactions.add(firstMoney);
    }

    public float getBalance() {
        float balance = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getType().equals(TransactionType.INCOME)) {
                balance += transaction.getAmount();
            } else if (transaction.getType().equals(TransactionType.EXPENSE)) {
                balance -= transaction.getAmount();
            }
        }
        return balance;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}
