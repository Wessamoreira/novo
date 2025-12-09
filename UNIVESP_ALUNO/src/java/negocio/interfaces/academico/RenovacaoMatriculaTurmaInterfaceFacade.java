package negocio.interfaces.academico;

import java.util.List;

import controle.arquitetura.AplicacaoControle;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoRenovacaoTurmaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.AcessoException;
import negocio.comuns.utilitarias.ConsistirException;

public interface RenovacaoMatriculaTurmaInterfaceFacade {

	void excluir(RenovacaoMatriculaTurmaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void persistir(RenovacaoMatriculaTurmaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception;
		

	List<RenovacaoMatriculaTurmaVO> consultar(String campoConsulta, String valorConsulta, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception;
	
	
	void realizarInicializacaoProcessamento(RenovacaoMatriculaTurmaVO obj, AplicacaoControle aplicacaoControle, boolean controlarAcesso, UsuarioVO usuarioVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception;
	
	void realizarInterrupcaoProcessamento(RenovacaoMatriculaTurmaVO obj, AplicacaoControle aplicacaoControle, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	boolean realizarVerificacaoRenovacaoTurmaInterrompida(RenovacaoMatriculaTurmaVO obj) throws Exception;
	
	void realizarAtualizacaoDadosProcessamento(RenovacaoMatriculaTurmaVO obj, UsuarioVO usuarioVO) throws Exception;

	void alterar(RenovacaoMatriculaTurmaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception;

	void alterarQuantitativo(RenovacaoMatriculaTurmaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	RenovacaoMatriculaTurmaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<RenovacaoMatriculaTurmaVO> consultarPorSituacao(SituacaoRenovacaoTurmaEnum situacaoRenovacaoTurmaEnum, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO, Integer limite, Integer offset) throws Exception;

	void validarDadosInicioProcessamento(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, AplicacaoControle aplicacaoControle) throws ConsistirException, AcessoException;

	void realizarInicializacaoThreadRenovacaoTurma(RenovacaoMatriculaTurmaVO obj, AplicacaoControle aplicacaoControle, UsuarioVO usuarioVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception;

	Integer consultarTotalRegistro(String campoConsulta, String valorConsulta, UsuarioVO usuarioVO) throws Exception;

	Integer consultarTotalRegistroPorSituacao(SituacaoRenovacaoTurmaEnum situacaoRenovacaoTurmaEnum, UsuarioVO usuarioVO) throws Exception;

	void alterarDadosBasicos(RenovacaoMatriculaTurmaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

}
