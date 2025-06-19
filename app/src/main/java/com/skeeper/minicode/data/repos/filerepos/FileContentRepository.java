package com.skeeper.minicode.data.repos.filerepos;

import com.skeeper.minicode.domain.contracts.repos.file.IFileContentRepository;
import com.skeeper.minicode.domain.exceptions.DomainIOException;
import com.skeeper.minicode.domain.exceptions.FileReadException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileContentRepository implements IFileContentRepository {

    @Override
    public String readFileText(String path) throws FileReadException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileReadException("File not found: " + file.getAbsolutePath());
        }
        if (!file.isFile()) {
            throw new FileReadException("Not a file: " + file.getAbsolutePath());
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new FileReadException(e.getMessage());
        }

        if (content.length() > 0) {
            content.setLength(content.length() - 1);
        }
        return content.toString();
    }

    @Override
    public void writeFileText(String path, String content) throws DomainIOException {
        File file = new File(path);

        if (!file.exists()) {
            throw new DomainIOException("file not exists");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            throw new DomainIOException(e.getMessage());
        }
    }


}
