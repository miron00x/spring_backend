package com.example.demo.dao;

import com.example.demo.domain.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Collection<Document> findByUserUserName(String username);
    List<Document> findByUserUserName(String username, Pageable pageable);
}
