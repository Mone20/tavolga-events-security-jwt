package com.tavolgaevents.backend.services.impl;

import com.tavolgaevents.backend.models.*;
import com.tavolgaevents.backend.repository.ContestRepository;
import com.tavolgaevents.backend.repository.FileRepository;
import com.tavolgaevents.backend.repository.NominationRepository;
import com.tavolgaevents.backend.repository.UserRepository;
import com.tavolgaevents.backend.services.FileService;
import com.tavolgaevents.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContestRepository contestRepository;

    @Autowired
    NominationRepository nominationRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    FileService fileService;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public List<User> getAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public List<User> getAllByContest(Long contestId) {
        Contest contest = contestRepository.findById(contestId).get();
        List<User> returnedUsers = new ArrayList<>();
        contest.getNominations().forEach(nomination -> returnedUsers.addAll(nomination.getParticipants()));
        return returnedUsers;
    }

    @Override
    public List<User> getAllParticipantsByNomination(Long nominationId) {
        return nominationRepository.findById(nominationId).get().getParticipants();
    }

    @Override
    public User getUserByLogin(String login) {
        Optional<User> optionalUser = userRepository.findByUsername(login);
        return optionalUser.orElse(null);
    }

    @Override
    public List<User> getSortedUsersByRating(Long contestId) {
        return null;
    }

    @Override
    public void changeAvatar(@NotBlank Long userId, MultipartFile newAvatar) {
        User user = getUserById(Long.valueOf(userId));
        File file = fileService.save(newAvatar);
        file.setUser(user);
        file = fileRepository.save(file);
        user.setAvatar(file);
        userRepository.save(user);

    }
}
