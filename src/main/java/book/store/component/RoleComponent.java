package book.store.component;

import book.store.model.Role;
import book.store.model.User;
import book.store.repository.role.RoleRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleComponent {

    private final RoleRepository roleRepository;

    public void addRoleToDataBase() {
        if (roleRepository.getByRoleName(Role.RoleName.ADMIN) == null
                || roleRepository.getByRoleName(Role.RoleName.USER) == null) {
            roleRepository.deleteAll();
            Role roleAdmin = new Role();
            roleAdmin.setRoleName(Role.RoleName.ADMIN);
            roleRepository.save(roleAdmin);
            Role roleUser = new Role();
            roleUser.setRoleName(Role.RoleName.USER);
            roleRepository.save(roleUser);
        }
    }

    public void addRole(User user) {
        addRoleToDataBase();
        Set<Role> roles = new HashSet<>();
        if (user.getEmail().endsWith("@bookstore.com")) {
            roles.add(roleRepository.getByRoleName(Role.RoleName.ADMIN));
        } else {
            roles.add(roleRepository.getByRoleName(Role.RoleName.USER));
        }
        user.setRoles(roles);
    }
}
