package negocio.interfaces.arquitetura;

import negocio.comuns.arquitetura.SimularAcessoAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface SimularAcessoAlunoInterfaceFacade {

	void incluir(SimularAcessoAlunoVO obj, UsuarioVO usuario) throws Exception;

	void alterar(SimularAcessoAlunoVO obj, UsuarioVO usuario) throws Exception;

	SimularAcessoAlunoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void incluir(SimularAcessoAlunoVO obj, boolean verificarPermissao, UsuarioVO usuario) throws Exception;

	void alterar(SimularAcessoAlunoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

}
