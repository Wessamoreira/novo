package relatorio.negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.protocolo.RequerimentoVO;
import relatorio.negocio.comuns.academico.RequerimentoRelVO;

public interface RequerimentoRelInterfaceFacade {

	public void validarDados(Date dataInicio, Date dataFim, List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception;

	public List<RequerimentoRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUniade, Boolean finalizadoDeferido, Boolean finalizadoIndeferido, Boolean emExecucao, Boolean pendente, Boolean aguardandoPagamento, Boolean aguardandoAutorizacaoPagamento, Boolean isento, Boolean pago, Boolean canceladoFinanceiro, Boolean solicitacaoIsencao, Boolean solicitacaoIsencaoDeferido, Boolean solicitacaoIsencaoIndeferido, Boolean prontoParaRetirada, Boolean atrasado, RequerimentoRelVO requerimentoRelVO, Date dataInicio, Date dataFim, Integer funcionario, Integer departamento, Integer disciplina, String layout,CursoVO curso, TurmaVO turma, List<CursoVO> cursoVOs, PessoaVO requerente, Integer situacaoRequerimentoDepartamento, PessoaVO coordenador, String filtrarPeriodoPor, TurmaVO turmaReposicao) throws Exception;
	
	public List<RequerimentoVO> criarObjetoRequerimentoTipoTCC(List<UnidadeEnsinoVO> listaUniade, Boolean finalizadoDeferido, Boolean finalizadoIndeferido, Boolean emExecucao, Boolean pendente, Boolean aguardandoPagamento, Boolean aguardandoAutorizacaoPagamento, Boolean isento, Boolean pago, Boolean canceladoFinanceiro, Boolean solicitacaoIsencao, Boolean solicitacaoIsencaoDeferido, Boolean solicitacaoIsencaoIndeferido, Boolean prontoParaRetirada, Boolean atrasado, Date dataInicio, Date dataFim, Integer funcionario, Integer departamento, Integer disciplina, String matricula, CursoVO curso, TurmaVO turma, List<CursoVO> cursoVOs, PessoaVO requerente, Integer situacaoRequerimentoDepartamento, PessoaVO coordenador, String filtrarPeriodoPor, TurmaVO turmaReposicao, Integer codTipoReq, UsuarioVO usuarioVO) throws Exception;
}
