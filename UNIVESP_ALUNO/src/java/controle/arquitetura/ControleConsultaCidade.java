package controle.arquitetura;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jakarta.faces. model.SelectItem;



import negocio.comuns.basico.EstadoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.basico.Estado;

public class ControleConsultaCidade extends DataModelo {

	/**
	 * @author Felipi Alves
	 */
	private static final long serialVersionUID = 5024808803129428668L;
	private List<SelectItem> listaSelectItemCidade;
	private List<SelectItem> listaSelectItemEstado;
	private Integer estado;
	
	public static String nomeCidade = "nomeCidade";
	public static String siglaEstado = "siglaEstado";
	
	public List<SelectItem> getListaSelectItemCidade() {
		if (listaSelectItemCidade == null) {
			listaSelectItemCidade = new ArrayList<>(0);
			listaSelectItemCidade.add(new SelectItem(nomeCidade, "Nome da Cidade"));
			listaSelectItemCidade.add(new SelectItem(siglaEstado, "Sigla do Estado"));
		}
		return listaSelectItemCidade;
	}
	
	public void setListaSelectItemCidade(List<SelectItem> listaSelectItemCidade) {
		this.listaSelectItemCidade = listaSelectItemCidade;
	}

	public List<SelectItem> getListaSelectItemEstado() throws Exception {
		if (listaSelectItemEstado == null) {
			listaSelectItemEstado = new ArrayList<>(0);
			Estado estado = new Estado();
			List<EstadoVO> listEstado = estado.consultarEstadosMinimo();
			if (!listEstado.isEmpty()) {
				for (EstadoVO obj : listEstado) {
					if (Uteis.isAtributoPreenchido(obj)) {
						listaSelectItemEstado.add(new SelectItem(obj.getCodigo(), obj.getSigla()));
						
					}
				}
			}
			
		}
		return listaSelectItemEstado;
	}
	
	public boolean isNomeCidade() {
		return getCampoConsulta().equals(nomeCidade);
	}
	
	public boolean isSiglaEstado() {
		return getCampoConsulta().equals(siglaEstado);
	}
	
	public Boolean getApresentarCampoNome() {
		return getCampoConsulta().equals(nomeCidade) || (!Uteis.isAtributoPreenchido(getCampoConsulta()));
	}
	
	public Boolean getApresentarCampoSiglaEstado() {
		return getCampoConsulta().equals(siglaEstado);
	}
	
	public void setListaSelectItemEstado(List<SelectItem> listaSelectItemEstado) {
		this.listaSelectItemEstado = listaSelectItemEstado;
	}

	public void scroll() throws Exception {
		
	}

	public void limparListaConsulta() {
		getListaConsulta().clear();
	}

	public Integer getEstado() {
		if (estado == null) {
			estado = 0;
		}
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

}
