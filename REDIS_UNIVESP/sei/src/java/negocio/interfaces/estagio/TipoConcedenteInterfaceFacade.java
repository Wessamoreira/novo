package negocio.interfaces.estagio;

import controle.arquitetura.DataModelo;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.estagio.TipoConcedenteVO;

public interface TipoConcedenteInterfaceFacade {

	void persistir(TipoConcedenteVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);

	void excluir(TipoConcedenteVO obj, boolean verificarAcesso, UsuarioVO usuario);

	void consultar(DataModelo dataModelo, TipoConcedenteVO obj) throws Exception;

	TipoConcedenteVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

}
