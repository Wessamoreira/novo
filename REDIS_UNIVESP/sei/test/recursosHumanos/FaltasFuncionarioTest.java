package recursosHumanos;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import arquitetura.TestManager;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.recursoshumanos.FaltasFuncionarioVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoFaltaEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.recursoshumanos.FaltasFuncionario;

public class FaltasFuncionarioTest extends TestManager {
	
	private static final long serialVersionUID = 467124405640877083L;
	
	FaltasFuncionario faltasFuncionario;
	FuncionarioCargoVO f;
	List<FaltasFuncionarioVO> listaDeFaltas = new ArrayList<>();

	public FaltasFuncionarioTest() {
		try {
			faltasFuncionario = new FaltasFuncionario(getConexao());	
		}catch (Exception e) {
			System.out.println("Deu ruim");
		}
	}

	private void fabricaDeFaltas(Date dataInicial) {
		FaltasFuncionarioVO faltas;
		try {
			faltas = new FaltasFuncionarioVO();
			faltas.setFuncionarioCargo(f);
			faltas.setDataInicio(dataInicial);
			faltas.setTipoFalta(TipoFaltaEnum.INJUSTIFICADA);
			faltas.setIntegral(true);
			faltas.setDebitado(false);
			
			faltasFuncionario.incluir(faltas, false, null);
			
			listaDeFaltas.add(faltas);
			
			faltas = new FaltasFuncionarioVO();
			faltas.setFuncionarioCargo(f);
			faltas.setDataInicio(UteisData.adicionarDiasEmData(dataInicial, 1));
			faltas.setTipoFalta(TipoFaltaEnum.ESTORNO);
			faltas.setIntegral(true);
			faltas.setDebitado(false);
			
			faltasFuncionario.incluir(faltas, false, null);
			
			listaDeFaltas.add(faltas);	
			
			faltas = new FaltasFuncionarioVO();
			faltas.setFuncionarioCargo(f);
			faltas.setDataInicio(UteisData.adicionarDiasEmData(dataInicial, 2));
			faltas.setTipoFalta(TipoFaltaEnum.INJUSTIFICADA);
			faltas.setIntegral(true);
			faltas.setDebitado(false);
			
			faltasFuncionario.incluir(faltas, false, null);
			
			listaDeFaltas.add(faltas);	
			
			faltas = new FaltasFuncionarioVO();
			faltas.setFuncionarioCargo(f);
			faltas.setDataInicio(UteisData.adicionarDiasEmData(dataInicial, 3));
			faltas.setTipoFalta(TipoFaltaEnum.INJUSTIFICADA);
			faltas.setIntegral(true);
			faltas.setDebitado(false);
			
			faltasFuncionario.incluir(faltas, false, null);
			
			listaDeFaltas.add(faltas);	
			
			faltas = new FaltasFuncionarioVO();
			faltas.setFuncionarioCargo(f);
			faltas.setDataInicio(UteisData.adicionarDiasEmData(dataInicial, 4));
			faltas.setTipoFalta(TipoFaltaEnum.JUSTIFICADA);
			faltas.setIntegral(true);
			faltas.setDebitado(false);
			
			faltasFuncionario.incluir(faltas, false, null);
			
			listaDeFaltas.add(faltas);	
		}catch (Exception e) {
			System.out.println("Deu ruim");
		}
	}

	@Before
    public void before() throws Exception {
		String sql = "SELECT * FROM FuncionarioCargo WHERE utilizaRH = true and ativo = true limit 1";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if(resultado.next()) {
        	f = new FuncionarioCargoVO();
            f.setCodigo(resultado.getInt("codigo"));	
        }		
    }
	
    @After
    public void after() throws Exception {
    	
    	for(FaltasFuncionarioVO falta : listaDeFaltas) {
    		faltasFuncionario.excluir(falta, false, null);	
    	}
    	
    }
	
	@Test
	public void testCalculaQtdDeFaltasFuncionario() throws Exception {
		
		fabricaDeFaltas(new Date());
		
		Date dataInicial = new Date();
		Date dataFinal = UteisData.adicionarDiasEmData(new Date(), 10);
		
		Integer qtdFalta = faltasFuncionario.consultarQtdFaltasDoPeriodo(f, dataInicial, dataFinal);
		
		assertTrue(qtdFalta.equals(2));
	}
}