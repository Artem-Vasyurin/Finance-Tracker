package vasyurin.work.repositories;

import vasyurin.work.entities.Transaction;

import java.io.IOException;
import java.util.List;

public interface FileStorageRepository {

    void save(List<Transaction> transactions) throws IOException;
    List<Transaction> download(Integer walletId) throws IOException;
}
