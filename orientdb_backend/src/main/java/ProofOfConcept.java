import com.orientechnologies.orient.client.remote.OServerAdmin;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import java.io.IOException;

/**
 * Created by ToVn on 22/05/15.
 */
public class ProofOfConcept {

    private static final String DB_DIR = "/POCTest";

    public static void main(String... args) throws IOException {
        // start with a non existing database


        //a database will be created automatically when you run local or in memory, however via remote this is not possible - security
        System.out.println("going to create database if not exists");
        final OServerAdmin serverAdmin = new OServerAdmin("remote:localhost:2424").connect("root", "ordina");
        if (!serverAdmin.listDatabases().containsKey("POCTest")) {
            serverAdmin.createDatabase("POCTest", "graph", "plocal");
            System.out.println("created database");
        }



        System.out.println("connect to database");
        final OrientGraphFactory factory = new OrientGraphFactory(
                "remote:localhost:2424" + DB_DIR, "admin", "admin");
        try {



            System.out.println("going to add some data");
            final OrientGraphNoTx g = factory.getNoTx();
            g.getVertexType("woliewolie");

            if (g.getVertexType("person") == null) {
                g.createVertexType("person");
            }
            if (g.getVertexType("microservice") == null) {
                g.createVertexType("microservice");
            }

            // database is now auto created

            Vertex marko = g.addVertex("class:person", "name", "marko", "age", 29);
            Vertex vadas = g.addVertex("class:person", "name", "vadas", "age", 27);
            Vertex lop = g.addVertex("class:microservice", "name", "lop", "lang", "java");
            Vertex josh = g.addVertex("class:person", "name", "josh", "age", 32);
            Vertex ripple = g.addVertex("class:microservice", "name", "ripple", "lang", "java");
            Vertex peter = g.addVertex("class:person", "name", "peter", "age", 35);

            marko.addEdge("knows", vadas).setProperty("weight", 0.5f);
            marko.addEdge("knows", josh).setProperty("weight", 1.0f);
            marko.addEdge("created", lop).setProperty("weight", 0.4f);
            josh.addEdge("created", ripple).setProperty("weight", 1.0f);
            josh.addEdge("created", lop).setProperty("weight", 0.4f);
            peter.addEdge("created", lop).setProperty("weight", 0.2f);
        } finally {
            System.out.println("going to close connection");
            // this also closes the OrientGraph instances created by the factory
            // Note that OrientGraphFactory does not implement Closeable
            factory.close();

        }

        System.out.println("test finished");
    }



}
