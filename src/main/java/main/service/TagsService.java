package main.service;

import main.api.response.TagResponse;
import main.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagsService {

    private List<TagResponse> tagResponses;

    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private Tag2PostRepository tag2PostRepository;

    public List<TagResponse> getTags(){

        if(tagResponses == null){
            tagResponses = new ArrayList<>();
            Iterable<Tag> tagIterable = tagsRepository.findAll();
            for (Tag tag : tagIterable) {
                TagResponse tagResponse = new TagResponse();
                tagResponse.setName(tag.getName());
                tagResponse.setWeight(String.format("%.2f", getTagWeight(tag.getName())));
                tagResponses.add(tagResponse);
            }
        }
        return tagResponses;
    }

    private double getTagWeight(String tagName){

        Map<String, Long> tagsCount = new HashMap<>();

        Iterable<Tag2Post> tag2PostIterable = tag2PostRepository.findAll();

        for (Tag2Post tag2Post : tag2PostIterable) {
            if (tagsCount.containsKey(tag2Post.getTag().getName())) {
                tagsCount.put(tag2Post.getTag().getName(),
                        tagsCount.get(tag2Post.getTag().getName()) + 1);
            }else{
                tagsCount.put(tag2Post.getTag().getName(), 1L);
            }
        }

        long maxRatingTag = 0;
        for (Map.Entry<String, Long> entry : tagsCount.entrySet()) {
            if(entry.getValue() > maxRatingTag){
                maxRatingTag = entry.getValue();
            }
        }
        if(maxRatingTag == 0 || !tagsCount.containsKey(tagName)){
            return 0;
        }
        return tagsCount.get(tagName).doubleValue() / ((double)maxRatingTag);
    }
}
