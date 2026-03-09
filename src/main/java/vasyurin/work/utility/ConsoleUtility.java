package vasyurin.work.utility;

import vasyurin.work.entities.Transaction;
import vasyurin.work.entities.Wallet;
import vasyurin.work.enums.TransactionTypes;
import vasyurin.work.exeption.NoMoneyException;
import vasyurin.work.services.FileStorageServiceImpl;
import vasyurin.work.services.FilterServiceImpl;
import vasyurin.work.services.InputInConsoleValidatorImpl;
import vasyurin.work.services.ReportServiceImpl;
import vasyurin.work.services.interfaces.FileStorageService;
import vasyurin.work.services.interfaces.FilterService;
import vasyurin.work.services.interfaces.InputInConsoleValidator;
import vasyurin.work.services.interfaces.ReportService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUtility {

    private static final ConsoleUtility instance = new ConsoleUtility();
    private static final Scanner scanner = new Scanner(System.in);
    private static final InputInConsoleValidator validator = new InputInConsoleValidatorImpl();
    private static final FileStorageService fileStorageService = new FileStorageServiceImpl();
    private static final FilterService filterService = new FilterServiceImpl();
    private static final ReportService reportService = new ReportServiceImpl();
    private static final List<Wallet> wallets = new ArrayList<>();


    private ConsoleUtility() {
    }

    public static ConsoleUtility getInstance() {
        return instance;
    }

    public void start() {
        System.out.println("Добро пожаловать! ");

        while (!Thread.currentThread().isInterrupted()) {
            System.out.println(("""
                    1. Перевести деньги
                    2. Просмотреть баланс
                    3. Создать кошелёк
                    4. Просмотреть список транзакций
                    5. Сохранить в файл транзакции
                    0. Выход
                    """));

            String input = scanner.nextLine();
            int choice = Integer.parseInt(input);

            switch (choice) {
                case 1 -> performTransfer();
                case 2 -> viewBalance();
                case 3 -> createWallet();
                case 4 -> showTransaction();
                case 5 -> save();
                case 0 -> Thread.currentThread().interrupt();
            }
        }
    }

    private void beginTransaction(Wallet senderWallet, Wallet receiverWallet, int sumTransaction) {
        Transaction income = new Transaction(receiverWallet.getTransactions().size() + 1, receiverWallet.getId(), sumTransaction, TransactionTypes.INCOME);
        Transaction expense = new Transaction(senderWallet.getTransactions().size() + 1, senderWallet.getId(), sumTransaction, TransactionTypes.EXPENSE);

        if (senderWallet.getBalance() >= sumTransaction) {

            senderWallet.addTransaction(expense);
            receiverWallet.addTransaction(income);
        } else {
            throw new NoMoneyException("Не достаточно средств");
        }

        System.out.println("Транзакция успешно проведена");
    }

    private int getWallet() {
        while (true) {
            int id = 1;
            for (Wallet wallet : ConsoleUtility.wallets) {
                System.out.println(id++ + ". Баланс: " + wallet.getBalance());
            }

            System.out.println(0 + ". Назад");
            String choice = scanner.nextLine();

            if (validator.validate(choice)) {
                if (Integer.parseInt(choice) <= ConsoleUtility.wallets.size() && Integer.parseInt(choice) >= 0) {
                    return Integer.parseInt(choice);
                } else {
                    System.out.println("Выберете существующий кошелёк из списка");
                }
            } else {
                System.out.println("Введите корректное число");
            }
        }
    }

    private void showTransaction() {
        while (true) {
            System.out.println("Выберите кошелёк список транзакций которого вы хотите просмотреть");

            int walletChoice = getWallet();
            if (walletChoice == 0) break;

            System.out.println(("""
                    Выберите какие транзакции вы хотите просмотреть
                    1. Все транзакции
                    2. Отфильтровать по категории
                    3. Отфильтровать по месяцу
                    4. Отфильтровать по типу
                    0. Назад
                    """));

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> {
                    try {
                        reportService.printTransactionsTable(fileStorageService.download(walletChoice));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
                case 2 -> {
                    try {
                        filteringTransactionsByCategory(walletChoice);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 3 -> {
                    try {
                        filteringTransactionsByMonth(walletChoice);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 4 -> {
                    try {
                        filteringTransactionsByType(walletChoice);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 0 -> {
                    return;
                }
            }
        }
    }

    private void filteringTransactionsByCategory(Integer walletId) throws IOException {
        while (true) {
            System.out.println(("""
                    Выберете категорию по которой надо отфильтровать
                    1. Супермаркеты
                    2. Фастфуд
                    3. Одежда
                    4. Билеты
                    5. Прочее
                    0. Назад
                    """));

            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;

            if (choice < 1 || choice > 5) {
                System.out.println("Введите корректную категорию");
                continue;
            }

            List<Transaction> transactions = filterService.filteringTransactionsByCategory(walletId, choice);
            if (transactions.isEmpty()) {
                System.out.println("Таких транзакций нет :( ");

            } else {
                reportService.printTransactionsTable(transactions);
                System.out.println("Сумма за все выбранная транзакции = " + getSumm(transactions));
                break;
            }
        }
    }

    private void filteringTransactionsByMonth(Integer walletId) throws IOException {
        while (true) {
            System.out.println("Напишите месяц по которому надо отфильтровать или 0 чтобы вернуться назад");

            int month = Integer.parseInt(scanner.nextLine());
            if (month == 0) return;
            if (month < 1 || month > 12) {
                System.out.println("Введите корректное число месяца");
                continue;
            }

            List<Transaction> transactions = filterService.filteringTransactionsByMonth(walletId, month);
            if (transactions.isEmpty()) {
                System.out.println("Таких транзакций нет :( ");
            } else {
                reportService.printTransactionsTable(transactions);
                System.out.println("Сумма за все выбранная транзакции = " + getSumm(transactions));
                break;
            }
        }
    }

    private void filteringTransactionsByType(Integer walletId) throws IOException {
        while (true) {
            System.out.println(("""
                    Выберите тип транзакции по которому надо отфильтровать
                    1. Поступления
                    2. Расходы
                    0. Назад
                    """));

            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;

            if (choice < 1 || choice > 2) {
                System.out.println("Введите корректный тип транзакции");
                continue;
            }

            List<Transaction> transactions = filterService.filteringTransactionsByType(walletId, choice);
            if (transactions.isEmpty()) {
                System.out.println("Таких транзакций нет :( ");
            } else {
                reportService.printTransactionsTable(transactions);
                System.out.println("Сумма за все выбранная транзакции = " + getSumm(transactions));
                break;
            }
        }
    }

    private float getSumm(List<Transaction> transactions) {
        float summ = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getType().equals(TransactionTypes.INCOME)) {
                summ += transaction.getAmount();
            } else if (transaction.getType().equals(TransactionTypes.EXPENSE)) {
                summ -= transaction.getAmount();
            }
        }
        return summ;
    }

    private void save() {
        while (true) {
            System.out.println("Выберите кошелёк для сохранения его списка транзакций");
            int walletChoice = getWallet();
            if (walletChoice == 0) break;
            try {
                fileStorageService.save(wallets.get(walletChoice - 1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void viewBalance() {
        while (true) {
            System.out.println("Выберите кошелёк");
            int walletChoice = getWallet();
            if (walletChoice == 0) break;
            System.out.println(wallets.get(walletChoice - 1).getBalance());
        }
    }

    private void performTransfer() {
        Wallet senderWallet, receiverWallet;

        while (true) {
            System.out.println("Выберите кошелёк списания ");

            int choice = getWallet();
            if (choice == 0) break;
            senderWallet = wallets.get(choice - 1);

            while (true) {

                System.out.println("Выберите кошелёк пополнения ");

                choice = getWallet();

                if (choice == 0) break;

                receiverWallet = wallets.get(choice - 1);

                if ((senderWallet.equals(receiverWallet))) {

                    System.out.println("Нельзя перевести деньги на кошелёк отправления");
                    continue;
                }

                while (true) {
                    System.out.println("""
                            На какую сумму совершить перевод?
                            0. Назад
                            """);

                    String input = scanner.nextLine();

                    if (!validator.validate(input)) {
                        System.out.println("Введите корректное число");
                        continue;
                    }
                    int sumTransaction = Integer.parseInt(input);

                    if (sumTransaction == 0) break;

                    try {
                        beginTransaction(senderWallet, receiverWallet, sumTransaction);
                    } catch (NoMoneyException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                    try {
                        fileStorageService.save(senderWallet);
                        fileStorageService.save(receiverWallet);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
            }
        }
    }

    private void createWallet() {
        Wallet wallet = new Wallet(wallets.size() + 1);
        wallets.add(wallet);
    }
}