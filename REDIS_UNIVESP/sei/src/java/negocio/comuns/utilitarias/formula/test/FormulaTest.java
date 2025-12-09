package negocio.comuns.utilitarias.formula.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.formula.FormulaFactory;
import negocio.comuns.utilitarias.formula.test.recursos.A;
import negocio.comuns.utilitarias.formula.test.recursos.B;

public class FormulaTest {

	@Autowired
	private FormulaFactory formulaFactory;
/*
	@Test
	public void deveCalcularCorretamenteExpressoesMatematicas() throws Exception {
		assertEquals(8, formulaFactory.getInstance("return 3+5;", new FuncionarioCargoVO()).execute());
	}

	@Test
	public void deveConseguirAcessarOGrafoDeObjetosDoContextoERetornaNumero() throws Exception {
		A context = new A("xpto", 20, new B("Narnia"));
		assertEquals(25.0, formulaFactory.getInstance("return contexto.getIdade()+5;", new FuncionarioCargoVO()).execute(context));
	}

	@Test
	public void deveConseguirAcessarMapaDeParametros() throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("nome", "xpto");
		hashMap.put("endereco", "narnia");
		assertEquals("xpto:narnia", formulaFactory.getInstance("return map.nome + ':' + map.endereco;", new FuncionarioCargoVO()).execute(hashMap));
	}

	@Test
	public void deveConseguirAcessarOGrafoDeObjetosDoContexto() throws Exception {
		A context = new A("xpto", 20, new B("Narnia"));
		assertEquals("Narnia", formulaFactory.getInstance("return contexto.getB().getEndereco();", new FuncionarioCargoVO()).execute(context));
	}

	@Test
	public void deveConseguirCriarEUtilizarVariaveis() throws Exception {
		assertEquals(15.0, formulaFactory.getInstance("var teste = 10;" + "return teste + 5;", new FuncionarioCargoVO()).execute());
	}

	@Test
	public void deveConseguirAcessarECalcularComBigDecimal() throws Exception {
		assertEquals(new BigDecimal("60"),
				formulaFactory.getInstance("return new BigDecimal('50').add(BigDecimal.TEN);", new FuncionarioCargoVO()).execute());
	}*/

}