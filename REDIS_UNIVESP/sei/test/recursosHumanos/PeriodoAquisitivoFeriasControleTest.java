package recursosHumanos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import arquitetura.TestManager;
import controle.recursoshumanos.PeriodoAquisitivoFeriasControle;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.recursoshumanos.PeriodoAquisitivoFeriasVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoPeriodoAquisitivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

public class PeriodoAquisitivoFeriasControleTest extends TestManager {
	
	private static final long serialVersionUID = 1179665256745344833L;

	public PeriodoAquisitivoFeriasControleTest() {
		try {
			periodoAquisitivoControle = new PeriodoAquisitivoFeriasControle();
			
			String sql = "SELECT * FROM FuncionarioCargo WHERE utilizaRH = true and ativo = true limit 1";
	        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
	        if(resultado.next()) {
	        	f = new FuncionarioCargoVO();
	            f.setCodigo(resultado.getInt("codigo"));
	        }
	        
		}catch (Exception e) {
			System.out.println("Deu ruim");
		}
		
	}

	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	
	PeriodoAquisitivoFeriasVO periodoAquisitivo;
	PeriodoAquisitivoFeriasControle periodoAquisitivoControle;
	List<PeriodoAquisitivoFeriasVO> listaPeriodoAquisitivos;
	FuncionarioCargoVO f;

	
	private PeriodoAquisitivoFeriasVO periodoAquisitivoControleFactory(Integer codigoFuncionario, Date dataInicial, Date dataFinal, SituacaoPeriodoAquisitivoEnum situacao){
		periodoAquisitivoControleFactory(codigoFuncionario, dataInicial, dataFinal, situacao, false);
		return periodoAquisitivo;
	}
	
	private PeriodoAquisitivoFeriasVO periodoAquisitivoControleFactory(Integer codigoFuncionario, Date dataInicial, Date dataFinal, SituacaoPeriodoAquisitivoEnum situacao, Boolean itemEmEdicao){
		periodoAquisitivo = new PeriodoAquisitivoFeriasVO();
		periodoAquisitivo.getFuncionarioCargo().setCodigo(codigoFuncionario);
		periodoAquisitivo.setInicioPeriodo(dataInicial);
		periodoAquisitivo.setFinalPeriodo(dataFinal);
		periodoAquisitivo.setSituacao(situacao);
		periodoAquisitivo.setItemEmEdicao(itemEmEdicao);

		return periodoAquisitivo;
	}

	
	private void adicionarNaListaPeriodoAquisitivoControleFactory(Integer codigoFuncionario, Date dataInicial, Date dataFinal, SituacaoPeriodoAquisitivoEnum situacao) {
		listaPeriodoAquisitivos.add(periodoAquisitivoControleFactory(codigoFuncionario, dataInicial, dataFinal, situacao));
	}
	
	
	@Before
    public void before() throws Exception {
		listaPeriodoAquisitivos = new ArrayList<>();
		periodoAquisitivo = new PeriodoAquisitivoFeriasVO();

		adicionarNaListaPeriodoAquisitivoControleFactory(f.getCodigo(), UteisData.getData("01/01/2015"), UteisData.getData("31/12/2015"), SituacaoPeriodoAquisitivoEnum.FECHADO);
		adicionarNaListaPeriodoAquisitivoControleFactory(f.getCodigo(), UteisData.getData("01/01/2016"), UteisData.getData("31/12/2016"), SituacaoPeriodoAquisitivoEnum.FECHADO);
		adicionarNaListaPeriodoAquisitivoControleFactory(f.getCodigo(), UteisData.getData("01/01/2017"), UteisData.getData("31/12/2017"), SituacaoPeriodoAquisitivoEnum.VENCIDO);
		adicionarNaListaPeriodoAquisitivoControleFactory(f.getCodigo(), UteisData.getData("01/01/2018"), UteisData.getData("31/12/2018"), SituacaoPeriodoAquisitivoEnum.ABERTO);
		
		periodoAquisitivoControle.setListaPeriodoAquisitivoFerias(listaPeriodoAquisitivos);
    }
	
    @After
    public void after() throws Exception {
    }
	
