package negocio.comuns.academico;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.StringUtils;

import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.academico.enumeradores.VersaoDiplomaDigitalEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracaoGedOrigemVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import webservice.DateAdapter;

@XmlRootElement(name = "DocumentoAssinado")
public class DocumentoAssinadoVO extends SuperVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8042165368128584351L;	
	@XmlElement(name = "codigo")
	private Integer codigo;	
	private Date dataRegistro;
	private UnidadeEnsinoVO unidadeEnsinoVO;	
	private ArquivoVO arquivo;	
	private UsuarioVO usuario;
	@XmlElement(name = "tipoOrigemDocumentoAssinadoEnum")
	private TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum;
	private MatriculaVO matricula;
	private GradeCurricularVO gradeCurricular;
	private TurmaVO turma;
	private DisciplinaVO disciplina;
	private String ano;
	private String semestre;
	private ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum;
	private String chaveProvedorDeAssinatura;
	private String urlProvedorDeAssinatura;
	private String codigoProvedorDeAssinatura;
	private boolean documentoAssinadoInvalido = false;
	private String motivoDocumentoAssinadoInvalido;
	private List<DocumentoAssinadoPessoaVO> listaDocumentoAssinadoPessoa;
	private CursoVO cursoVO;
	private ProgramacaoFormaturaVO programacaoFormaturaVO;
	
	private ProvedorDeAssinaturaEnum provedorAssinaturaVisaoAdm;
	private  Boolean selecionado;
	/**
	 * Atributos transient
	 */
	@XmlElement(name = "tipoRequerimento")
	private String tipoRequerimento;
	@XmlElement(name = "pessoaNome")
	private String pessoaNome;
	@XmlElement(name = "pessoaCpf")
	private String pessoaCpf;
	@XmlElement(name = "pessoaData")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date pessoaData;
	@XmlElement(name = "dataRegistroApresentar")
	private String dataRegistroApresentar;
	@XmlElement(name = "tipoOrigemDocumentoAssinadoEnumApresentar")
	private String tipoOrigemDocumentoAssinadoEnumApresentar;
	
	private ConfiguracaoGedOrigemVO configuracaoGedOrigemVO;
	@XmlElement(name = "ordemAssinatura")
	private Integer ordemAssinatura;
	private MatriculaPeriodoVO matriculaPeriodo;
	
	private Boolean documentoContrato;
	private PlanoEnsinoVO planoEnsinoVO;
	/*
	 * Atributo TRANSIENTE.
	 */
	private Integer codigoOrigem;
	
	private String caminhoPreview;	
	private DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO;	
	private Boolean impressaoContratoMatriculaExterna;
	private ArquivoVO arquivoVisual;
	private String codigoValidacaoHistoricoDigital;
	private Boolean decisaoJudicial;
	private VersaoDiplomaDigitalEnum versaoDiploma;
	private ExpedicaoDiplomaVO expedicaoDiplomaVO;
	private String codigoValidacaoDiplomaDigital;
	private String codigoValidacaoCurriculoEscolarDigital;
	@XmlElement(name = "dataAssinaturaSeiSignature")
	private Date dataAssinaturaSeiSignature;
	private Integer situacaoDocumentoApresentar;
	private List<String> listaErroImportaAssinatura;
	@XmlElement(name = "idDiplomaDigital")
	private String idDiplomaDigital;
	@XmlElement(name = "idDadosRegistrosDiplomaDigital")
	private String idDadosRegistrosDiplomaDigital;
	private Boolean erro;
	private String motivoErro;
	private Boolean isGerarNovoTermoESolicitarNovasAssinaturas;
	private String concedenteDocumentoEstagio;
	private String responsavelConcedente;
	private String nomeGradeCurricularEstagio;
	private String tituloColacaoGrau;
	private String infoAtaColacaoGrau;
	private OffsetDateTime expirationDate;
	private Date dataColacaoGrau;
	private Boolean isAtaColacaoGrau;

	/**
	 * Atributo Transient
	 * 
	 */
	private ConsistirException consistirException;
	
	
	public TipoOrigemDocumentoAssinadoEnum getTipoOrigemDocumentoAssinadoEnum() {
		if (tipoOrigemDocumentoAssinadoEnum == null) {
			tipoOrigemDocumentoAssinadoEnum = TipoOrigemDocumentoAssinadoEnum.NENHUM;
		}
		return tipoOrigemDocumentoAssinadoEnum;
	}

	public void setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum) {
		this.tipoOrigemDocumentoAssinadoEnum = tipoOrigemDocumentoAssinadoEnum;
	}
	
	public String getTipoOrigemDocumentoAssinadoEnumApresentar() {
		if (tipoOrigemDocumentoAssinadoEnumApresentar == null || tipoOrigemDocumentoAssinadoEnumApresentar == "") {
			tipoOrigemDocumentoAssinadoEnumApresentar = getTipoOrigemDocumentoAssinadoEnum().getDescricao();
		}
		return tipoOrigemDocumentoAssinadoEnumApresentar;
	}	

	public void setTipoOrigemDocumentoAssinadoEnumApresentar(String tipoOrigemDocumentoAssinadoEnumApresentar) {
		this.tipoOrigemDocumentoAssinadoEnumApresentar = tipoOrigemDocumentoAssinadoEnumApresentar;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}
	
	public Integer getCodigoOrigem() {
		if (codigoOrigem == null) {
			codigoOrigem = 0;
		}
		return codigoOrigem;
	}
	
	public void setCodigoOrigem(Integer codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
	}
	

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public GradeCurricularVO getGradeCurricular() {
		if (gradeCurricular == null) {
			gradeCurricular = new GradeCurricularVO();
		}
		return gradeCurricular;
	}

	public void setGradecurricular(GradeCurricularVO gradeCurricular) {
		this.gradeCurricular = gradeCurricular;
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

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}
	
	public String getDataRegistroApresentar() {
		if (dataRegistroApresentar == null || dataRegistroApresentar == "") {
			dataRegistroApresentar = Uteis.getData(getDataRegistro(), "dd/MM/yyyy HH:mm:ss");
		}
		return dataRegistroApresentar;
	}

	public void setDataRegistroApresentar(String dataRegistroApresentar) {
		this.dataRegistroApresentar = dataRegistroApresentar;
	}

	

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name = "arquivo")
	public ArquivoVO getArquivo() {
		if (arquivo == null) {
			arquivo = new ArquivoVO();
		}
		return arquivo;
	}

	public void setArquivo(ArquivoVO arquivo) {
		this.arquivo = arquivo;
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

	@XmlElement(name = "numeroDocumentoAssinado")
	public String getNumeroDocumentoAssinado() {
		if(!Uteis.isAtributoPreenchido(getCodigo())){
			return StringUtils.leftPad("0", 12, "0");
		}
		return StringUtils.leftPad(getCodigo().toString(), 12, "0");
	}
	public String getTipoRequerimento() {
		if (tipoRequerimento == null) {
			tipoRequerimento = "";
		}
		return tipoRequerimento;
	}

	public void setTipoRequerimento(String tipoRequerimento) {
		this.tipoRequerimento = tipoRequerimento;
	}

	public String getPessoaNome() {
		if (pessoaNome == null) {
			pessoaNome = "";
		}
		return pessoaNome;
	}

	public void setPessoaNome(String pessoaNome) {
		this.pessoaNome = pessoaNome;
	}

	public String getPessoaCpf() {
		if (pessoaCpf == null) {
			pessoaCpf = "";
		}
		return pessoaCpf;
	}

	public void setPessoaCpf(String pessoaCpf) {
		this.pessoaCpf = pessoaCpf;
	}

	public Date getPessoaData() {
		return pessoaData;
	}

	public void setPessoaData(Date pessoaData) {
		this.pessoaData = pessoaData;
	}
	
	@XmlElement(name = "listaDocumentoAssinadoPessoa")
	public List<DocumentoAssinadoPessoaVO> getListaDocumentoAssinadoPessoa() {
		if (listaDocumentoAssinadoPessoa == null) {
			listaDocumentoAssinadoPessoa = new ArrayList<>();
		}
		return listaDocumentoAssinadoPessoa;
	}

	public void setListaDocumentoAssinadoPessoa(List<DocumentoAssinadoPessoaVO> listaDocumentoAssinadoPessoa) {
		this.listaDocumentoAssinadoPessoa = listaDocumentoAssinadoPessoa;
	}
	
	
	@XmlElement(name = "unidadeEnsinoVO")
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
	

	@Override
	public String toString() {
		return "DocumentoAssinadoVO [codigo=" + codigo + "]";
	}
	
	
	public String descricaoPersonalizada;
	
	@XmlElement(name = "descricaoPersonalizada")
	public String getDescricaoPersonalizada() {
		if(descricaoPersonalizada == null) {
			if(Uteis.isAtributoPreenchido(getMatricula().getMatricula())) {
				descricaoPersonalizada = "Aluno: "+getMatricula().getMatricula()+" - "+getMatricula().getAluno().getNome();
				
				if(!getAno().isEmpty()) {
					descricaoPersonalizada += " - "+getAno();
				}
				if(!getSemestre().isEmpty()) {
					descricaoPersonalizada += "/"+getSemestre();
				}
			
			}else if(Uteis.isAtributoPreenchido(getTurma())) {
				descricaoPersonalizada = "Turma: "+getTurma().getIdentificadorTurma();
				if(Uteis.isAtributoPreenchido(getDisciplina())) {
					descricaoPersonalizada += " - Disciplina: "+getDisciplina().getNome()+" ";
				}else if(Uteis.isAtributoPreenchido(getAno()) && Uteis.isAtributoPreenchido(getSemestre())) {
					descricaoPersonalizada += " - Ano/Semestre: "+getAno()+"/"+getSemestre();
				}else if(Uteis.isAtributoPreenchido(getAno())) {
					descricaoPersonalizada += " - Ano: "+getAno()+" ";
				}
			}else if(Uteis.isAtributoPreenchido(getTurma())) {
				descricaoPersonalizada = getArquivo().getDescricao();
			}else if(getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.UPLOAD_INSTITUCIONAL)) {
				descricaoPersonalizada = getArquivo().getDescricao();
			} else if (Uteis.isAtributoPreenchido(getProgramacaoFormaturaVO())) {
				descricaoPersonalizada = " Pro. Formatura: " + getProgramacaoFormaturaVO().getCodigo() + (Uteis.isAtributoPreenchido(getCursoVO()) ? " - Curso: " + getCursoVO().getNome(): "") + " - Colação Grau: " + getProgramacaoFormaturaVO().getColacaoGrauVO().getTitulo() + " - Data Cadastro: " + getProgramacaoFormaturaVO().getDataCadastro_Apresentar();
			} else if (Uteis.isAtributoPreenchido(getGradeCurricular()) && Uteis.isAtributoPreenchido(getCursoVO())) {
				descricaoPersonalizada = "Curso: " + getCursoVO().getNome() + " Matriz Curricular: " + getGradeCurricular().getNome();
			}
		}
		return descricaoPersonalizada;
	}
	
	public String getUrlQrCode() {
		String url = null;
		switch (getTipoOrigemDocumentoAssinadoEnum()) {
		case EXPEDICAO_DIPLOMA:
			url = "/visaoAdministrativo/academico/documentoAssinado/diploma.xhtml?tipoDoc=" + getTipoOrigemDocumentoAssinadoEnum() + "&dados=" + getMatricula().getAluno().getCPF() + "";
			break;
		case DIPLOMA_DIGITAL:
			url = "/webservice/documentoAssinadoRS/consultarDiplomaCodigoValidacao/" + getCodigoValidacaoDiplomaDigital() + "";
			break;
		case HISTORICO_DIGITAL:
			url = "/webservice/documentoAssinadoRS/consultarHistoricoCodigoValidacao/" + getCodigoValidacaoHistoricoDigital() + "";
			break;			
		case CURRICULO_ESCOLAR_DIGITAL:
			url = "/webservice/documentoAssinadoRS/consultarCurriculoCodigoValidacao/" + getCodigoValidacaoCurriculoEscolarDigital() + "";
			break;			
		default:
			url = "/visaoAdministrativo/academico/documentoAssinado.xhtml?tipoDoc=" + getTipoOrigemDocumentoAssinadoEnum() + "&dados=" + getCodigo() + "";
			break;
		}
		return url;
	}
	
	
	public void setDescricaoPersonalizada(String descricaoPersonalizada) {
		this.descricaoPersonalizada = descricaoPersonalizada;
	}

	public Boolean getUsuarioPodeAssinarDocumento(UsuarioVO usuarioVO) {
		for (DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO : getListaDocumentoAssinadoPessoa()) {
			if (documentoAssinadoPessoaVO.getPessoaVO().getCodigo().equals(usuarioVO.getPessoa().getCodigo()) && !documentoAssinadoPessoaVO.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO)) {
				if (getTipoOrigemDocumentoAssinadoEnum().isXmlMec() 
						&& getProvedorDeAssinaturaEnum().isProvedorSei()
						&& Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoaResposanvelAssinatura())
						&& getDocumentoAssinadoPessoaResposanvelAssinatura().getPessoaVO().getCodigo().equals(usuarioVO.getPessoa().getCodigo())) {
					return true;
				} else if (!getTipoOrigemDocumentoAssinadoEnum().isXmlMec()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public String getUrlAssinaturaUsuario(UsuarioVO usuarioVO) {
		 Optional<DocumentoAssinadoPessoaVO> findFirst = getListaDocumentoAssinadoPessoa().stream().filter(p-> p.getPessoaVO().getCodigo().equals(usuarioVO.getPessoa().getCodigo())).findFirst();
		 return findFirst.isPresent() && Uteis.isAtributoPreenchido(findFirst.get().getUrlAssinatura()) ? findFirst.get().getUrlAssinatura().contains("AssinarEletronicoFrame") ? findFirst.get().getUrlAssinatura().replace("AssinarEletronicoFrame", "AssinarEletronico") : findFirst.get().getUrlAssinatura(): "";
	}
	
	@XmlElement(name = "configuracaoGedOrigemVO")
	public ConfiguracaoGedOrigemVO getConfiguracaoGedOrigemVO() {		
		return configuracaoGedOrigemVO;
}

	public void setConfiguracaoGedOrigemVO(ConfiguracaoGedOrigemVO configuracaoGedOrigemVO) {
		this.configuracaoGedOrigemVO = configuracaoGedOrigemVO;
	}


	public Integer getOrdemAssinatura() {
		if(ordemAssinatura == null) {
			ordemAssinatura = 0;
		}
		return ordemAssinatura;
	}

	public void setOrdemAssinatura(Integer ordemAssinatura) {
		this.ordemAssinatura = ordemAssinatura;
	}

	public MatriculaPeriodoVO getMatriculaPeriodo() {
		if (matriculaPeriodo == null) {
			matriculaPeriodo = new MatriculaPeriodoVO();
		}
		return matriculaPeriodo;
	}

	public void setMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodo) {
		this.matriculaPeriodo = matriculaPeriodo;
	}

	public ProvedorDeAssinaturaEnum getProvedorDeAssinaturaEnum() {
		if (provedorDeAssinaturaEnum == null) {
			provedorDeAssinaturaEnum = ProvedorDeAssinaturaEnum.SEI;
		}
		return provedorDeAssinaturaEnum;
	}

	public void setProvedorDeAssinaturaEnum(ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum) {
		this.provedorDeAssinaturaEnum = provedorDeAssinaturaEnum;
	}
	
	public ProvedorDeAssinaturaEnum getProvedorAssinaturaVisaoAdm() {
		return provedorAssinaturaVisaoAdm;
	}

	public void setProvedorAssinaturaVisaoAdm(ProvedorDeAssinaturaEnum provedorAssinaturaTmp) {
		this.provedorAssinaturaVisaoAdm = provedorAssinaturaTmp;
	}

	public String getChaveProvedorDeAssinatura() {		
		return chaveProvedorDeAssinatura;
	}

	public void setChaveProvedorDeAssinatura(String chaveProvedorDeAssinatura) {
		this.chaveProvedorDeAssinatura = chaveProvedorDeAssinatura;
	}

	public String getUrlProvedorDeAssinatura() {		
		return urlProvedorDeAssinatura;
	}

	public void setUrlProvedorDeAssinatura(String urlProvedorDeAssinatura) {
		this.urlProvedorDeAssinatura = urlProvedorDeAssinatura;
	}

	public String getCodigoProvedorDeAssinatura() {
		return codigoProvedorDeAssinatura;
	}

	public void setCodigoProvedorDeAssinatura(String codigoProvedorDeAssinatura) {
		this.codigoProvedorDeAssinatura = codigoProvedorDeAssinatura;
	}

	public Boolean getDocumentoContrato() {
		if (documentoContrato == null) {
			documentoContrato = false;
		}
		return documentoContrato;
	}

	public void setDocumentoContrato(Boolean documentoContrato) {
		this.documentoContrato = documentoContrato;
	}

	public boolean isDocumentoAssinadoInvalido() {
		return documentoAssinadoInvalido;
	}

	public void setDocumentoAssinadoInvalido(boolean documentoAssinadoInvalido) {
		this.documentoAssinadoInvalido = documentoAssinadoInvalido;
	}

	public String getMotivoDocumentoAssinadoInvalido() {
		if (motivoDocumentoAssinadoInvalido == null) {
			motivoDocumentoAssinadoInvalido = "";
		}
		return motivoDocumentoAssinadoInvalido;
	}

	public void setMotivoDocumentoAssinadoInvalido(String motivoDocumentoAssinadoInvalido) {
		this.motivoDocumentoAssinadoInvalido = motivoDocumentoAssinadoInvalido;
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
	
	public ProgramacaoFormaturaVO getProgramacaoFormaturaVO() {
		if (programacaoFormaturaVO == null) {
			programacaoFormaturaVO = new ProgramacaoFormaturaVO();
		}
		return programacaoFormaturaVO;
	}
	
	public void setProgramacaoFormaturaVO(ProgramacaoFormaturaVO programacaoFormaturaVO) {
		this.programacaoFormaturaVO = programacaoFormaturaVO;
	}
	

	
	public Boolean getSelecionado() {
	if(selecionado == null) {
		selecionado =  false;
	}
	return selecionado;
	}
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	@XmlElement(name = "caminhoPreview")
	public String getCaminhoPreview() {
		if(caminhoPreview == null) {
			caminhoPreview = "";
		}
		return caminhoPreview;
	}

	public void setCaminhoPreview(String caminhoPreview) {
		this.caminhoPreview = caminhoPreview;
	}

	@XmlElement(name = "documentoAssinadoPessoaVO")
	public DocumentoAssinadoPessoaVO getDocumentoAssinadoPessoaVO() {		
		return documentoAssinadoPessoaVO;
	}

	public void setDocumentoAssinadoPessoaVO(DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO) {
		this.documentoAssinadoPessoaVO = documentoAssinadoPessoaVO;
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
	
	public Boolean getPermitirVisualizarDocumentoAluno() {
		return Uteis.isAtributoPreenchido(getMatricula().getAluno().getCPF()) && (
				getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO) 
				|| getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DECLARACAO) 
				|| getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO)
				|| getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO)
				|| getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO));  
	}	
	
	public Boolean getPermitirVisualizarDocumentoInstituicao() {
		return  Uteis.isAtributoPreenchido(getMatricula().getAluno().getCPF()) && (getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) 
				|| getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.ATA_RESULTADO_FINAL) 
				|| getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.PLANO_DE_ENSINO)
				|| getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU));				 								 
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
	
	public ArquivoVO getArquivoVisual() {
		if (arquivoVisual == null) {
			arquivoVisual = new ArquivoVO();
		}
		return arquivoVisual;
	}

	public void setArquivoVisual(ArquivoVO arquivoVisual) {
		this.arquivoVisual = arquivoVisual;
	}

	public String getCodigoValidacaoHistoricoDigital() {
		if (codigoValidacaoHistoricoDigital == null) {
			codigoValidacaoHistoricoDigital = Constantes.EMPTY;
		}
		return codigoValidacaoHistoricoDigital;
	}

	public void setCodigoValidacaoHistoricoDigital(String codigoValidacaoHistoricoDigital) {
		this.codigoValidacaoHistoricoDigital = codigoValidacaoHistoricoDigital;
	}

	public Boolean getDecisaoJudicial() {
		if (decisaoJudicial == null) {
			decisaoJudicial = Boolean.FALSE;
		}
		return decisaoJudicial;
	}

	public void setDecisaoJudicial(Boolean decisaoJudicial) {
		this.decisaoJudicial = decisaoJudicial;
	}

	public VersaoDiplomaDigitalEnum getVersaoDiploma() {
		return versaoDiploma;
	}

	public void setVersaoDiploma(VersaoDiplomaDigitalEnum versaoDiploma) {
		this.versaoDiploma = versaoDiploma;
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
	
	public String getCodigoValidacaoDiplomaDigital() {
		if (codigoValidacaoDiplomaDigital == null) {
			codigoValidacaoDiplomaDigital = "";
		}
		return codigoValidacaoDiplomaDigital;
	}

	public void setCodigoValidacaoDiplomaDigital(String codigoValidacaoDiplomaDigital) {
		this.codigoValidacaoDiplomaDigital = codigoValidacaoDiplomaDigital;
	}
	
	public String getCodigoValidacaoCurriculoEscolarDigital() {
		if (codigoValidacaoCurriculoEscolarDigital == null) {
			codigoValidacaoCurriculoEscolarDigital = Constantes.EMPTY;
		}
		return codigoValidacaoCurriculoEscolarDigital;
	}
	
	public void setCodigoValidacaoCurriculoEscolarDigital(String codigoValidacaoCurriculoEscolarDigital) {
		this.codigoValidacaoCurriculoEscolarDigital = codigoValidacaoCurriculoEscolarDigital;
	}
	
	public Date getDataAssinaturaSeiSignature() {
		return dataAssinaturaSeiSignature;
	}
	
	public void setDataAssinaturaSeiSignature(Date dataAssinaturaSeiSignature) {
		this.dataAssinaturaSeiSignature = dataAssinaturaSeiSignature;
	}
	
	public Integer getSituacaoDocumentoApresentar() {
		if (situacaoDocumentoApresentar == null) {
			if (Uteis.isAtributoPreenchido(getListaDocumentoAssinadoPessoa())) {
				long qtdTotal = getListaDocumentoAssinadoPessoa().stream().count();
				long qtdAssinado = getListaDocumentoAssinadoPessoa().stream().filter(d -> d.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO)).count();
				long qtdPendente = getListaDocumentoAssinadoPessoa().stream().filter(d -> d.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)).count();
				long qtdRejeitado = getListaDocumentoAssinadoPessoa().stream().filter(d -> d.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO)).count();
				if (!isDocumentoAssinadoInvalido()) {
					if (qtdAssinado == qtdTotal) {
						return 1;
					} else if (((qtdAssinado + qtdPendente) == qtdTotal) || (qtdPendente == qtdTotal)) {
						return 2;
					} else if (qtdRejeitado > 0) {
						return 3;
					}
				} else {
					return 3;
				}
			}
			return 0;
		}
		return situacaoDocumentoApresentar;
	}
	
	public List<String> getListaErroImportaAssinatura() {
		if (listaErroImportaAssinatura == null) {
			listaErroImportaAssinatura = new ArrayList<String>(0);
		}
		return listaErroImportaAssinatura;
	}
	
	public void setListaErroImportaAssinatura(List<String> listaErroImportaAssinatura) {
		this.listaErroImportaAssinatura = listaErroImportaAssinatura;
	}
	
	public String getIdDiplomaDigital() {
		if (idDiplomaDigital == null) {
			idDiplomaDigital = Constantes.EMPTY;
		}
		return idDiplomaDigital;
	}
	
	public void setIdDiplomaDigital(String idDiplomaDigital) {
		this.idDiplomaDigital = idDiplomaDigital;
	}
	
	public String getIdDadosRegistrosDiplomaDigital() {
		if (idDadosRegistrosDiplomaDigital == null) {
			idDadosRegistrosDiplomaDigital = Constantes.EMPTY;
		}
		return idDadosRegistrosDiplomaDigital;
	}
	
	public void setIdDadosRegistrosDiplomaDigital(String idDadosRegistrosDiplomaDigital) {
		this.idDadosRegistrosDiplomaDigital = idDadosRegistrosDiplomaDigital;
	}
	
	public String getTooltipVisualizarPDF() {
		return getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) || getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL) ? "Visualizar Representação Visual" : "Visualizar PDF";
	}
	
	public String getTooltipDownloadArquivo() {
		return getTipoOrigemDocumentoAssinadoEnum().isXmlMec() ? "Download XML" : "Download Arquivo";
	}
	
	public ConsistirException getConsistirException() {
		if (consistirException == null) {
			consistirException = new ConsistirException();
		}
		return consistirException;
	}
	
	public void setConsistirException(ConsistirException consistirException) {
		this.consistirException = consistirException;
	}
	
	public Boolean getErro() {
		if (erro == null) {
			erro = Boolean.FALSE;
		}
		return erro;
	}
	
	public void setErro(Boolean erro) {
		this.erro = erro;
	}
	
	public String getMotivoErro() {
		if (motivoErro == null) {
			motivoErro = Constantes.EMPTY;
		}
		return motivoErro;
	}
	
	public void setMotivoErro(String motivoErro) {
		this.motivoErro = motivoErro;
	}
	
	public DocumentoAssinadoPessoaVO getDocumentoAssinadoPessoaResposanvelAssinatura() {
		DocumentoAssinadoPessoaVO documentoAssinadoPessoa = new DocumentoAssinadoPessoaVO();
		if (Uteis.isAtributoPreenchido(getListaDocumentoAssinadoPessoa()) && getListaDocumentoAssinadoPessoa().stream().anyMatch(d -> d.getSituacaoDocumentoAssinadoPessoaEnum().isPendente())) {
			Optional<DocumentoAssinadoPessoaVO> optional = getListaDocumentoAssinadoPessoa().stream().filter(d -> d.getSituacaoDocumentoAssinadoPessoaEnum().isPendente()).min(Comparator.comparing(DocumentoAssinadoPessoaVO::getOrdemAssinatura));
			if (optional.isPresent()) {
				documentoAssinadoPessoa = optional.get();
			}
		}
		return documentoAssinadoPessoa;
	}
	
