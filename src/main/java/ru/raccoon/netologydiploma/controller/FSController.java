package ru.raccoon.netologydiploma.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.raccoon.netologydiploma.dbentities.FileInfo;
import ru.raccoon.netologydiploma.exception.BadRequestException;
import ru.raccoon.netologydiploma.responseentities.Error;
import ru.raccoon.netologydiploma.responseentities.FileNameEntity;
import ru.raccoon.netologydiploma.service.FSService;

import java.util.List;

@RestController
@RequestMapping("/cloud/")
public class FSController {


    private final FSService fsService;

    public FSController(FSService fsService) {
        this.fsService = fsService;
    }

    @GetMapping("list")
    public ResponseEntity<List<FileInfo>> getListWithLimit(@RequestParam int limit) {
        if (limit < 0) {
            throw new BadRequestException(new Error("Получено неверное ограничение количества отображаемых файлов", 1003));
        }
        return fsService.getListWithLimit(limit);
    }

    @PostMapping("file")
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) {
        return fsService.uploadFile(file);
    }

    @DeleteMapping("file")
    public ResponseEntity<String> deleteFile(@RequestParam String filename) {
        return fsService.deleteFile(filename);
    }

    @GetMapping("file")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename) {
        return fsService.downloadFile(filename);
    }

    @PutMapping("file")
    public ResponseEntity<String> renameFile(@RequestParam String filename, @RequestBody FileNameEntity fileNameEntity) {
        return fsService.renameFile(filename, fileNameEntity.getFilename());
    }
}
