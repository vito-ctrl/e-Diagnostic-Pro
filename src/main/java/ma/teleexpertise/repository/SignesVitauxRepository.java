package ma.teleexpertise.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ma.teleexpertise.model.SignesVitaux;
import ma.teleexpertise.util.JPAUtil;

import java.util.List;
import java.util.Optional;

public class SignesVitauxRepository {

    public Optional<SignesVitaux> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(SignesVitaux.class, id));
        } finally {
            em.close();
        }
    }

    public List<SignesVitaux> findByPatientId(Long patientId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT s FROM SignesVitaux s WHERE s.patient.id = :pid ORDER BY s.datePrise DESC", SignesVitaux.class)
                    .setParameter("pid", patientId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public SignesVitaux save(SignesVitaux signes) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (signes.getId() == null) {
                em.persist(signes);
            } else {
                signes = em.merge(signes);
            }
            tx.commit();
            return signes;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
