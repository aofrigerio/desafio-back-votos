package br.com.assembliescorp.services;

import java.util.List;
import java.util.Optional;

import br.com.assembliescorp.domain.dtos.associate.AssociateCreateDTO;
import br.com.assembliescorp.domain.dtos.associate.AssociateListDTO;
import br.com.assembliescorp.domain.entities.AssociateEntity;

public interface AssociateService {
	
	/**
	 * Método para listar todos associados
	 * @return uma lista de associados
	 */
	List<AssociateListDTO> getList();
	/**
	 * Método para criar um associado
	 * @param associateCreateDTO AssociadoDTO
	 * @return O associado criado
	 */
	AssociateCreateDTO create(AssociateCreateDTO associateCreateDTO);
	/**
	 * Método para buscar um único associado
	 * @param idAssociate idAssociado
	 * @return Retornará se encontrar ou não associado
	 */
	Optional<AssociateEntity> findOne(Long idAssociate);

}
