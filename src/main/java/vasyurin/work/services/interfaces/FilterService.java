package vasyurin.work.services.interfaces;

import vasyurin.work.entities.Transaction;

import java.io.IOException;
import java.util.List;

public interface FilterService {

    List<Transaction> filteringTransactionsByCategory(Integer walletId, Integer choice) throws IOException;

    List<Transaction> filteringTransactionsByMonth(Integer walletId, Integer month) throws IOException;

    List<Transaction> filteringTransactionsByType(Integer walletId, Integer choice) throws IOException;
}