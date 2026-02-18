package vasyurin.work.utility;

import vasyurin.work.entities.Transaction;
import vasyurin.work.entities.Wallet;
import vasyurin.work.enums.TransactionType;
import vasyurin.work.exeption.NoMoneyException;
import vasyurin.work.services.FileStorageServiceImpl;
import vasyurin.work.services.InputInConsoleValidatorImpl;
import vasyurin.work.services.interfaces.FileStorageService;
import vasyurin.work.services.interfaces.InputInConsoleValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ConsoleUtility {

    private static final ConsoleUtility instance = new ConsoleUtility();
    private static final Scanner scanner = new Scanner(System.in);
    private static final InputInConsoleValidator validator = new InputInConsoleValidatorImpl();
    private static final FileStorageService fileStorageService = new FileStorageServiceImpl();
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
                    4. Создать кошелёк
                    5. Сохранить в файл транзакции
                    6. Просмотреть список транзакций
                    0. Выход
                    """));

            String input = scanner.nextLine();
            int choice = Integer.parseInt(input);

            switch (choice) {
                case 1 -> performTransfer();
                case 2 -> viewBalance();
                case 4 -> createWallet();
                case 5 -> save();
                case 6 -> showTransaction();
                case 0 -> Thread.currentThread().interrupt();
            }
        }
    }

    private void beginTransaction(Wallet senderWallet, Wallet receiverWallet, int sumTransaction) {
        Random random = new Random();
        Transaction income = new Transaction(random.nextInt(0, 1000000000), receiverWallet.getId(), sumTransaction, TransactionType.INCOME);
        Transaction expense = new Transaction(random.nextInt(0, 1000000000), senderWallet.getId(), sumTransaction, TransactionType.EXPENSE);

        if (senderWallet.getBalance() >= sumTransaction) {

            senderWallet.addTransaction(expense);
            receiverWallet.addTransaction(income);
        } else {
            throw new NoMoneyException("Не достаточно средств");
        }

        System.out.println("Транзакция успешно проведена");
    }

    private int getWallet(List<Wallet> wallets) {
        while (true) {
            int id = 1;
            for (Wallet wallet : wallets) {

                System.out.println(id++ + ". Баланс: " + wallet.getBalance());
            }
            System.out.println(0 + ". Назад");
            String choice = scanner.nextLine();

            if (validator.validate(choice)) {
                return Integer.parseInt(choice);
            } else {
                System.out.println("Введите корректное число");
            }
        }
    }

    private void showTransaction() {
        while (true) {
            System.out.println("Выберите кошелёк список транзакций которого вы хотите просмотреть");
            int walletChoice = getWallet(wallets);
            if (walletChoice == 0) break;
            try {
                fileStorageService.download(walletChoice).stream().forEach(System.out::println);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return;
        }
    }

    private void save() {
        while (true) {
            System.out.println("Выберите кошелёк для сохранения его списка транзакций");
            int walletChoice = getWallet(wallets);
            if (walletChoice == 0) break;
            try {
                fileStorageService.save(wallets.get(walletChoice - 1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
    }

    private void viewBalance() {

        while (true) {
            System.out.println("Выберите кошелёк");
            int walletChoice = getWallet(wallets);
            if (walletChoice == 0) break;
            System.out.println(wallets.get(walletChoice - 1).getBalance());
            return;
        }
    }

    private void performTransfer() {
        Wallet senderWallet, receiverWallet;

        while (true) {
            System.out.println("Выберите кошелёк списания ");

            int choice = getWallet(wallets);
            if (choice == 0) break;
            senderWallet = wallets.get(choice - 1);

            while (true) {

                System.out.println("Выберите кошелёк пополнения ");

                choice = getWallet(wallets);

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
