package be.ordina.service;

import be.ordina.dto.PersonDTO;
import twitter4j.User;

import java.util.List;

/**
 * Created by ToVn on 05/06/15.
 */
public interface TwitterService {

    List<Long> getConnections(String userId);


    List<Long> getConnectionsById(Long id);

    User getPersonDetails(Long id);

    User getPersonDetails(String screenName);

    PersonDTO transformToPersonDTO(User user);


}
