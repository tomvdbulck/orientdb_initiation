package be.ordina.service;

import be.ordina.dto.PersonDTO;
import com.orientechnologies.orient.client.remote.OServerAdmin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.IOException;
import java.util.List;

/**
 * Created by ToVn on 05/06/15.
 */
public class GraphServiceImpl implements  GraphService {

    public static final String TWITTER_CONNECTIONS_DB = "TwitterConnectionsDB";
    public static final String SCREENNAME = "screenname";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String NUMBER_OF_FOLLOWERS = "numberOfFollowers";
    public static final String TWITTER_ID = "twitterId";
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
                 g.addVertex("class:person", TWITTER_ID, person.getTwitterId()
                        , SCREENNAME, person.getScreenName(), NAME, person.getName()
                        , DESCRIPTION, person.getDescription(), NUMBER_OF_FOLLOWERS, person.getNumberOfFollowers());
            }
        } finally {
            System.out.println("going to close connection");
            // this also closes the OrientGraph instances created by the factory
            // Note that OrientGraphFactory does not implement Closeable
            factory.close();

        }
    }




    @Override
    public void addConnectionToPerson(Long twitterIdPerson, Long connectionId) {


        final OrientGraphFactory factory = new OrientGraphFactory(
                "remote:localhost:2424/" + TWITTER_CONNECTIONS_DB, "admin", "admin");

        try {

            final OrientGraphNoTx g = factory.getNoTx();

            Vertex personVertex = getPerson(twitterIdPerson, g);

            if (personVertex != null) {

                boolean connectionAlready = false;
                for (Edge edge : personVertex.getEdges(Direction.OUT, "connection") ){

                    if(edge.getVertex(Direction.IN).getProperty(TWITTER_ID).equals(connectionId)) {

                        System.out.println(">>>>>>>follower " + connectionId + " is already a connection of " + twitterIdPerson);

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

                        System.out.println("created a new connection");
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

    @Override
    public PersonDTO getPerson(Long twitterIdPerson) {

        PersonDTO person = null;

        final OrientGraphFactory factory = new OrientGraphFactory(
                "remote:localhost:2424/" + TWITTER_CONNECTIONS_DB, "admin", "admin");

        try {

            final OrientGraphNoTx g = factory.getNoTx();

            System.out.println("going to look for person with twitter id " + twitterIdPerson);

            Iterable<Vertex> vertices = (Iterable<Vertex>) g.command(
                    new OCommandSQL("select * from Person where twitterId = " + twitterIdPerson))
                    .execute();

            Vertex personVertex = null;
            for (Vertex vertex : vertices) {
                System.out.println(">>>>>>>>>>>>>>>person found " + vertex.toString());
                System.out.println("with twitter id = " + vertex.getProperty(TWITTER_ID) + " and name = " + vertex.getProperty(NAME) );

                if (personVertex == null) {
                    personVertex = vertex;
                }
            }

            if (personVertex != null) {
                person = new PersonDTO((Long)personVertex.getProperty("twitterId"), (String)personVertex.getProperty(SCREENNAME)
                    , (String)personVertex.getProperty(NAME), (String)personVertex.getProperty(DESCRIPTION)
                        , (Integer)personVertex.getProperty(NUMBER_OF_FOLLOWERS));


                int countOfLinkedFollowers = 0;
                for (Edge edge : personVertex.getEdges(Direction.OUT, "connection")){
                   countOfLinkedFollowers++;
                }
                person.setLinkedFollowers(countOfLinkedFollowers);
            }
        } finally {
            System.out.println("going to close connection");
            // this also closes the OrientGraph instances created by the factory
            // Note that OrientGraphFactory does not implement Closeable
            factory.close();

        }

        return person;
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
