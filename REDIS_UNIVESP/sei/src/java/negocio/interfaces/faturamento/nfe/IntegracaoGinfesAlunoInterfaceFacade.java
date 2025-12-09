package negocio.interfaces.faturamento.nfe;

import java.io.File;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesAlunoVO;

public interface IntegracaoGinfesAlunoInterfaceFacade {

	void persistir(IntegracaoGinfesAlunoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(IntegracaoGinfesAlunoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	void importar(final IntegracaoGinfesAlunoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	String executarGeracaoArquivoImportacao(IntegracaoGinfesAlunoVO obj, int limite, UsuarioVO usuario) throws Exception;
	
	IntegracaoGinfesAlunoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<IntegracaoGinfesAlunoVO> consultar(Integer unidadeEnsino, String anoReferencia, String mesReferencia, Boolean naoImportados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public File realizarGeracaoRelatorioExcelIntegracaoGinfesAluno(IntegracaoGinfesAlunoVO integracaoGinfesAlunoVO, String urlLogoPadraoRelatorio,  boolean isAlunosComErro, UsuarioVO usuario) throws Exception;

}
