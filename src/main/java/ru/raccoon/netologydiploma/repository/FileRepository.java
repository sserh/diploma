package ru.raccoon.netologydiploma.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ru.raccoon.netologydiploma.dbentities.FileInfo;

@Repository
public class FileRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public FileRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /** Метод для добавления в БД новой информации о файле
     * @param fileInfo Информация о файле
     */
    @Transactional
    public void insertWithEntityManager(FileInfo fileInfo) {
        this.entityManager.persist(fileInfo);
        flushAndClearEntity();
    }

    /** Метод для удаления из БД информации о файле
     * @param fileInfo Информация о файле
     */
    @Transactional
    public void deleteWithEntityManager(FileInfo fileInfo) {
        FileInfo fileInfo1 = entityManager.find(FileInfo.class, fileInfo.getFilename());
        entityManager.remove(fileInfo1);
        flushAndClearEntity();
    }

    /**
     * Метод для очистки менеджера данных
     */
    private void flushAndClearEntity() {
        entityManager.flush();
        entityManager.clear();
    }
}
