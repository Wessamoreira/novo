package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.enumeradores.SituacaoPlanoFinanceiroCursoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
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
public interface PlanoFinanceiroCursoInterfaceFacade {

	public PlanoFinanceiroCursoVO novo() throws Exception;

	public void incluir(PlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(PlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(PlanoFinanceiroCursoVO obj, UsuarioVO usuarioVO) throws Exception;

	public PlanoFinanceiroCursoVO consultarPorChavePrimaria(Integer codigo, String situacao, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public PlanoFinanceiroCursoVO consultarUnidadeEnsinoCursoTurno(Integer codigoUnidadeEnsino, Integer codigoCurso, Integer codigoTurno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<PlanoFinanceiroCursoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<PlanoFinanceiroCursoVO> consultarPorDescricao(String descricao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List<PlanoFinanceiroCursoVO> consultarPorCursoETurnoVinculadoTurma(Integer curso, Integer turno, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<PlanoFinanceiroCursoVO> consultarPorCursoETurnoVinculadoUnidadeEnsinoCurso(Integer curso, Integer turno, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<PlanoFinanceiroCursoVO> consultarPorUnidadeEnsino(Integer unidadeEnsino, SituacaoPlanoFinanceiroCursoEnum situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<PlanoFinanceiroCursoVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<PlanoFinanceiroCursoVO> consultaRapidaPorDescricao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, SituacaoPlanoFinanceiroCursoEnum situacao, UsuarioVO usuario) throws Exception;

	public void carregarDados(PlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception;

	public void carregarDados(PlanoFinanceiroCursoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<PlanoFinanceiroCursoVO> consultarPlanoFinanceiroCursoFiltrarRenovacaoTurmaNivelCombobox(Integer unidadeEnsino,  Integer curso, Integer turma, Integer gradeCurricular, String ano, String semestre) throws Exception;

	List<CondicaoPagamentoPlanoFinanceiroCursoVO> inicializarListaSelectItemPlanoFinanceiroCursoParaTurma(Boolean realizandoNovaMatricula, Integer codigoBanner, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception;
	
	public PlanoFinanceiroCursoVO consultarModeloGeracaoPorChavePrimaria(Integer codigoPrm) throws Exception;	
	
	void realizarAtivacaoPlanoFinanceiroCurso(PlanoFinanceiroCursoVO planoFinanceiroCursoVO, UsuarioVO usuarioVO) throws Exception;

	void realizarInativacaoPlanoFinanceiroCurso(PlanoFinanceiroCursoVO planoFinanceiroCursoVO, UsuarioVO usuarioVO) throws Exception;

	List<PlanoFinanceiroCursoVO> consultarPlanoFinanceiroTurmaEspecificaEPlanoFinanceiroAtivoUnidade(Integer turma, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<PlanoFinanceiroCursoVO> consultarPlanoFinanceiroUnidadeEnsinoCursoEspecificaEPlanoFinanceiroAtivoUnidade(Integer unidadeEnsinoCurso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;		
}