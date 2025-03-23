package xyz.adityajoshi.RssFeedSubscriber.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import xyz.adityajoshi.RssFeedSubscriber.RssFeedSubscriberApplication;

import java.time.LocalDateTime;

@Service
public class OracleDataService {
    private final JdbcTemplate jdbcTemplate;

    public OracleDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final Logger logger = LoggerFactory.getLogger(OracleDataService.class);

    public String fetchCurrentDate() {
        return jdbcTemplate.queryForObject("SELECT sysdate from dual", String.class);
    }

    public void saveFeedSource(String name, String url, String category) {
        String sql = "INSERT INTO FEED_SOURCE (NAME, URL, CATEGORY, CREATED_AT, UPDATED_AT) VALUES (?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql, name, url, category, LocalDateTime.now(), LocalDateTime.now());
            logger.info("Feed source added successfully: {}", name);
        } catch (Exception e) {
            logger.error("Error saving feed source: {}", e.getMessage(), e);
        }
    }
}
