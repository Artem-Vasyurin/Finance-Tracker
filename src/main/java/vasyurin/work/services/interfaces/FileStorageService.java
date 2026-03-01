package vasyurin.work.services.interfaces;

import vasyurin.work.entities.Transaction;
import vasyurin.work.entities.Wallet;

import java.io.IOException;
import java.util.List;

public interface FileStorageService {

    void save(Wallet wallet) throws IOException;

    List<Transaction> download(Integer walletId) throws IOException;

}