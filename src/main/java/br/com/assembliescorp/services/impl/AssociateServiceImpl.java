package br.com.assembliescorp.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.assembliescorp.domain.dtos.associate.AssociateCreateDTO;
import br.com.assembliescorp.domain.dtos.associate.AssociateListDTO;
import br.com.assembliescorp.domain.entities.AssociateEntity;
import br.com.assembliescorp.domain.repositories.AssociateRepository;
import br.com.assembliescorp.services.AssociateService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssociateServiceImpl implements AssociateService {
	
	private final AssociateRepository associateRepository;
	
	
	public AssociateServiceImpl(AssociateRepository associateRepository) {
		this.associateRepository = associateRepository;
	}
	
	/**
	 * Método para criar um associado
	 * @param associateCreateDTO AssociadoDTO
	 * @return O associado criado
	 */
	@Transactional
	public AssociateCreateDTO create(AssociateCreateDTO associateCreateDTO){			
		var associate = new AssociateEntity(associateCreateDTO);
		associateRepository.save(associate);
		log.info("ASSOCIADO CADASTRADO COM SUCESSO : {}",associateCreateDTO.name());
		return new AssociateCreateDTO(associate);		
	}

	/**
	 * Método para listar todos associados
	 * @return uma lista de associados
	 */
	public List<AssociateListDTO> getList() {
		return associateRepository.findAll().stream().map(AssociateListDTO::new).toList(); 	
	}

	/**
	 * Método para buscar um único associado
	 * @param idAssociate idAssociado
	 * @return Retornará se encontrar ou não associado
	 */
	public Optional<AssociateEntity> findOne(Long idAssociate) {
		return associateRepository.findById(idAssociate);
	}
}
