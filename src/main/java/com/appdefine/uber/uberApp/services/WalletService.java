package com.appdefine.uber.uberApp.services;

import com.appdefine.uber.uberApp.entities.Ride;
import com.appdefine.uber.uberApp.entities.User;
import com.appdefine.uber.uberApp.entities.Wallet;
import com.appdefine.uber.uberApp.entities.enums.TransactionMethod;

public interface WalletService {

    Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);

    Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);

    void withdrawAllMyMoneyFromWallet();

    Wallet findWalletById(Long walletId);

    Wallet createNewWallet(User user);

    Wallet findByUser(User user);

}
