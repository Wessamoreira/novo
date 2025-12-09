package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 * 
 * @author Pedro Andrade 09/01/2017
 *
 */
public class MonitorConhecimentoPBLVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4756919827355298490L;
	private List<GraficoAproveitamentoAssuntoPBLVO> listaGraficoAproveitamentoAssuntoPBLVOs;

	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private ConteudoVO conteudoVO;
	private String ano;
	private String semestre;
	
	public Integer getTamanhoListaGraficoAproveitamentoAssuntoPBLVOs(){
		return getListaGraficoAproveitamentoAssuntoPBLVOs().size();
	}

	public List<GraficoAproveitamentoAssuntoPBLVO> getListaGraficoAproveitamentoAssuntoPBLVOs() {
		if (listaGraficoAproveitamentoAssuntoPBLVOs == null) {
			listaGraficoAproveitamentoAssuntoPBLVOs = new ArrayList<GraficoAproveitamentoAssuntoPBLVO>();
		}
		return listaGraficoAproveitamentoAssuntoPBLVOs;
	}

	public void setListaGraficoAproveitamentoAssuntoPBLVOs(List<GraficoAproveitamentoAssuntoPBLVO> listaGraficoAproveitamentoPBLVOs) {
		this.listaGraficoAproveitamentoAssuntoPBLVOs = listaGraficoAproveitamentoPBLVOs;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public ConteudoVO getConteudoVO() {
		if (conteudoVO == null) {
			conteudoVO = new ConteudoVO();
		}
		return conteudoVO;
	}

	public void setConteudoVO(ConteudoVO conteudoVO) {
		this.conteudoVO = conteudoVO;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

}
