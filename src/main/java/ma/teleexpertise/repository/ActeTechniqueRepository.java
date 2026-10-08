package ma.teleexpertise.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ma.teleexpertise.model.ActeTechnique;
import ma.teleexpertise.util.JPAUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ActeTechniqueRepository {

    public Optional<ActeTechnique> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(ActeTechnique.class, id));
        } finally {
            em.close();
        }
    }

    public List<ActeTechnique> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM ActeTechnique a ORDER BY a.nom", ActeTechnique.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<ActeTechnique> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM ActeTechnique a WHERE a.id IN :ids", ActeTechnique.class)
                    .setParameter("ids", ids)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public ActeTechnique save(ActeTechnique acte) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (acte.getId() == null) {
                em.persist(acte);
            } else {
                acte = em.merge(acte);
            }
            tx.commit();
            return acte;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
