package br.com.assembliescorp.services;

import java.util.List;
import java.util.Optional;

import br.com.assembliescorp.domain.dtos.session.SessionCreateDTO;
import br.com.assembliescorp.domain.dtos.session.SessionListDTO;
import br.com.assembliescorp.domain.entities.SessionEntity;

public interface SessionService {
	
	/**
	 * Retorna lista de sessões
	 * @return Lista
	 */
	List<SessionListDTO> getList();
	/**
	 * Cria uma sessão
	 * @param sessionCreateDTO
	 * @return SessãoDTO criada
	 */
	SessionCreateDTO create(SessionCreateDTO sessionCreateDTO);
	/**
	 * Método para buscar uma sessão que ainda não está fechada
	 * @param idSession
	 * @return
	 */
	SessionEntity findSessionNotClosed(Long idSession);
	/**
	 * Método que busca uma sessão que pode estar expirada
	 * @param idSession
	 * @return
	 */
	SessionEntity findSessionExpirated(Long idSession);
	/**
	 * Método para procurar uma sessão
	 * @param idSession
	 * @return Retorna ou não uma sessão
	 */
	Optional<SessionEntity> findById(Long idSession);
	/**
	 * Método para finalizar a sessão com a data e resultado
	 * @param session Sessão entidade
	 * @param jsonResult resultado em Json
	 */
	void finishSession(SessionEntity session, String jsonResult);

}
