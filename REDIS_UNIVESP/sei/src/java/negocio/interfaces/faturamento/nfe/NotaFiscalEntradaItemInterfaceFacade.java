package negocio.interfaces.faturamento.nfe;

import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO;

public interface NotaFiscalEntradaItemInterfaceFacade {

	void persistir(List<NotaFiscalEntradaItemVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) ;

	void excluir(NotaFiscalEntradaItemVO obj, boolean verificarAcesso, UsuarioVO usuario) ;

	List<NotaFiscalEntradaItemVO> consultaRapidaPorProdutoServico(Integer compra, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) ;

	List<NotaFiscalEntradaItemVO> consultaRapidaPorNotaFiscalEntrada(Integer notaFiscalEntrada, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) ;

	NotaFiscalEntradaItemVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) ;

	void excluidoRegistroNaoExistenteLista(NotaFiscalEntradaVO nfe, UsuarioVO usuario);

	public void gerarNotaFiscalEntradaItemRecebimento(NotaFiscalEntradaItemVO nfei, UsuarioVO usuario);

	void adicionarCentroResultadoOrigemPorArquivoImportacao(FileUploadEvent upload,
			NotaFiscalEntradaVO notaFiscalEntradaVO, NotaFiscalEntradaItemVO notaFiscalEntradaItemVO,
			UsuarioVO usuarioVO) throws Exception;
	
	

}