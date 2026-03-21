package vasyurin.work.services;

import vasyurin.work.entities.Transaction;
import vasyurin.work.entities.Wallet;
import vasyurin.work.services.interfaces.FileStorageService;
import vasyurin.work.services.interfaces.WalletCreator;

import java.io.IOException;
import java.util.List;


public class WalletCreatorImpl implements WalletCreator {

    private static final FileStorageService fileStorageService = new FileStorageServiceImpl();

    @Override
    public Wallet createWallet(List<Wallet> wallets) {
        Wallet wallet = new Wallet(wallets.size() + 1);

        try {
            List<Transaction> transactions = fileStorageService.download(wallet);
            if (transactions.isEmpty()) {
                return wallet;
            } else {
                wallet.clearTransactions();
                wallet.addTransactions(transactions);
                return wallet;
            }
        } catch (IOException e) {
            return wallet;
        }
    }
}