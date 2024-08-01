package ru.raccoon.netologydiploma.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Limit;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.raccoon.netologydiploma.dbentities.FileInfo;
import ru.raccoon.netologydiploma.exception.BadRequestException;
import ru.raccoon.netologydiploma.repository.FileRepository;
import ru.raccoon.netologydiploma.repository.IFileJpaRepository;
import ru.raccoon.netologydiploma.responseentities.Error;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FSService {

    private final IFileJpaRepository fileJpaRepository;
    private final FileRepository fileRepository;

    private final String UPLOADED_FOLDER = "files\\";

    public FSService(IFileJpaRepository fileJpaRepository, FileRepository fileRepository) {
        this.fileJpaRepository = fileJpaRepository;
        this.fileRepository = fileRepository;
    }

    public ResponseEntity<List<FileInfo>> getListWithLimit(int limit) {
        return new ResponseEntity<>(fileJpaRepository.findByOrderByFilenameAsc(Limit.of(limit)), HttpStatus.OK);
    }

    public FileInfo getFileInfoByFileName(String fileName) {
        return fileJpaRepository.findByFilename(fileName);
    }

    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            fileRepository.insertWithEntityManager(new FileInfo(file.getOriginalFilename(), (int) file.getSize()));
            return ResponseEntity.ok("Upload:ok");
        } catch (IOException e) {
            throw new BadRequestException(new Error(e.getMessage(), 1008));
        }
    }

    public ResponseEntity<String> deleteFile(@RequestParam String filename) {
        fileRepository.deleteWithEntityManager(getFileInfoByFileName(filename));
        File file = new File(UPLOADED_FOLDER + filename);
        file.delete();
        return ResponseEntity.ok("Delete:ok");
    }

    public ResponseEntity<Resource> downloadFile(String filename) {
        Path path = Paths.get(UPLOADED_FOLDER + filename);
        Resource fileResource = new FileSystemResource(path);
        return ResponseEntity.ok()
                .contentLength(path.toFile().length())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(fileResource);
    }

    public ResponseEntity<String> renameFile(String filename, String newFilename) {
        File file = new File(UPLOADED_FOLDER + filename);
        file.renameTo(new File(UPLOADED_FOLDER + newFilename));
        fileRepository.deleteWithEntityManager(getFileInfoByFileName(filename));
        fileRepository.insertWithEntityManager(new FileInfo(newFilename, (int) file.length()));
        return ResponseEntity.ok("Rename:ok");
    }
}
