package vasyurin.work.dto;

import vasyurin.work.enums.TransactionType;

public class Transaction {

    private int walletIdSend, walletIdReceive;
    private final int id;
    private TransactionType type;
    private User sender, receiver;

    public Transaction(int id){
        this.id = id;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public Integer getWalletIdSend() {
        return walletIdSend;
    }

    public void setWalletIdSend(int walletIdSend) {
        this.walletIdSend = walletIdSend;
    }

    public Integer getWalletIdReceive() {
        return walletIdReceive;
    }

    public void setWalletIdReceive(int walletIdReceive) {
        this.walletIdReceive = walletIdReceive;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
