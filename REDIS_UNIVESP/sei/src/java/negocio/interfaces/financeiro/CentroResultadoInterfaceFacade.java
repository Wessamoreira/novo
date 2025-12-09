package negocio.interfaces.financeiro;

import java.util.List;

import org.richfaces.event.FileUploadEvent;

import controle.arquitetura.DataModelo;
import controle.arquitetura.TreeNodeCustomizado;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.CentroResultadoRestricaoUsoVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

public interface CentroResultadoInterfaceFacade {

	void persistir(CentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);

	void excluir(CentroResultadoVO obj, boolean retornarRestricao, boolean verificarAcesso, UsuarioVO usuario);	

	List<CentroResultadoVO> consultaRapidaPorIdentificadorCentroResultado(String numero, SituacaoEnum situacaoEnum,  boolean isValidarRestricaoUsoCentroResultado,DepartamentoVO departamento, CursoVO curso, TurmaVO turma, DataModelo dataModelo);

	Integer consultarTotalPorIdentificadorCentroResultado(String valorConsulta, SituacaoEnum situacaoEnum,  boolean isValidarRestricaoUsoCentroResultado,DepartamentoVO departamento, CursoVO curso, TurmaVO turma, DataModelo dataModelo);	

	List<CentroResultadoVO> consultaRapidaPorDescricaoCentroResultado(String numero, SituacaoEnum situacaoEnum,  boolean isValidarRestricaoUsoCentroResultado,DepartamentoVO departamento, CursoVO curso, TurmaVO turma, DataModelo dataModelo);

	Integer consultarTotalPorDescricaoCentroResultado(String valorConsulta, SituacaoEnum situacaoEnum, boolean isValidarRestricaoUsoCentroResultado,DepartamentoVO departamento, CursoVO curso, TurmaVO turma, DataModelo dataModelo);

	List<CentroResultadoVO> consultaRapidaPorDescricaoCentroResultadoSuperior(String numero, SituacaoEnum situacaoEnum,  boolean isValidarRestricaoUsoCentroResultado,DepartamentoVO departamento, CursoVO curso, TurmaVO turma, DataModelo dataModelo);

	Integer consultarTotalPorDescricaoCentroResultadoSuperior(String valorConsulta, SituacaoEnum situacaoEnum,  boolean isValidarRestricaoUsoCentroResultado, DepartamentoVO departamento, CursoVO curso, TurmaVO turma, DataModelo dataModelo);
	
	Integer consultarMaiorNivelCentroResultado(UsuarioVO usuario);
	
	StringBuilder getSQLPadraoConsultaArvoreCentroResultadoPorNivel();
	
	CentroResultadoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void validarNivelCentroResultadoSuperior(CentroResultadoVO obj, CentroResultadoVO centroResultadoSuperior);

	void consultar(SituacaoEnum situacaoEnum,  boolean isValidarRestricaoUsoCentroResultado, DepartamentoVO departamento, CursoVO curso, TurmaVO turma, DataModelo dataModelo);

	TreeNodeCustomizado consultarArvoreCentroResultadoSuperior(CentroResultadoVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO);

	TreeNodeCustomizado consultarArvoreCentroResultadoInferior(CentroResultadoVO obj, SituacaoEnum situacaoEnum, Boolean controlarAcesso, UsuarioVO usuarioVO);
	
	List<CentroResultadoVO>  consultarCentroResultadoPorArvoreCompleta(CentroResultadoVO obj, SituacaoEnum situacaoEnum, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	CentroResultadoVO validarGeracaoDoCentroResultadoAutomatico(String descricao, Integer unidadeEnsino, TipoNivelEducacional tipoNivelEducacional, Integer curso, boolean isValidarCentroResultadoSuperior, UsuarioVO usuarioVO);

	CentroResultadoVO consultarCentroResultadoPorUnidadeEnsino(Integer unidadeEnsino, UsuarioVO usuario);

	CentroResultadoVO consultarCentroResultadoPorDepartamento(Integer departamento, UsuarioVO usuario);

	CentroResultadoVO consultarCentroResultadoPorTurma(Integer turma, UsuarioVO usuario);

	CentroResultadoVO consultarCentroResultadoPorUnidadeEnsinoCurso(Integer unidadeEnsino, Integer curso, UsuarioVO usuario);

	CentroResultadoVO consultaCentroResultadoPadraoConfiguracaoFinanceiroPorUnidadeEnsino(Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	
	List<CentroResultadoVO> consultaResultadoEstoque(DataModelo dataModelo) throws Exception;
	
	void adicionarCentroResultadoRestricaoUso(CentroResultadoVO obj, CentroResultadoRestricaoUsoVO crru, UsuarioVO usuario);

	void removerCentroResultadoRestricaoUso(CentroResultadoVO obj, CentroResultadoRestricaoUsoVO crru, UsuarioVO usuario);

	boolean validarRestricaoUsoCentroResultado(CentroResultadoVO obj, DepartamentoVO departamento, CursoVO curso, TurmaVO turma, UsuarioVO usuario);

	CentroResultadoVO consultarCentroResultadoEstoquePorUnidadeEnsino(Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	boolean validarLoopCentroResultadoPrincipal(CentroResultadoVO obj);

	void validarGeracaoDoCentroResultadoSuperiorAutomatico(CentroResultadoVO obj, CentroResultadoVO centroResultadoSuperior, UsuarioVO usuarioVO);

	CentroResultadoVO consultarCentroResultadoPadraoRequerimentoPorUnidadeEnsino(Integer unidadeEnsino, Integer tipoRequerimento, UsuarioVO usuario);

	CentroResultadoVO consultarCentroResultadoPadraoBibliotecaPorEmprestimo(Integer emprestimo, UsuarioVO usuario);

	CentroResultadoVO consultarCentroResultadoPorContratoReceita(Integer contratosreceitas, UsuarioVO usuario);

	CentroResultadoVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario);
	
	CentroResultadoVO consultarCentroResultadoArquivoExcel(String descricaoCentroResultado, Integer codigoUnidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<CentroResultadoOrigemVO> realizarLeituraArquivoExcel(FileUploadEvent upload, UnidadeEnsinoVO unidadeEnsinoVO,  UsuarioVO usuarioLogado,   ConsistirException consistirException) throws Exception;

	void realizarCarregamentoDadosCentroResultadoOrigemPadraoCategoriaDespesa(CentroResultadoOrigemVO centroResultadoOrigemVO,
			CategoriaDespesaVO categoriaDespesaVO, UsuarioVO usuario) throws Exception;

	CentroResultadoVO consultarCentroResultadoPorUnidadeEnsinoNivelEducacional(Integer unidadeEnsino,
			TipoNivelEducacional tipoNivelEducacional, UsuarioVO usuario);
	

}
