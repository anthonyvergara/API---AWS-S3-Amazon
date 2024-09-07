package com.api.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.model.Documento;
import com.api.service.DocumentoService;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

@Service
public class DocumentoImpl implements DocumentoService{
	
	private final S3Client s3;
	
	@Value("${aws.s3.bucket.name}")
	private String bucketName;
	
	public DocumentoImpl(S3Client s3) {
		this.s3 = s3;
	}

	@Override
	public List<String> listAll() {
		
		ListObjectsV2Request listReq = ListObjectsV2Request.builder().bucket(bucketName).build();
		
		ListObjectsV2Iterable listV2 = s3.listObjectsV2Paginator(listReq);
		
		List<String> lista = new ArrayList<>();
		
		listV2.contents().forEach(value ->
			lista.add(value.key())
		);
		
		return lista;
	}

	@Override
	public List<String> save(List<MultipartFile> file) {
		
		List<String> listKey = new ArrayList<>();
		
		
		try {
			
			for(MultipartFile f : file) {
				
				CompletableFuture<String> future = CompletableFuture.supplyAsync(() ->{
					return this.upload(f);
				});
				
				listKey.add(future.get().toString() + f.getContentType());
				
			}
		}catch(RuntimeException | InterruptedException | ExecutionException e) {
			e.getMessage();
		}
		
		return listKey;
	}
	
	@Override
	public List<Resource> download(List<String> file) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String upload(MultipartFile file){
		
		String contentType = file.getContentType();
		String key = UUID.randomUUID().toString();
		
		try (InputStream fileInput = file.getInputStream()){
			s3.putObject(PutObjectRequest
					.builder()
					.bucket(bucketName)
					.key(key)
					.contentType(contentType)
					.build()
					, RequestBody.fromInputStream(fileInput, file.getSize()));
		} catch (AwsServiceException | IOException e) {
			e.printStackTrace();
		} 
		
		return key;
	}

}
