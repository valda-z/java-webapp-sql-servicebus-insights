package com.microsoft.azuresample.javawebapp.model;

import com.google.gson.Gson;
import com.microsoft.azure.documentdb.*;
import com.microsoft.azuresample.javawebapp.Utils.DocumentDBHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by vazvadsk on 2017-01-04.
 */
@Component
public class ToDoCommentDAO {

    // The name of our database.
    private static final String DATABASE_ID = "ToDoDB";

    // The name of our collection.
    private static final String COLLECTION_ID = "ToDoComments";

    // We'll use Gson for POJO <=> JSON serialization for this example.
    private static Gson gson = new Gson();

    // The DocumentDB Client
    private static DocumentClient documentClient = DocumentDBHelper.getDocumentClient();

    // Cache for the database object, so we don't have to query for it to
    // retrieve self links.
    private static Database databaseCache;

    // Cache for the collection object, so we don't have to query for it to
    // retrieve self links.
    private static DocumentCollection collectionCache;

    public ToDoComment create(ToDoComment todoItem) {

        //generate unique ID
        todoItem.setId(UUID.randomUUID().toString());

        // Serialize the TodoItem as a JSON Document.
        Document todoItemDocument = new Document(gson.toJson(todoItem));

        try {
            // Persist the document using the DocumentClient.
            todoItemDocument = documentClient.createDocument(
                    getTodoCollection().getSelfLink(), todoItemDocument, null,
                    false).getResource();
        } catch (DocumentClientException e) {
            e.printStackTrace();
            return null;
        }

        System.out.println("### Document in DocumentDB created: " + todoItemDocument.toString());

        return gson.fromJson(todoItemDocument.toString(), ToDoComment.class);
    }

    public List<ToDoComment> query(String todoGid) {
        List<ToDoComment> todoItems = new ArrayList<ToDoComment>();

        // Retrieve the TodoItem documents
        List<Document> documentList = documentClient
                .queryDocuments(getTodoCollection().getSelfLink(),
                        "SELECT * FROM root r WHERE r.todoGid = '" +
                                todoGid.replace('\'',' ')
                                + "'",
                        null).getQueryIterable().toList();

        // De-serialize the documents in to TodoItems.
        for (Document todoItemDocument : documentList) {
            todoItems.add(gson.fromJson(todoItemDocument.toString(),
                    ToDoComment.class));
        }

        return todoItems;
    }

    private Database getTodoDatabase() {
        if (databaseCache == null) {
            // Get the database if it exists
            List<Database> databaseList = documentClient
                    .queryDatabases(
                            "SELECT * FROM root r WHERE r.id='" + DATABASE_ID
                                    + "'", null).getQueryIterable().toList();

            if (databaseList.size() > 0) {
                // Cache the database object so we won't have to query for it
                // later to retrieve the selfLink.
                databaseCache = databaseList.get(0);
            } else {
                // Create the database if it doesn't exist.
                try {
                    Database databaseDefinition = new Database();
                    databaseDefinition.setId(DATABASE_ID);

                    databaseCache = documentClient.createDatabase(
                            databaseDefinition, null).getResource();
                } catch (DocumentClientException e) {
                    // Something has gone terribly wrong - the app wasn't
                    // able to query or create the collection.
                    // Verify your connection, endpoint, and key.
                    e.printStackTrace();
                }
            }
        }

        return databaseCache;
    }

    private DocumentCollection getTodoCollection() {
        if (collectionCache == null) {
            // Get the collection if it exists.
            List<DocumentCollection> collectionList = documentClient
                    .queryCollections(
                            getTodoDatabase().getSelfLink(),
                            "SELECT * FROM root r WHERE r.id='" + COLLECTION_ID
                                    + "'", null).getQueryIterable().toList();

            if (collectionList.size() > 0) {
                // Cache the collection object so we won't have to query for it
                // later to retrieve the selfLink.
                collectionCache = collectionList.get(0);
            } else {
                // Create the collection if it doesn't exist.
                try {
                    DocumentCollection collectionDefinition = new DocumentCollection();
                    collectionDefinition.setId(COLLECTION_ID);

                    collectionCache = documentClient.createCollection(
                            getTodoDatabase().getSelfLink(),
                            collectionDefinition, null).getResource();
                } catch (DocumentClientException e) {
                    // Something has gone terribly wrong - the app wasn't
                    // able to query or create the collection.
                    // Verify your connection, endpoint, and key.
                    e.printStackTrace();
                }
            }
        }

        return collectionCache;
    }
}
