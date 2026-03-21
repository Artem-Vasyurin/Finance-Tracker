package vasyurin.work.services.interfaces;

import vasyurin.work.entities.Transaction;
import vasyurin.work.entities.Wallet;

import java.util.List;

public interface TransactionService {
    void createTransaction(Wallet senderWallet, Wallet receiverWallet, int sumTransaction);

    Float getSumOfAllTransaction(List<Transaction> transactions);
}