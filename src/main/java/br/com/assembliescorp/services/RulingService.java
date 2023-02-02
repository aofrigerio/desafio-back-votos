package br.com.assembliescorp.services;

import java.util.List;
import java.util.Optional;

import br.com.assembliescorp.domain.dtos.ruling.RulingCreateDTO;
import br.com.assembliescorp.domain.dtos.ruling.RulingListDTO;
import br.com.assembliescorp.domain.entities.RulingEntity;


public interface RulingService {
	
	/**
	 * Método para listar pautas
	 * @return lista
	 */
	List<RulingListDTO> getList();
	/**
	 * Método para criar uma pauta
	 * @param rulingCreateDTO Uma RulingCreateDTO
	 * @return Uma pauta criada
	 */
	RulingCreateDTO create(RulingCreateDTO rulingCreateDTO);
	/**
	 * Método para buscar uma única pauta
	 * @param idRuling Id de uma pauta
	 * @return Retornará se encontrar ou não a pauta
	 */
	Optional<RulingEntity> findOne(Long idRuling);
}
