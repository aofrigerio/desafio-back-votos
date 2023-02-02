package br.com.assembliescorp.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.assembliescorp.domain.clients.CpfValidation;
import br.com.assembliescorp.domain.dtos.vote.VoteDTO;
import br.com.assembliescorp.domain.dtos.vote.VoteProcess;
import br.com.assembliescorp.domain.entities.AssociateEntity;
import br.com.assembliescorp.domain.entities.SessionEntity;
import br.com.assembliescorp.domain.entities.VoteEntity;
import br.com.assembliescorp.domain.projections.VoteGroupProjection;
import br.com.assembliescorp.domain.repositories.VoteRepository;
import br.com.assembliescorp.resources.exceptions.NotFoundEntityException;
import br.com.assembliescorp.resources.exceptions.UnableToVoteException;
import br.com.assembliescorp.services.AssociateService;
import br.com.assembliescorp.services.SendRabbitService;
import br.com.assembliescorp.services.SessionService;
import br.com.assembliescorp.services.VoteService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VotesServiceImpl implements VoteService {

	private final VoteRepository voteRepository;
	private final AssociateService associateService;
	private final SessionService sessionService;
	private final ObjectMapper objectMapper;
	private final SendRabbitService sendRabbitService;
	private final CpfValidation cpfValidation;
	
	public VotesServiceImpl(VoteRepository voteRepository, AssociateService associateService,
			SessionService sessionService, ObjectMapper objectMapper,
			SendRabbitService sendRabbitService, CpfValidation cpfValidation) {
		this.voteRepository = voteRepository;
		this.associateService = associateService;
		this.sessionService = sessionService;
		this.objectMapper = objectMapper;
		this.sendRabbitService = sendRabbitService;
		this.cpfValidation = cpfValidation;
	}

	public VoteDTO vote(VoteDTO voteDTO) {		
		AssociateEntity associate = associateService.findOne(voteDTO.idAssociate())
				.orElseThrow(NotFoundEntityException::new);
		
		validationCpfVote(associate.getCpf());
		
		SessionEntity session = sessionService.findSessionExpirated(voteDTO.idSession());
		
		var voteEntity = new VoteEntity(session, associate, Boolean.FALSE, voteDTO.value());
		voteRepository.save(voteEntity);
		log.info("VOTACAO DA SESSAO {} COMPUTADA COM SUCESSO DO ASSOCIADO", voteDTO.idSession(), voteDTO.idAssociate());
		return new VoteDTO(voteEntity);
	}
	
	/**
	 * Valida se o associado está habilitado a votar
	 * @param associate 
	 */
	private void validationCpfVote(String cpf) {
		String retorno = cpfValidation.getValidationCpf(cpf);
		if(retorno.contains("UNABLE_TO_VOTE")) {
			throw new UnableToVoteException();
		}
	}

	@Transactional
	public void process(VoteProcess voteProcess) {
		log.info(">>> INICIO DA APURAÇÃOD E VOTOS DA SESSAO {}", voteProcess.idSession());
		Long idSession = voteProcess.idSession();
		SessionEntity session = sessionService.findSessionNotClosed(idSession);

		List<VoteGroupProjection> votes = voteRepository.getCountBySession(idSession);
		log.info("APURADO {} VOTOS DA SESSAO {}", votes.size(), voteProcess.idSession());
		
		voteRepository.process(idSession);
		
		String votesJson = null;
		String sendRabbitMessage = null;
		Object[] objects = { session, votes };

		try {
			votesJson = objectMapper.writeValueAsString(votes);
			sendRabbitMessage = objectMapper.writeValueAsString(objects);
		} catch (JsonProcessingException e) {
			log.error("APURAÇÃO INCONSISTENTE DA SESSSAO {}", voteProcess.idSession());
			sendRabbitMessage = votesJson = "inconsistente";
		}

		sessionService.finishSession(session, votesJson);

		sendRabbitService.sendResultToRabbit(sendRabbitMessage);
		log.info(">>> FIM DA APURAÇÃO E VOTOS DA SESSAO {}", voteProcess.idSession());

	}

}
