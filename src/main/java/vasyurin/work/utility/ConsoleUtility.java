package vasyurin.work.utility;

import vasyurin.work.Exeption.NoMoneyException;
import vasyurin.work.dto.Transaction;
import vasyurin.work.enums.TransactionType;
import vasyurin.work.dto.Wallet;
import vasyurin.work.dto.User;
import vasyurin.work.services.InputInConsoleValidatorImpl;
import vasyurin.work.services.interfaces.InputInConsoleValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ConsoleUtility {

    private static final ConsoleUtility instance = new ConsoleUtility();
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<User> users = new ArrayList<User>();
    private static final InputInConsoleValidator validator = new InputInConsoleValidatorImpl();

    private ConsoleUtility() {

    }

    public static ConsoleUtility getInstance() {
        return instance;
    }

    private static int getWallet(User user) {
        while (true){
            int id = 1;
            for (Wallet wallet : user.getWallets()) {

                System.out.println(id++ + ". Баланс: " + wallet.getBalance());

            }
            System.out.println(0 + ". Назад");
            String choice = scanner.nextLine();

            if (validator.validate(choice)){
                return Integer.parseInt(choice);
            }else {
                System.out.println("Введите корректное число");
            }
        }
    }

    private static int getUser(List<User> users) {

        int id = 1;
        for (User user : users) {

            System.out.println(id++ + ". " + user.getName());

        }
        System.out.println(0 + ". Назад");

        while (true){
            String choice = scanner.nextLine();

            if (validator.validate(choice)){
                return Integer.parseInt(choice);
            }else {
                System.out.println("Введите корректное число");
            }
        }
    }

    private static boolean cheсkNoUsers(List<User> users) {
        if (users.isEmpty()) {
            System.out.println("Нет пользователей");
            return true;
        }
        return false;
    }

    public void start() {
        System.out.println("Добро пожаловать! ");

        while (!Thread.currentThread().isInterrupted()) {
            System.out.println(("""
                    1. Перевести деньги
                    2. Просмотреть баланс
                    3. Создать пользователя
                    4. Создать кошелёк
                    5. Просмотреть пользователей
                    0. Выход
                    """));

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> performTransfer(users);
                case 2 -> viewBalance(users);
                case 3 -> createUser();
                case 4 -> createWallet(users);
                case 5 -> viewUsers();
                case 0 -> Thread.currentThread().interrupt();
            }
        }
    }

    private void viewBalance(List<User> users) {
        if (cheсkNoUsers(users)) return;
        while (true) {
            System.out.println("Выберите пользователя, чей баланс хотите посмотреть");

            int choise = getUser(users);
            if (choise == 0) return;
            while (true) {
                System.out.println("Выберите кошелёк");
                int walletChoice = getWallet(users.get(choise - 1));
                if (walletChoice == 0) break;
                System.out.println(users.get(choise - 1).getWallets().get(walletChoice - 1).getBalance());
                return;
            }

        }

    }

    private void performTransfer(List<User> users) {
        if (cheсkNoUsers(users)) return;

        User sender, receiver;
        Wallet senderWallet, receiverWallet;

        while (true) {
            System.out.println("Выберете кто переводит деньги");

            int choice = getUser(users);
            if (choice == 0) return;
            sender = users.get(choice - 1);

            while (true) {
                System.out.println("Выберите кошелёк списания ");

                choice = getWallet(sender);
                if (choice == 0) break;
                senderWallet = sender.getWallets().get(choice - 1);

                while (true) {
                    System.out.println("Выберете кому поступят деньги");

                    choice = getUser(users);
                    if (choice == 0) break;
                    receiver = users.get(choice - 1);

                    while (true) {

                        System.out.println("Выберите кошелёк пополнения ");

                        choice = getWallet(receiver);

                        if (choice == 0) break;
                        receiverWallet = receiver.getWallets().get(choice - 1);

                        if (sender.equals(receiver) && (senderWallet.equals(receiverWallet))) {

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
                                beginTransaction(sender, receiver, senderWallet, receiverWallet, sumTransaction);
                            } catch (NoMoneyException e) {
                                System.out.println(e.getMessage());
                                continue;
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    private static void beginTransaction(User Sender, User Receiver, Wallet SenderWallet, Wallet ReceiverWallet, int sumTransaction) {
        Random random = new Random();
        Transaction transaction = new Transaction(random.nextInt());
        transaction.setSender(Sender);
        transaction.setReceiver(Receiver);
        transaction.setWalletIdSend(SenderWallet.getId());
        transaction.setWalletIdReceive(ReceiverWallet.getId());

        if (SenderWallet.getBalance() >= sumTransaction) {
            SenderWallet.setBalance(SenderWallet.getBalance() - sumTransaction);
            ReceiverWallet.setBalance(ReceiverWallet.getBalance() + sumTransaction);
        } else {
            throw new NoMoneyException("Не достаточно средств");
        }

        transaction.setType(TransactionType.EXPENSE);
        SenderWallet.getTransactions().add(transaction);

        transaction.setType(TransactionType.INCOME);
        ReceiverWallet.getTransactions().add(transaction);
        System.out.println("Транзакция успешно проведена");
    }

    private void createUser() {
        System.out.println("Введите имя пользователя ");
        String userName = scanner.next();
        User user = new User(userName);
        user.CreateWallet();
        users.add(user);
    }

    private void createWallet(List<User> users) {
        System.out.println("Кому создать кошелёк? ");
        int choise = getUser(users);
        if (choise == 0) return;
        users.get(choise - 1).CreateWallet();
        System.out.println("Кошёлёк " + users.get(choise - 1).getWallets().size() + " у " + users.get(choise - 1) + " Создан");
    }

    private void viewUsers() {
        if (cheсkNoUsers(users)) return;
        users.stream().map(User::getName).forEach(System.out::println);
    }
}
