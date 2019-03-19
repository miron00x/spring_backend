package com.example.demo.controller;

import com.example.demo.domain.Document;
import com.example.demo.domain.PageRequestModel;
import com.example.demo.service.impl.DocumentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    @Autowired
    DocumentServiceImpl documentService;

    private static final String GET_BY_ID = "/id/{id}";
    private static final String GET_BY_USERNAME = "/{username}";
    private static final String DOWNLOAD_BY_ID = "/download/{id}";
    private static final String GET_PAGE = "/page";
    private static final String CREATE = "/create";
    private static final String UPD_DOC = "/{documentId}";
    private static final String UPD_PROP = "/upd";
    private static final String DELETE_BY_ID = "/delete/{documentId}";
    private static final String DELETE_ALL = "/delete";

    @GetMapping()
    Collection<Document> readDocumentsByUsername(){
        return documentService.getAll();
    }

    @GetMapping(GET_BY_USERNAME)
    Collection<Document> readDocumentsByUsername(@PathVariable String username){
        return documentService.getByUserName(username);
    }

    @GetMapping(GET_BY_ID)
    Document readDocumentsById(@PathVariable int id){
        return documentService.getById(id).orElseThrow(NullPointerException::new);
    }

    @GetMapping(DOWNLOAD_BY_ID)
    ResponseEntity<ByteArrayResource> downloadDocumentsById(@PathVariable long id) {
        return documentService.downloadById(id);
    }

    @RequestMapping(
        value = GET_PAGE,
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

    @PostMapping(CREATE)
    public Document addDocument(@RequestParam("document") String document,
                                @RequestParam("uploadFile") MultipartFile file) {
        Document doc = new Document();
        doc.setDocName(file.getOriginalFilename());
        try {
            ObjectNode node = new ObjectMapper().readValue(document, ObjectNode.class);
            if (node.has("visibleName")) {
                doc.setVisibleName(String.valueOf(node.get("visibleName")));
            }
            if (node.has("description")) {
                doc.setDescription(String.valueOf(node.get("description")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documentService.create(file, doc);
    }

    @PostMapping(UPD_DOC)
    public Document updateDocument(@PathVariable Long documentId,
                                   @RequestParam("uploadFile") MultipartFile file,
                                   @RequestParam("document") String document) {
        Document doc = new Document();
        doc.setDocName(file.getOriginalFilename());
        try {
            ObjectNode node = new ObjectMapper().readValue(document, ObjectNode.class);
            if (node.has("visibleName")) {
                doc.setVisibleName(String.valueOf(node.get("visibleName")));
            }
            if (node.has("description")) {
                doc.setDescription(String.valueOf(node.get("description")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documentService.update(doc, documentService.getById(documentId).orElseThrow(NullPointerException::new), file);
    }

    @PostMapping(UPD_PROP)
    public Document upDocProp(@RequestBody Document document) {
        return documentService.upDocProp(document);
    }

    @DeleteMapping(DELETE_BY_ID)
    public void deleteDocument(@PathVariable Long documentId) {
        documentService.delete(documentId);
    }

    @DeleteMapping(DELETE_ALL)
    public void deleteAll() {
        documentService.delete();
    }
}
