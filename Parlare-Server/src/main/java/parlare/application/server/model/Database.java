/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parlare.application.server.model;

//import 

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author jesusrodriguez
 */
public class Database {
    
//    private final static Locale currentLocale = new Locale("es");
//    private static final ResourceBundle settings = ResourceBundle.getBundle("settings");
//    private static final ResourceBundle lang = ResourceBundle.getBundle("lang", currentLocale);
    String server = "localhost";
    String user = "html5";
    String password = "*******";
    String source = "html5apps";
        
    public Database() {
        
        doClientMongo();
        
    }

    private void doConnectionMySQL () {
        
        String url = "jdbc:mysql://localhost:3306/sms_wap";
        String username = "wapsms";
        
        try ( Connection con = DriverManager.getConnection(url, username, password)) {
            
            PreparedStatement pStmt = con.prepareStatement("SELECT * FROM sms_wap_sites");
            
            ResultSet rs = pStmt.executeQuery();
            
            while ( rs.next() ) {
                
                int siteId = rs.getInt("siteId");
                String nameSite = rs.getString("name");
                String urlSite = rs.getString("url");
                boolean active = rs.getBoolean("active");
                
                System.out.format("Site ID:  %s%n"
                        + "Name: %s%n"
                        + "URL: %s%n"
                        + "Active: %s%n",
                        siteId, nameSite, urlSite, active);

            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }           
    
    }
    
    private void doConnectionMongo() {
        try {
            
            System.out.println("MONGO server: " + server);
            System.out.println("MONGO user: " + user);
            System.out.println("MONGO source: " + source);
            
            System.out.println();
            
            MongoClient mongoClient = new MongoClient(new ServerAddress(server), Arrays.asList(MongoCredential.createMongoCRCredential(user, source, password.toCharArray())),
                    new MongoClientOptions.Builder().build());
            
            DB testDB = mongoClient.getDB("html5apps");
            
            System.out.println("Count: " + testDB.getCollection("html5apps").count());
            
            System.out.println("Insert result: " + testDB.getCollection("html5apps").insert(new BasicDBObject()));
        } catch (UnknownHostException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    private String doClientMongo() {
        
        
        String print = "";
        
        System.out.println("User:" + user + " Source:" + source + " Password:" + password);
        
        try {
            
            
            // connect to the local database server
            MongoClient mongoClient = new MongoClient(new ServerAddress(server), Arrays.asList(MongoCredential.createMongoCRCredential(user, source, password.toCharArray())),
                    new MongoClientOptions.Builder().build());
            
            // get handle to "mydb"
            DB db = mongoClient.getDB("html5apps");
            
            // Authenticate - optional
            // boolean auth = db.authenticate("foo", "bar");
            
            // get a list of the collections in this database and print them out
            Set<String> collectionNames = db.getCollectionNames();
            for (String s : collectionNames) {
                
                System.out.println(s);
            }
            
            // get a collection object to work with
            DBCollection testCollection = db.getCollection("testCollection");

            // drop all the data in it
            testCollection.drop();

            // make a document and insert it
            BasicDBObject doc = new BasicDBObject("name", "MongoDB").append("type", "database").append("count", 1)
                    .append("info", new BasicDBObject("x", 203).append("y", 102));

            testCollection.insert(doc);

            // get it (since it's the only one in there since we dropped the rest earlier on)
            DBObject myDoc = testCollection.findOne();
            System.out.println(myDoc);

            // now, lets add lots of little documents to the collection so we can explore queries and cursors
            for (int i = 0; i < 100; i++) {
                testCollection.insert(new BasicDBObject().append("i", i));
            }
            System.out.println("total # of documents after inserting 100 small ones (should be 101) " + testCollection.getCount());

            //  lets get all the documents in the collection and print them out
            DBCursor cursor = testCollection.find();
            try {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next());
                }
            } finally {
                cursor.close();
            }

            //  now use a query to get 1 document out
            BasicDBObject query = new BasicDBObject("i", 71);
            cursor = testCollection.find(query);

            try {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next());
                }
            } finally {
                cursor.close();
            }

            //  now use a range query to get a larger subset
            query = new BasicDBObject("i", new BasicDBObject("$gt", 50));  // i.e. find all where i > 50
            cursor = testCollection.find(query);

            try {
                while (cursor.hasNext()) {
                    System.out.println("Cursor: " + cursor.next());
                }
            } finally {
                cursor.close();
            }

            // range query with multiple constraints
            query = new BasicDBObject("i", new BasicDBObject("$gt", 20).append("$lte", 30));  // i.e.   20 < i <= 30
            cursor = testCollection.find(query);

            try {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next());
                }
            } finally {
                cursor.close();
            }

            // create an index on the "i" field
            testCollection.createIndex(new BasicDBObject("i", 1));  // create index on "i", ascending

            //  list the indexes on the collection
            List<DBObject> list = testCollection.getIndexInfo();
            for (DBObject o : list) {
                System.out.println(o);
            }

            // See if the last operation had an error
            System.out.println("Last error : " + db.getLastError());

            // see if any previous operation had an error
            System.out.println("Previous error : " + db.getPreviousError());

            // force an error
            db.forceError();

            // See if the last operation had an error
            System.out.println("Last error : " + db.getLastError());

            db.resetError();

            // release resources
            mongoClient.close();
            
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return print;
    
    }
    
}
