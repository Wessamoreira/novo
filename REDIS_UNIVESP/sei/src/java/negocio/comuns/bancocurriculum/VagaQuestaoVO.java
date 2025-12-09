package negocio.comuns.bancocurriculum;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.bancocurriculum.enumeradores.TipoVagaQuestaoEnum;

public class VagaQuestaoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7540333131535827190L;

	private Integer codigo;
	private VagasVO vaga;
	private String enunciado;
	private TipoVagaQuestaoEnum tipoVagaQuestao;
	private List<OpcaoRespostaVagaQuestaoVO> opcaoRespostaVagaQuestaoVOs;
	private Integer ordemApresentacao;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public VagasVO getVaga() {
		if (vaga == null) {
			vaga = new VagasVO();
		}
		return vaga;
	}

	public void setVaga(VagasVO vaga) {
		this.vaga = vaga;
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

	public TipoVagaQuestaoEnum getTipoVagaQuestao() {
		if (tipoVagaQuestao == null) {
			tipoVagaQuestao = TipoVagaQuestaoEnum.TEXTUAL;
		}
		return tipoVagaQuestao;
	}

	public void setTipoVagaQuestao(TipoVagaQuestaoEnum tipoVagaQuestao) {
		this.tipoVagaQuestao = tipoVagaQuestao;
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

	public List<OpcaoRespostaVagaQuestaoVO> getOpcaoRespostaVagaQuestaoVOs() {
		if (opcaoRespostaVagaQuestaoVOs == null) {
			opcaoRespostaVagaQuestaoVOs = new ArrayList<OpcaoRespostaVagaQuestaoVO>(0);
		}
		return opcaoRespostaVagaQuestaoVOs;
	}

	public void setOpcaoRespostaVagaQuestaoVOs(List<OpcaoRespostaVagaQuestaoVO> opcaoRespostaVagaQuestaoVOs) {
		this.opcaoRespostaVagaQuestaoVOs = opcaoRespostaVagaQuestaoVOs;
	}

}
