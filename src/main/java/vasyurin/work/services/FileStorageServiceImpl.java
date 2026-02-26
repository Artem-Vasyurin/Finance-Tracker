package vasyurin.work.services;

import vasyurin.work.entities.Transaction;
import vasyurin.work.entities.Wallet;
import vasyurin.work.repositories.FileStorageRepository;
import vasyurin.work.repositories.FileStorageRepositoryImpl;
import vasyurin.work.services.interfaces.FileStorageService;

import java.io.IOException;
import java.util.List;

public class FileStorageServiceImpl implements FileStorageService {

    private final FileStorageRepository fileStorageRepository = new FileStorageRepositoryImpl();

    @Override
    public void save(Wallet wallet) throws IOException {
        fileStorageRepository.save(wallet.getTransactions());
    }

    @Override
    public List<Transaction> download(Integer walletId) throws IOException {
        return fileStorageRepository.download(walletId);
    }

}
