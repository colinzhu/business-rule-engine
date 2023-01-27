package colinzhu.rules.core;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class RuleConfigRepository {

    private final EntityManager entityManager;

    public Optional<RuleConfig> findById(Integer id) {
        RuleConfig entity = entityManager.find(RuleConfig.class, id);
        return entity != null ? Optional.of(entity) : Optional.empty();
    }

    public List<RuleConfig> findAll() {
        return entityManager.createQuery("from RuleConfig").getResultList();
    }

    public Optional<RuleConfig> findByName(String name) {
        RuleConfig ruleConfig = entityManager.createQuery("SELECT c FROM RuleConfig c WHERE c.name = :name", RuleConfig.class)
                .setParameter("name", name)
                .getSingleResult();
        return ruleConfig != null ? Optional.of(ruleConfig) : Optional.empty();
    }

    public Optional<RuleConfig> save(RuleConfig ruleConfig) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(ruleConfig);
            entityManager.getTransaction().commit();
            return Optional.of(ruleConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
