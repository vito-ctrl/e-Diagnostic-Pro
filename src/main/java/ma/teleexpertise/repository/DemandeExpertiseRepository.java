package ma.teleexpertise.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ma.teleexpertise.model.DemandeExpertise;
import ma.teleexpertise.util.JPAUtil;

import java.util.List;
import java.util.Optional;

public class DemandeExpertiseRepository {

    public Optional<DemandeExpertise> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            DemandeExpertise de = em.find(DemandeExpertise.class, id);
            return Optional.ofNullable(de);
        } finally {
            em.close();
        }
    }

    public List<DemandeExpertise> findBySpecialiste(Long specialisteId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT de FROM DemandeExpertise de " +
                    "JOIN FETCH de.consultation c " +
                    "JOIN FETCH c.patient p " +
                    "WHERE de.specialiste.id = :sid ORDER BY de.dateDemande DESC", DemandeExpertise.class)
                    .setParameter("sid", specialisteId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<DemandeExpertise> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT de FROM DemandeExpertise de " +
                    "JOIN FETCH de.consultation c " +
                    "JOIN FETCH c.patient p " +
                    "ORDER BY de.dateDemande DESC", DemandeExpertise.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public DemandeExpertise save(DemandeExpertise demande) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (demande.getId() == null) {
                em.persist(demande);
            } else {
                demande = em.merge(demande);
            }
            tx.commit();
            return demande;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
