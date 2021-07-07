package com.algorand.starter.demo.controller;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.algod.client.api.AlgodApi;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.IndexerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
public class AlgorandController {

    @Autowired
    private Account account;
    @Autowired
    private AlgodClient client;
    @Autowired
    private IndexerClient indexer;

    private AlgodApi api;
    String[] headers = {"X-API-Key"};
    String[] values = {"OVKYBexVj38K5jEMKTwnk45Cg4hVbzeD1RL8zG60"};

    @GetMapping(path="/account", produces = "application/json; charset=UTF-8")
    public String account() throws Exception {
        return client.AccountInformation(account.getAddress()).execute(headers, values).body().toString();
    }

    @GetMapping(path="/asset", produces = "application/json; charset=UTF-8")
    public String asset(@RequestParam Long assetId) throws Exception {
        return client.GetAssetByID(assetId).execute(headers, values).body().toString();
    }

    @GetMapping(path="/pendingTransactions", produces = "application/json; charset=UTF-8")
    public String pendingTransactions() throws Exception {
        return client.GetPendingTransactionsByAddress(account.getAddress()).execute(headers, values).toString();
    }

    @GetMapping(path="/assetSearch", produces = "application/json; charset=UTF-8")
    public String assetSearch(@RequestParam String assetName) throws Exception {
        return indexer.searchForAssets().name(assetName).limit(1L).execute(headers, values).toString();
    }

    @GetMapping(path = "/accountTransactionsBefore", produces = "application/json; charset=UTF-8")
    public String accountTransactionsBeforeDate(@RequestParam Date date) throws Exception {
        return indexer.lookupAccountTransactions(account.getAddress()).beforeTime(date).limit(1L).execute(headers, values).toString();
    }
}
