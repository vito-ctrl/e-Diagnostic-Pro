package ma.teleexpertise.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ma.teleexpertise.model.Consultation;
import ma.teleexpertise.model.enums.StatutConsultation;
import ma.teleexpertise.util.JPAUtil;

import java.util.List;
import java.util.Optional;

public class ConsultationRepository {

    public Optional<Consultation> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Consultation c = em.find(Consultation.class, id);
            if (c != null) {
                c.getActesTechniques().size();
                if (c.getDemandeExpertise() != null) {
                    c.getDemandeExpertise().getId();
                }
            }
            return Optional.ofNullable(c);
        } finally {
            em.close();
        }
    }

    public List<Consultation> findByGeneraliste(Long generalisteId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT c FROM Consultation c LEFT JOIN FETCH c.actesTechniques " +
                    "WHERE c.generaliste.id = :gid ORDER BY c.dateConsultation DESC", Consultation.class)
                    .setParameter("gid", generalisteId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Consultation> findByPatient(Long patientId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT c FROM Consultation c LEFT JOIN FETCH c.actesTechniques " +
                    "WHERE c.patient.id = :pid ORDER BY c.dateConsultation DESC", Consultation.class)
                    .setParameter("pid", patientId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Consultation> findByStatut(StatutConsultation statut) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT c FROM Consultation c WHERE c.statut = :statut ORDER BY c.dateConsultation DESC", Consultation.class)
                    .setParameter("statut", statut)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Consultation save(Consultation consultation) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (consultation.getId() == null) {
                em.persist(consultation);
            } else {
                consultation = em.merge(consultation);
            }
            tx.commit();
            return consultation;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
