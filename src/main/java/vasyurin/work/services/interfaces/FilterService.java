package vasyurin.work.services.interfaces;

import vasyurin.work.entities.Transaction;
import vasyurin.work.entities.Wallet;

import java.io.IOException;
import java.util.List;

public interface FilterService {

    List<Transaction> filteringTransactionsByCategory(Wallet wallet, Integer choice) throws IOException;

    List<Transaction> filteringTransactionsByMonth(Wallet wallet, Integer month) throws IOException;

    List<Transaction> filteringTransactionsByType(Wallet wallet, Integer choice) throws IOException;
}