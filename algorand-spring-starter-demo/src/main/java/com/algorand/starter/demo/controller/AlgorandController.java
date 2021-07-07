package com.algorand.starter.demo.controller;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.algod.client.api.AlgodApi;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.model.NodeStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AlgorandController {

    @Autowired
    private Account account;
    @Autowired
    private AlgodClient client;

    private AlgodApi api;

    @GetMapping(path="/account", produces = "application/json; charset=UTF-8")
    public String account() throws Exception {

        String[] headers = {"X-API-Key"};
        String[] values = {"OVKYBexVj38K5jEMKTwnk45Cg4hVbzeD1RL8zG60"};

        NodeStatusResponse status = null;

        try {
            status = client.GetStatus().execute(headers, values).body();
        } catch (Exception e) {
            System.err.print("Failed to get algod status: " + e.getMessage());
        }

        return client.AccountInformation(account.getAddress()).execute(headers, values).body().toString();
    }

    /*@GetMapping("/account/mnemonic")
    public String amount(@RequestParam String mnemonic) throws GeneralSecurityException, ApiException, JsonProcessingException {
        Account account = new Account(mnemonic);

        ApiClient client = new ApiClient();
        client.setBasePath("https://testnet-algorand.api.purestake.io/ps2");
        ApiKeyAuth api_key = (ApiKeyAuth) client.getAuthentication("api_key");
        api_key.setApiKey("OVKYBexVj38K5jEMKTwnk45Cg4hVbzeD1RL8zG60");
        AlgodApi api = new AlgodApi(client);

        TransactionParams params = api.transactionParams();
        BigInteger suggestedFee = api.suggestedFee().getFee();
        BigInteger firstRound = params.getLastRound();
        BigInteger lastRound = firstRound.add(BigInteger.valueOf(1000));
        Digest genHash = new Digest(params.getGenesishashb64());

        Transaction tx1 = Transaction.PaymentTransactionBuilder()
                .sender(account.getAddress())
                .receiver("GD64YIY3TWGDMCNPP553DZPPR6LDUSFQOIJVFDPPXWEG3FVOJCCDBBHU5A")
                .fee(suggestedFee)
                .amount(10000)
                .firstValid(firstRound)
                .lastValid(lastRound)
                .genesisHash(genHash)
                .build();

        SignedTransaction signedTx = account.signTransaction(tx1);;
        // Submit the transaction
        try {
            byte[] encodedTxBytes = Encoder.encodeToMsgPack(signedTx);
            TransactionID id = api.rawTransaction(encodedTxBytes);
            System.out.println("Successfully sent tx group with first tx id: " + id);
        } catch (ApiException e) {
            // This is generally expected, but should give us an informative error message.
            System.err.println("Exception when calling algod#rawTransaction: " + e.getResponseBody());
        }

        return "Acct Address: " + account.getAddress();
    }*/
}
