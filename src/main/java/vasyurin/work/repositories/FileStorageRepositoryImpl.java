package vasyurin.work.repositories;

import vasyurin.work.entities.Transaction;
import vasyurin.work.enums.TransactionType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorageRepositoryImpl implements FileStorageRepository {
    @Override
    public void save(List<Transaction> transactions) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("transactionsInWalletId" + transactions.get(0).getWalletId() + ".txt"))) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.toString());
                writer.newLine();
            }
        }

    }


    @Override
    public List<Transaction> download(Integer walletId) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("transactionsInWalletId" + walletId + ".txt"));
        String line;
        while ((line = reader.readLine()) != null) {

            line = line.replace("Transaction{", "").replace("}", "");
            String[] elements = line.split(", ");

            int transactionId = 0;
            int filleWalletId = 0;
            float amount = 0;
            TransactionType transactionType = null;

            for (String element : elements) {
                String[] values = element.split("=");

                switch (values[0]) {
                    case "id" -> transactionId = Integer.parseInt(values[1]);
                    case "walletId" -> filleWalletId = Integer.parseInt(values[1]);
                    case "amount" -> amount = Float.parseFloat(values[1]);
                    case "type" -> transactionType = TransactionType.valueOf(values[1]);
                }
            }
            if (filleWalletId == walletId) {
                Transaction transaction = new Transaction(transactionId, filleWalletId, amount, transactionType);
                transactions.add(transaction);
            }

        }
        return transactions;
    }
}
