package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoGeracaoManualParcelaEnum;
import negocio.comuns.utilitarias.ProgressBarVO;

public class GeracaoManualParcelaVO extends ProgressBarVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1155386226224672110L;
	private Integer codigo;
	private UnidadeEnsinoVO unidadeEnsino;
	private CursoVO curso;
    private TurmaVO turma;
    private UsuarioVO usuario;
    private String mesReferencia;
    private String anoReferencia;
    private Boolean permitirGerarParcelaAlunoPreMatricula;
    private Integer qtdeParcelaGerar;    
    private Integer qtdeParcelaComErro;
    private Integer qtdeParcelaGerada;
    private Date dataInicioProcessamento;
    private Date dataTerminoProcessamento;
    private Double valorTotalParcelasGerar;
    private Double valorTotalParcelasGerada;
    private Double valorTotalParcelasNaoGerada;
    private Boolean gerarTodasParcelas;
    private Boolean utilizarDataCompetencia;
    private boolean trazerMatriculasComCanceladoFinanceiro = false;
    private String mensagemErro;
    private SituacaoProcessamentoGeracaoManualParcelaEnum situacaoProcessamentoGeracaoManualParcela;
    
    
    
    
    
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}
	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}
	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}
	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}
	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}
	public UsuarioVO getUsuario() {
		if (usuario == null) {
			usuario = new UsuarioVO();
		}
		return usuario;
	}
	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}
	public String getMesReferencia() {
		if (mesReferencia == null) {
			mesReferencia = "";
		}
		return mesReferencia;
	}
	public void setMesReferencia(String mesReferencia) {
		this.mesReferencia = mesReferencia;
	}
	public String getAnoReferencia() {
		if (anoReferencia == null) {
			anoReferencia = "";
		}
		return anoReferencia;
	}
	public void setAnoReferencia(String anoReferencia) {
		this.anoReferencia = anoReferencia;
	}
	public Boolean getPermitirGerarParcelaAlunoPreMatricula() {
		if (permitirGerarParcelaAlunoPreMatricula == null) {
			permitirGerarParcelaAlunoPreMatricula = false;
		}
		return permitirGerarParcelaAlunoPreMatricula;
	}
	public void setPermitirGerarParcelaAlunoPreMatricula(Boolean permitirGerarParcelaAlunoPreMatricula) {
		this.permitirGerarParcelaAlunoPreMatricula = permitirGerarParcelaAlunoPreMatricula;
	}
	public Integer getQtdeParcelaGerar() {
		if (qtdeParcelaGerar == null) {
			qtdeParcelaGerar = 0;
		}
		return qtdeParcelaGerar;
	}
	public void setQtdeParcelaGerar(Integer qtdeParcelaGerar) {
		this.qtdeParcelaGerar = qtdeParcelaGerar;
	}
	public Integer getParcelaAtual() {		
		return getQtdeParcelaComErro()+getQtdeParcelaGerada();
	}
	
	public Integer getQtdeParcelaComErro() {
		if (qtdeParcelaComErro == null) {
			qtdeParcelaComErro = 0;
		}
		return qtdeParcelaComErro;
	}
	public void setQtdeParcelaComErro(Integer qtdeParcelaComErro) {
		this.qtdeParcelaComErro = qtdeParcelaComErro;
	}
	public Integer getQtdeParcelaGerada() {
		if (qtdeParcelaGerada == null) {
			qtdeParcelaGerada = 0;
		}
		return qtdeParcelaGerada;
	}
	public void setQtdeParcelaGerada(Integer qtdeParcelaGerada) {
		this.qtdeParcelaGerada = qtdeParcelaGerada;
	}
	public Date getDataInicioProcessamento() {
		if (dataInicioProcessamento == null) {
			dataInicioProcessamento = new Date();
		}
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
	public Double getValorTotalParcelasGerar() {
		if (valorTotalParcelasGerar == null) {
			valorTotalParcelasGerar = 0.0;
		}
		return valorTotalParcelasGerar;
	}
	public void setValorTotalParcelasGerar(Double valorTotalParcelasGerar) {
		this.valorTotalParcelasGerar = valorTotalParcelasGerar;
	}
	public Double getValorTotalParcelasGerada() {
		if (valorTotalParcelasGerada == null) {
			valorTotalParcelasGerada = 0.0;
		}
		return valorTotalParcelasGerada;
	}
	public void setValorTotalParcelasGerada(Double valorTotalParcelasGerada) {
		this.valorTotalParcelasGerada = valorTotalParcelasGerada;
	}
	public Double getValorTotalParcelasNaoGerada() {
		if (valorTotalParcelasNaoGerada == null) {
			valorTotalParcelasNaoGerada = 0.0;
		}
		return valorTotalParcelasNaoGerada;
	}
	public void setValorTotalParcelasNaoGerada(Double valorTotalParcelasNaoGerada) {
		this.valorTotalParcelasNaoGerada = valorTotalParcelasNaoGerada;
	}		
	
	public SituacaoProcessamentoGeracaoManualParcelaEnum getSituacaoProcessamentoGeracaoManualParcela() {
		if (situacaoProcessamentoGeracaoManualParcela == null) {
			situacaoProcessamentoGeracaoManualParcela = SituacaoProcessamentoGeracaoManualParcelaEnum.AGUARDANDO_PROCESSAMENTO;
		}
		return situacaoProcessamentoGeracaoManualParcela;
	}
	public void setSituacaoProcessamentoGeracaoManualParcela(SituacaoProcessamentoGeracaoManualParcelaEnum situacaoProcessamentoGeracaoManualParcela) {
		this.situacaoProcessamentoGeracaoManualParcela = situacaoProcessamentoGeracaoManualParcela;
	}
	public Boolean getGerarTodasParcelas() {
		if(gerarTodasParcelas == null){
			gerarTodasParcelas = false;
		}
		return gerarTodasParcelas;
	}
	public void setGerarTodasParcelas(Boolean gerarTodasParcelas) {
		this.gerarTodasParcelas = gerarTodasParcelas;
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
    
    public String getLabelProcessamento(){
    	if(getSituacaoProcessamentoGeracaoManualParcela().equals(SituacaoProcessamentoGeracaoManualParcelaEnum.ERRO_PROCESSAMENTO)){
    		return "Processamento Interrompido";
    	}
    	if(getSituacaoProcessamentoGeracaoManualParcela().equals(SituacaoProcessamentoGeracaoManualParcelaEnum.PROCESSAMENTO_CONCLUIDO)){
    		return "Processamento Concluido";
    	}
    	return "Parcela "+getParcelaAtual()+" de "+getQtdeParcelaGerar();
    }
    
	public Boolean getUtilizarDataCompetencia() {
		if (utilizarDataCompetencia == null) {
			utilizarDataCompetencia = false;
		}
		return utilizarDataCompetencia;
	}
	public void setUtilizarDataCompetencia(Boolean utilizarDataCompetencia) {
		this.utilizarDataCompetencia = utilizarDataCompetencia;
	}
	public String getMensagemErro() {
		if (mensagemErro == null) {
			mensagemErro = "";
		}
		return mensagemErro;
	}
	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}
	
	 public boolean isTrazerMatriculasComCanceladoFinanceiro() {
			return trazerMatriculasComCanceladoFinanceiro;
		}

		public void setTrazerMatriculasComCanceladoFinanceiro(boolean trazerMatriculasComCanceladoFinanceiro) {
			this.trazerMatriculasComCanceladoFinanceiro = trazerMatriculasComCanceladoFinanceiro;
		}
    
	
}
