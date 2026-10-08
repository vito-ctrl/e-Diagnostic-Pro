package ma.teleexpertise.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ma.teleexpertise.util.DatabaseSeeder;
import ma.teleexpertise.util.JPAUtil;

@WebListener
public class AppInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Tele-Expertise Medicale app starting up: seeding database if needed...");
        try {
            DatabaseSeeder.seed();
        } catch (Exception e) {
            System.err.println("Database initialization warning: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Tele-Expertise Medicale app shutting down: closing EntityManagerFactory...");
        JPAUtil.shutdown();
    }
}
