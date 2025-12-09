package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class InclusaoHistoricoAlunoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9222841471175546665L;
	private Integer codigo;
	private MatriculaVO matriculaVO;
	private GradeCurricularVO gradeCurricular;
	private Date dataInclusao;
	private UsuarioVO responsavelInclusao;
	private String observacao;
	private List<InclusaoHistoricoAlunoDisciplinaVO> inclusaoHistoricoAlunoDisciplinaVOs;
	/*
	 * TRansiente
	 */
	private MatriculaVO matriculaAproveitarDisciplinaVO;
	private MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO;
	private List<HistoricoVO> historicoNaoAproveitadoVOs;
	private List<HistoricoVO> historicoAproveitadoVOs;	
	private List<HistoricoVO> historicoJaAdicionadoVOs;	
	
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public MatriculaVO getMatriculaVO() {
		if(matriculaVO == null){
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public GradeCurricularVO getGradeCurricular() {
		if(gradeCurricular == null){
			gradeCurricular = new GradeCurricularVO();
		}
		return gradeCurricular;
	}

	public void setGradeCurricular(GradeCurricularVO gradeCurricular) {
		this.gradeCurricular = gradeCurricular;
	}

	public Date getDataInclusao() {
		if(dataInclusao == null){
			dataInclusao = new Date();
		}
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public UsuarioVO getResponsavelInclusao() {
		if(responsavelInclusao == null){
			responsavelInclusao = new UsuarioVO();
		}
		return responsavelInclusao;
	}

	public void setResponsavelInclusao(UsuarioVO responsavelInclusao) {
		this.responsavelInclusao = responsavelInclusao;
	}

	public String getObservacao() {
		if(observacao == null){
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public List<InclusaoHistoricoAlunoDisciplinaVO> getInclusaoHistoricoAlunoDisciplinaVOs() {
		if(inclusaoHistoricoAlunoDisciplinaVOs == null){
			inclusaoHistoricoAlunoDisciplinaVOs = new ArrayList<InclusaoHistoricoAlunoDisciplinaVO>(0);
		}
		return inclusaoHistoricoAlunoDisciplinaVOs;
	}

	public void setInclusaoHistoricoAlunoDisciplinaVOs(
			List<InclusaoHistoricoAlunoDisciplinaVO> inclusaoHistoricoAlunoDisciplinaVOs) {
		this.inclusaoHistoricoAlunoDisciplinaVOs = inclusaoHistoricoAlunoDisciplinaVOs;
	}

	public MatriculaVO getMatriculaAproveitarDisciplinaVO() {
		if(matriculaAproveitarDisciplinaVO == null){
			matriculaAproveitarDisciplinaVO = new MatriculaVO();
		}
		return matriculaAproveitarDisciplinaVO;
	}

	public void setMatriculaAproveitarDisciplinaVO(MatriculaVO matriculaAproveitarDisciplinaVO) {
		this.matriculaAproveitarDisciplinaVO = matriculaAproveitarDisciplinaVO;
	}

	public List<HistoricoVO> getHistoricoNaoAproveitadoVOs() {
		if(historicoNaoAproveitadoVOs == null){
			historicoNaoAproveitadoVOs =  new ArrayList<HistoricoVO>(0);
		}
		return historicoNaoAproveitadoVOs;
	}

	public void setHistoricoNaoAproveitadoVOs(List<HistoricoVO> historicoNaoAproveitadoVOs) {
		this.historicoNaoAproveitadoVOs = historicoNaoAproveitadoVOs;
	}

	public List<HistoricoVO> getHistoricoAproveitadoVOs() {
		if(historicoAproveitadoVOs == null){
			historicoAproveitadoVOs = new ArrayList<HistoricoVO>();
		}
		return historicoAproveitadoVOs;
	}

	public void setHistoricoAproveitadoVOs(List<HistoricoVO> historicoAproveitadoVOs) {
		this.historicoAproveitadoVOs = historicoAproveitadoVOs;
	}

	public MapaEquivalenciaMatrizCurricularVO getMapaEquivalenciaMatrizCurricularVO() {
		if(mapaEquivalenciaMatrizCurricularVO == null){
			mapaEquivalenciaMatrizCurricularVO = new MapaEquivalenciaMatrizCurricularVO();
		}
		return mapaEquivalenciaMatrizCurricularVO;
	}

	public void setMapaEquivalenciaMatrizCurricularVO(
			MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO) {
		this.mapaEquivalenciaMatrizCurricularVO = mapaEquivalenciaMatrizCurricularVO;
	}

	public List<HistoricoVO> getHistoricoJaAdicionadoVOs() {
		if(historicoJaAdicionadoVOs == null){
			historicoJaAdicionadoVOs = new ArrayList<HistoricoVO>(0);
		}
		return historicoJaAdicionadoVOs;
	}

	public void setHistoricoJaAdicionadoVOs(List<HistoricoVO> historicoJaAdicionadoVOs) {
		this.historicoJaAdicionadoVOs = historicoJaAdicionadoVOs;
	}

	
	
}
