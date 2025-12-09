package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.RegistroTrailerPagarVO;

public interface RegistroTrailerPagarInterfaceFacade {

    public void excluir(RegistroTrailerPagarVO obj, UsuarioVO usuario) throws Exception;

    public RegistroTrailerPagarVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

	void persistir(RegistroTrailerPagarVO obj, UsuarioVO usuarioVO) throws Exception;

	RegistroTrailerPagarVO consultarPorControleCobrancaPagar(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}
