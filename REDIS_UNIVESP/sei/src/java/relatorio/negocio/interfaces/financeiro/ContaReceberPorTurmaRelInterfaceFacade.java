package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import relatorio.negocio.comuns.financeiro.ContaReceberRelVO;

public interface ContaReceberPorTurmaRelInterfaceFacade {

	public List<ContaReceberRelVO> criarObjeto(Boolean dataCompetencia, TurmaVO turmaVO, Date dataInicio, Date dataFim, String situacaoContaReceber, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO confFinanVO) throws Exception;

	public SqlRowSet executarConsultaParametrizada(Boolean dataCompetencia, TurmaVO turmaVO, Date dataInicio, Date dataFim, String situacaoContaReceber) throws Exception;

	public void inicializarOrdenacoesRelatorio();

	public Vector getOrdenacoesRelatorio();

	public void setOrdenarPor(int intValue);

        public String caminhoBaseRelatorio();

}