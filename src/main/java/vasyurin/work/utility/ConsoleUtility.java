package vasyurin.work.utility;

import vasyurin.work.entities.Transaction;
import vasyurin.work.entities.Wallet;
import vasyurin.work.exeption.NoMoneyException;
import vasyurin.work.kafka.KafkaBatchSchedulerService;
import vasyurin.work.services.*;
import vasyurin.work.services.interfaces.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUtility {

    private static final ConsoleUtility instance = new ConsoleUtility();
    private static final Scanner scanner = new Scanner(System.in);
    private static final InputInConsoleValidator validator = new InputInConsoleValidatorImpl();
    private static final KafkaBatchSchedulerService kafkaBatchSchedulerService = new KafkaBatchSchedulerService();
    private static final TransactionService transactionService = new TransactionServiceImpl(kafkaBatchSchedulerService);
    private static final FileStorageService fileStorageService = new FileStorageServiceImpl();
    private static final FilterService filterService = new FilterServiceImpl();
    private static final ReportService reportService = new ReportServiceImpl();
    private static final WalletCreator walletCreator = new WalletCreatorImpl();
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
                case 3 -> wallets.add(walletCreator.createWallet(wallets));
                case 4 -> showTransaction();
                case 5 -> save();
                case 0 -> Thread.currentThread().interrupt();
            }
        }
    }

    private Wallet getWallet(String message) {
        while (true) {
            System.out.println(message);
            int id = 1;
            for (Wallet wallet : wallets) {
                System.out.println(id++ + ". Баланс: " + wallet.getBalance());
            }

            System.out.println(0 + ". Назад");
            String choice = scanner.nextLine();

            if (choice.equals("0")) return null;

            if (validator.validate(choice)) {
                if (Integer.parseInt(choice) <= ConsoleUtility.wallets.size() && Integer.parseInt(choice) >= 0) {
                    return wallets.get(Integer.parseInt(choice) - 1);
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
            Wallet wallet = getWallet("Выберите кошелёк список транзакций которого вы хотите просмотреть");
            if (wallet == null) break;

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
                        reportService.printTransactionsTable(fileStorageService.download(wallet));
                    } catch (IOException e) {
                        System.out.println("Ошибка, данные не загрузились, попробуйте снова позже");
                    }
                    return;
                }
                case 2 -> {
                    try {
                        filteringTransactionsByCategory(wallet);
                    } catch (IOException e) {
                        System.out.println("Ошибка, данные не загрузились, попробуйте снова позже");
                    }
                }
                case 3 -> {
                    try {
                        filteringTransactionsByMonth(wallet);
                    } catch (IOException e) {
                        System.out.println("Ошибка, данные не загрузились, попробуйте снова позже");
                    }
                }
                case 4 -> {
                    try {
                        filteringTransactionsByType(wallet);
                    } catch (IOException e) {
                        System.out.println("Ошибка, данные не загрузились, попробуйте снова позже");
                    }
                }
                case 0 -> {
                    return;
                }
            }
        }
    }

    private void filteringTransactionsByCategory(Wallet wallet) throws IOException {
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

            List<Transaction> transactions = filterService.filteringTransactionsByCategory(wallet, choice);
            PrintTransactions(transactions);
        }
    }

    private void filteringTransactionsByMonth(Wallet wallet) throws IOException {
        while (true) {
            System.out.println("Напишите месяц по которому надо отфильтровать или 0 чтобы вернуться назад");

            int month = Integer.parseInt(scanner.nextLine());
            if (month == 0) return;
            if (month < 1 || month > 12) {
                System.out.println("Введите корректное число месяца");
                continue;
            }

            List<Transaction> transactions = filterService.filteringTransactionsByMonth(wallet, month);
            PrintTransactions(transactions);
        }
    }

    private void filteringTransactionsByType(Wallet wallet) throws IOException {
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

            List<Transaction> transactions = filterService.filteringTransactionsByType(wallet, choice);
            PrintTransactions(transactions);
        }
    }

    private void PrintTransactions(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("Таких транзакций нет :( ");
        } else {
            reportService.printTransactionsTable(transactions);
            System.out.println("Сумма за все выбранные транзакции = " + transactionService.getSumOfAllTransaction(transactions));
        }
    }

    private void save() {
        while (true) {
            Wallet walletChoice = getWallet("Выберите кошелёк для сохранения его списка транзакций");
            if (walletChoice == null) break;

            try {
                fileStorageService.save(walletChoice);
            } catch (IOException e) {
                System.out.println("Ошибка, данные не cохранились, попробуйте снова позже");
            }
        }
    }

    private void viewBalance() {
        while (true) {
            Wallet walletChoice = getWallet("Выберите кошелёк");
            if (walletChoice == null) break;
            System.out.println(walletChoice.getBalance());
        }
    }

    private void performTransfer() {
        Wallet senderWallet, receiverWallet;

        while (true) {
            senderWallet = getWallet("Выберите кошелёк списания ");
            if (senderWallet == null) break;

            while (true) {
                receiverWallet = getWallet("Выберите кошелёк пополнения ");
                if (receiverWallet == null) break;

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
                        transactionService.createTransaction(senderWallet, receiverWallet, sumTransaction);
                        fileStorageService.save(senderWallet);
                        fileStorageService.save(receiverWallet);
                    } catch (NoMoneyException e) {
                        System.out.println(e.getMessage());
                        continue;
                    } catch (IOException e) {
                        System.out.println("Ошибка, данные не cохранились, попробуйте снова позже");
                    }
                    return;
                }
            }
        }
    }
}