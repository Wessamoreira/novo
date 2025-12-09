package negocio.comuns.planoorcamentario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Repons�vel por manter os dados da entidade Agencia. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os m�todos de acesso a estes atributos. Classe utilizada para apresentar
 * e manter em mem�ria os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ItemSolicitacaoOrcamentoPlanoOrcamentarioVO extends SuperVO {

	public static final long serialVersionUID = 1L;
	private Integer codigo;
	private SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentario;
	private String descricao;
	private CategoriaDespesaVO categoriaDespesa;
	private Integer unidadeEnsino;
	private TurmaVO turma;
	private Double valor;
	private CursoVO cursoVO;
	private TurnoVO turnoVO;
	private BigDecimal valorAutorizado;
	private List<DetalhamentoPeriodoOrcamentoVO> detalhamentoPeriodoOrcamentoVOs;

	// Transiente.
	private Double valorConsumido;
	private Double valorProporcional;
	private MesAnoEnum mesRemanejar;
	private Boolean remanejarParaPropriaCategoriaDespesa;
	

	public ItemSolicitacaoOrcamentoPlanoOrcamentarioVO() {
		super();
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	/**
	 * @param descricao
	 *            the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the categoriaDespesa
	 */
	public CategoriaDespesaVO getCategoriaDespesa() {
		if (categoriaDespesa == null) {
			categoriaDespesa = new CategoriaDespesaVO();
		}
		return categoriaDespesa;
	}

	/**
	 * @param categoriaDespesa
	 *            the categoriaDespesa to set
	 */
	public void setCategoriaDespesa(CategoriaDespesaVO categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}

	/**
	 * @return the unidadeEnsino
	 */
	public Integer getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = 0;
		}
		return unidadeEnsino;
	}

	/**
	 * @param unidadeEnsino
	 *            the unidadeEnsino to set
	 */
	public void setUnidadeEnsino(Integer unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	/**
	 * @return the turma
	 */
	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	/**
	 * @param turma
	 *            the turma to set
	 */
	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	/**
	 * @return the valor
	 */
	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}

	/**
	 * @param valor
	 *            the valor to set
	 */
	public void setValor(Double valor) {
		this.valor = valor;
	}

	/**
	 * @return the solicitacaoOrcamentoPlanoOrcamentario
	 */
	public SolicitacaoOrcamentoPlanoOrcamentarioVO getSolicitacaoOrcamentoPlanoOrcamentario() {
		if (solicitacaoOrcamentoPlanoOrcamentario == null) {
			solicitacaoOrcamentoPlanoOrcamentario = new SolicitacaoOrcamentoPlanoOrcamentarioVO();
		}
		return solicitacaoOrcamentoPlanoOrcamentario;
	}

	/**
	 * @param solicitacaoOrcamentoPlanoOrcamentario
	 *            the solicitacaoOrcamentoPlanoOrcamentario to set
	 */
	public void setSolicitacaoOrcamentoPlanoOrcamentario(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentario) {
		this.solicitacaoOrcamentoPlanoOrcamentario = solicitacaoOrcamentoPlanoOrcamentario;
	}

	/**
	 * @return the codigo
	 */
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	/**
	 * @param codigo
	 *            the codigo to set
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public TurnoVO getTurnoVO() {
		if (turnoVO == null) {
			turnoVO = new TurnoVO();
		}
		return turnoVO;
	}

	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}

	public String getCursoTurno() {
		return (getCursoVO().getCodigo() != null && getCursoVO().getCodigo() != 0)
				&& (getTurnoVO().getCodigo() != null && getTurnoVO().getCodigo() != 0)
						? getCursoVO().getNome().concat("-").concat(getTurnoVO().getNome())
						: "";
	}

	public Double getValorConsumido() {
		if (valorConsumido == null) {
			valorConsumido = 0.0;
		}
		return valorConsumido;
	}

	public void setValorConsumido(Double valorConsumido) {
		this.valorConsumido = valorConsumido;
	}
	
	public Double getValorBaseUtilizar() {
		if(getValorAutorizado().doubleValue() > 0) {
			return getValorAutorizado().doubleValue();
		}
		return getValor();
	}


	public BigDecimal getValorAutorizado() {
		if (valorAutorizado == null) {
			valorAutorizado = BigDecimal.ZERO;
		}
		return valorAutorizado;
	}

	public void setValorAutorizado(BigDecimal valorAutorizado) {
		this.valorAutorizado = valorAutorizado;
	}

	public Double getValorProporcional() {
		if (valorProporcional == null) {
			double porcentagem = ( getValor() / getSolicitacaoOrcamentoPlanoOrcamentario().getValorTotalSolicitado()) * 100;
			valorProporcional = (getValor() * porcentagem) / 100;
		}
		return valorProporcional;
	}

	public void setValorProporcional(Double valorProporcional) {
		this.valorProporcional = valorProporcional;
	}

	public List<DetalhamentoPeriodoOrcamentoVO> getDetalhamentoPeriodoOrcamentoVOs() {
		if(detalhamentoPeriodoOrcamentoVOs == null) {
			detalhamentoPeriodoOrcamentoVOs=  new ArrayList<DetalhamentoPeriodoOrcamentoVO>(0);
		}
		return detalhamentoPeriodoOrcamentoVOs;
	}

	public void setDetalhamentoPeriodoOrcamentoVOs(List<DetalhamentoPeriodoOrcamentoVO> detalhamentoPeriodoOrcamentoVOs) {
		this.detalhamentoPeriodoOrcamentoVOs = detalhamentoPeriodoOrcamentoVOs;
	}

	public MesAnoEnum getMesRemanejar() {		
		return mesRemanejar;
	}

	public void setMesRemanejar(MesAnoEnum mesRemanejar) {
		this.mesRemanejar = mesRemanejar;
	}
	
	public Double getValorDisponivel() {
		return Uteis.arrendondarForcando2CadasDecimais(getValorAutorizado().doubleValue() - getValorConsumido());
	}

	public Boolean getRemanejarParaPropriaCategoriaDespesa() {
		if (remanejarParaPropriaCategoriaDespesa == null) {
			remanejarParaPropriaCategoriaDespesa = Boolean.FALSE;
		}
		return remanejarParaPropriaCategoriaDespesa;
	}

	public void setRemanejarParaPropriaCategoriaDespesa(Boolean remanejarParaPropriaCategoriaDespesa) {
		this.remanejarParaPropriaCategoriaDespesa = remanejarParaPropriaCategoriaDespesa;
	}
}
