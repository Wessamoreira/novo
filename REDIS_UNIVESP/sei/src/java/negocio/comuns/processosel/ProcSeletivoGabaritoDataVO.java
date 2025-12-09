package negocio.comuns.processosel;

import negocio.comuns.arquitetura.SuperVO;

public class ProcSeletivoGabaritoDataVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProva;
	private DisciplinasProcSeletivoVO disciplinaIdioma;
	private GabaritoVO gabaritoVO;

	public ProcSeletivoGabaritoDataVO() {

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

	public ItemProcSeletivoDataProvaVO getItemProcSeletivoDataProva() {
		if (itemProcSeletivoDataProva == null) {
			itemProcSeletivoDataProva = new ItemProcSeletivoDataProvaVO();
		}
		return itemProcSeletivoDataProva;
	}

	public void setItemProcSeletivoDataProva(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProva) {
		this.itemProcSeletivoDataProva = itemProcSeletivoDataProva;
	}

	public GabaritoVO getGabaritoVO() {
		if (gabaritoVO == null) {
			gabaritoVO = new GabaritoVO();
		}
		return gabaritoVO;
	}

	public void setGabaritoVO(GabaritoVO gabaritoVO) {
		this.gabaritoVO = gabaritoVO;
	}

	public DisciplinasProcSeletivoVO getDisciplinaIdioma() {
		if (disciplinaIdioma == null) {
			disciplinaIdioma = new DisciplinasProcSeletivoVO();
		}
		return disciplinaIdioma;
	}

	public void setDisciplinaIdioma(DisciplinasProcSeletivoVO disciplinaIdioma) {
		this.disciplinaIdioma = disciplinaIdioma;
	}

}
