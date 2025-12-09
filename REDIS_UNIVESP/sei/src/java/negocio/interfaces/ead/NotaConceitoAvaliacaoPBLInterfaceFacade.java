package negocio.interfaces.ead;

import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaAvaliacaoPBLVO;
import negocio.comuns.ead.NotaConceitoAvaliacaoPBLVO;
import negocio.comuns.ead.enumeradores.TipoAvaliacaoPBLEnum;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * @author VictorHugo - 5 de jul de 2016
 *
 */
public interface NotaConceitoAvaliacaoPBLInterfaceFacade {

	/** 
	 * @author VictorHugo - 5 de jul de 2016 
	 * @param notaConceitoAvaliacaoPBLVO
	 * @throws ConsistirException 
	 */
	void validarDados(NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO) throws ConsistirException;
	/**
	 * @author VictorHugo - 5 de jul de 2016 
	 */

	/** 
	 * @author Victor Hugo de Paula Costa - 6 de jul de 2016 
	 * @param notaConceitoAvaliacaoPBLVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void incluir(NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 6 de jul de 2016 
	 * @param notaConceitoAvaliacaoPBLVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void persistir(NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 6 de jul de 2016 
	 * @param notaConceitoAvaliacaoPBLVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void alterar(NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 6 de jul de 2016 
	 * @param notaConceitoAvaliacaoPBLVOs
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void persistirNotaConceitoAvaliacaoPBLVOS(List<NotaConceitoAvaliacaoPBLVO> notaConceitoAvaliacaoPBLVOs, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 6 de jul de 2016 
	 * @param notaConceitoAvaliacaoPBLVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void excluir(NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 6 de jul de 2016 
	 * @param codigoConteudoUnidadePaginaRecursoEducacional
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	List<NotaConceitoAvaliacaoPBLVO> consultarPorCodigoConteudoUnidadePaginaRecursoEducacional(Integer codigoConteudoUnidadePaginaRecursoEducacional, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 6 de jul de 2016 
	 * @param codigo
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	NotaConceitoAvaliacaoPBLVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 13 de jul de 2016 
	 * @param gestaoEventoConteudoTurmaAvaliacaoPBLVO
	 * @param conteudoUnidadePaginaRecursoEducacionalVO
	 * @throws Exception 
	 */
	void validarNotaMinimaMaximaConceitoAlunoAvaliaAluno(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 26 de jul de 2016 
	 * @param gestaoEventoConteudoTurmaAvaliacaoPBLVO
	 * @param conteudoUnidadePaginaRecursoEducacionalVO
	 * @throws Exception 
	 */
	void validarNotaMinimaMaximaConceitoAutoAvaliacao(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 26 de jul de 2016 
	 * @param gestaoEventoConteudoTurmaAvaliacaoPBLVO
	 * @param conteudoUnidadePaginaRecursoEducacionalVO
	 * @throws Exception 
	 */
	void validarNotaMinimaMaximaConceitoProfAvaliaAluno(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 8 de ago de 2016 
	 * @param notaConceitoAvaliacaoPBLVOs
	 * @param tipoAvaliacaoPBL
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	List<SelectItem> montarComboboxNotaConceito(List<NotaConceitoAvaliacaoPBLVO> notaConceitoAvaliacaoPBLVOs, TipoAvaliacaoPBLEnum tipoAvaliacaoPBL, UsuarioVO usuarioVO) throws Exception;
	
	List<NotaConceitoAvaliacaoPBLVO> consultarPorCodigoConteudoUnidadePaginaRecursoEducacionalPorTipoAvaliacao(Integer codigoConteudoUnidadePaginaRecursoEducacional, TipoAvaliacaoPBLEnum tipoAvaliacao, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
}
