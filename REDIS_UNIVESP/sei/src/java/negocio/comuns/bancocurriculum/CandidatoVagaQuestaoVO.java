package negocio.comuns.bancocurriculum;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.bancocurriculum.enumeradores.TipoVagaQuestaoEnum;

public class CandidatoVagaQuestaoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5413004407376104329L;
	
	private CandidatosVagasVO candidatosVagas;
	private VagaQuestaoVO vagaQuestao;
	private String respostaTextual;
	private Integer codigo;
	private List<CandidatoVagaQuestaoRespostaVO> candidatoVagaQuestaoRespostaVOs;
	public CandidatosVagasVO getCandidatosVagas() {
		if(candidatosVagas == null){
			candidatosVagas = new CandidatosVagasVO();
		}
		return candidatosVagas;
	}
	public void setCandidatosVagas(CandidatosVagasVO candidatosVagas) {
		this.candidatosVagas = candidatosVagas;
	}
	public VagaQuestaoVO getVagaQuestao() {
		if(vagaQuestao == null){
			vagaQuestao = new VagaQuestaoVO();
		}
		return vagaQuestao;
	}
	public void setVagaQuestao(VagaQuestaoVO vagaQuestao) {
		this.vagaQuestao = vagaQuestao;
	}
	public String getRespostaTextual() {
		if(respostaTextual == null){
			respostaTextual = "";
		}
		return respostaTextual;
	}
	public void setRespostaTextual(String respostaTextual) {
		this.respostaTextual = respostaTextual;
	}
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public List<CandidatoVagaQuestaoRespostaVO> getCandidatoVagaQuestaoRespostaVOs() {
		if(candidatoVagaQuestaoRespostaVOs == null){
			candidatoVagaQuestaoRespostaVOs = new ArrayList<CandidatoVagaQuestaoRespostaVO>(0);
		}
		return candidatoVagaQuestaoRespostaVOs;
	}
	public void setCandidatoVagaQuestaoRespostaVOs(List<CandidatoVagaQuestaoRespostaVO> candidatoVagaQuestaoRespostaVOs) {
		this.candidatoVagaQuestaoRespostaVOs = candidatoVagaQuestaoRespostaVOs;
	}
	
	
	public Boolean getTipoQuestaoTextual(){
		return getVagaQuestao().getTipoVagaQuestao().equals(TipoVagaQuestaoEnum.TEXTUAL);
	}
	
	public Boolean getTipoQuestaoUnicaEscolha(){
		return getVagaQuestao().getTipoVagaQuestao().equals(TipoVagaQuestaoEnum.UNICA_ESCOLHA);
	}
	public Boolean getTipoQuestaoMultiplaEscolha(){
		return getVagaQuestao().getTipoVagaQuestao().equals(TipoVagaQuestaoEnum.MULTIPLA_ESCOLHA);
	}
}
