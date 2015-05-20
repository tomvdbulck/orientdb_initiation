package be.ordina.orientdb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ToVn on 20/05/15.
 */

@Controller
public class SpringBootController {


    private static final String INDEX_TEMPLATE = "index";

    @RequestMapping("/")
    public String index() {


        return INDEX_TEMPLATE;
    }
}
