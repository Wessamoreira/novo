package negocio.interfaces.arquitetura;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.SolicitarAlterarSenhaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ProgressBarVO;

public interface SolicitarAlterarSenhaInterfaceFacede {

	public void persistir(SolicitarAlterarSenhaVO solicitarAlterarSenhaVO, Boolean verificarAcesso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public SolicitarAlterarSenhaVO consultarSolicitarAlterarSenhaPorCodigo(SolicitarAlterarSenhaVO solicitarAlterarSenhaVO, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public void excluir(SolicitarAlterarSenhaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<SolicitarAlterarSenhaVO> consultar(Date dataInicial, Date dataFinal, String valorConsulta, String campoConsulta, Integer limite, Integer offset, boolean verificarAcesso, UsuarioVO usuarioVO, SolicitarAlterarSenhaVO solicitarAlterarSenhaVO, int nivelMontarDados) throws Exception;

	public List<SolicitarAlterarSenhaVO> consultarSolicitarSenhaPorDataSolicitacao(Date dataInicial, Date dataFinal, Integer limite, Integer offset, boolean verificarAcesso, UsuarioVO usuarioVO, int nivelMontarDados) throws Exception;

	public List<SolicitarAlterarSenhaVO> consultarSolicitarSenhaPorTipoUsuario(String valorConsulta, boolean verificarAcesso, Integer limite, Integer offset, UsuarioVO usuarioVO, int nivelMontarDados) throws Exception;

	public Integer consultarTotal(Date dataInicial, Date dataFinal, String campoConsulta, String valorConsulta, boolean verificarAcesso, UsuarioVO usuarioVO, SolicitarAlterarSenhaVO solicitarAlterarSenhaVO) throws Exception;

	public List<SolicitarAlterarSenhaVO> consultarSolicitarSenhaPorResponsavel(String valorConsulta, boolean verificarAcesso, Integer limite, Integer offset, UsuarioVO usuarioVO, int nivelMontarDados) throws Exception;
	
	public void realizarSolicitacaoNovaSenhaUsuario(ProgressBarVO progressBarVO, SolicitarAlterarSenhaVO obj, Integer qtdeTotalUsuarioSolicitarNovaSenha, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
	
	public void realizarExclusaoSolicitacaoNovaSenhaUsuario(ProgressBarVO progressBarVO, SolicitarAlterarSenhaVO obj, Integer qtdeTotalUsuarioSolicitarNovaSenha, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
	
	public ProgressBarVO consultarProgressBarAtivo();

}
