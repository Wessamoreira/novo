
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.arquitetura;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.faces. model.DataModel;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Marco Tulio Bueno
 */
public class DataModelo extends DataModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int page;
	private int rowIndex = -1;
	private int pageSize;
	private List<?> list;
	private int limitePorPagina;
	private int offset;
	private Integer totalRegistrosEncontrados;
	private int paginaAtual;
	private String valorConsulta;
	private String campoConsulta;
	private Date dataIni;
	private Date dataFim;
	private boolean controlarAcesso = false;
	private int nivelMontarDados = Uteis.NIVELMONTARDADOS_DADOSCONSULTA;
	private UsuarioVO usuario;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<Object> listaFiltros;
	private Map<String, Double> totalizadores;
	public DataModelo() {
		this.dataIni = new Date();
	}

	public DataModelo(Integer limit, Integer offset, UsuarioVO usuario) {
		this(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, limit, offset, usuario);
	}

	public DataModelo(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		this(controlarAcesso, nivelMontarDados, 10, 0, usuario);
	}

	public DataModelo(boolean controlarAcesso, int nivelMontarDados, Integer limit, Integer offset, UsuarioVO usuario) {
		this.controlarAcesso = controlarAcesso;
		this.nivelMontarDados = nivelMontarDados;
		this.limitePorPagina = limit;
		this.offset = offset;
		this.usuario = usuario;
		this.listaFiltros = new ArrayList<>();
	}
	
	public void preencherDadosParaConsulta(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		setControlarAcesso(controlarAcesso);
		setNivelMontarDados(nivelMontarDados);
		setUsuario(usuario);
		setLimitePorPagina(10);
		getListaFiltros().clear();
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Date getDataIni() {
		return dataIni;
	}

	public void setDataIni(Date dataIni) {
		this.dataIni = dataIni;
	}

	public String getValorConsulta() {
		if (valorConsulta == null) {
			valorConsulta = "";
		}
		return valorConsulta;
	}

	public void setValorConsulta(String valorConsulta) {		
		this.valorConsulta = valorConsulta;
	}

	public String getCampoConsulta() {
		if(campoConsulta == null){
			campoConsulta = "";
		}
		return campoConsulta;
	}

	public void setCampoConsulta(String campoConsulta) {		
		this.campoConsulta = campoConsulta;
	}

	public int getLimitePorPagina() {
		return limitePorPagina;
	}

	public void setLimitePorPagina(int limitePorPagina) {
		this.limitePorPagina = limitePorPagina;
	}

	public int getOffset() {
		if (offset < 0) {
			return 0;
		}
		return getLimitePorPagina() * (getPaginaAtual() - 1);
	}

	public void setOffset(int offset) {
		if (offset < 0) {
			this.offset = 0;
		}
		this.offset = offset;
	}

	public int getPaginaAtual() {
		return paginaAtual;
	}

	public void setPaginaAtual(int paginaAtual) {
		if (paginaAtual <= 0) {
			this.paginaAtual = 1;

		} else {
			this.paginaAtual = paginaAtual;

		}
	}

	public Integer getTotalRegistrosEncontrados() {
		if (totalRegistrosEncontrados == null) {
			totalRegistrosEncontrados = (0);
		}
		return totalRegistrosEncontrados;
	}

	public void setTotalRegistrosEncontrados(Integer totalRegistrosEncontrados) {
		this.totalRegistrosEncontrados = totalRegistrosEncontrados;
	}
	
	public Integer getTotalElementosApresentar() {
		return getTotalRegistrosEncontrados() >= getLimitePorPagina() ?  getLimitePorPagina() : getTotalRegistrosEncontrados(); 
	}
	
	

	public int getPage() {

		// if(getPaginaAtual() == 1 && page !=2){
		// page = 0;
		// }
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	@Override
	public boolean isRowAvailable() {
		if (getListaConsulta() == null || getListaConsulta().isEmpty()) {
			return false;
		}

		if (this.rowIndex >= getTotalRegistrosEncontrados()) {
			return false;
		}

		int rowIndex = getRowIndex();
		if (rowIndex >= 0 && rowIndex < getListaConsulta().size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getRowCount() {
		return getTotalRegistrosEncontrados().intValue();
	}

	@Override
	public Object getRowData() {
		if (list == null) {
			return null;
		} else if (!isRowAvailable()) {
			throw new IllegalArgumentException();
		} else {
			int dataIndex = getRowIndex();
			return list.get(dataIndex);
		}
	}

	@Override
	public int getRowIndex() {
		if (pageSize == 0) {
			return 0;
		}
		if ((pageSize < getLimitePorPagina() && getLimitePorPagina() > rowIndex) || getLimitePorPagina() == 0) {
			return this.rowIndex;
		} else {
			return (rowIndex % getLimitePorPagina());
		}
	}

	@Override
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	@Override
	public Object getWrappedData() {
		return list;
	}

	@Override
	public void setWrappedData(Object list) {
		this.list = (List<?>) list;
	}

	@SuppressWarnings("rawtypes")
	public List<?> getListaConsulta() {
		if (list == null) {
			list = new ArrayList(0);
		}
		return list;
	}

	public void setListaConsulta(List<?> list) {
		setWrappedData(list);
		this.pageSize = list.size();
	}

	public Boolean getApresentarPaginador() {
		return list != null && !list.isEmpty();
	}

	public Boolean getApresentarListaConsulta() {
		return list != null && !list.isEmpty();
	}

	public Boolean getApresentarDatascroller() {
		return list != null && !list.isEmpty() && getTotalRegistrosEncontrados() > getLimitePorPagina();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isControlarAcesso() {
		return controlarAcesso;
	}

	public void setControlarAcesso(boolean controlarAcesso) {
		this.controlarAcesso = controlarAcesso;
	}

	public int getNivelMontarDados() {
		return nivelMontarDados;
	}

	public void setNivelMontarDados(int nivelMontarDados) {
		this.nivelMontarDados = nivelMontarDados;
	}

	public UsuarioVO getUsuario() {
		usuario = Optional.ofNullable(usuario).orElse(new UsuarioVO());
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}

	public List<Object> getListaFiltros() {
		listaFiltros = Optional.ofNullable(listaFiltros).orElse(new ArrayList<>());
		return listaFiltros;
	}

	public void setListaFiltros(List<Object> listaFiltros) {
		this.listaFiltros = listaFiltros;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if(unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}	
	public int getTotalPaginas() {
		
		BigDecimal totalPaginasParcial = BigDecimal.ZERO;		
		if (Uteis.isAtributoPreenchido(getTotalRegistrosEncontrados()) && Uteis.isAtributoPreenchido(getLimitePorPagina())) {
			totalPaginasParcial = new BigDecimal(getTotalRegistrosEncontrados()).divide(new BigDecimal(getLimitePorPagina()));
		}		
		
		return  Uteis.arredondarDivisaoEntreNumeros(totalPaginasParcial.doubleValue());
	}

	public Map<String, Double> getTotalizadores() {
		if (totalizadores == null) {
			totalizadores = new HashMap<>();
		}
		return totalizadores;
	}
}
