package negocio.interfaces.basico;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.richfaces.event.FileUploadEvent;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LinksUteisVO;
import negocio.comuns.basico.UsuarioLinksUteisVO;
import negocio.comuns.utilitarias.ProgressBarVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface LinksUteisInterfaceFacade {

	public LinksUteisVO novo() throws Exception;

	public void incluir(LinksUteisVO linksUteisVO, boolean controleAcesso, UsuarioVO usuarioVO) throws Exception;

	public void alterar(LinksUteisVO linksUteisVO, boolean controleAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(LinksUteisVO linksUteisVO, boolean controleAcesso, UsuarioVO usuarioVO) throws Exception;
	
	
	public List<LinksUteisVO> consultarPorDescricao(String valorConsulta, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public List<LinksUteisVO> consultarPorLink(String valorConsulta, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	public void realizarProcessamentoExcelPlanilha(FileUploadEvent uploadEvent, XSSFWorkbook xssfWorkbook, HSSFWorkbook hssfWorkbook, boolean importarPorCPF, boolean importarPorEmailInstitucional, LinksUteisVO linksUteisVO, ProgressBarVO progressBarVO, UsuarioVO usuario) throws Exception;

	void adicionarUsuarioLinkUteisVO(LinksUteisVO linksUteisVO, UsuarioVO usuarioVO) throws Exception;

	void removerUsuarioLinkUteisVO(LinksUteisVO linksUteisVO, UsuarioLinksUteisVO usuarioLinksUteisVO) throws Exception;

	List<LinksUteisVO> consultarLinksUteisUsuario(int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
}
