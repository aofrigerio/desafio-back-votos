package br.com.assembliescorp.services;

import org.springframework.stereotype.Service;

import br.com.assembliescorp.domain.dtos.vote.VoteDTO;
import br.com.assembliescorp.domain.dtos.vote.VoteProcess;

@Service
public interface VoteService {
	
	/**
	 * Método responsável para votação
	 * @param voteDTO Uma VoteDTO
	 * @return Devolve o voto computado
	 */
	VoteDTO vote(VoteDTO voteDTO);
	/**
	 * Método para processar os votos com base em uma sessão
	 * @param voteProcess
	 */
	void process(VoteProcess voteProcess);

}
