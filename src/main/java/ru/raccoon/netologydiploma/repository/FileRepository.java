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

    @Transactional
    public void insertWithEntityManager(FileInfo fileInfo) {
        this.entityManager.persist(fileInfo);
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public void deleteWithEntityManager(FileInfo fileInfo) {
        FileInfo fileInfo1 = entityManager.find(FileInfo.class, fileInfo.getFilename());
        entityManager.remove(fileInfo1);
        entityManager.flush();
        entityManager.clear();
    }
}