//	public boolean isCnpjAssinanteValido(Integer ordemAssinatura, CertificadoLacunaVO certificadoLacuna) {
//		if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO()) || !Uteis.isAtributoPreenchido(ordemAssinatura) || Objects.isNull(certificadoLacuna)) {
//			return Boolean.FALSE;
//		}
//		Uteis.validaCNPJ(getUnidadeEnsinoVO().getCNPJ());
//		Uteis.retirarMascaraCNPJ(getUnidadeEnsinoVO().getCNPJ());
//		return ordemAssinatura.equals(3) ? Uteis.validaCNPJ(getUnidadeEnsinoVO().getCNPJ()) && Uteis.retirarMascaraCNPJ(getUnidadeEnsinoVO().getCNPJ()).equals(Uteis.retirarMascaraCNPJ(certificadoLacuna.getPkiBrazil().getCnpj())) : Uteis.validaCNPJ(getUnidadeEnsinoVO().getCnpjUnidadeCertificadora()) && Uteis.retirarMascaraCNPJ(getUnidadeEnsinoVO().getCnpjUnidadeCertificadora()).equals(Uteis.retirarMascaraCNPJ(certificadoLacuna.getPkiBrazil().getCnpj()));
//	}
	
	public String obterTipoAssinanteOrdemAssinatura(Integer ordemAssinatura) {
		switch (ordemAssinatura) {
		case 1:
			return "IESRepresentantes";
		case 2:
			return "IESRepresentantes";
		case 3:
			return "IESEmissora";
		case 4:
			return "PessoasFisicas";
		case 5:
			return "IESRegistradora";
		default:
			return Constantes.EMPTY;
		}
	}
	
	public String obterTipoAssinanteOrdemAssinaturaDocumentacao(Integer ordemAssinatura) {
		switch (ordemAssinatura) {
		case 1:
			return "Representantes";
		case 3:
			return "IESEmissoraDadosDiploma";
		case 5:
			return "IESEmissoraRegistro";
		default:
			return Constantes.EMPTY;
		}
	}
	
	public String obterTipoAssinanteOrdemAssinaturaHistorico(Integer ordemAssinatura) {
		switch (ordemAssinatura) {
		case 1:
			return "IESEmissora";
		case 3:
			return "IESRegistradora";
		default:
			return Constantes.EMPTY;
		}
	}
	
	public String obterTipoAssinanteOrdemAssinaturaCurriculo(Integer ordemAssinatura) {
		switch (ordemAssinatura) {
		case 1:
			return "IESRepresentantes";
		case 3:
			return "IESEmissora";
		default:
			return Constantes.EMPTY;
		}
	}

	public Boolean getGerarNovoTermoESolicitarNovasAssinaturas() {
		if (isGerarNovoTermoESolicitarNovasAssinaturas == null) {
			isGerarNovoTermoESolicitarNovasAssinaturas = Boolean.FALSE;
		}
		return isGerarNovoTermoESolicitarNovasAssinaturas;
	}

	public void setGerarNovoTermoESolicitarNovasAssinaturas(Boolean gerarNovoTermoESolicitarNovasAssinaturas) {
		isGerarNovoTermoESolicitarNovasAssinaturas = gerarNovoTermoESolicitarNovasAssinaturas;
	}

	public String getNomeGradeCurricularEstagio() {
		if (nomeGradeCurricularEstagio == null) {
			nomeGradeCurricularEstagio = "";
		}
		return nomeGradeCurricularEstagio;
	}

	public void setNomeGradeCurricularEstagio(String nomeGradeCurricularEstagio) {
		this.nomeGradeCurricularEstagio = nomeGradeCurricularEstagio;
	}

	public String getResponsavelConcedente() {
		if (responsavelConcedente == null) {
			responsavelConcedente = "";
		}
		return responsavelConcedente;
	}

	public void setResponsavelConcedente(String responsavelConcedente) {
		this.responsavelConcedente = responsavelConcedente;
	}

	public String getConcedenteDocumentoEstagio() {
		if (concedenteDocumentoEstagio == null) {
			concedenteDocumentoEstagio = "";
		}
		return concedenteDocumentoEstagio;
	}

	public void setConcedenteDocumentoEstagio(String concedenteDocumentoEstagio) {
		this.concedenteDocumentoEstagio = concedenteDocumentoEstagio;
	}

	public String getTituloColacaoGrau() {
		if (tituloColacaoGrau == null) {
			tituloColacaoGrau = "";
		}
		return tituloColacaoGrau;
	}

	public void setTituloColacaoGrau(String tituloColacaoGrau) {
		this.tituloColacaoGrau = tituloColacaoGrau;
	}

	public String getInfoAtaColacaoGrau() {
		if (infoAtaColacaoGrau == null) {
			infoAtaColacaoGrau = "";
		}
		return infoAtaColacaoGrau;
	}

	public void setInfoAtaColacaoGrau(String infoAtaColacaoGrau) {
		this.infoAtaColacaoGrau = infoAtaColacaoGrau;
	}

	public OffsetDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(OffsetDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getDataColacaoGrau() {
		return dataColacaoGrau;
	}

	public void setDataColacaoGrau(Date dataColacaoGrau) {
		this.dataColacaoGrau = dataColacaoGrau;
	}

	public Boolean getAtaColacaoGrau() {
		if (isAtaColacaoGrau == null) {
			isAtaColacaoGrau = Boolean.FALSE;
		}
		return isAtaColacaoGrau;
	}

	public void setAtaColacaoGrau(Boolean ataColacaoGrau) {
		isAtaColacaoGrau = ataColacaoGrau;
	}
	
	public boolean isDocumentoAssinadoTechsert() {
		return Uteis.isAtributoPreenchido(getCodigo()) && Uteis.isAtributoPreenchido(getProvedorDeAssinaturaEnum()) && getProvedorDeAssinaturaEnum().equals(ProvedorDeAssinaturaEnum.TECHCERT);
	}
	
	public boolean isDocumentoAssinadoCertisign() {
		return Uteis.isAtributoPreenchido(getCodigo()) && Uteis.isAtributoPreenchido(getProvedorDeAssinaturaEnum()) && getProvedorDeAssinaturaEnum().equals(ProvedorDeAssinaturaEnum.CERTISIGN);
	}
	
	public boolean isDocumentoAssinadoSei() {
		return Uteis.isAtributoPreenchido(getCodigo()) && Uteis.isAtributoPreenchido(getProvedorDeAssinaturaEnum()) && getProvedorDeAssinaturaEnum().equals(ProvedorDeAssinaturaEnum.SEI);
	}
}
