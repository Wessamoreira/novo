package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.RegistroHeaderPagarVO;

public interface RegistroHeaderPagarInterfaceFacade {
	
	
	public void excluir(RegistroHeaderPagarVO obj, UsuarioVO usuario) throws Exception;

	public RegistroHeaderPagarVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	void persistir(RegistroHeaderPagarVO obj, UsuarioVO usuarioVO) throws Exception;

	RegistroHeaderPagarVO consultarPorControleCobrancaPagar(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
