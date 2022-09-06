package com.crud.spring.jpa.h2.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crud.spring.jpa.h2.model.Tarea;
import com.crud.spring.jpa.h2.repository.TareaRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TareaController {

	@Autowired
	TareaRepository tareaRepository;

	@GetMapping("/tareas")
	public ResponseEntity<List<Tarea>> getAllTareas(@RequestParam(required = false) String title) {
		try {
			List<Tarea> tareas = new ArrayList<Tarea>();

			if (title == null)
				tareaRepository.findAll().forEach(tareas::add);
			else
				tareaRepository.findByTitleContaining(title).forEach(tareas::add);

			if (tareas.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(tareas, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/tareas/{id}")
	public ResponseEntity<Tarea> getTareaById(@PathVariable("id") long id) {
		Optional<Tarea> tutorialData = tareaRepository.findById(id);

		if (tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/tareas")
	public ResponseEntity<Tarea> createTarea(@RequestBody Tarea tarea) {
		try {
			Tarea _tarea = tareaRepository
					.save(new Tarea(tarea.getTitle(), tarea.getDescription(), false));
			return new ResponseEntity<>(_tarea, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/tareas/{id}")
	public ResponseEntity<Tarea> updateTarea(@PathVariable("id") long id, @RequestBody Tarea tarea) {
		Optional<Tarea> tutorialData = tareaRepository.findById(id);

		if (tutorialData.isPresent()) {
			Tarea _tarea = tutorialData.get();
			_tarea.setTitle(tarea.getTitle());
			_tarea.setDescription(tarea.getDescription());
			_tarea.setPublished(tarea.isPublished());
			return new ResponseEntity<>(tareaRepository.save(_tarea), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/tareas/{id}")
	public ResponseEntity<HttpStatus> deleteTarea(@PathVariable("id") long id) {
		try {
			tareaRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/tareas")
	public ResponseEntity<HttpStatus> deleteAllTareas() {
		try {
			tareaRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/tareas/published")
	public ResponseEntity<List<Tarea>> findByPublished() {
		try {
			List<Tarea> tareas = tareaRepository.findByPublished(true);

			if (tareas.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(tareas, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
