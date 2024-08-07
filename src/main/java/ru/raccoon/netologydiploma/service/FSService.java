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
import ru.raccoon.netologydiploma.exception.ISEException;
import ru.raccoon.netologydiploma.repository.FileRepository;
import ru.raccoon.netologydiploma.repository.IFileJpaRepository;
import ru.raccoon.netologydiploma.responseentities.Error;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Класс, обрабатывающий запросы на работу с файлами в файловом сервисе
 */
@Service
public class FSService {

    private final IFileJpaRepository fileJpaRepository;
    private final FileRepository fileRepository;

    private final String UPLOADED_FOLDER = "files\\";

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
            //"копируем" файл в место хранения файла
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            //добавляем запись о файле в БД
            fileRepository.insertWithEntityManager(new FileInfo(file.getOriginalFilename(), (int) file.getSize()));
            //отправляем ответ, что загрузка завершена успешно
            return true;
        } catch (IOException e) {
            //если с файлом проблема, то отправляем ответ, что параметры запросы неудачные
            throw new BadRequestException(new Error(e.getMessage(), 1008));
        } catch (RuntimeException e) {
            //если проблема не в присланном файле, а в чём-то другом, то сообщаем о некоей внутренней ошибке
            throw new ISEException(new Error("Ошибка удаления файла. Внутренняя ошибка сервера", 10018));
        }
    }

    /** Метод обрабатывает запрос на удаление файла с файлового сервиса
     * @param filename Имя файла для удаления
     * @return Возвращает результат удаления файла
     */
    public boolean deleteFile(@RequestParam String filename) {
        File file = new File(UPLOADED_FOLDER + filename);
        if (!file.exists()) {
            //если файл, указанный для удаления, не найден, то отправляем ответ, что параметры запросы неудачные
            throw new BadRequestException(new Error("Запрошенный для удаления файл не найден", 10020));
        }
        if (file.delete()) {
            //если файл успешно удалён с диска, то удаляем информацию о нём в БД
            fileRepository.deleteWithEntityManager(getFileInfoByFileName(filename));
            return true;
        } else {
            //если с удалением какие-то проблемы, то сообщаем об этом
            throw new ISEException(new Error("Ошибка удаления файла. Внутренняя ошибка сервера", 10010));
        }
    }

    /** Метод обрабатывает запрос на скачивание файла с файлового сервиса
     * @param filename Имя скачиваемого файла
     * @return Возвращает результат скачивания файла
     */
    public ResponseEntity<Resource> downloadFile(String filename) {
        try {
            Path path = Paths.get(UPLOADED_FOLDER + filename);
            if (!path.toFile().exists()) {
                //если файл, указанный для скачивания, не найден, то отправляем ответ, что параметры запросы неудачные
                throw new BadRequestException(new Error("Запрошенный для скачивания файл не найден", 10021));
            }
            //передадим файл клиенту
            Resource fileResource = new FileSystemResource(path);
            return ResponseEntity.ok()
                    .contentLength(path.toFile().length())
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
        File file = new File(UPLOADED_FOLDER + filename);
        if (!file.exists()) {
            //если файл, указанный для переименования, не найден, то отправляем ответ, что параметры запросы неудачные
            throw new BadRequestException(new Error("Запрошенный для переименования файл не найден", 10022));
        }
        if (file.renameTo(new File(UPLOADED_FOLDER + newFilename))) {
            //если файл на диске успешно переименован, то обновляем информацию о файле в БД
            fileRepository.deleteWithEntityManager(getFileInfoByFileName(filename));
            fileRepository.insertWithEntityManager(new FileInfo(newFilename, (int) file.length()));
            return true;
        } else {
            //если при работе с файлом что-то пошло не так, то сообщаем об этом
            throw new ISEException(new Error("Ошибка переименования файла. Внутренняя ошибка сервера", 10012));
        }
    }
}
