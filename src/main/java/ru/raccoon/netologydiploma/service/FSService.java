package ru.raccoon.netologydiploma.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Limit;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.raccoon.netologydiploma.dbentities.FileInfo;
import ru.raccoon.netologydiploma.exception.BadRequestException;
import ru.raccoon.netologydiploma.exception.ISEException;
import ru.raccoon.netologydiploma.repository.FileRepository;
import ru.raccoon.netologydiploma.repository.IFileJpaRepository;
import ru.raccoon.netologydiploma.responseentities.Error;

import java.io.IOException;
import java.util.List;

/**
 * Класс, обрабатывающий запросы на работу с файлами в файловом сервисе
 */
@Service
public class FSService {

    private final IFileJpaRepository fileJpaRepository;
    private final FileRepository fileRepository;

    public FSService(IFileJpaRepository fileJpaRepository, FileRepository fileRepository) {
        this.fileJpaRepository = fileJpaRepository;
        this.fileRepository = fileRepository;
    }

    /** Метод перенаправляет запрос в репозиторий для получения списка записей с информацией о файле
     * @param limit Ограничение на количество записей в списке
     * @return Возвращает список записей с информацией о файле
     */
    public ResponseEntity<List<FileInfo>> getListWithLimit(int limit) {
        return new ResponseEntity<>(fileJpaRepository.findByOrderByFilenameAsc(Limit.of(limit)), HttpStatus.OK);
    }

    /** Метод перенаправляет запрос в репозиторий для получения информации о файле
     * @param fileName Имя файла
     * @return Возвращает информацию о файле
     */
    public FileInfo getFileInfoByFileName(String fileName) {
        return fileJpaRepository.findByFilename(fileName);
    }

    /** Метод обрабатывает запрос на загрузку файла на файловый сервис
     * @param file Файл для загрузки
     * @return Возвращает результат загрузки файла
     */
    public boolean uploadFile(@RequestPart("file") MultipartFile file) {
        try {
            //добавляем запись о файле в БД
            byte[] bytes = file.getBytes();
            fileRepository.insertWithEntityManager(new FileInfo(file.getOriginalFilename(), (int) file.getSize(), bytes, SecurityContextHolder.getContext().getAuthentication().getName()));
            //отправляем ответ, что загрузка завершена успешно
            return true;
        } catch (IOException e) {
            //если с файлом проблема, то отправляем ответ, что параметры запросы неудачные
            throw new BadRequestException(new Error(e.getMessage(), 1008));
        } catch (RuntimeException e) {
            //если проблема не в присланном файле, а в чём-то другом, то сообщаем о некоей внутренней ошибке
            throw new ISEException(new Error("Ошибка загрузки файла. Внутренняя ошибка сервера", 10018));
        }
    }

    /** Метод обрабатывает запрос на удаление файла с файлового сервиса
     * @param filename Имя файла для удаления
     * @return Возвращает результат удаления файла
     */
    public boolean deleteFile(@RequestParam String filename) {
        try {
            fileRepository.deleteWithEntityManager(getFileInfoByFileName(filename));
        } catch (Exception e) {
            throw new ISEException(new Error("Ошибка удаления файла. Внутренняя ошибка сервера", 10010));
        }
        return true;
    }

    /** Метод обрабатывает запрос на скачивание файла с файлового сервиса
     * @param filename Имя скачиваемого файла
     * @return Возвращает результат скачивания файла
     */
    public ResponseEntity<Resource> downloadFile(String filename) {
        try {
            //передадим файл клиенту
            byte[] file = getFileInfoByFileName(filename).getFile();
            Resource fileResource = new ByteArrayResource(file);

            return ResponseEntity.ok()
                    .contentLength(fileResource.contentLength())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(fileResource);
        } catch (Exception e) {
            //если при работе с файлом что-то пошло не так, то сообщаем об этом
            throw new ISEException(new Error("Ошибка скачивания файла. Внутренняя ошибка сервера", 10011));
        }
    }

    /** Метод обрабатывает запрос на переименование файла на файловом сервисе
     * @param filename Текущее имя файла
     * @param newFilename Новое имя файла
     * @return Возвращает результат переименования файла
     */
    public boolean renameFile(String filename, String newFilename) {
        try {
            fileRepository.renameWithEntityManager(getFileInfoByFileName(filename), newFilename);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new ISEException(new Error("Ошибка переименования файла. Внутренняя ошибка сервера", 10012));
        }
        return true;
    }
}
