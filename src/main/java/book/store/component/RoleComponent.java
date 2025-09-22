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

    public void addRole(User user) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.getByRoleName(Role.RoleName.USER));
        user.setRoles(roles);
    }
}
