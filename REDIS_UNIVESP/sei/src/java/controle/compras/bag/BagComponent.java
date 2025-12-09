package controle.compras.bag;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;

@Scope("viewScope")
@Controller("BagComponent")
public class BagComponent {

	private HashMap<String, Object> mapPesquisa;

	private BagFacade bagFacade;

	private DataModelo dataModelo;

	private String teste;

	private boolean foiConsultado;

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMap() {
		return (List<Map<String, Object>>) this.getBagDataModel().getListaConsulta();
	}

	@SuppressWarnings("unchecked")
	public void init(BagFacade bagFacade) {

		this.bagFacade = bagFacade;

		this.consultar();
		((HashMap<String, Object>) this.getBagDataModel().getListaConsulta().get(0))
		        .keySet().forEach(p -> this.getMapPesquisa().put(p, ""));

	}

	public DataModelo getBagDataModel() {
		this.dataModelo = Optional.ofNullable(this.dataModelo).orElseGet(() -> {
			DataModelo dataModelo = new DataModelo();
			dataModelo.setOffset(0);
			dataModelo.setLimitePorPagina(10);
			dataModelo.setPaginaAtual(1);
			return dataModelo;
		});

		return this.dataModelo;

	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getBagDataModel().setPaginaAtual(DataScrollEvent.getPage());
		getBagDataModel().setPage(DataScrollEvent.getPage());
		consultar();
	}

	public HashMap<String, Object> getMapPesquisa() {
		this.mapPesquisa = Optional.ofNullable(this.mapPesquisa).orElse(new LinkedHashMap<>());
		return mapPesquisa;
	}

	public void consultar() {
		try {

			if (this.isFoiConsultado()) {
				return;
			}

			int consultaContador = this.bagFacade.consultaContador(this.getMapPesquisa());
			List<Map<String, Object>> lista = this.bagFacade.consultar(this.getMapPesquisa(), consultaContador, this.getBagDataModel().getOffset());
			this.getBagDataModel().setTotalRegistrosEncontrados(consultaContador);
			this.getBagDataModel().setListaConsulta(lista);
			this.setFoiConsultado(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isFoiConsultado() {
		return foiConsultado;
	}

	public void setFoiConsultado(boolean foiConsultado) {
		this.foiConsultado = foiConsultado;
	}

	public String getTeste() {
		this.teste = "danillosl";
		return teste;
	}

	public void setTeste(String teste) {
		this.teste = teste;
	}

}