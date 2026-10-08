package ma.teleexpertise.service;

import ma.teleexpertise.model.User;
import ma.teleexpertise.model.enums.Role;
import ma.teleexpertise.model.enums.Specialite;
import ma.teleexpertise.repository.UserRepository;
import ma.teleexpertise.util.PasswordUtil;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> authenticate(String username, String plainPassword) {
        if (username == null || plainPassword == null) {
            return Optional.empty();
        }
        Optional<User> userOpt = userRepository.findByUsername(username.trim());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isActif() && PasswordUtil.checkPassword(plainPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public User createUser(String username, String email, String plainPassword,
                           String nom, String prenom, Role role,
                           Specialite specialite, Double tarif) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Le nom d'utilisateur existe déjà : " + username);
        }
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        User user = new User(username, email, hashedPassword, nom, prenom, role, specialite, tarif);
        return userRepository.save(user);
    }

    public User updateSpecialistProfile(Long userId, Specialite specialite, Double tarif) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'id : " + userId));
        if (user.getRole() != Role.SPECIALISTE) {
            throw new IllegalStateException("L'utilisateur n'est pas un spécialiste");
        }
        if (specialite != null) {
            user.setSpecialite(specialite);
        }
        if (tarif != null && tarif >= 0) {
            user.setTarifConsultation(tarif);
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.delete(id);
    }
}
