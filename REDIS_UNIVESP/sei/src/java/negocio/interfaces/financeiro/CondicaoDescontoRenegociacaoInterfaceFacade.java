package negocio.interfaces.financeiro;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CondicaoDescontoRenegociacaoVO;
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface CondicaoDescontoRenegociacaoInterfaceFacade {

	
	void persistir(CondicaoDescontoRenegociacaoVO obj, boolean verificarAcesso, UsuarioVO usuarioLogado) throws Exception;

	void excluir(CondicaoDescontoRenegociacaoVO obj, boolean verificarAcesso, UsuarioVO usuarioLogado) throws Exception;
	
	List<CondicaoDescontoRenegociacaoVO> consultar(String valorConsulta, String campoConsulta, DataModelo dataModelo) throws Exception;
	
	public CondicaoDescontoRenegociacaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);	
    
	void adicionarObjItemCondicaoDescontoRenegociacaoVOs(CondicaoDescontoRenegociacaoVO objCondicaoDescontoRenegociacaoVO, ItemCondicaoDescontoRenegociacaoVO obj) throws Exception;	

	void removerObjItemCondicaoDescontoRenegociacaoVOs(CondicaoDescontoRenegociacaoVO obj, ItemCondicaoDescontoRenegociacaoVO icdr) throws Exception;

	

}