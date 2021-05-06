package main.service;

import main.model.PostsRepository;
import main.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostsService {
    @Autowired
    private PostsRepository repository;

    public double getTagWeight(Tag tag){
        return 0.0;
    }
}
