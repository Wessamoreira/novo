package negocio.comuns.bancocurriculum;

import negocio.comuns.arquitetura.SuperVO;

public class CandidatoVagaQuestaoRespostaVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3515740409019569055L;
	private Integer codigo;
	private CandidatoVagaQuestaoVO candidatoVagaQuestao;
	private OpcaoRespostaVagaQuestaoVO opcaoRespostaVagaQuestao;
	private Boolean selecionada;
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public CandidatoVagaQuestaoVO getCandidatoVagaQuestao() {
		if(candidatoVagaQuestao == null){
			candidatoVagaQuestao = new CandidatoVagaQuestaoVO();
		}
		return candidatoVagaQuestao;
	}
	public void setCandidatoVagaQuestao(CandidatoVagaQuestaoVO candidatoVagaQuestao) {
		this.candidatoVagaQuestao = candidatoVagaQuestao;
	}
	public OpcaoRespostaVagaQuestaoVO getOpcaoRespostaVagaQuestao() {
		if(opcaoRespostaVagaQuestao == null){
			opcaoRespostaVagaQuestao = new OpcaoRespostaVagaQuestaoVO();
		}
		return opcaoRespostaVagaQuestao;
	}
	public void setOpcaoRespostaVagaQuestao(OpcaoRespostaVagaQuestaoVO opcaoRespostaVagaQuestao) {
		this.opcaoRespostaVagaQuestao = opcaoRespostaVagaQuestao;
	}
	public Boolean getSelecionada() {
		if(selecionada == null){
			selecionada = false;
		}
		return selecionada;
	}
	public void setSelecionada(Boolean selecionada) {
		this.selecionada = selecionada;
	}
	
	
	

}
