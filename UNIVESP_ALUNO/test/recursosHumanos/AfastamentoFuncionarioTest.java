package recursosHumanos;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import arquitetura.TestManager;
import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.recursoshumanos.AfastamentoFuncionarioVO;
import negocio.comuns.recursoshumanos.enumeradores.MotivoAfastamentoEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoAfastamentoEnum;
import negocio.facade.jdbc.recursoshumanos.AfastamentoFuncionario;

public class AfastamentoFuncionarioTest extends TestManager {

	private static final long serialVersionUID = -845524689250892903L;
	
	private AfastamentoFuncionarioVO afastamento = new AfastamentoFuncionarioVO(); 
	protected DataModelo dataModelo = new DataModelo();
	protected AfastamentoFuncionario afastamentoFuncionario;

	public AfastamentoFuncionarioTest() {
		try {
			montarDadosAfastamento();
			montarDadosDataModelo();
			afastamentoFuncionario = new AfastamentoFuncionario(getConexao(), getFacadeFactoryTest());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Before
	public void inserir() {
		try {
			afastamentoFuncionario.persistir(getAfastamento(), false, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void excluir() {
		try {
			afastamentoFuncionario.excluir(getAfastamento(), false, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void consultarAfastamentoPorFuncionarioCargo() {
		try {
			List<AfastamentoFuncionarioVO> list = afastamentoFuncionario.consultarAfastamentoPorCodigoFuncionarioCargo(144);
			System.out.println("consultarAfastamentoPorFuncionarioCargo: " + list.size());
			assertTrue(list.size() > 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void consultarAfastamentoFuncionario() {
		try {
			afastamentoFuncionario.consultarPorEnumCampoConsulta(dataModelo);
			System.out.println(dataModelo.getListaConsulta().size());
			assertTrue(dataModelo.getListaConsulta().size() > 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void montarDadosDataModelo() {
		dataModelo.setCampoConsulta("FUNCIONARIO");
		dataModelo.setValorConsulta("%%");
		dataModelo.setPage(1);
		dataModelo.setOffset(1);
	}
	
	private void montarDadosAfastamento() {
		FuncionarioCargoVO funcionarioCargoVO = new FuncionarioCargoVO();
		funcionarioCargoVO.setCodigo(144);
		
		getAfastamento().setFuncionarioCargo(funcionarioCargoVO);
		getAfastamento().setTipoAfastamento(TipoAfastamentoEnum.AFASTAMENTO_ACIDENTE_TRABALHO);
		getAfastamento().setMotivoAfastamento(MotivoAfastamentoEnum.AFASTAMENTO_ATIVIDADE_POLITICA);
		getAfastamento().setDataInicio(new Date());
		getAfastamento().setDataFinal(new Date());
		
		ArquivoVO arquivoVO = new ArquivoVO();
		arquivoVO.setCodigo(4);
		getAfastamento().setArquivo(arquivoVO);
	}

	public AfastamentoFuncionarioVO getAfastamento() {
		if (afastamento == null) {
			afastamento = new AfastamentoFuncionarioVO();
		}
		return afastamento;
	}

	public void setAfastamento(AfastamentoFuncionarioVO afastamento) {
		this.afastamento = afastamento;
	}
}
