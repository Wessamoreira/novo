package financeiro;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import arquitetura.TestManager;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.FechamentoFinanceiroCentroResultadoVO;
import negocio.comuns.financeiro.FechamentoFinanceiroContaCentroResultadoVO;
import negocio.comuns.financeiro.FechamentoFinanceiroContaVO;
import negocio.comuns.financeiro.FechamentoFinanceiroDetalhamentoValorVO;
import negocio.comuns.financeiro.FechamentoFinanceiroFormaPagamentoVO;
import negocio.comuns.financeiro.FechamentoFinanceiroVO;
import negocio.comuns.financeiro.enumerador.OrigemFechamentoFinanceiroContaEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemDetalheEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.financeiro.FechamentoFinanceiro;
import negocio.facade.jdbc.financeiro.FechamentoFinanceiroCentroResultado;
import negocio.facade.jdbc.financeiro.FechamentoFinanceiroConta;
import negocio.facade.jdbc.financeiro.FechamentoFinanceiroContaCentroResultado;
import negocio.facade.jdbc.financeiro.FechamentoFinanceiroDetalhamentoValor;
import negocio.facade.jdbc.financeiro.FechamentoFinanceiroFormaPagamento;

public class FechamentoFinanceiroTest extends TestManager {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3137829955799128949L;

	public FechamentoFinanceiroTest() {
		
		
	}
	
	
	FechamentoFinanceiroVO fechamentoFinanceiroVO;
	UsuarioVO usuarioVO;
	
	@Before
    public void before() throws Exception {
		
    }
	
    @After
    public void after() throws Exception {
    	
    }
	
	@Test
	public void testIncluirFechamentoFinanceiro() throws Exception {
			
			
			getFacadeFactory().setFechamentoFinanceiroFacade(new FechamentoFinanceiro());
			getFacadeFactory().setFechamentoFinanceiroCentroResultadoFacade(new FechamentoFinanceiroCentroResultado());
			getFacadeFactory().setFechamentoFinanceiroContaCentroResultadoFacade(new FechamentoFinanceiroContaCentroResultado());
			getFacadeFactory().setFechamentoFinanceiroContaFacade(new FechamentoFinanceiroConta());
			getFacadeFactory().setFechamentoFinanceiroFormaPagamentoFacade(new FechamentoFinanceiroFormaPagamento());
			getFacadeFactory().setFechamentoFinanceiroDetalhamentoValorFacade(new FechamentoFinanceiroDetalhamentoValor());
				
			
			usuarioVO =  new UsuarioVO();
			usuarioVO.setCodigo(1);
			fechamentoFinanceiroVO =  new FechamentoFinanceiroVO();
			fechamentoFinanceiroVO.setDataFechamento(new Date());
			fechamentoFinanceiroVO.setUsuarioFechamento(usuarioVO);
			fechamentoFinanceiroVO.setDescricaoFechamento("Deu certo");
			
			FechamentoFinanceiroContaVO fechamentoFinanceiroContaVO = realizarPreenchimentoFechamentoFinanceiroContaVO();
			FechamentoFinanceiroCentroResultadoVO fechamentoFinanceiroCentroResultadoVO = realizarPreenchimentoFechamentoFinanceiroCentroResultadoVO();
			
			FechamentoFinanceiroCentroResultadoVO fechamentoFinanceiroCentroResultadoVO2 = realizarPreenchimentoFechamentoFinanceiroCentroResultadoVO(); 
			fechamentoFinanceiroContaVO.getFechamentoFinanceiroDetalhamentoValorVOs().add(realizarPreenchimentoFechamentoFinanceiroDetalhamentoValor(TipoCentroResultadoOrigemDetalheEnum.VALOR_BASE, 1100.0, null, 1, true));
			fechamentoFinanceiroContaVO.getFechamentoFinanceiroDetalhamentoValorVOs().add(realizarPreenchimentoFechamentoFinanceiroDetalhamentoValor(TipoCentroResultadoOrigemDetalheEnum.DESCONTO_PROGRESSIVO, 50.0, Uteis.getDataPassada(new Date(), 2), 2, false));
			fechamentoFinanceiroContaVO.getFechamentoFinanceiroDetalhamentoValorVOs().add(realizarPreenchimentoFechamentoFinanceiroDetalhamentoValor(TipoCentroResultadoOrigemDetalheEnum.DESCONTO_ALUNO, 100.0, null, 3, true));
			fechamentoFinanceiroContaVO.getFechamentoFinanceiroCentroResultadoVOs().add(fechamentoFinanceiroCentroResultadoVO2);
			
			fechamentoFinanceiroCentroResultadoVO.getFechamentoFinanceiroContaCentroResultadoVOs().add(realizarPreenchimentoFechamentoFinanceiroCentroResultadoVO(fechamentoFinanceiroContaVO, fechamentoFinanceiroCentroResultadoVO));
			
			fechamentoFinanceiroVO.getFechamentoFinanceiroContaVOs().add(fechamentoFinanceiroContaVO);
			fechamentoFinanceiroVO.getFechamentoFinanceiroCentroResultadoVOs().add(fechamentoFinanceiroCentroResultadoVO);
			
			getFacadeFactory().getFechamentoFinanceiroFacade().persistir(fechamentoFinanceiroVO, false, usuarioVO);
			
		
	}
	
