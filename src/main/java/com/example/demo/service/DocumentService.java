package com.example.demo.service;

import com.example.demo.domain.Document;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Optional;

public interface DocumentService {
    Collection<Document> getAll();
    void delete(long id);
    Collection<Document> getByUserName(String name);
    Optional<Document> getById(long id);
    Document create(MultipartFile file);
    Document update(Document document, MultipartFile file);
    Collection<Document> findPaginated(int page, int size, String sortColumn, String sortDirection);
    void delete();

    ResponseEntity<ByteArrayResource> downloadById(long id);

    Document upDocProp(Document document);
}
