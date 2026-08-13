package com.becoder.schedular;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.becoder.entity.Notes;
import com.becoder.repository.NotesRespository;

@Component
public class NotesScheduler {
	
	@Autowired
	private NotesRespository notesRepo;

	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteNotesSchdular() {
		
		// 20-dec -14-dec (7 day)
		LocalDateTime cutOffDate = LocalDateTime.now().minusDays(7);
		List<Notes> deleteNotes =  notesRepo.findAllByIsDeletedAndDeletedOnBefore(true, cutOffDate);
		notesRepo.deleteAll(deleteNotes);
		
		
		
	}
}
