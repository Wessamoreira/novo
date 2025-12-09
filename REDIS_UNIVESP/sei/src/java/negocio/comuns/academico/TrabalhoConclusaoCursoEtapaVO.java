package negocio.comuns.academico;

import negocio.comuns.academico.enumeradores.EtapaTCCEnum;
import negocio.comuns.academico.enumeradores.SituacaoTCCEnum;

public class TrabalhoConclusaoCursoEtapaVO {
	
	private EtapaTCCEnum etapaTCC;
	private SituacaoTCCEnum situacaoTCC;
	private Integer quantidade;
	
	public SituacaoTCCEnum getSituacaoTCC() {
		return situacaoTCC;
	}
	
	public void setSituacaoTCC(SituacaoTCCEnum situacaoTCC) {
		this.situacaoTCC = situacaoTCC;
	}
	
	public Integer getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public EtapaTCCEnum getEtapaTCC() {
		return etapaTCC;
	}

	public void setEtapaTCC(EtapaTCCEnum etapaTCC) {
		this.etapaTCC = etapaTCC;
	}

}
