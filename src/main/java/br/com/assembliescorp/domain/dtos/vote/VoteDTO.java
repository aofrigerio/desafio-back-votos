package br.com.assembliescorp.domain.dtos.vote;
import br.com.assembliescorp.domain.entities.VoteEntity;
import br.com.assembliescorp.domain.enuns.ValueVoteDescription;
import jakarta.validation.constraints.NotEmpty;

public record VoteDTO(
		Long id,
		@NotEmpty
		Long idSession,
		@NotEmpty
		Long idRuling,
		@NotEmpty
		Long idAssociate,
		@NotEmpty
		ValueVoteDescription value			
		) {
	
	public VoteDTO(VoteEntity voteEntity) {
		this(voteEntity.getId(), voteEntity.getSession().getId(), voteEntity.getRuling().getId(), voteEntity.getAssociate().getId(), voteEntity.getValueVote());
	}

}
