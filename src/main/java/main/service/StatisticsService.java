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
        Integer viewCount = postsRepository.countUserViews(userId);
        Long firstPublicationTime = postsRepository.countUsersFirstPublicationTime(userId);
        return new StatisticsResponse(
                postsRepository.countByUserId(userId),
                votesRepository.getUserLikesCount(userId),
                votesRepository.getUserDislikesCount(userId),
                viewCount != null ? viewCount : 0,
                firstPublicationTime != null ? firstPublicationTime : 0);
    }

    public StatisticsResponse getAllStatistics() {

        return new StatisticsResponse(
                postsRepository.countAll(),
                votesRepository.countLikes(),
                votesRepository.countDislikes(),
                postsRepository.countViews(),
                postsRepository.firstPublicationTime());
    }
}
