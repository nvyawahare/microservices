package com.microservices.simpleService.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.simpleService.entities.Tutorial;
import com.microservices.simpleService.repo.TutorialRepository;

@RestController
@RequestMapping("myapi")
public class TutorialController2 {
	@Autowired
	TutorialRepository repo;

	@GetMapping("tutorials")
	public ResponseEntity<List<Tutorial>> getTutorials(@RequestParam(required=false) String title){
		List<Tutorial> tutorials = new ArrayList<>();
		ResponseEntity<List<Tutorial>> response = null;
		if(title == null) {
			repo.findAll().forEach(tutorials::add);
		}else {
			repo.findByTitleContaining(title).forEach(tutorials::add);
		}
		if(tutorials==null || tutorials.size()<1) {
			 response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			response = new ResponseEntity<>(tutorials, HttpStatus.OK);
		}
		return response;
	}
	
	@PostMapping("tutorials")
	public ResponseEntity<Tutorial> addTutorials(@RequestBody Tutorial tutorial) {
		try {
			ResponseEntity<Tutorial> response = null;
			Tutorial saved = null;
			if(tutorial!=null) {
				saved = repo.save(tutorial);
				response = new ResponseEntity<>(saved, HttpStatus.CREATED);
			}else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			return response;
		}catch(Exception p) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	} 
	
	@GetMapping("tutorials/{id}")
	public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id){
		try {
			ResponseEntity<Tutorial> response = null;
			Optional<Tutorial> tutorial = repo.findById(id);
			if(tutorial.isPresent()) {
				response = new ResponseEntity(tutorial, HttpStatus.FOUND);
			}else {
				response = new ResponseEntity<Tutorial>(HttpStatus.NOT_FOUND);
			}
			return response;
		}catch(IllegalArgumentException iae) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PutMapping("tutorials/{id}")
	public ResponseEntity<Tutorial> updateTurotial(@PathVariable("id")long id, @RequestBody Tutorial tutorial){
		try {
			ResponseEntity<Tutorial> response = null;
			if(!Objects.isNull(id) && tutorial !=null) {
				Tutorial updated = repo.save(tutorial);
				response = new ResponseEntity(updated, HttpStatus.OK);
			}else {
				response = new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
			return response;
		}catch(Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@DeleteMapping("tutorials/{id}")
	public ResponseEntity<Tutorial> deleteTutorial(@PathVariable("id")long id){
		try {
			ResponseEntity<Tutorial> response = null;
			repo.deleteById(id);
			response = new ResponseEntity(HttpStatus.ACCEPTED);
			return response;
		}catch(Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("tutorials")
	public ResponseEntity<Tutorial> deleteTutorials(@RequestParam(required=false) long id){
		try {
			ResponseEntity<Tutorial> response = null;
			if( !Objects.isNull(id) ) {
				repo.deleteById(id);
				response = new ResponseEntity(HttpStatus.ACCEPTED);
			}else {
				repo.deleteAll();
				response = new ResponseEntity(HttpStatus.ACCEPTED);
			}			
			return response;
		}catch(Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("tutorials/published")
	public ResponseEntity<Tutorial> getPublished(){
		try {
			ResponseEntity<Tutorial> response = null;
			List<Tutorial> tutorials = new ArrayList<>();
			repo.findByPublished(true).forEach(tutorials::add);
			
			if(tutorials != null)
				response = new ResponseEntity(tutorials, HttpStatus.OK);
			else
				response = new ResponseEntity(HttpStatus.NO_CONTENT);
						
			return response;
		}catch(Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
