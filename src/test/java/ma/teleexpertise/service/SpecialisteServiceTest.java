package ma.teleexpertise.service;

import ma.teleexpertise.model.Consultation;
import ma.teleexpertise.model.DemandeExpertise;
import ma.teleexpertise.model.Patient;
import ma.teleexpertise.model.User;
import ma.teleexpertise.model.enums.PrioriteExpertise;
import ma.teleexpertise.model.enums.Role;
import ma.teleexpertise.model.enums.Specialite;
import ma.teleexpertise.model.enums.StatutExpertise;
import ma.teleexpertise.repository.ConsultationRepository;
import ma.teleexpertise.repository.CreneauRepository;
import ma.teleexpertise.repository.DemandeExpertiseRepository;
import ma.teleexpertise.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpecialisteServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CreneauRepository creneauRepository;

    @Mock
    private DemandeExpertiseRepository demandeExpertiseRepository;

    @Mock
    private ConsultationRepository consultationRepository;

    private SpecialisteService specialisteService;

    @BeforeEach
    void setUp() {
        specialisteService = new SpecialisteService(
                userRepository,
                creneauRepository,
                demandeExpertiseRepository,
                consultationRepository
        );
    }

    @Test
    @DisplayName("US7: Stream API filtering of expertise requests by status and priority")
    void testGetDemandesExpertiseStreamApiFiltering() {
        Long specialisteId = 10L;

        User spec = new User("cardio", "c@m.com", "p", "Dr", "Cardio", Role.SPECIALISTE, Specialite.CARDIOLOGIE, 300.0);
        spec.setId(specialisteId);

        Consultation c1 = new Consultation();
        c1.setPatient(new Patient("P1", "A", "1990", "SEC1", null, null, null, null, null, null));
        DemandeExpertise d1 = new DemandeExpertise(c1, spec, null, "Question 1", PrioriteExpertise.URGENTE);
        d1.setStatut(StatutExpertise.EN_ATTENTE);

        Consultation c2 = new Consultation();
        c2.setPatient(new Patient("P2", "B", "1991", "SEC2", null, null, null, null, null, null));
        DemandeExpertise d2 = new DemandeExpertise(c2, spec, null, "Question 2", PrioriteExpertise.NORMALE);
        d2.setStatut(StatutExpertise.EN_ATTENTE);

        Consultation c3 = new Consultation();
        c3.setPatient(new Patient("P3", "C", "1992", "SEC3", null, null, null, null, null, null));
        DemandeExpertise d3 = new DemandeExpertise(c3, spec, null, "Question 3", PrioriteExpertise.URGENTE);
        d3.setStatut(StatutExpertise.TERMINEE);

        when(demandeExpertiseRepository.findBySpecialiste(specialisteId))
                .thenReturn(Arrays.asList(d1, d2, d3));

        // Filter by statut EN_ATTENTE only -> d1 and d2
        List<DemandeExpertise> attenteList = specialisteService.getDemandesExpertise(specialisteId, StatutExpertise.EN_ATTENTE, null);
        assertEquals(2, attenteList.size());

        // Filter by statut EN_ATTENTE and priorite URGENTE -> only d1
        List<DemandeExpertise> urgenteAttenteList = specialisteService.getDemandesExpertise(specialisteId, StatutExpertise.EN_ATTENTE, PrioriteExpertise.URGENTE);
        assertEquals(1, urgenteAttenteList.size());
        assertEquals("Question 1", urgenteAttenteList.get(0).getQuestion());

        // Filter by statut TERMINEE -> only d3
        List<DemandeExpertise> termineeList = specialisteService.getDemandesExpertise(specialisteId, StatutExpertise.TERMINEE, null);
        assertEquals(1, termineeList.size());
        assertEquals("Question 3", termineeList.get(0).getQuestion());
    }
}
