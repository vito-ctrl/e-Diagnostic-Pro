package ma.teleexpertise.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ma.teleexpertise.model.FileAttente;
import ma.teleexpertise.model.enums.StatutFileAttente;
import ma.teleexpertise.util.JPAUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class FileAttenteRepository {

    public Optional<FileAttente> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(FileAttente.class, id));
        } finally {
            em.close();
        }
    }

    public List<FileAttente> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT f FROM FileAttente f ORDER BY f.heureArrivee ASC", FileAttente.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<FileAttente> findByStatut(StatutFileAttente statut) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT f FROM FileAttente f WHERE f.statut = :statut ORDER BY f.heureArrivee ASC", FileAttente.class)
                    .setParameter("statut", statut)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<FileAttente> findByDate(LocalDate date) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(LocalTime.MAX);
            return em.createQuery("SELECT f FROM FileAttente f WHERE f.heureArrivee BETWEEN :start AND :end ORDER BY f.heureArrivee ASC", FileAttente.class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public FileAttente save(FileAttente item) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (item.getId() == null) {
                em.persist(item);
            } else {
                item = em.merge(item);
            }
            tx.commit();
            return item;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void updateStatut(Long id, StatutFileAttente statut) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            FileAttente item = em.find(FileAttente.class, id);
            if (item != null) {
                item.setStatut(statut);
                em.merge(item);
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
