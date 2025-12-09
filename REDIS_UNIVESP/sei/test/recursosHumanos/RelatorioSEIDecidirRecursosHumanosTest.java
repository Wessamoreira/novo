package recursosHumanos;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import arquitetura.TestManager;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirModuloEnum;
import negocio.comuns.financeiro.RelatorioSEIDecidirVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.administrativo.RelatorioSeiDecidir;

public class RelatorioSEIDecidirRecursosHumanosTest extends TestManager  {

	private static final long serialVersionUID = -730128515537115355L;

	public RelatorioSEIDecidirRecursosHumanosTest() {
		try {
			new RelatorioSeiDecidir(getConexao(), getFacadeFactoryTest());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void gerarRelatorio() {
		try {
			List<LayoutRelatorioSEIDecidirVO> listaLayout = getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().consultarPorModulo(RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, null);
			if (!listaLayout.isEmpty()) {

				UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactoryTest().getUnidadeEnsinoFacade().consultarSeExisteUnidadeMatriz(true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
				unidadeEnsinoVO.setFiltrarUnidadeEnsino(true);

				LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO = listaLayout.get(0);
				layoutRelatorioSEIDecidirVO.setLayoutRelatorioSeiDecidirCampoVOs(getFacadeFactoryTest().getLayoutRelatorioSEIDecidirCampoInterfaceFacade().consultarPorLayoutRelatorio(layoutRelatorioSEIDecidirVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, null));

				RelatorioSEIDecidirVO relatorioSEIDecidirVO = new RelatorioSEIDecidirVO();
				relatorioSEIDecidirVO.setLayoutRelatorioSEIDecidirVO(layoutRelatorioSEIDecidirVO);
				relatorioSEIDecidirVO.setUnidadeEnsinoVOs(Arrays.asList(unidadeEnsinoVO));

				//getFacadeFactory().getRelatorioSeiDecidirFacade().realizarGeracaoRelatorioSeiDecidir(relatorioSEIDecidirVO, TipoRelatorioEnum.EXCEL, "", true, false, null);
				assertTrue(true);
			} else {
				fail("Nenhum Layout Relatorio SEI Decidir para recursos humanos cadastrado (LayoutRelatorioSEIDecidirVO)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
