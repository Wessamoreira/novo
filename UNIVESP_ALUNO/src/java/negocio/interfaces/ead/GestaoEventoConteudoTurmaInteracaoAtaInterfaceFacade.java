package negocio.interfaces.ead;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaInteracaoAtaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;

public interface GestaoEventoConteudoTurmaInteracaoAtaInterfaceFacade {

	void persistir(List<GestaoEventoConteudoTurmaInteracaoAtaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<GestaoEventoConteudoTurmaInteracaoAtaVO> consultarPorCodigoGestaoEventoConteudoTurmaVO(Integer codigoGestaoEventoConteudoTurmaVO, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void persistirGestaoEventoConteudoTurmaInteracaoAtaVisaoAluno(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaInteracaoAtaVO ata, boolean visaoProfessor, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	void excluirGestaoEventoConteudoTurmaInteracaoAtaVisaoAluno(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaInteracaoAtaVO ata, boolean visaoProfessor, UsuarioVO usuario) throws Exception;

}
