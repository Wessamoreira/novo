package negocio.interfaces.estagio;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.QuestionarioRespostaOrigemMotivosPadroesEstagioVO;
import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.estagio.MotivosPadroesEstagioVO;

public interface MotivosPadroesEstagioInterfaceFacade {

	void persistir(MotivosPadroesEstagioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);

	void excluir(MotivosPadroesEstagioVO obj, boolean verificarAcesso, UsuarioVO usuario);

	void consultar(DataModelo dataModelo, MotivosPadroesEstagioVO obj) throws Exception;

	MotivosPadroesEstagioVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);
	
	List<MotivosPadroesEstagioVO> consultarMotivosPadroesEstagioUtilizarTipoComponente(TipoEstagioEnum tipoEstagio, Boolean retorno, Boolean indeferido, List<QuestionarioRespostaOrigemMotivosPadroesEstagioVO> listaFiltrar, UsuarioVO usuario );

}
