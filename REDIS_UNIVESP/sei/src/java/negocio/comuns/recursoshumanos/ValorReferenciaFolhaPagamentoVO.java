package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoValorReferenciaEnum;
import negocio.comuns.recursoshumanos.enumeradores.ValorFixoEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade ValorReferenciaFolhaPagamento.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ValorReferenciaFolhaPagamentoVO extends SuperVO {

	private static final long serialVersionUID = 8656220902564295248L;

	private Integer codigo;
	private String identificador;
	private String descricao;
	private ImpostoVO imposto;
	private Date dataInicioVigencia;
	private Date dataFimVigencia;
	private boolean valorFixo;
	private BigDecimal valor;
	private ValorFixoEnum referencia;
	private boolean atualizarFinalVigencia;
	private SituacaoEnum situacao;
	private UsuarioVO usuarioUltimaAlteracao;
	private Date dataUltimaAlteracao;
	private String sql;
	private TipoValorReferenciaEnum tipoValorReferencia;
	private Boolean valorReferenciaPadrao;

	private List<FaixaValorVO> listaFaixaValores;

	// Transiente
	private Boolean itemEmEdicao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getIdentificador() {
		if (identificador == null) {
			identificador = "";
		}
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public ImpostoVO getImposto() {
		if (imposto == null) {
			imposto = new ImpostoVO();
		}
		return imposto;
	}

	public void setImposto(ImpostoVO imposto) {
		this.imposto = imposto;
	}

	public Date getDataInicioVigencia() {
		if (dataInicioVigencia == null) {
			dataInicioVigencia = new Date();
		}
		return dataInicioVigencia;
	}

	public void setDataInicioVigencia(Date dataInicioVigencia) {
		this.dataInicioVigencia = dataInicioVigencia;
	}

	public Date getDataFimVigencia() {
		if (dataFimVigencia == null) {
			dataFimVigencia = new Date();
		}
		return dataFimVigencia;
	}

	public void setDataFimVigencia(Date dataFimVigencia) {
		this.dataFimVigencia = dataFimVigencia;
	}

	public List<FaixaValorVO> getListaFaixaValores() {
		if (listaFaixaValores == null) {
			listaFaixaValores = new ArrayList<>(0);
		}
		return listaFaixaValores;
	}

	public void setListaFaixaValores(List<FaixaValorVO> listaFaixaValores) {
		this.listaFaixaValores = listaFaixaValores;
	}

	public boolean isValorFixo() {
		return valorFixo;
	}

	public void setValorFixo(boolean valorFixo) {
		this.valorFixo = valorFixo;
	}

	public BigDecimal getValor() {
		if (valor == null) {
			valor = BigDecimal.ZERO;
		}
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public ValorFixoEnum getReferencia() {
		if(referencia == null)
			referencia = ValorFixoEnum.OUTRAS_FINALIDADES;
		return referencia;
	}

	public void setReferencia(ValorFixoEnum referencia) {
		this.referencia = referencia;
	}

	public boolean getAtualizarFinalVigencia() {
		if (!Uteis.isAtributoPreenchido(atualizarFinalVigencia)) {
			atualizarFinalVigencia = false;
		}
		return atualizarFinalVigencia;
	}

	public void setAtualizarFinalVigencia(boolean atualizarFinalVigencia) {
		this.atualizarFinalVigencia = atualizarFinalVigencia;
	}

	public SituacaoEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoEnum.ATIVO;
		}
		return situacao;
	}

	public void setSituacao(SituacaoEnum situacao) {
		this.situacao = situacao;
	}

	public UsuarioVO getUsuarioUltimaAlteracao() {
		if (usuarioUltimaAlteracao == null) {
			usuarioUltimaAlteracao = new UsuarioVO();
		}
		return usuarioUltimaAlteracao;
	}

	public void setUsuarioUltimaAlteracao(UsuarioVO usuarioUltimaAlteracao) {
		this.usuarioUltimaAlteracao = usuarioUltimaAlteracao;
	}

	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}

	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}

	public String getSql() {
		if (sql == null) {
			sql = "";
		}
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public TipoValorReferenciaEnum getTipoValorReferencia() {
		if(tipoValorReferencia == null)
			tipoValorReferencia = TipoValorReferenciaEnum.FAIXA_VALOR;
		return tipoValorReferencia;
	}

	public void setTipoValorReferencia(TipoValorReferenciaEnum tipoValorReferencia) {
		this.tipoValorReferencia = tipoValorReferencia;
	}

	public Boolean getItemEmEdicao() {
		if (itemEmEdicao == null) {
			itemEmEdicao = Boolean.FALSE;
		}
		return itemEmEdicao;
	}

	public Boolean getValorReferenciaPadrao() {
		if (valorReferenciaPadrao == null) {
			valorReferenciaPadrao = Boolean.FALSE;
		}
		return valorReferenciaPadrao;
	}

	public void setValorReferenciaPadrao(Boolean valorReferenciaPadrao) {
		this.valorReferenciaPadrao = valorReferenciaPadrao;
	}

	public void setItemEmEdicao(Boolean itemEmEdicao) {
		this.itemEmEdicao = itemEmEdicao;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("Valor de Referencia: \n ");
		str.append(" Identificador=").append(getIdentificador()).append(", Descricao=").append(getDescricao());

		str.append(", Referencia=").append(getReferencia().getValorApresentar());
		
		if(valorFixo)
			str.append(", Valor Fixo=").append(valorFixo).append(", valor=").append(getValor());
		
		if(getTipoValorReferencia().equals(TipoValorReferenciaEnum.SQL))
			str.append(", SQL=").append(sql);
		
		return str.toString();
	}
	
}