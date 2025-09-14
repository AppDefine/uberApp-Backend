package com.appdefine.uber.uberApp.services.impl;

import com.appdefine.uber.uberApp.entities.WalletTransaction;
import com.appdefine.uber.uberApp.repositories.WalletTransactionRepository;
import com.appdefine.uber.uberApp.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;

    @Override
    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
        walletTransactionRepository.save(walletTransaction);
    }
}
