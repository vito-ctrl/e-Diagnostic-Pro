package ma.teleexpertise.service;

import ma.teleexpertise.model.User;
import ma.teleexpertise.model.enums.Role;
import ma.teleexpertise.model.enums.Specialite;
import ma.teleexpertise.repository.UserRepository;
import ma.teleexpertise.util.PasswordUtil;

import java.util.List;

public class AdminService {

    private final UserRepository userRepository;

    public AdminService() {
        this.userRepository = new UserRepository();
    }

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllStaff() {
        return userRepository.findAll();
    }

    public User createStaffMember(String username, String email, String password,
                                  String nom, String prenom, Role role,
                                  Specialite specialite, Double tarif) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Le nom d'utilisateur est déjà utilisé : " + username);
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        User user = new User(username, email, hashedPassword, nom, prenom, role);
        if (role == Role.SPECIALISTE) {
            user.setSpecialite(specialite);
            user.setTarifConsultation(tarif != null ? tarif : 200.0);
            user.setDureeConsultation(30);
        }
        return userRepository.save(user);
    }

    public void deactivateStaff(Long userId) {
        userRepository.delete(userId);
    }
}
