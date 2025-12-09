package negocio.interfaces.basico;

import java.util.List;

import org.primefaces.event.FileUploadEvent;

import com.lowagie.text.pdf.BaseFont;



import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaTagVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.basico.enumeradores.TagEtiquetaEnum;

public interface LayoutEtiquetaInterfaceFacade {

	void excluir(LayoutEtiquetaVO obj, UsuarioVO usuarioLogado) throws Exception;

	void incluir(LayoutEtiquetaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	void alterar(LayoutEtiquetaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	void persistir(LayoutEtiquetaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	LayoutEtiquetaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	Integer consultarTotalRegistrosPorCodigo(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;

	List<LayoutEtiquetaVO> consultar(boolean controlarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	List<LayoutEtiquetaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	void adicionarObjLayoutEtiquetaTagVOs(LayoutEtiquetaVO objLayoutEtiquetaVO, TagEtiquetaEnum tagEtiquetaEnum) throws Exception;

	void excluirObjLayoutEtiquetaTagVOs(LayoutEtiquetaVO objLayoutEtiquetaVO, LayoutEtiquetaTagVO layoutEtiquetaTagVO) throws Exception;

	void validarDados(LayoutEtiquetaVO obj) throws Exception;

	void realizarUpperCaseDados(LayoutEtiquetaVO obj);

	List<LayoutEtiquetaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	void realizarMontagemPreviewEtiqueta(LayoutEtiquetaVO layoutEtiqueta, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	List<LayoutEtiquetaVO> consultarRapidaPorModulo(ModuloLayoutEtiquetaEnum moduloLayoutEtiquetaEnum, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	void realizarAlteracaoOrdemLayoutEtiquetaTag(LayoutEtiquetaVO layoutEtiquetaVO, LayoutEtiquetaTagVO layoutEtiquetaTagVO, boolean subir);

	void upLoadArquivo(FileUploadEvent uploadEvent, LayoutEtiquetaVO layoutEtiquetaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Rodrigo Wind - 14/03/2016
	 * @param uploadEvent
	 * @return
	 * @throws Exception
	 */
	LayoutEtiquetaVO realizarImportacaoLayout(FileUploadEvent uploadEvent) throws Exception;

	/**
	 * @author Rodrigo Wind - 14/03/2016
	 * @param layoutEtiquetaVO
	 * @return
	 * @throws Exception
	 */
	String realizarExportacaoLayout(LayoutEtiquetaVO layoutEtiquetaVO) throws Exception;
	
	float realizarCalculoLarguraTextoConformeTamanhoFonte(float tamanhoFonte, BaseFont baseFont, String texto);
}
