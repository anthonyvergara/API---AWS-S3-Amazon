package com.api.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.api.model.Documento;

public interface DocumentoService {
	
	public List<Documento> listAll();
	public List<String> save(List<MultipartFile> file);
	public List<Resource> download(List<String> file);
	public String upload(MultipartFile file);
	
}
