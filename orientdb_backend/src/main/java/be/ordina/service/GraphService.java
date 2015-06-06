package be.ordina.service;

import be.ordina.dto.PersonDTO;

import java.io.IOException;
import java.util.List;

/**
 * Created by ToVn on 05/06/15.
 */
public interface GraphService {

    void createPersonIfNotExists(PersonDTO person);

    void addConnectionsToPerson(Long person, List<Long> connections);

    void setupDB() throws IOException;
}
