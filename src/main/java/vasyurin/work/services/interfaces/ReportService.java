package vasyurin.work.services.interfaces;

import vasyurin.work.entities.Transaction;

import java.util.List;

public interface ReportService {
    void printTransactionsTable(List<Transaction> transactions);
}