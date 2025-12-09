package relatorio.negocio.comuns.academico;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.dominios.DiaSemana;

public class RequerimentoResumoOperacaoLoteVO {

	private TipoRequerimentoVO tipoRequerimentoVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CursoVO cursoVO;
	private Integer qtdeMatriculados;
	private Integer qtdeRequerimento;
	private Integer qtdeRequerimentoDeferido;
	
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}


	public Integer getQtdeMatriculados() {
		if(qtdeMatriculados == null) {
			qtdeMatriculados = 0;
		}
		return qtdeMatriculados;
	}

	public void setQtdeMatriculados(Integer qtdeMatriculados) {
		this.qtdeMatriculados = qtdeMatriculados;
	}

	public Integer getQtdeRequerimento() {
		if(qtdeRequerimento == null) {
			qtdeRequerimento = 0;
		}
		return qtdeRequerimento;
	}

	public void setQtdeRequerimento(Integer qtdeRequerimento) {
		this.qtdeRequerimento = qtdeRequerimento;
	}

	public TipoRequerimentoVO getTipoRequerimentoVO() {
		if(tipoRequerimentoVO == null) {
			tipoRequerimentoVO =  new TipoRequerimentoVO();
		}
		return tipoRequerimentoVO;
	}

	public void setTipoRequerimentoVO(TipoRequerimentoVO tipoRequerimentoVO) {
		this.tipoRequerimentoVO = tipoRequerimentoVO;
	}
	
	public String getOrdenarPorUnidadeEnsino() {
		return getUnidadeEnsinoVO().getNome();
	}
	
	public String getOrdenarPorUnidadeEnsinoCurso() {
		return getUnidadeEnsinoVO().getNome()+"C"+getCursoVO().getNome();
	}
	public Integer getQtdeRequerimentoDeferido() {
		return qtdeRequerimentoDeferido;
	}

	public void setQtdeRequerimentoDeferido(Integer qtdeRequerimentoDeferido) {
		if(qtdeRequerimentoDeferido == null){
			qtdeRequerimentoDeferido = 0;			
		}
		this.qtdeRequerimentoDeferido = qtdeRequerimentoDeferido;
	}

}
