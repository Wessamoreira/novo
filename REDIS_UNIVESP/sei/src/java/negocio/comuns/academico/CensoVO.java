package negocio.comuns.academico;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.Censo;

/**
 * Reponsável por manter os dados da entidade Censo. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class CensoVO extends SuperVO {

	public static final int TIPO_REGISTRO_CABECALHO_ALUNO = 40;
	public static final int TIPO_REGISTRO_ARQUIVO_ALUNO = 4;
	public static final int TIPO_REGISTRO_CABECALHO_PROFESSOR = 30;
	public static final int TIPO_REGISTRO_ARQUIVO_PROFESSOR = 3;
	public static final String SEPARADOR = "|";

	public static final String TIPO_REGISTRO_CADASTRO_LAYOUT_TECNICO = "00";
	public static final int SITUACAO_FUNCIONAMENTO_LAYOUT_TECNICO_EM_ATIVIDADE = 1;
	public static final int DEPENDENCIA_ADMINISTRATIVA_LAYOUT_TECNICO = 4;
//	public static final int CATEGORIA_ESCOLA_PRIVADA = 1;
	public static final int MANTENEDORA_ESCOLA_PRIVADA_SIM = 1;
	public static final int MANTENEDORA_ESCOLA_PRIVADA_NAO = 0;
	public static final int REGULAMENTACAO_AUTORIZACAO = 1;
	public static final int ESFERA_ADMINISTRATIVA_CONSELHO_RESPONSAVEL_AUTORIZACAO = 1;
//	public static final int CODIGO_MUNICIPIO_BRASILIA = 5300108;
//	public static final String CODIGO_UF_DISTRITO_FEDEREAL = "06";
//	public static final int CODIGO_UF = 53;

	public static final int TIPO_REGISTRO_LAYOUT_TECNICO = 10;
	public static final int CARGO_GESTOR_ESCOLAR_LAYOUT_TECNICO = 1;
	public static final int ESCOLA_CEDE_ESPACO_TURMA_BRASIL_ALFABETIZADO = 0;
	public static final int ESCOLA_ABRE_FINAL_SEMANA_COMUNIDADE = 0;
	public static final int ESCOLA_PROPOSTA_PEDAGOGICA_FORMACAO_ALTERNANCIA = 0;
	public static final int CARGO_GESTOR_ESCOLAR = 2;
	public static final int LOCAL_FUNCIONAMENTO_ESCOLA_SIM = 1;
	public static final int LOCAL_FUNCIONAMENTO_ESCOLA_NAO = 0;
	public static final int FORMA_OCUPACAO_PREDIO = 1;
	public static final int AGUA_CONSUMIDA_PELO_ALUNO = 2;
	public static final int PREDIO_COMPARTILHADO_OUTRA_ESCOLA = 0;
	public static final int ABASTECIMENTO_AGUA_NAO = 0;
	public static final int ABASTECIMENTO_AGUA_SIM = 1;
	public static final int ABASTECIMENTO_ENERGIA_ELETRICA_SIM = 1;
	public static final int ABASTECIMENTO_ENERGIA_ELETRICA_NAO = 0;
	public static final int ESGOTO_SANITARIO_SIM = 1;
	public static final int ESGOTO_SANITARIO_NAO = 0;
	public static final int DESTINACAO_LIXO_SIM = 1;
	public static final int DESTINACAO_LIXO_NAO = 0;
	public static final int DEPENDENCIA_EXISTENTE_ESCOLA_SIM = 1;
	public static final int DEPENDENCIA_EXISTENTE_ESCOLA_NAO = 0;
	public static final String NUMERO_SALA_AULA_ESCOLA = "0111";
	public static final String NUMERO_SALA_AULA_UTILIZADA_ESCOLA = "0111";
	public static final String TOTAL_FUNCIONARIO_ESCOLA = "0111";
	public static final int ALIMENTACAO_ESCOLAR_ALUNO = 0;
	public static final int ATENDIMENTO_EDUCACIONAL_ESPECIALIZADO = 0;
	public static final int ATIVIDADE_COMPLEMENTAR = 0;
	public static final int MODALIDADE_SIM = 1;
	public static final int MODALIDADE_NAO = 0;
	public static final int ETAPA_SIM = 1;
	public static final int ETAPA_NAO = 0;
	public static final int LOCALIZACAO_DIFERENCIADA_ESCOLA = 7;
	public static final int ENSINO_FUNDAMENTAL_ORGANIZADA_EM_CICLOS = 0;
	public static final int MATERIAL_DIDATICO_ESPECIFICO_ATENDIMENTO_DIVERSIDADE_SOCIOCULTURAL_SIM = 1;
	public static final int MATERIAL_DIDATICO_ESPECIFICO_ATENDIMENTO_DIVERSIDADE_SOCIOCULTURAL_NAO = 0;
	public static final int EDUCACAO_INDIGENA = 0;
	public static final int LINGUA_ENSINO_MINISTRADO_SIM = 1;
	public static final int LINGUA_ENSINO_MINISTRADO_NAO = 0;

	public static final int TIPO_REGISTRO_CADASTRO_TURMA_LAYOUT_TECNICO = 20;
	public static final int TIPO_ATENDIMENTO_CADASTRO_TURMA_LAYOUT_TECNICO = 1;
	public static final int DIA_SEMANA_CADASTRO_TURMA_AULA = 1;
	public static final int MODALIDADE = 1;
	public static final String ETAPA_ENSINO = "39";
	public static final int DISCIPLINA = 0;
	public static final String CODIGO_CURSO = "1006";

	public static final int TIPO_REGISTRO_CADASTRO_PROFESSOR_IDENTIFICACAO = 30;
	public static final String PAIS_ORIGEM = "76";
	public static final int TIPO_REGISTRO_CADASTRO_PROFESSOR_DOCUMENTO = 40;
	public static final int TIPO_REGISTRO_CADASTRO_PROFESSOR_DADOS_VARIAVEIS = 50;
	public static final int ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR = 0;
	public static final int FUNCAO_EXERCE_ESCOLA_TURMA_CADASTRO_PROFESSOR = 1;
	public static final int TIPO_REGISTRO_CADASTRO_PROFESSOR_DADO_DOCENCIA = 51;
	public static final int DISCIPLINA_PROFESSOR_LECIONA_DADO_DOCENCIA = 99;

	public static final int TIPO_REGISTRO_CADASTRO_ALUNO_IDENTIFICACAO = 60;
	public static final int CADASTRO_ALUNO_IDENTIFICAOCA_FILICACAO = 0;
	public static final int TIPO_REGISTRO_CADASTRO_ALUNO_DOCUMENTO = 70;
	public static final int LOCALIZACAO_ZONA_RESIDENCIA = 1;
	public static final int TIPO_REGISTRO_CADASTRO_ALUNO_VINCULO_MATRICULA = 80;
	public static final int RECEBE_ESCOLARIZACAO_OUTRO_ESPACO = 3;
	public static final int UTILIZA_TRANSPORTE_ESCOLAR_PUBLICO = 0;
	
	//Dados Censo 2014 Ensino Medio
	public static final int TIPO_ATENDIMENTO_CADASTRO_TURMA_LAYOUT_ENSINO_MEDIO = 1;
	public static final int ATIVIDADE_DO_ATENDIMENTO_EDUCACIONAL_ESPECIALIZADO_AEE = 0;
	public static final int CODIGO_DO_TIPO_DE_ATENDIMENTO = 0;
	public static final int FORMACAO_COMPLEMENTACAO_PEDAGOGICA = 0;
	public static final int ESPECIFICO_PARA_CRECHE = 0;
	public static final int SITUACAO_REGIME_REGIME_CONTRATACAO_TIPO_VINCULO = 4;
	public static final int FILIACAO = 0;
	public static final int ORGAO_ESCOLA_PUBLICA_VINCULADA_SIM = 1;
	public static final int ORGAO_ESCOLA_PUBLICA_VINCULADA_NAO = 1;
	public static final int RECURSOS_ACESSIBILIDADE_ESCOLA_SIM = 1;
	public static final int RECURSOS_ACESSIBILIDADE_ESCOLA_NAO = 0;
	public static final int EQUIPAMENTOS_EXISTENTES_ESCOLA_SIM = 1;
	public static final int EQUIPAMENTOS_EXISTENTES_ESCOLA_NAO = 0;
	public static final int ACESSO_INTERNET_SIM=1;
	public static final int ACESSO_INTERNET_NAO=0;
	public static final int REDE_LOCAL_SIM=1;
	public static final int REDE_LOCAL_NAO=0;
	
	private Integer codigo;
	private String ano;
	private Date dataGeracao;
	private UsuarioVO responsavel;
	private UnidadeEnsinoVO unidadeEnsino;
	private String semestre;
	private List<ProfessorCensoVO> listaProfessorCenso;
	private List<AlunoCensoVO> listaAlunoCenso;
	private ArquivoVO arquivoAluno;
	private ArquivoVO arquivoAlunoExcel;
	private ArquivoVO arquivoProfessor;
	private String observacao;
	private String layout;
	private List<TurmaCensoVO> listaTurmaCenso;
	private ArquivoVO arquivoLayoutTecnico;
	private ArquivoVO arquivoLayoutEnsinoMedio;
	private Date dataInicioPeriodoLetivoUnidadeEnsino;
	private Date dataFimPeriodoLetivoUnidadeEnsino;
	
	private Date dataBase;	
	private String tratarAbandonoCurso;
	private List<CensoUnidadeEnsinoVO> censoUnidadeEnsinoVOs;
	private List<File> arquivosExcel;
	private List<File> arquivosTxt;
	

	public static final long serialVersionUID = 1L;

	public CensoVO() {
		super();
	}

	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da
	 * classe <code>CensoVO</code>.
	 */
	public static void validarUnicidade(List<CensoVO> lista, CensoVO obj) throws ConsistirException {
		for (CensoVO repetido : lista) {
			if (repetido.getCodigo().intValue() == obj.getCodigo().intValue()) {
				throw new ConsistirException("O campo CODIGO já esta cadastrado!");
			}
		}
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>CensoVO</code>. Todos os tipos de consistência de dados são e devem
	 * ser implementadas neste método. São validações típicas: verificação de
	 * campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(CensoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			if(obj.getLayout().equals(Censo.GRADUACAO)){
				throw new ConsistirException("O campo Unidade de Ensino Matriz (Censo) deve ser informado.");
			}
			throw new ConsistirException("O campo Unidade de Ensino (Censo) deve ser informado.");
		}
		if(obj.getLayout().equals(Censo.GRADUACAO) || obj.getLayout().equals(Censo.GRADUACAO_TECNOLOGICA)){
			if (obj.getAno().equals("")) {
				throw new ConsistirException("O campo ANO (Censo) deve ser informado.");
			}					
			if(obj.getCensoUnidadeEnsinoSelecionadoVOs().isEmpty()) {
				throw new ConsistirException("O campo UNIDADE ENSINO (Censo) deve ser informado.");
			}		
		}else {

			if(obj.getDataBase()== null){
				throw new ConsistirException("O campo DATA BASE (Censo) deve ser informado.");
			}
		}
		
		
//		if (obj.getSemestre().equals("")) {
//			throw new ConsistirException("O campo SEMESTRE (Censo) deve ser informado.");
//		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		setAno(getAno().toUpperCase());
		setLayout(getLayout().toUpperCase());
	}

	/**
	 * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
	 * <code>Censo</code>).
	 */
	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return (responsavel);
	}

	/**
	 * Define o objeto da classe <code>Pessoa</code> relacionado com (
	 * <code>Censo</code>).
	 */
	public void setResponsavel(UsuarioVO obj) {
		this.responsavel = obj;
	}

	public List<AlunoCensoVO> getListaAlunoCenso() {
		if (listaAlunoCenso == null) {
			listaAlunoCenso = new ArrayList<AlunoCensoVO>(0);
		}
		return listaAlunoCenso;
	}

	public void setListaAlunoCenso(List<AlunoCensoVO> listaAlunoCenso) {
		this.listaAlunoCenso = listaAlunoCenso;
	}

	public List<ProfessorCensoVO> getListaProfessorCenso() {
		if (listaProfessorCenso == null) {
			listaProfessorCenso = new ArrayList<ProfessorCensoVO>(0);
		}
		return listaProfessorCenso;
	}

	public void setListaProfessorCenso(List<ProfessorCensoVO> listaProfessorCenso) {
		this.listaProfessorCenso = listaProfessorCenso;
	}

	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getDataGeracao() {
		if (dataGeracao == null) {
			dataGeracao = new Date();
		}
		return (dataGeracao);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataGeracao_Apresentar() {
		return (Uteis.getData(getDataGeracao()));
	}

	public void setDataGeracao(Date dataGeracao) {
		this.dataGeracao = dataGeracao;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return (ano);
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = Uteis.getSemestreAtual();
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public ArquivoVO getArquivoAluno() {
		if (arquivoAluno == null) {
			arquivoAluno = new ArquivoVO();
		}
		return arquivoAluno;
	}

	public void setArquivoAluno(ArquivoVO arquivoAluno) {
		this.arquivoAluno = arquivoAluno;
	}

	public ArquivoVO getArquivoProfessor() {
		if (arquivoProfessor == null) {
			arquivoProfessor = new ArquivoVO();
		}
		return arquivoProfessor;
	}

	public void setArquivoProfessor(ArquivoVO arquivoProfessor) {
		this.arquivoProfessor = arquivoProfessor;
	}

	public ArquivoVO getArquivoAlunoExcel() {
		if (arquivoAlunoExcel == null) {
			arquivoAlunoExcel = new ArquivoVO();
		}
		return arquivoAlunoExcel;
	}

	public void setArquivoAlunoExcel(ArquivoVO arquivoAlunoExcel) {
		this.arquivoAlunoExcel = arquivoAlunoExcel;
	}

	public String getLayout() {
		if (layout == null) {
			layout = "GRADUACAO";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}
	
	public boolean isLayoutGraduacao(){
		return getLayout().equals(Censo.GRADUACAO) ||  getLayout().equals(Censo.GRADUACAO_TECNOLOGICA); 
	}
	
	public boolean isLayoutTecnico(){
		return getLayout().equals(Censo.TECNICO); 
	}
	
	public boolean isLayoutEnsinoMedio(){
		return getLayout().equals(Censo.MEDIO); 
	}

	public List<TurmaCensoVO> getListaTurmaCenso() {
		if (listaTurmaCenso == null) {
			listaTurmaCenso = new ArrayList<TurmaCensoVO>(0);
		}
		return listaTurmaCenso;
	}

	public void setListaTurmaCenso(List<TurmaCensoVO> listaTurmaCenso) {
		this.listaTurmaCenso = listaTurmaCenso;
	}

	public ArquivoVO getArquivoLayoutTecnico() {
		if (arquivoLayoutTecnico == null) {
			arquivoLayoutTecnico = new ArquivoVO();
		}
		return arquivoLayoutTecnico;
	}

	public void setArquivoLayoutTecnico(ArquivoVO arquivoLayoutTecnico) {
		this.arquivoLayoutTecnico = arquivoLayoutTecnico;
	}

	public Date getDataInicioPeriodoLetivoUnidadeEnsino() {
		return dataInicioPeriodoLetivoUnidadeEnsino;
	}

	public void setDataInicioPeriodoLetivoUnidadeEnsino(Date dataInicioPeriodoLetivoUnidadeEnsino) {
		this.dataInicioPeriodoLetivoUnidadeEnsino = dataInicioPeriodoLetivoUnidadeEnsino;
	}

	public Date getDataFimPeriodoLetivoUnidadeEnsino() {
		return dataFimPeriodoLetivoUnidadeEnsino;
	}

	public void setDataFimPeriodoLetivoUnidadeEnsino(Date dataFimPeriodoLetivoUnidadeEnsino) {
		this.dataFimPeriodoLetivoUnidadeEnsino = dataFimPeriodoLetivoUnidadeEnsino;
	}

	public Date getDataBase() {
		if(dataBase == null){
			dataBase = new Date();
		}
		return dataBase;
	}

	public void setDataBase(Date dataBase) {
		this.dataBase = dataBase;
	}

	public ArquivoVO getArquivoLayoutEnsinoMedio() {
		if (arquivoLayoutEnsinoMedio == null) {
			arquivoLayoutEnsinoMedio = new ArquivoVO();
		}
		return arquivoLayoutEnsinoMedio;
	}

	public void setArquivoLayoutEnsinoMedio(ArquivoVO arquivoLayoutEnsinoMedio) {
		this.arquivoLayoutEnsinoMedio = arquivoLayoutEnsinoMedio;
	}

	public String getTratarAbandonoCurso() {
		if(tratarAbandonoCurso == null) {
			tratarAbandonoCurso = "TR";
		}
		return tratarAbandonoCurso;
	}

	public void setTratarAbandonoCurso(String tratarAbandonoCurso) {
		this.tratarAbandonoCurso = tratarAbandonoCurso;
	}

	public List<CensoUnidadeEnsinoVO> getCensoUnidadeEnsinoVOs() {
		if(censoUnidadeEnsinoVOs == null) {
			censoUnidadeEnsinoVOs =  new ArrayList<CensoUnidadeEnsinoVO>(0);
		}
		return censoUnidadeEnsinoVOs;
	}

	public void setCensoUnidadeEnsinoVOs(List<CensoUnidadeEnsinoVO> censoUnidadeEnsinoVOs) {
		this.censoUnidadeEnsinoVOs = censoUnidadeEnsinoVOs;
	}
	
	
	public List<CensoUnidadeEnsinoVO> getCensoUnidadeEnsinoSelecionadoVOs() {		
		return getCensoUnidadeEnsinoVOs().stream().filter(c -> c.getIsSelecionado()).collect(Collectors.toList());
	}

	private String unidadeEnsinoDescricao;
	public String getUnidadeEnsinoDescricao() {
		if(unidadeEnsinoDescricao == null || unidadeEnsinoDescricao.equals("")) {
			unidadeEnsinoDescricao = "";
			getCensoUnidadeEnsinoSelecionadoVOs().forEach(c ->{
			if(!unidadeEnsinoDescricao.isEmpty()) {
				unidadeEnsinoDescricao += ", ";
			}
			unidadeEnsinoDescricao += c.getUnidadeEnsinoVO().getNome();
			}
			);
			
		}
		return unidadeEnsinoDescricao;
	}

	public void setUnidadeEnsinoDescricao(String unidadeEnsinoDescricao) {
		this.unidadeEnsinoDescricao = unidadeEnsinoDescricao;
	}
	
	private PrintWriter pwAluno;
	private PrintWriter pwProfessor;
	private PrintWriter pwLayoutTecnico;
	private PrintWriter pwLayoutEducacaoBasica;
	private XSSFSheet worksheet;
	private XSSFWorkbook workbook;
	private FileOutputStream fileOutputStream;	

	public PrintWriter getPwAluno() {
		return pwAluno;
	}

	public void setPwAluno(PrintWriter pwAluno) {
		this.pwAluno = pwAluno;
	}

	public PrintWriter getPwProfessor() {
		return pwProfessor;
	}

	public void setPwProfessor(PrintWriter pwProfessor) {
		this.pwProfessor = pwProfessor;
	}

	public PrintWriter getPwLayoutTecnico() {
		return pwLayoutTecnico;
	}

	public void setPwLayoutTecnico(PrintWriter pwLayoutTecnico) {
		this.pwLayoutTecnico = pwLayoutTecnico;
	}

	public PrintWriter getPwLayoutEducacaoBasica() {
		return pwLayoutEducacaoBasica;
	}

	public void setPwLayoutEducacaoBasica(PrintWriter pwLayoutEducacaoBasica) {
		this.pwLayoutEducacaoBasica = pwLayoutEducacaoBasica;
	}

	public XSSFSheet getWorksheet() {
		return worksheet;
	}

	public void setWorksheet(XSSFSheet worksheet) {
		this.worksheet = worksheet;
	}

	public XSSFWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(XSSFWorkbook workbook) {		
		this.workbook = workbook;
	}

	public FileOutputStream getFileOutputStream() {
		return fileOutputStream;
	}

	public void setFileOutputStream(FileOutputStream fileOutputStream) {
		this.fileOutputStream = fileOutputStream;
	}

	public List<File> getArquivosExcel() {
		if(arquivosExcel == null) {
			arquivosExcel =  new ArrayList<File>(0);
		}
		return arquivosExcel;
	}

	public void setArquivosExcel(List<File> arquivosExcel) {
		this.arquivosExcel = arquivosExcel;
	}

	public List<File> getArquivosTxt() {
		if(arquivosTxt == null) {
			arquivosTxt =  new ArrayList<File>(0);
		}
		return arquivosTxt;
	}

	public void setArquivosTxt(List<File> arquivosTxt) {
		this.arquivosTxt = arquivosTxt;
	}

	
	
}
