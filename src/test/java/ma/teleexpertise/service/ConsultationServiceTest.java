package ma.teleexpertise.service;

import ma.teleexpertise.model.ActeTechnique;
import ma.teleexpertise.model.Consultation;
import ma.teleexpertise.model.DemandeExpertise;
import ma.teleexpertise.model.User;
import ma.teleexpertise.model.enums.PrioriteExpertise;
import ma.teleexpertise.model.enums.Role;
import ma.teleexpertise.model.enums.Specialite;
import ma.teleexpertise.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceTest {

    @Mock
    private ConsultationRepository consultationRepository;

    @Mock
    private DemandeExpertiseRepository demandeExpertiseRepository;

    @Mock
    private CreneauRepository creneauRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ActeTechniqueRepository acteTechniqueRepository;

    @Mock
    private FileAttenteRepository fileAttenteRepository;

    private ConsultationService consultationService;

    @BeforeEach
    void setUp() {
        consultationService = new ConsultationService(
                consultationRepository,
                demandeExpertiseRepository,
                creneauRepository,
                userRepository,
                patientRepository,
                acteTechniqueRepository,
                fileAttenteRepository
        );
    }

    @Test
    @DisplayName("US4: Total cost calculation with Lambda map().sum() for direct care with technical acts")
    void testCalculerCoutTotalDirectCare() {
        Consultation consultation = new Consultation();
        consultation.setCoutBase(150.0);

        ActeTechnique acte1 = new ActeTechnique("Électrocardiogramme", "ECG", 150.0);
        ActeTechnique acte2 = new ActeTechnique("Radiographie", "Radio", 250.0);
        consultation.setActesTechniques(Arrays.asList(acte1, acte2));

        Double total = consultationService.calculerCoutTotal(consultation);

        // 150 (base) + 150 (ECG) + 250 (Radio) = 550.0 DH
        assertEquals(550.0, total);
    }

    @Test
    @DisplayName("US4: Total cost calculation with Lambda for tele-expertise with specialist fee and technical acts")
    void testCalculerCoutTotalWithExpertise() {
        Consultation consultation = new Consultation();
        consultation.setCoutBase(150.0);

        User specialiste = new User("cardio", "cardio@mail.com", "pass", "Tazi", "Fatima",
                Role.SPECIALISTE, Specialite.CARDIOLOGIE, 300.0);

        DemandeExpertise expertise = new DemandeExpertise(consultation, specialiste, null, "Avis ECG", PrioriteExpertise.URGENTE);
        consultation.setDemandeExpertise(expertise);

        ActeTechnique acte1 = new ActeTechnique("Échographie", "Echo", 200.0);
        consultation.setActesTechniques(List.of(acte1));

        Double total = consultationService.calculerCoutTotal(consultation);

        // 150 (base) + 300 (specialist fee) + 200 (echo) = 650.0 DH
        assertEquals(650.0, total);
    }

    @Test
    @DisplayName("US3: Stream API specialist filtering by specialty and sorting by ascending fee")
    void testGetSpecialistesFiltresEtTries() {
        User s1 = new User("cardio1", "c1@mail.com", "p", "A", "A", Role.SPECIALISTE, Specialite.CARDIOLOGIE, 350.0);
        User s2 = new User("cardio2", "c2@mail.com", "p", "B", "B", Role.SPECIALISTE, Specialite.CARDIOLOGIE, 250.0);
        User s3 = new User("dermato", "d@mail.com", "p", "C", "C", Role.SPECIALISTE, Specialite.DERMATOLOGIE, 200.0);

        when(userRepository.findByRole(Role.SPECIALISTE)).thenReturn(Arrays.asList(s1, s2, s3));

        List<User> result = consultationService.getSpecialistesFiltresEtTries(Specialite.CARDIOLOGIE, null);

        // Should contain only 2 cardiologists sorted by rate ascending: 250.0 DH, then 350.0 DH
        assertEquals(2, result.size());
        assertEquals("cardio2", result.get(0).getUsername());
        assertEquals(250.0, result.get(0).getTarifConsultation());
        assertEquals("cardio1", result.get(1).getUsername());
        assertEquals(350.0, result.get(1).getTarifConsultation());
    }
}
