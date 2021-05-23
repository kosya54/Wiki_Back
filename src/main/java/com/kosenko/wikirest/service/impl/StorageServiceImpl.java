package com.kosenko.wikirest.service.impl;

import com.kosenko.wikirest.exception.FileNotFoundException;
import com.kosenko.wikirest.exception.FileUploadException;
import com.kosenko.wikirest.service.StorageService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@Component
public class StorageServiceImpl implements StorageService {
    private final static int MAX_FILE_SIZE_IN_MB = 10;
    private final static String CONTENT_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    private final String uploadDirDoc = "F:" + File.separator + "uploads" + File.separator + "doc" + File.separator;
    private final String uploadDirImg = "F:" + File.separator + "uploads" + File.separator + "img" + File.separator;

    public StorageServiceImpl() {

    }

    private boolean isFileSizeValid(MultipartFile multipartFile) throws IOException {
        return (multipartFile.getBytes().length / 1024) / 1024 < MAX_FILE_SIZE_IN_MB;
    }

    private boolean isFileContentTypeValid(MultipartFile multipartFile) {
        return Objects.equals(multipartFile.getContentType(), CONTENT_TYPE_DOCX);
    }

    private List<String> getExistingFileNames(List<MultipartFile> multipartFiles) {
        String[] filesInDirArray = new File(uploadDirDoc).list();
        if (filesInDirArray == null) {
            return null;
        }

        List<String> filesInDir = Arrays.asList(filesInDirArray);
        List<String> existingFileNames = new ArrayList<>();
        multipartFiles.forEach(multipartFile -> {
            if (filesInDir.contains(multipartFile.getOriginalFilename())) {
                existingFileNames.add(multipartFile.getOriginalFilename());
            }
        });

        return existingFileNames;
    }

