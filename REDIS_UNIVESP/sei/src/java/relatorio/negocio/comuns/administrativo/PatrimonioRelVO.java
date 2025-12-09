/**
 * 
 */
package relatorio.negocio.comuns.administrativo;

import java.io.Serializable;
import java.math.BigDecimal;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.PatrimonioVO;

/**
 * @author Leonardo Riciolle
 *
 */
public class PatrimonioRelVO extends SuperVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private PatrimonioVO patrimonioVO;
	private LocalArmazenamentoVO localArmazenamentoVO;
	private Integer qtdEmUso;
	private Integer qtdManutencao;
	private Integer qtdEmprestado;
	private Integer qtdSeparadoParaDescarte;
	private Integer qtdDescartado;
	private BigDecimal valorRecurso;
	private Boolean emUso;
	private Boolean manutencao;
	private Boolean emprestado;
	private Boolean separadoParaDescarte;
	private Boolean descartado;
	private Boolean incluirSubLocal;

	public PatrimonioVO getPatrimonioVO() {
		if (patrimonioVO == null) {
			patrimonioVO = new PatrimonioVO();
		}
		return patrimonioVO;
	}

	public void setPatrimonioVO(PatrimonioVO patrimonioVO) {
		this.patrimonioVO = patrimonioVO;
	}

	public Boolean getEmUso() {
		if (emUso == null) {
			emUso = false;
		}
		return emUso;
	}

	public void setEmUso(Boolean emUso) {
		this.emUso = emUso;
	}

	public Boolean getManutencao() {
		if (manutencao == null) {
			manutencao = false;
		}
		return manutencao;
	}

	public void setManutencao(Boolean manutencao) {
		this.manutencao = manutencao;
	}

	public Boolean getEmprestado() {
		if (emprestado == null) {
			emprestado = false;
		}
		return emprestado;
	}

	public void setEmprestado(Boolean emprestado) {
		this.emprestado = emprestado;
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

	public Integer getQtdEmUso() {
		if (qtdEmUso == null) {
			qtdEmUso = 0;
		}
		return qtdEmUso;
	}

	public void setQtdEmUso(Integer qtdEmUso) {
		this.qtdEmUso = qtdEmUso;
	}

	public Integer getQtdManutencao() {
		if (qtdManutencao == null) {
			qtdManutencao = 0;
		}
		return qtdManutencao;
	}

	public void setQtdManutencao(Integer qtdManutencao) {
		this.qtdManutencao = qtdManutencao;
	}

	public Integer getQtdEmprestado() {
		if (qtdEmprestado == null) {
			qtdEmprestado = 0;
		}
		return qtdEmprestado;
	}

	public void setQtdEmprestado(Integer qtdEmprestado) {
		this.qtdEmprestado = qtdEmprestado;
	}

	public Integer getQtdSeparadoParaDescarte() {
		if (qtdSeparadoParaDescarte == null) {
			qtdSeparadoParaDescarte = 0;
		}
		return qtdSeparadoParaDescarte;
	}

	public void setQtdSeparadoParaDescarte(Integer qtdSeparadoParaDescarte) {
		this.qtdSeparadoParaDescarte = qtdSeparadoParaDescarte;
	}

	public Integer getQtdDescartado() {
		if (qtdDescartado == null) {
			qtdDescartado = 0;
		}
		return qtdDescartado;
	}

	public void setQtdDescartado(Integer qtdDescartado) {
		this.qtdDescartado = qtdDescartado;
	}

	public BigDecimal getValorRecurso() {
		if (valorRecurso == null) {
			valorRecurso = BigDecimal.ZERO;
		}
		return valorRecurso;
	}

	public void setValorRecurso(BigDecimal valorRecurso) {
		this.valorRecurso = valorRecurso;
	}

	public LocalArmazenamentoVO getLocalArmazenamentoVO() {
		if (localArmazenamentoVO == null) {
			localArmazenamentoVO = new LocalArmazenamentoVO();
		}
		return localArmazenamentoVO;
	}

	public void setLocalArmazenamentoVO(LocalArmazenamentoVO localArmazenamentoVO) {
		this.localArmazenamentoVO = localArmazenamentoVO;
	}

}