    @Test
	public void testValidarDadosCodigoFuncionarioFaltando() throws Exception {
		
		expectedEx.expect(ConsistirException.class);
	    expectedEx.expectMessage(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_funcionarioCargo"));
	    
		periodoAquisitivoControle.setPeriodoAquisitivoFeriasVO(periodoAquisitivoControleFactory(0, UteisData.getData("01/01/2019"), UteisData.getData("31/12/2019"), SituacaoPeriodoAquisitivoEnum.ABERTO));
		getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().validarDados(periodoAquisitivoControle.getPeriodoAquisitivoFeriasVO());
	}

	@Test
	public void testValidarDadosSituacaoFaltando() throws Exception {
		
		expectedEx.expect(ConsistirException.class);
	    expectedEx.expectMessage(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_situacao"));
	    
		periodoAquisitivoControle.setPeriodoAquisitivoFeriasVO(periodoAquisitivoControleFactory(f.getCodigo(), UteisData.getData("01/01/2019"), UteisData.getData("31/12/2019"), null));
		
		getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().validarDados(periodoAquisitivoControle.getPeriodoAquisitivoFeriasVO());
	}
	
	@Test
	public void testValidarDadosSituacaoFechadaParaPeriodoPosteriorDataAtual() throws Exception {
		
		expectedEx.expect(ConsistirException.class);
	    expectedEx.expectMessage(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_dataInvalidaParaSituacaoFechada"));
	    
		periodoAquisitivoControle.setPeriodoAquisitivoFeriasVO(periodoAquisitivoControleFactory(f.getCodigo(), UteisData.getData("01/01/2019"), UteisData.getData("31/12/2019"), SituacaoPeriodoAquisitivoEnum.FECHADO));
		
		getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().validarDados(periodoAquisitivoControle.getPeriodoAquisitivoFeriasVO());
	}
	
	@Test
	public void testValidarDadosPeriodosIguais() throws Exception {
		
		expectedEx.expect(ConsistirException.class);
	    expectedEx.expectMessage(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_periodosIguais"));
	    
		periodoAquisitivoControle.setPeriodoAquisitivoFeriasVO(periodoAquisitivoControleFactory(f.getCodigo(), new Date(), new Date(), SituacaoPeriodoAquisitivoEnum.ABERTO));
		
		getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().validarDados(periodoAquisitivoControle.getPeriodoAquisitivoFeriasVO());
	}
	
	@Test
	public void testValidarDadosDataInicialMaiorQueDataFinal() throws Exception {
		
		expectedEx.expect(ConsistirException.class);
	    expectedEx.expectMessage(UteisJSF.internacionalizar("msg_erro_dataFimMenorDataInicio"));
	    
		periodoAquisitivoControle.setPeriodoAquisitivoFeriasVO(periodoAquisitivoControleFactory(f.getCodigo(), UteisData.adicionarDiasEmData(new Date(), 1), new Date(), SituacaoPeriodoAquisitivoEnum.ABERTO));
		
		getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().validarDados(periodoAquisitivoControle.getPeriodoAquisitivoFeriasVO());
	}
	
	@Test
	public void testValidarDadosPeriodoMaiorQueUmAno() throws Exception {
		
		expectedEx.expect(ConsistirException.class);
	    expectedEx.expectMessage(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_periodoAquisitivoMaiorQueUmAno"));
	    
		periodoAquisitivoControle.setPeriodoAquisitivoFeriasVO(periodoAquisitivoControleFactory(f.getCodigo(), UteisData.getData("01/01/2019"), UteisData.getData("01/01/2020"), SituacaoPeriodoAquisitivoEnum.ABERTO));
		
		getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().validarDados(periodoAquisitivoControle.getPeriodoAquisitivoFeriasVO());
	}
	
	@Test
	public void testValidarDadosEditarPeriodoParaAberto() throws Exception {
		
		expectedEx.expect(ConsistirException.class);
	    expectedEx.expectMessage(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_jaExisteUmPeriodoAbertoCadastrado"));
		
		periodoAquisitivoControle.setPeriodoAquisitivoFeriasVO(periodoAquisitivoControleFactory(f.getCodigo(), UteisData.getData("01/01/2017"), UteisData.getData("31/12/2017"), SituacaoPeriodoAquisitivoEnum.ABERTO, true));
		
		getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().validarDados(periodoAquisitivoControle.getPeriodoAquisitivoFeriasVO());
	}
	
	@Test
	public void testValidarDadosEditarPeriodoParaFechado() throws Exception {
		
		periodoAquisitivoControle.setPeriodoAquisitivoFeriasVO(periodoAquisitivoControleFactory(f.getCodigo(), UteisData.getData("01/01/2017"), UteisData.getData("31/12/2017"), SituacaoPeriodoAquisitivoEnum.FECHADO, true));
		
		getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().validarDados(periodoAquisitivoControle.getPeriodoAquisitivoFeriasVO());
	}
}