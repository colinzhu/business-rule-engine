package colinzhu.config;

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
public class ConfigRepository {

    private final EntityManager entityManager;

    public Optional<Config> findById(Integer id) {
        Config entity = entityManager.find(Config.class, id);
        return entity != null ? Optional.of(entity) : Optional.empty();
    }

    public List<Config> findAll() {
        return entityManager.createQuery("from Config").getResultList();
    }

    public Optional<Config> findByName(String name) {
        URL classpathFileUrl = ClassLoader.getSystemResource("rule-config/" + name);
        if (classpathFileUrl != null) {
            return Optional.of(getConfigFromFile(classpathFileUrl));
        }
        log.info("Get Config from DB, Name: " + name);
        Config config = entityManager.createQuery("SELECT c FROM Config c WHERE c.name = :name", Config.class)
                .setParameter("name", name)
                .getSingleResult();
        return config != null ? Optional.of(config) : Optional.empty();
    }

    public Optional<Config> findByNameAndUpdateTimeGreaterThan(String name, Long updateTime) {
        Config config = entityManager.createQuery("SELECT c FROM Config c WHERE c.name = :name and c.updateTime > :updateTime", Config.class)
                .setParameter("name", name)
                .getSingleResult();
        return config != null ? Optional.of(config) : Optional.empty();
    }

    public Optional<Config> save(Config config) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(config);
            entityManager.getTransaction().commit();
            return Optional.of(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @SneakyThrows
    private Config getConfigFromFile(URL fileUrl) {
        String path = fileUrl.getFile();
        log.info("Get Config from classpath: " + path);
        Path filePath = Path.of(fileUrl.toURI());
        String json = Files.readString(filePath);
        log.info(path + ":\n" + json);

        Config config = new Config();
        config.setName(path.substring(path.lastIndexOf('/') + 1));
        config.setContent(json);
        config.setUpdateTime(Files.getLastModifiedTime(filePath).toMillis());
        return config;
    }

}
