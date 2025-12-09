package negocio.interfaces.administrativo;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioPerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.UnidadeEnsinoBibliotecaVO;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.protocolo.enumeradores.TipoDistribuicaoResponsavelEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface FuncionarioInterfaceFacade {

	public List<FuncionarioVO> consultarPorCPF(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultarPorCPFUnico(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorCPF(String valorConsulta, String tipoPessoa, Boolean ativo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorNome(String valorConsulta, String tipoPessoa, Boolean ativo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorNomeCidade(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorNomeCidade(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorRG(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorRG(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public FuncionarioVO novo() throws Exception;

	public void incluir(FuncionarioVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	public void alterar(FuncionarioVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	public void excluir(FuncionarioVO obj, UsuarioVO usuario) throws Exception;
	
	public void realizarAtualizacaoNaoNotificarInsercaoUsuario(FuncionarioVO obj, UsuarioVO usuario) throws Exception;

	public FuncionarioVO importarPessoaCadastrada(FuncionarioVO funcionarioVO, PessoaVO pessoaVO, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultarPorChavePrimaria(Integer codigo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultarPorCodigoPessoa(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public String consultarMatriculaFuncionarioPorCodigoPessoa(Integer valorConsulta, Integer unidadeEnsino) throws Exception;

	public FuncionarioVO consultarPorRequisitanteMatricula(String campoConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorMatricula(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultarPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<FuncionarioVO> consultarPorMatricula(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultarFuncionarioPorMatricula(String prm, String tipoPessoa, Boolean ativo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultarPorMatricula(String prm, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorNomeUnidadeEnsino(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<FuncionarioVO> consultaRapidaPorUnidadeEnsino(List<UnidadeEnsinoVO> listaUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorNomeDepartamento(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorNomeDepartamentoESemDepartamento(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorNomeECodigoDepartamentoEMultiDepartamento(String nome, Integer codigo, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorCPFECodigoDepartamentoEMultiDepartamento(String nome, Integer codigo, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorMatriculaECodigoDepartamentoEMultiDepartamento(String nome, Integer codigo, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorCodigoDepartamentoESemDepartamento(Integer codigo, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorNomeCargo(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarPorDataAdmissao(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List<FuncionarioVO> consultarProfessoresParaCenso(boolean b, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorNome(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Integer consultaTotalDeRegistroRapidaPorNome(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorNome(String valorConsulta, Integer departamento, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorNomeConsultor(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorMatricula(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorMatricula(String valorConsulta, Integer departamento, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorMatriculaConsultor(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

	public Integer consultaTotalDeRegistroRapidaPorMatricula(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultarProfessorPorMatriculaDisciplina(String matricula, Integer disciplina, Integer unidadeEnsino, boolean verificarTitular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultarProfessorTitularPorMatriculaDisciplina(String matricula, Integer disciplina, Integer unidadeEnsino, boolean verificarTitular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorCidade(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorCidade(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Integer consultaTotalDeRegistroRapidaPorCidade(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorCPF(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorCPF(String valorConsulta, Integer departamento, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorCPFConsultor(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Integer consultaTotalDeRegistroRapidaPorCPF(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorCargo(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorNomePessoaAutoComplete(String valorConsulta, Integer codigoUnidadeEnsino, int limit, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorCargo(String valorConsulta, Integer departamento, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorCargoConsultor(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Integer consultaTotalDeRegistroRapidaPorCargo(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorUnidadeEnsino(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorUnidadeEnsino(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorUnidadeEnsinoConsultor(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Integer consultaTotalDeRegistroRapidaPorUnidadeEnsino(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorNomeDepartamento(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorNomeDepartamento(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorNomeDepartamentoConsultor(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Integer consultaTotalDeRegistroRapidaPorNomeDepartamento(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void carregarDados(FuncionarioVO obj, UsuarioVO usuario) throws Exception;

	public void carregarDados(FuncionarioVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void inicializarDadosEntidadesSubordinadasFuncionarioVOCompleto(FuncionarioVO obj, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultaRapidaPorCodigoPessoa(Integer codPessoa, Boolean nivelMontarDados, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void validarDados(PessoaVO obj) throws ConsistirException;

	public FuncionarioVO consultaFuncionarioResponsavelAssinaturaTermoEstagioRapidaPorCurso(Integer curso, UsuarioVO usuario) throws Exception;
	
	public FuncionarioVO consultaRapidaPorMatriculaUnica(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultarFuncionarioParaGrupoDestinatarios(Integer codigoFuncionario, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultarProfessorTitularPorMatriculaDisciplinaSemRegistroAulta(String matricula, Integer disciplina, String nomeProfessor, Integer unidadeEnsino, boolean verificarTitular, boolean controlarAcesso, int nivelMontarDados,boolean historicoPorEquivalencia, String anoDisciplina , String semestreDisciplina, UsuarioVO usuario) throws Exception;

	@Deprecated
	public FuncionarioVO consultarProfessorComAulaProgramadaPorMatriculaDisciplina(String matricula, Integer disciplina, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultaRapidaPorChavePrimaria(Integer codigo, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaProfessorPorNomeAgendaProfessoresVisaoCoordenador(String valorConsulta, Integer codigoCoordenador, String tipoPessoa, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaProfessorPorMatriculaAgendaProfessoresVisaoCoordenador(String valorConsulta, Integer codigoCoordenador, String tipoPessoa, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaProfessorPorNomeCidadeAgendaProfessoresVisaoCoordenador(String valorConsulta, Integer codigoCoordenador, String tipoPessoa, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaProfessorPorCPFAgendaProfessoresVisaoCoordenador(String valorConsulta, Integer codigoCoordenador, String tipoPessoa, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaProfessorPorCargoAgendaProfessoresVisaoCoordenador(String valorConsulta, Integer codigoCoordenador, String tipoPessoa, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaProfessorPorDepartamentoAgendaProfessoresVisaoCoordenador(String valorConsulta, Integer codigoCoordenador, String tipoPessoa, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultaRapidaProfessorPorMatriculaUnicaAgendaProfessoresVisaoCoordenador(String valorConsulta, Integer codigoCoordenador, String tipoPessoa, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultaRapidaPorChavePrimariaSemExcecao(Integer codigo, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorNomeProfessorTitularDisciplinaTurma(String valorConsulta, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, String semestre, String ano, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorCpfProfessorTitularDisciplinaTurma(String valorConsulta, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, String semestre, String ano, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorNome(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorMatricula(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorCidade(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorCPF(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorCargo(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorNomeDepartamento(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorUnidadeEnsino(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaCoordenadorPorNomeApresentarModal(String valorConsulta, Boolean permissao, UsuarioVO usuarioVO) throws Exception;

	public List<FuncionarioVO> consultaRapidaCoordenadorPorCPFApresentarModal(String valorConsulta, Boolean permissao, UsuarioVO usuarioVO) throws Exception;

	public List<FuncionarioVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<FuncionarioVO> consultarPorUnifificacao(String valorConsulta, String campoConsulta, boolean funcionarioAtivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorNomeAutoComplete(String valorConsulta, String listaCodigoUnidadeEnsino, String tipoPessoa, Boolean exerceCargoAdministrativo, Boolean ativo, int limit, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultarProfessorComAulaProgramadaPorMatriculaDisciplinaNome(String matricula, Integer disciplina, String nomeProfessor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public String consultarNomeFuncionarioPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultaRapidaConsultorPorMatricula(String matricula, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<FuncionarioVO> consultarFuncionarioConsultorPorUnidade(Integer unidadeEnsino);

	public List<FuncionarioVO> consultaRapidaPorMatriculaUnidadeEnsinoBiblioteca(String valorConsulta, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultaRapidaPorMatriculaUnicaUnidadeEnsinoBiblioteca(String valorConsulta, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void unificarFuncionario(final Integer codFuncionarioManter, final Integer codFuncionarioRemover, UsuarioVO usuario) throws Exception;

	FuncionarioVO consultaRapidaDiretorGeralPorCodigoUnidadeEnsino(Integer codUndiadeEnsino, UsuarioVO usuario) throws Exception;

	public FuncionarioVO consultaRapidaPorCodigoPessoaCenso(Integer codPessoa, UsuarioVO usuario) throws Exception;

	FuncionarioVO consultarFuncionarioEConsultorDaUnidade(Integer unidadeEnsino, Integer consultor);

	FuncionarioVO realizarDistribuicaoResponsavelRequerimento(Integer requerimento, TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, TipoDistribuicaoResponsavelEnum tipoDistribuicaoResponsavelEnum, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario, TipoRequerimentoVO tipoRequerimentoVO) throws Exception;

	public List<FuncionarioVO> consultarConsultoresPropsectsUnificacao(Integer prospect1, Integer prospect2, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<FuncionarioVO> consultaRapidaPorMatriculaNivelEducacional(String valorConsulta, String nivelEducacional, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<FuncionarioVO> consultaRapidaPorCPFNivelEducacional(String valorConsulta, String nivelEducacional, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<FuncionarioVO> consultaRapidaPorNomeNivelEducacional(String valorConsulta, String nivelEducacional, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 09/10/2015
	 * @param requerimento
	 * @param departamento
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	FuncionarioVO realizarVerificacaoExistenciaFuncionarioJaRealizouTramite(Integer requerimento, Integer departamento, Integer cargo, Integer ordemExecucaoTramite, TipoDistribuicaoResponsavelEnum tipoDistribuicaoResponsavel, UsuarioVO usuario) throws Exception;

	List<FuncionarioVO> consultaRapidaPorMatriculaProfessorTitularDisciplinaTurma(String valorConsulta, Integer unidadeEnsino, Integer curso, String semestre, String ano, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * @author Carlos Eugênio - 11/11/2016
	 * @param codigoPessoaUsuario
	 * @param usuarioVO
	 * @return
	 */
	FuncionarioVO consultarConsultorUltimaInteracaoProspectPorProspect(Integer codigoPessoaUsuario, UsuarioVO usuarioVO);

	public List<FuncionarioVO> consultarPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, boolean ativo, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultarFuncionarioPorNomeDepartamentoAtivo(String nome, DepartamentoVO departamento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<FuncionarioVO> consultarProfessoresCoordenadorPorPeriodo(String valorConsulta, String tipoConsulta, UsuarioVO usuario) throws Exception;

	List<FuncionarioVO> consultarFuncionarioPorNomeAtivo(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<FuncionarioVO> consultaRapidaPorSecao(String valorConsulta, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaPorFormaContratacao(String valorConsulta, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Integer consultaTotalDeRegistroRapidaPorFormaContratacao(String valorConsulta, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Integer consultaTotalDeRegistroRapidaPorSecao(String valorConsulta, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<FuncionarioVO> consultarFuncionarioAtivoPorMatricula(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario, Boolean ativo) throws Exception;

    public FuncionarioVO consultarProfessorTutoriaOnlineDisciplina(String matricula, Integer disciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public FuncionarioVO consultaCordenadorPorCurso(String codigoMatricula, Integer codigoCurso, String codigoTurma,Integer codigoUnidadeEnsino, int nivelmontardadosDadosbasicos, UsuarioVO usuarioVO) throws Exception;
    
    FuncionarioVO consultarProfessorTitularPorCursoDisciplinaAnoSemestre(Integer curso, Integer disciplina, String ano, String semestre, boolean verificarTitular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    List<FuncionarioVO> consultarProfessoresPorCursoDisciplinaAnoSemestre(Integer curso, Integer disciplina, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
	public List<FuncionarioVO> consultarFuncionarioUnificar(String valorConsulta, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<FuncionarioVO> consultarPorCPF(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso,
			int nivelMontarDados, boolean funcionarioAtivo, UsuarioVO usuario) throws Exception;

	Integer consultaTotalPorDataModeloUnidadeEnsinoBiblioteca(DataModelo dataModelo, String tipoPessoa,
			List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;

	List<FuncionarioVO> consultaPorDataModeloUnidadeEnsinoBiblioteca(DataModelo dataModelo, String tipoPessoa,
			List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;

	FuncionarioVO consultaRapidaPorCPFUnicaUnidadeEnsinoBiblioteca(String valorConsulta,
			List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;


	public boolean realizarVerificacaoPessoaVinculadaFuncionario(Integer codigoPessoa, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public void adicionarTipoDocumento(List<TipoDocumentoVO> listaTipoDocumentoVOs, TipoDocumentoVO tipoDocumentoVO);

	public List<FuncionarioVO> consultarPorArquivoConsiderandoDocumentoAssinado(ArquivoVO arquivo) throws Exception;

	void persistirFuncionarioComEmailInstituciona(FuncionarioVO obj, UsuarioVO usuario,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
	
	void realizarCriacaoUsuarioPorFuncionario(FuncionarioVO funcionarioVO, UsuarioPerfilAcessoVO usuarioPerfilAcessoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioLogado) throws Exception;
	
	void realizarVerificacaoUsuarioRegistradoLdap(FuncionarioVO obj, UsuarioVO usuarioLdap,String senhaAntesCriptografia, UsuarioVO usuarioLogado) throws Exception;

	List<FuncionarioVO> consultaRapidaResumidaPorEmailInstitucional(String valorConsulta, 
			Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, DataModelo dataModelo,
			UsuarioVO usuario) throws Exception;

	public List<FuncionarioVO> consultaRapidaCoordenadorPorCurso(Integer codigoCurso, Integer codigoUnidadeEnsino, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void carregarDadosSemExcecao(FuncionarioVO obj, UsuarioVO usuario) throws Exception;

}
