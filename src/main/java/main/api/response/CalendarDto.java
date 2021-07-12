package main.api.response;

import java.util.ArrayList;
import java.util.Map;

public class CalendarDto {
    private ArrayList<String> years;
    private Map<String, Integer> posts;

    public ArrayList<String> getYears() {
        return years;
    }

    public void setYears(ArrayList<String> years) {
        this.years = years;
    }

    public Map<String, Integer> getPosts() {
        return posts;
    }

    public void setPosts(Map<String, Integer> posts) {
        this.posts = posts;
    }
}
