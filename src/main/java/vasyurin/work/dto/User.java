package vasyurin.work.dto;

import java.util.ArrayList;
import java.util.List;

public class User {

    private List<Wallet> wallets = new ArrayList<Wallet>();
    private final String name;

    public User(String name) {
        this.name = name;
    }

    public void CreateWallet() {
        Wallet wallet = new Wallet(wallets.size() + 1);
        wallets.add(wallet);
    }

    public String getName() {
        return name;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

}
