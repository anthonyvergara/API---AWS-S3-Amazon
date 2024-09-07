package com.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.serviceImpl.DocumentoImpl;

@RestController
@RequestMapping(value = "/documento")
public class DocumentoController {

	@Autowired
	private DocumentoImpl documentoService;
	
	@PostMapping(value = "/")
	public ResponseEntity<List<String>> insertDoc(@RequestParam("file") List<MultipartFile> file){
		return new ResponseEntity<List<String>>(this.documentoService.save(file),HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<String>> listAll(){
		return new ResponseEntity<List<String>>(this.documentoService.listAll(),HttpStatus.OK);
	}
}
