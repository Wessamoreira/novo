package webservice.moodle;

import com.google.gson.annotations.SerializedName;

public class CursosItemRSVO {

	@SerializedName("curso")
	private CursoItemRSVO cursoRSVO;
	@SerializedName("turma")
	private TurmaItemRSVO turmaRSVO;
	@SerializedName("modulo")
	private ModuloItemRSVO moduloRSVO;
	
	public CursoItemRSVO getCursoRSVO() {
		if (cursoRSVO == null)
			cursoRSVO = new CursoItemRSVO();
		return cursoRSVO;
	}
	public void setCursoRSVO(CursoItemRSVO cursoRSVO) {
		this.cursoRSVO = cursoRSVO;
	}
	
	public TurmaItemRSVO getTurmaRSVO() {
		if (turmaRSVO == null)
			turmaRSVO = new TurmaItemRSVO();
		return turmaRSVO;
	}
	public void setTurmaRSVO(TurmaItemRSVO turmaRSVO) {
		this.turmaRSVO = turmaRSVO;
	}
	
	public ModuloItemRSVO getModuloRSVO() {
		if (moduloRSVO == null)
			moduloRSVO = new ModuloItemRSVO();
		return moduloRSVO;
	}
	public void setModuloRSVO(ModuloItemRSVO moduloRSVO) {
		this.moduloRSVO = moduloRSVO;
	}
}