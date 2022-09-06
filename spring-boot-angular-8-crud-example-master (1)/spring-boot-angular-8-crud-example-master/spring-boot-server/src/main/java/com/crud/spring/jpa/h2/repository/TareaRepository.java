package com.crud.spring.jpa.h2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crud.spring.jpa.h2.model.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
  List<Tarea> findByPublished(boolean published);

  List<Tarea> findByTitleContaining(String title);
}
