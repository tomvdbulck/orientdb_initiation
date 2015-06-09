import com.orientechnologies.orient.client.remote.OServerAdmin;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collection;

/**
 * Created by ToVn on 01/06/15.
 */

@RunWith(JUnit4.class)
public class EnvironmentTest {


    private static final String DB_DIR = "/POCTest";


    private OServerAdmin serverAdmin;

    @Before
    public void setup() throws  Exception{
        serverAdmin = new OServerAdmin("remote:localhost:2424").connect("root", "ordina");
    }

    @Test
    public void verifyEnvironmentSetup() throws Exception{
        //a database will be created automatically when you run local or in memory, however via remote this is not possible - security
        System.out.println("going to create database if not exists");

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


            int size = 0;
            for (Edge edge : peter.getEdges(Direction.OUT, "created")) {
                size++;
            }
            Assert.assertEquals(1, size);

        } finally {
            System.out.println("going to close connection");
            // this also closes the OrientGraph instances created by the factory
            // Note that OrientGraphFactory does not implement Closeable
            factory.close();

        }

        System.out.println("test finished");
    }



    @After
    public void cleanup() throws  Exception{
        try {
            serverAdmin = new OServerAdmin("remote:localhost:2424/POCTest").connect("root", "ordina");
            Assert.assertTrue(serverAdmin.existsDatabase());


            serverAdmin.dropDatabase("POCTest");

           Assert.assertFalse(serverAdmin.existsDatabase());
        } finally {
            serverAdmin.close();
        }


    }
}
