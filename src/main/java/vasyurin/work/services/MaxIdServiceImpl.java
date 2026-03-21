package vasyurin.work.services;

import vasyurin.work.entities.MaxId;
import vasyurin.work.entities.Transaction;
import vasyurin.work.entities.Wallet;
import vasyurin.work.services.interfaces.MaxIdService;


public class MaxIdServiceImpl implements MaxIdService {
    @Override
    public MaxId getMaxId(Wallet senderWallet, Wallet receiverWallet) {

        int maxReceiverID = 0;
        int maxSenderID = 0;

        for (Transaction transaction : receiverWallet.getTransactions()) {
            if (transaction.getId() > maxReceiverID) {
                maxReceiverID = transaction.getId();
            }
        }

        for (Transaction transaction : senderWallet.getTransactions()) {
            if (transaction.getId() > maxSenderID) {
                maxSenderID = transaction.getId();
            }
        }

        MaxId maxId = new MaxId(maxReceiverID, maxSenderID);

        return maxId;
    }
}