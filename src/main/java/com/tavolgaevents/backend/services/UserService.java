package com.tavolgaevents.backend.services;

import com.tavolgaevents.backend.models.Contest;
import com.tavolgaevents.backend.models.Nomination;
import com.tavolgaevents.backend.models.Role;
import com.tavolgaevents.backend.models.User;

import java.util.List;

public interface UserService {
    public User getUserById(Long Id);
    public List<User> getUsersByRole(Role role);
    public List<User> getAll();
    public List<User> getAllByContest(Long contestId);
    public List<User> getAllParticipantsByNomination(Long contestId);
    public User getUserByLogin(String login);
}
