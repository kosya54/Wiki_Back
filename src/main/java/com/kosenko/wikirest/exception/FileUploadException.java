package com.kosenko.wikirest.exception;

import java.util.List;

public class FileUploadException extends RuntimeException {
    private final List<String> invalidFileNames;
    private final String message;

    public FileUploadException(List<String> invalidFileNames, String message) {
        this.invalidFileNames = invalidFileNames;
        this.message = message;
    }

    public List<String> getInvalidFileNames() {
        return invalidFileNames;
    }

    @Override
    public String getMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(message);

        for (int i = 0; i < invalidFileNames.size(); i++) {
            stringBuilder.append(invalidFileNames.get(i));

            if (!(i == invalidFileNames.size() - 1)) {
                stringBuilder.append(", ");
            }
        }

        return stringBuilder.toString();
    }
}
