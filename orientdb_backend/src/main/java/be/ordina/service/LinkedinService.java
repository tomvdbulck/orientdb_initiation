package be.ordina.service;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * Service to access LinkedIn
 *
 * Created by ToVn on 01/06/15.
 */
public class LinkedinService {

    public static final void main (String[] args) {

        LinkedinService service = new LinkedinService();
        service.connect();
    }




    public void connect() {
        ServiceBuilder serviceBuilder = new ServiceBuilder();
        serviceBuilder.provider(LinkedInApi.class);
        serviceBuilder.apiKey("77u0p7p2rdc72h");
        serviceBuilder.apiSecret("FPwYOZm2QU57Z6El");
        OAuthService service = serviceBuilder.build();

        Token requestToken = service.getRequestToken();


        String url = "http://api.linkedin.com/v1/people/~";
        OAuthRequest request = new OAuthRequest(Verb.GET, url);
        service.signRequest(requestToken, request);
        Response response = request.send();
        System.out.println(response.getBody());
        System.out.println();System.out.println();





    }




}
