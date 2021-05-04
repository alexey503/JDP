package main.controller;

import model.BlogRepository;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
public class DefaultController {
    //для обычных запросов не через API (главная страница - /, в частности)

    @Autowired
    private BlogRepository repository;


    @RequestMapping("/")
    public String index(Model model) {
        Iterable<User> userIterable = repository.findAll();
        ArrayList<User> users = new ArrayList<>();
        for (User user : userIterable) {
            users.add(user);
        }
        model.addAttribute("user", users);
        model.addAttribute("usersCount", users.size());

        User user = new User(1,true, null, "A", "B", "C", "D", "E");

        repository.save(user);

        return "index";
    }


}
