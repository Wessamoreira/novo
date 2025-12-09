package negocio.interfaces.patrimonio;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.OcorrenciaPatrimonioVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;
import negocio.comuns.patrimonio.PatrimonioVO;
import negocio.comuns.patrimonio.TextoPadraoPatrimonioVO;
import negocio.comuns.patrimonio.enumeradores.TipoUsoTextoPadraoPatrimonioEnum;
import negocio.comuns.utilitarias.ConsistirException;

public interface TextoPadraoPatrimonioInterfaceFacade {

	void persistir(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO, UsuarioVO usuarioVO) throws Exception;
	
	void validarDados(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO) throws ConsistirException;
	
	void excluir(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarClonagem(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO);

	Integer consultarTotal(String campoConsulta, String valorConsulta, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterarSituacaoTextoPadraoPatrimonio(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO);

	List<TextoPadraoPatrimonioVO> consultar(String campoConsulta, String valorConsulta, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Rodrigo Wind - 29/06/2015
	 * @param situacao
	 * @param tipoUso
	 * @return
	 * @throws Exception
	 */
	List<TextoPadraoPatrimonioVO> consultarPorSituacaoTipoUsoCombobox(StatusAtivoInativoEnum situacao, TipoUsoTextoPadraoPatrimonioEnum tipoUso) throws Exception;

	
	/**
	 * @author Rodrigo Wind - 30/06/2015
	 * @param textoPadraoPatrimonioVO
	 * @param patrimonioVO
	 * @param ocorrenciaPatrimonioVO
	 * @param patrimonioUnidadeVOs
	 * @param configuracaoGeralSistemaVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	public String realizarImpressaoTextoPadraoPatrimonio(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO, PatrimonioVO patrimonioVO, OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, List<PatrimonioUnidadeVO> patrimonioUnidadeVOs, LocalArmazenamentoVO localArmazenamentoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception ;

	/**
	 * @author Rodrigo Wind - 31/07/2015
	 * @param textoPadraoPatrimonioVO
	 * @param configuracaoGeralSistemaVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	String realizarImpressaoTextoVisualizacao(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

}
