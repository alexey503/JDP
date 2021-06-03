package main.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class DefaultController {

    //для остальных запросов не через API (главная страница - /, в частности)
    @RequestMapping(value="/**", method= RequestMethod.GET)
    public String index()
    {
        return "index";
    }

}
