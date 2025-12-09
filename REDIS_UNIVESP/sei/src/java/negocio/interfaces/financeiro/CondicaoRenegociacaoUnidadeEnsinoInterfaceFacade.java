package negocio.interfaces.financeiro;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoUnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface CondicaoRenegociacaoUnidadeEnsinoInterfaceFacade {
	

    public void validarDados(CondicaoRenegociacaoUnidadeEnsinoVO obj) throws ConsistirException;
    public CondicaoRenegociacaoUnidadeEnsinoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario, int nivelMontarDados ) throws Exception;
    public void setIdEntidade(String aIdEntidade);
	void incluirCondicaoRenegociacaoUnidadeEnsinoVOs(Integer condicaoRenegociacaoPrm, List<CondicaoRenegociacaoUnidadeEnsinoVO> objetos) throws Exception;
	List consultarCondicaoRenegociacaoUnidadeEnsinoPorCondicaoRenegociacao(Integer condicaoRenegociacao, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	void alterarCondicaoRenegociacaoUnidadeEnsinoVOs(Integer condicaoRenegociacao, List<CondicaoRenegociacaoUnidadeEnsinoVO> objetos) throws Exception;
	CondicaoRenegociacaoUnidadeEnsinoVO consultarCondicaoRenegociacaoUnidadeEnsinoPorCondicaoRenegociacaoPorUnidadeEnsino(Integer condicaoRenegociacao, Integer unidadeensino, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
    
}