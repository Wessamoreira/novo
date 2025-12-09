package relatorio.negocio.comuns.academico;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ObservacaoComplementarDiplomaVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import relatorio.negocio.comuns.biblioteca.EmprestimoFiltroRelVO;

/**
 * @author Otimize-TI
 */
public class HistoricoAlunoRelVO implements Cloneable {

	private MatriculaVO matriculaVO;
	private String matricula;
	private String nome;
	private String nomePai;
	private String nomeMae;
	private String sexo;
	private String dataNasc;
	private String nacionalidade;
	private String naturalidade;
	private String uf;
	private String rg;
	private String orgaoExpedidor;
	private String dataEmissaoRg;
	private String estadoEmissaoRg;
	private String cpf;
	private String docMilitar;
	private String dataEmissaoDocMilitar;
	private String orgaoExpedidorDocMilitar;
	private String tituloEleitor;
	private String dataEmissaoTituloEleitor;
	private String zonaEleitoral;
	private String secaoZonaEleitoral;
	private String conclusaoEnsMedio;
	private String cidadeConclusaoEnsMedio;
	private String estadoConclusaoEnsMedio;
	private String anoConclusaoEnsMedio;
	private String semestreConclusaoEnsMedio;
	private String nomeCurso;
	private String habilitacao;
	private String dataReconhecimento;
	private String reconhecimento;
	private String publicacaoDO;
	private String publicacaoDOExtenso;
	private String nrRegistroInterno;
	private String conclusaoCurso;
	private String colacaoGrau;
	private String expedicaoDiploma;
	private String chGradeCurricular;
	private String chExigida;
	private String chExigidaPeriodoLetivos;
	private String chCumprida;
	private String formaIngresso;
	private String classificacaoIngresso;	
	private String localProcessoSeletivo;
	private String anoIngresso;
	private String mesIngresso;
	private String mesIngressoExtenso;
	private String semestreIngresso;
	private String discProcSeletivo;
	private Double totalPontoProcSeletivo;
	private String nomeDisciplina;
	private String nomeMatriz;
	private String semestre;
	private String anoSemstre;
	private String chDisciplina;
	private String crDisciplina;
	private String freqDisciplina;
	private String mediaFinal;
	private String situacaoFinal;
	private String totalcargahorariaatividadecomplementar;
	private String periodoCursado;
	private Boolean apresentarFrequencia;
	private Boolean cursoProfissionalizante;
	
	private Integer codigoIES;
	private Integer codigoIESMantenedora;
	private Integer codigoEmecCurso;
	private String anoMesIngressoInstituicao;
	private String mesIngressoExtensoAbreviado;
	// unidade matriz
	private String mantenedora;
	// unidade onde o curso esta cadastrado
	private String mantida;
	private String cidade;
	private String cidadeDataAtual;
	private String dataAtual;
	private String dataAtualExtenso;
	private String endereco;
	private String caixaPostal;
	private String numero;
	private String bairro;
	private String complemento;
	private String cep;
	private String estado;
	private String email;
	private String fone;
	private String site;
	private String cnpj;
	private String inscEstadual;
	private String inscMunicipal;
	private List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaRelVOs;
	private List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaForaGradeRelVOs;
	private List<HistoricoAlunoDisciplinaRelVO> historicoAlunoPeriodoRelVOs;
	private List<HistoricoAlunoAtividadeComplementarRelVO> historicoAlunoAtividadeComplementarRelVOs;
	private List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaTccRelVOs;
	private List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaEstagioRelVOs;
	private List<GradeCurricularEstagioVO> historicoAlunoComponenteEstagioRelVOs;
	private FuncionarioVO funcionarioPrincipalVO;
	private FuncionarioVO funcionarioSecundarioVO;
	private String tituloFuncionarioPrincipal;
	private String tituloFuncionarioSecundario;
	private CargoVO cargoFuncionarioPrincipal;
	private CargoVO cargoFuncionarioSecundario;
	private String obsHistorico;
	private String obsHistoricoTransferenciaMatrizCurricular;
	private String obsHistoricoIntegralizado;	
	private String sistemaAvaliacao;
	private String perfilProfissional;
	private String competenciaProfissional;
	private String enade;
	private String dataPorExtenso;
	private String razaoSocial;
	private String credenciamentoPortaria;
	private String dataPublicacaoDoEmpresa;
	private String tituloCurso;
	private String titulacaoCurso;
	private String tituloMonografia;
	private String notaMonografia;
	// private String baseLegal;
	private Date dataInicioTurma;
	private Date dataExpedicaoDiploma;
	private Boolean curriculoIntegralizado;
	public Integer cargaHorariaCursada;
	public Integer cargaHorariaDisciplinaCursada;
	public Integer creditoCursado;
	private Integer creditoCumprido;
	private String cargaHorariaRealizadaAtividadeComplementar;
	private String cargaHorariaObrigatoriaAtividadeComplementar;
	private Integer cargaHorariaRealizadaEstagio;
	private Integer cargaHorariaObrigatoriaEstagio;
	private Integer cargaHorariaDisciplinaForaGrade;
	private Integer cargaHorariaCursadaDisciplinaForaGrade;
	private Integer cargaHorariaComplementacaoCargaHoraria;
	private String periodicidadeCurso;
	private String instituicaoIngresso;
	private String cidadeIngresso;
	private String estadoIngresso;
	private String textoCertidaoEstudo;		

	private Double notaEnem;

	private String dataPrimeiroReconhecimento;
	private String primeiroReconhecimento;

	// Data matricula utilizada no layout 5 da geração do histórico escolar
	private Date dataMatricula;
	private String credenciamento;
	private List<DocumetacaoMatriculaVO> listaDocumentacaoPendente;
	private List<HistoricoAlunoDisciplinaRelVO> listaDisciplinasACursar;
	private Boolean apresentarTopoRelatorio;
	// Utilizado no relatorio: Mapa do aluno
	private SituacaoFinanceiraAlunoRelVO situacaoFinanceiraAluno;
	// Utilizado no relatorio: Mapa do aluno
	private List<EmprestimoFiltroRelVO> livrosEmprestados;
	private Boolean apresentarDisciplinasCursadas;
	private Boolean apresentarDisciplinasNaoCursadas;
	private Boolean apresentarDocumentacaoPendente;
	private Boolean apresentarLivrosEmprestados;
	private String cursoAnterior;
	private String estabelecimentoAnterior;
	private String enderecoEstabelecimentoAnterior;
	private String nomeExpedicaoDiploma;
	private String areaConhecimentoCurso;

    private String cursoOrigem;
    private String nomeCidadeOrigem;
    private String estadoCidadeOrigem;
    private String legendaSituacaoHistorico;
    private String legendaTitulacaoProfessor;
    
    //Transiet utilizado no relatório
    private String dataEmissaoHistorico;
    private String dataInicioCurso;
    private String dataConclusaoCurso;
    private Integer cargaHorariaDisciplinaObrigatoria;
    private Integer cargaHorariaDisciplinaOptativaSerCumprida;
	private Integer cargaHorariaDisciplinaOptativaCursada;
    
    private String nomeCursoGraduacao;
    private String instituicaoCursoGraduacao;
    private String cidadeCursoGraduacao;
    private String estadoCursoGraduacao;
    private String anoConclusaoCursoGraduacao;
    private String modalidadeConclusaoCursoGraduacao;
    private String orientadorMonografia;
    private String titulacaoOrientadorMonografia;
    private Integer cargaHorariaMonografia;
    
    private FuncionarioVO funcionarioTerciarioVO;
	private String tituloFuncionarioTerciario;
	private CargoVO cargoFuncionarioTerciario;
	private String anoSemestreIntegralizouMatrizCurricular;
	private String grauCurso;
	private String dataProcessoSeletivo;
	
	private String nomeUnidadeEnsino;	private String cnpjMantenedora;
    private String unidadeCertificadora;
    private String  cnpjUnidadeCertificadora;
    private Integer codigoIESUnidadeCertificadora;	// private String observacaoComplementar;
	private String nivelEducacionalCurso;
	
    private String proficienciaLinguaEstrangeira;
    private String situacaoProficienciaLinguaEstrangeira;
    private String anoSemestreConclusaoEnsMedio;
    private Integer  cargaHorariaDisciplinaEstagioExigida;
    private Integer  cargaHorariaDisciplinaEstagioRealizada;
    private String dataExpedicaoDiplomaString;
    private Double cargaHorariaMatriculado;
    private Double cargaHorariaReprovado;
    private Double cargaHorariaAprovado;
    private Double percentualIntegralizacaoCurricular;
	private Boolean trazerTodosProfessoresDisciplinas;  
	private String anoConclusao;
	private String semestreConclusao;
    private GradeCurricularVO gradeCurricularVO;
    private Boolean gradeCurricularAtual;
    
    private Integer cargaHorariaTotalDisciplina;
    private Integer cargaHorariaTotalEstagio;
    private Integer cargaHorariaTotalTcc;
    private Integer cargaHorariaAprovadaDisciplina;
    private Integer cargaHorariaAprovadaEstagio;
    private Integer cargaHorariaAprovadaTcc;
    private Integer cargaHorariaPendenteDisciplina;
    private Integer cargaHorariaPendenteEstagio;
    private Integer cargaHorariaPendenteTcc;
    private Boolean existeDisplinasEstagio;
    private Boolean existeComponentesEstagio;
	private String anoSemestreConclusaoCurso;
	private ObservacaoComplementarDiplomaVO observacaoComplementarDiploma1;
	private ObservacaoComplementarDiplomaVO observacaoComplementarDiploma2;
	private ObservacaoComplementarDiplomaVO observacaoComplementarDiploma3;
	private List<ConfiguracaoAcademicoNotaConceitoVO> listaConfiguracaoAcademicoNotaConceitoVO;
	private ConsistirException consistirException;
	private String cargaHorariaUtilizar;

	public HistoricoAlunoRelVO() {
		setMatricula("");
		setNome("");
		setSexo("");
		setDataNasc("");
		setNacionalidade("");
		setNaturalidade("");
		setUf("");
		setRg("");
		setOrgaoExpedidor("");
		setEstadoEmissaoRg("");
		setCpf("");
		setDocMilitar("");
		setTituloEleitor("");
		setConclusaoEnsMedio("");
		setCidadeConclusaoEnsMedio("");
		setEstadoConclusaoEnsMedio("");
		setAnoConclusaoEnsMedio("");
		setSemestreConclusaoEnsMedio("");
		setNomeCurso("");
		setHabilitacao("");
		setDataReconhecimento("");
		setReconhecimento("");
		setPublicacaoDO("");
		setConclusaoCurso("");
		setColacaoGrau("");
		setExpedicaoDiploma("");
		setChExigida("");
		setChCumprida("");
		setFormaIngresso("");
		setAnoIngresso("");
		setSemestreIngresso("");
		setDiscProcSeletivo("");
		setNomeDisciplina("");
		setNomeMatriz("");
		setTitulacaoCurso("");
		setSemestre("");
		setAnoSemstre("");
		setChDisciplina("");
		setCrDisciplina("");
		setFreqDisciplina("");
		setMediaFinal("");
		setSituacaoFinal("");
		setMantenedora("");
		setMantida("");
		setCidade("");
		setEndereco("");
		setCaixaPostal("");
		setBairro("");
		setCep("");
		setEstado("");
		setFone("");
		setSite("");
		setCnpj("");
		setInscEstadual("");
		setCidadeIngresso("");
		setEstadoIngresso("");
		
		// setObservacaoComplementar("");
	}

