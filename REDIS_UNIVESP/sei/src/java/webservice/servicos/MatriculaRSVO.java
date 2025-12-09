package webservice.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import webservice.servicos.objetos.DisciplinaRSVO;
import webservice.servicos.objetos.enumeradores.TagsTextoFinalizacaoMatriculaEnum;

@XmlRootElement(name = "matricula")
public class MatriculaRSVO {

	private String matricula;
	private CursoObject cursoObject;
	private UnidadeEnsinoRSVO unidadeEnsinoRSVO;
	private ProcessoMatriculaRSVO processoMatriculaRSVO;
	private CondicaoPagamentoRSVO condicaoPagamentoRSVO;
	private TurmaRSVO turmaRSVO;
	private PeriodoLetivoRSVO periodoLetivoRSVO;
	private PessoaObject pessoaObject;
	private TurnoRSVO turnoRSVO;
	private List<CursoObject> cursoObjects;
	private List<UnidadeEnsinoRSVO> unidadeEnsinoRSVOs;
	private List<ProcessoMatriculaRSVO> processoMatriculaRSVOs;
	private List<CondicaoPagamentoRSVO> condicaoPagamentoRSVOs;
	private List<TurmaRSVO> turmaRSVOs;
	private List<TurnoRSVO> turnoRSVOs;
	private boolean possuiMaisDeUmaUnidadeEnsino = false;
	private boolean possuiMaisDeUmTurno = false;
	private boolean possuiMaisDeUmProcessoMatricula = false;
	private boolean possuiMaisDeUmaTurma = false;
	private boolean possuiMaisDeUmaCondicaoDePagamento = false;
	private Integer codigoMatriculaPeriodo;
	private Integer codigoBanner;
	private Boolean matriculaRealizadaComSucesso;
	private String linkDownloadComprovante;
	private Integer codigoUnidadeEnsino;
	private Integer codigoTurno;
	private Integer codigoProcessoMatricula;
	private Integer codigoTurma;
	private Integer codigoCondicaoPagamento;
	private String ano;
	private String semestre;
	private String mensagem;
	private String linkDownloadContrato;
	private String motivoRecusa;
	private Boolean assinarDigitalmenteContrato;
	private DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO;
	private Boolean bolsasAuxilios;
	private Boolean autodeclaracaoPretoPardoIndigena;
	private Boolean normasMatricula;
	private String linkNormasMatricula;
	private String textoFinalizacaoMatriculaOnline;
	private Integer codigoInscricao;
	private String formaIngresso;
	private Boolean existeMatriculaPendenteDocumento;
	private Boolean existeMatriculaAptaPendenteAssinaturaContrato;
	private Boolean escolaPublica;
	private String textoDeclaracaoPPI;
	private String textoDeclaracaoEscolaridadePublica;
	private String textoConfirmacaoNovaMatricula;
	private String textoDeclaracaoBolsasAuxilios;  
	private Boolean ativarPreMatriculaAposEntregaDocumentosObrigatorios;
    private Boolean permitirAssinarContratoPendenciaDocumentacao;
    private List<DocumetacaoMatriculaVO> documentacaoMatriculaVOs;
    private String  codigoAutenticacaoNavegador;
    private String   navegadorAcesso ;
    private Boolean  permiteAcessarNavegador;
    private Boolean  informarDadosBancarios;
    private String   bancoAluno;
    private String   agenciaAluno;
    private String   contaCorrenteAluno;
    private Boolean  possuiMatriculaAtivaOuPreOutroCurso;
	private String textoOrientacaoCancelamentoPorOutraMatricula;	
	private Boolean confirmouCancelamentoMatriculaAtivaOuPreOutroCurso;
	private Boolean permiteAlunoIncluirExcluirDisciplina;
	private List<DisciplinaRSVO> disciplinasMatricula;
	private Boolean validarAlunoJaMatriculado ;
	
    private String mensagemErroContrato;
	
	@XmlElement(name = "matricula")
	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	@XmlElement(name = "curso")
	public CursoObject getCursoObject() {
		if (cursoObject == null) {
			cursoObject = new CursoObject();
		}
		return cursoObject;
	}

	public void setCursoObject(CursoObject cursoObject) {
		this.cursoObject = cursoObject;
	}

	@XmlElement(name = "unidadeEnsino")
	public UnidadeEnsinoRSVO getUnidadeEnsinoRSVO() {
		if (unidadeEnsinoRSVO == null) {
			unidadeEnsinoRSVO = new UnidadeEnsinoRSVO();
		}
		return unidadeEnsinoRSVO;
	}

