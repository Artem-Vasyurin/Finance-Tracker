package vasyurin.work.services;

import vasyurin.work.entities.Transaction;
import vasyurin.work.enums.CategoriesTypes;
import vasyurin.work.enums.TransactionTypes;
import vasyurin.work.services.interfaces.FileStorageService;
import vasyurin.work.services.interfaces.FilterService;

import java.io.IOException;
import java.util.List;


public class FilterServiceImpl implements FilterService {

    private static final FileStorageService fileStorageService = new FileStorageServiceImpl();

    @Override
    public List<Transaction> filteringTransactionsByCategory(Integer walletId, Integer choice) throws IOException {

        switch (choice) {
            case 1 -> {
                return getAndFilteringByCategory(walletId, CategoriesTypes.SUPERMARKET);
            }
            case 2 -> {
                return getAndFilteringByCategory(walletId, CategoriesTypes.FASTFOOD);
            }
            case 3 -> {
                return getAndFilteringByCategory(walletId, CategoriesTypes.CLOTHING);
            }
            case 4 -> {
                return getAndFilteringByCategory(walletId, CategoriesTypes.TICKETS);
            }
            case 5 -> {
                return getAndFilteringByCategory(walletId, CategoriesTypes.OTHER);
            }
        }
        return List.of();
    }


    @Override
    public List<Transaction> filteringTransactionsByMonth(Integer walletId, Integer month) throws IOException {
        return getAndFilteringByMonth(walletId, month);
    }

    @Override
    public List<Transaction> filteringTransactionsByType(Integer walletId, Integer choice) throws IOException {

        switch (choice) {
            case 1 -> {
                return getAndFilteringByType(walletId, TransactionTypes.INCOME);
            }
            case 2 -> {
                return getAndFilteringByType(walletId, TransactionTypes.EXPENSE);
            }
        }
        return List.of();
    }

    private List<Transaction> getAndFilteringByCategory(Integer walletId, CategoriesTypes category) throws IOException {

        return fileStorageService.download(walletId)
                .stream()
                .filter(transaction -> transaction.getCategory() == category)
                .toList();
    }

    private List<Transaction> getAndFilteringByMonth(Integer walletId, Integer month) throws IOException {

        return fileStorageService.download(walletId)
                .stream()
                .filter(transaction -> transaction.getCreated().toLocalDateTime().getMonthValue() == month)
                .toList();
    }

    private List<Transaction> getAndFilteringByType(Integer walletId, TransactionTypes type) throws IOException {

        return fileStorageService.download(walletId)
                .stream()
                .filter(transaction -> transaction.getType() == type)
                .toList();
    }
}