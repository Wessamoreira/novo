package negocio.interfaces.academico;

import java.util.List;

import controle.academico.RenovarMatriculaControle;
import negocio.comuns.academico.CondicaoPagamentoPlanoDescontoVO;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import webservice.servicos.CondicaoPagamentoRSVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface CondicaoPagamentoPlanoFinanceiroCursoInterfaceFacade {

    public CondicaoPagamentoPlanoFinanceiroCursoVO novo() throws Exception;

    public void incluir(CondicaoPagamentoPlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(CondicaoPagamentoPlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(CondicaoPagamentoPlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception;

    public CondicaoPagamentoPlanoFinanceiroCursoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CondicaoPagamentoPlanoFinanceiroCursoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public void excluirCondicaoPagamentoNaoUtilizada(Integer codigo, List<CondicaoPagamentoPlanoFinanceiroCursoVO> condicaoPagamentoPlanoFinanceiroCursoVOs, UsuarioVO usuario) throws Exception;

    public List<CondicaoPagamentoPlanoFinanceiroCursoVO> consultarPorCodigoPlanoFinanceiroCurso(Integer codigo, String situacao, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void validarDadosAdicionarCondicaoPagamentoPlanoFinanceiroCurso(CondicaoPagamentoPlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws ConsistirException;

    public void realizarAtivacaoCondicaoPagamento(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoVO, Boolean ativado, UsuarioVO usuarioVO) throws Exception;

    public void realizarInativacaoCondicaoPagamento(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoVO, Boolean ativado, UsuarioVO usuarioVO) throws Exception;

    public void excluirCondicaoPagamentoNaoUtilizadaMatriculaPeriodo(Integer planoFinanceiroCursoPrm, List<CondicaoPagamentoPlanoFinanceiroCursoVO> objetosPersistir, UsuarioVO usuario) throws Exception;

    void adicionarCondicaoPlanoFinanceiroCursoTurma(CondicaoPagamentoPlanoFinanceiroCursoVO obj, Integer turma) throws Exception;

	Boolean consultarCondicaoPlanoAlunoControlaMatriculaPorMatriculaPeriodo(Integer matriculaPeriodo) throws Exception;
	
	public Boolean consultarAplicarCalculoComBaseDescontosCalculadosPorContaReceber(Integer contaReceber, UsuarioVO usuarioVO) throws Exception;
	
	public void adicionarItemPlanoDesconto(CondicaoPagamentoPlanoDescontoVO condicaoplanoDescontoVO, List<CondicaoPagamentoPlanoDescontoVO> condicaoPagamentoPlanoDescontoVOs, UsuarioVO usuarioVO) throws Exception;

	List<CondicaoPagamentoPlanoFinanceiroCursoVO> consultarCondicaoPagamentoPlanoFinanceiroCursoFiltrarRenovacaoTurmaNivelCombobox(Integer unidadeEnsino, Integer curso, Integer turma, Integer gradeCurricular, Integer planoFinanceiroCurso, String ano, String semestre) throws Exception;
		
	public List<CondicaoPagamentoPlanoFinanceiroCursoVO> consultarPorCodigoUnidadeEnsino(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	
	/**
	 * Consulta as categorias da condicao de pagamento do plano financeiro curso que estao preenchidas
	 *  
	 * @param codigoPlanoFinanceiroCurso
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	public List<String> consultarCategoriaDasCondicoesDePagamentoDoPlanoFinanceiroCurso(Integer codigoPlanoFinanceiroCurso, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	
	/**
	 * 
	 *  Consulta as condicoes de pagamento do plano financeiro curso pelo codigoPlanoFinanceiroCurso
	 *  Opcao de consultar tambem por:
	 *  								@param categoria
	 * 									@param situacao
	 *  
	 * @param codigoPlanoFinanceiroCurso
	 * @param categoria
	 * @param situacao
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	public List<CondicaoPagamentoPlanoFinanceiroCursoVO> consultarPorCodigoPlanoFinanceiroCursoECategoria(Integer codigoPlanoFinanceiroCurso, String categoria, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	CondicaoPagamentoPlanoFinanceiroCursoVO consultarPorMatriculaPeriodo(Integer matriculaPeriodo, UsuarioVO usuarioVO);

	List<CondicaoPagamentoRSVO> consultarCondicaoPagamentoPlanoFinanceiroCursoParaMatriculaOnlineProcessoSeletivo(
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, RenovarMatriculaControle renovarMatriculaControle);
}