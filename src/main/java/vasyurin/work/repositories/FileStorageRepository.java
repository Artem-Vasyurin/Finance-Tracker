package vasyurin.work.repositories;

import vasyurin.work.entities.Transaction;
import vasyurin.work.entities.Wallet;

import java.io.IOException;
import java.util.List;

public interface FileStorageRepository {

    void save(List<Transaction> transactions) throws IOException;
    List<Transaction> download(Wallet wallet) throws IOException;
}
