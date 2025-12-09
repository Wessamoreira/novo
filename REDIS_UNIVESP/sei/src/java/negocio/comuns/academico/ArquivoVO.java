package negocio.comuns.academico;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.gson.annotations.Expose;

import negocio.comuns.academico.enumeradores.ModuloDisponibilizarMaterialEnum;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import webservice.DateAdapterMobile;

/**
 * Reponsável por manter os dados da entidade Arquivo. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */

@XmlRootElement(name = "arquivo")
public class ArquivoVO extends SuperVO implements Serializable  {

	private Integer codigo;
	private String nome;
	private String descricao;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private String descricaoAntesAlteracao;
	private String descricaoArquivo;
	private String extensao;
	private Date dataUpload;
	private Date dataDisponibilizacao;
	private Date dataIndisponibilizacao;
	private Boolean manterDisponibilizacao;
	private String origem;
	private String situacao;
	private Boolean controlarDownload;
	private Boolean permitirArquivoResposta;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private Integer arquivoResposta;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private Boolean downloadRealizado;
	private Integer codOrigem;
	private String pastaBaseArquivo;
	private PastaBaseArquivoEnum pastaBaseArquivoEnum;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private Boolean arquivoEstaNoDiretorioFixo;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private Boolean apresentarPortalAluno;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private Boolean apresentarPortalCoordenador;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private Boolean apresentarPortalProfessor;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private Boolean apresentarDeterminadoPeriodo;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private ArquivoVO uploadArquivo;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Usuario </code>.
	 */
	private UsuarioVO responsavelUpload;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Disciplina </code>.
	 */
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private DisciplinaVO disciplina;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Turma </code>.
	 */
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private TurmaVO turma;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private PessoaVO professor;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private String nivelEducacional;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private Boolean arquivoExisteHD;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private String cpfAlunoDocumentacao;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private String cpfPessoaDocumentacao;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private String cpfRequerimento;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private CursoVO curso;
	public static final long serialVersionUID = 1L;
	private String agrupador;
	private Integer indice;
	private List<ArquivoVO> arquivoFilhoVOs;
	private Integer indiceAgrupador;
	private Boolean agrupadorTurmaDisciplinaProfessor;
	private Boolean arquivoAssinadoDigitalmente;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private PessoaVO pessoaVO;
	private Integer tamanho;
	private Integer codigoCatalogo;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private UnidadeEnsinoVO unidadeEnsinoVO;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private TipoDocumentoVO tipoDocumentoVO;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private DepartamentoVO departamentoVO;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private ModuloDisponibilizarMaterialEnum moduloDisponibilizarMaterial; 
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private Boolean arquivoAssinadoFuncionario;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private Boolean arquivoAssinadoUnidadeEnsino;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private Boolean arquivoAssinadoUnidadeCertificadora;

	private ServidorArquivoOnlineEnum servidorArquivoOnline;
	private Boolean apresentarDocumentoPortalTransparencia;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	 private List<FuncionarioVO> listaFuncionarioAssinarDigitalmenteVOs;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private TipoRelatorioEnum tipoRelatorio;
	 
	//Transiente
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private Boolean validarDisciplina;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private boolean uploadRealizado = false;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private Boolean arquivoIreportPrincipal;
	
	//Transiente
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
		private Boolean assinarCertificadoDigitalUnidadeEnsino;
		//Transiente
	    //private Boolean assinarCertificadoDigitalUnidadeCertificadora;
	    
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private String caminhoImagemAnexo;	
	private List<String> listaAnoSemestreMobile;
	  
