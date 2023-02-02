package br.com.assembliescorp.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.assembliescorp.domain.dtos.session.SessionCreateDTO;
import br.com.assembliescorp.domain.dtos.session.SessionListDTO;
import br.com.assembliescorp.domain.entities.SessionEntity;
import br.com.assembliescorp.domain.repositories.SessionRepository;
import br.com.assembliescorp.resources.exceptions.NotFoundEntityException;
import br.com.assembliescorp.resources.exceptions.SessionClosedException;
import br.com.assembliescorp.services.SessionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SessionServiceImpl implements SessionService {
	
	private Long minutes_session = 1L;
	
	public final SessionRepository sessionRepository;
	
	public SessionServiceImpl(SessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}

	/**
	 * Retorna lista de sessões
	 * @return Lista
	 */
	public List<SessionListDTO> getList() {
		return sessionRepository.findAll().stream().map(SessionListDTO::new).toList();
	}

	/**
	 * Cria uma sessão
	 * @param sessionCreateDTO
	 * @return SessãoDTO criada
	 */
	public SessionCreateDTO create(SessionCreateDTO sessionCreateDTO) {
		var session = new SessionEntity(sessionCreateDTO);
		if(sessionCreateDTO.minutes() == null) {
			session.setMinutes(minutes_session);
		}
		sessionRepository.save(session);
		log.info("SESSAO CADASTRADO COM SUCESSO : {}", sessionCreateDTO.name());
		return new SessionCreateDTO(session);
	}

	/**
	 * Método para finalizar a sessão com a data e resultado
	 * @param session Sessão entidade
	 * @param jsonResult resultado em Json
	 */
	public void finishSession(SessionEntity session, String jsonResult) {	
		session.setFinish(LocalDateTime.now());
		session.setResult(jsonResult);
		sessionRepository.save(session);
		log.info("SESSAO FINALIZADA COM SUCESSO: {}", session.getName());
	}
	
	/**
	 * Método para buscar uma sessão que ainda não está fechada
	 * @param idSession
	 * @return
	 */
	public SessionEntity findSessionNotClosed(Long idSession) {
		SessionEntity session = findById(idSession).orElseThrow(NotFoundEntityException::new);
		if(session.getFinish() != null) {
			log.error("SESSAO {} JÁ FINALIZADA", session.getName());
			throw new SessionClosedException();
		}
		
		return session;
	}
	
	/**
	 * Método para procurar uma sessão
	 * @param idSession
	 * @return Retorna ou não uma sessão
	 */
	public SessionEntity findSessionExpirated(Long idSession) {
		SessionEntity session = findById(idSession).orElseThrow(NotFoundEntityException::new);
		if(session.getBegin().plusMinutes(session.getMinutes()).isBefore(LocalDateTime.now())) {
			return session;
		}
		log.error("SESSAO {} JÁ EXPIRADA", session.getName());
		throw new NotFoundEntityException();
	}

	/**
	 * Método para finalizar a sessão com a data e resultado
	 * @param session Sessão entidade
	 * @param jsonResult resultado em Json
	 */
	public Optional<SessionEntity> findById(Long idSession) {
		return sessionRepository.findById(idSession);
	}


}
	