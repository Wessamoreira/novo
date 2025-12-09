package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

public class TurmaDisciplinaNotaParcialVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String tituloNota;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO;
	private String variavel;

	public TurmaDisciplinaNotaParcialVO() {
		super();
	}

	public TurmaDisciplinaNotaParcialVO(String tituloNota, Boolean possuiFormula, String formula,
			TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO) {
		super();
		this.tituloNota = tituloNota;
		this.turmaDisciplinaNotaTituloVO = turmaDisciplinaNotaTituloVO;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getTituloNota() {
		if(tituloNota == null) {
			tituloNota = "";
		}
		return tituloNota;
	}

	public void setTituloNota(String tituloNota) {
		this.tituloNota = tituloNota;
	}

	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTituloVO() {
		if(turmaDisciplinaNotaTituloVO == null) {
			turmaDisciplinaNotaTituloVO = new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTituloVO;
	}

	public void setTurmaDisciplinaNotaTituloVO(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO) {
		this.turmaDisciplinaNotaTituloVO = turmaDisciplinaNotaTituloVO;
	}

	public String getVariavel() {
		if(variavel == null) {
			variavel = "";
		}
		return variavel;
	}

	public void setVariavel(String variavel) {
		this.variavel = variavel;
	}

}
