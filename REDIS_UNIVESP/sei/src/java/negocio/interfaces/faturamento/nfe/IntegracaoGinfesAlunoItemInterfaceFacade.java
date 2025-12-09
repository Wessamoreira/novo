package negocio.interfaces.faturamento.nfe;

import java.util.List;
import java.util.Map;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesAlunoItemVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesAlunoVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

public interface IntegracaoGinfesAlunoItemInterfaceFacade {

	void incluirAlunos(final IntegracaoGinfesAlunoVO obj, List<IntegracaoGinfesAlunoItemVO> alunos, UsuarioVO usuario) throws Exception;
	
	public void persistirIntegracaoGinfesAlunoItem(IntegracaoGinfesAlunoItemVO obj, boolean verificarAcesso, UsuarioVO usuario) throws ConsistirException;

	void excluirAlunos(IntegracaoGinfesAlunoVO obj, UsuarioVO usuario) throws Exception;
	
	Map<String, List<IntegracaoGinfesAlunoItemVO>> gerarAlunos(IntegracaoGinfesAlunoVO obj, String matricula, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, UsuarioVO usuario) throws Exception;

	List<IntegracaoGinfesAlunoItemVO> consultarPorIntegracao(Integer integracao, UsuarioVO usuario) throws Exception;

	void realizarAtualizacaoAluno(IntegracaoGinfesAlunoVO integracaoGinfesAlunoVO, IntegracaoGinfesAlunoItemVO integracaoGinfesAlunoItemVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, UsuarioVO usuarioVO) throws Exception;
	
	 


}
