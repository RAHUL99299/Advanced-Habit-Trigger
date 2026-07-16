package com.habittrigger.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AnalyticsJdbcDao {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsJdbcDao.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    public Map<String, Integer> getStreakData(Long habitId) {
        String sql = """
            SELECT log_date, completed
            FROM habit_logs
            WHERE habit_id = ?
            ORDER BY log_date ASC
            """;

        Map<String, Integer> result = new HashMap<>();
        result.put("currentStreak", 0);
        result.put("longestStreak", 0);

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, habitId);
            ResultSet rs = stmt.executeQuery();

            List<Object[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new Object[]{rs.getDate("log_date"), rs.getBoolean("completed")});
            }

            int currentStreak = 0;
            int longestStreak = 0;
            int runningStreak = 0;
            java.sql.Date prevDate = null;

            for (Object[] row : rows) {
                java.sql.Date logDate = (java.sql.Date) row[0];
                boolean completed = (boolean) row[1];

                if (completed) {
                    if (prevDate == null) {
                        runningStreak = 1;
                    } else {
                        long diffDays = (logDate.getTime() - prevDate.getTime()) / (1000 * 60 * 60 * 24);
                        runningStreak = (diffDays == 1) ? runningStreak + 1 : 1;
                    }
                    prevDate = logDate;
                } else {
                    runningStreak = 0;
                    prevDate = null;
                }

                longestStreak = Math.max(longestStreak, runningStreak);
            }

            currentStreak = runningStreak;
            result.put("currentStreak", currentStreak);
            result.put("longestStreak", longestStreak);

            log.debug("Streak data for habit {}: current={}, longest={}", habitId, currentStreak, longestStreak);

        } catch (SQLException e) {
            log.error("JDBC error computing streak for habit {}: {}", habitId, e.getMessage());
        }

        return result;
    }

    public Map<String, Object> getBestAndWorstTrigger(Long habitId) {
        Map<String, Object> result = new HashMap<>();

        String[] triggerTypes = {"mood", "weather", "location", "time_of_day"};
        String[] triggerLabels = {"MOOD", "WEATHER", "LOCATION", "TIME"};

        double bestRate = -1;
        double worstRate = 2;
        String bestType = null, bestValue = null;
        String worstType = null, worstValue = null;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {

            for (int i = 0; i < triggerTypes.length; i++) {
                String col = triggerTypes[i];
                String label = triggerLabels[i];

                String sql = String.format("""
                    SELECT %s AS trigger_val,
                           COUNT(*) AS total,
                           SUM(CASE WHEN completed = true THEN 1 ELSE 0 END) AS done
                    FROM habit_logs
                    WHERE habit_id = ? AND %s IS NOT NULL AND %s != ''
                    GROUP BY %s
                    HAVING COUNT(*) >= 2
                    """, col, col, col, col);

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setLong(1, habitId);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        String triggerVal = rs.getString("trigger_val");
                        long total = rs.getLong("total");
                        long done = rs.getLong("done");
                        double rate = total > 0 ? (double) done / total : 0;

                        if (rate > bestRate) {
                            bestRate = rate;
                            bestType = label;
                            bestValue = triggerVal;
                        }
                        if (rate < worstRate) {
                            worstRate = rate;
                            worstType = label;
                            worstValue = triggerVal;
                        }
                    }
                }
            }

        } catch (SQLException e) {
            log.error("JDBC error finding best/worst trigger for habit {}: {}", habitId, e.getMessage());
        }

        result.put("bestTriggerType", bestType);
        result.put("bestTriggerValue", bestValue);
        result.put("bestRate", bestRate >= 0 ? bestRate : null);
        result.put("worstTriggerType", worstType);
        result.put("worstTriggerValue", worstValue);
        result.put("worstRate", worstRate <= 1 ? worstRate : null);

        return result;
    }

    public Map<String, Object> getUserCompletionStats(Long userId) {
        String sql = """
            SELECT
                COUNT(*) AS total_logs,
                SUM(CASE WHEN hl.completed = true THEN 1 ELSE 0 END) AS completed_logs
            FROM habit_logs hl
            INNER JOIN habits h ON hl.habit_id = h.id
            WHERE h.user_id = ?
            """;

        Map<String, Object> stats = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long total = rs.getLong("total_logs");
                long completed = rs.getLong("completed_logs");
                double rate = total > 0 ? (double) completed / total : 0;

                stats.put("totalLogs", total);
                stats.put("completedLogs", completed);
                stats.put("completionRate", Math.round(rate * 100));
            }

        } catch (SQLException e) {
            log.error("JDBC error fetching user stats for user {}: {}", userId, e.getMessage());
        }

        return stats;
    }
}
