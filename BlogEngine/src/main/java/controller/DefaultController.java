package controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {
    //для обычных запросов не через API (главная страница - /, в частности)

    @RequestMapping("/")
    public String index(Model model) {

        return "index";
    }


}
