package com.example.demo.service.impl;

import com.example.demo.dao.DocumentRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.domain.Document;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.exception.DocNotFondException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {
    private static final String PATH = ".\\src\\main\\resources\\documents";

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
        File file = new File(PATH + "\\" + getDocById(id).getUser().getUserName(), documentRepository.findById(id).orElseThrow(NullPointerException::new).getDocName());
        if(file.delete()) {
            documentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Error deleting file");
        }
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
        for (Document document : documentRepository.findByUserUserName(getCurrentUsername())){
            if (document.getDocName().equals(name)) throw new IllegalStateException("This file already exist");
        }
        Document document = new Document();
        document.setDocName(name);
        User currentUser = getUserByName(getCurrentUsername());
        document.setUser(currentUser);
        if (file.getSize() > 1024*1024*20) throw new  RuntimeException("Unfortunately file size is over 20MB");
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(PATH + "\\" + getCurrentUsername(), name)));
                stream.write(bytes);
                stream.close();
                document.setUploadDate(new Date());
                document.setUpdateDate(new Date());
                return document;
            } catch (Exception e) {
                //return "Вам не удалось загрузить " + name + " => " + e.getMessage();
            }
        } else {
            throw new NullPointerException("File is empty");
        }
        throw new RuntimeException("Error creating file");
    }

    @Override
    public Document update(Document document, MultipartFile file) {
        File prev_file = new File(PATH + "\\" + getCurrentUsername(), getDocById(document.getId()).getDocName());
        if(prev_file.delete()) {
            Document updateDocument = createFile(file);
            updateDocument.setId(document.getId());
            updateDocument.setUpdateDate(new Date());
            return documentRepository.saveAndFlush(updateDocument);
        }
        throw new RuntimeException("Error updating file");
    }

    @Override
    public Collection<Document> findPaginated(int page, int size, String sortColumn, String sortDirection) {
        User currentUser = getUserByName(getCurrentUsername());
        PageRequest pageRequest;
        if (sortDirection.equals("NULL")) {
            pageRequest = PageRequest.of(page, size);
        } else {
            pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(sortDirection), sortColumn);
        }
        switch(currentUser.getRole()) {
            case USER:
                return documentRepository.findByUserUserName(currentUser.getUserName(), pageRequest);
            case ADMIN:
                return documentRepository.findAll(pageRequest).getContent();
            default:
                return documentRepository.findAll(pageRequest).getContent();
        }
    }

    @Override
    public void delete() {
        Collection<Document> documents;
        if (getUserByName(getCurrentUsername()).getRole().equals(Role.USER))
            documents = documentRepository.findByUserUserName(getCurrentUsername());
        else
            documents = documentRepository.findAll();

        for (Document document : documents)
            delete(document.getId());
    }

    @Override
    public ResponseEntity<ByteArrayResource> downloadById(long id) {
        String fileName = getDocById(id).getDocName();
        String mineType = servletContext.getMimeType(fileName);
        MediaType mediaType = MediaType.parseMediaType(mineType);

        Path path = Paths.get(PATH + "\\" + getDocById(id).getUser().getUserName() + "/" + fileName);
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

    @Override
    public Document upDocProp(Document document) {
        File file = new File(PATH + "\\" + getCurrentUsername(), getDocById(document.getId()).getDocName());
        if(file.renameTo(new File(PATH + "\\" + getCurrentUsername(), document.getDocName()))) {
            return documentRepository.save(document);
        }
        throw new RuntimeException("Error updating properties");
    }

    public String getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public Document getDocById(long id){
        return documentRepository.findById(id).orElseThrow(DocNotFondException::new);
    }

    public User getUserByName(String name){
        return userRepository.findByUserName(name).orElseThrow(UserNotFoundException::new);
    }
}
