package negocio.comuns.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoArquivoRetornoEnum;
import negocio.comuns.financeiro.enumerador.TipoListaProcessamentoArquivoEnum;
import negocio.comuns.utilitarias.Uteis;

public class ProcessamentoArquivoRetornoParceiroVO extends SuperVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3572365205782452771L;
	private Integer codigo;
	private ParceiroVO parceiroVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private ContaPagarVO contaPagarVO;
	private Date dataGeracao;
	private Date dataRepasse;
	private UsuarioVO responsavel;
	private boolean contasBaixadas = false;
	private boolean gerarContaPagar = false;
	private String nomeArquivo;
	private Integer qtdeLote;
	private Integer loteAtual;
	private Date dataInicioProcessamento;
	private Date dataTerminoProcessamento;
	private SituacaoProcessamentoArquivoRetornoEnum situacaoProcessamento;
	private String motivoErroProcessamento;
	private String datasDeCompetencia;

	private List<ProcessamentoArquivoRetornoParceiroAlunoVO> listaContasRecebidas;
	private List<ProcessamentoArquivoRetornoParceiroAlunoVO> listaContasAReceber;
	private List<ProcessamentoArquivoRetornoParceiroAlunoVO> listaContaValoresDivergente;
	private List<ProcessamentoArquivoRetornoParceiroAlunoVO> listaContasNaoLocalizada;
	private List<ProcessamentoArquivoRetornoParceiroAlunoVO> listaContasMesCompetencia;

	private List<Integer> listaCodigoContaReceberEncontradosNoArquivo;
	private BigDecimal totalValorContaParceiroRecebidas;
	private BigDecimal totalValorRepasseRecebidas;
	private BigDecimal totalValorContaRecebidas;

	private BigDecimal totalValorContaParceiroAReceber;
	private BigDecimal totalValorRepasseAReceber;
	private BigDecimal totalValorContaAReceber;

	private BigDecimal totalValorContaParceiroNaoLocalizada;
	private BigDecimal totalValorRepasseNaoLocalizada;

	private BigDecimal totalValorContaParceiroValoresDivergente;
	private BigDecimal totalValorRepasseValoresDivergente;
	private BigDecimal totalValorContaValoresDivergente;

	private BigDecimal totalValorContaMesCompetencia;

	private BigDecimal totalValorContaParceiroArquivoExcel;
	private BigDecimal totalValorRepasseArquivoExcel;

	public void separarProcessamentoArquivoRetornoParceiroExcelVOEmLista(ProcessamentoArquivoRetornoParceiroAlunoVO objAluno) throws Exception {
		if (!objAluno.getCpf().isEmpty() && objAluno.isProcessamentoSituacaoRecebida()) {			
			getListaContasRecebidas().add(objAluno);
			setTotalValorContaRecebidas(getTotalValorContaRecebidas().add(new BigDecimal(objAluno.getValorTotalContaReceber())));
			setTotalValorRepasseRecebidas(getTotalValorRepasseRecebidas().add(new BigDecimal(objAluno.getValorRepasse())));
			setTotalValorContaParceiroRecebidas(getTotalValorContaParceiroRecebidas().add(new BigDecimal(objAluno.getValorTotalParceiro())));
			if (objAluno.getTipoListaProcessamentoArquivo().isNenhum()) {
				objAluno.setTipoListaProcessamentoArquivo(TipoListaProcessamentoArquivoEnum.CONTAS_RECEBIDAS);
			}
		} else if (!objAluno.getCpf().isEmpty() && objAluno.isProcessamentoSituacaoDivergenteValoresEntreConta()) {
			getListaContaValoresDivergente().add(objAluno);
			setTotalValorContaValoresDivergente(getTotalValorContaValoresDivergente().add(new BigDecimal(objAluno.getValorTotalContaReceber())));
			setTotalValorRepasseValoresDivergente(getTotalValorRepasseValoresDivergente().add(new BigDecimal(objAluno.getValorRepasse())));
			setTotalValorContaParceiroValoresDivergente(getTotalValorContaParceiroValoresDivergente().add(new BigDecimal(objAluno.getValorTotalParceiro())));
			if (objAluno.getTipoListaProcessamentoArquivo().isNenhum()) {
				objAluno.setTipoListaProcessamentoArquivo(TipoListaProcessamentoArquivoEnum.CONTAS_VALORES_DIVERGENTE);
			}			
		} else if (!objAluno.getCpf().isEmpty() && objAluno.isProcessamentoSituacaoAReceber()) {
			getListaContasAReceber().add(objAluno);
			setTotalValorContaAReceber(getTotalValorContaAReceber().add(new BigDecimal(objAluno.getValorTotalContaReceber())));
			setTotalValorRepasseAReceber(getTotalValorRepasseAReceber().add(new BigDecimal(objAluno.getValorRepasse())));
			setTotalValorContaParceiroAReceber(getTotalValorContaParceiroAReceber().add(new BigDecimal(objAluno.getValorTotalParceiro())));
			if (objAluno.getTipoListaProcessamentoArquivo().isNenhum()) {
				objAluno.setTipoListaProcessamentoArquivo(TipoListaProcessamentoArquivoEnum.CONTAS_RECEBER);
			}
		} else if (!objAluno.getCpf().isEmpty() && objAluno.isProcessamentoSituacaoContaNaoLocalizada()) {
			getListaContasNaoLocalizada().add(objAluno);
			setTotalValorRepasseNaoLocalizada(getTotalValorRepasseNaoLocalizada().add(new BigDecimal(objAluno.getValorRepasse())));
			setTotalValorContaParceiroNaoLocalizada(getTotalValorContaParceiroNaoLocalizada().add(new BigDecimal(objAluno.getValorTotalParceiro())));
			if (objAluno.getTipoListaProcessamentoArquivo().isNenhum()) {
				objAluno.setTipoListaProcessamentoArquivo(TipoListaProcessamentoArquivoEnum.NAO_LOCALIZADA_SISTEMA);
			}
		} else if (objAluno.getCpf().isEmpty() && Uteis.isAtributoPreenchido(objAluno.getListaContasAReceber())) {			
			getListaContasMesCompetencia().add(objAluno);
			setTotalValorContaMesCompetencia(getTotalValorContaMesCompetencia().add(new BigDecimal(objAluno.getValorTotalContaReceber())));
			if (objAluno.getTipoListaProcessamentoArquivo().isNenhum()) {
				objAluno.setTipoListaProcessamentoArquivo(TipoListaProcessamentoArquivoEnum.NAO_LOCALIZADA_ARQUIVO);
			}
		}
		if (!objAluno.getCpf().isEmpty()) {
			setTotalValorRepasseArquivoExcel(getTotalValorRepasseArquivoExcel().add(new BigDecimal(objAluno.getValorRepasse())));
			setTotalValorContaParceiroArquivoExcel(getTotalValorContaParceiroArquivoExcel().add(new BigDecimal(objAluno.getValorTotalParceiro())));
		}
		if (Uteis.isAtributoPreenchido(objAluno.getListaContasAReceber())) {
			forContaReceber:
			for (ProcessamentoArquivoRetornoParceiroExcelVO processamentoConta : objAluno.getListaContasAReceber()) {
				if(processamentoConta.isContaReceberExistente()){
					objAluno.getAluno().setCodigo(processamentoConta.getContaReceberVO().getMatriculaAluno().getAluno().getCodigo());
					objAluno.getAluno().setNome(processamentoConta.getContaReceberVO().getMatriculaAluno().getAluno().getNome());
					objAluno.setMatricula(processamentoConta.getContaReceberVO().getMatriculaAluno().getMatricula());
					break forContaReceber;
				}
			}
		}
	}
	

	public void limparCampos() {
		getListaContasAReceber().clear();
		getListaContasNaoLocalizada().clear();
		getListaContasRecebidas().clear();
		getListaContaValoresDivergente().clear();
		getListaContasMesCompetencia().clear();
		getListaCodigoContaReceberEncontradosNoArquivo().clear();
		setTotalValorContaParceiroRecebidas(new BigDecimal(0));
		setTotalValorRepasseRecebidas(new BigDecimal(0));
		setTotalValorContaRecebidas(new BigDecimal(0));
		setTotalValorContaParceiroAReceber(new BigDecimal(0));
		setTotalValorRepasseAReceber(new BigDecimal(0));
		setTotalValorContaAReceber(new BigDecimal(0));
		setTotalValorContaParceiroNaoLocalizada(new BigDecimal(0));
		setTotalValorRepasseNaoLocalizada(new BigDecimal(0));
		setTotalValorContaParceiroValoresDivergente(new BigDecimal(0));
		setTotalValorRepasseValoresDivergente(new BigDecimal(0));
		setTotalValorContaValoresDivergente(new BigDecimal(0));
		setTotalValorContaMesCompetencia(new BigDecimal(0));
		setTotalValorContaParceiroArquivoExcel(new BigDecimal(0));
		setTotalValorRepasseArquivoExcel(new BigDecimal(0));
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

	public ParceiroVO getParceiroVO() {
		if (parceiroVO == null) {
			parceiroVO = new ParceiroVO();
		}
		return parceiroVO;
	}

	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
	}

	public Date getDataRepasse() {
		if (dataRepasse == null) {
			dataRepasse = new Date();
		}
		return dataRepasse;
	}

	public void setDataRepasse(Date dataRepasse) {
		this.dataRepasse = dataRepasse;
	}

	public List<ProcessamentoArquivoRetornoParceiroAlunoVO> getListaContasRecebidas() {
		if (listaContasRecebidas == null) {
			listaContasRecebidas = new ArrayList<ProcessamentoArquivoRetornoParceiroAlunoVO>();
		}
		return listaContasRecebidas;
	}

	public void setListaContasRecebidas(List<ProcessamentoArquivoRetornoParceiroAlunoVO> listaContasRecebidas) {
		this.listaContasRecebidas = listaContasRecebidas;
	}

	public List<ProcessamentoArquivoRetornoParceiroAlunoVO> getListaContasAReceber() {
		if (listaContasAReceber == null) {
			listaContasAReceber = new ArrayList<ProcessamentoArquivoRetornoParceiroAlunoVO>();
		}
		return listaContasAReceber;
	}

	public void setListaContasAReceber(List<ProcessamentoArquivoRetornoParceiroAlunoVO> listaContasAReceber) {
		this.listaContasAReceber = listaContasAReceber;
	}

	public List<ProcessamentoArquivoRetornoParceiroAlunoVO> getListaContasNaoLocalizada() {
		if (listaContasNaoLocalizada == null) {
			listaContasNaoLocalizada = new ArrayList<ProcessamentoArquivoRetornoParceiroAlunoVO>();
		}
		return listaContasNaoLocalizada;
	}

	public void setListaContasNaoLocalizada(List<ProcessamentoArquivoRetornoParceiroAlunoVO> listaContasNaoLocalizada) {
		this.listaContasNaoLocalizada = listaContasNaoLocalizada;
	}

	public List<ProcessamentoArquivoRetornoParceiroAlunoVO> getListaContaValoresDivergente() {
		if (listaContaValoresDivergente == null) {
			listaContaValoresDivergente = new ArrayList<ProcessamentoArquivoRetornoParceiroAlunoVO>();
		}
		return listaContaValoresDivergente;
	}

	public void setListaContaValoresDivergente(List<ProcessamentoArquivoRetornoParceiroAlunoVO> listaContaValoresDivergente) {
		this.listaContaValoresDivergente = listaContaValoresDivergente;
	}

	public List<ProcessamentoArquivoRetornoParceiroAlunoVO> getListaContasMesCompetencia() {
		if (listaContasMesCompetencia == null) {
			listaContasMesCompetencia = new ArrayList<ProcessamentoArquivoRetornoParceiroAlunoVO>();
		}
		return listaContasMesCompetencia;
	}

	public void setListaContasMesCompetencia(List<ProcessamentoArquivoRetornoParceiroAlunoVO> listaContasMesCompetencia) {
		this.listaContasMesCompetencia = listaContasMesCompetencia;
	}

	public List<Integer> getListaCodigoContaReceberEncontradosNoArquivo() {
		if (listaCodigoContaReceberEncontradosNoArquivo == null) {
			listaCodigoContaReceberEncontradosNoArquivo = new ArrayList<Integer>();
		}
		return listaCodigoContaReceberEncontradosNoArquivo;
	}

	public void setListaCodigoContaReceberEncontradosNoArquivo(List<Integer> listaCodigoContaReceberEncontradosNoArquivo) {
		this.listaCodigoContaReceberEncontradosNoArquivo = listaCodigoContaReceberEncontradosNoArquivo;
	}

	public Date getDataGeracao() {
		if (dataGeracao == null) {
			dataGeracao = new Date();
		}
		return dataGeracao;
	}

	public void setDataGeracao(Date dataGeracao) {
		this.dataGeracao = dataGeracao;
	}	

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public Integer getTotalListaContasRecebidas() {
		return getListaContasRecebidas().size();
	}

	public Integer getTotalListaContasAReceber() {
		return getListaContasAReceber().size();
	}

	public Integer getTotalListaContasNaoLocalizada() {
		return getListaContasNaoLocalizada().size();
	}

	public Integer getTotalListaContaValoresDivergente() {
		return getListaContaValoresDivergente().size();
	}

	public Integer getTotalListaContaMesCompetencia() {
		return getListaContasMesCompetencia().size();
	}

	public Integer getQtdeLote() {
		if (qtdeLote == null) {
			qtdeLote = 0;
		}
		return qtdeLote;
	}

	public void setQtdeLote(Integer qtdeLote) {
		this.qtdeLote = qtdeLote;
	}

	public Integer getLoteAtual() {
		if (loteAtual == null) {
			loteAtual = 0;
		}
		return loteAtual;
	}

	public void setLoteAtual(Integer loteAtual) {
		this.loteAtual = loteAtual;
	}

	public Date getDataInicioProcessamento() {
		return dataInicioProcessamento;
	}

	public void setDataInicioProcessamento(Date dataInicioProcessamento) {
		this.dataInicioProcessamento = dataInicioProcessamento;
	}

	public Date getDataTerminoProcessamento() {
		return dataTerminoProcessamento;
	}

	public void setDataTerminoProcessamento(Date dataTerminoProcessamento) {
		this.dataTerminoProcessamento = dataTerminoProcessamento;
	}

	public SituacaoProcessamentoArquivoRetornoEnum getSituacaoProcessamento() {
		if (situacaoProcessamento == null) {
			situacaoProcessamento = SituacaoProcessamentoArquivoRetornoEnum.AGUARDANDO_PROCESSAMENTO;
		}
		return situacaoProcessamento;
	}

	public void setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum situacaoProcessamento) {
		this.situacaoProcessamento = situacaoProcessamento;
	}

	public String getMotivoErroProcessamento() {
		if (motivoErroProcessamento == null) {
			motivoErroProcessamento = "";
		}
		return motivoErroProcessamento;
	}

	public void setMotivoErroProcessamento(String motivoErroProcessamento) {
		this.motivoErroProcessamento = motivoErroProcessamento;
	}

	public boolean isGerarContaPagar() {
		return gerarContaPagar;
	}

	public void setGerarContaPagar(boolean gerarContaPagar) {
		this.gerarContaPagar = gerarContaPagar;
	}

	public boolean getContasBaixadas() {
		return contasBaixadas;
	}

	public void setContasBaixadas(boolean contasBaixadas) {
		this.contasBaixadas = contasBaixadas;
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

	public ContaPagarVO getContaPagarVO() {
		if (contaPagarVO == null) {
			contaPagarVO = new ContaPagarVO();
		}
		return contaPagarVO;
	}

	public void setContaPagarVO(ContaPagarVO contaPagarVO) {
		this.contaPagarVO = contaPagarVO;
	}

	public String getDatasDeCompetencia() {
		if (datasDeCompetencia == null) {
			datasDeCompetencia = "";
		}
		return datasDeCompetencia;
	}

	public void setDatasDeCompetencia(String datasDeCompetencia) {
		this.datasDeCompetencia = datasDeCompetencia;
	}

	public String getNomeArquivo_Apresentar() {
		return Uteis.getNomeArquivo(getNomeArquivo());
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getProgressBar() {
		if (getQtdeLote() == 0) {
			return "Iniciando Processamento...";
		}
		return "Processando lote " + getLoteAtual() + " de " + getQtdeLote();
	}

	public String getDataGeracao_Apresentar() {
		return Uteis.getDataAno4Digitos(getDataGeracao());
	}

	public BigDecimal getTotalValorContaParceiroRecebidas() {
		if (totalValorContaParceiroRecebidas == null) {
			totalValorContaParceiroRecebidas = new BigDecimal(0);
		}
		return totalValorContaParceiroRecebidas;
	}

	public void setTotalValorContaParceiroRecebidas(BigDecimal totalValorContaParceiroRecebidas) {
		this.totalValorContaParceiroRecebidas = totalValorContaParceiroRecebidas;
	}

	public BigDecimal getTotalValorRepasseRecebidas() {
		if (totalValorRepasseRecebidas == null) {
			totalValorRepasseRecebidas = new BigDecimal(0);
		}
		return totalValorRepasseRecebidas;
	}

	public void setTotalValorRepasseRecebidas(BigDecimal totalValorRepasseRecebidas) {
		this.totalValorRepasseRecebidas = totalValorRepasseRecebidas;
	}

	public BigDecimal getTotalValorContaRecebidas() {
		if (totalValorContaRecebidas == null) {
			totalValorContaRecebidas = new BigDecimal(0);
		}
		return totalValorContaRecebidas;
	}

	public void setTotalValorContaRecebidas(BigDecimal totalValorContaRecebidas) {
		this.totalValorContaRecebidas = totalValorContaRecebidas;
	}

	public BigDecimal getTotalValorContaParceiroAReceber() {
		if (totalValorContaParceiroAReceber == null) {
			totalValorContaParceiroAReceber = new BigDecimal(0);
		}
		return totalValorContaParceiroAReceber;
	}

	public void setTotalValorContaParceiroAReceber(BigDecimal totalValorContaParceiroAReceber) {
		this.totalValorContaParceiroAReceber = totalValorContaParceiroAReceber;
	}

	public BigDecimal getTotalValorRepasseAReceber() {
		if (totalValorRepasseAReceber == null) {
			totalValorRepasseAReceber = new BigDecimal(0);
		}
		return totalValorRepasseAReceber;
	}

	public void setTotalValorRepasseAReceber(BigDecimal totalValorRepasseAReceber) {
		this.totalValorRepasseAReceber = totalValorRepasseAReceber;
	}

	public BigDecimal getTotalValorContaAReceber() {
		if (totalValorContaAReceber == null) {
			totalValorContaAReceber = new BigDecimal(0);
		}
		return totalValorContaAReceber;
	}

	public void setTotalValorContaAReceber(BigDecimal totalValorContaAReceber) {
		this.totalValorContaAReceber = totalValorContaAReceber;
	}

	public BigDecimal getTotalValorContaParceiroNaoLocalizada() {
		if (totalValorContaParceiroNaoLocalizada == null) {
			totalValorContaParceiroNaoLocalizada = new BigDecimal(0);
		}
		return totalValorContaParceiroNaoLocalizada;
	}

	public void setTotalValorContaParceiroNaoLocalizada(BigDecimal totalValorContaParceiroNaoLocalizada) {
		this.totalValorContaParceiroNaoLocalizada = totalValorContaParceiroNaoLocalizada;
	}

	public BigDecimal getTotalValorRepasseNaoLocalizada() {
		if (totalValorRepasseNaoLocalizada == null) {
			totalValorRepasseNaoLocalizada = new BigDecimal(0);
		}
		return totalValorRepasseNaoLocalizada;
	}

	public void setTotalValorRepasseNaoLocalizada(BigDecimal totalValorRepasseNaoLocalizada) {
		this.totalValorRepasseNaoLocalizada = totalValorRepasseNaoLocalizada;
	}

	public BigDecimal getTotalValorContaParceiroValoresDivergente() {
		if (totalValorContaParceiroValoresDivergente == null) {
			totalValorContaParceiroValoresDivergente = new BigDecimal(0);
		}
		return totalValorContaParceiroValoresDivergente;
	}

	public void setTotalValorContaParceiroValoresDivergente(BigDecimal totalValorContaParceiroValoresDivergente) {
		this.totalValorContaParceiroValoresDivergente = totalValorContaParceiroValoresDivergente;
	}

	public BigDecimal getTotalValorRepasseValoresDivergente() {
		if (totalValorRepasseValoresDivergente == null) {
			totalValorRepasseValoresDivergente = new BigDecimal(0);
		}
		return totalValorRepasseValoresDivergente;
	}

	public void setTotalValorRepasseValoresDivergente(BigDecimal totalValorRepasseValoresDivergente) {
		this.totalValorRepasseValoresDivergente = totalValorRepasseValoresDivergente;
	}

	public BigDecimal getTotalValorContaValoresDivergente() {
		if (totalValorContaValoresDivergente == null) {
			totalValorContaValoresDivergente = new BigDecimal(0);
		}
		return totalValorContaValoresDivergente;
	}

	public void setTotalValorContaValoresDivergente(BigDecimal totalValorContaValoresDivergente) {
		this.totalValorContaValoresDivergente = totalValorContaValoresDivergente;
	}

	public BigDecimal getTotalValorContaMesCompetencia() {
		if (totalValorContaMesCompetencia == null) {
			totalValorContaMesCompetencia = new BigDecimal(0);
		}
		return totalValorContaMesCompetencia;
	}

	public void setTotalValorContaMesCompetencia(BigDecimal totalValorContaMesCompetencia) {
		this.totalValorContaMesCompetencia = totalValorContaMesCompetencia;
	}

	public BigDecimal getTotalValorContaParceiroArquivoExcel() {
		if (totalValorContaParceiroArquivoExcel == null) {
			totalValorContaParceiroArquivoExcel = new BigDecimal(0);
		}
		return totalValorContaParceiroArquivoExcel;
	}

	public void setTotalValorContaParceiroArquivoExcel(BigDecimal totalValorContaParceiroArquivoExcel) {
		this.totalValorContaParceiroArquivoExcel = totalValorContaParceiroArquivoExcel;
	}

	public BigDecimal getTotalValorRepasseArquivoExcel() {
		if (totalValorRepasseArquivoExcel == null) {
			totalValorRepasseArquivoExcel = new BigDecimal(0);
		}
		return totalValorRepasseArquivoExcel;
	}

	public void setTotalValorRepasseArquivoExcel(BigDecimal totalValorRepasseArquivoExcel) {
		this.totalValorRepasseArquivoExcel = totalValorRepasseArquivoExcel;
	}
	
	public BigDecimal getTotalValorImposto() {
		return getTotalValorContaParceiroAReceber().subtract(getTotalValorRepasseAReceber());
	}

}
