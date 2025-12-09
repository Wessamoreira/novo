package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;

import relatorio.negocio.comuns.academico.DadosMatriculaRelVO;

public interface DadosMatriculaRelInterfaceFacade {

	public void inicializarParametros();

	public List<DadosMatriculaRelVO> criarObjeto(Integer matriculaPeriodo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	

}