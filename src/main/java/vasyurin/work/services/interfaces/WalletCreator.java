package vasyurin.work.services.interfaces;

import vasyurin.work.entities.Wallet;

import java.util.List;

public interface WalletCreator {
    Wallet createWallet(List<Wallet> wallets);
}
