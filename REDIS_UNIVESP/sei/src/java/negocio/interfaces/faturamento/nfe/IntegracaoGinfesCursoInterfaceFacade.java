package negocio.interfaces.faturamento.nfe;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesCursoVO;

public interface IntegracaoGinfesCursoInterfaceFacade {

	void persistir(IntegracaoGinfesCursoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(IntegracaoGinfesCursoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	void importar(final IntegracaoGinfesCursoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	String executarGeracaoArquivoImportacao(IntegracaoGinfesCursoVO obj, int limite, UsuarioVO usuario) throws Exception;
	
	IntegracaoGinfesCursoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<IntegracaoGinfesCursoVO> consultar(Integer unidadeEnsino, Date dataInicio, Date dataFim, Boolean naoImportados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

}
