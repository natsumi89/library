package com.library.service;

import com.library.domain.User;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void insert(User user) {
        userRepository.insert(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }


    public User findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void delete(Integer userId) {
        userRepository.delete(userId);
    }

}