	public void setUnidadeEnsinoRSVO(UnidadeEnsinoRSVO unidadeEnsinoRSVO) {
		this.unidadeEnsinoRSVO = unidadeEnsinoRSVO;
	}

	@XmlElement(name = "processoMatricula")
	public ProcessoMatriculaRSVO getProcessoMatriculaRSVO() {
		if (processoMatriculaRSVO == null) {
			processoMatriculaRSVO = new ProcessoMatriculaRSVO();
		}
		return processoMatriculaRSVO;
	}

	public void setProcessoMatriculaRSVO(ProcessoMatriculaRSVO processoMatriculaRSVO) {
		this.processoMatriculaRSVO = processoMatriculaRSVO;
	}

	@XmlElement(name = "condicaoPagamento")
	public CondicaoPagamentoRSVO getCondicaoPagamentoRSVO() {
		if (condicaoPagamentoRSVO == null) {
			condicaoPagamentoRSVO = new CondicaoPagamentoRSVO();
		}
		return condicaoPagamentoRSVO;
	}

	public void setCondicaoPagamentoRSVO(CondicaoPagamentoRSVO condicaoPagamentoRSVO) {
		this.condicaoPagamentoRSVO = condicaoPagamentoRSVO;
	}

	@XmlElement(name = "turma")
	public TurmaRSVO getTurmaRSVO() {
		if (turmaRSVO == null) {
			turmaRSVO = new TurmaRSVO();
		}
		return turmaRSVO;
	}

	public void setTurmaRSVO(TurmaRSVO turmaRSVO) {
		this.turmaRSVO = turmaRSVO;
	}

	@XmlElement(name = "cursos")
	public List<CursoObject> getCursoObjects() {
		if (cursoObjects == null) {
			cursoObjects = new ArrayList<CursoObject>();
		}
		return cursoObjects;
	}

	public void setCursoObjects(List<CursoObject> cursoObjects) {
		this.cursoObjects = cursoObjects;
	}

	@XmlElement(name = "unidadeEnsinos")
	public List<UnidadeEnsinoRSVO> getUnidadeEnsinoRSVOs() {
		if (unidadeEnsinoRSVOs == null) {
			unidadeEnsinoRSVOs = new ArrayList<UnidadeEnsinoRSVO>();
		}
		return unidadeEnsinoRSVOs;
	}

	public void setUnidadeEnsinoRSVOs(List<UnidadeEnsinoRSVO> unidadeEnsinoRSVOs) {
		this.unidadeEnsinoRSVOs = unidadeEnsinoRSVOs;
	}

	@XmlElement(name = "processoMatriculas")
	public List<ProcessoMatriculaRSVO> getProcessoMatriculaRSVOs() {
		if (processoMatriculaRSVOs == null) {
			processoMatriculaRSVOs = new ArrayList<ProcessoMatriculaRSVO>();
		}
		return processoMatriculaRSVOs;
	}

	public void setProcessoMatriculaRSVOs(List<ProcessoMatriculaRSVO> processoMatriculaRSVOs) {
		this.processoMatriculaRSVOs = processoMatriculaRSVOs;
	}

	@XmlElement(name = "condicaoPagamentos")
	public List<CondicaoPagamentoRSVO> getCondicaoPagamentoRSVOs() {
		if (condicaoPagamentoRSVOs == null) {
			condicaoPagamentoRSVOs = new ArrayList<CondicaoPagamentoRSVO>();
		}
		return condicaoPagamentoRSVOs;
	}

	public void setCondicaoPagamentoRSVOs(List<CondicaoPagamentoRSVO> condicaoPagamentoRSVOs) {
		this.condicaoPagamentoRSVOs = condicaoPagamentoRSVOs;
	}

	@XmlElement(name = "turmas")
	public List<TurmaRSVO> getTurmaRSVOs() {
		if (turmaRSVOs == null) {
			turmaRSVOs = new ArrayList<TurmaRSVO>();
		}
		return turmaRSVOs;
	}

	public void setTurmaRSVOs(List<TurmaRSVO> turmaRSVOs) {
		this.turmaRSVOs = turmaRSVOs;
	}

	@XmlElement(name = "pessoa")
	public PessoaObject getPessoaObject() {
		if (pessoaObject == null) {
			pessoaObject = new PessoaObject();
		}
		return pessoaObject;
	}

	public void setPessoaObject(PessoaObject pessoaObject) {
		this.pessoaObject = pessoaObject;
	}

	@XmlElement(name = "periodoLetivo")
	public PeriodoLetivoRSVO getPeriodoLetivoRSVO() {
		if (periodoLetivoRSVO == null) {
			periodoLetivoRSVO = new PeriodoLetivoRSVO();
		}
		return periodoLetivoRSVO;
	}

