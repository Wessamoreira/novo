package negocio.interfaces.financeiro;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberAgrupadaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.RegistroArquivoVO;
import negocio.comuns.financeiro.enumerador.NivelAgrupamentoContaReceberEnum;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;

public interface ContaReceberAgrupadaInterfaceFacade {

	void persistir(ContaReceberAgrupadaVO contaReceberAgrupada, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	List<ContaReceberAgrupadaVO> realizarAgrupamentoContaReceber(NivelAgrupamentoContaReceberEnum nivelAgrupamentoContaReceber, 
			UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, MatriculaVO matriculaVO, TurmaVO turmaVO, PessoaVO responsavelFinanceiro,
			Boolean agruparBiblioteca, Boolean agruparRequerimento, Boolean agruparContratoReceita, Boolean agruparContasResponsavelFinanceiro,
			ContaCorrenteVO contaCorrenteVO, Integer ano, MesAnoEnum mes, UsuarioVO usuarioVO) throws Exception;
	
	List<ContaReceberAgrupadaVO> consultarContaReceberAgrupada(UnidadeEnsinoVO unidadeEnsinoVO, String opcaoConsulta, String valorConsulta, 
	Date dataInicio, Date dataTermino, SituacaoContaReceber situacaoContaReceber, Integer limit, Integer offset, boolean validarAcesso, 
	UsuarioVO usuarioVO) throws Exception;
	
	Integer consultarTotalRegistroContaReceberAgrupada(UnidadeEnsinoVO unidadeEnsinoVO, String opcaoConsulta, String valorConsulta, 
			Date dataInicio, Date dataTermino, SituacaoContaReceber situacaoContaReceber) throws Exception;
	
	ContaReceberAgrupadaVO consultaPorChavePrimaria(Integer contaReceberAgrupada, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	void realizarCriacaoNossoNumero(ContaReceberAgrupadaVO contaReceberAgrupada, UsuarioVO usuarioVO) throws Exception;

	void excluir(ContaReceberAgrupadaVO contaReceberAgrupada, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	void montarDadosContaCorrente(ContaReceberAgrupadaVO contaReceberAgrupadaVO, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	void removerContaReceber(ContaReceberAgrupadaVO contaReceberAgrupada, ContaReceberVO contaReceberVO) throws Exception;

	void adicionarContaReceber(ContaReceberAgrupadaVO contaReceberAgrupada, ContaReceberVO contaReceberVO) throws Exception;
	
	public Map<BigInteger, ContaReceberAgrupadaVO> consultarPorNossoNumeroControleCobranca(RegistroArquivoVO registroArquivoVO, Boolean bancoBrasil);

	void alterarSituacaoContaAgrupada(final NegociacaoRecebimentoVO negociacaoRecebimentoVO, final String situacao, final UsuarioVO usuarioVO) throws Exception;
	
	public void alterarSituacaoContaAgrupadaArquivoRetorno(final Double valorTotalRecebido, final Double valortotal, final Date dataRecebimento, final Date dataAlteracao, final Integer codigoContaReceberAgrupada, final String situacao, final UsuarioVO usuarioVO) throws Exception;
	
}
