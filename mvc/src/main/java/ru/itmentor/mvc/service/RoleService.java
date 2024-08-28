package ru.itmentor.mvc.service;

import ru.itmentor.common.entity.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {

    List<Role> getListRoles();

    Set<Role> getRolesByIds(List<Long> roleIds);
}
