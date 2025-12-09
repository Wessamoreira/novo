package negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.academico.CertificadoCursoExtensaoDisciplinasRelVO;
import relatorio.negocio.comuns.academico.CertificadoCursoExtensaoRelVO;
import relatorio.negocio.comuns.financeiro.TermoReconhecimentoDividaRelVO;
import relatorio.negocio.jdbc.financeiro.DeclaracaoImpostoRendaRel;

/**
 * 
 * @author Carlos
 */
public class ImpressaoContratoVO extends SuperVO implements Serializable {
	
	private Integer codigo;
	private MatriculaVO matriculaVO;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private UsuarioVO usuarioLogado;
	private Integer quantidade;
	private Integer textoPadraoDeclaracao;
	private String observacao;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private GradeDisciplinaVO gradeDisciplinaVO;
	private Boolean alunoSelecionado;
	private List<HistoricoVO> listaDisciplinasCursadasOuMinistradas;
	private FuncionarioVO professor;
	public static final long serialVersionUID = 1L;
	private List<LogImpressaoContratoVO> listaLogImpressaoContratoVO;
	private TrancamentoVO trancamentoVO;
	private MatriculaVO matriculaOrigem;
	private MatriculaVO matriculaDestino;
	private TurmaVO turmaOrigem;
	private TurmaVO turmaDestino;
    private FuncionarioVO funcionarioPrincipalVO;
    private FuncionarioVO funcionarioSecundarioVO;    
    private CargoVO cargoFuncionarioPrincipal;
    private CargoVO cargoFuncionarioSecundario;
    private List<CertificadoCursoExtensaoDisciplinasRelVO> listaDisciplinasAprovadasPeriodoLetivo;
    private ControleLivroFolhaReciboVO controleLivroFolhaReciboVO;
    private ExpedicaoDiplomaVO expedicaoDiplomaVO;
    private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
    private Double coeficienteRendimentoPeriodoLetivoAluno;
    private Double coeficienteRendimentoGeralAluno;
    AutorizacaoCursoVO autorizacaoCurso;
    private AutorizacaoCursoVO primeiroReconhecimentoCurso;
    private AutorizacaoCursoVO renovacaoReconhecimentoCurso;	
    private Integer quantidadePeriodoLetivoACursar;
    private Integer quantidadeDisciplinasNaoCursadas;
    private Integer cargaHorariaRealizadaAtividadeComplementar;
    private CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO;
    private AdvertenciaVO advertenciaVO;
    private TermoReconhecimentoDividaRelVO termoReconhecimentoDividaRelVO;
    private List<HistoricoVO> listaDisciplinaCertificadoEnsinoMedio;
    private String situacaoFinalPeriodoLetivo;
    private PlanoEnsinoVO planoEnsinoVO;
    private EstagioVO estagioVO;
    private InscricaoVO inscricaoVO;	
    private RequerimentoVO requerimentoVO;
    private Boolean gerarNovoArquivoAssinado;
    private boolean impressaoPdf;
    private DocumentoAssinadoVO documentoAssinado;
	private TipoDoTextoImpressaoContratoEnum tipoTextoEnum;
	private ConfiguracaoFinanceiroVO configuracaoFinanceiroVO;
	private Boolean impressaoContratoExistente = false;
	private List listaDisciplinasPeriodoLetivoAtual;
	private List listaDisciplinasPreRequisitos;
	private List<PlanoEnsinoVO> listaPlanoEnsino;
	private String menorHorarioAluno;
	private String maiorHorarioAluno;
	private List<DisciplinaVO> listaDisciplinasAluno;
	private ConfiguracaoGEDVO configuracaoGEDVO;
	private List<HistoricoVO> listaDisciplinasHistoricoPeriodoLetivoSituacao;
	private String legendaSituacaoHistorico;
	private Double mediaGlobal;
	private CancelamentoVO cancelamentoVO;
	private TransferenciaEntradaVO transferenciaEntradaVO;

	private boolean impressaoDoc;
	//Transiente.
	private NegociacaoContaReceberVO negociacaoContaReceberVO;
	private List<ObservacaoComplementarDiplomaVO> observacaoComplementarDiplomaVOs;
    private FuncionarioVO funcionarioTerceiroVO;
    private CargoVO cargoFuncionarioTerceiro;
    private String numeroRegistroDiploma;
    private ExpedicaoDiplomaVO primeiraViaExpedicaoDiplomaVO;
	private List<DeclaracaoImpostoRendaRel> listaDeclaracaoImpostoRendaRelVO;
	private HorarioProfessorDiaVO horarioProfessorDiaVO;
	private Boolean impressaoContratoMatriculaExterna;
	private PeriodoLetivoVO periodoLetivoIngresso;
	private PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO;
	private MatriculaEnadeVO matriculaEnadeDataVO;
	private ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum;
	private String motivoRejeicao;
	private ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO;
	private ProgramacaoFormaturaVO programacaoFormaturaVO;
	private List<ProgramacaoFormaturaAlunoVO> programacaoFormaturaAlunoVOs;
	private ProgramacaoFormaturaCursoVO programacaoFormaturaCursoVO;
	private HistoricoVO historicoVO;
	private Boolean utilizarNomeRelatorioEspecifico ;
	private String nomeRelatorioEspecifico;
	private Boolean possuiErro;
	private String motivoErro;
	private Boolean impressaoRequerimento;
	private Boolean gravarImpressaoContrato = true;
	
