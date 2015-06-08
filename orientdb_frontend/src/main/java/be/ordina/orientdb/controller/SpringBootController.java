package be.ordina.orientdb.controller;

import be.ordina.dto.PersonDTO;
import be.ordina.service.GraphService;
import be.ordina.service.TwitterService;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import twitter4j.User;

import javax.swing.*;
import java.util.List;

/**
 * Created by ToVn on 20/05/15.
 */

@Controller
public class SpringBootController {


    private ServiceBuilder serviceBuilder;
    private Token requestToken;

    private static final String INDEX_TEMPLATE = "index";
    private static final String LINKEDIN_RESULTS_TEMPLATE = "linkedinResults";

    private TwitterService twitterService;
    private GraphService graphService;

    @Autowired
    public SpringBootController(TwitterService twitterService, GraphService graphService) {
        this.twitterService = twitterService;
        this.graphService = graphService;
    }

    @RequestMapping("/")
    public String index() {


        return INDEX_TEMPLATE;
    }


    @RequestMapping("/callback")
    public String callBack(@RequestParam(value="oauth_verifier", required=false) String oauthVerifier, WebRequest request) {

        System.out.println("entering callback to process access token");

        OAuthService service = serviceBuilder.build();

// getting access token - this is tied to the requesttoken with which you issued the request - new request token requires new access token
        Verifier verifier = new Verifier(oauthVerifier);
        Token accessToken = service.getAccessToken(requestToken, verifier);
// getting user profile
        OAuthRequest oauthRequestConnections = new OAuthRequest(Verb.GET, "http://api.linkedin.com/v1/people/~/connections:(id,first-name,last-name,industry,headline)");
        service.signRequest(accessToken, oauthRequestConnections);
        Response oauthResponseConnections = oauthRequestConnections.send();
        System.out.println("==========> CONNECTIONS <=========");
        System.out.println(oauthResponseConnections.getBody());

        return LINKEDIN_RESULTS_TEMPLATE;
    }



    @RequestMapping("/linkedin")
    public String connect() {
        serviceBuilder = new ServiceBuilder();
        serviceBuilder.provider(LinkedInApi.class);
        serviceBuilder.apiKey("77u0p7p2rdc72h");
        serviceBuilder.apiSecret("FPwYOZm2QU57Z6El");
        serviceBuilder.callback("http://127.0.0.1:8080/callback");
        OAuthService service = serviceBuilder.build();

        System.out.println("going to go to authorization url with callback ");
        requestToken = service.getRequestToken();

        // redirect to linkedin auth page
        return "redirect:" + service.getAuthorizationUrl(requestToken);
    }

    @RequestMapping("twitter")
    public String getTwitter() {
        return "twitter";
    }



    @RequestMapping("/twitterGet")
    public String getTwitterConnectionsAndTheirConnections(String userId) {


        User startUser = twitterService.getPersonDetails("tomvdbulck");
        graphService.createPersonIfNotExists(twitterService.transformToPersonDTO(startUser));

        System.out.println("get connections Tom");
        List<Long> connections = twitterService.getConnections("tomvdbulck");


        PersonDTO person = twitterService.transformToPersonDTO(twitterService.getPersonDetails(startUser.getId()));
        System.out.println("followers for Tom is " + person.getNumberOfFollowers() );
        System.out.print(">>>>>>> already linked is " + person.getLinkedFollowers());

        for (Long id : connections) {
            System.out.println(">>handle connection with id " + id);

            if (graphService.getPerson(id) == null) {
                PersonDTO baseConnectionPerson = twitterService.transformToPersonDTO(twitterService.getPersonDetails(id));
                graphService.createPersonIfNotExists(baseConnectionPerson);
            }

            System.out.println("going to add " + id  + " to Tom");
            graphService.addConnectionToPerson(startUser.getId(), id);
        }

        for (Long id : connections) {
            getConnectionsLevel2(id);
        }

        return "twitterResults";



    }

    private void getConnectionsLevel2(Long id) {

        //check if person is not already connected fully!
        PersonDTO person = graphService.getPerson(id);
        System.out.println("going to process " + person.getName() + " with id " + person.getTwitterId());
        System.out.println("number of followers =  " + person.getNumberOfFollowers() + " of which " + person.getLinkedFollowers()
                + " have already been linked into the database");


        if (person.getNumberOfFollowers() > person.getLinkedFollowers()) {
            List<Long> connectionsFromConnection = twitterService.getConnectionsById(id);
            System.out.println("connections from Connection (" + id + ") ========> " + connectionsFromConnection);

            for (Long connectionId : connectionsFromConnection) {
                if (graphService.getPerson(connectionId) == null) {
                    User user = twitterService.getPersonDetails(connectionId);
                    if (user != null) {
                        graphService.createPersonIfNotExists(twitterService.transformToPersonDTO(user));
                    }

                }

                if (graphService.getPerson(connectionId) != null) {
                    graphService.addConnectionToPerson(id, connectionId);
                }

            }
        }



    }


}
