package planoOrcamentario;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import arquitetura.TestManager;
import negocio.comuns.compras.RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.utilitarias.Uteis;

public class RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioTest  extends TestManager {

	private static final long serialVersionUID = -2005626093247928296L;

	private RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj = new RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO();

	@Test
	public void consultarAfastamentoPorFuncionarioCargo() {
		try {
			obj = getFacadeFactoryTest().getRequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioInterfaceFacade().consultarPorChavePrimaria(1L);
			System.out.println(obj.getCodigo());
			assertTrue(Uteis.isAtributoPreenchido(obj));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
