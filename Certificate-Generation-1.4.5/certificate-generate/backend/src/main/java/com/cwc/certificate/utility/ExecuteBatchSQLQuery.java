package com.cwc.certificate.utility;

import com.cwc.certificate.config.ConstantValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/09/07
 */

@Component
@Slf4j
public class ExecuteBatchSQLQuery {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExecuteBatchSQLQuery(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void runSqlScripts() {
        if (!checkIfTableExists("BATCH_JOB_INSTANCE")) {
            executeSqlScript();
        } else {
            log.info("Tables already exist. Skipping creation.");
        }
    }

    private boolean checkIfTableExists(String tableName) {
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{tableName.toLowerCase()}, Integer.class);
        return count != null && count > 0;
    }

    private void executeSqlScript() {
        String[] sqlStatements = ConstantValue.DEFAULT_BATCH_SQL_TABLE_QUERY;

        for (String sql : sqlStatements) {
            try {
                jdbcTemplate.execute(sql);
                log.info("Executed: " + sql);
            } catch (Exception e) {
                log.error("Error executing SQL: " + sql + " - " + e.getMessage());
            }
        }
    }
}
