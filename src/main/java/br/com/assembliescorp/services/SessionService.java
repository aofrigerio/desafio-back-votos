package br.com.assembliescorp.services;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import br.com.assembliescorp.domain.RulingCreateDTO;
import br.com.assembliescorp.domain.RulingListDTO;
import br.com.assembliescorp.domain.dtos.session.SessionCreateDTO;
import br.com.assembliescorp.domain.dtos.session.SessionListDTO;

public interface SessionService {
	
	List<SessionListDTO> getList();	
	SessionCreateDTO create(SessionCreateDTO sessionCreateDTO);
	void finishSession(Long idSession, String jsonResult);

}