	public void setPeriodoLetivoRSVO(PeriodoLetivoRSVO periodoLetivoRSVO) {
		this.periodoLetivoRSVO = periodoLetivoRSVO;
	}

	@XmlElement(name = "turno")
	public TurnoRSVO getTurnoRSVO() {
		if(turnoRSVO == null) {
			turnoRSVO = new TurnoRSVO();
		}
		return turnoRSVO;
	}

	public void setTurnoRSVO(TurnoRSVO turnoRSVO) {
		this.turnoRSVO = turnoRSVO;
	}

	@XmlElement(name = "turnos")
	public List<TurnoRSVO> getTurnoRSVOs() {
		if(turnoRSVOs == null) {
			turnoRSVOs = new ArrayList<TurnoRSVO>();
		}
		return turnoRSVOs;
	}

	public void setTurnoRSVOs(List<TurnoRSVO> turnoRSVOs) {
		this.turnoRSVOs = turnoRSVOs;
	}
	
	@XmlElement(name = "possuiMaisDeUmaUnidadeEnsino", required = false, defaultValue = "false")
	public boolean getPossuiMaisDeUmaUnidadeEnsino() {
		return possuiMaisDeUmaUnidadeEnsino = getUnidadeEnsinoRSVOs().size() > 1;
	}
	
	@XmlElement(name = "possuiMaisDeUmTurno", required = false, defaultValue = "false")
	public boolean getPossuiMaisDeUmTurno() {
		return possuiMaisDeUmTurno = getTurnoRSVOs().size() > 1;
	}

	@XmlElement(name = "possuiMaisDeUmProcessoMatricula", required = false, defaultValue = "false")
	public boolean getPossuiMaisDeUmProcessoMatricula() {
		return possuiMaisDeUmProcessoMatricula = getProcessoMatriculaRSVOs().size() > 1;
	}
	
	@XmlElement(name = "possuiMaisDeUmaTurma", required = false, defaultValue = "false")
	public boolean getPossuiMaisDeUmaTurma() {
		return possuiMaisDeUmaTurma = getTurmaRSVOs().size() > 1;
	}
	
	@XmlElement(name = "possuiMaisDeUmaCondicaoDePagamento", required = false, defaultValue = "false")
	public boolean getPossuiMaisDeUmaCondicaoDePagamento() {
		return possuiMaisDeUmaCondicaoDePagamento = getCondicaoPagamentoRSVOs().size() > 1;
	}

	@XmlElement(name = "codigoMatriculaPeriodo")
	public Integer getCodigoMatriculaPeriodo() {
		if(codigoMatriculaPeriodo == null) {
			codigoMatriculaPeriodo = 0;
		}
		return codigoMatriculaPeriodo;
	}

	public void setCodigoMatriculaPeriodo(Integer codigoMatriculaPeriodo) {
		this.codigoMatriculaPeriodo = codigoMatriculaPeriodo;
	}

	@XmlElement(name = "codigoBanner")
	public Integer getCodigoBanner() {
		if(codigoBanner == null) {
			codigoBanner = 0;
		}
		return codigoBanner;
	}

	public void setCodigoBanner(Integer codigoBanner) {
		this.codigoBanner = codigoBanner;
	}

	@XmlElement(name = "matriculaRealizadaComSucesso", required = false, defaultValue = "false")
	public Boolean getMatriculaRealizadaComSucesso() {
		if(matriculaRealizadaComSucesso == null) {
			matriculaRealizadaComSucesso = false;
		}
		return matriculaRealizadaComSucesso;
	}

	public void setMatriculaRealizadaComSucesso(Boolean matriculaRealizadaComSucesso) {
		this.matriculaRealizadaComSucesso = matriculaRealizadaComSucesso;
	}

	@XmlElement(name = "linkDownloadComprovante")
	public String getLinkDownloadComprovante() {
		if(linkDownloadComprovante == null) {
			linkDownloadComprovante = "";
		}
		return linkDownloadComprovante;
	}

	public void setLinkDownloadComprovante(String linkDownloadComprovante) {
		this.linkDownloadComprovante = linkDownloadComprovante;
	}

	@XmlElement(name = "codigoUnidadeEnsino")
	public Integer getCodigoUnidadeEnsino() {
		if(codigoUnidadeEnsino == null) {
			codigoUnidadeEnsino = 0;
		}
		return codigoUnidadeEnsino;
	}

