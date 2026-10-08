package ma.teleexpertise.service;

import ma.teleexpertise.model.FileAttente;
import ma.teleexpertise.model.Patient;
import ma.teleexpertise.model.SignesVitaux;
import ma.teleexpertise.repository.FileAttenteRepository;
import ma.teleexpertise.repository.PatientRepository;
import ma.teleexpertise.repository.SignesVitauxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private SignesVitauxRepository signesVitauxRepository;

    @Mock
    private FileAttenteRepository fileAttenteRepository;

    private PatientService patientService;

    @BeforeEach
    void setUp() {
        patientService = new PatientService(patientRepository, signesVitauxRepository, fileAttenteRepository);
    }

    @Test
    @DisplayName("US2: Filter patients of the day and sort from oldest to newest arrival with Stream API")
    void testGetPatientsDuJourStreamApi() {
        LocalDate today = LocalDate.of(2026, 10, 8);
        LocalDate yesterday = today.minusDays(1);

        Patient p1 = new Patient("Alami", "Ahmed", "1995-01-01", "SEC1", null, null, null, null, null, null);
        Patient p2 = new Patient("Bennani", "Sara", "1998-02-02", "SEC2", null, null, null, null, null, null);
        Patient p3 = new Patient("Chraibi", "Omar", "1985-03-03", "SEC3", null, null, null, null, null, null);

        // Arrival 10:30 today
        FileAttente item1 = new FileAttente(p1, new SignesVitaux());
        item1.setHeureArrivee(LocalDateTime.of(2026, 10, 8, 10, 30));

        // Arrival 09:15 today (arrived earlier!)
        FileAttente item2 = new FileAttente(p2, new SignesVitaux());
        item2.setHeureArrivee(LocalDateTime.of(2026, 10, 8, 9, 15));

        // Arrival yesterday (should be filtered out by date)
        FileAttente item3 = new FileAttente(p3, new SignesVitaux());
        item3.setHeureArrivee(LocalDateTime.of(2026, 10, 7, 14, 0));

        when(fileAttenteRepository.findAll()).thenReturn(Arrays.asList(item1, item2, item3));

        List<FileAttente> result = patientService.getPatientsDuJour(today);

        // Only 2 patients for today, ordered by arrival: item2 (09:15) first, then item1 (10:30)
        assertEquals(2, result.size());
        assertEquals("Bennani", result.get(0).getPatient().getNom());
        assertEquals("Alami", result.get(1).getPatient().getNom());
    }
}
