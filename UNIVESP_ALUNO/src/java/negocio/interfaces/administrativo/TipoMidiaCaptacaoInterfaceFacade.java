package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe
 * Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de
 * padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir
 * os dados das classes VO).
 */
public interface TipoMidiaCaptacaoInterfaceFacade {

	TipoMidiaCaptacaoVO novo() throws Exception;

	void incluir(TipoMidiaCaptacaoVO obj, UsuarioVO usuario) throws Exception;

	void alterar(TipoMidiaCaptacaoVO obj, UsuarioVO usuario) throws Exception;

	void excluir(TipoMidiaCaptacaoVO obj, UsuarioVO usuarioVO) throws Exception;

	TipoMidiaCaptacaoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<TipoMidiaCaptacaoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<TipoMidiaCaptacaoVO> consultarPorNomeMidia(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void setIdEntidade(String aIdEntidade);
}