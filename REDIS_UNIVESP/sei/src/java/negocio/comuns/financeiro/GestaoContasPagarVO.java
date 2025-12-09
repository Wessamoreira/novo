package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.enumerador.GestaoContasPagarOperacaoEnum;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

public class GestaoContasPagarVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2969181596069578735L;
	private Integer codigo;	
	private GestaoContasPagarOperacaoEnum gestaoContasPagarOperacaoEnum;
	private UsuarioVO usuarioOperacao;
	private Date dataRegistro;
	private String descricaoOperacao;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	// filtros
	private List<GestaoContasPagarVO> listaGestaoContasPagarLogs;
	private OrigemContaPagar origemContaPagarEnum;
	private String valorConsulta;
	private String campoConsulta;
	private Date dataVencimentoInicio;
	private Date dataVencimentoFim;
	private Date dataOperacaoInicio;
	private Date dataOperacaoFim;
	private Double valorFaixaInicio;
	private Double valorFaixaFim;
	private Date dataVencimentoAlterar;	
	private Double valorAlterar;
	private Date dataVencimentoAntesAlteracaoLog;	
	private Double valorAntesAlteracaoLog;
	private boolean contasMesmoFavorecidoOrigemAvancarParcelasMesSubsquente = false;
	private Date dataOperacaoAlterar;
	private FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO;
	private ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO;
	private boolean desconsiderarConciliacaoBancaria = false;
	private String motivoAlteracao;
	private Map<Integer, List<String>>mapaUnidadeEnsinoLog;
	private Map<Integer, List<String>>mapaUnidadeEnsinoLogErro;
	
	
	
	
	public void inicializarDados(UsuarioVO usuario) {
		setDataRegistro(new Date());
		setUsuarioOperacao(usuario);
		setDescricaoOperacao("");
		getMapaUnidadeEnsinoLog().clear();	
		getMapaUnidadeEnsinoLogErro().clear();	
		getListaGestaoContasPagarLogs().clear();
	}
	public void preencherCabecalhoOperacao(String titulo) {
		StringBuilder dados = new StringBuilder();
		dados.append(System.lineSeparator());
		dados.append("-------------------------------");
		dados.append(titulo);
		dados.append("-------------------------------");
		setDescricaoOperacao(getDescricaoOperacao() + dados.toString());
	}
	
	public String getPreencherCamposDescricaoOperacao(ContaPagarVO contaPagarVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(System.lineSeparator());
		sb.append(" Nrº:").append(contaPagarVO.getCodigo());
		sb.append(" - Favorecido:").append(contaPagarVO.getFavorecido());
		sb.append(" - Doc:").append(contaPagarVO.getNrDocumento());
		sb.append(" - Parc:").append(contaPagarVO.getParcela());
		if(getGestaoContasPagarOperacaoEnum().isAlteracao() || getGestaoContasPagarOperacaoEnum().isPagamento()) {
			if(getDataVencimentoAntesAlteracaoLog() != null && !UteisData.getCompararDatas(getDataVencimentoAntesAlteracaoLog(), contaPagarVO.getDataVencimento())) {
				sb.append(" - Venc-Antes:").append(Uteis.getData(getDataVencimentoAntesAlteracaoLog()));	
			}
			sb.append(" - Venc:").append(contaPagarVO.getDataVencimento_Apresentar());
			if(getValorAntesAlteracaoLog() != null && !getValorAntesAlteracaoLog().equals(contaPagarVO.getValor())) {
				sb.append(" - Valor-Antes:R$").append(Uteis.arrendondarForcando2CadasDecimaisStrComSepador(getValorAntesAlteracaoLog(), ","));	
			}
			sb.append(" - Valor:R$").append(Uteis.arrendondarForcando2CadasDecimaisStrComSepador(contaPagarVO.getValor(), ","));
		}else {
			sb.append(" - Venc:").append(contaPagarVO.getDataVencimento_Apresentar());
			sb.append(" - Valor:R$").append(Uteis.arrendondarForcando2CadasDecimaisStrComSepador(contaPagarVO.getValor(), ","));	
		}	
		setDataVencimentoAntesAlteracaoLog(null);
		setValorAntesAlteracaoLog(null);
		return sb.toString();
	}
	
	public String getPreencherCamposDescricaoOperacaoLog(ContaPagarVO contaPagarVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(System.lineSeparator());
		sb.append(" Nrº:").append(contaPagarVO.getCodigo());
		sb.append(" - Favorecido:").append(contaPagarVO.getFavorecido());
		return sb.toString();		
	}
	
	public String getPreencherStatusProgressBarVO(ProgressBarVO progressBarVO, ContaPagarVO contaPagarVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" Processando ").append(progressBarVO.getProgresso()).append(" de ").append(progressBarVO.getMaxValue());
		sb.append(" - (Conta Pagar Atual Nº = ").append(contaPagarVO.getCodigo()).append(") ");
		return sb.toString();		
	}
	
	public void getPreencherMapaUnidadeEnsino(Map<Integer, List<String>>mapaUnidadeEnsino, Integer unidadeEnsino, String msg) {
		if(mapaUnidadeEnsino.containsKey(unidadeEnsino)) {
			mapaUnidadeEnsino.get(unidadeEnsino).add(msg);
		}else {
			List<String> listaMsg = new ArrayList<String>();
			listaMsg.add(msg);
			mapaUnidadeEnsino.put(unidadeEnsino,listaMsg);
		}		
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public OrigemContaPagar getOrigemContaPagarEnum() {
		if (origemContaPagarEnum == null) {
			origemContaPagarEnum = OrigemContaPagar.REGISTRO_MANUAL;
		}
		return origemContaPagarEnum;
	}

	public void setOrigemContaPagarEnum(OrigemContaPagar origemContaPagarEnum) {
		this.origemContaPagarEnum = origemContaPagarEnum;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public GestaoContasPagarOperacaoEnum getGestaoContasPagarOperacaoEnum() {
		if (gestaoContasPagarOperacaoEnum == null) {
			gestaoContasPagarOperacaoEnum = GestaoContasPagarOperacaoEnum.ALTERACAO;
		}
		return gestaoContasPagarOperacaoEnum;
	}

	public void setGestaoContasPagarOperacaoEnum(GestaoContasPagarOperacaoEnum gestaoContasPagarOperacaoEnum) {
		this.gestaoContasPagarOperacaoEnum = gestaoContasPagarOperacaoEnum;
	}

	public UsuarioVO getUsuarioOperacao() {
		if (usuarioOperacao == null) {
			usuarioOperacao = new UsuarioVO();
		}
		return usuarioOperacao;
	}

	public void setUsuarioOperacao(UsuarioVO usuarioOperacao) {
		this.usuarioOperacao = usuarioOperacao;
	}

	public Date getDataRegistro() {
		if (dataRegistro == null) {
			dataRegistro = new Date();
		}
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public String getDescricaoOperacao() {
		if (descricaoOperacao == null) {
			descricaoOperacao = "";
		}
		return descricaoOperacao;
	}

	public void setDescricaoOperacao(String descricaoOperacao) {
		this.descricaoOperacao = descricaoOperacao;
	}

	public String getValorConsulta() {
		if (valorConsulta == null) {
			valorConsulta = "";
		}
		return valorConsulta;
	}

	public void setValorConsulta(String valorConsulta) {
		this.valorConsulta = valorConsulta;
	}

	public String getCampoConsulta() {
		if (campoConsulta == null) {
			campoConsulta = "";
		}
		return campoConsulta;
	}

	public void setCampoConsulta(String campoConsulta) {
		this.campoConsulta = campoConsulta;
	}

	public Date getDataVencimentoInicio() {
		if (dataVencimentoInicio == null) {
			dataVencimentoInicio = new Date();
		}
		return dataVencimentoInicio;
	}

	public void setDataVencimentoInicio(Date dataVencimentoInicio) {
		this.dataVencimentoInicio = dataVencimentoInicio;
	}

	public Date getDataVencimentoFim() {
		if (dataVencimentoFim == null) {
			dataVencimentoFim = new Date();
		}
		return dataVencimentoFim;
	}

	public void setDataVencimentoFim(Date dataVencimentoFim) {
		this.dataVencimentoFim = dataVencimentoFim;
	}

	public Date getDataOperacaoInicio() {
		/*if (dataOperacaoInicio == null) {
			dataOperacaoInicio = new Date();
		}*/
		return dataOperacaoInicio;
	}

	public void setDataOperacaoInicio(Date dataOperacaoInicio) {
		this.dataOperacaoInicio = dataOperacaoInicio;
	}

	public Date getDataOperacaoFim() {
		/*if (dataOperacaoFim == null) {
			dataOperacaoFim = new Date();
		}*/
		return dataOperacaoFim;
	}

	public void setDataOperacaoFim(Date dataOperacaoFim) {
		this.dataOperacaoFim = dataOperacaoFim;
	}

	public Double getValorFaixaInicio() {
		if (valorFaixaInicio == null) {
			valorFaixaInicio = 0.0;
		}
		return valorFaixaInicio;
	}

	public void setValorFaixaInicio(Double valorFaixaInicio) {
		this.valorFaixaInicio = valorFaixaInicio;
	}

	public Double getValorFaixaFim() {
		if (valorFaixaFim == null) {
			valorFaixaFim = 0.0;
		}
		return valorFaixaFim;
	}

	public void setValorFaixaFim(Double valorFaixaFim) {
		this.valorFaixaFim = valorFaixaFim;
	}
	
	public Date getDataVencimentoAlterar() {
		return dataVencimentoAlterar;
	}

	public void setDataVencimentoAlterar(Date dataVencimentoAlterar) {
		this.dataVencimentoAlterar = dataVencimentoAlterar;
	}

	public Date getDataOperacaoAlterar() {
		return dataOperacaoAlterar;
	}
	
	public void setDataOperacaoAlterar(Date dataOperacaoAlterar) {
		this.dataOperacaoAlterar = dataOperacaoAlterar;
	}
	
	public boolean isApresentarDataOperacao() {
		return getGestaoContasPagarOperacaoEnum().isPagamento() || getGestaoContasPagarOperacaoEnum().isEstornoPagamento() || getGestaoContasPagarOperacaoEnum().isCancelamento();
	}
	
	public String getTituloCampoDataOperacao() {
		switch (getGestaoContasPagarOperacaoEnum()) {
		case PAGAMENTO:
			return "Data Pagamento";
		case ESTORNO_PAGAMENTO:
			return "Data Estorno Pagamento";
		case CANCELAMENTO:
			return "Data Cancelamento";
		case ESTORNO_CANCELAMENTO:
			return "Data Estorno Cancelamento";
		default:
			return "";
		}
	}
	
	public FormaPagamentoNegociacaoPagamentoVO getFormaPagamentoNegociacaoPagamentoVO() {
		if (formaPagamentoNegociacaoPagamentoVO == null) {
			formaPagamentoNegociacaoPagamentoVO = new FormaPagamentoNegociacaoPagamentoVO();
		}
		return formaPagamentoNegociacaoPagamentoVO;
	}
	
	public void setFormaPagamentoNegociacaoPagamentoVO(FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO) {
		this.formaPagamentoNegociacaoPagamentoVO = formaPagamentoNegociacaoPagamentoVO;
	}
	
	public ConfiguracaoFinanceiroCartaoVO getConfiguracaoFinanceiroCartaoVO() {
		if (configuracaoFinanceiroCartaoVO == null) {
			configuracaoFinanceiroCartaoVO = new ConfiguracaoFinanceiroCartaoVO();
		}
		return configuracaoFinanceiroCartaoVO;
	}
	
	public void setConfiguracaoFinanceiroCartaoVO(ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO) {
		this.configuracaoFinanceiroCartaoVO = configuracaoFinanceiroCartaoVO;
	}
	
	public boolean isDesconsiderarConciliacaoBancaria() {		
		return desconsiderarConciliacaoBancaria;
	}
	
	public void setDesconsiderarConciliacaoBancaria(boolean desconsiderarConciliacaoBancaria) {
		this.desconsiderarConciliacaoBancaria = desconsiderarConciliacaoBancaria;
	}
	
	public String getMotivoAlteracao() {
		if (motivoAlteracao == null) {
			motivoAlteracao = "";
		}
		return motivoAlteracao;
	}

	public void setMotivoAlteracao(String motivoAlteracao) {
		this.motivoAlteracao = motivoAlteracao;
	}
	
	public Double getValorAlterar() {
		return valorAlterar;
	}

	public void setValorAlterar(Double valorAlterar) {
		this.valorAlterar = valorAlterar;
	}
	
	public boolean isContasMesmoFavorecidoOrigemAvancarParcelasMesSubsquente() {
		return contasMesmoFavorecidoOrigemAvancarParcelasMesSubsquente;
	}

	public void setContasMesmoFavorecidoOrigemAvancarParcelasMesSubsquente(boolean contasMesmoFavorecidoOrigemAvancarParcelasMesSubsquente) {
		this.contasMesmoFavorecidoOrigemAvancarParcelasMesSubsquente = contasMesmoFavorecidoOrigemAvancarParcelasMesSubsquente;
	}
	
	public Date getDataVencimentoAntesAlteracaoLog() {
		return dataVencimentoAntesAlteracaoLog;
	}
	public void setDataVencimentoAntesAlteracaoLog(Date dataVencimentoAntesAlteracaoLog) {
		this.dataVencimentoAntesAlteracaoLog = dataVencimentoAntesAlteracaoLog;
	}
	public Double getValorAntesAlteracaoLog() {
		return valorAntesAlteracaoLog;
	}
	public void setValorAntesAlteracaoLog(Double valorAntesAlteracaoLog) {
		this.valorAntesAlteracaoLog = valorAntesAlteracaoLog;
	}
	
	public Map<Integer, List<String>> getMapaUnidadeEnsinoLog() {
		if (mapaUnidadeEnsinoLog == null) {
			mapaUnidadeEnsinoLog = new HashMap<>();
		}
		return mapaUnidadeEnsinoLog;
	}
	
	public void setMapaUnidadeEnsinoLog(Map<Integer, List<String>> mapaUnidadeEnsinoLog) {
		this.mapaUnidadeEnsinoLog = mapaUnidadeEnsinoLog;
	}
	
	public Map<Integer, List<String>> getMapaUnidadeEnsinoLogErro() {
		if (mapaUnidadeEnsinoLogErro == null) {
			mapaUnidadeEnsinoLogErro = new HashMap<>();
		}
		return mapaUnidadeEnsinoLogErro;
	}
	
	public void setMapaUnidadeEnsinoLogErro(Map<Integer, List<String>> mapaUnidadeEnsinoLogErro) {
		this.mapaUnidadeEnsinoLogErro = mapaUnidadeEnsinoLogErro;
	}
	
	public List<GestaoContasPagarVO> getListaGestaoContasPagarLogs() {
		if (listaGestaoContasPagarLogs == null) {
			listaGestaoContasPagarLogs = new ArrayList<>();
		}
		return listaGestaoContasPagarLogs;
	}
	public void setListaGestaoContasPagarLogs(List<GestaoContasPagarVO> listaGestaoContasPagarLogs) {
		this.listaGestaoContasPagarLogs = listaGestaoContasPagarLogs;
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
