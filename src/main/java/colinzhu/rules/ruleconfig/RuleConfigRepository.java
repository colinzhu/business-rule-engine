package colinzhu.rules.ruleconfig;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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
        URL classpathFileUrl = ClassLoader.getSystemResource("rule-config/" + name);
        if (classpathFileUrl != null) {
            return Optional.of(getRuleConfigFromClasspath(classpathFileUrl));
        }
        log.info("Get RuleConfig from DB, Name: " + name);
        RuleConfig ruleConfig = entityManager.createQuery("SELECT c FROM RuleConfig c WHERE c.name = :name", RuleConfig.class)
                .setParameter("name", name)
                .getSingleResult();
        return ruleConfig != null ? Optional.of(ruleConfig) : Optional.empty();
    }

    public Optional<RuleConfig> findByNameAndUpdateTimeGreaterThan(String name, Long updateTime) {
        RuleConfig ruleConfig = entityManager.createQuery("SELECT c FROM RuleConfig c WHERE c.name = :name and c.updateTime > :updateTime", RuleConfig.class)
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

    @SneakyThrows
    private RuleConfig getRuleConfigFromClasspath(URL classpathFileUrl) {
        String path = classpathFileUrl.getFile();
        log.info("Get RuleConfig from classpath: " + path);
        Path filePath = Path.of(classpathFileUrl.toURI());
        String json = Files.readString(filePath);
        log.info(path + ":\n" + json);

        RuleConfig ruleConfig = new RuleConfig();
        ruleConfig.setName(path.substring(path.lastIndexOf('/') + 1));
        ruleConfig.setContent(json);
        ruleConfig.setUpdateTime(Files.getLastModifiedTime(filePath).toMillis());
        return ruleConfig;
    }

}
