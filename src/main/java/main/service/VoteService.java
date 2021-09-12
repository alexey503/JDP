package main.service;

import main.api.response.PostDataResponse;
import main.model.entities.Post;
import main.model.entities.PostVote;
import main.model.entities.User;
import main.model.repositories.PostsRepository;
import main.model.repositories.UserRepository;
import main.model.repositories.VotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class VoteService {

    @Autowired
    private VotesRepository votesRepository;
    @Autowired
    private PostsRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;


    public PostDataResponse putVote(int postId, byte voteValue) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post;
        if (optionalPost.isPresent()) {
            post = optionalPost.get();
        } else {
            return new PostDataResponse();
        }

        User user = userRepository.findByEmail(authService.getAuthUserEmail()).get();

        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalPost.isPresent()) {
            user = optionalUser.get();
        } else {
            return new PostDataResponse();
        }

        PostVote postVote = votesRepository.findByPostAndByUser(post, user).orElse(new PostVote());

        if (postVote.getValue() == voteValue) {
            return new PostDataResponse();
        }
        postVote.setUser(user);
        postVote.setPost(post);
        postVote.setTime(new Date().getTime() / 1000);
        postVote.setValue(voteValue);

        votesRepository.save(postVote);

        PostDataResponse response = new PostDataResponse();
        response.setResult(true);
        return response;
    }
}
