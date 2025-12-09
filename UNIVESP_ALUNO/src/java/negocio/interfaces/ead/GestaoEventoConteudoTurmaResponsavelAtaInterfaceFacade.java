package negocio.interfaces.ead;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaResponsavelAtaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;
import negocio.comuns.ead.enumeradores.FuncaoResponsavelAtaEnum;

public interface GestaoEventoConteudoTurmaResponsavelAtaInterfaceFacade {

	void persistir(List<GestaoEventoConteudoTurmaResponsavelAtaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<GestaoEventoConteudoTurmaResponsavelAtaVO> consultarPorCodigoGestaoEventoConteudoTurmaVO(Integer codigoGestaoEventoConteudoTurmaVO, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	Boolean consultarSeUsuarioResponsavelFuncaoAta(Integer gestaoEventoTurma, FuncaoResponsavelAtaEnum funcao, UsuarioVO usuarioLogado) throws Exception;

	void persistirGestaoEventoConteudoTurmaResponsavelAtaVisaoAluno(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaResponsavelAtaVO responsavel, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	void excluirGestaoEventoConteudoTurmaResponsavelAtaVisaoAluno(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaResponsavelAtaVO responsavel, UsuarioVO usuario) throws Exception;

}
