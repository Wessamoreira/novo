package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.financeiro.enumerador.VisaoParcelarEnum;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;

/**
 * 
 * @author Victor Hugo de Paula Costa 20/05/2015 11:17
 *
 */
public class ConfiguracaoFinanceiroCartaoRecebimentoVO extends SuperVO {

	private static final long serialVersionUID = 1323428868377328548L;

	private Integer codigo;
	private Integer parcelasAte;
	private Double valorMinimo;
	private TipoFinanciamentoEnum tipoFinanciamentoEnum;
	private Integer qtdeDiasInicialParcelarContaVencida;
	private Integer qtdeDiasFinalParcelarContaVencida;
	private VisaoParcelarEnum visao;
	private ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO;

	private Boolean tipoOrigemContaReceberMatricula;
	private Boolean tipoOrigemContaReceberMensalidade;
	private Boolean tipoOrigemContaReceberMaterialDidatico;
	private Boolean tipoOrigemContaReceberInscricaoProcessoSeletivo;
	private Boolean tipoOrigemContaReceberRequerimento;
	private Boolean tipoOrigemContaReceberBiblioteca;
	private Boolean tipoOrigemContaReceberNegociacao;
	private Boolean tipoOrigemContaReceberOutros;
	private Boolean tipoOrigemContaReceberDevolucaoCheque;
	private Boolean tipoOrigemContaReceberBolsaCusteada;
	private Boolean tipoOrigemContaReceberContratoReceita;
	private Boolean tipoOrigemContaReceberInclusaoReposicao;
	
	// Transiente
		private Boolean itemEmEdicao;
	
	public ConfiguracaoFinanceiroCartaoRecebimentoVO clone() throws CloneNotSupportedException {
		ConfiguracaoFinanceiroCartaoRecebimentoVO clone = (ConfiguracaoFinanceiroCartaoRecebimentoVO) super.clone();
		clone.setCodigo(0);
		clone.setNovoObj(true);
		return clone;
	}

	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getParcelasAte() {
		if(parcelasAte == null) {
			parcelasAte = 0;
		}
		return parcelasAte;
	}

	public void setParcelasAte(Integer parcelasAte) {
		this.parcelasAte = parcelasAte;
	}

	public Double getValorMinimo() {
		if(valorMinimo == null) {
			valorMinimo = 0.0;
		}
		return valorMinimo;
	}