    private void createUploadDirIfNotExist(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
//TODO: Запись логов
            }
        }

        if (!dir.canWrite()) {
//TODO: запись логов
        }
    }

    private void uploadFile(MultipartFile multipartFile) {
        createUploadDirIfNotExist(uploadDirDoc);

        try {
            multipartFile.transferTo(new File(uploadDirDoc + File.separator + multipartFile.getOriginalFilename()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private List<String> getUploadedFileNames(List<MultipartFile> multipartFiles) {
        List<String> uploadedFileNames = new ArrayList<>();
        multipartFiles.forEach(multipartFile -> {
            uploadFile(multipartFile);
            uploadedFileNames.add(multipartFile.getOriginalFilename());
        });

        return uploadedFileNames;
    }

    @Override
    public List<String> upload(List<MultipartFile> multipartFiles) {
        List<String> invalidFileSizeFileNames = new ArrayList<>();
        List<String> invalidContentTypeFileNames = new ArrayList<>();
        multipartFiles.forEach(multipartFile -> {
            try {
                if (!isFileSizeValid(multipartFile)) {
                    invalidFileSizeFileNames.add(multipartFile.getOriginalFilename());
                }

                if (!isFileContentTypeValid(multipartFile)) {
                    invalidContentTypeFileNames.add(multipartFile.getOriginalFilename());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        if (!invalidFileSizeFileNames.isEmpty()) {
            String message = "Файлы превышающие допустимый размер (" + MAX_FILE_SIZE_IN_MB + "MB): ";
            throw new FileUploadException(invalidFileSizeFileNames, message);
        }

        if (!invalidContentTypeFileNames.isEmpty()) {
            String message = "Файлы c некорректным содержимым: ";
            throw new FileUploadException(invalidContentTypeFileNames, message);
        }

        List<String> existingFileNames = getExistingFileNames(multipartFiles);
        if (existingFileNames != null && !existingFileNames.isEmpty()) {
            String message = "Файлы уже имеються на сервере: ";
            throw new FileUploadException(existingFileNames, message);
        }

        return getUploadedFileNames(multipartFiles);
    }

    @Override
    public void upload(Map<String, BufferedImage> images) {
        createUploadDirIfNotExist(uploadDirImg);

        images.forEach((fileName, image) -> {
            try {
                ImageIO.write(image, "jpg", new File(uploadDirImg + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public File getFileByFileName(String fileName) {
        File file = new File(uploadDirDoc + fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("Файл " + fileName + " не найден.");
        }

        return file;
    }

    @Override
    public void deleteDocumentFileByArticleTitle(String fileName) {
        File file = new File(uploadDirDoc + fileName + ".docx");
        if (!file.exists()) {
            throw new FileNotFoundException("Файл " + fileName + " не найден.");
        }

        if (!file.delete()) {
            throw new FileNotFoundException("Немогу удалить файл " + fileName);
        }
    }

    @Override
    public void deleteImageByName(String fileName) {
        File file = new File(uploadDirImg + fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("Файл " + fileName + " не найден.");
        }

        if (!file.delete()) {
            throw new FileNotFoundException("Немогу удалить файл " + fileName);
        }
    }

/*    @Override
    public List<File> getFilesByFileNames(List<String> fileNames) {
        List<File> files = new ArrayList<>();
        fileNames.forEach(fileName -> {
            files.add(new File(uploadDirDoc + fileName));
        });

        if (files.isEmpty()) {
            throw new FilesNotFoundException(fileNames);
        }

        return files;
    } */

    @Override
    public String getPathUploadDirImg() {
        return uploadDirImg;
    }





/*    @Override
    public String getPathUploadDirDoc() {
        return uploadDirDoc;
    }

    @Override
    public String getPathUploadDirImg() {
        return uploadDirImg;
    }

    //New upload
    @Override
    public List<String> crudUpload(List<MultipartFile> files) {
        File dir = new File(uploadDirDoc);
        if (!dir.exists()) {
            dir.mkdir();
        }

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Ошибка создания каталога");
            }
        }

        if (!dir.canWrite()) {
            throw new AccessDeniedException("Отсутствуют права на запись файла в каталог " + dir.getName());
        }


        return null;
    }

    @Override
    public List<MultipartFile> upload(List<MultipartFile> files) throws IOException {
        List<MultipartFile> uploadedFiles = new ArrayList<>();

        if (!files.isEmpty() && isCanUpload(uploadDirDoc)) {
            String[] filesInDir = new File(uploadDirDoc).list();
            if (filesInDir != null && filesInDir.length > 0) {
                for (MultipartFile file : files) {
                    if (!Arrays.asList(filesInDir).contains(file.getOriginalFilename())) {
                        transferTo(file);
                        uploadedFiles.add(file);
                    }
                }
            } else {
                for (MultipartFile file : files) {
                    transferTo(file);
                    uploadedFiles.add(file);
                }
            }
        }

        return uploadedFiles;
    }

    @Override
    public void upload(Map<String, BufferedImage> images, String articleTitle) throws IOException {
        String articleDir = uploadDirImg + articleTitle;

        if (!images.isEmpty() && isCanUpload(articleDir)) {
            images.forEach((fileName, image) -> {
                try {
                    ImageIO.write(image, "jpg", new File(articleDir + File.separator + fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void upload(Map<String, BufferedImage> images) throws IOException {
        if (!images.isEmpty() && isCanUpload(uploadDirImg)) {
            images.forEach((fileName, image) -> {
                try {
                    ImageIO.write(image, "jpg", new File(uploadDirImg + fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public List<File> getFilesByName(List<String> fileNames) throws FileNotFoundException {
        File[] filesInDir = new File(uploadDirDoc).listFiles();
        if (filesInDir == null || filesInDir.length == 0) {
            throw new FileNotFoundException("Нет таких файлов");
        }

        List<File> files = new ArrayList<>();
        fileNames.forEach(fileName -> Arrays.asList(filesInDir).forEach(file -> {
            if (file.getName().equals(fileName)) {
                files.add(file);
            }
        }));

        return files;
    }

    @Override
    public File getFileByName(String fileName) throws FileNotFoundException {
        File file = new File(uploadDirDoc + fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("Нет такого файла");
        }

        return file;
    }

    @Override
    public boolean deleteDocumentFileByName(String name) {
        File file = new File(uploadDirDoc + name + ".docx");

        return file.delete();
    }

    @Override
    public void deleteImageFilesByName(List<String> imageNames) throws IOException {
        for (String name : imageNames) {
            File file = new File(uploadDirImg + name);
            if (!file.delete()) {
                throw new IOException("Ошибка удаления файла");
            }
        }
    }

    private boolean isCanUpload(String dirPath) throws IOException {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Ошибка создания каталога");
            }
        }

        if (!dir.canWrite()) {
            throw new AccessDeniedException("Отсутствуют права на запись файла в каталог " + dir.getName());
        }

        return true;
    }

     */
}
