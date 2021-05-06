package main.service;

import main.api.response.TagResponse;
import main.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagsService {

    private List<TagResponse> listTagResponse;

    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private Tag2PostRepository tag2PostRepository;
    @Autowired
    private PostsRepository postsRepository;

    public List<TagResponse> getTags(){

        if(listTagResponse == null){
            listTagResponse = new ArrayList<>();
            Iterable<Tag> tagIterable = tagsRepository.findAll();
            for (Tag tag : tagIterable) {
                listTagResponse.add(getTagResponse(tag));
            }
        }
        return listTagResponse;
    }

    private TagResponse getTagResponse(Tag tag){
        TagResponse tagResponse = new TagResponse();
        tagResponse.setName(tag.getName());
        tagResponse.setWeight(getTagWeight(tag.getName()));
        return tagResponse;
    }

    private double getTagWeight(String tagName){
        long count = postsRepository.count();
        Map<String, Long> tagsCount = new HashMap<>();

        Iterable<Tag2Post> tag2PostIterable = tag2PostRepository.findAll();

        for (Tag2Post tag2Post : tag2PostIterable) {
            if (tagsCount.containsKey(tag2Post.getTag().getName())) {
                tagsCount.put(tag2Post.getTag().getName(),
                        tagsCount.get(tag2Post.getTag().getName()) + 1);
            }
        }

        for (Map.Entry<String, Long> entry : tagsCount.entrySet()) {

        }
//tagsCount.

        return 0.1;
    }
    /*
    * количество постов всего: count = 20
    * количество постов с искомым тегом tagPostCount = 4
    * ненормированный вес:
    *   dWeightHibernate = tagPostCount / count = 4 / 20 = 0.20
    * K для нормализации:
    *   K = 1 / ненорм вес самого популярного тэга = 1.11
    *
    *
    * */
}
