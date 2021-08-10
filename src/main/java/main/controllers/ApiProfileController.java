package main.controllers;

import main.api.response.PostDataResponse;
import main.service.ProfileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.text.Collator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profile")
public class ApiProfileController {

    private final ProfileService profileService;

    public ApiProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    //TODO редактирование профиля Api page 19
    @PostMapping(value = "/my" /*,
            produces = { "application/json" },
            consumes = {"multipart/form-data"}
            */
            )

    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostDataResponse> updateMyProfile(
/*
            @RequestPart("removePhoto")  Byte removePhoto,
            @RequestPart("name") String name,
            @RequestPart("email")  String email,
            @RequestPart("password")  String password,
            @RequestPart("photo")  MultipartFile photo,
*/



            @RequestParam(name = "removePhoto", required = false) Byte removePhoto,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "photo", required = false) MultipartFile photo,

            Principal principal
    ) {

        return ResponseEntity.ok(profileService.updateProfile(photo, removePhoto, name, email, password, principal.getName()));
    }
}
