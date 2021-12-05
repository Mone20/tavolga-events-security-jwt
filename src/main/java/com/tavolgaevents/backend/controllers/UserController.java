package com.tavolgaevents.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.tavolgaevents.backend.models.*;
import com.tavolgaevents.backend.repository.RoleRepository;
import com.tavolgaevents.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	RoleRepository roleRepository;

	@JsonView(Views.Public.class)
	@GetMapping("/all/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<User>> getAllParticipants() {
		List<User> users = userService.getUsersByRole(roleRepository.findByName(RoleConstants.ROLE_USER).get());
		return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
	}

	@JsonView(Views.Public.class)
	@GetMapping("/contest/{contestId}")
	@PreAuthorize("hasRole('JURY') or hasRole('ASSESSOR') or hasRole('ADMIN')")
	public ResponseEntity<List<User>> getParticipantsByContestId(@PathVariable String contestId) {
		List<User> users = userService.getAllByContest(Long.parseLong(contestId));
		return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
	}

	@JsonView(Views.Public.class)
	@GetMapping("/nominations/{nominationId}")
	@PreAuthorize("hasRole('JURY') or hasRole('ASSESSOR') or hasRole('ADMIN')")
	public ResponseEntity<List<User>> getParticipantsByNominationId(@PathVariable String nominationId) {
		List<User> users = userService.getAllParticipantsByNomination(Long.parseLong(nominationId));
		return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
	}

	@JsonView(Views.Public.class)
	@PostMapping("/avatar/{userId}")
	@PreAuthorize("hasRole('USER') or hasRole('JURY') or hasRole('ASSESSOR') or hasRole('ADMIN')")
	public ResponseEntity<?> changeAvatar(@PathVariable String userId, @RequestBody MultipartFile newAvatar) {
		userService.changeAvatar(Long.valueOf(userId), newAvatar);
		return ResponseEntity.ok().build();
	}

	@JsonView(Views.Public.class)
	@GetMapping("/id/{userId}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<User> getUserById(@PathVariable String userId) {
		return ResponseEntity.ok(userService.getUserById(Long.valueOf(userId)));
	}

	@JsonView(Views.Public.class)
	@GetMapping("/role/{roleId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<User>> getUserByRoleId(@PathVariable String roleId) {
		Optional<Role> role = roleRepository.findById(Integer.parseInt(roleId));
		return role.map(value -> ResponseEntity.ok(userService.getUsersByRole(value))).orElseGet(() -> ResponseEntity.notFound().build());
	}

}
