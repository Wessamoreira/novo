package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TipoAdvertenciaVO;
import negocio.comuns.academico.enumeradores.SituacaoTipoAdvertenciaEnum;
import negocio.comuns.arquitetura.UsuarioVO;

public interface TipoAdvertenciaInterfaceFacade {

	void persistir(TipoAdvertenciaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(TipoAdvertenciaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<TipoAdvertenciaVO> consultarPorDescricao(String descricao, SituacaoTipoAdvertenciaEnum situacao, Boolean controlarAcesso, UsuarioVO usuarioVO,int nivelMontarDados) throws Exception;

	TipoAdvertenciaVO consultarPorCodigo(Integer codigo, SituacaoTipoAdvertenciaEnum situacao, Boolean controlarAcesso, UsuarioVO usuarioVO,int nivelMontarDados) throws Exception;

	public TipoAdvertenciaVO consultarNotificacaoEmail(TipoAdvertenciaVO tipoAdvertenciaVO);

}