	public HistoricoAlunoRelVO getClone() throws Exception {
		return (HistoricoAlunoRelVO) super.clone();
	}

	/**
	 * @return the matricula
	 */
	public String getMatricula() {
		return matricula;
	}

	/**
	 * @param matricula
	 *            the matricula to set
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the dataNasc
	 */
	public String getDataNasc() {
		return dataNasc;
	}

	/**
	 * @param dataNasc
	 *            the dataNasc to set
	 */
	public void setDataNasc(String dataNasc) {
		this.dataNasc = dataNasc;
	}

	/**
	 * @return the nacionalidade
	 */
	public String getNacionalidade() {
		return nacionalidade;
	}

	/**
	 * @param nacionalidade
	 *            the nacionalidade to set
	 */
	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	/**
	 * @return the naturalidade
	 */
	public String getNaturalidade() {
		return naturalidade;
	}

	/**
	 * @param naturalidade
	 *            the naturalidade to set
	 */
	public void setNaturalidade(String naturalidade) {
		this.naturalidade = naturalidade;
	}

	/**
	 * @return the uf
	 */
	public String getUf() {
		return uf;
	}

	/**
	 * @param uf
	 *            the uf to set
	 */
	public void setUf(String uf) {
		this.uf = uf;
	}

	/**
	 * @return the orgaoExpedidor
	 */
	public String getOrgaoExpedidor() {
		return orgaoExpedidor;
	}

	/**
	 * @param orgaoExpedidor
	 *            the orgaoExpedidor to set
	 */
	public void setOrgaoExpedidor(String orgaoExpedidor) {
		this.orgaoExpedidor = orgaoExpedidor;
	}

	/**
	 * @return the estadoEmissaoRg
	 */
	public String getEstadoEmissaoRg() {
		return estadoEmissaoRg;
	}

	/**
	 * @param estadoEmissaoRg
	 *            the estadoEmissaoRg to set
	 */
	public void setEstadoEmissaoRg(String estadoEmissaoRg) {
		this.estadoEmissaoRg = estadoEmissaoRg;
	}

	/**
	 * @return the cpf
	 */
	public String getCpf() {
		return cpf;
	}

	/**
	 * @param cpf
	 *            the cpf to set
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	/**
	 * @return the docMilitar
	 */
	public String getDocMilitar() {
		return docMilitar;
	}

	/**
	 * @param docMilitar
	 *            the docMilitar to set
	 */
	public void setDocMilitar(String docMilitar) {
		this.docMilitar = docMilitar;
	}

	/**
	 * @return the tituloEleitor
	 */
	public String getTituloEleitor() {
		return tituloEleitor;
	}

	/**
	 * @param tituloEleitor
	 *            the tituloEleitor to set
	 */
	public void setTituloEleitor(String tituloEleitor) {
		this.tituloEleitor = tituloEleitor;
	}

	/**
	 * @return the conclusaoEnsMedio
	 */
	public String getConclusaoEnsMedio() {
		return conclusaoEnsMedio;
	}

	/**
	 * @param conclusaoEnsMedio
	 *            the conclusaoEnsMedio to set
	 */
	public void setConclusaoEnsMedio(String conclusaoEnsMedio) {
		this.conclusaoEnsMedio = conclusaoEnsMedio;
	}

	/**
	 * @return the anoConclusaoEnsMedio
	 */
	public String getAnoConclusaoEnsMedio() {
		return anoConclusaoEnsMedio;
	}

	/**
	 * @param anoConclusaoEnsMedio
	 *            the anoConclusaoEnsMedio to set
	 */
	public void setAnoConclusaoEnsMedio(String anoConclusaoEnsMedio) {
		this.anoConclusaoEnsMedio = anoConclusaoEnsMedio;
	}

	public String getSemestreConclusaoEnsMedio() {
		return semestreConclusaoEnsMedio;
	}
	
	public void setSemestreConclusaoEnsMedio(String semestreConclusaoEnsMedio) {
		this.semestreConclusaoEnsMedio = semestreConclusaoEnsMedio;
	}
	
	/**
	 * @return the nomeCurso
	 */
	public String getNomeCurso() {
		return nomeCurso;
	}

	/**
	 * @param nomeCurso
	 *            the nomeCurso to set
	 */
	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	/**
	 * @return the habilitacao
	 */
	public String getHabilitacao() {
		return habilitacao;
	}

	/**
	 * @param habilitacao
	 *            the habilitacao to set
	 */
	public void setHabilitacao(String habilitacao) {
		this.habilitacao = habilitacao;
	}

	/**
	 * @return the autorizacao
	 */
	public String getDataReconhecimento() {
		return dataReconhecimento;
	}

	/**
	 * @param autorizacao
	 *            the autorizacao to set
	 */
	public void setDataReconhecimento(String dataReconhecimento) {
		this.dataReconhecimento = dataReconhecimento;
	}
	
	public String getDataReconhecimentoExtenso() throws ParseException {
		if (Uteis.isAtributoPreenchido(getDataReconhecimento())) {
			Date data = UteisData.getData(getDataReconhecimento());
			return UteisData.getDataPorExtenso(data);
		}
		return "";
	}
	
	public String getDataPublicacaoDoEmpresaExtenso() throws ParseException {
		if (Uteis.isAtributoPreenchido(getDataPublicacaoDoEmpresa())) {
			Date data = UteisData.getData(getDataPublicacaoDoEmpresa());
			return UteisData.getDataPorExtenso(data);
		}
		return "";
	}

	/**
	 * @return the reconhecimento
	 */
	public String getReconhecimento() {
		return reconhecimento;
	}

	/**
	 * @param reconhecimento
	 *            the reconhecimento to set
	 */
	public void setReconhecimento(String reconhecimento) {
		this.reconhecimento = reconhecimento;
	}
	
	/**
	 * @return the conclusaoCurso
	 */
	public String getConclusaoCurso() {
		return conclusaoCurso;
	}

	/**
	 * @param conclusaoCurso
	 *            the conclusaoCurso to set
	 */
	public void setConclusaoCurso(String conclusaoCurso) {
		this.conclusaoCurso = conclusaoCurso;
	}

	/**
	 * @return the colacaoGrau
	 */
	public String getColacaoGrau() {
		return colacaoGrau;
	}

	/**
	 * @param colacaoGrau
	 *            the colacaoGrau to set
	 */
	public void setColacaoGrau(String colacaoGrau) {
		this.colacaoGrau = colacaoGrau;
	}

	/**
	 * @return the expedicaoDiploma
	 */
	public String getExpedicaoDiploma() {
		return expedicaoDiploma;
	}

	/**
	 * @param expedicaoDiploma
	 *            the expedicaoDiploma to set
	 */
	public void setExpedicaoDiploma(String expedicaoDiploma) {
		this.expedicaoDiploma = expedicaoDiploma;
	}

	/**
	 * @return the chExigida
	 */
	public String getChExigida() {
		return chExigida;
	}

	/**
	 * @param chExigida
	 *            the chExigida to set
	 */
	public void setChExigida(String chExigida) {
		this.chExigida = chExigida;
	}

	/**
	 * @return the chCumprida
	 */
	public String getChCumprida() {
		return chCumprida;
	}

	/**
	 * @param chCumprida
	 *            the chCumprida to set
	 */
	public void setChCumprida(String chCumprida) {
		this.chCumprida = chCumprida;
	}

	/**
	 * @return the formaIngresso
	 */
	public String getFormaIngresso() {
		return formaIngresso;
	}

	/**
	 * @param formaIngresso
	 *            the formaIngresso to set
	 */
	public void setFormaIngresso(String formaIngresso) {
		this.formaIngresso = formaIngresso;
	}

	/**
	 * @return the anoIngresso
	 */
	public String getAnoIngresso() {
		return anoIngresso;
	}

	/**
	 * @param anoIngresso
	 *            the anoIngresso to set
	 */
	public void setAnoIngresso(String anoIngresso) {
		this.anoIngresso = anoIngresso;
	}

	/**
	 * @return the semestreIngresso
	 */
	public String getSemestreIngresso() {
		return semestreIngresso;
	}

	/**
	 * @param semestreIngresso
	 *            the semestreIngresso to set
	 */
	public void setSemestreIngresso(String semestreIngresso) {
		this.semestreIngresso = semestreIngresso;
	}

	/**
	 * @return the discProcSeletivo
	 */
	public String getDiscProcSeletivo() {
		if (discProcSeletivo == null) {
			discProcSeletivo = "";
		}
		return discProcSeletivo;
	}

	/**
	 * @param discProcSeletivo
	 *            the discProcSeletivo to set
	 */
	public void setDiscProcSeletivo(String discProcSeletivo) {
		this.discProcSeletivo = discProcSeletivo;
	}

	/**
	 * @return the nomeDisciplina
	 */
	public String getNomeDisciplina() {
		return nomeDisciplina;
	}

