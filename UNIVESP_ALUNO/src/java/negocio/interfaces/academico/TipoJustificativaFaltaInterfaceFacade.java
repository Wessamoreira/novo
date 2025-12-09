package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TipoJustificativaFaltaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface TipoJustificativaFaltaInterfaceFacade {

	public void setIdEntidade(String aIdEntidade);

	public void incluir(TipoJustificativaFaltaVO tipoJustificativaFaltaVO, Boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	public void alterar(TipoJustificativaFaltaVO tipoJustificativaFaltaVO, Boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	public void excluir(TipoJustificativaFaltaVO tipoJustificativaFaltaVO, Boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	public TipoJustificativaFaltaVO consultarPorChavePrimeira(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}
