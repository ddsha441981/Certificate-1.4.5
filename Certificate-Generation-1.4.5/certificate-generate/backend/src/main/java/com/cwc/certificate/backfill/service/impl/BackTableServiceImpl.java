package com.cwc.certificate.backfill.service.impl;

import com.cwc.certificate.backfill.service.BackTableService;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/12/16
 */

@Service
public class BackTableServiceImpl implements BackTableService {


    private final EntityManager entityManager;


    public BackTableServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public void backfillEntity(String entityName, String columnName, Object newValue, int batchSize) {
        try {
            Class<?> entityClass = Class.forName(entityName);

            int offset = 0;
            List<?> records;
            do {
                records = entityManager.createQuery("FROM " + entityClass.getSimpleName(), entityClass)
                        .setFirstResult(offset)
                        .setMaxResults(batchSize)
                        .getResultList();

                for (Object record : records) {
                    Field field = entityClass.getDeclaredField(columnName);
                    field.setAccessible(true);


                    if (field.getType() == String.class) {
                        field.set(record, newValue.toString());
                    } else if (field.getType() == Integer.class) {
                        field.set(record, Integer.valueOf(newValue.toString()));
                    } else if (field.getType() == LocalDateTime.class) {
                        field.set(record, LocalDateTime.parse(newValue.toString()));
                    } else {
                        throw new RuntimeException("Unsupported field type: " + field.getType());
                    }
                }

                entityManager.flush();
                offset += batchSize;
            } while (!records.isEmpty());

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Entity not found: " + entityName, e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field not found: " + columnName, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access field: " + columnName, e);
        }
    }
}
