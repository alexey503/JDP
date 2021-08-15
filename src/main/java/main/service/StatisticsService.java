package main.service;

import main.api.response.StatisticsResponse;
import main.model.repositories.PostsRepository;
import main.model.repositories.SettingsRepository;
import main.model.repositories.VotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private VotesRepository votesRepository;
    @Autowired
    private SettingsRepository settingsRepository;




    public StatisticsResponse getMyStatistics(int userId) {
        return new StatisticsResponse(
                postsRepository.countByUserId(userId),
                votesRepository.getUserLikesCount(userId),
                votesRepository.getUserDislikesCount(userId),
                postsRepository.countUserViews(userId),
                postsRepository.countUsersFirstPublicationTime(userId)
        );
    }

    public StatisticsResponse getAllStatistics() {

        return new StatisticsResponse(
                postsRepository.countAll(),
                votesRepository.countLikes(),
                votesRepository.countDislikes(),
                postsRepository.countViews(),
                postsRepository.firstPublicationTime()
        );

    }
}
