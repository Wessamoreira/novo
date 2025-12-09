package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.SituacaoSolicitacaoAberturaTurmaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class SolicitacaoAberturaTurmaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 850069999465784087L;

	private Integer codigo;
	private Date dataSolicitacao;
	private UsuarioVO usuarioSolicitacao;
	private Date dataAlteracao;
	private UsuarioVO usuarioAlteracao;
	private UnidadeEnsinoVO unidadeEnsino;	
	private TurmaVO turma;
	private Integer quantidadeModulo;
	private SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAberturaTurma;
	private List<SolicitacaoAberturaTurmaDisciplinaVO> solicitacaoAberturaTurmaDisciplinaVOs;
	
	
	List<CalendarioHorarioAulaVO<SolicitacaoAberturaTurmaDisciplinaVO>> calendarioHorarioAulaVOs;

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getDataSolicitacao() {
		if(dataSolicitacao == null){
			dataSolicitacao = new Date();
		}
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public UsuarioVO getUsuarioSolicitacao() {
		if(usuarioSolicitacao == null){
			usuarioSolicitacao = new UsuarioVO();
		}
		return usuarioSolicitacao;
	}

	public void setUsuarioSolicitacao(UsuarioVO usuarioSolicitacao) {
		this.usuarioSolicitacao = usuarioSolicitacao;
	}

	public Date getDataAlteracao() {
		if(dataAlteracao == null){
			dataAlteracao = new Date();
		}
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public UsuarioVO getUsuarioAlteracao() {
		if(usuarioAlteracao == null){
			usuarioAlteracao = new UsuarioVO();
		}
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(UsuarioVO usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public TurmaVO getTurma() {
		if(turma == null){
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	public Integer getQuantidadeModulo() {
		if (quantidadeModulo == null) {
			quantidadeModulo = 0;
		}
		return quantidadeModulo;
	}

	public void setQuantidadeModulo(Integer quantidadeModulo) {
		this.quantidadeModulo = quantidadeModulo;
	}

	public SituacaoSolicitacaoAberturaTurmaEnum getSituacaoSolicitacaoAberturaTurma() {
		if (situacaoSolicitacaoAberturaTurma == null) {
			situacaoSolicitacaoAberturaTurma = SituacaoSolicitacaoAberturaTurmaEnum.AGUARDANDO_AUTORIZACAO;
		}
		return situacaoSolicitacaoAberturaTurma;
	}

	public void setSituacaoSolicitacaoAberturaTurma(SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAberturaTurma) {
		this.situacaoSolicitacaoAberturaTurma = situacaoSolicitacaoAberturaTurma;
	}

	public List<SolicitacaoAberturaTurmaDisciplinaVO> getSolicitacaoAberturaTurmaDisciplinaVOs() {
		if(solicitacaoAberturaTurmaDisciplinaVOs == null){
			solicitacaoAberturaTurmaDisciplinaVOs = new ArrayList<SolicitacaoAberturaTurmaDisciplinaVO>(0);
		}
		return solicitacaoAberturaTurmaDisciplinaVOs;
	}

	public void setSolicitacaoAberturaTurmaDisciplinaVOs(List<SolicitacaoAberturaTurmaDisciplinaVO> solicitacaoAberturaTurmaDisciplinaVOs) {
		this.solicitacaoAberturaTurmaDisciplinaVOs = solicitacaoAberturaTurmaDisciplinaVOs;
	}

	public List<CalendarioHorarioAulaVO<SolicitacaoAberturaTurmaDisciplinaVO>> getCalendarioHorarioAulaVOs() {
		if(calendarioHorarioAulaVOs == null){
			calendarioHorarioAulaVOs =  new ArrayList<CalendarioHorarioAulaVO<SolicitacaoAberturaTurmaDisciplinaVO>>(0);
		}
		return calendarioHorarioAulaVOs;
	}

	public void setCalendarioHorarioAulaVOs(List<CalendarioHorarioAulaVO<SolicitacaoAberturaTurmaDisciplinaVO>> calendarioHorarioAulaVOs) {
		this.calendarioHorarioAulaVOs = calendarioHorarioAulaVOs;
	}

	
	

}
