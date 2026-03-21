package vasyurin.work.services;

import vasyurin.work.entities.Transaction;
import vasyurin.work.entities.Wallet;
import vasyurin.work.enums.CategoriesTypes;
import vasyurin.work.enums.TransactionTypes;
import vasyurin.work.services.interfaces.FileStorageService;
import vasyurin.work.services.interfaces.FilterService;

import java.io.IOException;
import java.util.List;


public class FilterServiceImpl implements FilterService {

    private static final FileStorageService fileStorageService = new FileStorageServiceImpl();

    @Override
    public List<Transaction> filteringTransactionsByCategory(Wallet wallet, Integer choice) throws IOException {

        switch (choice) {
            case 1 -> {
                return getAndFilteringByCategory(wallet, CategoriesTypes.SUPERMARKET);
            }
            case 2 -> {
                return getAndFilteringByCategory(wallet, CategoriesTypes.FASTFOOD);
            }
            case 3 -> {
                return getAndFilteringByCategory(wallet, CategoriesTypes.CLOTHING);
            }
            case 4 -> {
                return getAndFilteringByCategory(wallet, CategoriesTypes.TICKETS);
            }
            case 5 -> {
                return getAndFilteringByCategory(wallet, CategoriesTypes.OTHER);
            }
        }
        return List.of();
    }

    @Override
    public List<Transaction> filteringTransactionsByMonth(Wallet wallet, Integer month) throws IOException {
        return getAndFilteringByMonth(wallet, month);
    }

    @Override
    public List<Transaction> filteringTransactionsByType(Wallet wallet, Integer choice) throws IOException {

        switch (choice) {
            case 1 -> {
                return getAndFilteringByType(wallet, TransactionTypes.INCOME);
            }
            case 2 -> {
                return getAndFilteringByType(wallet, TransactionTypes.EXPENSE);
            }
        }
        return List.of();
    }

    private List<Transaction> getAndFilteringByCategory(Wallet wallet, CategoriesTypes category) throws IOException {

        return fileStorageService.download(wallet)
                .stream()
                .filter(transaction -> transaction.getCategory() == category)
                .toList();
    }

    private List<Transaction> getAndFilteringByMonth(Wallet wallet, Integer month) throws IOException {

        return fileStorageService.download(wallet)
                .stream()
                .filter(transaction -> transaction.getCreated().toLocalDateTime().getMonthValue() == month)
                .toList();
    }

    private List<Transaction> getAndFilteringByType(Wallet wallet, TransactionTypes type) throws IOException {

        return fileStorageService.download(wallet)
                .stream()
                .filter(transaction -> transaction.getType() == type)
                .toList();
    }
}