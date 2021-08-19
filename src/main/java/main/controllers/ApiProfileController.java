package main.controllers;

import main.api.request.ProfileDTO;
import main.api.response.PostDataResponse;
import main.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/profile")
public class ApiProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping(value = "/my",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostDataResponse> updateMyProfile(@RequestBody ProfileDTO profileDTO, Principal principal) {

        return ResponseEntity.ok(profileService.updateProfile(null, profileDTO.getRemovePhoto(), profileDTO.getName(), profileDTO.getEmail(), profileDTO.getPassword(), principal.getName()));

    }

    @PostMapping(value = "/my",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostDataResponse> updateProfileAndPhoto(
            @RequestParam(name = "photo", required = false) MultipartFile photo,
            @RequestParam(name = "removePhoto", required = false) Byte removePhoto,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "password", required = false) String password,
            Principal principal
    ) {

        return ResponseEntity.ok(profileService.updateProfile(photo, removePhoto, name, email, password, principal.getName()));
    }
}
