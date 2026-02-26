package vasyurin.work.repositories;

import vasyurin.work.entities.Transaction;
import vasyurin.work.enums.CategoriesTypes;
import vasyurin.work.enums.TransactionTypes;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FileStorageRepositoryImpl implements FileStorageRepository {
    @Override
    public void save(List<Transaction> transactions) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("transactions/transactionsInWalletId" + transactions.get(0).getWalletId() + ".txt"))) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.toString());
                writer.newLine();
            }
        }

    }

    @Override
    public List<Transaction> download(Integer walletId) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("transactions/transactionsInWalletId" + walletId + ".txt"));
        String line;
        while ((line = reader.readLine()) != null) {

            line = line.replace("Transaction{", "").replace("}", "");
            String[] elements = line.split(", ");

            int transactionId = 0;
            int filleWalletId = 0;
            float amount = 0;
            TransactionTypes transactionType = null;
            CategoriesTypes category = CategoriesTypes.OTHER;
            Timestamp created = null;

            for (String element : elements) {
                String[] values = element.split("=");

                switch (values[0]) {
                    case "id" -> transactionId = Integer.parseInt(values[1]);
                    case "walletId" -> filleWalletId = Integer.parseInt(values[1]);
                    case "amount" -> amount = Float.parseFloat(values[1]);
                    case "type" -> transactionType = TransactionTypes.valueOf(values[1]);
                    case "category" -> category = CategoriesTypes.valueOf(values[1]);
                    case "created" -> created = Timestamp.valueOf(values[1]);
                }
            }
            if (filleWalletId == walletId) {
                Transaction transaction = new Transaction(transactionId, filleWalletId, amount, transactionType, category, created);
                transactions.add(transaction);
            }
        }
        return transactions;
    }
}
