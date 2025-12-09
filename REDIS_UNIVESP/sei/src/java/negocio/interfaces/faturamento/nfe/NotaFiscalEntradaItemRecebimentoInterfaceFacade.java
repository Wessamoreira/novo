package negocio.interfaces.faturamento.nfe;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemRecebimentoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemVO;

public interface NotaFiscalEntradaItemRecebimentoInterfaceFacade {

	void persistir(List<NotaFiscalEntradaItemRecebimentoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO);

	List<NotaFiscalEntradaItemRecebimentoVO> consultaRapidaPorNotaFiscalEntrada(NotaFiscalEntradaItemVO notaFiscalEntrada, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	NotaFiscalEntradaItemRecebimentoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	Double consultarQuantidadeNotaFiscalEntradaTotal(NotaFiscalEntradaItemRecebimentoVO obj, UsuarioVO usuario) throws Exception;

}
