package be.ordina.orientdb.controller;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by ToVn on 20/05/15.
 */

@Controller
public class SpringBootController {


    private ServiceBuilder serviceBuilder;
    private Token requestToken;

    private static final String INDEX_TEMPLATE = "index";
    private static final String LINKEDIN_RESULTS_TEMPLATE = "linkedinResults";

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
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://api.linkedin.com/v1/people/~:(id,first-name,last-name,industry,headline)");
        service.signRequest(accessToken, oauthRequest);
        Response oauthResponse = oauthRequest.send();
        System.out.println(oauthResponse.getBody());


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


}
