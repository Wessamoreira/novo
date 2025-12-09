/**
 * 
 */
package negocio.interfaces.patrimonio;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;
import negocio.comuns.patrimonio.TipoPatrimonioVO;

/**
 * @author Rodrigo Wind
 *
 */
public interface MapaPatrimonioSeparadoDescarteInterfaceFacade {

	List<PatrimonioUnidadeVO> consultarPatrimonioUnidadeVOs(Integer unidadeEnsino, LocalArmazenamentoVO localArmazenamentoVO, Boolean incluirSubLocal, TipoPatrimonioVO tipoPatrimonioVO, String codigoBarra, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	List<PatrimonioUnidadeVO> realizarSeparacaoPatrimonioUnidadeSelecionado(List<PatrimonioUnidadeVO> patrimonioUnidadeVOs) throws Exception ;
	void persistir(List<PatrimonioUnidadeVO> patrimonioUnidadeVOs, String observacao, LocalArmazenamentoVO localArmazenamentoDestinoVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	void realizarMarcarDesmarcarTodosPatrimonioUnidade(List<PatrimonioUnidadeVO> patrimonioUnidadeVOs, boolean selecionar);
	
		
	
}
