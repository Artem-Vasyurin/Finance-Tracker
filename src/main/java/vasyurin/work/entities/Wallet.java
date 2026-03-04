package vasyurin.work.entities;

import vasyurin.work.enums.TransactionTypes;
import vasyurin.work.services.TransactionServiceImpl;
import vasyurin.work.services.interfaces.TransactionService;

import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private static final TransactionService transactionService = new TransactionServiceImpl();

    private final int id;
    private final List<Transaction> transactions = new ArrayList<>();

    public Wallet(int id) {
        this.id = id;
        Transaction firstMoney = new Transaction(1, id, 1000, TransactionTypes.INCOME);
        transactions.add(firstMoney);
    }

    public int getId() {
        return id;
    }

    public float getBalance() {
      return transactionService.getSumOfAllTransaction(transactions);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void addTransactions(List<Transaction> transactions) {
        transactions.forEach(this::addTransaction);
    }

    public void clearTransactions() {
        transactions.clear();
    }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }
}