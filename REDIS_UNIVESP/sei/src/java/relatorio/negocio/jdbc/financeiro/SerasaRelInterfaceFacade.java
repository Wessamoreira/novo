package relatorio.negocio.jdbc.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

import relatorio.negocio.comuns.financeiro.SerasaRelVO;

public interface SerasaRelInterfaceFacade {

	public List<SerasaRelVO> consultarAlunosSerasa(String filtro, Integer unidadeEnsino, Integer curso, Integer turma, String matricula, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

}