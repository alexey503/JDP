package main.controllers;

import main.model.BlogRepository;
import main.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;

@Controller
public class DefaultController {
    //для обычных запросов не через API (главная страница - /, в частности)

    @Autowired
    private BlogRepository repository;

    @RequestMapping("/")
    public String index(Model model)
    {
        //Сделать так, чтобы при входе на главную страницу открывался шаблон index.html

        return "index";
    }
/*
    @RequestMapping("/")
    public String index() {

//        Iterable<UserEntity> userIterable = repository.findAll();
//        ArrayList<UserEntity> userEntities = new ArrayList<>();
//        for (UserEntity userEntity : userIterable) {
//            userEntities.add(userEntity);
//        }
//        model.addAttribute("user", userEntities);
//        model.addAttribute("usersCount", userEntities.size());

        UserEntity userEntity = new UserEntity(1,true, null, "A", "B", "C", "D", "E");
        repository.save(userEntity);
        return new Date().toString();
    }
*/

}
