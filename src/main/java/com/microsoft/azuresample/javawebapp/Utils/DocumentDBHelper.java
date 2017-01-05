package com.microsoft.azuresample.javawebapp.Utils;

import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.DocumentClient;

import java.util.Map;

/**
 * Created by vazvadsk on 2017-01-04.
 */
public class DocumentDBHelper {
    // TODO: provide valid URL address to DocumentDB database
    private static String HOST = "https://<documentdb-name>.documents.azure.com:443/";
    // TODO: provide valid primary key for DocumentDB database
    private static String MASTER_KEY = "<documentdb-key>";

    private static DocumentClient documentClient;

    public static DocumentClient getDocumentClient() {
        if (documentClient == null) {
            init();
            documentClient = new DocumentClient(HOST, MASTER_KEY,
                    ConnectionPolicy.GetDefault(), ConsistencyLevel.Session);
        }

        return documentClient;
    }

    private static void init(){
        Map<String, String> env = System.getenv();
        HOST = env.get("DOCDB_HOST");
        MASTER_KEY = env.get("DOCDB_KEY");

        System.out.println("### INIT of DocumentDBHelper called.");
    }
}