	public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
		this.codigoUnidadeEnsino = codigoUnidadeEnsino;
	}

	@XmlElement(name = "codigoTurno")
	public Integer getCodigoTurno() {
		if(codigoTurno == null) {
			codigoTurno = 0;
		}
		return codigoTurno;
	}

	public void setCodigoTurno(Integer codigoTurno) {
		this.codigoTurno = codigoTurno;
	}

	@XmlElement(name = "codigoProcessoMatricula")
	public Integer getCodigoProcessoMatricula() {
		if(codigoProcessoMatricula == null) {
			codigoProcessoMatricula = 0;
		}
		return codigoProcessoMatricula;
	}

	public void setCodigoProcessoMatricula(Integer codigoProcessoMatricula) {
		this.codigoProcessoMatricula = codigoProcessoMatricula;
	}

	@XmlElement(name = "codigoTurma")
	public Integer getCodigoTurma() {
		if(codigoTurma == null) {
			codigoTurma = 0;
		}
		return codigoTurma;
	}

	public void setCodigoTurma(Integer codigoTurma) {
		this.codigoTurma = codigoTurma;
	}

	@XmlElement(name = "codigoCondicaoPagamento")
	public Integer getCodigoCondicaoPagamento() {
		if(codigoCondicaoPagamento == null) {
			codigoCondicaoPagamento = 0;
		}
		return codigoCondicaoPagamento;
	}

	public void setCodigoCondicaoPagamento(Integer codigoCondicaoPagamento) {
		this.codigoCondicaoPagamento = codigoCondicaoPagamento;
	}

	@XmlElement(name = "ano")
	public String getAno() {
		if(ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	@XmlElement(name = "semestre")
	public String getSemestre() {
		if(semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	

	@XmlElement(name = "mensagem")
	public String getMensagem() {
		if(mensagem == null){
			mensagem ="";
		}
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	@XmlElement(name = "linkDownloadContrato")
	public String getLinkDownloadContrato() {
		if(linkDownloadContrato == null){
			linkDownloadContrato ="";
		}
		return linkDownloadContrato;
	}

	public void setLinkDownloadContrato(String linkDownloadContrato) {
		this.linkDownloadContrato = linkDownloadContrato;
	}
	
	@XmlElement(name = "motivoRecusa")
	public String getMotivoRecusa() {
		if (motivoRecusa == null) {
			motivoRecusa = "";
		}
		return motivoRecusa;
	}

	public void setMotivoRecusa(String motivoRecusa) {
		this.motivoRecusa = motivoRecusa;
	}
	
	@XmlElement(name = "assinarDigitalmenteContrato")
	public Boolean getAssinarDigitalmenteContrato() {
		if (assinarDigitalmenteContrato == null) {
			assinarDigitalmenteContrato = false;
		}
		return assinarDigitalmenteContrato;
	}

	public void setAssinarDigitalmenteContrato(Boolean assinarDigitalmenteContrato) {
		this.assinarDigitalmenteContrato = assinarDigitalmenteContrato;
	}
	
	@XmlElement(name = "documentoAssinadoPessoa")
	public DocumentoAssinadoPessoaVO getDocumentoAssinadoPessoaVO() {
		if (documentoAssinadoPessoaVO == null) {
			documentoAssinadoPessoaVO = new DocumentoAssinadoPessoaVO();
		}
		return documentoAssinadoPessoaVO;
	}

	public void setDocumentoAssinadoPessoaVO(DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO) {
		this.documentoAssinadoPessoaVO = documentoAssinadoPessoaVO;
	}
	
	@XmlElement(name = "bolsasAuxilios")
	public Boolean getBolsasAuxilios() {
		if (bolsasAuxilios == null) {
			bolsasAuxilios = false;
		}
		return bolsasAuxilios;
	}

	public void setBolsasAuxilios(Boolean bolsasAuxilios) {
		this.bolsasAuxilios = bolsasAuxilios;
	}
	@XmlElement(name = "autodeclaracaoPretoPardoIndigena")
	public Boolean getAutodeclaracaoPretoPardoIndigena() {
		if (autodeclaracaoPretoPardoIndigena == null) {
			autodeclaracaoPretoPardoIndigena = false;
		}
		return autodeclaracaoPretoPardoIndigena;
	}

	public void setAutodeclaracaoPretoPardoIndigena(Boolean autodeclaracaoPretoPardoIndigena) {
		this.autodeclaracaoPretoPardoIndigena = autodeclaracaoPretoPardoIndigena;
	}
	@XmlElement(name = "normasMatricula")
	public Boolean getNormasMatricula() {
		if (normasMatricula == null) {
			normasMatricula = false;
		}
		return normasMatricula;
	}

	public void setNormasMatricula(Boolean normasMatricula) {
		this.normasMatricula = normasMatricula;
	}
	
	@XmlElement(name = "linkNormasMatricula")
	public String getLinkNormasMatricula() {
		if (linkNormasMatricula == null) {
			linkNormasMatricula = "";
		}
		return linkNormasMatricula;
	}

	public void setLinkNormasMatricula(String linkNormasMatricula) {
		this.linkNormasMatricula = linkNormasMatricula;
	}
	
	@XmlElement(name = "textoFinalizacaoMatriculaOnline")
	public String getTextoFinalizacaoMatriculaOnline() {
		if (textoFinalizacaoMatriculaOnline == null) {
			textoFinalizacaoMatriculaOnline = "";
		}
		return textoFinalizacaoMatriculaOnline;
	}

	public void setTextoFinalizacaoMatriculaOnline(String textoFinalizacaoMatriculaOnline) {
		this.textoFinalizacaoMatriculaOnline = textoFinalizacaoMatriculaOnline;
	}
	
	@XmlElement(name = "codigoInscricao")
	public Integer getCodigoInscricao() {
		return codigoInscricao;
	}

	public void setCodigoInscricao(Integer codigoInscricao) {
		this.codigoInscricao = codigoInscricao;
	}
	
	@XmlElement(name = "formaIngresso")
	public String getFormaIngresso() {
		if (formaIngresso == null) {
			formaIngresso = "";
		}
		return formaIngresso;
	}

	public void setFormaIngresso(String formaIngresso) {
		this.formaIngresso = formaIngresso;
	}
	
	
	@XmlElement(name = "existeMatriculaPendenteDocumento")
	public Boolean getExisteMatriculaPendenteDocumento() {
		if(existeMatriculaPendenteDocumento == null) {
			existeMatriculaPendenteDocumento = false ;
		}
		return existeMatriculaPendenteDocumento;
	}

	public void setExisteMatriculaPendenteDocumento(Boolean existeMatriculaPendenteDocumento) {
		this.existeMatriculaPendenteDocumento = existeMatriculaPendenteDocumento;
	}


	
	@XmlElement(name = "existeMatriculaAptaPendenteAssinaturaContrato")
	public Boolean getExisteMatriculaAptaPendenteAssinaturaContrato() {
		if(existeMatriculaAptaPendenteAssinaturaContrato == null ) {
			existeMatriculaAptaPendenteAssinaturaContrato = Boolean.FALSE;
		}
		return existeMatriculaAptaPendenteAssinaturaContrato;
	}

	public void setExisteMatriculaAptaPendenteAssinaturaContrato(
			Boolean existeMatriculaAptaPendenteAssinaturaContrato) {
		this.existeMatriculaAptaPendenteAssinaturaContrato = existeMatriculaAptaPendenteAssinaturaContrato;
	}


	@XmlElement(name = "escolaPublica")
	public Boolean getEscolaPublica() {
		if (escolaPublica == null) {
			escolaPublica = false;
		}
		return escolaPublica;
	}

	public void setEscolaPublica(Boolean escolaPublica) {
		this.escolaPublica = escolaPublica;
	}
	
	
	@XmlElement(name = "textoDeclaracaoPPI")
	public String getTextoDeclaracaoPPI() {
		if(textoDeclaracaoPPI == null ) {
			textoDeclaracaoPPI = "";
		}
		return textoDeclaracaoPPI;
	}

	public void setTextoDeclaracaoPPI(String textoDeclaracaoPPI) {
		this.textoDeclaracaoPPI = textoDeclaracaoPPI;
	}

	
	@XmlElement(name = "textoDeclaracaoEscolaridadePublica")
	public String getTextoDeclaracaoEscolaridadePublica() {
		if(textoDeclaracaoEscolaridadePublica == null ) {
			textoDeclaracaoEscolaridadePublica ="";
		}
		return textoDeclaracaoEscolaridadePublica;
	}

	public void setTextoDeclaracaoEscolaridadePublica(
			String textoDeclaracaoEscolaridadePublica) {
		this.textoDeclaracaoEscolaridadePublica = textoDeclaracaoEscolaridadePublica;
	}
	
	
	@XmlElement(name = "textoDeclaracaoBolsasAuxilios")
	public String getTextoDeclaracaoBolsasAuxilios() {
		if(textoDeclaracaoBolsasAuxilios == null ) {
			textoDeclaracaoBolsasAuxilios ="";
		}
		return textoDeclaracaoBolsasAuxilios;
	}

	public void setTextoDeclaracaoBolsasAuxilios(
			String textoDeclaracaoBolsasAuxilios) {
		this.textoDeclaracaoBolsasAuxilios = textoDeclaracaoBolsasAuxilios;
	}

	@XmlElement(name = "textoConfirmacaoNovaMatricula")
	public String getTextoConfirmacaoNovaMatricula() {
		if(textoConfirmacaoNovaMatricula == null ) {
			textoConfirmacaoNovaMatricula =""; 
		}
		return textoConfirmacaoNovaMatricula;
	}

	public void setTextoConfirmacaoNovaMatricula(String textoConfirmacaoNovaMatricula) {
		this.textoConfirmacaoNovaMatricula = textoConfirmacaoNovaMatricula;
	}
	
	
	@XmlElement(name = "permitirAssinarContratoPendenciaDocumentacao")
	public Boolean getPermitirAssinarContratoPendenciaDocumentacao() {
		if(permitirAssinarContratoPendenciaDocumentacao == null ) {
			permitirAssinarContratoPendenciaDocumentacao = Boolean.FALSE;
		}
		return permitirAssinarContratoPendenciaDocumentacao;
	}

	public void setPermitirAssinarContratoPendenciaDocumentacao(Boolean permitirAssinarContratoPendenciaDocumentacao) {
		this.permitirAssinarContratoPendenciaDocumentacao = permitirAssinarContratoPendenciaDocumentacao;
	}
	

	@XmlElement(name = "ativarPreMatriculaAposEntregaDocumentosObrigatorios")
    public Boolean getAtivarPreMatriculaAposEntregaDocumentosObrigatorios() {
    	if(ativarPreMatriculaAposEntregaDocumentosObrigatorios == null) {
    		ativarPreMatriculaAposEntregaDocumentosObrigatorios = Boolean.FALSE;
    	}
		return ativarPreMatriculaAposEntregaDocumentosObrigatorios;
	}

	public void setAtivarPreMatriculaAposEntregaDocumentosObrigatorios(
			Boolean ativarPreMatriculaAposEntregaDocumentosObrigatorios) {
		this.ativarPreMatriculaAposEntregaDocumentosObrigatorios = ativarPreMatriculaAposEntregaDocumentosObrigatorios;
	}
	
	
	@XmlElement(name = "documentacaoMatriculaVOs")
	public List<DocumetacaoMatriculaVO> getDocumentacaoMatriculaVOs() {
		if(documentacaoMatriculaVOs == null) {
			documentacaoMatriculaVOs = new ArrayList<DocumetacaoMatriculaVO>(0);
		}
		return documentacaoMatriculaVOs;
	}

	public void setDocumentacaoMatriculaVOs(List<DocumetacaoMatriculaVO> documentacaoMatriculaVOs) {
		this.documentacaoMatriculaVOs = documentacaoMatriculaVOs;
	}
	
	
     public void realizarValidacaoEcriacaoTextoDeclaracaoPertinentesMatriculaOnline(MatriculaRSVO matriculaRSVO, CursoVO cursoVO) { 
    	 
    	    //flag utodeclaracaoPretoPardoIndigena 
    	    if(matriculaRSVO.getAutodeclaracaoPretoPardoIndigena()  &&  Uteis.isAtributoPreenchido(cursoVO.getTextoDeclaracaoPPI())) {
		    	matriculaRSVO.setTextoDeclaracaoPPI(matriculaRSVO.realizarSubstituicaoTagsMatriculaExterna(cursoVO.getTextoDeclaracaoPPI() ));	
		    }
    	    
    	    // flag  sobre bolsas auxilios
    	    if(Uteis.isAtributoPreenchido(cursoVO.getTextoDeclaracaoBolsasAuxilios())) {
		    	matriculaRSVO.setTextoDeclaracaoBolsasAuxilios(matriculaRSVO.realizarSubstituicaoTagsMatriculaExterna(cursoVO.getTextoDeclaracaoBolsasAuxilios()));
		    }
    	 
    	    // flag ecolaridade publica 
    	    if(matriculaRSVO.getEscolaPublica() &&   Uteis.isAtributoPreenchido(cursoVO.getTextoDeclaracaoEscolaridadePublica())) {
		    	matriculaRSVO.setTextoDeclaracaoEscolaridadePublica(matriculaRSVO.realizarSubstituicaoTagsMatriculaExterna(cursoVO.getTextoDeclaracaoEscolaridadePublica()));	
		    }    	    
    	    
    	    
    	    if(Uteis.isAtributoPreenchido(cursoVO.getTextoConfirmacaoNovaMatricula())) {
		    	matriculaRSVO.setTextoConfirmacaoNovaMatricula(matriculaRSVO.realizarSubstituicaoTagsMatriculaExterna(cursoVO.getTextoConfirmacaoNovaMatricula()));	
		    }
    	 
    	    matriculaRSVO.setLinkNormasMatricula(cursoVO.getUrlDeclaracaoNormasMatricula());	
	}
	
     
     public String realizarSubstituicaoTagsMatriculaExterna(String Mensagem ) {
    	 return MatriculaRSVO.realizarSubstituicaoTagsMatriculaExterna(Mensagem, getPessoaObject().getNome(), getPessoaObject().getRg(), getPessoaObject().getCpf() );
 	}

     public static String realizarSubstituicaoTagsMatriculaExterna(String Mensagem, String nome, String rg, String cpf ) {
 		String mensagemTexto = Mensagem;
 		mensagemTexto = mensagemTexto.replaceAll(TagsTextoFinalizacaoMatriculaEnum.NOME_ALUNO.name(), nome);
 		mensagemTexto = mensagemTexto.replaceAll(TagsTextoFinalizacaoMatriculaEnum.RG_ALUNO.getName(), rg);
 		mensagemTexto = mensagemTexto.replaceAll(TagsTextoFinalizacaoMatriculaEnum.CPF_ALUNO.getName(), cpf);		
 		return mensagemTexto;
 	}


     @XmlElement(name = "codigoAutenticacaoNavegador")
 	public String getCodigoAutenticacaoNavegador() {
 		if(codigoAutenticacaoNavegador == null) {
 			codigoAutenticacaoNavegador ="";
 		}
 		return codigoAutenticacaoNavegador;
 	}

 	public void setCodigoAutenticacaoNavegador(String codigoAutenticacaoNavegador) {
 		this.codigoAutenticacaoNavegador = codigoAutenticacaoNavegador;
 	}
 	
 	
 	@XmlElement(name = "permiteAcessarNavegador")
	public Boolean getPermiteAcessarNavegador() {
		if(permiteAcessarNavegador == null) {
			permiteAcessarNavegador = Boolean.FALSE; 
		} 
		return permiteAcessarNavegador;
	}

	public void setPermiteAcessarNavegador(Boolean permiteAcessarNavegador) {
		this.permiteAcessarNavegador = permiteAcessarNavegador;
	}

	@XmlElement(name = "navegadorAcesso")
	public String getNavegadorAcesso() {
		if(navegadorAcesso == null) {
			navegadorAcesso = "";
		}
		return navegadorAcesso;
	}

	public void setNavegadorAcesso(String navegadorAcesso) {
		this.navegadorAcesso = navegadorAcesso;
	}
	
	
	@XmlElement(name = "informarDadosBancarios")
	public Boolean getInformarDadosBancarios() {
		if (informarDadosBancarios == null) {
			informarDadosBancarios = false;
		}
		return informarDadosBancarios;
	}
	
	public void setInformarDadosBancarios(Boolean informarDadosBancarios) {
		this.informarDadosBancarios = informarDadosBancarios;
	}

	@XmlElement(name = "bancoAluno")
	public String getBancoAluno() {
		if(bancoAluno == null ) {
			bancoAluno = "";
		}
		return bancoAluno;
	}

	public void setBancoAluno(String bancoAluno) {
		this.bancoAluno = bancoAluno;
	}

	@XmlElement(name = "agenciaAluno")
	public String getAgenciaAluno() {
		if(agenciaAluno == null ) {
			agenciaAluno = "";
		}
		return agenciaAluno;
	}

	public void setAgenciaAluno(String agenciaAluno) {
		this.agenciaAluno = agenciaAluno;
	}

	@XmlElement(name = "contaCorrenteAluno")
	public String getContaCorrenteAluno() {
		if(contaCorrenteAluno == null ) {
			contaCorrenteAluno = "";
		}
		return contaCorrenteAluno;
	}

	public void setContaCorrenteAluno(String contaCorrenteAluno) {
		this.contaCorrenteAluno = contaCorrenteAluno;
	}
	
	@XmlElement(name = "textoOrientacaoCancelamentoPorOutraMatricula")
    public String getTextoOrientacaoCancelamentoPorOutraMatricula() {
	   if(textoOrientacaoCancelamentoPorOutraMatricula == null) {
		   textoOrientacaoCancelamentoPorOutraMatricula = "";
	   }
		return textoOrientacaoCancelamentoPorOutraMatricula;
	}

	public void setTextoOrientacaoCancelamentoPorOutraMatricula(String textoOrientacaoCancelamentoPorOutraMatricula) {
		this.textoOrientacaoCancelamentoPorOutraMatricula = textoOrientacaoCancelamentoPorOutraMatricula;
	}

	

	@XmlElement(name = "possuiMatriculaAtivaOuPreOutroCurso")
	public Boolean getPossuiMatriculaAtivaOuPreOutroCurso() {
		if(possuiMatriculaAtivaOuPreOutroCurso == null ) {
			possuiMatriculaAtivaOuPreOutroCurso = Boolean.FALSE;
		}
		return possuiMatriculaAtivaOuPreOutroCurso;
	}

	public void setPossuiMatriculaAtivaOuPreOutroCurso(
			Boolean possuiMatriculaAtivaOuPreOutroCurso) {
		this.possuiMatriculaAtivaOuPreOutroCurso = possuiMatriculaAtivaOuPreOutroCurso;
	}

	@XmlElement(name = "confirmouCancelamentoMatriculaAtivaOuPreOutroCurso")
	public Boolean getConfirmouCancelamentoMatriculaAtivaOuPreOutroCurso() {
		if(confirmouCancelamentoMatriculaAtivaOuPreOutroCurso == null ) {
			confirmouCancelamentoMatriculaAtivaOuPreOutroCurso = Boolean.FALSE;
		}
		return confirmouCancelamentoMatriculaAtivaOuPreOutroCurso;
	}

	public void setConfirmouCancelamentoMatriculaAtivaOuPreOutroCurso(
			Boolean confirmouCancelamentoMatriculaAtivaOuPreOutroCurso) {
		this.confirmouCancelamentoMatriculaAtivaOuPreOutroCurso = confirmouCancelamentoMatriculaAtivaOuPreOutroCurso;
	}

		
	

	@XmlElement(name = "mensagemErroContrato")
	public String getMensagemErroContrato() {
		if (mensagemErroContrato == null ) {
			mensagemErroContrato = Constantes.EMPTY;
		}
		return mensagemErroContrato;
	}
	
	public void setMensagemErroContrato(String mensagemErroContrato) {
		this.mensagemErroContrato = mensagemErroContrato;
	}

	@XmlElement(name = "permiteAlunoIncluirExcluirDisciplina")
	public Boolean getPermiteAlunoIncluirExcluirDisciplina() {
		if(permiteAlunoIncluirExcluirDisciplina == null ) {
			permiteAlunoIncluirExcluirDisciplina = Boolean.FALSE;
		}
		return permiteAlunoIncluirExcluirDisciplina;
	}

	public void setPermiteAlunoIncluirExcluirDisciplina(Boolean permiteAlunoIncluirExcluirDisciplina) {
		this.permiteAlunoIncluirExcluirDisciplina = permiteAlunoIncluirExcluirDisciplina;
	}

	
	@XmlElement(name = "disciplinasMatricula")
	public List<DisciplinaRSVO> getDisciplinasMatricula() {
		if(disciplinasMatricula == null ){
			disciplinasMatricula = new ArrayList<DisciplinaRSVO>();
		}
		return disciplinasMatricula;
	}

	public static MatriculaRSVO converterDadosEmMatriculaRSVO(Integer unidadeEnsino, Integer aluno, Integer periodoLetivo,
			Integer condicaoPagamento, Integer turma, Integer curso, Integer turno, Integer gradeCurricular,
			Integer processoMatricula) {
		MatriculaRSVO matriculaRSVO= new MatriculaRSVO();
		matriculaRSVO.getUnidadeEnsinoRSVO().setCodigo(unidadeEnsino);
		matriculaRSVO.getPessoaObject().setCodigo(aluno);
		matriculaRSVO.getPeriodoLetivoRSVO().setCodigo(periodoLetivo);
		matriculaRSVO.getCondicaoPagamentoRSVO().setCodigo(condicaoPagamento);
		matriculaRSVO.getTurmaRSVO().setCodigo(turma);
		matriculaRSVO.getCursoObject().setCodigo(curso);
		matriculaRSVO.getTurnoRSVO().setCodigo(turno);
		matriculaRSVO.getCursoObject().getGradeDisciplinaObject().setCodigo(gradeCurricular);
		matriculaRSVO.getProcessoMatriculaRSVO().setCodigo(processoMatricula);	
		return matriculaRSVO ;
	}

	public Boolean getValidarAlunoJaMatriculado() {
		if(validarAlunoJaMatriculado == null ) {
			validarAlunoJaMatriculado =Boolean.TRUE;
		}
		return validarAlunoJaMatriculado;
	}

	public void setValidarAlunoJaMatriculado(Boolean validarAlunoJaMatriculado) {
		this.validarAlunoJaMatriculado = validarAlunoJaMatriculado;
	}

	

	
	
}
