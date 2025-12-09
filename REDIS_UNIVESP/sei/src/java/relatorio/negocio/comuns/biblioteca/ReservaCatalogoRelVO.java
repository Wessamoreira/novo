package relatorio.negocio.comuns.biblioteca;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.CatalogoVO;

public class ReservaCatalogoRelVO extends SuperVO implements Serializable {
	
	private String catalogo;
	private String edicao;
	private Integer reservas;
	private String editora;
	private String autor;
	private String anoPublicacao;
	private PessoaVO pessoaVO;
	private CatalogoVO catalogoVO;
	private String situacaoReserva;
    private Date dataReserva;
    private Date dataTerminoReserva;
	
	public String getCatalogo() {
		if (catalogo == null) {
			catalogo = "";
		}
		return catalogo;
	}
	public void setCatalogo(String catalogo) {
		this.catalogo = catalogo;
	}
	public String getEdicao() {
		if (edicao == null) {
			edicao = "";
		}
		return edicao;
	}
	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}
	public Integer getReservas() {
		if (reservas == null) {
			reservas = 0;
		}
		return reservas;
	}
	public void setReservas(Integer reservas) {
		this.reservas = reservas;
	}
	public String getEditora() {
		if (editora == null) {
			editora = "";
		}
		return editora;
	}
	public void setEditora(String editora) {
		this.editora = editora;
	}
	public String getAutor() {
		if (autor == null) {
			autor = "";
		}
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public String getAnoPublicacao() {
		if (anoPublicacao == null) {
			anoPublicacao = "";
		}
		return anoPublicacao;
	}
	public void setAnoPublicacao(String anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}
	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}
	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}
	public CatalogoVO getCatalogoVO() {
		if (catalogoVO == null) {
			catalogoVO = new CatalogoVO();
		}
		return catalogoVO;
	}
	public void setCatalogoVO(CatalogoVO catalogoVO) {
		this.catalogoVO = catalogoVO;
	}
	public String getSituacaoReserva() {
		if (situacaoReserva == null) {
			situacaoReserva = "";
		}
		return situacaoReserva;
	}
	public void setSituacaoReserva(String situacaoReserva) {
		this.situacaoReserva = situacaoReserva;
	}
	public Date getDataReserva() {
		return dataReserva;
	}
	public void setDataReserva(Date dataReserva) {
		this.dataReserva = dataReserva;
	}
	public Date getDataTerminoReserva() {
		return dataTerminoReserva;
	}
	public void setDataTerminoReserva(Date dataTerminoReserva) {
		this.dataTerminoReserva = dataTerminoReserva;
	}

}
