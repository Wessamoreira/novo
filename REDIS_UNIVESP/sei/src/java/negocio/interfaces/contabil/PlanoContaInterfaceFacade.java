package negocio.interfaces.contabil;

import java.util.List;

import controle.arquitetura.TreeNodeCustomizado;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface PlanoContaInterfaceFacade {

	public PlanoContaVO novo() throws Exception;

	public void gerarIdentificadorMascaraPlanoConta2(PlanoContaVO obj,UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public void obterNivelPlanoConta(PlanoContaVO obj);

	public void incluir(PlanoContaVO obj,UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public void alterar(PlanoContaVO obj, UsuarioVO usuarioVO) throws Exception;

	public List<PlanoContaVO> consultar(Integer unidadeEnsino, String campoConsulta, String valorConsulta,  boolean controlarAcesso, UsuarioVO usuario) throws Exception;
       
	public void excluir(PlanoContaVO obj, UsuarioVO usuarioVO) throws Exception;

	public List<PlanoContaVO> consultaUltimaMascaraGerada(String mascaraConsulta, String mascaraPlanoContaPrincipal, Integer unidadeEnsino, Integer nivelMontarDados,UsuarioVO usuario) throws Exception;		

	public List<PlanoContaVO> consultarPorUnidadeEnsino(String descricao, Integer unidadeEnsino,  boolean controlarAcesso, Integer nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List<PlanoContaVO> consultarPorDescricao(String valorConsulta, Integer unidadeEnsino,  boolean controlarAcesso, Integer nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List<PlanoContaVO> consultarPorDescricaoOrdenarIdentrificador(String valorConsulta, Integer unidadeEnsino, Integer codigoIgnorar,  boolean controlarAcesso, Integer nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List<PlanoContaVO> consultarPorIdentificadorPlanoConta(String valorConsulta, Integer unidadeEnsino,  boolean controlarAcesso, Integer nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List<PlanoContaVO> consultarPorPlanoContaPrincipal(Integer valorConsulta, Integer unidadeEnsino,  boolean controlarAcesso, Integer nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List<PlanoContaVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino,  boolean controlarAcesso, Integer nivelMontarDados,UsuarioVO usuario) throws Exception;	

	public PlanoContaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public PlanoContaVO consultarPorChavePrimaria(Integer codigo, Integer i, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	TreeNodeCustomizado consultarArvorePlanoContaSuperior(PlanoContaVO planoContaVO, Boolean controlarAcesso,
			UsuarioVO usuarioVO) throws Exception;

	TreeNodeCustomizado consultarArvorePlanoContaInferior(PlanoContaVO planoContaVO, Boolean controlarAcesso,
			UsuarioVO usuarioVO) throws Exception;

	List<PlanoContaVO> consultarPorCodigoReduzido(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso,
			Integer nivelMontarDados, UsuarioVO usuario) throws Exception;

	String consultarPosFixoSugestaoPlanoConta(String prefixo, Integer nivelAdicionar) throws Exception;
	
	String caminhoBaseRelatorio();
	
	String designIReportRelatorioExcel();

}