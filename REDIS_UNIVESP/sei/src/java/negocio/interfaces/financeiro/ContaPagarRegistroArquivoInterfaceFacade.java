package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarRegistroArquivoVO;

public interface ContaPagarRegistroArquivoInterfaceFacade {

	void persistir(List<ContaPagarRegistroArquivoVO> lista, UsuarioVO usuario) throws Exception;

	void excluir(ContaPagarRegistroArquivoVO obj, UsuarioVO usuario) throws Exception;

	List<ContaPagarRegistroArquivoVO> consultarPorControleCobrancaPagar(Integer controleCobrancaPagar, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	ContaPagarRegistroArquivoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
