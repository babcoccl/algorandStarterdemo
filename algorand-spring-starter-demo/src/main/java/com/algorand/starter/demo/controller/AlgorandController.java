package com.algorand.starter.demo.controller;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.IndexerClient;
import com.algorand.algosdk.v2.client.model.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


@RestController
public class AlgorandController {

    @Autowired
    private Account account;
    @Autowired
    private AlgodClient client;
    @Autowired
    private IndexerClient indexer;

    String[] headers = {"X-API-Key"};
    String[] values = {"OVKYBexVj38K5jEMKTwnk45Cg4hVbzeD1RL8zG60"};

    /**
     * A method to retrieve account details as specified in the properties file
     * @return String representing account data
     * @throws Exception
     */
    @GetMapping(path="/account", produces = "application/json; charset=UTF-8")
    public String account() throws Exception {
        return client.AccountInformation(account.getAddress()).execute(headers, values).body().toString();
    }

    /**
     * A method to retrieve specific asset details from an account as specified in the properties file
     * @return String representing account data
     * @throws Exception
     */
    @GetMapping(path="/accountAssetSearch", produces = "application/json; charset=UTF-8")
    public String account(@RequestParam String assetName) throws Exception {
        Asset returnAsset = new Asset();
        List<Asset> createdAssets =
                client.AccountInformation(account.getAddress()).execute(headers, values).body().createdAssets;
        for (Asset asset : createdAssets) {
            System.out.println(asset.params.name);
            if(asset.params.name.equals(assetName)){
             returnAsset = asset;

            }
        }
        return returnAsset.toString();
    }

    /**
     * A method to retrieve asset data from the blockchain
     * @param assetId The asset ID of interest
     * @return String representing the asset datat
     * @throws Exception
     */
    @GetMapping(path="/asset", produces = "application/json; charset=UTF-8")
    public String asset(@RequestParam Long assetId) throws Exception {
        return client.GetAssetByID(assetId).execute(headers, values).body().toString();
    }

    /**
     * A method to retrieve pending transactions against the account configured in the properties file
     * @return String representing the pending transactions
     * @throws Exception
     */
    @GetMapping(path="/pendingTransactions", produces = "application/json; charset=UTF-8")
    public String pendingTransactions() throws Exception {
        return client.GetPendingTransactionsByAddress(account.getAddress()).execute(headers, values).toString();
    }

    /**
     * A method to search for an asset by name using the indexer
     * @param assetName The name of the asset to search for
     * @return String representing the asset details
     * @throws Exception
     */
    @GetMapping(path="/assetSearch", produces = "application/json; charset=UTF-8")
    public String assetSearch(@RequestParam String assetName) throws Exception {
        return indexer.searchForAssets().name(assetName).limit(1L).execute(headers, values).toString();
    }

    /**
     * A method to search for account transactions before a given date using the indexer
     * @param date the date of interest to search before
     * @return String representation of the account transactions
     * @throws Exception
     */
    @GetMapping(path = "/accountTransactionsBefore", produces = "application/json; charset=UTF-8")
    public String accountTransactionsBeforeDate(@RequestParam Date date) throws Exception {
        return indexer.lookupAccountTransactions(account.getAddress()).beforeTime(date).limit(1L).execute(
                headers, values).toString();
    }
}
