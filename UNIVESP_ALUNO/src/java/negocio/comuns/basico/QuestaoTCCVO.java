package negocio.comuns.basico;

import negocio.comuns.administrativo.ConfiguracaoTCCVO;
import negocio.comuns.arquitetura.SuperVO;

public class QuestaoTCCVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7540333131535827190L;

	private Integer codigo;
	private String tipoPergunta;
	private ConfiguracaoTCCVO configuracao;
	private String enunciado;
	private String origemQuestao;
	private Double valorNotaMaximo;
	private Integer ordemApresentacao;
	

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ConfiguracaoTCCVO getConfiguracao() {
		if (configuracao == null) {
			configuracao = new ConfiguracaoTCCVO();
		}
		return configuracao;
	}

	public void setConfiguracao(ConfiguracaoTCCVO configuracao) {
		this.configuracao = configuracao;
	}

	public String getEnunciado() {
		if (enunciado == null) {
			enunciado = "";
		}
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public Double getValorNotaMaximo() {
		return valorNotaMaximo;
	}

	public void setValorNotaMaximo(Double valorNotaMaximo) {
		this.valorNotaMaximo = valorNotaMaximo;
	}

	public Integer getOrdemApresentacao() {
		if (ordemApresentacao == null) {
			ordemApresentacao = 0;
		}
		return ordemApresentacao;
	}

	public void setOrdemApresentacao(Integer ordemApresentacao) {
		this.ordemApresentacao = ordemApresentacao;
	}

//	public List<OpcaoRespostaVagaQuestaoVO> getOpcaoRespostaVagaQuestaoVOs() {
//		if (opcaoRespostaVagaQuestaoVOs == null) {
//			opcaoRespostaVagaQuestaoVOs = new ArrayList<OpcaoRespostaVagaQuestaoVO>(0);
//		}
//		return opcaoRespostaVagaQuestaoVOs;
//	}
//
//	public void setOpcaoRespostaVagaQuestaoVOs(List<OpcaoRespostaVagaQuestaoVO> opcaoRespostaVagaQuestaoVOs) {
//		this.opcaoRespostaVagaQuestaoVOs = opcaoRespostaVagaQuestaoVOs;
//	}

	public String getTipoPergunta() {
		if (tipoPergunta == null) {
			tipoPergunta = "conteudo";
		}
		return tipoPergunta;
	}

	public void setTipoPergunta(String tipoPergunta) {
		this.tipoPergunta = tipoPergunta;
	}

	public String getOrigemQuestao() {
		if (origemQuestao == null) {
			origemQuestao = "";
		}
		return origemQuestao;
	}

	public void setOrigemQuestao(String origemQuestao) {
		this.origemQuestao = origemQuestao;
	}

}
