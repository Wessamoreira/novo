package controle.arquitetura;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.richfaces.model.Filter;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import net.sf.jasperreports.engine.type.SortOrderEnum;


/**
 * @author Otimize
 *
 */
@Controller("FilterFactory")
@Scope("viewScope")
@Lazy
public class FilterFactory extends SuperControle implements Filter<Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6794786536050630691L;
	private String filtro;
	private Date filtroData;
	private Map<String, FilterFactory> mapFilter;
	private String campo;
	private String idTable;
	private TipoCampoEnum tipoCampo;	
	private SortOrderEnum sortOrderEnum;
	private Ordenacao ordenacao;

	public Filter<Object> filter(String idTable, String campo, TipoCampoEnum tipoCampo){
		if(!Uteis.isAtributoPreenchido(idTable)){
			return this;
		}
		if(!getMapFilter().containsKey(idTable)){
			getMapFilter().put(idTable, new FilterFactory(campo, idTable, tipoCampo));
		}
		return getMapFilter().get(idTable);				
	}
	
	/**
	 * 
	 */
	public FilterFactory() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param campo
	 * @param idTable
	 * @param tipoCampo
	 */
	public FilterFactory(String campo, String idTable, TipoCampoEnum tipoCampo) {
		super();
		this.campo = campo;
		this.idTable = idTable;
		this.tipoCampo = tipoCampo;
	}


	/* (non-Javadoc)
	 * @see org.richfaces.model.Filter#accept(java.lang.Object)
	 */
	@Override
	public boolean accept(Object arg0) {
		if(!Uteis.isAtributoPreenchido(campo) || !Uteis.isAtributoPreenchido(getIdTable()) || tipoCampo == null){
			return true;
		}
		if(campo.contains(",")) {
			String[] campos  = campo.split(",");
			for(String camp: campos) {
				if(validarCampo(camp, arg0)) {
					return true;
				}
			}
			return false;
		}else if(campo.contains("-")) { 
			String[] campos  = campo.split("-");
			for(String camp: campos) {
				if(!validarCampo(camp, arg0)) {
					return false;
				}
			}
			return true;
		}else { 
			return validarCampo(campo, arg0);
		}
		
	}
	
	private boolean validarCampo(String campo, Object arg0) {
		String[] campos = campo.split("\\.");
		String campoComparar = campo;
		if(campos.length > 1){
			int tam = campos.length;
			int x = 1;
			for(String bean:campos){
				if(x == tam){
					campoComparar = bean;
					break;
				}
				Object obj = UtilReflexao.invocarMetodoGet(arg0, bean);
				if(obj instanceof SuperVO){
					arg0 = (SuperVO) obj;
				}else if(obj instanceof Enum){
					arg0 = (Enum) obj;
				}
				x++;
			}
		}
		switch (getTipoCampo()) {
		case BOOLEAN:
			Boolean valorBoolean = (Boolean)UtilReflexao.invocarMetodoGet(arg0, campoComparar);
			return (getFiltro().trim().isEmpty() || (Uteis.isAtributoPreenchido(valorBoolean) && (valorBoolean ? getFiltro().equals("true") : getFiltro().equals("false"))));				
		case DATA:
			Date valorDate = (Date)UtilReflexao.invocarMetodoGet(arg0, campoComparar);
			return (getFiltro().trim().isEmpty() || (valorDate != null  &&  Uteis.getData(valorDate, "dd/MM/yyyy").toString().contains(getFiltro())));			
		case DOUBLE:
			Double valorDouble = (Double)UtilReflexao.invocarMetodoGet(arg0, campoComparar);
			return (getFiltro().trim().isEmpty() || (valorDouble != null && valorDouble.toString().startsWith(getFiltro().replaceAll(",", "."))));				
		case MES_ANO:
			valorDate = (Date)UtilReflexao.invocarMetodoGet(arg0, campoComparar);
			return (getFiltro().trim().isEmpty() || (valorDate != null && Uteis.getData(valorDate, "MM/yyyy").toString().startsWith(getFiltro())));				
		case TEXTO:
			String valor = (String)UtilReflexao.invocarMetodoGet(arg0, campoComparar);
			return (getFiltro().trim().isEmpty() || (valor != null && Uteis.removerAcentos(valor.toUpperCase()).contains(Uteis.removerAcentos(getFiltro().toUpperCase()))));			
		case INTEIRO:
			Object valorRet = UtilReflexao.invocarMetodoGet(arg0, campoComparar);
			if(valorRet != null && valorRet instanceof Integer){
				Integer valorInt = (Integer)UtilReflexao.invocarMetodoGet(arg0, campoComparar);
				return (getFiltro().trim().isEmpty() || valorInt.toString().startsWith(getFiltro()));
			}else if(valorRet != null &&  valorRet instanceof Long){
				Long valorLong = (Long)UtilReflexao.invocarMetodoGet(arg0, campoComparar);
				return (getFiltro().trim().isEmpty() || valorLong.toString().startsWith(getFiltro()));
			}
		default:
			valor = (String)UtilReflexao.invocarMetodoGet(arg0, campoComparar);
			return (getFiltro().trim().isEmpty() || (valor != null && Uteis.removerAcentos(valor.toUpperCase()).contains(Uteis.removerAcentos(getFiltro().toUpperCase()))));			
		}
	}
		
	
	public String getFiltro() {
		if (filtro == null) {
			filtro = "";
		}
		return filtro;
	}
	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}


	public String getCampo() {
		if (campo == null) {
			campo = "";
		}
		return campo;
	}


	public void setCampo(String campo) {
		this.campo = campo;
	}


	public TipoCampoEnum getTipoCampo() {
		if (tipoCampo == null) {
			tipoCampo = TipoCampoEnum.TEXTO;
		}
		return tipoCampo;
	}


	public void setTipoCampo(TipoCampoEnum tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	public Map<String, FilterFactory> getMapFilter() {
		if (mapFilter == null) {
			mapFilter = new HashMap<String, FilterFactory>();
		}
		return mapFilter;
	}

	public void setMapFilter(Map<String, FilterFactory> mapFilter) {
		this.mapFilter = mapFilter;
	}

	public String getIdTable() {
		if (idTable == null) {
			idTable = "";
		}
		return idTable;
	}

	public void setIdTable(String idTable) {
		this.idTable = idTable;
	}
	
	public void limparFiltro(String... idTable) {
		for (String string : idTable) {
			if(Uteis.isAtributoPreenchido(string) && getMapFilter().containsKey(string)){
				getMapFilter().get(string).setFiltro("");
			}	
		}	
	}
	
	public void limparFiltroData(String... idTable) {
		for (String string : idTable) {
			if(Uteis.isAtributoPreenchido(string) && getMapFilter().containsKey(string)){
				getMapFilter().get(string).setFiltroData(null);
			}	
		}	
	}
	
	public void ordenar(String idTable, List<? extends SuperVO> listaOrdenar) {
		if (Uteis.isAtributoPreenchido(idTable) && getMapFilter().containsKey(idTable)) {	
			FilterFactory filter =  getMapFilter().get(idTable);
			Collections.sort(listaOrdenar,filter.getOrdenacao());				
			if (filter.getSortOrderEnum() == null || filter.getSortOrderEnum().equals(SortOrderEnum.DESCENDING)) {
				filter.setSortOrderEnum(SortOrderEnum.ASCENDING);
			} else if (filter.getSortOrderEnum().equals(SortOrderEnum.ASCENDING)) {
				filter.setSortOrderEnum(SortOrderEnum.DESCENDING);
				Collections.reverse(listaOrdenar);				
			}
		}
	}

	public SortOrderEnum getSortOrderEnum() {		
		return sortOrderEnum;
	}

	public void setSortOrderEnum(SortOrderEnum sortOrderEnum) {
		this.sortOrderEnum = sortOrderEnum;
	}
	
	public String getIconeOrdenacao(){
		if (getSortOrderEnum() == null) {
			/**
			if(context() != null && context().getViewRoot().getViewId().contains("/relatorio/")){
				return "../../../resources/imagens/setaDupla.png";
			}
			if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("aluno") || getUsuarioLogado().getVisaoLogar().equals("coordenador") || getUsuarioLogado().getVisaoLogar().equals("pais")){
				return "../resources/imagens/setaDupla.png";
			}
			**/
			return  FilterFactory.ICONE_SEM_ORDENACAO;
		} else if (getSortOrderEnum().equals(SortOrderEnum.DESCENDING)) {
			/**
			if(context() != null && context().getViewRoot().getViewId().contains("/relatorio/")){
				return "../../../resources/imagens/arrowup.png";
			}
			if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("aluno") || getUsuarioLogado().getVisaoLogar().equals("coordenador") || getUsuarioLogado().getVisaoLogar().equals("pais")){
				return "../resources/imagens/arrowup.png";
			}
			**/
			return FilterFactory.ICONE_ORDEM_DECRESCENTE;
		} else {
			return FilterFactory.ICONE_ORDEM_CRESCENTE;
		}
		/**
		if(context() != null && context().getViewRoot().getViewId().contains("/relatorio/")){
			return "../../../resources/imagens/arrowdown.png";
		}
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("aluno") || getUsuarioLogado().getVisaoLogar().equals("coordenador") || getUsuarioLogado().getVisaoLogar().equals("pais")){
			return "../resources/imagens/arrowdown.png";
		}
		**/
				
	}

	private static final String ICONE_SEM_ORDENACAO = "fa fa-sort fa-btn-white btn-primary dark"; 
	private static final String ICONE_ORDEM_CRESCENTE = "fa fa-sort-asc fa-btn-white btn-primary dark";
	private static final String ICONE_ORDEM_DECRESCENTE = "fa fa-sort-desc fa-btn-white btn-primary dark";

	public Ordenacao getOrdenacao() {
		if (ordenacao == null) {
			ordenacao = new Ordenacao();
			ordenacao.setCampo(getCampo());            
		}
		return ordenacao;
	}

	public void setOrdenacao(Ordenacao ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Date getFiltroData() {
		return filtroData;
	}

	public void setFiltroData(Date filtroData) {
		this.filtroData = filtroData;
	}
	
	
}
