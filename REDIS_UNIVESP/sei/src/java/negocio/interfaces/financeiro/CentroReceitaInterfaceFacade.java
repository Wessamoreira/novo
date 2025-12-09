package negocio.interfaces.financeiro;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface CentroReceitaInterfaceFacade {

   
	

    public CentroReceitaVO novo() throws Exception;
    public void incluir(CentroReceitaVO obj, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;
    public void alterar(CentroReceitaVO obj, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;
    public void excluir(CentroReceitaVO obj, UsuarioVO usuarioVO) throws Exception;
    public CentroReceitaVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CentroReceitaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CentroReceitaVO> consultarPorCentroReceitaPrincipal(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CentroReceitaVO> consultarPorCentroReceitaPrincipalFilho(Integer codigo,boolean controlarAcesso,  int NIVELMONTARDADOS_DADOSBASICOS, UsuarioVO usuario) throws Exception;
    public List<CentroReceitaVO> consultarPorDescricao(String valorConsulta,boolean controlarAcesso,  int NIVELMONTARDADOS_DADOSBASICOS, UsuarioVO usuario) throws Exception;
    public List<CentroReceitaVO> consultarPorIdentificadorCentroReceita(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CentroReceitaVO> consultarPorIdentificadorCentroReceitaCentroReceita(String valorConsulta,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CentroReceitaVO> consultarPorNomeDepartamento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
public List<CentroReceitaVO> consultarUltimaMascaraGerada(String mascaraConsulta, String mascaraPlanoContaPrincipal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)throws Exception;
public CentroReceitaVO consultarPorCodigoCentroReceitaPrincipal(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
/**
 * @author Rodrigo Wind - 14/03/2016
 * @param unidadeEnsinoVOs
 * @param nivelMontarDados
 * @param usuarioVO
 * @return
 * @throws Exception
 */
List<CentroReceitaVO> consultarCentroReceitaVinculadoContaReceber(List<UnidadeEnsinoVO> unidadeEnsinoVOs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
CentroReceitaVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,
		UsuarioVO usuario) throws Exception;
}