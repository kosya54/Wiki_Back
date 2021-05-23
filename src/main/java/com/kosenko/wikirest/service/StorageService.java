package com.kosenko.wikirest.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public interface StorageService {
    List<String> upload(List<MultipartFile> files);

    void upload(Map<String, BufferedImage> images);

    File getFileByFileName(String name);

    void deleteDocumentFileByArticleTitle(String title);

    void deleteImageByName(String fileName);

    String getPathUploadDirImg();

//    List<File> getFilesByFileNames(List<String> fileNames);

//Old
//    String getPathUploadDirDoc();



/*    List<MultipartFile> upload(List<MultipartFile> files) throws IOException;

    List<String> crudUpload(List<MultipartFile> files);

    void upload(Map<String, BufferedImage> images) throws IOException;

//    void upload(Map<String, BufferedImage> images, String articleTitle) throws IOException;

    boolean deleteDocumentFileByName(String name);

    void deleteImageFilesByName(List<String> imageNames) throws IOException;

    List<File> getFilesByName(List<String> fileNames) throws FileNotFoundException;

    File getFileByName(String fileName) throws FileNotFoundException; */
}