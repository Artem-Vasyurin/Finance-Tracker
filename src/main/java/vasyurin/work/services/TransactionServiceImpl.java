package vasyurin.work.services;

import vasyurin.work.entities.MaxId;
import vasyurin.work.entities.Transaction;
import vasyurin.work.entities.Wallet;
import vasyurin.work.enums.TransactionTypes;
import vasyurin.work.exeption.NoMoneyException;
import vasyurin.work.services.interfaces.MaxIdService;
import vasyurin.work.services.interfaces.TransactionService;

import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    private static final MaxIdService maxIdService = new MaxIdServiceImpl();

    @Override
    public void createTransaction(Wallet senderWallet, Wallet receiverWallet, int sumTransaction) {
        MaxId maxId = maxIdService.getMaxId(senderWallet, receiverWallet);

        Transaction income = new Transaction(maxId.maxRID() + 1, receiverWallet.getId(), sumTransaction, TransactionTypes.INCOME);
        Transaction expense = new Transaction(maxId.maxSID() + 1, senderWallet.getId(), sumTransaction, TransactionTypes.EXPENSE);

        if (senderWallet.getBalance() >= sumTransaction) {

            senderWallet.addTransaction(expense);
            receiverWallet.addTransaction(income);
        } else {
            throw new NoMoneyException("Недостаточно средств");
        }
        System.out.println("Транзакция успешно проведена");
    }

    @Override
    public Float getSumOfAllTransaction(List<Transaction> transactions) {
        float summ = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getType().equals(TransactionTypes.INCOME)) {
                summ += transaction.getAmount();
            } else if (transaction.getType().equals(TransactionTypes.EXPENSE)) {
                summ -= transaction.getAmount();
            }
        }
        return summ;
    }
}
