package main.controllers;

import main.api.response.PostDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ApiProfileController {

    //TODO редактирование моего профиля Api page 19
    @PostMapping("/my")
    public ResponseEntity<PostDataResponse> editMyProfile(Map<String,String> editParamsMap) {

        return ResponseEntity.ok(new PostDataResponse());
    }
}
