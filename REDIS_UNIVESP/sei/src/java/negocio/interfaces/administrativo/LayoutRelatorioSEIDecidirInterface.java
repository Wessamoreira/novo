package negocio.interfaces.administrativo;

import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.administrativo.FiltroPersonalizadoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirVO;
import negocio.comuns.administrativo.LayoutRelatorioSeiDecidirCampoVO;
import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirModuloEnum;
import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirNivelDetalhamentoEnum;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface LayoutRelatorioSEIDecidirInterface {

	public void persistir(LayoutRelatorioSEIDecidirVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void alterar(LayoutRelatorioSEIDecidirVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(LayoutRelatorioSEIDecidirVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public LayoutRelatorioSEIDecidirVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<LayoutRelatorioSEIDecidirVO> consultar(String valorConsulta, String campoConsulta, boolean verificarAcesso, UsuarioVO usuarioVO, LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, int nivelMontarDados, String modulo) throws Exception;

	List<LayoutRelatorioSEIDecidirVO> consultarPorModulo(RelatorioSEIDecidirModuloEnum modulo, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	void realizarAlteracaoOrdemLayoutRelatorioSeiDecidirCampoVOs(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, LayoutRelatorioSeiDecidirCampoVO obj, boolean esquerda) throws Exception;

	void adicionarLayoutRelatorioSeiDecidirCampoVOs(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, LayoutRelatorioSeiDecidirCampoVO obj) throws Exception;

	void removerLayoutRelatorioSeiDecidirCampoVOs(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, LayoutRelatorioSeiDecidirCampoVO obj);

	void alterarOrdemApresentacaoCampo(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO, LayoutRelatorioSeiDecidirCampoVO obj, LayoutRelatorioSeiDecidirCampoVO obj2);

	/**
	 * @author Rodrigo Wind - 29/10/2015
	 * @param layoutRelatorioSEIDecidirVO
	 * @return
	 * @throws Exception
	 */
	String realizarExportacaoLayout(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO) throws Exception;

	/**
	 * @author Rodrigo Wind - 29/10/2015
	 * @param uploadEvent
	 * @return
	 * @throws Exception
	 */
	LayoutRelatorioSEIDecidirVO realizarImportacaoLayout(FileUploadEvent uploadEvent) throws Exception;

	public void persistirTodos(LayoutRelatorioSEIDecidirVO obj, boolean verificarAcesso, UsuarioVO usuarioVO, List<PerfilAcessoVO> listaIdsPerfilAcesso, List<FuncionarioVO> listaIdsFuncionario) throws Exception ;
	
	public List<LayoutRelatorioSEIDecidirVO> consultarTodos(boolean verificarAcesso, UsuarioVO usuarioVO, int nivelMontarDados) throws Exception;

	List<LayoutRelatorioSEIDecidirVO> consultarPorModuloENivelDetalhamento(RelatorioSEIDecidirModuloEnum modulo,
			RelatorioSEIDecidirNivelDetalhamentoEnum nivelDetalhamento, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	void alterarOrdemFiltroPersonalizado(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO,
			FiltroPersonalizadoVO filtroPersonalizadoVO1, FiltroPersonalizadoVO filtroPersonalizadoVO2)
			throws Exception;
}
