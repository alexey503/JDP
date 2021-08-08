package main.api.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.Map;

@Data
public class CalendarDto {
    private ArrayList<String> years;
    private Map<String, Integer> posts;

}