	/**
	 * @param nomeDisciplina
	 *            the nomeDisciplina to set
	 */
	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}

	/**
	 * @return the semestre
	 */
	public String getSemestre() {
		return semestre;
	}

	/**
	 * @param semestre
	 *            the semestre to set
	 */
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	/**
	 * @return the anoSemstre
	 */
	public String getAnoSemstre() {
		return anoSemstre;
	}

	/**
	 * @param anoSemstre
	 *            the anoSemstre to set
	 */
	public void setAnoSemstre(String anoSemstre) {
		this.anoSemstre = anoSemstre;
	}

	/**
	 * @return the chDisciplina
	 */
	public String getChDisciplina() {
		return chDisciplina;
	}

	/**
	 * @param chDisciplina
	 *            the chDisciplina to set
	 */
	public void setChDisciplina(String chDisciplina) {
		this.chDisciplina = chDisciplina;
	}

	/**
	 * @return the mediaFinal
	 */
	public String getMediaFinal() {
		if (mediaFinal == null) {
			mediaFinal = "";
		}
		return mediaFinal;
	}

	/**
	 * @param mediaFinal
	 *            the mediaFinal to set
	 */
	public void setMediaFinal(String mediaFinal) {
		this.mediaFinal = mediaFinal;
	}

	/**
	 * @return the situacaoFinal
	 */
	public String getSituacaoFinal() {
		return situacaoFinal;
	}

	/**
	 * @param situacaoFinal
	 *            the situacaoFinal to set
	 */
	public void setSituacaoFinal(String situacaoFinal) {
		this.situacaoFinal = situacaoFinal;
	}

	/**
	 * @return the rg
	 */
	public String getRg() {
		return rg;
	}

	/**
	 * @param rg
	 *            the rg to set
	 */
	public void setRg(String rg) {
		this.rg = rg;
	}

	/**
	 * @return the publicacaoDO
	 */
	public String getPublicacaoDO() {
		if (publicacaoDO == null) {
			publicacaoDO = "";
		}
		return publicacaoDO;
	}

	/**
	 * @param publicacaoDO
	 *            the publicacaoDO to set
	 */
	public void setPublicacaoDO(String publicacaoDO) {
		this.publicacaoDO = publicacaoDO;
	}
	
	public String getPublicacaoDOExtenso() throws ParseException {
		if (Uteis.isAtributoPreenchido(getPublicacaoDO())) {
			Date data = UteisData.getData(getPublicacaoDO());
			return UteisData.getDataPorExtenso(data);
		}
		return "";
	}
	
	

	/**
	 * @return the crDisciplina
	 */
	public String getCrDisciplina() {
		return crDisciplina;
	}

	/**
	 * @param crDisciplina
	 *            the crDisciplina to set
	 */
	public void setCrDisciplina(String crDisciplina) {
		this.crDisciplina = crDisciplina;
	}

	/**
	 * @return the freqDisciplina
	 */
	public String getFreqDisciplina() {
		return freqDisciplina;
	}

	/**
	 * @param freqDisciplina
	 *            the freqDisciplina to set
	 */
	public void setFreqDisciplina(String freqDisciplina) {
		this.freqDisciplina = freqDisciplina;
	}

	/**
	 * @return the sexo
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * @param sexo
	 *            the sexo to set
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	/**
	 * @return the mantenedora
	 */
	public String getMantenedora() {
		return mantenedora;
	}

	/**
	 * @param mantenedora
	 *            the mantenedora to set
	 */
	public void setMantenedora(String mantenedora) {
		this.mantenedora = mantenedora;
	}

	/**
	 * @return the mantida
	 */
	public String getMantida() {
		return mantida;
	}

	/**
	 * @param mantida
	 *            the mantida to set
	 */
	public void setMantida(String mantida) {
		this.mantida = mantida;
	}

	/**
	 * @return the cidade
	 */
	public String getCidade() {
		return cidade;
	}

	/**
	 * @param cidade
	 *            the cidade to set
	 */
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	/**
	 * @return the endereco
	 */
	public String getEndereco() {
		return endereco;
	}

	/**
	 * @param endereco
	 *            the endereco to set
	 */
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	/**
	 * @return the bairro
	 */
	public String getBairro() {
		return bairro;
	}

	/**
	 * @param bairro
	 *            the bairro to set
	 */
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	/**
	 * @return the cep
	 */
	public String getCep() {
		return cep;
	}

	/**
	 * @param cep
	 *            the cep to set
	 */
	public void setCep(String cep) {
		this.cep = cep;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 *            the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the fone
	 */
	public String getFone() {
		return fone;
	}

	/**
	 * @param fone
	 *            the fone to set
	 */
	public void setFone(String fone) {
		this.fone = fone;
	}

	/**
	 * @return the email
	 */
	public String getSite() {
		return site;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setSite(String site) {
		this.site = site;
	}

	/**
	 * @return the cidadeConclusaoEnsMedio
	 */
	public String getCidadeConclusaoEnsMedio() {
		return cidadeConclusaoEnsMedio;
	}

	/**
	 * @param cidadeConclusaoEnsMedio
	 *            the cidadeConclusaoEnsMedio to set
	 */
	public void setCidadeConclusaoEnsMedio(String cidadeConclusaoEnsMedio) {
		this.cidadeConclusaoEnsMedio = cidadeConclusaoEnsMedio;
	}

	/**
	 * @return the estadoConclusaoEnsMedio
	 */
	public String getEstadoConclusaoEnsMedio() {
		return estadoConclusaoEnsMedio;
	}

	/**
	 * @param estadoConclusaoEnsMedio
	 *            the estadoConclusaoEnsMedio to set
	 */
	public void setEstadoConclusaoEnsMedio(String estadoConclusaoEnsMedio) {
		this.estadoConclusaoEnsMedio = estadoConclusaoEnsMedio;
	}

	public JRDataSource getHistoricoAlunoDisciplinaRelVOs() {		
		
		return new JRBeanArrayDataSource(getListaHistoricoAlunoDisciplinaRelVOs().toArray());
	}
	
	public JRDataSource getHistoricoAlunoDisciplinaTccRelVOs() {
		return new JRBeanArrayDataSource(getListaHistoricoAlunoDisciplinaTccRelVOs().toArray());
	}
	
	public JRDataSource getHistoricoAlunoDisciplinaEstagioRelVOs() {
		return new JRBeanArrayDataSource(getListaHistoricoAlunoDisciplinaEstagioRelVOs().toArray());		

	}
	
	public JRDataSource getHistoricoAlunoComponenteEstagioRelVOs() {
		return new JRBeanArrayDataSource(getListaHistoricoAlunoComponenteEstagioRelVOs().toArray());
		
	}
	
	public JRDataSource getHistoricoAlunoDisciplinaForaGradeRelVOs() {
		return new JRBeanArrayDataSource(getListaHistoricoAlunoDisciplinaForaGradeRelVOs().toArray());
	}
	
	public boolean getExibirDisciplinasForaGrade() {
		return !getListaHistoricoAlunoDisciplinaForaGradeRelVOs().isEmpty();
	}

	public List<HistoricoAlunoDisciplinaRelVO> getListaHistoricoAlunoDisciplinaRelVOs() {
		if (historicoAlunoDisciplinaRelVOs == null) {
			historicoAlunoDisciplinaRelVOs = new ArrayList<HistoricoAlunoDisciplinaRelVO>(0);
		}
		return historicoAlunoDisciplinaRelVOs;
	}
	
	public void setHistoricoAlunoDisciplinaRelVOs(List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaRelVOs) {
		this.historicoAlunoDisciplinaRelVOs = historicoAlunoDisciplinaRelVOs;
	}
	
	public List<HistoricoAlunoDisciplinaRelVO> getListaHistoricoAlunoDisciplinaForaGradeRelVOs() {
		if (historicoAlunoDisciplinaForaGradeRelVOs == null) {
			historicoAlunoDisciplinaForaGradeRelVOs = new ArrayList<HistoricoAlunoDisciplinaRelVO>(0);
		}
		return historicoAlunoDisciplinaForaGradeRelVOs;
	}
	
	public void setHistoricoAlunoDisciplinaForaGradeRelVOs(List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaForaGradeRelVOs) {
		this.historicoAlunoDisciplinaForaGradeRelVOs = historicoAlunoDisciplinaForaGradeRelVOs;
	}
	
	public JRDataSource getHistoricoAlunoAtividadeComplementarRelVOs() {
		return new JRBeanArrayDataSource(getListaHistoricoAlunoAtividadeComplementarRelVOs().toArray());
	}

	public List<HistoricoAlunoAtividadeComplementarRelVO> getListaHistoricoAlunoAtividadeComplementarRelVOs() {
		if (historicoAlunoAtividadeComplementarRelVOs == null) {
			historicoAlunoAtividadeComplementarRelVOs = new ArrayList<HistoricoAlunoAtividadeComplementarRelVO>(0);
		}
		return historicoAlunoAtividadeComplementarRelVOs;
	}
	
	public boolean getApresentarHistoricoAlunoAtividadeComplementarRelVOs() {
		return getListaHistoricoAlunoAtividadeComplementarRelVOs().size() > 0;
	}

	public void setHistoricoAlunoAtividadeComplmentarRelVOs(List<HistoricoAlunoAtividadeComplementarRelVO> historicoAlunoAtividadeComplementarRelVOs) {
		this.historicoAlunoAtividadeComplementarRelVOs = historicoAlunoAtividadeComplementarRelVOs;
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

	public String getObsHistorico() {
		if (obsHistorico == null) {
			obsHistorico = "";
		}
		return obsHistorico;
	}

	public void setObsHistorico(String obsHistorico) {
		this.obsHistorico = obsHistorico;
	}

	public String getEnade() {
		if (enade == null) {
			enade = "";
		}
		return enade;
	}

	public void setEnade(String enade) {
		this.enade = enade;
	}

	public void setDataPorExtenso(String dataPorExtenso) {
		this.dataPorExtenso = dataPorExtenso;
	}

	public String getDataPorExtenso() {
		return dataPorExtenso;
	}

	public String getRazaoSocial() {
		if (razaoSocial == null) {
			razaoSocial = "";
		}
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCredenciamentoPortaria() {
		if (credenciamentoPortaria == null) {
			credenciamentoPortaria = "";
		}
		return credenciamentoPortaria;
	}

	public void setCredenciamentoPortaria(String credenciamentoPortaria) {
		this.credenciamentoPortaria = credenciamentoPortaria;
	}

	public String getDataPublicacaoDoEmpresa() {
		if (dataPublicacaoDoEmpresa == null) {
			dataPublicacaoDoEmpresa = "";
		}
		return dataPublicacaoDoEmpresa;
	}

	public void setDataPublicacaoDoEmpresa(String dataPublicacaoDoEmpresa) {
		this.dataPublicacaoDoEmpresa = dataPublicacaoDoEmpresa;
	}

	public String getTituloCurso() {
		if (tituloCurso == null) {
			tituloCurso = "";
		}
		return tituloCurso;
	}

	public void setTituloCurso(String tituloCurso) {
		this.tituloCurso = tituloCurso;
	}

	// public String getBaseLegal() {
	// if (baseLegal == null) {
	// baseLegal = "";
	// }
	// return baseLegal;
	// }
	//
	// public void setBaseLegal(String baseLegal) {
	// this.baseLegal = baseLegal;
	// }
	/**
	 * @return the cidadeDataAtual
	 */
	public String getCidadeDataAtual() {
		if (cidadeDataAtual == null) {
			cidadeDataAtual = "";
		}
		return cidadeDataAtual;
	}

	/**
	 * @param cidadeDataAtual
	 *            the cidadeDataAtual to set
	 */
	public void setCidadeDataAtual(String cidadeDataAtual) {
		this.cidadeDataAtual = cidadeDataAtual;
	}

	/**
	 * @return the periodoCursado
	 */
	public String getPeriodoCursado() {
		if (periodoCursado == null) {
			periodoCursado = "";
		}
		return periodoCursado;
	}

	/**
	 * @param periodoCursado
	 *            the periodoCursado to set
	 */
	public void setPeriodoCursado(String periodoCursado) {
		this.periodoCursado = periodoCursado;
	}

	/**
	 * @return the complemento
	 */
	public String getComplemento() {
		if (complemento == null) {
			complemento = "";
		}
		return complemento;
	}

	public String getDataAtual() {
		if (dataAtual == null) {
			dataAtual = "";
		}
		return dataAtual;
	}

	public void setDataAtual(String dataAtual) {
		this.dataAtual = dataAtual;
	}

	public Date getDataInicioTurma() {
		return dataInicioTurma;
	}

	public void setDataInicioTurma(Date dataInicioTurma) {
		this.dataInicioTurma = dataInicioTurma;
	}

	/**
	 * @return the tituloMonografia
	 */
	public String getTituloMonografia() {
		if (tituloMonografia == null) {
			tituloMonografia = "";
		}
		return tituloMonografia;
	}

	/**
	 * @param tituloMonografia
	 *            the tituloMonografia to set
	 */
	public void setTituloMonografia(String tituloMonografia) {
		this.tituloMonografia = tituloMonografia;
	}

	/**
	 * @return the notaMonografia
	 */
	public String getNotaMonografia() {
		if (notaMonografia == null) {
			notaMonografia = "";
		}
		return notaMonografia;
	}

	/**
	 * @param notaMonografia
	 *            the notaMonografia to set
	 */
	public void setNotaMonografia(String notaMonografia) {
		this.notaMonografia = notaMonografia;
	}

	/**
	 * @return the dataAtualExtenso
	 */
	public String getDataAtualExtenso() {
		if (dataAtualExtenso == null) {
			dataAtualExtenso = "";
		}
		return dataAtualExtenso;
	}

	/**
	 * @param dataAtualExtenso
	 *            the dataAtualExtenso to set
	 */
	public void setDataAtualExtenso(String dataAtualExtenso) {
		this.dataAtualExtenso = dataAtualExtenso;
	}

	public Date getDataExpedicaoDiploma() {
		return dataExpedicaoDiploma;
	}

	public void setDataExpedicaoDiploma(Date dataExpedicaoDiploma) {
		this.dataExpedicaoDiploma = dataExpedicaoDiploma;
	}

	public Boolean getCurriculoIntegralizado() {
		if (curriculoIntegralizado == null) {
			curriculoIntegralizado = Boolean.FALSE;
		}
		return curriculoIntegralizado;
	}

	public void setCurriculoIntegralizado(Boolean curriculoIntegralizado) {
		this.curriculoIntegralizado = curriculoIntegralizado;
	}

	public Boolean getApresentarFrequencia() {
		if (apresentarFrequencia == null) {
			apresentarFrequencia = Boolean.FALSE;
		}
		return apresentarFrequencia;
	}

	public void setApresentarFrequencia(Boolean apresentarFrequencia) {
		this.apresentarFrequencia = apresentarFrequencia;
	}

	public String getNomePai() {
		if (nomePai == null) {
			nomePai = "";
		}
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	public String getNomeMae() {
		if (nomeMae == null) {
			nomeMae = "";
		}
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public List<HistoricoAlunoDisciplinaRelVO> getHistoricoAlunoPeriodoRelVOs() {
		if (historicoAlunoPeriodoRelVOs == null) {
			historicoAlunoPeriodoRelVOs = new ArrayList<HistoricoAlunoDisciplinaRelVO>(0);
		}
		return historicoAlunoPeriodoRelVOs;
	}

	public void setHistoricoAlunoPeriodoRelVOs(List<HistoricoAlunoDisciplinaRelVO> historicoAlunoPeriodoRelVOs) {
		this.historicoAlunoPeriodoRelVOs = historicoAlunoPeriodoRelVOs;
	}

	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCargaHorariaCursada() {
		// cargaHorariaCursada = 0;
		// for(HistoricoAlunoDisciplinaRelVO
		// historicoAlunoDisciplinaRelVO:getListaHistoricoAlunoDisciplinaRelVOs()){
		// if(!historicoAlunoDisciplinaRelVO.getCargaHorariaCursada().equals("--")){
		// cargaHorariaCursada +=
		// Integer.valueOf(historicoAlunoDisciplinaRelVO.getCargaHorariaCursada());
		// }
		// }

		if (cargaHorariaCursada == 0 || cargaHorariaCursada == null) {
			return "--";
		}
		return cargaHorariaCursada.toString();
	}

	public void setCargaHorariaCursada(Integer cargaHorariaCursada) {
		this.cargaHorariaCursada = cargaHorariaCursada;
	}

	public String getMesIngresso() {
		if (mesIngresso == null) {
			mesIngresso = "";
		}
		return mesIngresso;
	}

	public void setMesIngresso(String mesIngresso) {
		this.mesIngresso = mesIngresso;
	}

	public String getNumero() {
		if (numero == null) {
			numero = "";
		}
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDataEmissaoRg() {
		if (dataEmissaoRg == null) {
			dataEmissaoRg = "";
		}
		return dataEmissaoRg;
	}

	public void setDataEmissaoRg(String dataEmissaoRg) {
		this.dataEmissaoRg = dataEmissaoRg;
	}

	public String getCargaHorariaRealizadaAtividadeComplementar() {
		if (cargaHorariaRealizadaAtividadeComplementar == null) {
			cargaHorariaRealizadaAtividadeComplementar = "";
		}
		return cargaHorariaRealizadaAtividadeComplementar;
	}

	public void setCargaHorariaRealizadaAtividadeComplementar(String cargaHorariaRealizadaAtividadeComplementar) {
		this.cargaHorariaRealizadaAtividadeComplementar = cargaHorariaRealizadaAtividadeComplementar;
	}

	public String getCargaHorariaObrigatoriaAtividadeComplementar() {
		if (cargaHorariaObrigatoriaAtividadeComplementar == null) {
			cargaHorariaObrigatoriaAtividadeComplementar = "";
		}
		return cargaHorariaObrigatoriaAtividadeComplementar;
	}

	public void setCargaHorariaObrigatoriaAtividadeComplementar(String cargaHorariaObrigatoriaAtividadeComplementar) {
		this.cargaHorariaObrigatoriaAtividadeComplementar = cargaHorariaObrigatoriaAtividadeComplementar;
	}

	public String getPeriodicidadeCurso() {
		if (periodicidadeCurso == null) {
			periodicidadeCurso = "";
		}
		return periodicidadeCurso;
	}

	public void setPeriodicidadeCurso(String periodicidadeCurso) {
		this.periodicidadeCurso = periodicidadeCurso;
	}

	public String getDataEmissaoDocMilitar() {
		if (dataEmissaoDocMilitar == null) {
			dataEmissaoDocMilitar = "";
		}
		return dataEmissaoDocMilitar;
	}

	public void setDataEmissaoDocMilitar(String dataEmissaoDocMilitar) {
		this.dataEmissaoDocMilitar = dataEmissaoDocMilitar;
	}

	public String getOrgaoExpedidorDocMilitar() {
		if (orgaoExpedidorDocMilitar == null) {
			orgaoExpedidorDocMilitar = "";
		}
		return orgaoExpedidorDocMilitar;
	}

	public void setOrgaoExpedidorDocMilitar(String orgaoExpedidorDocMilitar) {
		this.orgaoExpedidorDocMilitar = orgaoExpedidorDocMilitar;
	}

	public String getDataEmissaoTituloEleitor() {
		if (dataEmissaoTituloEleitor == null) {
			dataEmissaoTituloEleitor = "";
		}
		return dataEmissaoTituloEleitor;
	}

	public void setDataEmissaoTituloEleitor(String dataEmissaoTituloEleitor) {
		this.dataEmissaoTituloEleitor = dataEmissaoTituloEleitor;
	}

	public String getZonaEleitoral() {
		if (zonaEleitoral == null) {
			zonaEleitoral = "";
		}
		return zonaEleitoral;
	}

	public void setZonaEleitoral(String zonaEleitoral) {
		this.zonaEleitoral = zonaEleitoral;
	}

	public Date getDataMatricula() {
		return dataMatricula;
	}

	public void setDataMatricula(Date dataMatricula) {
		this.dataMatricula = dataMatricula;
	}

	public String getCredenciamento() {
		if (credenciamento == null) {
			credenciamento = "";
		}
		return credenciamento;
	}

	public void setCredenciamento(String credenciamento) {
		this.credenciamento = credenciamento;
	}

	public String getChExigidaPeriodoLetivos() {
		if (chExigidaPeriodoLetivos == null) {
			chExigidaPeriodoLetivos = "";
		}
		return chExigidaPeriodoLetivos;
	}

	public void setChExigidaPeriodoLetivos(String chExigidaPeriodoLetivos) {
		this.chExigidaPeriodoLetivos = chExigidaPeriodoLetivos;
	}

	public Integer getCargaHorariaRealizadaEstagio() {
		if (cargaHorariaRealizadaEstagio == null) {
			cargaHorariaRealizadaEstagio = 0;
		}
		return cargaHorariaRealizadaEstagio;
	}

	public void setCargaHorariaRealizadaEstagio(Integer cargaHorariaRealizadaEstagio) {
		this.cargaHorariaRealizadaEstagio = cargaHorariaRealizadaEstagio;
	}

	public Integer getCargaHorariaObrigatoriaEstagio() {
		if (cargaHorariaObrigatoriaEstagio == null) {
			cargaHorariaObrigatoriaEstagio = 0;
		}
		return cargaHorariaObrigatoriaEstagio;
	}

	public void setCargaHorariaObrigatoriaEstagio(Integer cargaHorariaObrigatoriaEstagio) {
		this.cargaHorariaObrigatoriaEstagio = cargaHorariaObrigatoriaEstagio;
	}

	public Integer getCargaHorariaDisciplinaForaGrade() {
		if (cargaHorariaDisciplinaForaGrade == null) {
			cargaHorariaDisciplinaForaGrade = 0;
		}
		return cargaHorariaDisciplinaForaGrade;
	}

	public void setCargaHorariaDisciplinaForaGrade(Integer cargaHorariaDisciplinaForaGrade) {
		this.cargaHorariaDisciplinaForaGrade = cargaHorariaDisciplinaForaGrade;
	}

	public Integer getCargaHorariaCursadaDisciplinaForaGrade() {
		if (cargaHorariaCursadaDisciplinaForaGrade == null) {
			cargaHorariaCursadaDisciplinaForaGrade = 0;
		}
		return cargaHorariaCursadaDisciplinaForaGrade;
	}

	public void setCargaHorariaCursadaDisciplinaForaGrade(Integer cargaHorariaCursadaDisciplinaForaGrade) {
		this.cargaHorariaCursadaDisciplinaForaGrade = cargaHorariaCursadaDisciplinaForaGrade;
	}

	public Double getTotalPontoProcSeletivo() {
		if (totalPontoProcSeletivo == null) {
			totalPontoProcSeletivo = 0.0;
		}
		return totalPontoProcSeletivo;
	}

	public void setTotalPontoProcSeletivo(Double totalPontoProcSeletivo) {
		this.totalPontoProcSeletivo = totalPontoProcSeletivo;
	}

	public String getSistemaAvaliacao() {
		if (sistemaAvaliacao == null) {
			sistemaAvaliacao = "";
		}
		return sistemaAvaliacao;
	}

	public void setSistemaAvaliacao(String sistemaAvaliacao) {
		this.sistemaAvaliacao = sistemaAvaliacao;
	}

	public String getCompetenciaProfissional() {
		if (competenciaProfissional == null) {
			competenciaProfissional = "";
		}
		return competenciaProfissional;
	}

	public void setCompetenciaProfissional(String competenciaProfissional) {
		this.competenciaProfissional = competenciaProfissional;
	}

	public String getInstituicaoIngresso() {
		if (instituicaoIngresso == null) {
			instituicaoIngresso = "";
		}
		return instituicaoIngresso;
	}

	public void setInstituicaoIngresso(String instituicaoIngresso) {
		this.instituicaoIngresso = instituicaoIngresso;
	}

	public Double getNotaEnem() {
		if (notaEnem == null) {
			notaEnem = 0.0;
		}
		return notaEnem;
	}

	public void setNotaEnem(Double notaEnem) {
		this.notaEnem = notaEnem;
	}

	public List<DocumetacaoMatriculaVO> getListaDocumentacaoPendente() {
		if (listaDocumentacaoPendente == null) {
			listaDocumentacaoPendente = new ArrayList<DocumetacaoMatriculaVO>(0);
		}
		return listaDocumentacaoPendente;
	}

	public void setListaDocumentacaoPendente(List<DocumetacaoMatriculaVO> listaDocumentacaoPendente) {
		this.listaDocumentacaoPendente = listaDocumentacaoPendente;
	}

	public JRDataSource getListaDocumentacaoPendenteIntegralizacaoCurricular() {
		return new JRBeanArrayDataSource(getListaDocumentacaoPendente().toArray());
	}

	public List<HistoricoAlunoDisciplinaRelVO> getListaDisciplinasACursar() {
		if (listaDisciplinasACursar == null) {
			listaDisciplinasACursar = new ArrayList<HistoricoAlunoDisciplinaRelVO>(0);
		}
		return listaDisciplinasACursar;
	}

	public void setListaDisciplinasACursar(List<HistoricoAlunoDisciplinaRelVO> listaDisciplinasACursar) {
		this.listaDisciplinasACursar = listaDisciplinasACursar;
	}

	public JRDataSource getListaDisciplinasNaoCursadas() {
		return new JRBeanArrayDataSource(getListaDisciplinasACursar().toArray());
	}

	public Boolean getApresentarTopoRelatorio() {
		if (apresentarTopoRelatorio == null) {
			apresentarTopoRelatorio = Boolean.FALSE;
		}
		return apresentarTopoRelatorio;
	}

	public void setApresentarTopoRelatorio(Boolean apresentarTopoRelatorio) {
		this.apresentarTopoRelatorio = apresentarTopoRelatorio;
	}

	public String getDataPrimeiroReconhecimento() {
		if (dataPrimeiroReconhecimento == null) {
			dataPrimeiroReconhecimento = "";
		}
		return dataPrimeiroReconhecimento;
	}

	public void setDataPrimeiroReconhecimento(String dataPrimeiroReconhecimento) {
		this.dataPrimeiroReconhecimento = dataPrimeiroReconhecimento;
	}
	
	public String getDataPrimeiroReconhecimentoExtenso() throws ParseException {
		if (Uteis.isAtributoPreenchido(getDataPrimeiroReconhecimento())) {
			Date data = UteisData.getData(getDataPrimeiroReconhecimento());
			return UteisData.getDataPorExtenso(data);
		}
		return "";
	}

	public String getPrimeiroReconhecimento() {
		if (primeiroReconhecimento == null) {
			primeiroReconhecimento = "";
		}
		return primeiroReconhecimento;
	}

	public void setPrimeiroReconhecimento(String primeiroReconhecimento) {
		this.primeiroReconhecimento = primeiroReconhecimento;
	}

	public Integer getCargaHorariaComplementacaoCargaHoraria() {
		if (cargaHorariaComplementacaoCargaHoraria == null) {
			cargaHorariaComplementacaoCargaHoraria = 0;
		}
		return cargaHorariaComplementacaoCargaHoraria;
	}

	public void setCargaHorariaComplementacaoCargaHoraria(Integer cargaHorariaComplementacaoCargaHoraria) {
		this.cargaHorariaComplementacaoCargaHoraria = cargaHorariaComplementacaoCargaHoraria;
	}

	public SituacaoFinanceiraAlunoRelVO getSituacaoFinanceiraAluno() {
		if (situacaoFinanceiraAluno == null) {
			situacaoFinanceiraAluno = new SituacaoFinanceiraAlunoRelVO();
		}
		return situacaoFinanceiraAluno;
	}

	public void setSituacaoFinanceiraAluno(SituacaoFinanceiraAlunoRelVO situacaoFinanceiraAluno) {
		this.situacaoFinanceiraAluno = situacaoFinanceiraAluno;
	}

	public List<EmprestimoFiltroRelVO> getLivrosEmprestados() {
		if (livrosEmprestados == null) {
			livrosEmprestados = new ArrayList<EmprestimoFiltroRelVO>();
		}
		return livrosEmprestados;
	}

	public void setLivrosEmprestados(List<EmprestimoFiltroRelVO> livrosEmprestados) {
		this.livrosEmprestados = livrosEmprestados;
	}

	public JRDataSource getListaLivrosEmprestados() {
		return new JRBeanArrayDataSource(getLivrosEmprestados().toArray());
	}

	public Boolean getApresentarDisciplinasCursadas() {
		if (!getListaHistoricoAlunoDisciplinaRelVOs().isEmpty()) {
			return true;
		}
		return apresentarDisciplinasCursadas;
	}

	public Boolean getApresentarDisciplinasNaoCursadas() {
		if (!getListaDisciplinasACursar().isEmpty()) {
			return true;
		}
		return apresentarDisciplinasNaoCursadas;
	}

	public Boolean getApresentarDocumentacaoPendente() {
		if (!getListaDocumentacaoPendente().isEmpty()) {
			return true;
		}
		return apresentarDocumentacaoPendente;
	}

	public Boolean getApresentarLivrosEmprestados() {
		if (!getLivrosEmprestados().isEmpty()) {
			return true;
		}
		return apresentarLivrosEmprestados;
	}

	public String getTituloFuncionarioPrincipal() {
		if (tituloFuncionarioPrincipal == null) {
			tituloFuncionarioPrincipal = "";
		}
		return tituloFuncionarioPrincipal;
	}

	public void setTituloFuncionarioPrincipal(String tituloFuncionarioPrincipal) {
		this.tituloFuncionarioPrincipal = tituloFuncionarioPrincipal;
	}

	public String getTituloFuncionarioSecundario() {
		if (tituloFuncionarioSecundario == null) {
			tituloFuncionarioSecundario = "";
		}
		return tituloFuncionarioSecundario;
	}

	public void setTituloFuncionarioSecundario(String tituloFuncionarioSecundario) {
		this.tituloFuncionarioSecundario = tituloFuncionarioSecundario;
	}

	public String getCursoAnterior() {
		if (cursoAnterior == null) {
			cursoAnterior = "";
		}
		return cursoAnterior;
	}

	public void setCursoAnterior(String cursoAnterior) {
		this.cursoAnterior = cursoAnterior;
	}

	public String getEstabelecimentoAnterior() {
		if (estabelecimentoAnterior == null) {
			estabelecimentoAnterior = "";
		}
		return estabelecimentoAnterior;
	}

	public void setEstabelecimentoAnterior(String estabelecimentoAnterior) {
		this.estabelecimentoAnterior = estabelecimentoAnterior;
	}

	public String getEnderecoEstabelecimentoAnterior() {
		if (enderecoEstabelecimentoAnterior == null) {
			enderecoEstabelecimentoAnterior = "";
		}
		return enderecoEstabelecimentoAnterior;
	}

	public void setEnderecoEstabelecimentoAnterior(String enderecoEstabelecimentoAnterior) {
		this.enderecoEstabelecimentoAnterior = enderecoEstabelecimentoAnterior;
	}

	/**
	 * @return the nrRegistroInterno
	 */
	public String getNrRegistroInterno() {
		if (nrRegistroInterno == null) {
			nrRegistroInterno = "";
		}
		return nrRegistroInterno;
	}

	/**
	 * @param nrRegistroInterno the nrRegistroInterno to set
	 */
	public void setNrRegistroInterno(String nrRegistroInterno) {
		this.nrRegistroInterno = nrRegistroInterno;
	}

	/**
	 * @param complemento the complemento to set
	 */
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	public String getCaixaPostal() {
		return caixaPostal;
	}

	public void setCaixaPostal(String caixaPostal) {
		this.caixaPostal = caixaPostal;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscEstadual() {
		return inscEstadual;
	}

	public void setInscEstadual(String inscEstadual) {
		this.inscEstadual = inscEstadual;
	}

	public String getNomeExpedicaoDiploma() {
		if(nomeExpedicaoDiploma == null) {
			nomeExpedicaoDiploma = "" ;
		}
		return nomeExpedicaoDiploma;
	}

	public void setNomeExpedicaoDiploma(String nomeExpedicaoDiploma) {
		this.nomeExpedicaoDiploma = nomeExpedicaoDiploma;
	}

	public String getPerfilProfissional() {
		if (perfilProfissional == null) {
			perfilProfissional = "";
		}
		return perfilProfissional;
	}

	public void setPerfilProfissional(String perfilProfissional) {
		this.perfilProfissional = perfilProfissional;
	}

	public Integer getCreditoCursado() {
		if (creditoCursado == null) {
			creditoCursado = 0;
		}
		return creditoCursado;
	}

	public void setCreditoCursado(Integer creditoCursado) {
		this.creditoCursado = creditoCursado;
	}

	public Integer getCreditoCumprido() {
		if (creditoCumprido == null) {
			creditoCumprido = 0;
		}
		return creditoCumprido;
	}

	public void setCreditoCumprido(Integer creditoCumprido) {
		this.creditoCumprido = creditoCumprido;
	}

	public String getInscMunicipal() {
		if (inscMunicipal == null) {
			inscMunicipal = "";
		}
		return inscMunicipal;
	}

	public void setInscMunicipal(String inscMunicipal) {
		this.inscMunicipal = inscMunicipal;
	}

	public Boolean getCursoProfissionalizante() {
		if (cursoProfissionalizante == null) {
			cursoProfissionalizante = false;
		}
		return cursoProfissionalizante;
	}

	public void setCursoProfissionalizante(Boolean cursoProfissionalizante) {
		this.cursoProfissionalizante = cursoProfissionalizante;
	}
	
	public Integer getPrimeiroPeriodoLetivo() {
		if (getHistoricoAlunoPeriodoRelVOs() != null && !getHistoricoAlunoPeriodoRelVOs().isEmpty()) {
			Ordenacao.ordenarLista(getHistoricoAlunoPeriodoRelVOs(), "numeroPeriodoLetivo");
			return getHistoricoAlunoPeriodoRelVOs().get(0).getNumeroPeriodoLetivo();
		}
		return 1;
	}

	public String getAreaConhecimentoCurso() {
		if (areaConhecimentoCurso == null) {
			areaConhecimentoCurso = "";
		}
		return areaConhecimentoCurso;
	}

	public void setAreaConhecimentoCurso(String areaConhecimentoCurso) {
		this.areaConhecimentoCurso = areaConhecimentoCurso;
	}

	public String getObsHistoricoIntegralizado() {
		if (obsHistoricoIntegralizado == null) {
			obsHistoricoIntegralizado = "";
		}
		return obsHistoricoIntegralizado;
	}

	public void setObsHistoricoIntegralizado(String obsHistoricoIntegralizado) {
		this.obsHistoricoIntegralizado = obsHistoricoIntegralizado;
	}

	public String getClassificacaoIngresso() {
		if (classificacaoIngresso == null) {
			classificacaoIngresso = "";
		}
		return classificacaoIngresso;
	}

	public void setClassificacaoIngresso(String classificacaoIngresso) {
		this.classificacaoIngresso = classificacaoIngresso;
	}

    /**
     * @return the cursoOrigem
     */
    public String getCursoOrigem() {
        if (cursoOrigem == null) { 
            cursoOrigem = "";
        }
        return cursoOrigem;
    }

    /**
     * @param cursoOrigem the cursoOrigem to set
     */
    public void setCursoOrigem(String cursoOrigem) {
        this.cursoOrigem = cursoOrigem;
    }

    /**
     * @return the nomeCidadeOrigem
     */
    public String getNomeCidadeOrigem() {
        if (nomeCidadeOrigem == null) { 
            nomeCidadeOrigem = "";
        }
        return nomeCidadeOrigem;
    }

    /**
     * @param nomeCidadeOrigem the nomeCidadeOrigem to set
     */
    public void setNomeCidadeOrigem(String nomeCidadeOrigem) {
        this.nomeCidadeOrigem = nomeCidadeOrigem;
    }

    /**
     * @return the estadoCidadeOrigem
     */
    public String getEstadoCidadeOrigem() {
        if (estadoCidadeOrigem == null) {
            estadoCidadeOrigem = "";
        }
        return estadoCidadeOrigem;
    }

    /**
     * @param estadoCidadeOrigem the estadoCidadeOrigem to set
     */
    public void setEstadoCidadeOrigem(String estadoCidadeOrigem) {
        this.estadoCidadeOrigem = estadoCidadeOrigem;
    }

	public String getTextoCertidaoEstudo() {
		if (textoCertidaoEstudo == null) {
			textoCertidaoEstudo = "";
		}
		return textoCertidaoEstudo;
	}

	public void setTextoCertidaoEstudo(String textoCertidaoEstudo) {
		this.textoCertidaoEstudo = textoCertidaoEstudo;
	}
	
	public String getLocalProcessoSeletivo() {
		if (localProcessoSeletivo == null) {
			localProcessoSeletivo = "";
		}
		return localProcessoSeletivo;
	}

	public void setLocalProcessoSeletivo(String localProcessoSeletivo) {
		this.localProcessoSeletivo = localProcessoSeletivo;
	}

	public String getChGradeCurricular() {
		if (chGradeCurricular == null) {
			chGradeCurricular = "";
		}
		return chGradeCurricular;
	}

	public void setChGradeCurricular(String chGradeCurricular) {
		this.chGradeCurricular = chGradeCurricular;
	}
	
	public String getLegendaSituacaoHistorico() {
		if (legendaSituacaoHistorico == null) {
			legendaSituacaoHistorico = "";
		}
		return legendaSituacaoHistorico;
	}

	public void setLegendaSituacaoHistorico(String legendaSituacaoHistorico) {
		this.legendaSituacaoHistorico = legendaSituacaoHistorico;
	}

	public String getDataEmissaoHistorico() {
		if (dataEmissaoHistorico == null) {
			dataEmissaoHistorico = "";
		}
		return dataEmissaoHistorico;
	}

	public void setDataEmissaoHistorico(String dataEmissaoHistorico) {
		this.dataEmissaoHistorico = dataEmissaoHistorico;
	}

	public String getCidadeIngresso() {
		if (cidadeIngresso == null) {
			cidadeIngresso = "";
		}
		return cidadeIngresso;
	}

	public void setCidadeIngresso(String cidadeIngresso) {
		this.cidadeIngresso = cidadeIngresso;
	}

	public String getEstadoIngresso() {
		if (estadoIngresso == null) {
			estadoIngresso = "";
		}
		return estadoIngresso;
	}

	public void setEstadoIngresso(String estadoIngresso) {
		this.estadoIngresso = estadoIngresso;
	}
	
	public String getDataInicioCurso() {
		if (dataInicioCurso == null) {
			dataInicioCurso = "";
		}
		return dataInicioCurso;
	}
	
	public void setDataInicioCurso(String dataInicioCurso) {
		this.dataInicioCurso = dataInicioCurso;
	}

	public String getDataConclusaoCurso() {
		if (dataConclusaoCurso == null) {
			dataConclusaoCurso = "";
		}
		return dataConclusaoCurso;
	}

	public void setDataConclusaoCurso(String dataConclusaoCurso) {
		this.dataConclusaoCurso = dataConclusaoCurso;
	}
	
	public String getNomeCursoGraduacao() {
		if (nomeCursoGraduacao == null) {
			nomeCursoGraduacao = "";
		}
		return nomeCursoGraduacao;
	}

	public void setNomeCursoGraduacao(String nomeCursoGraduacao) {
		this.nomeCursoGraduacao = nomeCursoGraduacao;
	}

	public String getInstituicaoCursoGraduacao() {
		if (instituicaoCursoGraduacao == null) {
			instituicaoCursoGraduacao = "";
		}
		return instituicaoCursoGraduacao;
	}

	public void setInstituicaoCursoGraduacao(String instituicaoCursoGraduacao) {
		this.instituicaoCursoGraduacao = instituicaoCursoGraduacao;
	}

	public String getCidadeCursoGraduacao() {
		if (cidadeCursoGraduacao == null) {
			cidadeCursoGraduacao = "";
		}
		return cidadeCursoGraduacao;
	}

	public void setCidadeCursoGraduacao(String cidadeCursoGraduacao) {
		this.cidadeCursoGraduacao = cidadeCursoGraduacao;
	}

	public String getAnoConclusaoCursoGraduacao() {
		if (anoConclusaoCursoGraduacao == null) {
			anoConclusaoCursoGraduacao = "";
		}
		return anoConclusaoCursoGraduacao;
	}

	public void setAnoConclusaoCursoGraduacao(String anoConclusaoCursoGraduacao) {
		this.anoConclusaoCursoGraduacao = anoConclusaoCursoGraduacao;
	}

	public String getModalidadeConclusaoCursoGraduacao() {
		if (modalidadeConclusaoCursoGraduacao == null) {
			modalidadeConclusaoCursoGraduacao = "";
		}
		return modalidadeConclusaoCursoGraduacao;
	}

	public void setModalidadeConclusaoCursoGraduacao(String modalidadeConclusaoCursoGraduacao) {
		this.modalidadeConclusaoCursoGraduacao = modalidadeConclusaoCursoGraduacao;
	}

	public String getEstadoCursoGraduacao() {
		if (estadoCursoGraduacao == null) {
			estadoCursoGraduacao = "";
		}
		return estadoCursoGraduacao;
	}

	public void setEstadoCursoGraduacao(String estadoCursoGraduacao) {
		this.estadoCursoGraduacao = estadoCursoGraduacao;
	}

	public String getEnderecoCompletoUnidadeEnsino() {
		return getEndereco() + ",  " + getNumero() + " - " + getBairro() + " - " + getCep() + " " + getCidade() + " - " + getEstado() + " fone: " + getFone();  
	}

	public FuncionarioVO getFuncionarioTerciarioVO() {
		if (funcionarioTerciarioVO == null) {
			funcionarioTerciarioVO = new FuncionarioVO();
		}
		return funcionarioTerciarioVO;
	}

	public void setFuncionarioTerciarioVO(FuncionarioVO funcionarioTerciarioVO) {
		this.funcionarioTerciarioVO = funcionarioTerciarioVO;
	}

	public String getTituloFuncionarioTerciario() {
		if (tituloFuncionarioTerciario == null) {
			tituloFuncionarioTerciario = "";
		}
		return tituloFuncionarioTerciario;
	}

	public void setTituloFuncionarioTerciario(String tituloFuncionarioTerciario) {
		this.tituloFuncionarioTerciario = tituloFuncionarioTerciario;
	}

	public CargoVO getCargoFuncionarioTerciario() {
		if (cargoFuncionarioTerciario == null) {
			cargoFuncionarioTerciario = new CargoVO();
		}
		return cargoFuncionarioTerciario;
	}

	public void setCargoFuncionarioTerciario(CargoVO cargoFuncionarioTerciario) {
		this.cargoFuncionarioTerciario = cargoFuncionarioTerciario;
	}

	public String getOrientadorMonografia() {
		if (orientadorMonografia == null) {
			orientadorMonografia = "";
		}
		return orientadorMonografia;
	}

	public void setOrientadorMonografia(String orientadorMonografia) {
		this.orientadorMonografia = orientadorMonografia;
	}


	public Integer getCargaHorariaDisciplinaObrigatoria() {
		if(cargaHorariaDisciplinaObrigatoria == null) {
			cargaHorariaDisciplinaObrigatoria = 0;
		}

		return cargaHorariaDisciplinaObrigatoria;
	}

	public void setCargaHorariaDisciplinaObrigatoria(Integer cargaHorariaDisciplinaObrigatoria) {
		this.cargaHorariaDisciplinaObrigatoria = cargaHorariaDisciplinaObrigatoria;
	}

	public Integer getCargaHorariaDisciplinaOptativaSerCumprida() {
		if(cargaHorariaDisciplinaOptativaSerCumprida == null) {
			cargaHorariaDisciplinaOptativaSerCumprida = 0;
		}
		return cargaHorariaDisciplinaOptativaSerCumprida;
	}

	public void setCargaHorariaDisciplinaOptativaSerCumprida(Integer cargaHorariaDisciplinaOptativaSerCumprida) {
		this.cargaHorariaDisciplinaOptativaSerCumprida = cargaHorariaDisciplinaOptativaSerCumprida;
	}
	
	public String getAnoSemestreIntegralizouMatrizCurricular() {
		if (anoSemestreIntegralizouMatrizCurricular == null) {
			anoSemestreIntegralizouMatrizCurricular = "";
		}
		return anoSemestreIntegralizouMatrizCurricular;
	}

	public void setAnoSemestreIntegralizouMatrizCurricular(String anoSemestreIntegralizouMatrizCurricular) {
		this.anoSemestreIntegralizouMatrizCurricular = anoSemestreIntegralizouMatrizCurricular;
	}

	public String getNomeMatriz() {
		if (nomeMatriz == null) {
			nomeMatriz = "";
		}
		return nomeMatriz;
	}

	public void setNomeMatriz(String nomeMatriz) {
		this.nomeMatriz = nomeMatriz;
	}

	public String getTitulacaoCurso() {
		if (titulacaoCurso == null) {
			titulacaoCurso = "";
		}
		return titulacaoCurso;
	}

	public void setTitulacaoCurso(String titulacaoCurso) {
		
		this.titulacaoCurso = titulacaoCurso;
	}

	public String getTotalcargahorariaatividadecomplementar() {
		if (totalcargahorariaatividadecomplementar == null) {
			totalcargahorariaatividadecomplementar = "";		}
		
		return totalcargahorariaatividadecomplementar;
	}

	public void setTotalcargahorariaatividadecomplementar(String totalcargahorariaatividadecomplementar) {
		this.totalcargahorariaatividadecomplementar = totalcargahorariaatividadecomplementar;
	}

	public String getGrauCurso() {
		if (grauCurso == null) {
			grauCurso = "";
		}
		return grauCurso;
	}

	public void setGrauCurso(String grauCurso) {
		this.grauCurso = grauCurso;
	}

	public String getDataProcessoSeletivo() {
		if (dataProcessoSeletivo == null) {
			dataProcessoSeletivo = "";
		}
		return dataProcessoSeletivo;
	}

	public void setDataProcessoSeletivo(String dataProcessoSeletivo) {
		this.dataProcessoSeletivo = dataProcessoSeletivo;
	}
	
	public String getSecaoZonaEleitoral() {
		if (secaoZonaEleitoral == null) {
			secaoZonaEleitoral = "";
		}
		return secaoZonaEleitoral;
	}

	public void setSecaoZonaEleitoral(String secaoZonaEleitoral) {
		this.secaoZonaEleitoral = secaoZonaEleitoral;
	}

	public String getNomeUnidadeEnsino() {
		return nomeUnidadeEnsino;
	}

	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
	}
	public String getMesReferenciaExtenso() {
		if (Uteis.isAtributoPreenchido(getMesIngresso())) {
			return Uteis.getMesReferenciaExtenso(getMesIngresso());
		}
		return "";
	}

	public int getCodigoIES() {
		if (codigoIES == null) {
			codigoIES = 0;
		}
		return codigoIES;
	}

	public void setCodigoIES(int codigoIES) {
		this.codigoIES = codigoIES;
	}

	public int getCodigoIESMantenedora() {
		if (codigoIESMantenedora == null) {
			codigoIESMantenedora = 0;
		}
		return codigoIESMantenedora;
	}

	public void setCodigoIESMantenedora(int codigoIESMantenedora) {
		this.codigoIESMantenedora = codigoIESMantenedora;
	}

	public int getCodigoEmecCurso() {
		if (codigoEmecCurso == null) {
			codigoEmecCurso = 0;
		}
		return codigoEmecCurso;
	}

	public void setCodigoEmecCurso(int codigoEmecCurso) {
		this.codigoEmecCurso = codigoEmecCurso;
	}
    public String getMesIngressoExtenso() {
		if (mesIngressoExtenso == null) {
			mesIngressoExtenso = getMesReferenciaExtenso();
		}
		return mesIngressoExtenso;
	}

	public void setMesIngressoExtenso(String mesIngressoExtenso) {
		this.mesIngressoExtenso = mesIngressoExtenso;
	}
	
	public String getAnoMesIngressoInstituicao() throws ParseException {
		if (Uteis.isAtributoPreenchido(dataInicioCurso)) {
			Date data = UteisData.getData(dataInicioCurso);
			anoMesIngressoInstituicao = UteisData.getAnoData(data) + "/" + Uteis.getSemestreData(data);
		}
		return anoMesIngressoInstituicao;
	}

	public void setAnoMesIngressoInstituicao(String anoMesIngressoInstituicao) {
		this.anoMesIngressoInstituicao = anoMesIngressoInstituicao;
	}
	public String getLegendaTitulacaoProfessor() {
		if(legendaTitulacaoProfessor == null) {
			legendaTitulacaoProfessor = "" ;
		}
		return legendaTitulacaoProfessor;
	}

	public void setLegendaTitulacaoProfessor(String legendaTitulacaoProfessor) {
		this.legendaTitulacaoProfessor = legendaTitulacaoProfessor;
	}
	public String getCnpjMantenedora() {
		if (cnpjMantenedora == null) {
			cnpjMantenedora = "";
		}
		return cnpjMantenedora;
	}

	public void setCnpjMantenedora(String cnpjMantenedora) {
		this.cnpjMantenedora = cnpjMantenedora;
	}

	public String getUnidadeCertificadora() {
		if (unidadeCertificadora == null) {
			unidadeCertificadora = "";
		}
		return unidadeCertificadora;
	}

	public void setUnidadeCertificadora(String unidadeCertificadora) {
		this.unidadeCertificadora = unidadeCertificadora;
	}

	public String getCnpjUnidadeCertificadora() {
		if (cnpjUnidadeCertificadora == null) {
			cnpjUnidadeCertificadora = "";
		}
		return cnpjUnidadeCertificadora;
	}

	public void setCnpjUnidadeCertificadora(String cnpjUnidadeCertificadora) {
		this.cnpjUnidadeCertificadora = cnpjUnidadeCertificadora;
	}

	public Integer getCodigoIESUnidadeCertificadora() {
		if (codigoIESUnidadeCertificadora == null) {
			codigoIESUnidadeCertificadora = 0;
		}
		return codigoIESUnidadeCertificadora;
	}

	public void setCodigoIESUnidadeCertificadora(Integer codigoIESUnidadeCertificadora) {
		this.codigoIESUnidadeCertificadora = codigoIESUnidadeCertificadora;
	}
	
	public Double mediaGlobal;
	
	public Double getMediaGlobal() {
		if(mediaGlobal == null) {
			mediaGlobal = 0.0;		
		}
		return mediaGlobal;
		
	}
	
	public void setMediaGlobal(Double mediaGlobal) {
		this.mediaGlobal = mediaGlobal;
	}
	
	public Double percentualGlobalFrequencia;	
			
	public Double getPercentualGlobalFrequencia() {
		if(percentualGlobalFrequencia == null) {
			percentualGlobalFrequencia = 0.0;
		}
		return percentualGlobalFrequencia;
	}

	public void setPercentualGlobalFrequencia(Double percentualGlobalFrequencia) {
		this.percentualGlobalFrequencia = percentualGlobalFrequencia;
	}
	
	public Double aproveitamentoAprendizagem;	

	public Double getAproveitamentoAprendizagem() {
		if(aproveitamentoAprendizagem == null) {
			aproveitamentoAprendizagem = 0.0;
		}
		return aproveitamentoAprendizagem;
	}

	public void setAproveitamentoAprendizagem(Double aproveitamentoAprendizagem) {
		this.aproveitamentoAprendizagem = aproveitamentoAprendizagem;
	}

	public List<ConfiguracaoAcademicoNotaConceitoVO> getListaConfiguracaoAcademicoNotaConceitoVO() {
		if (listaConfiguracaoAcademicoNotaConceitoVO == null) {
			listaConfiguracaoAcademicoNotaConceitoVO = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>();
		}
		return listaConfiguracaoAcademicoNotaConceitoVO;
	}

	public void setListaConfiguracaoAcademicoNotaConceitoVO(List<ConfiguracaoAcademicoNotaConceitoVO> listaConfiguracaoAcademicoNotaConceitoVO) {
		this.listaConfiguracaoAcademicoNotaConceitoVO = listaConfiguracaoAcademicoNotaConceitoVO;
	}
	
	public JRDataSource getConfiguracaoAcademicoNotaConceitoVO() {
		return new JRBeanArrayDataSource(getListaConfiguracaoAcademicoNotaConceitoVO().toArray());
	}


	public String getNivelEducacionalCurso() {
		if (nivelEducacionalCurso == null) {
			nivelEducacionalCurso = "";
		}
		return nivelEducacionalCurso;
	}

	public void setNivelEducacionalCurso(String nivelEducacionalCurso) {
		this.nivelEducacionalCurso = nivelEducacionalCurso;
	}
	
	public String getProficienciaLinguaEstrangeira() {
		if (proficienciaLinguaEstrangeira == null) {
			proficienciaLinguaEstrangeira = "";
		}
		return proficienciaLinguaEstrangeira;
	}


	public void setProficienciaLinguaEstrangeira(String proficienciaLinguaEstrangeira) {
		this.proficienciaLinguaEstrangeira = proficienciaLinguaEstrangeira;
	}


	public String getSituacaoProficienciaLinguaEstrangeira() {
		if (situacaoProficienciaLinguaEstrangeira == null) {
			situacaoProficienciaLinguaEstrangeira = "";
		}
		return situacaoProficienciaLinguaEstrangeira;
	}


	public void setSituacaoProficienciaLinguaEstrangeira(String situacaoProficienciaLinguaEstrangeira) {
		this.situacaoProficienciaLinguaEstrangeira = situacaoProficienciaLinguaEstrangeira;
	}

	public String getAnoSemestreConclusaoEnsMedio() {
		if(anoSemestreConclusaoEnsMedio == null) {
			anoSemestreConclusaoEnsMedio = "";
 		}
		return anoSemestreConclusaoEnsMedio;
	}

	public void setAnoSemestreConclusaoEnsMedio(String anoSemestreConclusaoEnsMedio) {
		this.anoSemestreConclusaoEnsMedio = anoSemestreConclusaoEnsMedio;
	}
	
	public String getMesAnoRealizacaoExtensoApresentar() throws ParseException {
		String dataProcessoSeletivoExtenso = "";
		if(Uteis.isAtributoPreenchido(dataProcessoSeletivo)){
			Date data = UteisData.getData(dataProcessoSeletivo);
			dataProcessoSeletivoExtenso  =  Uteis.getMesReferenciaExtenso(String.valueOf(Uteis.getMesData(data))) + " de " + UteisData.getAnoData(data ) ;
		}		
		return dataProcessoSeletivoExtenso ;
	}
	
	public String getMesIngressoExtensoAbreviado() {				
		if (mesIngressoExtensoAbreviado == null) {
			if (Uteis.isAtributoPreenchido(getMesIngresso())) {
			  	mesIngressoExtensoAbreviado = Uteis.getMesReferenciaAbreviadoExtenso(Integer.parseInt(getMesIngresso()));				
			}else {
				mesIngressoExtensoAbreviado = "" ;
			}
		}
		return mesIngressoExtensoAbreviado;
	}
	
	public String getAnoMesIngressoExtensoAbreviado_Apresentar() {
		 String anoSemestreConclusao = "";
	    	if(Uteis.isAtributoPreenchido(mesIngressoExtensoAbreviado) && Uteis.isAtributoPreenchido(anoIngresso)) {
	    		anoSemestreConclusao = mesIngressoExtensoAbreviado +" / "+ anoIngresso ;
	    	}
	    	return anoSemestreConclusao ;
		
	}
	
	public void setMesIngressoExtensoAbreviado(String mesIngressoExtensoAbreviado) {
		this.mesIngressoExtensoAbreviado = mesIngressoExtensoAbreviado;
	}
	public Integer getCargaHorariaDisciplinaCursada() {
		if(cargaHorariaDisciplinaCursada == null) {
			cargaHorariaDisciplinaCursada = 0;
		}
		return cargaHorariaDisciplinaCursada;
	}

	public void setCargaHorariaDisciplinaCursada(Integer cargaHorariaDisciplinaCursada) {
		this.cargaHorariaDisciplinaCursada = cargaHorariaDisciplinaCursada;
	}

	public Integer getCargaHorariaDisciplinaOptativaCursada() {
		if(cargaHorariaDisciplinaOptativaCursada == null) {
			cargaHorariaDisciplinaOptativaCursada = 0;
		}
		return cargaHorariaDisciplinaOptativaCursada;
	}

	public void setCargaHorariaDisciplinaOptativaCursada(Integer cargaHorariaDisciplinaOptativaCursada) {
		this.cargaHorariaDisciplinaOptativaCursada = cargaHorariaDisciplinaOptativaCursada;
	}

	public Integer getCargaHorariaDisciplinaEstagioExigida() {
		if(cargaHorariaDisciplinaEstagioExigida == null) {
			cargaHorariaDisciplinaEstagioExigida = 0;
		}
		return cargaHorariaDisciplinaEstagioExigida;
	}

	public void setCargaHorariaDisciplinaEstagioExigida(Integer cargaHorariaDisciplinaEstagioExigida) {
		this.cargaHorariaDisciplinaEstagioExigida = cargaHorariaDisciplinaEstagioExigida;
	}

	public Integer getCargaHorariaDisciplinaEstagioRealizada() {
		if(cargaHorariaDisciplinaEstagioRealizada == null) {
			cargaHorariaDisciplinaEstagioRealizada = 0;
		}
		return cargaHorariaDisciplinaEstagioRealizada;
	}

	public void setCargaHorariaDisciplinaEstagioRealizada(Integer cargaHorariaDisciplinaEstagioRealizada) {
		this.cargaHorariaDisciplinaEstagioRealizada = cargaHorariaDisciplinaEstagioRealizada;
	}
	
	public String getDataExpedicaoDiplomaString() {
		if(dataExpedicaoDiplomaString == null) {
			dataExpedicaoDiplomaString = "";
		}
		return dataExpedicaoDiplomaString;
	}

	public void setDataExpedicaoDiplomaString(String dataExpedicaoDiplomaString) {
		this.dataExpedicaoDiplomaString = dataExpedicaoDiplomaString;
	}

	public MatriculaVO getMatriculaVO() {
		if(matriculaVO == null) {
			matriculaVO =  new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}
	public String getTitulacaoOrientadorMonografia() {
		if(titulacaoOrientadorMonografia == null) {
			titulacaoOrientadorMonografia = "";
		}
		return titulacaoOrientadorMonografia;
	}

	public void setTitulacaoOrientadorMonografia(String titulacaoOrientadorMonografia) {
		this.titulacaoOrientadorMonografia = titulacaoOrientadorMonografia;
	}

	public Integer getCargaHorariaMonografia() {
		if(cargaHorariaMonografia == null) {
			cargaHorariaMonografia = 0;
		}
		return cargaHorariaMonografia;
	}

	public void setCargaHorariaMonografia(Integer cargaHorariaMonografia) {
		this.cargaHorariaMonografia = cargaHorariaMonografia;
	}

	public Double getCargaHorariaMatriculado() {
		if(cargaHorariaMatriculado == null) {
			cargaHorariaMatriculado = 0.0;
		}
		return cargaHorariaMatriculado;
	}

	public void setCargaHorariaMatriculado(Double cargaHorariaMatriculado) {
		this.cargaHorariaMatriculado = cargaHorariaMatriculado;
	}

	public Double getCargaHorariaReprovado() {
		if(cargaHorariaReprovado == null) {
			cargaHorariaReprovado = 0.0;
		}
		return cargaHorariaReprovado;
	}

	public void setCargaHorariaReprovado(Double cargaHorariaReprovado) {
		this.cargaHorariaReprovado = cargaHorariaReprovado;
	}

	public Double getCargaHorariaAprovado() {
		if(cargaHorariaAprovado == null) {
			cargaHorariaAprovado = 0.0;
		}
		return cargaHorariaAprovado;
	}

	public void setCargaHorariaAprovado(Double cargaHorariaAprovado) {
		this.cargaHorariaAprovado = cargaHorariaAprovado;
	}

	public Double getPercentualIntegralizacaoCurricular() {
		if(percentualIntegralizacaoCurricular == null) {
			percentualIntegralizacaoCurricular = 0.0;
		}
		return percentualIntegralizacaoCurricular;
	}

	public void setPercentualIntegralizacaoCurricular(Double percentualIntegralizacaoCurricular) {
		this.percentualIntegralizacaoCurricular = percentualIntegralizacaoCurricular;
	}	
	
	public Boolean getTrazerTodosProfessoresDisciplinas() {
		if(trazerTodosProfessoresDisciplinas == null) {
			trazerTodosProfessoresDisciplinas = Boolean.FALSE;
		}
		return trazerTodosProfessoresDisciplinas;
	}

	public void setTrazerTodosProfessoresDisciplinas(Boolean trazerTodosProfessoresDisciplinas) {
		this.trazerTodosProfessoresDisciplinas = trazerTodosProfessoresDisciplinas;
	}

	public String getAnoConclusao() {
		if(anoConclusao == null) {
			anoConclusao= "";
		}
		return anoConclusao;
	}

	public void setAnoConclusao(String anoConclusao) {
		this.anoConclusao = anoConclusao;
	}

	public String getSemestreConclusao() {
		if(semestreConclusao == null) {
			semestreConclusao = "";
		}
		return semestreConclusao;
	}

	public void setSemestreConclusao(String semestreConclusao) {
		this.semestreConclusao = semestreConclusao;
	}			
		

	public GradeCurricularVO getGradeCurricularVO() {
		if(gradeCurricularVO == null) {
			gradeCurricularVO =  new GradeCurricularVO();
		}
		return gradeCurricularVO;
	}

	public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
		this.gradeCurricularVO = gradeCurricularVO;
	}

	public Boolean getGradeCurricularAtual() {
		if(gradeCurricularAtual == null) {
			gradeCurricularAtual =  true;
		}
		return gradeCurricularAtual;
	}

	public void setGradeCurricularAtual(Boolean gradeCurricularAtual) {
		this.gradeCurricularAtual = gradeCurricularAtual;
	}
	
	public List<HistoricoAlunoDisciplinaRelVO> getListaHistoricoAlunoDisciplinaTccRelVOs() {
		if(historicoAlunoDisciplinaTccRelVOs == null) {
			historicoAlunoDisciplinaTccRelVOs = new ArrayList<HistoricoAlunoDisciplinaRelVO>(0);
		}
		return historicoAlunoDisciplinaTccRelVOs;
	}	

	public void setHistoricoAlunoDisciplinaTccRelVOs(
			List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaTccRelVOs) {
		this.historicoAlunoDisciplinaTccRelVOs = historicoAlunoDisciplinaTccRelVOs;
	}

	public List<HistoricoAlunoDisciplinaRelVO> getListaHistoricoAlunoDisciplinaEstagioRelVOs() {
		if(historicoAlunoDisciplinaEstagioRelVOs == null) {
			historicoAlunoDisciplinaEstagioRelVOs = new ArrayList<HistoricoAlunoDisciplinaRelVO>(0);
		}
		return historicoAlunoDisciplinaEstagioRelVOs;
	}

	public void setHistoricoAlunoDisciplinaEstagioRelVOs(
			List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaEstagioRelVOs) {
		this.historicoAlunoDisciplinaEstagioRelVOs = historicoAlunoDisciplinaEstagioRelVOs;
	}
	
	public List<GradeCurricularEstagioVO> getListaHistoricoAlunoComponenteEstagioRelVOs() {
		if(historicoAlunoComponenteEstagioRelVOs == null) {
			historicoAlunoComponenteEstagioRelVOs = new ArrayList<GradeCurricularEstagioVO>(0);
		}
		return historicoAlunoComponenteEstagioRelVOs;
	}

	public String getObsHistoricoTransferenciaMatrizCurricular() {
		if(obsHistoricoTransferenciaMatrizCurricular == null) {
			obsHistoricoTransferenciaMatrizCurricular =  "";
		}
		return obsHistoricoTransferenciaMatrizCurricular;
	}

	public void setObsHistoricoTransferenciaMatrizCurricular(String obsHistoricoTransferenciaMatrizCurricular) {
		this.obsHistoricoTransferenciaMatrizCurricular = obsHistoricoTransferenciaMatrizCurricular;
	}
	public void setHistoricoAlunoComponenteEstagioRelVOs(
			List<GradeCurricularEstagioVO> historicoAlunoComponenteEstagioRelVOs) {
		this.historicoAlunoComponenteEstagioRelVOs = historicoAlunoComponenteEstagioRelVOs;
	}

	public Integer getCargaHorariaTotalDisciplina() {
		if(cargaHorariaTotalDisciplina == null) {
			cargaHorariaTotalDisciplina = 0;
		}
		return cargaHorariaTotalDisciplina;
	}	
	
	public void setCargaHorariaTotalDisciplina(Integer cargaHorariaTotalDisciplina) {
		this.cargaHorariaTotalDisciplina = cargaHorariaTotalDisciplina;
	}

	public Integer getCargaHorariaTotalEstagio() {
		if(cargaHorariaTotalEstagio == null) {
			cargaHorariaTotalEstagio = 0;
		}
		return cargaHorariaTotalEstagio;
	}

	public void setCargaHorariaTotalEstagio(Integer cargaHorariaTotalEstagio) {
		this.cargaHorariaTotalEstagio = cargaHorariaTotalEstagio;
	}

	public Integer getCargaHorariaTotalTcc() {
		if(cargaHorariaTotalTcc == null) {
			cargaHorariaTotalTcc = 0;
		}
		return cargaHorariaTotalTcc;
	}

	public void setCargaHorariaTotalTcc(Integer cargaHorariaTotalTcc) {
		this.cargaHorariaTotalTcc = cargaHorariaTotalTcc;
	}

	public Integer getCargaHorariaAprovadaDisciplina() {
		if(cargaHorariaAprovadaDisciplina == null) {
			cargaHorariaAprovadaDisciplina = 0;
		}
		return cargaHorariaAprovadaDisciplina;
	}

	public void setCargaHorariaAprovadaDisciplina(Integer cargaHorariaAprovadaDisciplina) {
		this.cargaHorariaAprovadaDisciplina = cargaHorariaAprovadaDisciplina;
	}

	public Integer getCargaHorariaAprovadaEstagio() {
		if(cargaHorariaAprovadaEstagio == null) {
			cargaHorariaAprovadaEstagio = 0;
		}
		return cargaHorariaAprovadaEstagio;
	}

	public void setCargaHorariaAprovadaEstagio(Integer cargaHorariaAprovadaEstagio) {
		this.cargaHorariaAprovadaEstagio = cargaHorariaAprovadaEstagio;
	}

	public Integer getCargaHorariaAprovadaTcc() {
		if(cargaHorariaAprovadaTcc == null) {
			cargaHorariaAprovadaTcc = 0;
		}
		return cargaHorariaAprovadaTcc;
	}

	public void setCargaHorariaAprovadaTcc(Integer cargaHorariaAprovadaTcc) {
		this.cargaHorariaAprovadaTcc = cargaHorariaAprovadaTcc;
	}

	public Integer getCargaHorariaPendenteDisciplina() {
		if(cargaHorariaPendenteDisciplina == null) {
			cargaHorariaPendenteDisciplina = 0;
		}
		return cargaHorariaPendenteDisciplina;
	}

	public void setCargaHorariaPendenteDisciplina(Integer cargaHorariaPendenteDisciplina) {
		this.cargaHorariaPendenteDisciplina = cargaHorariaPendenteDisciplina;
	}

	public Integer getCargaHorariaPendenteEstagio() {
		if(cargaHorariaPendenteEstagio == null) {
			cargaHorariaPendenteEstagio = 0;
		}
		return cargaHorariaPendenteEstagio;
	}

	public void setCargaHorariaPendenteEstagio(Integer cargaHorariaPendenteEstagio) {
		this.cargaHorariaPendenteEstagio = cargaHorariaPendenteEstagio;
	}

	public Integer getCargaHorariaPendenteTcc() {
		if(cargaHorariaPendenteTcc == null) {
			cargaHorariaPendenteTcc = 0;
		}
		return cargaHorariaPendenteTcc;
	}

	public void setCargaHorariaPendenteTcc(Integer cargaHorariaPendenteTcc) {
		this.cargaHorariaPendenteTcc = cargaHorariaPendenteTcc;
	}

	public Boolean getExisteDisplinasEstagio() {
		if(existeDisplinasEstagio == null) {
			existeDisplinasEstagio = Boolean.FALSE;
		}
		return existeDisplinasEstagio;
	}

	public void setExisteDisplinasEstagio(Boolean existeDisplinasEstagio) {
		this.existeDisplinasEstagio = existeDisplinasEstagio;
	}

	public Boolean getExisteComponentesEstagio() {
		if(existeComponentesEstagio == null) {
			existeComponentesEstagio = Boolean.FALSE;
		}
		return existeComponentesEstagio;
	}

	public void setExisteComponentesEstagio(Boolean existeComponentesEstagio) {
		this.existeComponentesEstagio = existeComponentesEstagio;
	}	
	
	public String getAnoSemestreConclusaoCurso() {
		if (anoSemestreConclusaoCurso == null) {
			anoSemestreConclusaoCurso = "";
		}
		return anoSemestreConclusaoCurso;
	}

	public void setAnoSemestreConclusaoCurso(String anoSemestreConclusaoCurso) {
		this.anoSemestreConclusaoCurso = anoSemestreConclusaoCurso;
	}
	
	public ObservacaoComplementarDiplomaVO getObservacaoComplementarDiploma1() {
		if (observacaoComplementarDiploma1 == null) {
			observacaoComplementarDiploma1 = new ObservacaoComplementarDiplomaVO();
		}
		return observacaoComplementarDiploma1;
	}

	public void setObservacaoComplementarDiploma1(ObservacaoComplementarDiplomaVO observacaoComplementarDiploma1) {
		this.observacaoComplementarDiploma1 = observacaoComplementarDiploma1;
	}

	public ObservacaoComplementarDiplomaVO getObservacaoComplementarDiploma2() {
		if (observacaoComplementarDiploma2 == null) {
			observacaoComplementarDiploma2 = new ObservacaoComplementarDiplomaVO();
		}
		return observacaoComplementarDiploma2;
	}

	public void setObservacaoComplementarDiploma2(ObservacaoComplementarDiplomaVO observacaoComplementarDiploma2) {
		this.observacaoComplementarDiploma2 = observacaoComplementarDiploma2;
	}

	public ObservacaoComplementarDiplomaVO getObservacaoComplementarDiploma3() {
		if (observacaoComplementarDiploma3 == null) {
			observacaoComplementarDiploma3 = new ObservacaoComplementarDiplomaVO();
		}
		return observacaoComplementarDiploma3;
	}

	public void setObservacaoComplementarDiploma3(ObservacaoComplementarDiplomaVO observacaoComplementarDiploma3) {
		this.observacaoComplementarDiploma3 = observacaoComplementarDiploma3;
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
	
	public String getCargaHorariaUtilizar() {
		if (cargaHorariaUtilizar == null) {
			cargaHorariaUtilizar = Constantes.EMPTY;
		}
		return cargaHorariaUtilizar;
	}
	
	public void setCargaHorariaUtilizar(String cargaHorariaUtilizar) {
		this.cargaHorariaUtilizar = cargaHorariaUtilizar;
	}
}