package com.example.demo.service.impl;

import com.example.demo.dao.DocumentRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.domain.Document;
import com.example.demo.domain.User;
import com.example.demo.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {
    private static final String PATH = "D:\\Miron\\dich\\demo\\src\\main\\resources\\documents";

    @Autowired
    ServletContext servletContext;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Collection<Document> getAll() {
        return documentRepository.findAll();
    }

    @Override
    public void delete(long id) {
        File file = new File(PATH, documentRepository.findById(id).get().getDocName());
        file.delete();
        documentRepository.deleteById(id);
    }

    public Collection<Document> getByUserName(String username){
        return documentRepository.findByUserUserName(username);
    }

    @Override
    public Optional<Document> getById(long id) {
        return documentRepository.findById(id);
    }

    @Override
    public Document create(MultipartFile file) {
        return documentRepository.save(createFile(file));
    }

    public Document createFile(MultipartFile file) {
        String name = file.getOriginalFilename();
        Document document = new Document();
        document.setDocName(name);
        User currentUser = userRepository.findByUserName(getCurrentUsername()).get();
        document.setUser(currentUser);
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(PATH, name)));
                stream.write(bytes);
                stream.close();
                document.setUploadDate(new Date());
                document.setUpdateDate(new Date());
                return document;
            } catch (Exception e) {
                //return "Вам не удалось загрузить " + name + " => " + e.getMessage();
            }
        } else {
            //return "Вам не удалось загрузить " + name + " потому что файл пустой.";
        }
        return null;
    }

    @Override
    public Document update(Document document, MultipartFile file) {
        File prev_file = new File(PATH, documentRepository.findById(document.getId()).get().getDocName());
        prev_file.delete();
        Document updateDocument = createFile(file);
        updateDocument.setId(document.getId());
        updateDocument.setUpdateDate(new Date());
        return documentRepository.saveAndFlush(updateDocument);
    }

    @Override
    public Collection<Document> findPaginated(int page, int size, String sortColumn, String sortDirection) {
        User currentUser = userRepository.findByUserName(getCurrentUsername()).get();
        PageRequest pageRequest;
        if (sortDirection.equals("NULL")) {
            pageRequest = new PageRequest(page, size);
        } else {
            pageRequest = new PageRequest(page, size, Sort.Direction.valueOf(sortDirection), sortColumn);
        }
        switch(currentUser.getRole()) {
            case USER:
                List<Document> documents = documentRepository.findByUserUserName(currentUser.getUserName(), pageRequest);
                return documents;
            case ADMIN:
                return documentRepository.findAll(pageRequest).getContent();
            default:
                return documentRepository.findAll(pageRequest).getContent();
        }
    }

    @Override
    public void delete() {
        Collection<Document> documents = documentRepository.findByUserUserName(getCurrentUsername());
        for (Document document : documents)
            documentRepository.deleteById(document.getId());
    }

    @Override
    public ResponseEntity<ByteArrayResource> downloadById(int id) {
        String fileName = getById(id).get().getDocName();
        String mineType = servletContext.getMimeType(fileName);
        MediaType mediaType = MediaType.parseMediaType(mineType);

        Path path = Paths.get(PATH + "/" + fileName);
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                // Content-Type
                .contentType(mediaType) //
                // Content-Lengh
                .contentLength(data.length) //
                .body(resource);
    }

    public String getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
