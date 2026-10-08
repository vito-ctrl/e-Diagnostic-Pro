package ma.teleexpertise.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import ma.teleexpertise.model.Patient;
import ma.teleexpertise.util.JPAUtil;

import java.util.List;
import java.util.Optional;

public class PatientRepository {

    public Optional<Patient> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Patient p = em.find(Patient.class, id);
            if (p != null) {
                // Initialize lazy collection
                p.getSignesVitauxList().size();
            }
            return Optional.ofNullable(p);
        } finally {
            em.close();
        }
    }

    public Optional<Patient> findByNumSecu(String numSecu) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Patient p = em.createQuery("SELECT p FROM Patient p WHERE p.numSecu = :numSecu", Patient.class)
                    .setParameter("numSecu", numSecu.trim())
                    .getSingleResult();
            p.getSignesVitauxList().size();
            return Optional.of(p);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    public List<Patient> search(String query) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String pattern = "%" + (query != null ? query.trim().toLowerCase() : "") + "%";
            List<Patient> list = em.createQuery(
                    "SELECT DISTINCT p FROM Patient p LEFT JOIN FETCH p.signesVitauxList " +
                    "WHERE LOWER(p.nom) LIKE :pattern OR LOWER(p.prenom) LIKE :pattern OR LOWER(p.numSecu) LIKE :pattern " +
                    "ORDER BY p.nom, p.prenom", Patient.class)
                    .setParameter("pattern", pattern)
                    .getResultList();
            return list;
        } finally {
            em.close();
        }
    }

    public List<Patient> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT p FROM Patient p LEFT JOIN FETCH p.signesVitauxList " +
                    "ORDER BY p.dateEnregistrement DESC", Patient.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Patient save(Patient patient) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (patient.getId() == null) {
                em.persist(patient);
            } else {
                patient = em.merge(patient);
            }
            tx.commit();
            return patient;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
