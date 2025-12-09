package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.SolicitacaoAberturaTurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoSolicitacaoAberturaTurmaEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface SolicitacaoAberturaTurmaInterfaceFacade {
	
	
	void persitir(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, UsuarioVO usuarioVO) throws Exception;
	
	void registrarNaoAutorizacaoAberturaTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, UsuarioVO usuarioVO) throws Exception;
	
	void registrarAutorizacaoAberturaTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, UsuarioVO usuarioVO) throws Exception;
	
	void registrarRevisaoAberturaTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, UsuarioVO usuarioVO) throws Exception;
	
	List<SolicitacaoAberturaTurmaVO> consultar(Integer unidadeEnsino, Integer unidadeEnsinoCurso, String turma, SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAberturaTurmaEnum, Integer limite, Integer offset, boolean verificarPermissao, UsuarioVO usuarioVO) throws Exception;
	
	void validarDados(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO) throws ConsistirException, Exception;
	
	void adicionarSolicitacaoAberturaTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO) throws Exception;

	void registrarRevisaoRealizadaAberturaTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, UsuarioVO usuarioVO) throws Exception;

	Integer consultarTotalRegistro(Integer unidadeEnsino, Integer unidadeEnsinoCurso, String turma, SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAberturaTurmaEnum) throws Exception;

	void realizarGeracaoCalendarioSolicitacaoAberturaTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO) throws Exception;

	void realizarEnvioNotificacao(ComunicacaoInternaVO comunicacaoInternaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	void registrarFinalizacaoAberturaTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, UsuarioVO usuarioVO) throws Exception;

	SolicitacaoAberturaTurmaVO consultarSolicitacaoAberturaTurmaEmAbertoPorTurma(Integer turma);	
	
	

}
