package negocio.interfaces.financeiro;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoFuncionarioVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface CondicaoRenegociacaoFuncionarioInterfaceFacade {
	

    public void validarDados(CondicaoRenegociacaoFuncionarioVO obj) throws ConsistirException;
    public CondicaoRenegociacaoFuncionarioVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario, int nivelMontarDados ) throws Exception;
    public void setIdEntidade(String aIdEntidade);
	void incluirCondicaoRenegociacaoFuncionarioVOs(Integer condicaoRenegociacaoPrm, List<CondicaoRenegociacaoFuncionarioVO> objetos) throws Exception;
	List consultarCondicaoRenegociacaoFuncionarioPorCondicaoRenegociacao(Integer condicaoRenegociacao, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	void alterarCondicaoRenegociacaoFuncionarioVOs(Integer condicaoRenegociacao, List<CondicaoRenegociacaoFuncionarioVO> objetos) throws Exception;
    
}