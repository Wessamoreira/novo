package negocio.comuns.ead;

import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.ead.enumeradores.TipoAvaliacaoPBLEnum;

/**
 * @author Victor Hugo de Paula Costa - 30 de jun de 2016
 *
 */
public class NotaConceitoAvaliacaoPBLVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	/**
	 * @author Victor Hugo de Paula Costa - 30 de jun de 2016
	 */
	private Integer codigo;
	private ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO;
	private String conceito;
	private Double notaCorrespondente;
	private TipoAvaliacaoPBLEnum tipoAvaliacao;
	//Transient
	private String tipoNotaConceito;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ConteudoUnidadePaginaRecursoEducacionalVO getConteudoUnidadePaginaRecursoEducacionalVO() {
		if (conteudoUnidadePaginaRecursoEducacionalVO == null) {
			conteudoUnidadePaginaRecursoEducacionalVO = new ConteudoUnidadePaginaRecursoEducacionalVO();
		}
		return conteudoUnidadePaginaRecursoEducacionalVO;
	}

	public void setConteudoUnidadePaginaRecursoEducacionalVO(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) {
		this.conteudoUnidadePaginaRecursoEducacionalVO = conteudoUnidadePaginaRecursoEducacionalVO;
	}

	public String getConceito() {
		if (conceito == null) {
			conceito = "";
		}
		return conceito;
	}

	public void setConceito(String conceito) {
		this.conceito = conceito;
	}

	public Double getNotaCorrespondente() {
		if (notaCorrespondente == null) {
			notaCorrespondente = 0.0;
		}
		return notaCorrespondente;
	}

	public void setNotaCorrespondente(Double notaCorrespondente) {
		this.notaCorrespondente = notaCorrespondente;
	}

	public TipoAvaliacaoPBLEnum getTipoAvaliacao() {
		if (tipoAvaliacao == null) {
			tipoAvaliacao = TipoAvaliacaoPBLEnum.AUTO_AVALIACAO;
		}
		return tipoAvaliacao;
	}

	public void setTipoAvaliacao(TipoAvaliacaoPBLEnum tipoAvaliacao) {
		this.tipoAvaliacao = tipoAvaliacao;
	}

	public String getTipoNotaConceito() {
		if (tipoNotaConceito == null) {
			tipoNotaConceito = "";
		}
		return tipoNotaConceito;
	}

	public void setTipoNotaConceito(String tipoNotaConceito) {
		this.tipoNotaConceito = tipoNotaConceito;
	}
}
