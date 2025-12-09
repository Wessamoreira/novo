package relatorio.negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ParceiroVO;
import relatorio.negocio.comuns.academico.SituacaoFinanceiraAlunoRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

public interface SituacaoFinanceiraAlunoRelInterfaceFacade {

	public List<SituacaoFinanceiraAlunoRelVO> criarObjeto(MatriculaVO matriculaVO, SituacaoFinanceiraAlunoRelVO situacaoFinanceiraAlunoRelVO, String tipoPessoa, ParceiroVO parceiroVO, UsuarioVO usuarioVO, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, PessoaVO responsavelFinanceiro, String ano, String semestre, Date dataInicio, Date dataFim, Boolean filtrarPorDataCompetencia, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, CentroReceitaVO centroReceitaVO, String tipoLayout) throws Exception;

	PessoaVO consultarPessoaResponsavelFinanceiroPorMatriculaAluno(String matricula) throws Exception;

	List<MatriculaVO> consultarAlunoResponsavelFinanceiro(Integer respFinan) throws Exception;

}
