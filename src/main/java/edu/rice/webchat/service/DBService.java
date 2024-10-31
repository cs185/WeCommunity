package edu.rice.webchat.service;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.rice.webchat.entity.message.GroupMessage;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.*;

@Service
public class DBService {
    public String[] getGroupMessage(int group_id) {
        try (MongoClient mongoClient = new MongoClient( "localhost" , 27017 )){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("chatapp");
            MongoCollection<Document> collection = mongoDatabase.getCollection("chat_log");
            List<String> messages = new ArrayList<>();

            FindIterable<Document> documentCursor = collection.find(new Document("groupId", group_id))
                    .projection(new Document("message", 1).append("_id", 0).append("senderName", 1).append("type", 1))
                    .sort(new Document("date", 1));

            for (Document doc : documentCursor) {
                messages.add(doc.getString("senderName") + " says: " + doc.getString("message"));
            }

            return messages.toArray(new String[0]);

        } catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return null;
    }


    public static void addMsgToGroup(int group_id, String content, String senderName) {
        try (MongoClient mongoClient = new MongoClient( "localhost" , 27017 )){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("chatapp");
            MongoCollection<Document> collection = mongoDatabase.getCollection("chat_log");

            Document doc = new Document("date", new Date())
                    .append("groupId", group_id)
                    .append("senderName", senderName)
                    .append("content", content)
                    .append("type", "general");

            collection.insertOne(doc);

        } catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

}
