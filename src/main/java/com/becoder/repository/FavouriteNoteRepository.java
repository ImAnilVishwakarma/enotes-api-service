package com.becoder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yaml.snakeyaml.events.Event.ID;

import com.becoder.entity.FavouriteNote;

public interface FavouriteNoteRepository extends JpaRepository<FavouriteNote, Integer>{

	List<FavouriteNote> findByUserId(int userld);

}
