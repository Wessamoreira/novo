package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces.model.SelectItem;

import negocio.comuns.academico.enumeradores.SituacaoRenovacaoMatriculaPeriodoEnum;
import negocio.comuns.arquitetura.SuperVO;

public class RenovacaoMatriculaTurmaMatriculaPeriodoVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private RenovacaoMatriculaTurmaVO renovacaoTurmaVO;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private MatriculaPeriodoVO novaMatriculaPeriodoVO;
	private Boolean selecionado;
	private SituacaoRenovacaoMatriculaPeriodoEnum situacao;
	private String mensagemErro;
//	private CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO;
//	private String informacaoFinanceira; 
	
	/**
	 * Transiente
	 * @return
	 */
	private List<SelectItem> listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso;
	private ProcessoMatriculaVO processoMatriculaRenovarTemp;
	private PeriodoLetivoVO periodoLetivoRenovarTemp;
	private TurmaVO turmaRenovarTemp;
//	private PlanoFinanceiroCursoVO planoFinanceiroCursoRenovarTemp;
//	private CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoRenovarTemp;
	
	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = true;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

//	public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCursoVO() {
//		if (condicaoPagamentoPlanoFinanceiroCursoVO == null) {
//			condicaoPagamentoPlanoFinanceiroCursoVO = new CondicaoPagamentoPlanoFinanceiroCursoVO();
//		}
//		return condicaoPagamentoPlanoFinanceiroCursoVO;
//	}
//
//	public void setCondicaoPagamentoPlanoFinanceiroCursoVO(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO) {
//		this.condicaoPagamentoPlanoFinanceiroCursoVO = condicaoPagamentoPlanoFinanceiroCursoVO;
//	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public RenovacaoMatriculaTurmaVO getRenovacaoTurmaVO() {
		if (renovacaoTurmaVO == null) {
			renovacaoTurmaVO = new RenovacaoMatriculaTurmaVO();
		}
		return renovacaoTurmaVO;
	}

	public void setRenovacaoTurmaVO(RenovacaoMatriculaTurmaVO renovacaoTurmaVO) {
		this.renovacaoTurmaVO = renovacaoTurmaVO;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public SituacaoRenovacaoMatriculaPeriodoEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoRenovacaoMatriculaPeriodoEnum.AGUARDANDO_REALIZACAO;
		}
		return situacao;
	}

	public void setSituacao(SituacaoRenovacaoMatriculaPeriodoEnum situacao) {
		this.situacao = situacao;
	}

	public String getMensagemErro() {
		if (mensagemErro == null) {
			mensagemErro = "";
		}
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

//	public List<SelectItem> getListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso() {
//		if (listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso == null) {
//			listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso = new ArrayList<SelectItem>(0);
//			if(getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo()>0){
//				listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso.add(new SelectItem(getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo(), getCondicaoPagamentoPlanoFinanceiroCursoVO().getDescricao()));
//			}			
//		}
//		return listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso;
//	}
//
//	public void setListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso(List<CondicaoPagamentoPlanoFinanceiroCursoVO> listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso) {
//		this.listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso = new ArrayList<SelectItem>(0);
//		this.listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso.add(new SelectItem(0, ""));
//		for(CondicaoPagamentoPlanoFinanceiroCursoVO obj: listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso){
//			this.listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
//		}
//	}

	public MatriculaPeriodoVO getNovaMatriculaPeriodoVO() {
		if (novaMatriculaPeriodoVO == null) {
			novaMatriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return novaMatriculaPeriodoVO;
	}

	public void setNovaMatriculaPeriodoVO(MatriculaPeriodoVO novaMatriculaPeriodoVO) {
		this.novaMatriculaPeriodoVO = novaMatriculaPeriodoVO;
	}

//	public String getInformacaoFinanceira() {
//		if (informacaoFinanceira == null || getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo() == 0) {
//			informacaoFinanceira = "";
//		}
//		return informacaoFinanceira;
//	}
//
//	public void setInformacaoFinanceira(String informacaoFinanceira) {
//		this.informacaoFinanceira = informacaoFinanceira;
//	}

	public ProcessoMatriculaVO getProcessoMatriculaRenovarTemp() {
		if (processoMatriculaRenovarTemp == null) {
			processoMatriculaRenovarTemp = new ProcessoMatriculaVO();
		}
		return processoMatriculaRenovarTemp;
	}

	public void setProcessoMatriculaRenovarTemp(ProcessoMatriculaVO processoMatriculaRenovarTemp) {
		this.processoMatriculaRenovarTemp = processoMatriculaRenovarTemp;
	}

	public PeriodoLetivoVO getPeriodoLetivoRenovarTemp() {
		if (periodoLetivoRenovarTemp == null) {
			periodoLetivoRenovarTemp = new PeriodoLetivoVO();
		}
		return periodoLetivoRenovarTemp;
	}

	public void setPeriodoLetivoRenovarTemp(PeriodoLetivoVO periodoLetivoRenovarTemp) {
		this.periodoLetivoRenovarTemp = periodoLetivoRenovarTemp;
	}

	public TurmaVO getTurmaRenovarTemp() {
		if (turmaRenovarTemp == null) {
			turmaRenovarTemp = new TurmaVO();
		}
		return turmaRenovarTemp;
	}

	public void setTurmaRenovarTemp(TurmaVO turmaRenovarTemp) {
		this.turmaRenovarTemp = turmaRenovarTemp;
	}

//	public PlanoFinanceiroCursoVO getPlanoFinanceiroCursoRenovarTemp() {
//		if (planoFinanceiroCursoRenovarTemp == null) {
//			planoFinanceiroCursoRenovarTemp = new PlanoFinanceiroCursoVO();
//		}
//		return planoFinanceiroCursoRenovarTemp;
//	}
//
//	public void setPlanoFinanceiroCursoRenovarTemp(PlanoFinanceiroCursoVO planoFinanceiroCursoRenovarTemp) {
//		this.planoFinanceiroCursoRenovarTemp = planoFinanceiroCursoRenovarTemp;
//	}
//
//	public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCursoRenovarTemp() {
//		if (condicaoPagamentoPlanoFinanceiroCursoRenovarTemp == null) {
//			condicaoPagamentoPlanoFinanceiroCursoRenovarTemp = new CondicaoPagamentoPlanoFinanceiroCursoVO();
//		}
//		return condicaoPagamentoPlanoFinanceiroCursoRenovarTemp;
//	}
//
//	public void setCondicaoPagamentoPlanoFinanceiroCursoRenovarTemp(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoRenovarTemp) {
//		this.condicaoPagamentoPlanoFinanceiroCursoRenovarTemp = condicaoPagamentoPlanoFinanceiroCursoRenovarTemp;
//	}
	
	
	
	

}
