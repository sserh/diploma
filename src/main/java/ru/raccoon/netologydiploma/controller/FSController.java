package ru.raccoon.netologydiploma.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.raccoon.netologydiploma.dbentities.FileInfo;
import ru.raccoon.netologydiploma.exception.BadRequestException;
import ru.raccoon.netologydiploma.responseentities.Error;
import ru.raccoon.netologydiploma.responseentities.FileNameEntity;
import ru.raccoon.netologydiploma.service.FSService;

import java.util.List;

/**
 * Класс-контроллер для принятия от пользователей запросов, связанных с файловым сервисом
 */
@RestController
@RequestMapping("/cloud/")
public class FSController {

    private final FSService fsService;

    public FSController(FSService fsService) {
        this.fsService = fsService;
    }

    /** Метод, принимающий и перенаправляющий в нужный сервис запрос на получение списка файлов
     * @param limit Ограничение по количеству файлов в списке файлов
     * @return Возвращает список с названиями и размерами файлов
     */
    @GetMapping("list")
    public ResponseEntity<List<FileInfo>> getListWithLimit(@RequestParam int limit) {
        if (limit < 0) {
            throw new BadRequestException(new Error("Получено неверное ограничение количества отображаемых файлов", 1003));
        }
        return new ResponseEntity<>(fsService.getListWithLimit(limit), HttpStatus.OK);
    }

    /** Метод, принимающий и перенаправляющий в нужный сервис запрос на загрузку нового файла на файловый сервис
     * @param file Загружаемый файл
     * @return Возвращает от сервиса результат загрузки файла
     */
    @PostMapping("file")
    public ResponseEntity<Void> uploadFile(@RequestPart("file") MultipartFile file) {
        return fsService.uploadFile(file) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    /** Метод, принимающий и перенаправляющий в нужный сервис запрос на удаление файла с файлового сервиса
     * @param filename Название удаляемого файла
     * @return Возвращает от сервиса результат удаления файла
     */
    @DeleteMapping("file")
    public ResponseEntity<Void> deleteFile(@RequestParam String filename) {
        return fsService.deleteFile(filename) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    /** Метод, принимающий и перенаправляющий в нужный сервис запрос на скачивание файла с файлового сервиса
     * @param filename Название скачиваемого файла
     * @return Возвращает от сервиса результат скачивания файла
     */
    @GetMapping("file")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename) {
        return fsService.downloadFile(filename);
    }

    /** Метод, принимающий и перенаправляющий в нужный сервис запрос на переименование файла на файловом сервисе
     * @param filename Текущее название файла
     * @param fileNameEntity Новое название файла
     * @return Возвращает от сервиса результат переименования файла
     */
    @PutMapping("file")
    public ResponseEntity<String> renameFile(@RequestParam String filename, @RequestBody FileNameEntity fileNameEntity) {
        return fsService.renameFile(filename, fileNameEntity.getFilename()) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
