package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.academico.ComprovanteRenovacaoMatriculaVO;

public interface ComprovanteRenovacaoMatriculaRelInterfaceFacade {

	public List<ComprovanteRenovacaoMatriculaVO> criarObjeto(Integer matriculaPeriodo, String matricula, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	public ComprovanteRenovacaoMatriculaVO getComprovanteRenovacaoMatriculaVO();

	public void setComprovanteRenovacaoMatriculaVO(ComprovanteRenovacaoMatriculaVO comprovanteRenovacaoMatriculaVO);
}