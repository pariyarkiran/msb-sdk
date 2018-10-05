package com.lftechnology.msb.sdk.service.delivery;

import com.lftechnology.msb.sdk.dto.*;
import com.lftechnology.msb.sdk.mapper.VtnObjectMapper;
import com.lftechnology.vtn.api.BalanceApi;
import com.lftechnology.vtn.api.BankApi;
import com.lftechnology.vtn.api.FxRateApi;
import com.lftechnology.vtn.api.TransactionApi;
import com.lftechnology.vtn.dto.request.BankRequest;
import com.lftechnology.vtn.dto.request.TransactionRequest;
import com.lftechnology.vtn.dto.response.BalanceResponse;
import com.lftechnology.vtn.dto.response.BankResponse;
import com.lftechnology.vtn.dto.response.FxRateResponse;
import com.lftechnology.vtn.exception.ApiException;
import com.lftechnology.vtn.exception.VtnException;

import java.math.BigDecimal;

public class VtnDeliveryServiceImpl implements DeliveryService {

    @Override
    public TransactionResponse create(Transaction transaction, String credential) throws VtnException, ApiException {

        TransactionRequest transactionRequest = VtnObjectMapper.toTransaction(transaction);
        com.lftechnology.vtn.dto.request.Credential vtnCredential = VtnObjectMapper.toCredential(credential);
        TransactionApi transactionApi = new TransactionApi(vtnCredential);
        com.lftechnology.vtn.dto.response.TransactionResponse transactionResponse = transactionApi.createTransaction(transactionRequest);

        return VtnObjectMapper.toTransactionResponse(transactionResponse);
    }

    @Override
    public Bank verifyBank(Bank bank, String credential) throws VtnException, ApiException {
        BankRequest bankRequest = VtnObjectMapper.toBank(bank);
        com.lftechnology.vtn.dto.request.Credential vtnCredential = VtnObjectMapper.toCredential(credential);
        BankApi bankApi = new BankApi(vtnCredential);
        BankResponse bankResponse = bankApi.verifyBank(bankRequest);

        return VtnObjectMapper.toBankResponse(bankResponse);
    }

    @Override
    public ExchangeRateResponse rate(ExchangeRateRequest request, String credential) throws VtnException, ApiException {
        com.lftechnology.vtn.dto.request.Credential vtnCredential = VtnObjectMapper.toCredential(credential);
        FxRateApi fxRateApi = new FxRateApi(vtnCredential);
        FxRateResponse fxRateResponse = fxRateApi.getRate(request.getDestination().getCurrencyCode());

        return VtnObjectMapper.toExchangeRateResponse(fxRateResponse);
    }

    @Override
    public BigDecimal getBalance(String credential) throws VtnException, ApiException {
        com.lftechnology.vtn.dto.request.Credential vtnCredential = VtnObjectMapper.toCredential(credential);
        BalanceApi balanceApi = new BalanceApi(vtnCredential);
        BalanceResponse bankResponse = balanceApi.getBalance();

        return bankResponse.getBalance();
    }


}
