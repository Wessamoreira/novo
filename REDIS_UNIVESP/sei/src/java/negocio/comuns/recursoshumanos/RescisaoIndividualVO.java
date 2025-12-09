package negocio.comuns.recursoshumanos;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;

public class RescisaoIndividualVO extends SuperVO {

	private static final long serialVersionUID = 6764892924967035445L;

	private Integer codigo;
	private RescisaoVO rescisao;
	private FuncionarioCargoVO funcionarioCargo;
	private HistoricoSituacaoVO historicoSituacao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public RescisaoVO getRescisao() {
		if (rescisao == null) {
			rescisao = new RescisaoVO();
		}
		return rescisao;
	}

	public void setRescisao(RescisaoVO rescisao) {
		this.rescisao = rescisao;
	}

	public FuncionarioCargoVO getFuncionarioCargo() {
		if (funcionarioCargo == null) {
			funcionarioCargo = new FuncionarioCargoVO();
		}
		return funcionarioCargo;
	}

	public void setFuncionarioCargo(FuncionarioCargoVO funcionarioCargo) {
		this.funcionarioCargo = funcionarioCargo;
	}

	public HistoricoSituacaoVO getHistoricoSituacao() {
		if (historicoSituacao == null) {
			historicoSituacao = new HistoricoSituacaoVO();
		}
		return historicoSituacao;
	}

	public void setHistoricoSituacao(HistoricoSituacaoVO historicoSituacao) {
		this.historicoSituacao = historicoSituacao;
	}

}