	private PessoaVO pessoaVO;
	private String controleGeracaoAssinatura;
	private String tipoImpressaoContrato;
	
	public ImpressaoContratoVO() {
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	/**
	 * @return the usuarioLogado
	 */
	public UsuarioVO getUsuarioLogado() {
		if (usuarioLogado == null) {
			usuarioLogado = new UsuarioVO();
		}
		return usuarioLogado;
	}

	/**
	 * @param usuarioLogado
	 *            the usuarioLogado to set
	 */
	public void setUsuarioLogado(UsuarioVO usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Integer getQuantidade() {
		if (quantidade == null) {
			quantidade = 0;
		}
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Integer getTextoPadraoDeclaracao() {
		if (textoPadraoDeclaracao == null) {
			textoPadraoDeclaracao = 0;
		}
		return textoPadraoDeclaracao;
	}

	public void setTextoPadraoDeclaracao(Integer textoPadraoDeclaracao) {
		this.textoPadraoDeclaracao = textoPadraoDeclaracao;
	}

	/**
	 * @return the observacao
	 */
	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	/**
	 * @param observacao
	 *            the observacao to set
	 */
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public Boolean getAlunoSelecionado() {
		if (alunoSelecionado == null) {
			alunoSelecionado = Boolean.FALSE;
		}
		return alunoSelecionado;
	}

	public void setAlunoSelecionado(Boolean alunoSelecionado) {
		this.alunoSelecionado = alunoSelecionado;
	}

	/**
	 * @return the disciplinaVO
	 */
	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	/**
	 * @param disciplinaVO
	 *            the disciplinaVO to set
	 */
	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	/**
	 * @return the listaDisciplinasCursadasOuMinistradas
	 */
	public List<HistoricoVO> getListaDisciplinasCursadasOuMinistradas() {
		if (listaDisciplinasCursadasOuMinistradas == null) {
			listaDisciplinasCursadasOuMinistradas = new ArrayList<>();
		}
		return listaDisciplinasCursadasOuMinistradas;
	}

	/**
	 * @param listaDisciplinasCursadasOuMinistradas
	 *            the listaDisciplinasCursadasOuMinistradas to set
	 */
	public void setListaDisciplinasCursadasOuMinistradas(List<HistoricoVO> listaDisciplinasCursadasOuMinistradas) {
		this.listaDisciplinasCursadasOuMinistradas = listaDisciplinasCursadasOuMinistradas;
	}

	/**
	 * @return the professor
	 */
	public FuncionarioVO getProfessor() {
		if (professor == null) {
			professor = new FuncionarioVO();
		}
		return professor;
	}

	/**
	 * @param professor
	 *            the professor to set
	 */
	public void setProfessor(FuncionarioVO professor) {
		if (professor == null) {
			professor = new FuncionarioVO();
		}
		this.professor = professor;
	}

	public List<LogImpressaoContratoVO> getListaLogImpressaoContratoVO() {
		if (listaLogImpressaoContratoVO == null) {
			listaLogImpressaoContratoVO = new ArrayList<LogImpressaoContratoVO>(0);
		}
		return listaLogImpressaoContratoVO;
	}

	public void setListaLogImpressaoContratoVO(List<LogImpressaoContratoVO> listaLogImpressaoContratoVO) {
		this.listaLogImpressaoContratoVO = listaLogImpressaoContratoVO;
	}

	public GradeDisciplinaVO getGradeDisciplinaVO() {
		if (gradeDisciplinaVO == null) {
			gradeDisciplinaVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaVO;
	}

	public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
		this.gradeDisciplinaVO = gradeDisciplinaVO;
	}

	public TrancamentoVO getTrancamentoVO() {
		if (trancamentoVO == null) {
			trancamentoVO = new TrancamentoVO();
		}
		return trancamentoVO;
	}

	public void setTrancamentoVO(TrancamentoVO trancamentoVO) {
		this.trancamentoVO = trancamentoVO;
	}

	public MatriculaVO getMatriculaOrigem() {
		if (matriculaOrigem == null) {
			matriculaOrigem = new MatriculaVO();
		}
		return matriculaOrigem;
	}

	public void setMatriculaOrigem(MatriculaVO matriculaOrigem) {
		this.matriculaOrigem = matriculaOrigem;
	}

	public MatriculaVO getMatriculaDestino() {
		if (matriculaDestino == null) {
			matriculaDestino = new MatriculaVO();
		}
		return matriculaDestino;
	}

	public void setMatriculaDestino(MatriculaVO matriculaDestino) {
		this.matriculaDestino = matriculaDestino;
	}

	public TurmaVO getTurmaOrigem() {
		if (turmaOrigem == null) {
			turmaOrigem = new TurmaVO();
		}
		return turmaOrigem;
	}

	public void setTurmaOrigem(TurmaVO turmaOrigem) {
		this.turmaOrigem = turmaOrigem;
	}

	public TurmaVO getTurmaDestino() {
		if (turmaDestino == null) {
			turmaDestino = new TurmaVO();
		}
		return turmaDestino;
	}

	public void setTurmaDestino(TurmaVO turmaDestino) {
		this.turmaDestino = turmaDestino;
	}

	public FuncionarioVO getFuncionarioPrincipalVO() {
		if (funcionarioPrincipalVO == null) {
			funcionarioPrincipalVO = new FuncionarioVO();
		}
		return funcionarioPrincipalVO;
	}

	public void setFuncionarioPrincipalVO(FuncionarioVO funcionarioPrincipalVO) {
		this.funcionarioPrincipalVO = funcionarioPrincipalVO;
	}

	public FuncionarioVO getFuncionarioSecundarioVO() {
		if (funcionarioSecundarioVO == null) {
			funcionarioSecundarioVO = new FuncionarioVO();
		}
		return funcionarioSecundarioVO;
	}

	public void setFuncionarioSecundarioVO(FuncionarioVO funcionarioSecundarioVO) {
		this.funcionarioSecundarioVO = funcionarioSecundarioVO;
	}

	public CargoVO getCargoFuncionarioPrincipal() {
		if (cargoFuncionarioPrincipal == null) {
			cargoFuncionarioPrincipal = new CargoVO();
		}
		return cargoFuncionarioPrincipal;
	}

	public void setCargoFuncionarioPrincipal(CargoVO cargoFuncionarioPrincipal) {
		this.cargoFuncionarioPrincipal = cargoFuncionarioPrincipal;
	}

	public CargoVO getCargoFuncionarioSecundario() {
		if (cargoFuncionarioSecundario == null) {
			cargoFuncionarioSecundario = new CargoVO();
		}
		return cargoFuncionarioSecundario;
	}

	public void setCargoFuncionarioSecundario(CargoVO cargoFuncionarioSecundario) {
		this.cargoFuncionarioSecundario = cargoFuncionarioSecundario;
	}

	public List<CertificadoCursoExtensaoDisciplinasRelVO> getListaDisciplinasAprovadasPeriodoLetivo() {
		if (listaDisciplinasAprovadasPeriodoLetivo == null) {
			listaDisciplinasAprovadasPeriodoLetivo = new ArrayList<CertificadoCursoExtensaoDisciplinasRelVO>(0);
		}
		return listaDisciplinasAprovadasPeriodoLetivo;
	}

	public void setListaDisciplinasAprovadasPeriodoLetivo(List<CertificadoCursoExtensaoDisciplinasRelVO> listaDisciplinasAprovadasPeriodoLetivo) {
		this.listaDisciplinasAprovadasPeriodoLetivo = listaDisciplinasAprovadasPeriodoLetivo;
	}

	public ControleLivroFolhaReciboVO getControleLivroFolhaReciboVO() {
		if (controleLivroFolhaReciboVO == null) {
			controleLivroFolhaReciboVO = new ControleLivroFolhaReciboVO();
		}
		return controleLivroFolhaReciboVO;
	}

	public void setControleLivroFolhaReciboVO(ControleLivroFolhaReciboVO controleLivroFolhaReciboVO) {
		this.controleLivroFolhaReciboVO = controleLivroFolhaReciboVO;
	}

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		if (configuracaoGeralSistemaVO == null) {
			configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
		}
		return configuracaoGeralSistemaVO;
	}

	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}

	public Double getCoeficienteRendimentoPeriodoLetivoAluno() {
		if (coeficienteRendimentoPeriodoLetivoAluno == null) {
			coeficienteRendimentoPeriodoLetivoAluno = 0.0;
		}
		return coeficienteRendimentoPeriodoLetivoAluno;
	}

	public void setCoeficienteRendimentoPeriodoLetivoAluno(Double coeficienteRendimentoPeriodoLetivoAluno) {
		this.coeficienteRendimentoPeriodoLetivoAluno = coeficienteRendimentoPeriodoLetivoAluno;
	}

	public Double getCoeficienteRendimentoGeralAluno() {
		if (coeficienteRendimentoGeralAluno == null) {
			coeficienteRendimentoGeralAluno = 0.0;
		}
		return coeficienteRendimentoGeralAluno;
	}

	public void setCoeficienteRendimentoGeralAluno(Double coeficienteRendimentoGeralAluno) {
		this.coeficienteRendimentoGeralAluno = coeficienteRendimentoGeralAluno;
	}

	public AutorizacaoCursoVO getAutorizacaoCurso() {
		if (autorizacaoCurso == null) {
			autorizacaoCurso = new AutorizacaoCursoVO();
		}
		return autorizacaoCurso;
	}

	public void setAutorizacaoCurso(AutorizacaoCursoVO autorizacaoCurso) {
		this.autorizacaoCurso = autorizacaoCurso;
	}

	public Integer getQuantidadePeriodoLetivoACursar() {
		if (quantidadePeriodoLetivoACursar == null) {
			quantidadePeriodoLetivoACursar = 0;
		}
		return quantidadePeriodoLetivoACursar;
	}

	public void setQuantidadePeriodoLetivoACursar(Integer quantidadePeriodoLetivoACursar) {
		this.quantidadePeriodoLetivoACursar = quantidadePeriodoLetivoACursar;
	}

	public Integer getQuantidadeDisciplinasNaoCursadas() {
		if (quantidadeDisciplinasNaoCursadas == null) {
			quantidadeDisciplinasNaoCursadas = 0;
		}
		return quantidadeDisciplinasNaoCursadas;
	}

	public void setQuantidadeDisciplinasNaoCursadas(Integer quantidadeDisciplinasNaoCursadas) {
		this.quantidadeDisciplinasNaoCursadas = quantidadeDisciplinasNaoCursadas;
	}

	public Integer getCargaHorariaRealizadaAtividadeComplementar() {
		if (cargaHorariaRealizadaAtividadeComplementar == null) {
			cargaHorariaRealizadaAtividadeComplementar = 0;
		}
		return cargaHorariaRealizadaAtividadeComplementar;
	}

	public void setCargaHorariaRealizadaAtividadeComplementar(Integer cargaHorariaRealizadaAtividadeComplementar) {
		this.cargaHorariaRealizadaAtividadeComplementar = cargaHorariaRealizadaAtividadeComplementar;
	}

	public CertificadoCursoExtensaoRelVO getCertificadoCursoExtensaoRelVO() {
		if (certificadoCursoExtensaoRelVO == null) {
			certificadoCursoExtensaoRelVO = new CertificadoCursoExtensaoRelVO();
		}
		return certificadoCursoExtensaoRelVO;
	}

	public void setCertificadoCursoExtensaoRelVO(CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO) {
		this.certificadoCursoExtensaoRelVO = certificadoCursoExtensaoRelVO;
	}

	public AdvertenciaVO getAdvertenciaVO() {
		if (advertenciaVO == null) {
			advertenciaVO = new AdvertenciaVO();
		}
		return advertenciaVO;
	}

	public void setAdvertenciaVO(AdvertenciaVO advertenciaVO) {
		this.advertenciaVO = advertenciaVO;
	}

	public TermoReconhecimentoDividaRelVO getTermoReconhecimentoDividaRelVO() {
		if (termoReconhecimentoDividaRelVO == null) {
			termoReconhecimentoDividaRelVO = new TermoReconhecimentoDividaRelVO();
		}
		return termoReconhecimentoDividaRelVO;
	}

	public void setTermoReconhecimentoDividaRelVO(TermoReconhecimentoDividaRelVO termoReconhecimentoDividaRelVO) {
		this.termoReconhecimentoDividaRelVO = termoReconhecimentoDividaRelVO;
	}

	public List<HistoricoVO> getListaDisciplinaCertificadoEnsinoMedio() {
		if (listaDisciplinaCertificadoEnsinoMedio == null) {
			listaDisciplinaCertificadoEnsinoMedio = new ArrayList<HistoricoVO>(0);
		}
		return listaDisciplinaCertificadoEnsinoMedio;
	}

	public void setListaDisciplinaCertificadoEnsinoMedio(List<HistoricoVO> listaDisciplinaCertificadoEnsinoMedio) {
		this.listaDisciplinaCertificadoEnsinoMedio = listaDisciplinaCertificadoEnsinoMedio;
	}

	public String getSituacaoFinalPeriodoLetivo() {
		if (situacaoFinalPeriodoLetivo == null) {
			situacaoFinalPeriodoLetivo = "";
		}
		return situacaoFinalPeriodoLetivo;
	}

	public void setSituacaoFinalPeriodoLetivo(String situacaoFinalPeriodoLetivo) {
		this.situacaoFinalPeriodoLetivo = situacaoFinalPeriodoLetivo;
	}

	public PlanoEnsinoVO getPlanoEnsinoVO() {
		if (planoEnsinoVO == null) {
			planoEnsinoVO = new PlanoEnsinoVO();
		}
		return planoEnsinoVO;
	}

	public void setPlanoEnsinoVO(PlanoEnsinoVO planoEnsinoVO) {
		this.planoEnsinoVO = planoEnsinoVO;
	}

	public AutorizacaoCursoVO getPrimeiroReconhecimentoCurso() {
		if (primeiroReconhecimentoCurso == null) {
			primeiroReconhecimentoCurso = new AutorizacaoCursoVO();
		}
		return primeiroReconhecimentoCurso;
	}

	public void setPrimeiroReconhecimentoCurso(AutorizacaoCursoVO primeiroReconhecimentoCurso) {
		this.primeiroReconhecimentoCurso = primeiroReconhecimentoCurso;
	}

	public AutorizacaoCursoVO getRenovacaoReconhecimentoCurso() {
		if (renovacaoReconhecimentoCurso == null) {
			renovacaoReconhecimentoCurso = new AutorizacaoCursoVO();
		}
		return renovacaoReconhecimentoCurso;
	}

	public void setRenovacaoReconhecimentoCurso(AutorizacaoCursoVO renovacaoReconhecimentoCurso) {
		this.renovacaoReconhecimentoCurso = renovacaoReconhecimentoCurso;
	}
	
	/**
     * @return the estagioVO
     */
    public EstagioVO getEstagioVO() {
        if (estagioVO == null) {
            estagioVO = new EstagioVO();
        }
        return estagioVO;
    }

    /**
     * @param estagioVO the estagioVO to set
     */
    public void setEstagioVO(EstagioVO estagioVO) {
        this.estagioVO = estagioVO;
    }	
	
	public InscricaoVO getInscricaoVO() {
		if (inscricaoVO == null) {
			inscricaoVO = new InscricaoVO();
		}
		return inscricaoVO;
	}

	public void setInscricaoVO(InscricaoVO inscricaoVO) {
		this.inscricaoVO = inscricaoVO;
	}
	
	public RequerimentoVO getRequerimentoVO() {
		if(requerimentoVO == null){
			requerimentoVO = new RequerimentoVO();
		}
		return requerimentoVO;
	}

	public void setRequerimentoVO(RequerimentoVO requerimentoVO) {
		this.requerimentoVO = requerimentoVO;
	}

	public Boolean getGerarNovoArquivoAssinado() {
		if (gerarNovoArquivoAssinado == null) {
			gerarNovoArquivoAssinado = false;
		}
		return gerarNovoArquivoAssinado;
	}

	public void setGerarNovoArquivoAssinado(Boolean gerarNovoArquivoAssinado) {
		this.gerarNovoArquivoAssinado = gerarNovoArquivoAssinado;
	}
	
	public boolean isImpressaoPdf() {
		return impressaoPdf;
	}

	public void setImpressaoPdf(boolean impressaoPdf) {
		this.impressaoPdf = impressaoPdf;
	}
	
	public DocumentoAssinadoVO getDocumentoAssinado() {
		if (documentoAssinado == null) {
			documentoAssinado = new DocumentoAssinadoVO();
		}
		return documentoAssinado;
	}

	public void setDocumentoAssinado(DocumentoAssinadoVO documentoAssinado) {
		this.documentoAssinado = documentoAssinado;
	}

	public TipoDoTextoImpressaoContratoEnum getTipoTextoEnum() {
		if(tipoTextoEnum == null){
			tipoTextoEnum = TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO;
		}
		return tipoTextoEnum;
	}

	public void setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum tipoTextoEnum) {
		this.tipoTextoEnum = tipoTextoEnum;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	

	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
		if(configuracaoFinanceiroVO == null){
			configuracaoFinanceiroVO =  new ConfiguracaoFinanceiroVO();
		}
		return configuracaoFinanceiroVO;
	}

	public void setConfiguracaoFinanceiroVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
		this.configuracaoFinanceiroVO = configuracaoFinanceiroVO;
	}

	public Boolean getImpressaoContratoExistente() {
		if(impressaoContratoExistente == null){
			impressaoContratoExistente = false;
		}
		return impressaoContratoExistente;
	}

	public void setImpressaoContratoExistente(Boolean impressaoContratoExistente) {
		this.impressaoContratoExistente = impressaoContratoExistente;
	}

	public List getListaDisciplinasPeriodoLetivoAtual() {
		if (listaDisciplinasPeriodoLetivoAtual == null) {
			listaDisciplinasPeriodoLetivoAtual = new ArrayList<>();
		}
		return listaDisciplinasPeriodoLetivoAtual;
	}

	public void setListaDisciplinasPeriodoLetivoAtual(List listaDisciplinasPeriodoLetivoAtual) {
		this.listaDisciplinasPeriodoLetivoAtual = listaDisciplinasPeriodoLetivoAtual;
	}

	public List getListaDisciplinasPreRequisitos() {
		if (listaDisciplinasPreRequisitos == null) {
			listaDisciplinasPreRequisitos = new ArrayList<>();
		}
		
		return listaDisciplinasPreRequisitos;
	}

	public void setListaDisciplinasPreRequisitos(List listaDisciplinasPreRequisitos) {
		this.listaDisciplinasPreRequisitos = listaDisciplinasPreRequisitos;
	}

	public List<PlanoEnsinoVO> getListaPlanoEnsino() {
		if(listaPlanoEnsino == null) {
			listaPlanoEnsino = new ArrayList<PlanoEnsinoVO>();
		}
		return listaPlanoEnsino;
	}

	public void setListaPlanoEnsino(List<PlanoEnsinoVO> listaPlanoEnsino) {
		this.listaPlanoEnsino = listaPlanoEnsino;
	}

	public ExpedicaoDiplomaVO getExpedicaoDiplomaVO() {
		if (expedicaoDiplomaVO == null) {
			expedicaoDiplomaVO = new ExpedicaoDiplomaVO();
		}
		return expedicaoDiplomaVO;
	}

	public void setExpedicaoDiplomaVO(ExpedicaoDiplomaVO expedicaoDiplomaVO) {
		this.expedicaoDiplomaVO = expedicaoDiplomaVO;
	}
	
	public String getMenorHorarioAluno() {
		if (menorHorarioAluno == null) {
			menorHorarioAluno = "";
		}
		return menorHorarioAluno;
	}

	public void setMenorHorarioAluno(String menorHorarioAluno) {
		this.menorHorarioAluno = menorHorarioAluno;
	}

	public String getMaiorHorarioAluno() {
		if (maiorHorarioAluno == null) {
			maiorHorarioAluno = "";
		}
		return maiorHorarioAluno;
	}

	public void setMaiorHorarioAluno(String maiorHorarioAluno) {
		this.maiorHorarioAluno = maiorHorarioAluno;
	}

	public List<DisciplinaVO> getListaDisciplinasAluno() {
		if (listaDisciplinasAluno == null) {
			listaDisciplinasAluno = new ArrayList<DisciplinaVO>();
		}
		return listaDisciplinasAluno;
	}

	public void setListaDisciplinasAluno(List<DisciplinaVO> listaDisciplinasAluno) {
		this.listaDisciplinasAluno = listaDisciplinasAluno;
	}	
	public NegociacaoContaReceberVO getNegociacaoContaReceberVO() {
		return negociacaoContaReceberVO;
	}

	public void setNegociacaoContaReceberVO(NegociacaoContaReceberVO negociacaoContaReceberVO) {
		this.negociacaoContaReceberVO = negociacaoContaReceberVO;
	}

	
	public ConfiguracaoGEDVO getConfiguracaoGEDVO() {
		if (configuracaoGEDVO == null) {
			configuracaoGEDVO = new ConfiguracaoGEDVO();
		}
		return configuracaoGEDVO;
	}

	public void setConfiguracaoGEDVO(ConfiguracaoGEDVO configuracaoGEDVO) {
		this.configuracaoGEDVO = configuracaoGEDVO;
	}

	public List<HistoricoVO> getListaDisciplinasHistoricoPeriodoLetivoSituacao() {
		if(listaDisciplinasHistoricoPeriodoLetivoSituacao == null) {
			listaDisciplinasHistoricoPeriodoLetivoSituacao = new ArrayList<HistoricoVO>(0);
		}
		return listaDisciplinasHistoricoPeriodoLetivoSituacao;
	}

	public void setListaDisciplinasHistoricoPeriodoLetivoSituacao(
			List<HistoricoVO> listaDisciplinasHistoricoPeriodoLetivoSituacao) {
		this.listaDisciplinasHistoricoPeriodoLetivoSituacao = listaDisciplinasHistoricoPeriodoLetivoSituacao;
	}

	public String getLegendaSituacaoHistorico() {
		if(legendaSituacaoHistorico == null) {
			legendaSituacaoHistorico = "";
		}
		return legendaSituacaoHistorico;
	}

	public void setLegendaSituacaoHistorico(String legendaSituacaoHistorico) {
		this.legendaSituacaoHistorico = legendaSituacaoHistorico;
	}

	public List<DeclaracaoImpostoRendaRel> getListaDeclaracaoImpostoRendaRelVO() {
		if(listaDeclaracaoImpostoRendaRelVO == null) {
			listaDeclaracaoImpostoRendaRelVO = new ArrayList<DeclaracaoImpostoRendaRel>();
		}
		return listaDeclaracaoImpostoRendaRelVO;
	}
	
	
	public Double getMediaGlobal() {
		if (mediaGlobal == null) {
			mediaGlobal = 0.0;
		}
		return mediaGlobal;
	}

	public void setMediaGlobal(Double mediaGlobal) {
		this.mediaGlobal = mediaGlobal;
	}
	public boolean isImpressaoDoc() {
		return impressaoDoc;
	}

	public void setImpressaoDoc(boolean impressaoDoc) {
		this.impressaoDoc = impressaoDoc;
	}
	public List<ObservacaoComplementarDiplomaVO> getObservacaoComplementarDiplomaVOs() {
		if (observacaoComplementarDiplomaVOs == null) {
			observacaoComplementarDiplomaVOs = new ArrayList<>();
		}
		return observacaoComplementarDiplomaVOs;
	}

	public void setObservacaoComplementarDiplomaVOs(List<ObservacaoComplementarDiplomaVO> observacaoComplementarDiplomaVOs) {
		this.observacaoComplementarDiplomaVOs = observacaoComplementarDiplomaVOs;
	}

	public FuncionarioVO getFuncionarioTerceiroVO() {
		if (funcionarioTerceiroVO == null) {
			funcionarioTerceiroVO = new FuncionarioVO();
		}
		return funcionarioTerceiroVO;
	}

	public void setFuncionarioTerceiroVO(FuncionarioVO funcionarioTerceiroVO) {
		this.funcionarioTerceiroVO = funcionarioTerceiroVO;
	}

	public CargoVO getCargoFuncionarioTerceiro() {
		if (cargoFuncionarioTerceiro == null) {
			cargoFuncionarioTerceiro = new CargoVO();
		}
		return cargoFuncionarioTerceiro;
	}

	public void setCargoFuncionarioTerceiro(CargoVO cargoFuncionarioTerceiro) {
		this.cargoFuncionarioTerceiro = cargoFuncionarioTerceiro;
	}

	public String getNumeroRegistroDiploma() {
		if (numeroRegistroDiploma == null) {
			numeroRegistroDiploma = "";
		}
		return numeroRegistroDiploma;
	}

	public void setNumeroRegistroDiploma(String numeroRegistroDiploma) {
		this.numeroRegistroDiploma = numeroRegistroDiploma;
	}

	public ExpedicaoDiplomaVO getPrimeiraViaExpedicaoDiplomaVO() {
		if (primeiraViaExpedicaoDiplomaVO == null) {
			primeiraViaExpedicaoDiplomaVO = new ExpedicaoDiplomaVO();
		}
		return primeiraViaExpedicaoDiplomaVO;
	}

	public void setPrimeiraViaExpedicaoDiplomaVO(ExpedicaoDiplomaVO primeiraViaExpedicaoDiplomaVO) {
		this.primeiraViaExpedicaoDiplomaVO = primeiraViaExpedicaoDiplomaVO;
	}

	public void setListaDeclaracaoImpostoRendaRelVO(List<DeclaracaoImpostoRendaRel> listaDeclaracaoImpostoRendaRelVO) {
		this.listaDeclaracaoImpostoRendaRelVO = listaDeclaracaoImpostoRendaRelVO;
	}

	public HorarioProfessorDiaVO getHorarioProfessorDiaVO() {
		if (horarioProfessorDiaVO == null) {
			horarioProfessorDiaVO = new HorarioProfessorDiaVO();
		}
		return horarioProfessorDiaVO;
	}

	public void setHorarioProfessorDiaVO(HorarioProfessorDiaVO horarioProfessorDiaVO) {
		this.horarioProfessorDiaVO = horarioProfessorDiaVO;
	}

	public Boolean getImpressaoContratoMatriculaExterna() {
		if (impressaoContratoMatriculaExterna == null) {
			impressaoContratoMatriculaExterna = false;
		}
		return impressaoContratoMatriculaExterna;
	}

	public void setImpressaoContratoMatriculaExterna(Boolean impressaoContratoMatriculaExterna) {
		this.impressaoContratoMatriculaExterna = impressaoContratoMatriculaExterna;
	}
	

	public PeriodoLetivoVO getPeriodoLetivoIngresso() {
		if (periodoLetivoIngresso == null) {
			periodoLetivoIngresso = new PeriodoLetivoVO();
		}
		return periodoLetivoIngresso;
	}

	public void setPeriodoLetivoIngresso(PeriodoLetivoVO periodoLetivoIngresso) {
		this.periodoLetivoIngresso = periodoLetivoIngresso;
	}

	public PessoaEmailInstitucionalVO getPessoaEmailInstitucionalVO() {
		if (pessoaEmailInstitucionalVO == null) {
			pessoaEmailInstitucionalVO = new PessoaEmailInstitucionalVO();
		}
		return pessoaEmailInstitucionalVO;
	}

	public void setPessoaEmailInstitucionalVO(PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO) {
		this.pessoaEmailInstitucionalVO = pessoaEmailInstitucionalVO;
	}

	public MatriculaEnadeVO getMatriculaEnadeDataVO() {
		if (matriculaEnadeDataVO == null) {
			matriculaEnadeDataVO = new MatriculaEnadeVO();
		}
		return matriculaEnadeDataVO;
	}

	public void setMatriculaEnadeDataVO(MatriculaEnadeVO matriculaEnadeDataVO) {
		this.matriculaEnadeDataVO = matriculaEnadeDataVO;
	}

	public ProvedorDeAssinaturaEnum getProvedorDeAssinaturaEnum() {
		return provedorDeAssinaturaEnum;
	}

	public void setProvedorDeAssinaturaEnum(ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum) {
		this.provedorDeAssinaturaEnum = provedorDeAssinaturaEnum;
	}

	public String getMotivoRejeicao() {
		if (motivoRejeicao == null) {
			motivoRejeicao = "";
		}
		return motivoRejeicao;
	}

	public void setMotivoRejeicao(String motivoRejeicao) {
		this.motivoRejeicao = motivoRejeicao;
	}

	public ProgramacaoFormaturaAlunoVO getProgramacaoFormaturaAlunoVO() {
		if (programacaoFormaturaAlunoVO == null) {
			programacaoFormaturaAlunoVO = new ProgramacaoFormaturaAlunoVO();
		}
		return programacaoFormaturaAlunoVO;
	}
	
	public void setProgramacaoFormaturaAlunoVO(ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO) {
		this.programacaoFormaturaAlunoVO = programacaoFormaturaAlunoVO;
	}
	
	public ProgramacaoFormaturaVO getProgramacaoFormaturaVO() {
		if (programacaoFormaturaVO == null) {
			programacaoFormaturaVO = new ProgramacaoFormaturaVO();
		}
		return programacaoFormaturaVO;
	}
	
	public void setProgramacaoFormaturaVO(ProgramacaoFormaturaVO programacaoFormaturaVO) {
		this.programacaoFormaturaVO = programacaoFormaturaVO;
	}
	
	public List<ProgramacaoFormaturaAlunoVO> getProgramacaoFormaturaAlunoVOs() {
		if (programacaoFormaturaAlunoVOs == null) {
			programacaoFormaturaAlunoVOs = new ArrayList<ProgramacaoFormaturaAlunoVO>(0);
		}
		return programacaoFormaturaAlunoVOs;
	}
	
	public void setProgramacaoFormaturaAlunoVOs(List<ProgramacaoFormaturaAlunoVO> programacaoFormaturaAlunoVOs) {
		this.programacaoFormaturaAlunoVOs = programacaoFormaturaAlunoVOs;
	}
	
	public ProgramacaoFormaturaCursoVO getProgramacaoFormaturaCursoVO() {
		if (programacaoFormaturaCursoVO == null) {
			programacaoFormaturaCursoVO = new ProgramacaoFormaturaCursoVO();
		}
		return programacaoFormaturaCursoVO;
	}
	
	public void setProgramacaoFormaturaCursoVO(ProgramacaoFormaturaCursoVO programacaoFormaturaCursoVO) {
		this.programacaoFormaturaCursoVO = programacaoFormaturaCursoVO;
	}
	public HistoricoVO getHistoricoVO() {
		if(historicoVO == null) {
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}

	public Boolean getUtilizarNomeRelatorioEspecifico() {
		if(utilizarNomeRelatorioEspecifico == null ) {
			utilizarNomeRelatorioEspecifico = Boolean.FALSE;
		}
		return utilizarNomeRelatorioEspecifico;
	}

	public void setUtilizarNomeRelatorioEspecifico(Boolean utilizarNomeRelatorioEspecifico) {
		this.utilizarNomeRelatorioEspecifico = utilizarNomeRelatorioEspecifico;
	}

	public String getNomeRelatorioEspecifico() {
		if(nomeRelatorioEspecifico == null ) {
			nomeRelatorioEspecifico =""; 
		}
		return nomeRelatorioEspecifico;
	}

	public void setNomeRelatorioEspecifico(String nomeRelatorioEspecifico) {
		this.nomeRelatorioEspecifico = nomeRelatorioEspecifico;
	}
	
	public Boolean getImpressaoRequerimento() {
		if (impressaoRequerimento == null) {
			impressaoRequerimento = false;
		}
		return impressaoRequerimento;
	}
	
	public void setImpressaoRequerimento(Boolean impressaoRequerimento) {
		this.impressaoRequerimento = impressaoRequerimento;
	}
	
	public Boolean getPossuiErro() {
		if(possuiErro == null ) {
			possuiErro = Boolean.FALSE;
		}
		return possuiErro;
	}

	public void setPossuiErro(Boolean possuiErro) {
		this.possuiErro = possuiErro;
	}

	public String getMotivoErro() {
		if(motivoErro == null ) {
			motivoErro = "";
		}
		return motivoErro;
	}

	public void setMotivoErro(String motivoErro) {
		this.motivoErro = motivoErro;
	}
	
	public Boolean getGravarImpressaoContrato() {
		if (gravarImpressaoContrato == null) {
			gravarImpressaoContrato = true;
		}
		return gravarImpressaoContrato;
	}
	
	public void setGravarImpressaoContrato(Boolean gravarImpressaoContrato) {
		this.gravarImpressaoContrato = gravarImpressaoContrato;
	}
	public PessoaVO getPessoaVO() {
		if(pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}
	
	public String getControleGeracaoAssinatura() {
		if (controleGeracaoAssinatura == null) {
			controleGeracaoAssinatura = "";
		}
		return controleGeracaoAssinatura;
	}

	public void setControleGeracaoAssinatura(String controleGeracaoAssinatura) {
		this.controleGeracaoAssinatura = controleGeracaoAssinatura;
	}

	public CancelamentoVO getCancelamentoVO() {
		if(cancelamentoVO == null) {
			cancelamentoVO = new CancelamentoVO();
		}
		return cancelamentoVO;
	}

	public void setCancelamentoVO(CancelamentoVO cancelamentoVO) {
		this.cancelamentoVO = cancelamentoVO;
	}

	public TransferenciaEntradaVO getTransferenciaEntradaVO() {
		if(transferenciaEntradaVO == null) {
			transferenciaEntradaVO = new TransferenciaEntradaVO();
		}
		return transferenciaEntradaVO;
	}

	public void setTransferenciaEntradaVO(TransferenciaEntradaVO transferenciaEntradaVO) {
		this.transferenciaEntradaVO = transferenciaEntradaVO;
	}

	public String getTipoImpressaoContrato() {
		return tipoImpressaoContrato;
	}
	
	public void setTipoImpressaoContrato(String tipoImpressaoContrato) {
		this.tipoImpressaoContrato = tipoImpressaoContrato;
	}
	
	public Boolean getTipoImpressaoContratoDiplomaDigital() {
		return Uteis.isAtributoPreenchido(getTipoImpressaoContrato()) && getTipoImpressaoContrato().equals("DIPLOMA_DIGITAL");
	}
	
	public Boolean getTipoImpressaoContratoExpedicaoDiploma() {
		return Uteis.isAtributoPreenchido(getTipoImpressaoContrato()) && getTipoImpressaoContrato().equals("EXPEDICAO_DIPLOMA");
	}

}
