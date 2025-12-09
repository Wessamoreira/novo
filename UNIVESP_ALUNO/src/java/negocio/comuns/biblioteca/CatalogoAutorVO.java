package negocio.comuns.biblioteca;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.biblioteca.enumeradores.TipoAutoriaEnum;

public class CatalogoAutorVO extends SuperVO {

	private Integer codigo;
	private AutorVO autor;
	private CatalogoVO catalogo;
	private String tipoAutoria;
	private String siglaAutoria;
	private Integer ordemApresentacao;
	
	public static final long serialVersionUID = 1L;

	public CatalogoAutorVO() {
		super();
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public void setAutor(AutorVO autor) {
		this.autor = autor;
	}

	public AutorVO getAutor() {
		if (autor == null) {
			autor = new AutorVO();
		}
		return autor;
	}

	public void setCatalogo(CatalogoVO catalogo) {
		this.catalogo = catalogo;
	}

	public CatalogoVO getCatalogo() {
		if (catalogo == null) {
			catalogo = new CatalogoVO();
		}
		return catalogo;
	}

	public void setTipoAutoria(String tipoAutoria) {
		this.tipoAutoria = tipoAutoria;
	}

	public String getTipoAutoria() {
		if (tipoAutoria == null) {
			tipoAutoria = "";
		}
		return tipoAutoria;
	}

	public String getTipoAutoria_Apresentar() {
		for (TipoAutoriaEnum tipoAutoriaEnum : TipoAutoriaEnum.values()) {
			if (tipoAutoriaEnum.getKey().equals(getTipoAutoria())) {
				return tipoAutoriaEnum.getValue();
			}
		}
		return getTipoAutoria();
	}

	public void setSiglaAutoria(String siglaAutoria) {
		this.siglaAutoria = siglaAutoria;
	}

	public String getSiglaAutoria() {
		if (siglaAutoria == null) {
			siglaAutoria = "";			
		}
		return siglaAutoria;
	}

	public TipoAutoriaEnum getTipoAutoriaEnum() {
		return TipoAutoriaEnum.getTipoAutoriaEnumPorKey(getTipoAutoria());
	}

	public Integer getOrdemApresentacao() {
		if (ordemApresentacao == null) {
			ordemApresentacao = 0;
		}
		return ordemApresentacao;
	}

	public void setOrdemApresentacao(Integer ordemApresentacao) {
		this.ordemApresentacao = ordemApresentacao;
	}

}
