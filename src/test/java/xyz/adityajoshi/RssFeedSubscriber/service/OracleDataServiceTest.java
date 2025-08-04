package xyz.adityajoshi.RssFeedSubscriber.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OracleDataServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private OracleDataService oracleDataService;

    @BeforeEach
    void setUp() {
        oracleDataService = new OracleDataService(jdbcTemplate);
    }

    @Test
    void fetchCurrentDate_ShouldReturnDateString() {
        // Arrange
        String expectedDate = "2024-01-15 10:30:45";
        when(jdbcTemplate.queryForObject(anyString(), eq(String.class)))
                .thenReturn(expectedDate);

        // Act
        String result = oracleDataService.fetchCurrentDate();

        // Assert
        assertNotNull(result);
        assertEquals(expectedDate, result);
        verify(jdbcTemplate, times(1)).queryForObject("SELECT sysdate from dual", String.class);
    }

    @Test
    void fetchCurrentDate_ShouldReturnNull_WhenDatabaseReturnsNull() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), eq(String.class)))
                .thenReturn(null);

        // Act
        String result = oracleDataService.fetchCurrentDate();

        // Assert
        assertNull(result);
        verify(jdbcTemplate, times(1)).queryForObject("SELECT sysdate from dual", String.class);
    }

    @Test
    void fetchCurrentDate_ShouldThrowException_WhenDatabaseThrowsException() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), eq(String.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> oracleDataService.fetchCurrentDate());
        verify(jdbcTemplate, times(1)).queryForObject("SELECT sysdate from dual", String.class);
    }

    @Test
    void saveFeedSource_ShouldExecuteSuccessfully() {
        // Arrange
        String name = "Test Feed";
        String url = "https://example.com/feed";
        String category = "Technology";

        // Act
        oracleDataService.saveFeedSource(name, url, category);

        // Assert
        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO FEED_SOURCE (NAME, URL, CATEGORY, CREATED_AT, UPDATED_AT) VALUES (?, ?, ?, ?, ?)"),
                eq(name),
                eq(url),
                eq(category),
                any(),
                any()
        );
    }

    @Test
    void saveFeedSource_ShouldHandleException_WhenDatabaseThrowsException() {
        // Arrange
        String name = "Test Feed";
        String url = "https://example.com/feed";
        String category = "Technology";

        doThrow(new RuntimeException("Database error"))
                .when(jdbcTemplate).update(anyString(), any(), any(), any(), any(), any());

        // Act & Assert - should not throw exception as it's caught internally
        assertDoesNotThrow(() -> oracleDataService.saveFeedSource(name, url, category));

        // Verify the method was still called
        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO FEED_SOURCE (NAME, URL, CATEGORY, CREATED_AT, UPDATED_AT) VALUES (?, ?, ?, ?, ?)"),
                eq(name),
                eq(url),
                eq(category),
                any(),
                any()
        );
    }

    @Test
    void saveFeedSource_ShouldHandleNullValues() {
        // Arrange
        String name = null;
        String url = null;
        String category = null;

        // Act
        oracleDataService.saveFeedSource(name, url, category);

        // Assert
        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO FEED_SOURCE (NAME, URL, CATEGORY, CREATED_AT, UPDATED_AT) VALUES (?, ?, ?, ?, ?)"),
                eq(name),
                eq(url),
                eq(category),
                any(),
                any()
        );
    }

    @Test
    void saveFeedSource_ShouldHandleEmptyStrings() {
        // Arrange
        String name = "";
        String url = "";
        String category = "";

        // Act
        oracleDataService.saveFeedSource(name, url, category);

        // Assert
        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO FEED_SOURCE (NAME, URL, CATEGORY, CREATED_AT, UPDATED_AT) VALUES (?, ?, ?, ?, ?)"),
                eq(name),
                eq(url),
                eq(category),
                any(),
                any()
        );
    }
}