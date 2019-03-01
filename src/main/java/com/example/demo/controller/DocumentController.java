package com.example.demo.controller;

import com.example.demo.domain.Document;
import com.example.demo.domain.PageRequestModel;
import com.example.demo.service.impl.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class DocumentController {
    @Autowired
    DocumentServiceImpl documentService;

    @GetMapping("/api/documents")
    Collection<Document> readDocumentsByUsername(){
        return documentService.getAll();
    }

    @GetMapping("/api/documents/{username}")
    Collection<Document> readDocumentsByUsername(@PathVariable String username){
        return documentService.getByUserName(username);
    }

    @GetMapping("/api/documents/id/{id}")
    Document readDocumentsById(@PathVariable int id){
        return documentService.getById(id).orElseThrow(NullPointerException::new);
    }

    @GetMapping("/api/documents/download/{id}")
    ResponseEntity<ByteArrayResource> downloadDocumentsById(@PathVariable long id) {
        return documentService.downloadById(id);
    }

    @RequestMapping(
        value = "/api/documents/page",
        params = { "page", "size" },
        method = RequestMethod.GET
    )
    public Collection<Document> readDocumentsPaginated(
            @ModelAttribute("pageRequestModel") PageRequestModel pageRequestModel
    ){
        return documentService.findPaginated(
                pageRequestModel.getPage(),
                pageRequestModel.getSize(),
                pageRequestModel.getSortColumn(),
                pageRequestModel.getSortDirection()
        );
    }

    @PostMapping("/api/documents/create")
    public Document addDocument(@RequestParam("uploadFile") MultipartFile file) {
        return documentService.create(file);
    }

    @PutMapping("/api/documents/{documentId}")
    public Document updateDocument(@PathVariable Long documentId,
                                   @RequestParam("uploadFile") MultipartFile file) {
        return documentService.update(documentService.getById(documentId).orElseThrow(NullPointerException::new), file);
    }

    @PostMapping("/api/documents/upd")
    public Document upDocProp(@RequestBody Document document) {
        return documentService.upDocProp(document);
    }

    @DeleteMapping("/api/documents/delete/{documentId}")
    public void deleteDocument(@PathVariable Long documentId) {
        documentService.delete(documentId);
    }

    @DeleteMapping("/api/documents/delete")
    public void deleteAll() {
        documentService.delete();
    }
}