	public void setValorMinimo(Double valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public TipoFinanciamentoEnum getTipoFinanciamentoEnum() {
		if(tipoFinanciamentoEnum == null) {
			tipoFinanciamentoEnum = TipoFinanciamentoEnum.INSTITUICAO;
		}
		return tipoFinanciamentoEnum;
	}

	public void setTipoFinanciamentoEnum(TipoFinanciamentoEnum tipoFinanciamentoEnum) {
		this.tipoFinanciamentoEnum = tipoFinanciamentoEnum;
	}

	public ConfiguracaoRecebimentoCartaoOnlineVO getConfiguracaoRecebimentoCartaoOnlineVO() {
		if(configuracaoRecebimentoCartaoOnlineVO == null) {
			configuracaoRecebimentoCartaoOnlineVO = new ConfiguracaoRecebimentoCartaoOnlineVO();
		}
		return configuracaoRecebimentoCartaoOnlineVO;
	}

	public void setConfiguracaoRecebimentoCartaoOnlineVO(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO) {
		this.configuracaoRecebimentoCartaoOnlineVO = configuracaoRecebimentoCartaoOnlineVO;
	}
	
	public Integer getQtdeDiasInicialParcelarContaVencida() {
		if (qtdeDiasInicialParcelarContaVencida == null) {
			qtdeDiasInicialParcelarContaVencida = 0;
		}
		return qtdeDiasInicialParcelarContaVencida;
	}

	public void setQtdeDiasInicialParcelarContaVencida(Integer qtdeDiasInicialParcelarContaVencida) {
		this.qtdeDiasInicialParcelarContaVencida = qtdeDiasInicialParcelarContaVencida;
	}

	public Integer getQtdeDiasFinalParcelarContaVencida() {
		if (qtdeDiasFinalParcelarContaVencida == null) {
			qtdeDiasFinalParcelarContaVencida = 0;
		}
		return qtdeDiasFinalParcelarContaVencida;
	}

	public void setQtdeDiasFinalParcelarContaVencida(Integer qtdeDiasFinalParcelarContaVencida) {
		this.qtdeDiasFinalParcelarContaVencida = qtdeDiasFinalParcelarContaVencida;
	}

	public VisaoParcelarEnum getVisao() {
		if (visao == null) {
			visao = VisaoParcelarEnum.ADMINISTRATIVA;
		}
		return visao;
	}

	public void setVisao(VisaoParcelarEnum visao) {
		this.visao = visao;
	}

	public Boolean getTipoOrigemContaReceberMatricula() {
		if (tipoOrigemContaReceberMatricula == null) {
			tipoOrigemContaReceberMatricula = Boolean.TRUE;
		}
		return tipoOrigemContaReceberMatricula;
	}

	public void setTipoOrigemContaReceberMatricula(Boolean tipoOrigemContaReceberMatricula) {
		this.tipoOrigemContaReceberMatricula = tipoOrigemContaReceberMatricula;
	}

	public Boolean getTipoOrigemContaReceberMensalidade() {
		if (tipoOrigemContaReceberMensalidade == null) {
			tipoOrigemContaReceberMensalidade = Boolean.TRUE;
		}
		return tipoOrigemContaReceberMensalidade;
	}

	public void setTipoOrigemContaReceberMensalidade(Boolean tipoOrigemContaReceberMensalidade) {
		this.tipoOrigemContaReceberMensalidade = tipoOrigemContaReceberMensalidade;
	}

	public Boolean getTipoOrigemContaReceberMaterialDidatico() {
		if (tipoOrigemContaReceberMaterialDidatico == null) {
			tipoOrigemContaReceberMaterialDidatico = Boolean.TRUE;
		}
		return tipoOrigemContaReceberMaterialDidatico;
	}

	public void setTipoOrigemContaReceberMaterialDidatico(Boolean tipoOrigemContaReceberMaterialDidatico) {
		this.tipoOrigemContaReceberMaterialDidatico = tipoOrigemContaReceberMaterialDidatico;
	}

	public Boolean getTipoOrigemContaReceberInscricaoProcessoSeletivo() {
		if (tipoOrigemContaReceberInscricaoProcessoSeletivo == null) {
			tipoOrigemContaReceberInscricaoProcessoSeletivo = Boolean.TRUE;
		}
		return tipoOrigemContaReceberInscricaoProcessoSeletivo;
	}

	public void setTipoOrigemContaReceberInscricaoProcessoSeletivo(
			Boolean tipoOrigemContaReceberInscricaoProcessoSeletivo) {
		this.tipoOrigemContaReceberInscricaoProcessoSeletivo = tipoOrigemContaReceberInscricaoProcessoSeletivo;
	}

	public Boolean getTipoOrigemContaReceberRequerimento() {
		if (tipoOrigemContaReceberRequerimento == null) {
			tipoOrigemContaReceberRequerimento = Boolean.TRUE;
		}
		return tipoOrigemContaReceberRequerimento;
	}

	public void setTipoOrigemContaReceberRequerimento(Boolean tipoOrigemContaReceberRequerimento) {
		this.tipoOrigemContaReceberRequerimento = tipoOrigemContaReceberRequerimento;
	}

	public Boolean getTipoOrigemContaReceberBiblioteca() {
		if (tipoOrigemContaReceberBiblioteca == null) {
			tipoOrigemContaReceberBiblioteca = Boolean.TRUE;
		}
		return tipoOrigemContaReceberBiblioteca;
	}

	public void setTipoOrigemContaReceberBiblioteca(Boolean tipoOrigemContaReceberBiblioteca) {
		this.tipoOrigemContaReceberBiblioteca = tipoOrigemContaReceberBiblioteca;
	}

	public Boolean getTipoOrigemContaReceberNegociacao() {
		if (tipoOrigemContaReceberNegociacao == null) {
			tipoOrigemContaReceberNegociacao = Boolean.TRUE;
		}
		return tipoOrigemContaReceberNegociacao;
	}

	public void setTipoOrigemContaReceberNegociacao(Boolean tipoOrigemContaReceberNegociacao) {
		this.tipoOrigemContaReceberNegociacao = tipoOrigemContaReceberNegociacao;
	}

	public Boolean getTipoOrigemContaReceberOutros() {
		if (tipoOrigemContaReceberOutros == null) {
			tipoOrigemContaReceberOutros = Boolean.TRUE;
		}
		return tipoOrigemContaReceberOutros;
	}

	public void setTipoOrigemContaReceberOutros(Boolean tipoOrigemContaReceberOutros) {
		this.tipoOrigemContaReceberOutros = tipoOrigemContaReceberOutros;
	}

	public Boolean getTipoOrigemContaReceberDevolucaoCheque() {
		if (tipoOrigemContaReceberDevolucaoCheque == null) {
			tipoOrigemContaReceberDevolucaoCheque = Boolean.TRUE;
		}
		return tipoOrigemContaReceberDevolucaoCheque;
	}

	public void setTipoOrigemContaReceberDevolucaoCheque(Boolean tipoOrigemContaReceberDevolucaoCheque) {
		this.tipoOrigemContaReceberDevolucaoCheque = tipoOrigemContaReceberDevolucaoCheque;
	}

	public Boolean getTipoOrigemContaReceberBolsaCusteada() {
		if (tipoOrigemContaReceberBolsaCusteada == null) {
			tipoOrigemContaReceberBolsaCusteada = Boolean.TRUE;
		}
		return tipoOrigemContaReceberBolsaCusteada;
	}

	public void setTipoOrigemContaReceberBolsaCusteada(Boolean tipoOrigemContaReceberBolsaCusteada) {
		this.tipoOrigemContaReceberBolsaCusteada = tipoOrigemContaReceberBolsaCusteada;
	}

	public Boolean getTipoOrigemContaReceberContratoReceita() {
		if (tipoOrigemContaReceberContratoReceita == null) {
			tipoOrigemContaReceberContratoReceita = Boolean.TRUE;
		}
		return tipoOrigemContaReceberContratoReceita;
	}

	public void setTipoOrigemContaReceberContratoReceita(Boolean tipoOrigemContaReceberContratoReceita) {
		this.tipoOrigemContaReceberContratoReceita = tipoOrigemContaReceberContratoReceita;
	}

	public Boolean getTipoOrigemContaReceberInclusaoReposicao() {
		if (tipoOrigemContaReceberInclusaoReposicao == null) {
			tipoOrigemContaReceberInclusaoReposicao = Boolean.TRUE;
		}
		return tipoOrigemContaReceberInclusaoReposicao;
	}

	public void setTipoOrigemContaReceberInclusaoReposicao(Boolean tipoOrigemContaReceberInclusaoReposicao) {
		this.tipoOrigemContaReceberInclusaoReposicao = tipoOrigemContaReceberInclusaoReposicao;
	}
	
	public Boolean getItemEmEdicao() {
		if (itemEmEdicao == null) {
			itemEmEdicao = false;
		}
		return itemEmEdicao;
	}

	public void setItemEmEdicao(Boolean itemEmEdicao) {
		this.itemEmEdicao = itemEmEdicao;
	}
	
	private String descricaoOrigemParcelas;
	
	public String getDescricaoOrigemParcelas() {
		if(descricaoOrigemParcelas == null) {
		StringBuilder origem = new StringBuilder("");
		if(getTipoOrigemContaReceberBiblioteca()) {
			if(origem.length() > 0) {
				origem.append(", ");
}
			origem.append(TipoOrigemContaReceber.BIBLIOTECA.getDescricao());
		}
		if(getTipoOrigemContaReceberBolsaCusteada()) {
			if(origem.length() > 0) {
				origem.append(", ");
			}
			origem.append(TipoOrigemContaReceber.BOLSA_CUSTEADA_CONVENIO.getDescricao());
		}
		if(getTipoOrigemContaReceberContratoReceita()) {
			if(origem.length() > 0) {
				origem.append(", ");
			}
			origem.append(TipoOrigemContaReceber.CONTRATO_RECEITA.getDescricao());
		}
		if(getTipoOrigemContaReceberDevolucaoCheque()) {
			if(origem.length() > 0) {
				origem.append(", ");
			}
			origem.append(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getDescricao());
		}
		if(getTipoOrigemContaReceberInclusaoReposicao()) {
			if(origem.length() > 0) {
				origem.append(", ");
			}
			origem.append(TipoOrigemContaReceber.INCLUSAOREPOSICAO.getDescricao());
		}
		if(getTipoOrigemContaReceberInscricaoProcessoSeletivo()) {
			if(origem.length() > 0) {
				origem.append(", ");
			}
			origem.append(TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO.getDescricao());
		}
		if(getTipoOrigemContaReceberMaterialDidatico()) {
			if(origem.length() > 0) {
				origem.append(", ");
			}
			origem.append(TipoOrigemContaReceber.MATERIAL_DIDATICO.getDescricao());
		}
		if(getTipoOrigemContaReceberMatricula()) {
			if(origem.length() > 0) {
				origem.append(", ");
			}
			origem.append(TipoOrigemContaReceber.MATRICULA.getDescricao());
		}
		if(getTipoOrigemContaReceberMensalidade()) {
			if(origem.length() > 0) {
				origem.append(", ");
			}
			origem.append(TipoOrigemContaReceber.MENSALIDADE.getDescricao());
		}
		if(getTipoOrigemContaReceberNegociacao()) {
			if(origem.length() > 0) {
				origem.append(", ");
			}
			origem.append(TipoOrigemContaReceber.NEGOCIACAO.getDescricao());
		}
		if(getTipoOrigemContaReceberOutros()) {
			if(origem.length() > 0) {
				origem.append(", ");
			}
			origem.append(TipoOrigemContaReceber.OUTROS.getDescricao());
		}
		if(getTipoOrigemContaReceberRequerimento()) {
			if(origem.length() > 0) {
				origem.append(", ");
			}
			origem.append(TipoOrigemContaReceber.REQUERIMENTO.getDescricao());
		}
		descricaoOrigemParcelas = origem.toString();
		}
		return descricaoOrigemParcelas;
	}
	
	public List<String> listaDescricaoOrigemParcelas;
	
	
	
	public void setDescricaoOrigemParcelas(String descricaoOrigemParcelas) {
		this.descricaoOrigemParcelas = descricaoOrigemParcelas;
	}

	public void setListaDescricaoOrigemParcelas(List<String> listaDescricaoOrigemParcelas) {
		this.listaDescricaoOrigemParcelas = listaDescricaoOrigemParcelas;
	}

	public List<String> getListaDescricaoOrigemParcelas() {
		if(listaDescricaoOrigemParcelas == null) {
			listaDescricaoOrigemParcelas = new ArrayList<String>(0);
		if(getTipoOrigemContaReceberBiblioteca()) {			
			listaDescricaoOrigemParcelas.add(TipoOrigemContaReceber.BIBLIOTECA.getDescricao());
		}
		if(getTipoOrigemContaReceberBolsaCusteada()) {
			
			listaDescricaoOrigemParcelas.add(TipoOrigemContaReceber.BOLSA_CUSTEADA_CONVENIO.getDescricao());
		}
		if(getTipoOrigemContaReceberContratoReceita()) {
			
			listaDescricaoOrigemParcelas.add(TipoOrigemContaReceber.CONTRATO_RECEITA.getDescricao());
		}
		if(getTipoOrigemContaReceberDevolucaoCheque()) {


			listaDescricaoOrigemParcelas.add(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getDescricao());
		}
		if(getTipoOrigemContaReceberInclusaoReposicao()) {
			listaDescricaoOrigemParcelas.add(TipoOrigemContaReceber.INCLUSAOREPOSICAO.getDescricao());
		}
		if(getTipoOrigemContaReceberInscricaoProcessoSeletivo()) {
			listaDescricaoOrigemParcelas.add(TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO.getDescricao());
		}
		if(getTipoOrigemContaReceberMaterialDidatico()) {
			listaDescricaoOrigemParcelas.add(TipoOrigemContaReceber.MATERIAL_DIDATICO.getDescricao());
		}
		if(getTipoOrigemContaReceberMatricula()) {
			listaDescricaoOrigemParcelas.add(TipoOrigemContaReceber.MATRICULA.getDescricao());
		}
		if(getTipoOrigemContaReceberMensalidade()) {
			listaDescricaoOrigemParcelas.add(TipoOrigemContaReceber.MENSALIDADE.getDescricao());
		}
		if(getTipoOrigemContaReceberNegociacao()) {
			listaDescricaoOrigemParcelas.add(TipoOrigemContaReceber.NEGOCIACAO.getDescricao());
		}
		if(getTipoOrigemContaReceberOutros()) {
			listaDescricaoOrigemParcelas.add(TipoOrigemContaReceber.OUTROS.getDescricao());
		}
		if(getTipoOrigemContaReceberRequerimento()) {
			listaDescricaoOrigemParcelas.add(TipoOrigemContaReceber.REQUERIMENTO.getDescricao());
		}
		
		}
		return listaDescricaoOrigemParcelas;
	}
}
