package com.example.demo.service;

import com.example.demo.domain.Document;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Optional;

public interface DocumentService {
    Collection<Document> getAll();
    Collection<Document> getByUserName(String name);
    Optional<Document> getById(long id);

    void delete(long id);
    void delete();

    Document create(MultipartFile file, Document document);
    Document update(Document newDoc, Document document, MultipartFile file);

    Collection<Document> findPaginated(int page, int size, String sortColumn, String sortDirection);

    ResponseEntity<ByteArrayResource> downloadById(long id);

    Document upDocProp(Document document);
}
