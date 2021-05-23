package com.kosenko.wikirest.exception;

import java.util.List;

public class FilesNotFoundException extends RuntimeException {
    private List<String> fileNames;

    public FilesNotFoundException(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    @Override
    public String getMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Файлы: ");

        for (int i = 0; i < fileNames.size(); i++) {
            stringBuilder.append(fileNames.get(i));

            if (!(i == fileNames.size() - 1)) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append(" не найдены, загрузите сново.");

        return stringBuilder.toString();
    }
}