	public FechamentoFinanceiroCentroResultadoVO realizarPreenchimentoFechamentoFinanceiroCentroResultadoVO() throws Exception {
		FechamentoFinanceiroCentroResultadoVO fechamentoFinanceiroCentroResultadoVO =  new FechamentoFinanceiroCentroResultadoVO();
		fechamentoFinanceiroCentroResultadoVO.getCentroResultado().setCodigo(1);
		fechamentoFinanceiroCentroResultadoVO.setTipoCentroResultadoOrigemEnum(TipoCentroResultadoOrigemEnum.CONTA_RECEBER);
		fechamentoFinanceiroCentroResultadoVO.setValor(1000.0);
		fechamentoFinanceiroCentroResultadoVO.setValorContaReceberAReceber(1000.0);
		fechamentoFinanceiroCentroResultadoVO.getCentroReceita().setCodigo(1);
		fechamentoFinanceiroCentroResultadoVO.setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.ENTRADA);
				
		return fechamentoFinanceiroCentroResultadoVO;		
	}
	
	
	public FechamentoFinanceiroDetalhamentoValorVO realizarPreenchimentoFechamentoFinanceiroDetalhamentoValor(
			TipoCentroResultadoOrigemDetalheEnum tipoCentroResultadoOrigemDetalheEnum, Double valor, Date dataLimite, Integer ordemApresentacao, boolean utilizado) {
		FechamentoFinanceiroDetalhamentoValorVO fechamentoFinanceiroDetalhamentoValorVO =  new FechamentoFinanceiroDetalhamentoValorVO();
		fechamentoFinanceiroDetalhamentoValorVO.setTipoCentroResultadoOrigemDetalhe(tipoCentroResultadoOrigemDetalheEnum);
		fechamentoFinanceiroDetalhamentoValorVO.setValor(valor);
		fechamentoFinanceiroDetalhamentoValorVO.setDataLimiteAplicacaoDesconto(dataLimite);
		fechamentoFinanceiroDetalhamentoValorVO.setUtilizado(utilizado);
		fechamentoFinanceiroDetalhamentoValorVO.setOrdemApresentacao(ordemApresentacao);
		return fechamentoFinanceiroDetalhamentoValorVO;		
	}
	
	public FechamentoFinanceiroContaCentroResultadoVO realizarPreenchimentoFechamentoFinanceiroCentroResultadoVO(FechamentoFinanceiroContaVO fechamentoFinanceiroContaVO, FechamentoFinanceiroCentroResultadoVO fechamentoFinanceiroCentroResultadoVO) {
		FechamentoFinanceiroContaCentroResultadoVO fechamentoFinanceiroContaCentroResultadoVO = new FechamentoFinanceiroContaCentroResultadoVO();
		fechamentoFinanceiroContaCentroResultadoVO.setFechamentoFinanceiroCentroResultado(fechamentoFinanceiroCentroResultadoVO);
		fechamentoFinanceiroContaCentroResultadoVO.setFechamentoFinanceiroConta(fechamentoFinanceiroContaVO);
		return fechamentoFinanceiroContaCentroResultadoVO;		
	}
	
	public FechamentoFinanceiroContaVO realizarPreenchimentoFechamentoFinanceiroContaVO() {
		FechamentoFinanceiroContaVO fechamentoFinanceiroContaVO =  new FechamentoFinanceiroContaVO();
		fechamentoFinanceiroContaVO.setCodigoOrigem("1");
		fechamentoFinanceiroContaVO.setTipoOrigemContaReceber(TipoOrigemContaReceber.MENSALIDADE);
		fechamentoFinanceiroContaVO.getMatricula().setMatricula("123456789");
		fechamentoFinanceiroContaVO.getMatriculaPeriodo().setCodigo(1);
		fechamentoFinanceiroContaVO.setCpfCnpjSacado("999.999.999-99");
		fechamentoFinanceiroContaVO.setNomeSacado("SACADO CONTA A RECEBER");
		fechamentoFinanceiroContaVO.setDataRecebimento(new Date());
		fechamentoFinanceiroContaVO.setValor(1000.0);
		fechamentoFinanceiroContaVO.setDataVencimento(new Date());
		fechamentoFinanceiroContaVO.setDataCompetencia(new Date());
		fechamentoFinanceiroContaVO.getPessoa().setCodigo(0);
		fechamentoFinanceiroContaVO.setTipoPessoa(TipoPessoa.ALUNO);
		fechamentoFinanceiroContaVO.setNossoNumero("123456789");
		fechamentoFinanceiroContaVO.setParcela("1/6");
		fechamentoFinanceiroContaVO.setSituacaoContaReceber(SituacaoContaReceber.RECEBIDO);
		fechamentoFinanceiroContaVO.getUnidadeEnsinoAcademica().setCodigo(1);
		fechamentoFinanceiroContaVO.getUnidadeEnsinoFinanceira().setCodigo(1);
		fechamentoFinanceiroContaVO.setOrigemFechamentoFinanceiroConta(OrigemFechamentoFinanceiroContaEnum.CONTA_RECEBER);
		fechamentoFinanceiroContaVO.getFechamentoFinanceiroFormaPagamentoVOs().add(realizarPreenchimentoFechamentoFormaPagamento());
		return fechamentoFinanceiroContaVO;
		
	}
	
	public FechamentoFinanceiroFormaPagamentoVO realizarPreenchimentoFechamentoFormaPagamento() {
		FechamentoFinanceiroFormaPagamentoVO fechamentoFinanceiroFormaPagamentoVO =  new FechamentoFinanceiroFormaPagamentoVO();
		fechamentoFinanceiroFormaPagamentoVO.getContaCorrente().setCodigo(1);
		fechamentoFinanceiroFormaPagamentoVO.getFormaPagamento().setCodigo(1);
		fechamentoFinanceiroFormaPagamentoVO.setDataCompensacao(new Date());
		fechamentoFinanceiroFormaPagamentoVO.setValor(1000.0);		
		return fechamentoFinanceiroFormaPagamentoVO;		
	}
	
	
}