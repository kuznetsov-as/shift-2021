package ftc.shift.sample.service;

import ftc.shift.sample.entity.ExpiringIDs;
import ftc.shift.sample.repository.ExpiringIDsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Service
@EnableScheduling
public class ExpiringService {

    private final ExpiringIDsRepository expiringIDsRepository;

    @Autowired
    public ExpiringService(ExpiringIDsRepository expiringIDsRepository) {
        this.expiringIDsRepository = expiringIDsRepository;
    }

    @Scheduled(cron = "${cron.config}")
    public void Update() {
        try { expiringIDsRepository.update(); }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}