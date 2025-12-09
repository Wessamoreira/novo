package negocio.comuns.arquitetura;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.arquitetura.enumeradores.TipoFiltroEnum;

public class CampoLogTriggerVO implements Serializable {
	
	public CampoLogTriggerVO() {
	}
	
	public CampoLogTriggerVO(String nome, String tipo) {
		
		this.nome = nome;
		if (tipo.equals("character varying")) {
			this.tipo = "varchar";
		} else if (tipo.equals("timestamp without time zone")) {
			this.tipo = "timestamp";
		} else {
			this.tipo = tipo;
		}
		montarListaTiposFiltro();
	}

	private String nome;
	private String valor;
	private String valorAlterado;
	private String tipo;
	private List<SelectItem> listaTiposFiltro;
	private TipoFiltroEnum tipoFiltro;
	private String input;
	private Date dataInicialCampo;
	private Date dataFinalCampo;
	private Boolean selecionado;
	private Integer linha;
	private Integer coluna;
	
	public void montarListaTiposFiltro() {
		
		if (getTipo().equals("character")) {
			montarListaTipoCaracter();
			setTipoFiltro(TipoFiltroEnum.IGUAL_TEXTO);
		} else if (getTipo().equals("varchar") || getTipo().equals("text")) {
			montarListaTipoTexto();
			setTipoFiltro(TipoFiltroEnum.IGUAL_TEXTO);
		} else if (getTipo().equals("boolean")) {
			montarListaTipoBooleano();
			setTipoFiltro(TipoFiltroEnum.VERDADEIRO);
		} else if (getTipo().equals("integer") || getTipo().equals("numeric") || getTipo().equals("real")) {
			montarListaTipoNumerico();
			setTipoFiltro(TipoFiltroEnum.IGUAL_NUMERO);
		} else if (getTipo().equals("timestamp") || getTipo().equals("date")) {
			montarListaTipoData();
			setTipoFiltro(TipoFiltroEnum.ENTRE_DATAS);
		}
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.NULO.toString(), TipoFiltroEnum.NULO.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.NAO_NULO.toString(), TipoFiltroEnum.NAO_NULO.getTexto()));
	}
	
	public void montarListaTipoCaracter() {
		
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.IGUAL_TEXTO.toString(), TipoFiltroEnum.IGUAL_TEXTO.getTexto()));
	}
	
	public void montarListaTipoTexto() {
		
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.IGUAL_TEXTO.toString(), TipoFiltroEnum.IGUAL_TEXTO.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.CONTENDO_TEXTO.toString(), TipoFiltroEnum.CONTENDO_TEXTO.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.INICIANDO_COM_TEXTO.toString(), TipoFiltroEnum.INICIANDO_COM_TEXTO.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.TERMINANDO_COM_TEXTO.toString(), TipoFiltroEnum.TERMINANDO_COM_TEXTO.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.DIFERENTE_TEXTO.toString(), TipoFiltroEnum.DIFERENTE_TEXTO.getTexto()));
	}
	
	public void montarListaTipoBooleano() {
		
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.VERDADEIRO.toString(), TipoFiltroEnum.VERDADEIRO.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.FALSO.toString(), TipoFiltroEnum.FALSO.getTexto()));
	}
	
	public void montarListaTipoNumerico() {
		
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.IGUAL_NUMERO.toString(), TipoFiltroEnum.IGUAL_NUMERO.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.MAIOR_IGUAL_NUMERO.toString(), TipoFiltroEnum.MAIOR_IGUAL_NUMERO.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.MAIOR_NUMERO.toString(), TipoFiltroEnum.MAIOR_NUMERO.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.MENOR_IGUAL_NUMERO.toString(), TipoFiltroEnum.MENOR_IGUAL_NUMERO.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.MENOR_NUMERO.toString(), TipoFiltroEnum.MENOR_NUMERO.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.DIFERENTE_NUMERO.toString(), TipoFiltroEnum.DIFERENTE_NUMERO.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.CONTENDO_NUMEROS.toString(), TipoFiltroEnum.CONTENDO_NUMEROS.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.NAO_CONTENDO_NUMEROS.toString(), TipoFiltroEnum.NAO_CONTENDO_NUMEROS.getTexto()));
	}
	
	public void montarListaTipoData() {
		
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.IGUAL_DATA.toString(), TipoFiltroEnum.IGUAL_DATA.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.ENTRE_DATAS.toString(), TipoFiltroEnum.ENTRE_DATAS.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.A_PARTIR_DATA.toString(), TipoFiltroEnum.A_PARTIR_DATA.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.ATE_DATA.toString(), TipoFiltroEnum.ATE_DATA.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.DIFERENTE_DATA.toString(), TipoFiltroEnum.DIFERENTE_DATA.getTexto()));
		getListaTiposFiltro().add(new SelectItem(TipoFiltroEnum.NAO_ENTRE_DATAS.toString(), TipoFiltroEnum.NAO_ENTRE_DATAS.getTexto()));
	}
	
	public boolean getApresentarFiltroDataInicial() {
		
		return (getTipoFiltro() != null && (
				getTipoFiltro().equals(TipoFiltroEnum.IGUAL_DATA) ||
				getTipoFiltro().equals(TipoFiltroEnum.A_PARTIR_DATA) || 
				getTipoFiltro().equals(TipoFiltroEnum.DIFERENTE_DATA) || 
				getTipoFiltro().equals(TipoFiltroEnum.ENTRE_DATAS) ||
				getTipoFiltro().equals(TipoFiltroEnum.NAO_ENTRE_DATAS)));
	}
	
	public boolean getApresentarFiltroDataFinal() {
		
		return (getTipoFiltro() != null && (
				getTipoFiltro().equals(TipoFiltroEnum.ATE_DATA) || 
				getTipoFiltro().equals(TipoFiltroEnum.ENTRE_DATAS) ||
				getTipoFiltro().equals(TipoFiltroEnum.NAO_ENTRE_DATAS)));
	}
	
	public boolean getApresentarFiltroInput() {
		
		return (getTipoFiltro() != null &&
				!getTipoFiltro().equals(TipoFiltroEnum.VERDADEIRO) &&
				!getTipoFiltro().equals(TipoFiltroEnum.FALSO) &&
				!getTipoFiltro().equals(TipoFiltroEnum.NULO) &&
				!getTipoFiltro().equals(TipoFiltroEnum.NAO_NULO) &&
				!getApresentarFiltroDataInicial() &&
				!getApresentarFiltroDataFinal());
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getInput() {
		if (input == null) {
			input = "";
		}
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
	
	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = false;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public String getTipo() {
		if (tipo == null) {
			tipo = "";
		}
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValor() {
		if (valor == null) {
			valor = "";
		}
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getValorAlterado() {
		if (valorAlterado == null) {
			valorAlterado = "";
		}
		return valorAlterado;
	}

	public void setValorAlterado(String valorAlterado) {
		this.valorAlterado = valorAlterado;
	}

	public TipoFiltroEnum getTipoFiltro() {
		return tipoFiltro;
	}

	public void setTipoFiltro(TipoFiltroEnum tipoFiltro) {
		this.tipoFiltro = tipoFiltro;
	}

	public Date getDataInicialCampo() {
		return dataInicialCampo;
	}

	public void setDataInicialCampo(Date dataInicialCampo) {
		this.dataInicialCampo = dataInicialCampo;
	}

	public Date getDataFinalCampo() {
		return dataFinalCampo;
	}

	public void setDataFinalCampo(Date dataFinalCampo) {
		this.dataFinalCampo = dataFinalCampo;
	}

	public List<SelectItem> getListaTiposFiltro() {
		if (listaTiposFiltro == null) {
			listaTiposFiltro = new ArrayList<SelectItem>(0);
		}
		return listaTiposFiltro;
	}

	public void setListaTiposFiltro(List<SelectItem> listaTiposFiltro) {
		this.listaTiposFiltro = listaTiposFiltro;
	}

	/**
	 * @return the linha
	 */
	public Integer getLinha() {
		if (linha == null) {
			linha = 1;
		}
		return linha;
	}

	/**
	 * @param linha the linha to set
	 */
	public void setLinha(Integer linha) {
		this.linha = linha;
	}

	/**
	 * @return the coluna
	 */
	public Integer getColuna() {
		if (coluna == null) {
			coluna = 1;
		}
		return coluna;
	}

	/**
	 * @param coluna the coluna to set
	 */
	public void setColuna(Integer coluna) {
		this.coluna = coluna;
	}
	
	public Integer getOrdenacaoLinhaColuna(){
		return Integer.valueOf(getLinha()+""+getColuna());
	}
	
	

}
