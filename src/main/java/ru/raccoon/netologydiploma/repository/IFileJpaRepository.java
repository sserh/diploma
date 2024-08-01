package ru.raccoon.netologydiploma.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.raccoon.netologydiploma.dbentities.FileInfo;

import java.util.List;

@Repository
public interface IFileJpaRepository extends JpaRepository<FileInfo, Long> {

    List<FileInfo> findByOrderByFilenameAsc(Limit limit);

    FileInfo findByFilename(@NotNull String filename);

}
