package com.fenris06.applicationmanager.service.impl;

import com.fenris06.applicationmanager.dto.UserDto;
import com.fenris06.applicationmanager.exception.NotFoundException;
import com.fenris06.applicationmanager.mapper.UserMapper;
import com.fenris06.applicationmanager.model.Role;
import com.fenris06.applicationmanager.model.User;
import com.fenris06.applicationmanager.repository.RoleRepository;
import com.fenris06.applicationmanager.repository.UserRepository;
import com.fenris06.applicationmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Value("${user.role:ROLE_OPERATOR}")
    private String updateRole;

    @Override
    public List<UserDto> getUsers(Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return userRepository.findAllWithRoles(pageRequest).stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> updateUsersRole(Set<Long> ids) {
        Role role = roleRepository.findByName(updateRole)
                .orElseThrow(() -> new NotFoundException(String.format("Role %s not found", updateRole)));
        List<User> users = userRepository.findByIdInAndRoles_NameNot(ids, updateRole);
        setRole(users, role);
        return userRepository.saveAll(users).stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    private void setRole(List<User> users, Role role) {
        users.forEach(user -> user.getRoles().add(role));
    }
}
