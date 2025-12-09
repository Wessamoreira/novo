package negocio.interfaces.protocolo;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoDisciplinasAproveitadasVO;
import negocio.comuns.protocolo.RequerimentoVO;

public interface RequerimentoDisciplinasAproveitadasInterfaceFacade {

	void persistir(List<RequerimentoDisciplinasAproveitadasVO> lista, boolean verificarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO);

	List<RequerimentoDisciplinasAproveitadasVO> consultarPorRequerimentoVO(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario);

	void realizarConfirmacaoIndeferido(RequerimentoDisciplinasAproveitadasVO obj, UsuarioVO usuario) throws Exception;

	void realizarConfirmacaoDeferido(RequerimentoDisciplinasAproveitadasVO obj, UsuarioVO usuario) throws Exception;

	void adicionarRequerimentoDisciplinasAproveitadas(RequerimentoVO requerimento, RequerimentoDisciplinasAproveitadasVO rda, UsuarioVO usuario) throws Exception;

	void removerRequerimentoDisciplinasAproveitadas(RequerimentoVO requerimento, RequerimentoDisciplinasAproveitadasVO rda, UsuarioVO usuario) throws Exception;
	
	Integer consultarQtdeRequerimentoDisciplinasAproveitadasPorDisciplinaPorMatriculaPorSituacaoIndeferida(Integer disciplina, String matricula);
	
	boolean consultarSeExisteBloqueioParaDisciplinaAproveitadaPorMatricula(Integer disciplina, String matricula);

	List<RequerimentoDisciplinasAproveitadasVO> consultarRequerimentoDisciplinasAproveitadasIndeferidas(String matricula, Integer disciplina, UsuarioVO usuario);

}
