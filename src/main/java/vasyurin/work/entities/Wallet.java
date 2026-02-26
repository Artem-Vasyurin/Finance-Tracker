package vasyurin.work.entities;

import vasyurin.work.enums.TransactionType;
import vasyurin.work.repositories.FileStorageRepository;
import vasyurin.work.repositories.FileStorageRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class Wallet {

    private final int id;
    private final List<Transaction> transactions = new ArrayList<>();
    private final FileStorageRepository fileStorageRepository = new FileStorageRepositoryImpl();

    public Wallet(int id) {
        this.id = id;
        Transaction firstMoney = new Transaction(1,id, 1000,TransactionType.INCOME);
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

    public int getId() {
        return id;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
