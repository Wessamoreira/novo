package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface PersonalizacaoMensagemAutomaticaUnidadeEnsinoInterfaceFacade {

	PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO novo() throws Exception;

	void incluir(PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO obj) throws Exception;

	void alterar(PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO obj) throws Exception;
	
	void incluirPersonalizacaoMensagemAutomaticaUnidadeEnsino(Integer codigoPersonalizacaoMensagemAutomatica, List<PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO> personalizacaoMensagemAutomaticaUnidadeEnsinoVOs) throws Exception;
	
	void alterarPersonalizacaoMensagemAutomaticaUnidadeEnsino(Integer codigoPersonalizacaoMensagemAutomatica, List<PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO> personalizacaoMensagemAutomaticaUnidadeEnsinoVOs) throws Exception;

	List consultarPersonalizacaoMensagemAutomaticaUnidadeEnsinoPorPersonalizacaoMensagemAutomatica(Integer personalizacaoMensagemAutomatica, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
	void setIdEntidade(String idEntidade);

}