package ma.teleexpertise.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ma.teleexpertise.model.Creneau;
import ma.teleexpertise.model.enums.StatutCreneau;
import ma.teleexpertise.util.JPAUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class CreneauRepository {

    public Optional<Creneau> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Creneau.class, id));
        } finally {
            em.close();
        }
    }

    public List<Creneau> findBySpecialiste(Long specialisteId) {
        archivePastSlots(specialisteId);
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Creneau c WHERE c.specialiste.id = :sid ORDER BY c.date ASC, c.heureDebut ASC", Creneau.class)
                    .setParameter("sid", specialisteId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Creneau> findBySpecialisteAndDate(Long specialisteId, LocalDate date) {
        archivePastSlots(specialisteId);
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Creneau c WHERE c.specialiste.id = :sid AND c.date = :date ORDER BY c.heureDebut ASC", Creneau.class)
                    .setParameter("sid", specialisteId)
                    .setParameter("date", date)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Creneau> findAvailableBySpecialiste(Long specialisteId) {
        archivePastSlots(specialisteId);
        EntityManager em = JPAUtil.getEntityManager();
        try {
            LocalDate today = LocalDate.now();
            LocalTime nowTime = LocalTime.now();
            return em.createQuery(
                    "SELECT c FROM Creneau c WHERE c.specialiste.id = :sid AND c.statut = :statut " +
                    "AND (c.date > :today OR (c.date = :today AND c.heureDebut >= :nowTime)) " +
                    "ORDER BY c.date ASC, c.heureDebut ASC", Creneau.class)
                    .setParameter("sid", specialisteId)
                    .setParameter("statut", StatutCreneau.DISPONIBLE)
                    .setParameter("today", today)
                    .setParameter("nowTime", nowTime)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void archivePastSlots(Long specialisteId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            LocalDate today = LocalDate.now();
            LocalTime nowTime = LocalTime.now();
            em.createQuery(
                    "UPDATE Creneau c SET c.statut = :archiveStatut " +
                    "WHERE c.specialiste.id = :sid AND c.statut != :archiveStatut " +
                    "AND (c.date < :today OR (c.date = :today AND c.heureFin < :nowTime))")
                    .setParameter("archiveStatut", StatutCreneau.ARCHIVE)
                    .setParameter("sid", specialisteId)
                    .setParameter("today", today)
                    .setParameter("nowTime", nowTime)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
        } finally {
            em.close();
        }
    }

    public Creneau save(Creneau creneau) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (creneau.getId() == null) {
                em.persist(creneau);
            } else {
                creneau = em.merge(creneau);
            }
            tx.commit();
            return creneau;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void updateStatut(Long id, StatutCreneau statut) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Creneau c = em.find(Creneau.class, id);
            if (c != null) {
                c.setStatut(statut);
                em.merge(c);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