	private ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum;
	@ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
	private  Boolean selecionado;
	private Long tamanhoArquivo;
	private Boolean processadoPDFA;
	private String erroPDFA;
	private Boolean arquivoConvertidoPDFA;
	private Boolean arquivoIsPdfa;
	    
	
	/**
	 * Construtor padrão da classe <code>Arquivo</code>. Cria uma nova instância
	 * desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public ArquivoVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
	 */
	public void realizarUpperCaseDados() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		setNome(getNome().toUpperCase());
		setDescricao(getDescricao().toUpperCase());
		setExtensao(getExtensao().toUpperCase());
		setOrigem(getOrigem().toUpperCase());
		setSituacao(getSituacao().toUpperCase());
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setDataDisponibilizacao(new Date());
		setManterDisponibilizacao(Boolean.TRUE);
		setDataIndisponibilizacao(null);
	}

	/**
	 * Retorna o objeto da classe <code>Turma</code> relacionado com (
	 * <code>Arquivo</code>).
	 */
	
	@XmlElement(name = "turma")
	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return (turma);
	}

	/**
	 * Define o objeto da classe <code>Turma</code> relacionado com (
	 * <code>Arquivo</code>).
	 */
	public void setTurma(TurmaVO obj) {
		this.turma = obj;
	}

	/**
	 * Retorna o objeto da classe <code>Disciplina</code> relacionado com (
	 * <code>Arquivo</code>).
	 */
	@XmlElement(name = "disciplina")
	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return (disciplina);
	}

	/**
	 * Define o objeto da classe <code>Disciplina</code> relacionado com (
	 * <code>Arquivo</code>).
	 */
	public void setDisciplina(DisciplinaVO obj) {
		this.disciplina = obj;
	}

	/**
	 * Retorna o objeto da classe <code>Usuario</code> relacionado com (
	 * <code>Arquivo</code>).
	 */
	@XmlElement(name = "responsavelUpload")
	public UsuarioVO getResponsavelUpload() {
		if (responsavelUpload == null) {
			responsavelUpload = new UsuarioVO();
		}
		return (responsavelUpload);
	}

	/**
	 * Define o objeto da classe <code>Usuario</code> relacionado com (
	 * <code>Arquivo</code>).
	 */
	public void setResponsavelUpload(UsuarioVO obj) {
		this.responsavelUpload = obj;
	}

	@XmlElement(name = "controlarDownload")
	public Boolean getControlarDownload() {
		if (controlarDownload == null) {
			controlarDownload = Boolean.FALSE;
		}
		return (controlarDownload);
	}

	public void setControlarDownload(Boolean controlarDownload) {
		this.controlarDownload = controlarDownload;
	}

	@XmlElement(name = "situacao")
	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return (situacao);
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@XmlElement(name = "origem")
	public String getOrigem() {
		if (origem == null) {
			origem = "";
		}
		return (origem);
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	@XmlElement(name = "manterDisponibilizacao")
	public Boolean getManterDisponibilizacao() {
		return (manterDisponibilizacao);
	}

	public Boolean isManterDisponibilizacao() {
		if (manterDisponibilizacao == null) {
			manterDisponibilizacao = Boolean.FALSE;
		}
		return (manterDisponibilizacao);
	}

	public void setManterDisponibilizacao(Boolean manterDisponibilizacao) {
		this.manterDisponibilizacao = manterDisponibilizacao;
	}

	@XmlElement(name = "dataIndisponibilizacao")
	public Date getDataIndisponibilizacao() {
		return (dataIndisponibilizacao);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão
	 * dd/mm/aaaa.
	 */
	@XmlElement(name = "dataIndisponibilizacaoApresentar")
	public String getDataIndisponibilizacao_Apresentar() {
		return (Uteis.getData(dataIndisponibilizacao));
	}

	public void setDataIndisponibilizacao(Date dataIndisponibilizacao) {
		this.dataIndisponibilizacao = dataIndisponibilizacao;
	}

	@XmlElement(name = "dataDisponibilizacao")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataDisponibilizacao() {
		if (dataDisponibilizacao == null) {
			return null;
		}
		return (dataDisponibilizacao);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão
	 * dd/mm/aaaa.
	 */
	@XmlElement(name = "dataDisponibilizacaoApresentar")
	public String getDataDisponibilizacao_Apresentar() {
		return (Uteis.getData(getDataDisponibilizacao()));
	}

	public void setDataDisponibilizacao(Date dataDisponibilizacao) {
		this.dataDisponibilizacao = dataDisponibilizacao;
	}

	@XmlElement(name = "dataUpload")
    @XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataUpload() {
		if (dataUpload == null) {
			dataUpload = new Date();
		}
		return (dataUpload);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão
	 * dd/mm/aaaa.
	 */
	@XmlElement(name = "dataUploadApresentar")
	public String getDataUpload_Apresentar() {
		return (Uteis.getData(getDataUpload()));
	}

	public void setDataUpload(Date dataUpload) {
		this.dataUpload = dataUpload;
	}

	@XmlElement(name = "extensao")
	public String getExtensao() {
		if (extensao == null) {
			extensao = "";
		}
		if(extensao.equals("") && getNome().contains(".")) {
			extensao = getNome().substring(getNome().lastIndexOf(".")+1, getNome().length());
		}
		return (extensao);
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	@XmlElement(name = "descricao")
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return (descricao);
	}

	public String getDescricaoCurto() {
		if (descricao == null) {
			descricao = "";
		}
		return (descricao.substring(0, 21));
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricaoAntesAlteracao() {
		if (descricaoAntesAlteracao == null) {
			descricaoAntesAlteracao = "";
		}
		return descricaoAntesAlteracao;
	}

	public void setDescricaoAntesAlteracao(String descricaoAntesAlteracao) {
		this.descricaoAntesAlteracao = descricaoAntesAlteracao;
	}

	@XmlElement(name = "nome")
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return (nome);
	}

	public String getNomeCurto() {
		if (nome == null) {
			nome = "";
		}
		return (nome.substring(0, 21));
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ArquivoVO clones() {
		ArquivoVO obj = new ArquivoVO();
		obj.setArquivoResposta(arquivoResposta);
		obj.setCodigo(codigo);
		obj.setControlarDownload(controlarDownload);
		obj.setDataDisponibilizacao(dataDisponibilizacao);
		obj.setDataIndisponibilizacao(dataIndisponibilizacao);
		obj.setDataUpload(dataUpload);
		obj.setDescricao(descricao);
		obj.setDownloadRealizado(downloadRealizado);
		obj.setExtensao(extensao);
		obj.setManterDisponibilizacao(manterDisponibilizacao);
		obj.setNome(nome);
		obj.setOrigem(origem);
		obj.setPermitirArquivoResposta(permitirArquivoResposta);
		obj.setResponsavelUpload(responsavelUpload);
		obj.setSituacao(situacao);
		obj.getTurma().setCodigo(getTurma().getCodigo());
		obj.getTurma().setIdentificadorTurma(getTurma().getIdentificadorTurma());

		obj.getDisciplina().setCodigo(getDisciplina().getCodigo());
		obj.getDisciplina().setNome(getDisciplina().getNome());
		
		if(Uteis.isAtributoPreenchido(getListaFuncionarioAssinarDigitalmenteVOs())) {
			obj.setListaFuncionarioAssinarDigitalmenteVOs(getListaFuncionarioAssinarDigitalmenteVOs());
		}

		return obj;
	}

	@XmlElement(name = "permitirArquivoResposta")
	public Boolean getPermitirArquivoResposta() {
		if (permitirArquivoResposta == null) {
			permitirArquivoResposta = Boolean.FALSE;
		}
		return (permitirArquivoResposta);
	}

	public void setPermitirArquivoResposta(Boolean permitirArquivoResposta) {
		this.permitirArquivoResposta = permitirArquivoResposta;
	}

	@XmlElement(name = "arquivoResposta")
	public Integer getArquivoResposta() {
		if (arquivoResposta == null) {
			arquivoResposta = 0;
		}
		return arquivoResposta;
	}

	public void setArquivoResposta(Integer arquivoResposta) {
		this.arquivoResposta = arquivoResposta;
	}

	@XmlElement(name = "downloadRealizado")
	public Boolean getDownloadRealizado() {
		if (downloadRealizado == null) {
			downloadRealizado = false;
		}
		return downloadRealizado;
	}

	public void setDownloadRealizado(Boolean downloadRealizado) {
		this.downloadRealizado = downloadRealizado;
	}

	public String getCssDownloadRealizado() {
		if (getDownloadRealizado()) {
			return "background-color: #B7FFDB;";
		}
		return "background-color: #FFFFFF;";
	}

	@XmlElement(name = "codOrigem")
	public Integer getCodOrigem() {
		if (codOrigem == null) {
			codOrigem = 0;
		}
		return codOrigem;
	}

	public void setCodOrigem(Integer codOrigem) {
		this.codOrigem = codOrigem;
	}

	public void setPastaBaseArquivo(String pastaBaseArquivo) {
		this.pastaBaseArquivo = pastaBaseArquivo;
	}

	
	@XmlElement(name = "pastaBaseArquivo")
	public String getPastaBaseArquivo() {
		if (pastaBaseArquivo == null) {
			pastaBaseArquivo = "";
		}
		return pastaBaseArquivo;
	}

	public void setPastaBaseArquivoEnum(PastaBaseArquivoEnum pastaBaseArquivoEnum) {
		this.pastaBaseArquivoEnum = pastaBaseArquivoEnum;
	}

	@XmlElement(name = "pastaBaseArquivoEnum")
	public PastaBaseArquivoEnum getPastaBaseArquivoEnum() {
		return pastaBaseArquivoEnum;
	}

	public void setArquivoEstaNoDiretorioFixo(Boolean arquivoEstaNoDiretorioFixo) {
		this.arquivoEstaNoDiretorioFixo = arquivoEstaNoDiretorioFixo;
	}

	@XmlElement(name = "arquivoEstaNoDiretorioFixo")
	public Boolean getArquivoEstaNoDiretorioFixo() {
		if (arquivoEstaNoDiretorioFixo == null) {
			arquivoEstaNoDiretorioFixo = false;
		}
		return arquivoEstaNoDiretorioFixo;
	}

	/**
	 * @return the professor
	 */
	
	@XmlElement(name = "professor")
	public PessoaVO getProfessor() {
		if (professor == null) {
			professor = new PessoaVO();
		}
		return professor;
	}

	/**
	 * @param professor
	 *            the professor to set
	 */
	public void setProfessor(PessoaVO professor) {
		this.professor = professor;
	}

	public String getNivelEducacional_Apresentar() {
		return TipoNivelEducacional.getDescricao(getNivelEducacional());
	}

	@XmlElement(name = "nivelEducacional")
	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	/**
	 * @return the arquivoExisteHD
	 */
	@XmlElement(name = "arquivoExisteHD")
	public Boolean getArquivoExisteHD() {
		if (arquivoExisteHD == null) {
			arquivoExisteHD = Boolean.FALSE;
		}
		return arquivoExisteHD;
	}

	/**
	 * @param arquivoExisteHD
	 *            the arquivoExisteHD to set
	 */
	public void setArquivoExisteHD(Boolean arquivoExisteHD) {
		this.arquivoExisteHD = arquivoExisteHD;
	}

	@XmlElement(name = "cpfAlunoDocumentacao")
	public String getCpfAlunoDocumentacao() {
		if (cpfAlunoDocumentacao == null) {
			cpfAlunoDocumentacao = "";
		}
		return cpfAlunoDocumentacao;
	}

	public void setCpfAlunoDocumentacao(String cpfAlunoDocumentacao) {
		this.cpfAlunoDocumentacao = cpfAlunoDocumentacao;
	}
	
	public String getCpfPessoaDocumentacao() {
		if(cpfPessoaDocumentacao == null) {
			cpfPessoaDocumentacao = "";
		}
		return cpfPessoaDocumentacao;
	}

	public void setCpfPessoaDocumentacao(String cpfPessoaDocumentacao) {
		this.cpfPessoaDocumentacao = cpfPessoaDocumentacao;
	}

	@XmlElement(name = "cpfRequerimento")
	public String getCpfRequerimento() {
		if (cpfRequerimento == null) {
			cpfRequerimento = "";
		}
		return cpfRequerimento;
	}

	public void setCpfRequerimento(String cpfRequerimento) {
		this.cpfRequerimento = cpfRequerimento;
	}

	@XmlElement(name = "apresentarPortalAluno")
	public Boolean getApresentarPortalAluno() {
		if (apresentarPortalAluno == null) {
			apresentarPortalAluno = Boolean.TRUE;
		}
		return apresentarPortalAluno;
	}

	public Boolean getApresentarNomeArquivo() {
		if (getDescricao() == null || getDescricao().equals("")) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public void setApresentarPortalAluno(Boolean apresentarPortalAluno) {
		this.apresentarPortalAluno = apresentarPortalAluno;
	}

	@XmlElement(name = "apresentarPortalCoordenador")
	public Boolean getApresentarPortalCoordenador() {
		if (apresentarPortalCoordenador == null) {
			apresentarPortalCoordenador = Boolean.TRUE;
		}
		return apresentarPortalCoordenador;
	}

	public void setApresentarPortalCoordenador(Boolean apresentarPortalCoordenador) {
		this.apresentarPortalCoordenador = apresentarPortalCoordenador;
	}

	@XmlElement(name = "apresentarPortalProfessor")
	public Boolean getApresentarPortalProfessor() {
		if (apresentarPortalProfessor == null) {
			apresentarPortalProfessor = Boolean.TRUE;
		}
		return apresentarPortalProfessor;
	}

	public void setApresentarPortalProfessor(Boolean apresentarPortalProfessor) {
		this.apresentarPortalProfessor = apresentarPortalProfessor;
	}

	@XmlElement(name = "curso")
	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}

	public Boolean getIsImagem() {
		if (getNome().endsWith(".jpeg") || getNome().endsWith(".JPEG") || getNome().endsWith(".jpg")
		        || getNome().endsWith(".JPG") || getNome().endsWith(".png") || getNome().endsWith(".PNG")
		        || getNome().endsWith(".gif") || getNome().endsWith(".GIF") || getNome().endsWith(".bmp")
		        || getNome().endsWith(".BMP") || getNome().endsWith(".ico") || getNome().endsWith(".ICO")) {
			return true;
		}
		return false;
	}

	public Boolean getIsWord() {
		if (getNome().endsWith(".doc") || getNome().endsWith(".docx") || getNome().endsWith(".DOC")
		        || getNome().endsWith(".DOCX")) {
			return true;
		}
		return false;
	}

	public Boolean getIsExcel() {
		if (getNome().endsWith(".xls") || getNome().endsWith(".xlsx") || getNome().endsWith(".XLS")
		        || getNome().endsWith(".XLSX")) {
			return true;
		}
		return false;
	}

	public Boolean getIsPdF() {
		if (getNome().endsWith(".pdf") || getNome().endsWith(".PDF")) {
			return true;
		}
		return false;
	}

	public Boolean getIsTxt() {
		if (getNome().endsWith(".txt") || getNome().endsWith(".TXT")) {
			return true;
		}
		return false;
	}

	public Boolean getIsOutros() {
		if (!getIsImagem() && !getIsWord() && !getIsExcel() && !getIsPdF() && !getIsTxt() && Uteis.isAtributoPreenchido(getNome())) {
			return true;
		}
		return false;
	}

	@XmlElement(name = "apresentarDeterminadoPeriodo")
	public Boolean getApresentarDeterminadoPeriodo() {
		if (apresentarDeterminadoPeriodo == null) {
			apresentarDeterminadoPeriodo = false;
		}
		return apresentarDeterminadoPeriodo;
	}

	public void setApresentarDeterminadoPeriodo(Boolean apresentarDeterminadoPeriodo) {
		this.apresentarDeterminadoPeriodo = apresentarDeterminadoPeriodo;
	}

	@XmlElement(name = "agrupador")
	public String getAgrupador() {
		if (agrupador == null) {
			agrupador = "";
		}
		return agrupador;
	}

	public void setAgrupador(String agrupador) {
		this.agrupador = agrupador;
	}

	@XmlElement(name = "arquivoFilhoVOs")
	public List<ArquivoVO> getArquivoFilhoVOs() {
		if (arquivoFilhoVOs == null) {
			arquivoFilhoVOs = new ArrayList<ArquivoVO>(0);
		}
		return arquivoFilhoVOs;
	}

	public void setArquivoFilhoVOs(List<ArquivoVO> arquivoFilhoVOs) {
		this.arquivoFilhoVOs = arquivoFilhoVOs;
	}

	@XmlElement(name = "indice")
	public Integer getIndice() {
		if (indice == null) {
			indice = 0;
		}
		return indice;
	}

	public void setIndice(Integer indice) {
		this.indice = indice;
	}

	@XmlElement(name = "indiceAgrupador")
	public Integer getIndiceAgrupador() {
		if (indiceAgrupador == null) {
			indiceAgrupador = 0;
		}
		return indiceAgrupador;
	}

	public void setIndiceAgrupador(Integer indiceAgrupador) {
		this.indiceAgrupador = indiceAgrupador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((indice == null) ? 0 : indice.hashCode());
		result = prime * result + ((indiceAgrupador == null) ? 0 : indiceAgrupador.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArquivoVO other = (ArquivoVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (indice == null) {
			if (other.indice != null)
				return false;
		} else if (!indice.equals(other.indice))
			return false;
		if (indiceAgrupador == null) {
			if (other.indiceAgrupador != null)
				return false;
		} else if (!indiceAgrupador.equals(other.indiceAgrupador))
			return false;
		return true;
	}

	/**
	 * @return the agrupadorTurmaDisciplinaProfessor
	 */
	@XmlElement(name = "agrupadorTurmaDisciplinaProfessor")
	public Boolean getAgrupadorTurmaDisciplinaProfessor() {
		if (agrupadorTurmaDisciplinaProfessor == null) {
			agrupadorTurmaDisciplinaProfessor = false;
		}
		return agrupadorTurmaDisciplinaProfessor;
	}

	/**
	 * @param agrupadorTurmaDisciplinaProfessor
	 *            the agrupadorTurmaDisciplinaProfessor to set
	 */
	public void setAgrupadorTurmaDisciplinaProfessor(Boolean agrupadorTurmaDisciplinaProfessor) {
		this.agrupadorTurmaDisciplinaProfessor = agrupadorTurmaDisciplinaProfessor;
	}

	public String keyAgrupadorTurmaDisciplinaProfessor;

	@XmlElement(name = "keyAgrupadorTurmaDisciplinaProfessor")
	public String getKeyAgrupadorTurmaDisciplinaProfessor() {
		if (keyAgrupadorTurmaDisciplinaProfessor == null) {
			keyAgrupadorTurmaDisciplinaProfessor = "D" + getDisciplina().getCodigo() + "T" + getTurma().getCodigo()
			        + "P" + getProfessor().getCodigo();
		}
		return keyAgrupadorTurmaDisciplinaProfessor;
	}

	/**
	 * @param keyAgrupadorTurmaDisciplinaProfessor
	 *            the keyAgrupadorTurmaDisciplinaProfessor to set
	 */
	public void setKeyAgrupadorTurmaDisciplinaProfessor(String keyAgrupadorTurmaDisciplinaProfessor) {
		this.keyAgrupadorTurmaDisciplinaProfessor = keyAgrupadorTurmaDisciplinaProfessor;
	}

	@XmlElement(name = "arquivoAssinadoDigitalmente")
	public Boolean getArquivoAssinadoDigitalmente() {
		if (arquivoAssinadoDigitalmente == null) {
			arquivoAssinadoDigitalmente = false;
		}
		return arquivoAssinadoDigitalmente;
	}

	public void setArquivoAssinadoDigitalmente(Boolean arquivoAssinadoDigitalmente) {
		this.arquivoAssinadoDigitalmente = arquivoAssinadoDigitalmente;
	}

	@XmlElement(name = "pessoaVO")
	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	private String pastaBaseArquivoWeb;

	@XmlElement(name = "pastaBaseArquivoWeb")
	public String getPastaBaseArquivoWeb() {
		if (pastaBaseArquivoWeb == null) {
			pastaBaseArquivoWeb = "";
		}
		return pastaBaseArquivoWeb;
	}

	public void setPastaBaseArquivoWeb(String pastaBaseArquivoWeb) {
		this.pastaBaseArquivoWeb = pastaBaseArquivoWeb;
	}

	public String getZoom() {
		if (getTamanho() != null) {
			return "width:" + getTamanho() + "%";
		}
		return "width:50%";
	}

	@XmlElement(name = "tamanho")
	public Integer getTamanho() {
		if (tamanho == null) {
			tamanho = 10;
		}
		return tamanho;
	}
	
	@XmlElement(name = "descricaoArquivo")
	public String getDescricaoArquivo() {
		if(descricaoArquivo == null) {
			descricaoArquivo = "";
		}
		return descricaoArquivo;
	}

	public void setDescricaoArquivo(String descricaoArquivo) {
		this.descricaoArquivo = descricaoArquivo;
	}

	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	@XmlElement(name = "servidorArquivoOnline")
	public ServidorArquivoOnlineEnum getServidorArquivoOnline() {
		if (servidorArquivoOnline == null)
			servidorArquivoOnline = ServidorArquivoOnlineEnum.APACHE;
		return servidorArquivoOnline;
	}

	public void setServidorArquivoOnline(ServidorArquivoOnlineEnum servidorArquivoOnline) {
		this.servidorArquivoOnline = servidorArquivoOnline;
	}

	
	public String obterCaminhoAposDiretorioBase(PastaBaseArquivoEnum pastaBaseArquivoEnum) {
		if(getPastaBaseArquivo().contains(pastaBaseArquivoEnum.getValue())) {
			return getPastaBaseArquivo().substring(getPastaBaseArquivo().indexOf(pastaBaseArquivoEnum.getValue()), getPastaBaseArquivo().length());
		}
		return getPastaBaseArquivo();
	}
	
	public String obterUrlParaDownload(String urlExternoDownloadArquivo, PastaBaseArquivoEnum pastaBaseArquivoEnum) {
		if(pastaBaseArquivoEnum == null) {
			pastaBaseArquivoEnum = PastaBaseArquivoEnum.obterPastaBaseEnum(urlExternoDownloadArquivo, getPastaBaseArquivo());
		}
		if (getPastaBaseArquivoWeb().isEmpty()) {
			if (!urlExternoDownloadArquivo.endsWith("/")) {
				urlExternoDownloadArquivo += "/";
			}
			urlExternoDownloadArquivo += obterCaminhoAposDiretorioBase(pastaBaseArquivoEnum);
			if (!urlExternoDownloadArquivo.endsWith("/")) {
				urlExternoDownloadArquivo += "/";
			}
			if (urlExternoDownloadArquivo.contains(File.separator)) {
				urlExternoDownloadArquivo = urlExternoDownloadArquivo.replace("\\", "/");
			}
			urlExternoDownloadArquivo += getNome();
			setPastaBaseArquivoWeb(urlExternoDownloadArquivo);
		}
		return getPastaBaseArquivoWeb();
	}
	
	public String obterLocalFisico(String urlBaseRepositorio, PastaBaseArquivoEnum pastaBaseArquivoEnum) {
			if(pastaBaseArquivoEnum == null) {
				pastaBaseArquivoEnum = PastaBaseArquivoEnum.obterPastaBaseEnum(urlBaseRepositorio, getPastaBaseArquivo());
			}
			if (urlBaseRepositorio.contains("/") && !File.separator.equals("/")) {
				urlBaseRepositorio = urlBaseRepositorio.replace("/", File.separator);
			}	
			if (!urlBaseRepositorio.endsWith(File.separator)) {
				urlBaseRepositorio += File.separator;
			}
			urlBaseRepositorio += obterCaminhoAposDiretorioBase(pastaBaseArquivoEnum);
			if (!urlBaseRepositorio.endsWith(File.separator)) {
				urlBaseRepositorio += File.separator;
			}
			if (urlBaseRepositorio.contains("/") && !File.separator.equals("/")) {
				urlBaseRepositorio = urlBaseRepositorio.replace("/", File.separator);
			}
			urlBaseRepositorio += getNome();				
		return urlBaseRepositorio;
	}
	
	/**
	 * Retorna 
	 * @param pastaBaseArquivo
	 * @param localUploadArquivo
	 * @return
	 */
	public String recuperaNomePastaBaseCorrigidoLocalUpload(String pastaBaseArquivo, String localUploadArquivo) {
		String pastaUsar = pastaBaseArquivo.replace(localUploadArquivo, "").replace("\\", "/");
		if(pastaUsar.startsWith("/")) {
			pastaUsar = pastaUsar.substring(1, pastaUsar.length());
		}
		return pastaUsar;
	}
	
	public String recuperaNomePastaBaseCorrigido(String pastaBaseArquivo, String localUploadArquivo) {
		String pastaUsar = recuperaNomePastaBaseCorrigidoLocalUpload(pastaBaseArquivo, localUploadArquivo);
		pastaUsar = pastaUsar.replace("TMP", "");
		return pastaUsar;
	}
	
	public String recuperarNomeArquivoServidorExterno(String pastaBaseArquivo, String localUploadArquivo, String nomeArquivo) {
		return recuperaNomePastaBaseCorrigido(pastaBaseArquivo, localUploadArquivo) + "/" + nomeArquivo;
	}
	
	/*public String adicionarParametrosNaPastaBaseArquivo(String pastaBaseUsar) {
		if(getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ARQUIVO) || getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ARQUIVO_TMP)) {
			if(Uteis.isAtributoPreenchido(getTurma())) {
				pastaBaseUsar += (File.separator+getTurma().getCodigo());
			}			
			if(Uteis.isAtributoPreenchido(getProfessor())) {
				pastaBaseUsar += (File.separator+getProfessor().getCodigo());
			}
			if(Uteis.isAtributoPreenchido(getAgrupador())) {
				pastaBaseUsar += (File.separator+Uteis.removerAcentuacao(getAgrupador()).replace(" ", "").replace("/", ""));
			}
		}
		return pastaBaseUsar;
	}*/

	@XmlElement(name = "validarDisciplina")
	public Boolean getValidarDisciplina() {
		if (validarDisciplina == null) {
			validarDisciplina = Boolean.TRUE;
		}
		return validarDisciplina;
	}

	public void setValidarDisciplina(Boolean validarDisciplina) {
		this.validarDisciplina = validarDisciplina;
	}
	
	public boolean isUploadRealizado() {
		return uploadRealizado;
	}

	public void setUploadRealizado(boolean uploadRealizado) {
		this.uploadRealizado = uploadRealizado;
	}
	
	
	@XmlElement(name = "descricaoArquivoApresentar")
	public String getDescricaoArquivo_Apresentar() {
		if(getDescricaoArquivo() != null && !getDescricaoArquivo().equals("")) {
			return getDescricaoArquivo();
		}
		else if(getDescricao() != null && !getDescricao().equals("")) {
			return getDescricao();
		}else {
			return getNome();
		}	

	}
	
	
	public String getDescricaoApresentar() {
		if(!getDescricaoArquivo().trim().isEmpty()) {
			return getDescricaoArquivo();
		}else if(!getDescricao().trim().isEmpty()) {
				return getDescricao();
		}
		return getNome();
	}

	public Integer getCodigoCatalogo() {
		if (codigoCatalogo == null) {
			codigoCatalogo = 0;
		}
		return codigoCatalogo;
	}

	public void setCodigoCatalogo(Integer codigoCatalogo) {
		this.codigoCatalogo = codigoCatalogo;
	}

	public Boolean getArquivoAssinadoFuncionario() {
		if(arquivoAssinadoFuncionario == null) {
			arquivoAssinadoFuncionario = Boolean.FALSE;
		}
		return arquivoAssinadoFuncionario;
	}

	public void setArquivoAssinadoFuncionario(Boolean arquivoAssinadoFuncionario) {
		this.arquivoAssinadoFuncionario = arquivoAssinadoFuncionario;
	}

	public Boolean getArquivoAssinadoUnidadeEnsino() {
		if(arquivoAssinadoUnidadeEnsino == null) {
			arquivoAssinadoUnidadeEnsino = Boolean.FALSE;
		}
		return arquivoAssinadoUnidadeEnsino;
	}

	public void setArquivoAssinadoUnidadeEnsino(Boolean arquivoAssinadoUnidadeEnsino) {
		this.arquivoAssinadoUnidadeEnsino = arquivoAssinadoUnidadeEnsino;
	}

	public Boolean getArquivoAssinadoUnidadeCertificadora() {
		if(arquivoAssinadoUnidadeCertificadora == null) {
			arquivoAssinadoUnidadeCertificadora = Boolean.FALSE;
		}
		return arquivoAssinadoUnidadeCertificadora;
	}

	public void setArquivoAssinadoUnidadeCertificadora(Boolean arquivoAssinadoUnidadeCertificadora) {
		this.arquivoAssinadoUnidadeCertificadora = arquivoAssinadoUnidadeCertificadora;
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

	public TipoDocumentoVO getTipoDocumentoVO() {
		if (tipoDocumentoVO == null) {
			tipoDocumentoVO = new TipoDocumentoVO();
		}
		return tipoDocumentoVO;
	}

	public void setTipoDocumentoVO(TipoDocumentoVO tipoDocumentoVO) {
		this.tipoDocumentoVO = tipoDocumentoVO;
	}

	public DepartamentoVO getDepartamentoVO() {
		if (departamentoVO == null) {
			departamentoVO = new DepartamentoVO();
		}
		return departamentoVO;
	}

	public void setDepartamentoVO(DepartamentoVO departamentoVO) {
		this.departamentoVO = departamentoVO;
	}


	public Boolean getAssinarCertificadoDigitalUnidadeEnsino() {
		if (assinarCertificadoDigitalUnidadeEnsino == null) {
			assinarCertificadoDigitalUnidadeEnsino = false;
		}
		return assinarCertificadoDigitalUnidadeEnsino;
	}

	public void setAssinarCertificadoDigitalUnidadeEnsino(Boolean assinarCertificadoDigitalUnidadeEnsino) {
		this.assinarCertificadoDigitalUnidadeEnsino = assinarCertificadoDigitalUnidadeEnsino;
	}

//	public Boolean getAssinarCertificadoDigitalUnidadeCertificadora() {
//		if (assinarCertificadoDigitalUnidadeCertificadora == null) {
//			assinarCertificadoDigitalUnidadeCertificadora = false;
//		}
//		return assinarCertificadoDigitalUnidadeCertificadora;
//	}
//
//	public void setAssinarCertificadoDigitalUnidadeCertificadora(Boolean assinarCertificadoDigitalUnidadeCertificadora) {
//		this.assinarCertificadoDigitalUnidadeCertificadora = assinarCertificadoDigitalUnidadeCertificadora;
//	}

	public Boolean getApresentarDocumentoPortalTransparencia() {
		if (apresentarDocumentoPortalTransparencia == null) {
			apresentarDocumentoPortalTransparencia = false;
		}
		return apresentarDocumentoPortalTransparencia;
	}

	public void setApresentarDocumentoPortalTransparencia(Boolean apresentarDocumentoPortalTransparencia) {
		this.apresentarDocumentoPortalTransparencia = apresentarDocumentoPortalTransparencia;
	}

	public ModuloDisponibilizarMaterialEnum getModuloDisponibilizarMaterial() {
		if (moduloDisponibilizarMaterial == null) {
			moduloDisponibilizarMaterial = ModuloDisponibilizarMaterialEnum.ACADEMICO;
		}
		return moduloDisponibilizarMaterial;
	}

	public void setModuloDisponibilizarMaterial(ModuloDisponibilizarMaterialEnum moduloDisponibilizarMaterial) {
		this.moduloDisponibilizarMaterial = moduloDisponibilizarMaterial;
	}
	
	private String icone;
	
	public String getIcone() {
		if(icone == null) {		
			icone = ArquivoHelper.getIcone(getNome());		
		}
		return icone;
	}
    

	public Boolean getArquivoIreportPrincipal() {
		if(arquivoIreportPrincipal == null) {
			arquivoIreportPrincipal = false;
		}
		return arquivoIreportPrincipal;
	}

	public void setArquivoIreportPrincipal(Boolean arquivoIreportPrincipal) {
		this.arquivoIreportPrincipal = arquivoIreportPrincipal;
	}
	
	public List<FuncionarioVO> getListaFuncionarioAssinarDigitalmenteVOs() {
		if (listaFuncionarioAssinarDigitalmenteVOs == null) {
			listaFuncionarioAssinarDigitalmenteVOs = new ArrayList<FuncionarioVO>(0);
		}
		return listaFuncionarioAssinarDigitalmenteVOs;
	}

	public void setListaFuncionarioAssinarDigitalmenteVOs(List<FuncionarioVO> listaFuncionarioAssinarDigitalmenteVOs) {
		this.listaFuncionarioAssinarDigitalmenteVOs = listaFuncionarioAssinarDigitalmenteVOs;
	}

	@XmlElement(name = "caminhoImagemAnexo")
	public String getCaminhoImagemAnexo() {
		if (caminhoImagemAnexo == null) {
			caminhoImagemAnexo = "";
		}
		return caminhoImagemAnexo;
	}

	public void setCaminhoImagemAnexo(String caminhoImagemAnexo) {
		this.caminhoImagemAnexo = caminhoImagemAnexo;
	}
	
	public ProvedorDeAssinaturaEnum getProvedorDeAssinaturaEnum() {
		return provedorDeAssinaturaEnum;
	}

	public void setProvedorDeAssinaturaEnum(ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum) {
		this.provedorDeAssinaturaEnum = provedorDeAssinaturaEnum;
	}

	public Boolean getApresentarIconeExcel() {
		return getExtensao().contentEquals("xls") || getExtensao().contentEquals("xlsx");
	}
	
	public Boolean getApresentarIconeWord() {
		return getExtensao().contentEquals("doc") || getExtensao().contentEquals("docm")  || getExtensao().contentEquals("docx");
	}
	
	public Boolean getApresentarIconeTXT() {
		return getExtensao().contentEquals("txt");
	}
	public Boolean getApresentarIconePDF() {
		return getExtensao().contentEquals("pdf");
	}
	
	public Boolean getApresentarImagem() {
		return getExtensao().contentEquals("jpeg") || getExtensao().contentEquals("jpg") || getExtensao().contentEquals("gif")  || getExtensao().contentEquals("png")  || getExtensao().contentEquals("bmp") 
				|| getExtensao().contentEquals("tiff")  || getExtensao().contentEquals("psd") || getExtensao().contentEquals("exif")  || getExtensao().contentEquals("raw");
	}
	
	@XmlElement(name = "listaAnoSemestreMobile")	
	public List<String> getListaAnoSemestreMobile() {
		if (listaAnoSemestreMobile == null) {
			listaAnoSemestreMobile = new ArrayList<String>();
		}
		return listaAnoSemestreMobile;
	}

	public void setListaAnoSemestreMobile(List<String> listaAnoSemestreMobile) {
		this.listaAnoSemestreMobile = listaAnoSemestreMobile;
	}

	public TipoRelatorioEnum getTipoRelatorio() {
		if(tipoRelatorio == null) {
			tipoRelatorio =  TipoRelatorioEnum.PDF;
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(TipoRelatorioEnum tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}
	
	public ArquivoVO getUploadArquivo() {
		if(uploadArquivo == null	){
			uploadArquivo = new ArquivoVO();
		}
		return uploadArquivo;
	}

	public void setUploadArquivo(ArquivoVO uploadArquivo) {
		this.uploadArquivo = uploadArquivo;
	}
	
	public void montarCampoDescricao(String urlExternoDownloadArquivo) {
		if (urlExternoDownloadArquivo.endsWith("/") || urlExternoDownloadArquivo.endsWith("\\")) {
			setDescricao(urlExternoDownloadArquivo + getPastaBaseArquivo() + File.separator + getNome());
		} else {
			setDescricao(urlExternoDownloadArquivo + File.separator + getPastaBaseArquivo() + File.separator + getNome());
		}
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

	public Long getTamanhoArquivo() {
		if (tamanhoArquivo == null) {
			tamanhoArquivo = 0L;
		}
		return tamanhoArquivo;
	}

	public void setTamanhoArquivo(Long tamanhoArquivo) {
		this.tamanhoArquivo = tamanhoArquivo;
	}
	
	public Boolean getProcessadoPDFA() {
		if (processadoPDFA == null) {
			processadoPDFA = Boolean.FALSE;
		}
		return processadoPDFA;
	}
	
	public void setProcessadoPDFA(Boolean processadoPDFA) {
		this.processadoPDFA = processadoPDFA;
	}
	
	public String getErroPDFA() {
		if (erroPDFA == null) {
			erroPDFA = Constantes.EMPTY;
		}
		return erroPDFA;
	}
	
	public void setErroPDFA(String erroPDFA) {
		this.erroPDFA = erroPDFA;
	}
	
	public Boolean getArquivoConvertidoPDFA() {
		if (arquivoConvertidoPDFA == null) {
			arquivoConvertidoPDFA = Boolean.FALSE;
		}
		return arquivoConvertidoPDFA;
	}
	
	public void setArquivoConvertidoPDFA(Boolean arquivoConvertidoPDFA) {
		this.arquivoConvertidoPDFA = arquivoConvertidoPDFA;
	}
	
	public Boolean getArquivoIsPdfa() {
		return arquivoIsPdfa;
	}
	
	public void setArquivoIsPdfa(Boolean arquivoIsPdfa) {
		this.arquivoIsPdfa = arquivoIsPdfa;
	}
}