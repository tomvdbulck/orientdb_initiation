package be.ordina.service;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * Created by ToVn on 01/06/15.
 */
public class DatabaseService {

    private String databaseUrl;
    private String user;
    private String password;


    public DatabaseService(String databaseUrl, String user, String password) {
        this.databaseUrl = databaseUrl;
        this.user = user;
        this.password = password;
    }



    public Vertex createVertex(String type) {


        return null;
    }

    public Edge createEdge(Vertex from, Vertex to, String relationName) {


        return null;
    }





}
