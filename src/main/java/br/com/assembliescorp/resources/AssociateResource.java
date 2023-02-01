package br.com.assembliescorp.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.assembliescorp.domain.dtos.associate.AssociateCreateDTO;
import br.com.assembliescorp.domain.dtos.associate.AssociateListDTO;
import br.com.assembliescorp.services.AssociateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Associado")
@RestController
@RequestMapping("api/v1/associate")
public class AssociateResource {
	
	public final AssociateService associateService;
	
	@Autowired
	public AssociateResource(AssociateService associateService) {
		this.associateService = associateService;
	}
	
	@GetMapping
	@Operation(summary = "Listar de Associados")
	public ResponseEntity<List<AssociateListDTO>> getPageable(){
		return ResponseEntity.ok(this.associateService.getList());
	}
		
	@PostMapping
	@Operation(summary = "Criar Associados")
	public ResponseEntity<AssociateCreateDTO> create(@Valid @RequestBody AssociateCreateDTO associateCreateDTO, UriComponentsBuilder uriBuilder) {		
		var associate = associateService.create(associateCreateDTO);
        var uri = uriBuilder.path("api/v1/associate/{id}").buildAndExpand(associate.id()).toUri();
        return ResponseEntity.created(uri).body(associate);       		
	}

}
