package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.RegistroDetalhePagarVO;

public interface RegistroDetalhePagarInterfaceFacade {

	void excluir(RegistroDetalhePagarVO obj, UsuarioVO usuario) throws Exception;	

	void excluirRegistroDetalhePagars(Integer registroArquivo, UsuarioVO usuario) throws Exception;	

	void persistir(List<RegistroDetalhePagarVO> lista, UsuarioVO usuario) throws Exception;

	List<RegistroDetalhePagarVO> consultarPorRegistroHeaderLotePagar(Integer registroHeaderLotePagar, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	RegistroDetalhePagarVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
