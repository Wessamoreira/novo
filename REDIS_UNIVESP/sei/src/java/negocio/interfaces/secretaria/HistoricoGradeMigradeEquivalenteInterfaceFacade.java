package negocio.interfaces.secretaria;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.secretaria.HistoricoGradeMigradaEquivalenteVO;

public interface HistoricoGradeMigradeEquivalenteInterfaceFacade {
	public void incluir(final HistoricoGradeMigradaEquivalenteVO obj, UsuarioVO usuario) throws Exception;
	public void alterar(final HistoricoGradeMigradaEquivalenteVO obj, UsuarioVO usuario) throws Exception;
	public void excluir(HistoricoGradeMigradaEquivalenteVO obj, UsuarioVO usuario) throws Exception;
	public void validarDados(HistoricoGradeMigradaEquivalenteVO obj) throws Exception;
	public void incluirHistoricoGradeMigradaEquivalente(Integer transferenciaMatrizCurricular, List objetos, UsuarioVO usuario) throws Exception;
	public List<HistoricoGradeMigradaEquivalenteVO> consultarPorMatriculaHistoricoGradeMigradaEquivalenteTransferidasGrade(String matricula, Integer gradeAtual, UsuarioVO usuarioVO) ;
	void excluirComBaseNaMatriculaPeriodo(Integer matriculaPeriodo, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

}
