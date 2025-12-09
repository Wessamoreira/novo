package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface OperadoraCartaoInterfaceFacade {
	
    public void excluir(OperadoraCartaoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    public List consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public void validarDados(OperadoraCartaoVO obj) throws ConsistirException;
    public OperadoraCartaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorTipo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public OperadoraCartaoVO consultarPorFormaPagamentoNegociacaoRecebimento(Integer formaPagamentoNegociacaoRecebimento, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
	void persistir(OperadoraCartaoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO) throws Exception;
	OperadoraCartaoVO consultarPorCodigoConfiguracaoFinanceiroCartao(Integer codigoConfiguracaoFinanceiroCartao, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	OperadoraCartaoVO consultarPorChavePrimariaUnica(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;
}