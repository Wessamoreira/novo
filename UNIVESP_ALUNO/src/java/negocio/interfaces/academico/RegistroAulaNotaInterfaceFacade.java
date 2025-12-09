package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface RegistroAulaNotaInterfaceFacade {

	public void persistir(List<RegistroAulaVO> registroAulaVOs, ConfiguracaoAcademicoVO configuracaoAcademicoVO, Boolean permiteLancamentoAulaFutura, UsuarioVO usuarioLogado) throws Exception;
        public void marcarDesmarcarAlunoPresenteAula(Boolean controlarMarcarDesmarcarTodos, RegistroAulaVO registroAula, String origemRegistro, UsuarioVO usuario) throws Exception;
        public void calcularMedia(List<RegistroAulaVO> listaAulas, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO) throws Exception;
		Boolean setarNotasDeAcordoComMedia(List<HistoricoVO> listaNotas,  ConfiguracaoAcademicoVO configuracaoAcademicoVO, Boolean listasIguais) throws Exception;

}