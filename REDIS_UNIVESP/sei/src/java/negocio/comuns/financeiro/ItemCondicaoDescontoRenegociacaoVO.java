package negocio.comuns.financeiro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.enumerador.OrdenarItemCondicaoDescontoRenegociacaoUnidadeEnsinoEnum;

public class ItemCondicaoDescontoRenegociacaoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8344083492992233662L;
	private Integer codigo;
	private CondicaoDescontoRenegociacaoVO condicaoDescontoRenegociacaoVO;
	private Integer quantidadeDiasAtrasoParcela;
	private Integer quantidadeDiasAtrasoParcelaFinal;
	private BigDecimal juroIsencao;
	private BigDecimal multaIsencao;
	private Boolean considerarPlanoDescontoPerdidoDevidoDataAntecipacao;
	private BigDecimal percentualPlanoDesconto;
	private Boolean considerarDescontoProgressivoPerdidoDevidoDataAntecipacao;
	private BigDecimal percentualDescontoProgressivo;
	private Boolean considerarDescontoAlunoPerdidoDevidoDataAntecipacao;
	private BigDecimal percentualDescontoAluno;
	private SituacaoEnum situacao;
	private UsuarioVO responsavelCriacaoVO;
	private Date dataCriacao;
	private UsuarioVO responsavelAlteracaoVO;
	private Date dataUltimaAlteracao;
	private List<ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO> itemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs;
	/**
	 * transient
	 */
	private String nomeUnidadeEnsisnoSelecionadas;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public CondicaoDescontoRenegociacaoVO getCondicaoDescontoRenegociacaoVO() {
		if (condicaoDescontoRenegociacaoVO == null) {
			condicaoDescontoRenegociacaoVO = new CondicaoDescontoRenegociacaoVO();
		}
		return condicaoDescontoRenegociacaoVO;
	}

	public void setCondicaoDescontoRenegociacaoVO(CondicaoDescontoRenegociacaoVO condicaoDescontoRenegociacaoVO) {
		this.condicaoDescontoRenegociacaoVO = condicaoDescontoRenegociacaoVO;
	}

	public Integer getQuantidadeDiasAtrasoParcela() {
		if (quantidadeDiasAtrasoParcela == null) {
			quantidadeDiasAtrasoParcela = 0;
		}
		return (quantidadeDiasAtrasoParcela);
	}

	public void setQuantidadeDiasAtrasoParcela(Integer quantidadeDiasAtrasoParcela) {
		this.quantidadeDiasAtrasoParcela = quantidadeDiasAtrasoParcela;
	}

	public Integer getQuantidadeDiasAtrasoParcelaFinal() {
		if (quantidadeDiasAtrasoParcelaFinal == null) {
			quantidadeDiasAtrasoParcelaFinal = 0;
		}
		return (quantidadeDiasAtrasoParcelaFinal);
	}

	public void setQuantidadeDiasAtrasoParcelaFinal(Integer quantidadeDiasAtrasoParcelaFinal) {
		this.quantidadeDiasAtrasoParcelaFinal = quantidadeDiasAtrasoParcelaFinal;
	}

	public BigDecimal getJuroIsencao() {
		if (juroIsencao == null) {
			juroIsencao = BigDecimal.ZERO;
		}
		return juroIsencao;
	}

	public void setJuroIsencao(BigDecimal juroIsencao) {
		this.juroIsencao = juroIsencao;
	}

	public BigDecimal getMultaIsencao() {
		if (multaIsencao == null) {
			multaIsencao = BigDecimal.ZERO;
		}
		return multaIsencao;
	}

	public void setMultaIsencao(BigDecimal multaIsencao) {
		this.multaIsencao = multaIsencao;
	}

	public Boolean getConsiderarPlanoDescontoPerdidoDevidoDataAntecipacao() {
		if (considerarPlanoDescontoPerdidoDevidoDataAntecipacao == null) {
			considerarPlanoDescontoPerdidoDevidoDataAntecipacao = false;
		}
		return considerarPlanoDescontoPerdidoDevidoDataAntecipacao;
	}

	public void setConsiderarPlanoDescontoPerdidoDevidoDataAntecipacao(Boolean considerarPlanoDescontoPerdidoDevidoDataAntecipacao) {
		this.considerarPlanoDescontoPerdidoDevidoDataAntecipacao = considerarPlanoDescontoPerdidoDevidoDataAntecipacao;
	}

	public Boolean getConsiderarDescontoProgressivoPerdidoDevidoDataAntecipacao() {
		if (considerarDescontoProgressivoPerdidoDevidoDataAntecipacao == null) {
			considerarDescontoProgressivoPerdidoDevidoDataAntecipacao = false;
		}
		return considerarDescontoProgressivoPerdidoDevidoDataAntecipacao;
	}

	public void setConsiderarDescontoProgressivoPerdidoDevidoDataAntecipacao(Boolean considerarDescontoProgressivoPerdidoDevidoDataAntecipacao) {
		this.considerarDescontoProgressivoPerdidoDevidoDataAntecipacao = considerarDescontoProgressivoPerdidoDevidoDataAntecipacao;
	}

	public Boolean getConsiderarDescontoAlunoPerdidoDevidoDataAntecipacao() {
		if (considerarDescontoAlunoPerdidoDevidoDataAntecipacao == null) {
			considerarDescontoAlunoPerdidoDevidoDataAntecipacao = false;
		}
		return considerarDescontoAlunoPerdidoDevidoDataAntecipacao;
	}

	public void setConsiderarDescontoAlunoPerdidoDevidoDataAntecipacao(Boolean considerarDescontoAlunoPerdidoDevidoDataAntecipacao) {
		this.considerarDescontoAlunoPerdidoDevidoDataAntecipacao = considerarDescontoAlunoPerdidoDevidoDataAntecipacao;
	}
	
	

	public BigDecimal getPercentualPlanoDesconto() {
		if (percentualPlanoDesconto == null) {
			percentualPlanoDesconto = BigDecimal.ZERO;
		}
		return percentualPlanoDesconto;
	}

	public void setPercentualPlanoDesconto(BigDecimal percentualPlanoDesconto) {
		this.percentualPlanoDesconto = percentualPlanoDesconto;
	}

	public BigDecimal getPercentualDescontoProgressivo() {
		if (percentualDescontoProgressivo == null) {
			percentualDescontoProgressivo = BigDecimal.ZERO;
		}
		return percentualDescontoProgressivo;
	}

	public void setPercentualDescontoProgressivo(BigDecimal percentualDescontoProgressivo) {
		this.percentualDescontoProgressivo = percentualDescontoProgressivo;
	}

	public BigDecimal getPercentualDescontoAluno() {
		if (percentualDescontoAluno == null) {
			percentualDescontoAluno = BigDecimal.ZERO;
		}
		return percentualDescontoAluno;
	}

	public void setPercentualDescontoAluno(BigDecimal percentualDescontoAluno) {
		this.percentualDescontoAluno = percentualDescontoAluno;
	}

	public UsuarioVO getResponsavelCriacaoVO() {
		if (responsavelCriacaoVO == null) {
			responsavelCriacaoVO = new UsuarioVO();
		}
		return responsavelCriacaoVO;
	}

	public void setResponsavelCriacaoVO(UsuarioVO responsavelCriacaoVO) {
		this.responsavelCriacaoVO = responsavelCriacaoVO;
	}

	public Date getDataCriacao() {
		if (dataCriacao == null) {
			dataCriacao = new Date();
		}
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public UsuarioVO getResponsavelAlteracaoVO() {
		if (responsavelAlteracaoVO == null) {
			responsavelAlteracaoVO = new UsuarioVO();
		}
		return responsavelAlteracaoVO;
	}

	public void setResponsavelAlteracaoVO(UsuarioVO responsavelAlteracaoVO) {
		this.responsavelAlteracaoVO = responsavelAlteracaoVO;
	}

	public Date getDataUltimaAlteracao() {
		if (dataUltimaAlteracao == null) {
			dataUltimaAlteracao = new Date();
		}
		return dataUltimaAlteracao;
	}

	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}

	public List<ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO> getItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs() {
		if (itemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs == null) {
			itemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs = new ArrayList<>(0);
		}
		return itemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs;
	}

	public void setItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs(List<ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO> itemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs) {
		this.itemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs = itemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs;
	}

	public String getNomeUnidadeEnsisnoSelecionadas() {
		nomeUnidadeEnsisnoSelecionadas = Optional.ofNullable(nomeUnidadeEnsisnoSelecionadas).orElse("");
		return nomeUnidadeEnsisnoSelecionadas;
	}

	public void setNomeUnidadeEnsisnoSelecionadas(String nomeUnidadeEnsisnoSelecionadas) {
		this.nomeUnidadeEnsisnoSelecionadas = nomeUnidadeEnsisnoSelecionadas;
	}

	public void preencherNomeUnidadeEnsisnoSelecionadas() {
		StringBuilder unidade = new StringBuilder("");
		Collections.sort(getItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs(), OrdenarItemCondicaoDescontoRenegociacaoUnidadeEnsinoEnum.UNIDADE_ENSINO_NOME);
		getItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs().stream().forEach(p -> {
			unidade.append(p.getUnidadeEnsinoVO().getNome()).append(", ");
		});
		setNomeUnidadeEnsisnoSelecionadas(unidade.length() > 0 ? unidade.substring(0, unidade.length() - 2) : "");
	}

	public SituacaoEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoEnum.EM_CONSTRUCAO;
		}
		return situacao;
	}

	public void setSituacao(SituacaoEnum situacao) {
		this.situacao = situacao;
	}

	public boolean equalsItemCondicaoDescontoRenegociacaoVO(ItemCondicaoDescontoRenegociacaoVO obj) {
		return getQuantidadeDiasAtrasoParcela().equals(obj.getQuantidadeDiasAtrasoParcela())
				&& getQuantidadeDiasAtrasoParcelaFinal().equals(obj.getQuantidadeDiasAtrasoParcelaFinal())
		/*
		 * && getJuroIsencao().equals(obj.getJuroIsencao()) && getMultaIsencao().equals(obj.getMultaIsencao()) && getConsiderarPlanoDescontoPerdidoDevidoDataAntecipacao().equals(obj.getConsiderarPlanoDescontoPerdidoDevidoDataAntecipacao()) && getConsiderarDescontoProgressivoPerdidoDevidoDataAntecipacao().equals(obj.getConsiderarDescontoProgressivoPerdidoDevidoDataAntecipacao()) && getConsiderarDescontoAlunoPerdidoDevidoDataAntecipacao().equals(obj.getConsiderarDescontoAlunoPerdidoDevidoDataAntecipacao())
		 */
		;
	}

}
