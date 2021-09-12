package main.service;

import main.api.response.CalendarDto;
import main.api.response.PostDateCountDto;
import main.model.repositories.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    @Autowired
    private PostsRepository repository;

    public CalendarDto getCalendarDto(String year) {

        CalendarDto result = new CalendarDto();
        List<String> dateAndPostCount = repository.findCountPostsByDateForYear(Integer.parseInt(year));

        dateAndPostCount.stream().forEach(System.out::println);

        result.setPosts(
                dateAndPostCount.stream()
                        .map(s -> {
                            String[] str = s.split("[, ]");
                            return new PostDateCountDto(str[0], str[2]);
                        })

                        .collect(
                                Collectors.toMap(
                                        date -> date.getDate(),
                                        count -> Integer.parseInt(count.getCount()))
                        )

        );
        result.setYears(repository.findAllYearPublications());

        return result;
    }
}
