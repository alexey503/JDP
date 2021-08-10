package main.controllers;

import main.api.response.PostDataResponse;
import main.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ApiProfileController {

    private final ProfileService profileService;

    public ApiProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    //TODO редактирование профиля Api page 19
    @PostMapping("/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostDataResponse> editMyProfile(@RequestBody Map<String,Object> editParamsMap, Principal principal) {

        return ResponseEntity.ok(profileService.editProfile(editParamsMap, principal.getName()));
    }
}
