package ru.itmentor.mvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.common.entity.Role;
import ru.itmentor.common.repository.RoleRepository;
import ru.itmentor.mvc.service.RoleService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getListRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Set<Role> getRolesByIds(List<Long> roleIds) {
        return new HashSet<>(roleRepository.findAllById(roleIds));
    }
}