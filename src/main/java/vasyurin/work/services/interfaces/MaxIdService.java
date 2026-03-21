package vasyurin.work.services.interfaces;

import vasyurin.work.entities.MaxId;
import vasyurin.work.entities.Wallet;

public interface MaxIdService {
    MaxId getMaxId(Wallet senderWallet, Wallet receiverWallet);
}