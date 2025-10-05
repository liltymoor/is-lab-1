package com.crudoshlep.islab1.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class JpaProducer {

    private final EntityManagerFactory entityManagerFactory = createEntityManagerFactory();

    private EntityManagerFactory createEntityManagerFactory() {
        try {
            // Ensure JDBC driver is registered in environments where auto-loading may not occur
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) {
            // Driver auto-registration via ServiceLoader should work if present on classpath
        }

        Map<String, Object> overrides = new HashMap<>();

        // не воруйте мои креды это флекс (haggiwaggi)
        String url = "jdbc:postgresql://localhost:5433/studs"; // todo 5432 deploy port
        String user = "s413105";
        String password = "r5BklO7TucYga3Oe";

        overrides.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
        overrides.put("jakarta.persistence.jdbc.password", password);


        if (url != null && !url.isBlank()) {
            overrides.put("jakarta.persistence.jdbc.url", url);
        }
        if (user != null && !user.isBlank()) {
            overrides.put("jakarta.persistence.jdbc.user", user);
        }
        if (password != null && !password.isBlank()) {
            overrides.put("jakarta.persistence.jdbc.password", password);
        }

        System.out.println(overrides);

        return overrides.isEmpty()
                ? Persistence.createEntityManagerFactory("default")
                : Persistence.createEntityManagerFactory("default", overrides);
    }

    @Produces
    @ApplicationScoped
    public EntityManagerFactory produceEntityManagerFactory() {
        return entityManagerFactory;
    }

    @Produces
    @RequestScoped
    public EntityManager produceEntityManager(EntityManagerFactory emf) {
        return emf.createEntityManager();
    }

    public void closeEntityManager(@Disposes EntityManager entityManager) {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}


