package com.estate.waterbilling;

import com.estate.waterbilling.model.Manager;
import com.estate.waterbilling.repository.ManagerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Runs once on application startup.
 * Creates the default manager account if none exists yet.
 *
 * Default credentials:
 *   Username : admin
 *   Password : Nafaka2024!
 *
 * ⚠️ Change the password after first login!
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder   passwordEncoder;

    public DataSeeder(ManagerRepository managerRepository,
                      PasswordEncoder passwordEncoder) {
        this.managerRepository = managerRepository;
        this.passwordEncoder   = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (managerRepository.findByUsername("admin").isEmpty()) {
            Manager manager = new Manager(
                "admin",
                passwordEncoder.encode("Nafaka2026!")   // ← change this password!
            );
            managerRepository.save(manager);
            System.out.println("✅ Default manager account created: admin / Nafaka2024!");
        }
    }
}
