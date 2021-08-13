package main.service;

import main.api.response.PostDataResponse;
import main.model.entities.PostVote;
import main.model.repositories.VotesRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class VoteService {

    @Autowired
    private final VotesRepository votesRepository;
    @Autowired
    private final PostRepository postRepository;
    @Autowired
    private final UserRepository userRepository;


    public PostDataResponse putVote(int postId, int userId, byte voteValue){

        Post post = postRepository.findPostById(postId).orElse(return new PostDataResponse());
        User user = userRepository.findUserById(userId).orElse(return new PostDataResponse());
        PostVote postVote = votesRepository.findByPostAndByUser(post, user).orElse(new PostVote());

        if(postVote.getValue() == voteValue){
            return new PostDataResponse();//result = false
        }
        postVote.setUser(user);
        postVote.setPost(post);
        postVote.setTime(new Date().gettime()/1000);
        postVote.setValue(voteValue);

        votesRepository.save(postVote);

        PostDataResponse response = new PostDataResponse();
        response.setResult(true);
        return response;

    }

}
