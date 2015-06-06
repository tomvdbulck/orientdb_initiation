package be.ordina.service;

import be.ordina.dto.PersonDTO;
import com.orientechnologies.orient.client.remote.OServerAdmin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import java.io.IOException;
import java.util.List;

/**
 * Created by ToVn on 05/06/15.
 */
public class GraphServiceImpl implements  GraphService {

    public static final String TWITTER_CONNECTIONS_DB = "TwitterConnectionsDB";
    private OServerAdmin serverAdmin;

    private OServerAdmin setup() throws IOException{
        serverAdmin = new OServerAdmin("remote:localhost:2424").connect("root", "ordina");

        if (!serverAdmin.listDatabases().containsKey(TWITTER_CONNECTIONS_DB)) {
            serverAdmin.createDatabase(TWITTER_CONNECTIONS_DB, "graph", "plocal");
            System.out.println("created database");
        }

        final OrientGraphFactory factory = new OrientGraphFactory(
                "remote:localhost:2424/" + TWITTER_CONNECTIONS_DB, "admin", "admin");

        try {
            final OrientGraphNoTx g = factory.getNoTx();

            if (g.getVertexType("person") == null) {
                g.createVertexType("person");
            }
        } finally {
            System.out.println("going to close connection");
            // this also closes the OrientGraph instances created by the factory
            // Note that OrientGraphFactory does not implement Closeable
            factory.close();

        }

        return serverAdmin;
    }


    @Override
    public void setupDB() throws IOException {
        serverAdmin = setup();
    }



    @Override
    public void createPersonIfNotExists(PersonDTO person) {
        final OrientGraphFactory factory = new OrientGraphFactory(
                "remote:localhost:2424/" + TWITTER_CONNECTIONS_DB, "admin", "admin");

        try {
            final OrientGraphNoTx g = factory.getNoTx();

            Vertex personVertex = getPerson(person.getTwitterId(), g);

            if (personVertex == null) {
                 g.addVertex("class:person", "twitterId", person.getTwitterId()
                        , "screenname", person.getScreenName(), "name", person.getName()
                        , "description", person.getDescription());
            }
        } finally {
            System.out.println("going to close connection");
            // this also closes the OrientGraph instances created by the factory
            // Note that OrientGraphFactory does not implement Closeable
            factory.close();

        }
    }

    @Override
    public void addConnectionsToPerson(Long twitterIdPerson, List<Long> connections) {


        final OrientGraphFactory factory = new OrientGraphFactory(
                "remote:localhost:2424/" + TWITTER_CONNECTIONS_DB, "admin", "admin");

        try {

            final OrientGraphNoTx g = factory.getNoTx();

            Vertex personVertex = getPerson(twitterIdPerson, g);

            if (personVertex != null) {
                for (Long connectionId : connections) {
                    boolean connectionAlready = false;
                    for (Edge edge : personVertex.getEdges(Direction.OUT, "connection") ){
                        if(edge.getVertex(Direction.OUT).getProperty("twitterid").equals(connectionId)) {
                            connectionAlready = true;
                            break;
                        }
                    }

                    if (!connectionAlready) {
                        //lookup other person
                        Vertex personWhoMustBecomeAConnection =  getPerson(connectionId, g);

                        //create new edge of the connection type
                        if (personWhoMustBecomeAConnection != null) {
                            personVertex.addEdge("connection", personWhoMustBecomeAConnection);
                        }
                    }
                }
            }
        } finally {
            System.out.println("going to close connection");
            // this also closes the OrientGraph instances created by the factory
            // Note that OrientGraphFactory does not implement Closeable
            factory.close();

        }

    }

    private Vertex getPerson(Long twitterIdPerson, OrientGraphNoTx g) {
        Iterable<Vertex> vertices = (Iterable<Vertex>) g.command(
                new OCommandSQL("select * from Person where twitterId = " + twitterIdPerson))
                .execute();

        Vertex personVertex = null;
        for (Vertex vertex : vertices) {
            System.out.println(">>>>>>>>>>>>>>>person found " + vertex.toString());

            if (personVertex == null) {
                personVertex = vertex;
            }

        }
        return personVertex;
    }
}
