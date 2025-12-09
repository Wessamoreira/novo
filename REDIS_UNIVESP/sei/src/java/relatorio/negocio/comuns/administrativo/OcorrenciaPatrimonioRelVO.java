/**
 * 
 */
package relatorio.negocio.comuns.administrativo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.OcorrenciaPatrimonioVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;

/**
 * @author Leonardo Riciolle
 *
 */
public class OcorrenciaPatrimonioRelVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO;
 	private Boolean manutencao;
	private Boolean emprestimo;
	private Boolean trocaLocal;
	private Boolean separadoParaDescarte;
	private Boolean descartado;
	private Boolean incluirSubLocal;
	private List<PatrimonioUnidadeVO> patrimonioUnidadeVOs;
	private List<LocalArmazenamentoVO> localArmazenamentoVOs;

	public Boolean getManutencao() {
		if (manutencao == null) {
			manutencao = false;
		}
		return manutencao;
	}

	public void setManutencao(Boolean manutencao) {
		this.manutencao = manutencao;
	}

	public Boolean getEmprestimo() {
		if (emprestimo == null) {
			emprestimo = false;
		}
		return emprestimo;
	}

	public void setEmprestimo(Boolean emprestimo) {
		this.emprestimo = emprestimo;
	}

	public Boolean getTrocaLocal() {
		if (trocaLocal == null) {
			trocaLocal = false;
		}
		return trocaLocal;
	}

	public void setTrocaLocal(Boolean trocaLocal) {
		this.trocaLocal = trocaLocal;
	}

	public Boolean getSeparadoParaDescarte() {
		if (separadoParaDescarte == null) {
			separadoParaDescarte = false;
		}
		return separadoParaDescarte;
	}

	public void setSeparadoParaDescarte(Boolean separadoParaDescarte) {
		this.separadoParaDescarte = separadoParaDescarte;
	}

	public Boolean getDescartado() {
		if (descartado == null) {
			descartado = false;
		}
		return descartado;
	}

	public void setDescartado(Boolean descartado) {
		this.descartado = descartado;
	}

	public Boolean getIncluirSubLocal() {
		if (incluirSubLocal == null) {
			incluirSubLocal = false;
		}
		return incluirSubLocal;
	}

	public void setIncluirSubLocal(Boolean incluirSubLocal) {
		this.incluirSubLocal = incluirSubLocal;
	}

	public OcorrenciaPatrimonioVO getOcorrenciaPatrimonioVO() {
		if (ocorrenciaPatrimonioVO == null) {
			ocorrenciaPatrimonioVO = new OcorrenciaPatrimonioVO();
		}
		return ocorrenciaPatrimonioVO;
	}

	public void setOcorrenciaPatrimonioVO(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO) {
		this.ocorrenciaPatrimonioVO = ocorrenciaPatrimonioVO;
	}
	public List<PatrimonioUnidadeVO> getPatrimonioUnidadeVOs() {
		if (patrimonioUnidadeVOs == null) {
			patrimonioUnidadeVOs = new ArrayList<PatrimonioUnidadeVO>(0);
		}
		return patrimonioUnidadeVOs;
	}

	public void setPatrimonioUnidadeVOs(List<PatrimonioUnidadeVO> patrimonioUnidadeVOs) {
		this.patrimonioUnidadeVOs = patrimonioUnidadeVOs;
	}

	public List<LocalArmazenamentoVO> getLocalArmazenamentoVOs() {
		if(localArmazenamentoVOs == null ){
			localArmazenamentoVOs = new ArrayList<LocalArmazenamentoVO>();
		}
		return localArmazenamentoVOs;
	}

	public void setLocalArmazenamentoVOs(List<LocalArmazenamentoVO> localArmazenamentoVOs) {
		this.localArmazenamentoVOs = localArmazenamentoVOs;
	}
}
