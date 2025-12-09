package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.SituacaoCriterioAvaliacaoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

@XmlRootElement(name = "criterioAvaliacaoVO")
public class CriterioAvaliacaoVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8850075555170375400L;
	private Integer codigo;
	private UnidadeEnsinoVO unidadeEnsino;
	private CursoVO curso;
	private GradeCurricularVO gradeCurricularVO;
	private String anoVigencia;
	private SituacaoCriterioAvaliacaoEnum situacao;
	private Date dataCadastro;
	private UsuarioVO usuarioCadastro;
	private Date dataAtivacao;
	private UsuarioVO usuarioAtivacao;
	private Date dataInativacao;
	private UsuarioVO usuarioInativacao;
	private List<CriterioAvaliacaoPeriodoLetivoVO> criterioAvaliacaoPeriodoLetivoVOs;
	
	//Transiente Utilizado na resposta do aluno
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private MatriculaVO matriculaVO;
	private Double notaFinal;
	private Boolean criterioAvaliacaoAlunoRespondido;
	
	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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
	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}
	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}
	public GradeCurricularVO getGradeCurricularVO() {
		if (gradeCurricularVO == null) {
			gradeCurricularVO = new GradeCurricularVO();
		}
		return gradeCurricularVO;
	}
	public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
		this.gradeCurricularVO = gradeCurricularVO;
	}
	public String getAnoVigencia() {
		if (anoVigencia == null) {
			anoVigencia = Uteis.getAnoDataAtual4Digitos();
		}
		return anoVigencia;
	}
	public void setAnoVigencia(String anoVigencia) {
		this.anoVigencia = anoVigencia;
	}
	public SituacaoCriterioAvaliacaoEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoCriterioAvaliacaoEnum.EM_CONSTRUCAO;
		}
		return situacao;
	}
	public void setSituacao(SituacaoCriterioAvaliacaoEnum situacao) {
		this.situacao = situacao;
	}
	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}
	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	public UsuarioVO getUsuarioCadastro() {
		if (usuarioCadastro == null) {
			usuarioCadastro = new UsuarioVO();
		}
		return usuarioCadastro;
	}
	public void setUsuarioCadastro(UsuarioVO usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}
	public Date getDataAtivacao() {
		
		return dataAtivacao;
	}
	public void setDataAtivacao(Date dataAtivacao) {
		this.dataAtivacao = dataAtivacao;
	}
	public UsuarioVO getUsuarioAtivacao() {
		if (usuarioAtivacao == null) {
			usuarioAtivacao = new UsuarioVO();
		}
		return usuarioAtivacao;
	}
	public void setUsuarioAtivacao(UsuarioVO usuarioAtivacao) {
		this.usuarioAtivacao = usuarioAtivacao;
	}
	public Date getDataInativacao() {
		
		
		return dataInativacao;
	}
	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}
	public UsuarioVO getUsuarioInativacao() {
		if (usuarioInativacao == null) {
			usuarioInativacao = new UsuarioVO();
		}
		return usuarioInativacao;
	}
	public void setUsuarioInativacao(UsuarioVO usuarioInativacao) {
		this.usuarioInativacao = usuarioInativacao;
	}
	public List<CriterioAvaliacaoPeriodoLetivoVO> getCriterioAvaliacaoPeriodoLetivoVOs() {
		if (criterioAvaliacaoPeriodoLetivoVOs == null) {
			criterioAvaliacaoPeriodoLetivoVOs = new ArrayList<CriterioAvaliacaoPeriodoLetivoVO>(0);
		}
		return criterioAvaliacaoPeriodoLetivoVOs;
	}
	public void setCriterioAvaliacaoPeriodoLetivoVOs(List<CriterioAvaliacaoPeriodoLetivoVO> criterioAvaliacaoPeriodoLetivoVOs) {
		this.criterioAvaliacaoPeriodoLetivoVOs = criterioAvaliacaoPeriodoLetivoVOs;
	}
	
	
	public Boolean getIsEmConstrucao(){
		return getSituacao().equals(SituacaoCriterioAvaliacaoEnum.EM_CONSTRUCAO);
	}
	public Boolean getIsAtivo(){
		return getSituacao().equals(SituacaoCriterioAvaliacaoEnum.ATIVO);
	}
	public Boolean getIsInativo(){
		return getSituacao().equals(SituacaoCriterioAvaliacaoEnum.INATIVO);
	}
	
	
	public Double getNotaFinal() {
		if (notaFinal == null) {
			notaFinal = 0.0;
		}
		return notaFinal;
	}
	public void setNotaFinal(Double notaFinal) {
		this.notaFinal = notaFinal;
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
	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}
	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}
	
	public String getOrdenarPorAluno(){
		return getMatriculaVO().getAluno().getNome();
	}
	public Boolean getCriterioAvaliacaoAlunoRespondido() {
		if (criterioAvaliacaoAlunoRespondido == null) {
			criterioAvaliacaoAlunoRespondido = false;
		}
		return criterioAvaliacaoAlunoRespondido;
	}
	public void setCriterioAvaliacaoAlunoRespondido(Boolean criterioAvaliacaoAlunoRespondido) {
		this.criterioAvaliacaoAlunoRespondido = criterioAvaliacaoAlunoRespondido;
	}
	
	public void distribuirCriterioAvaliacao(String observacao) {
		Iterator<CriterioAvaliacaoPeriodoLetivoVO> o = getCriterioAvaliacaoPeriodoLetivoVOs().iterator();
		while (o.hasNext()) {
			CriterioAvaliacaoPeriodoLetivoVO obj1 = o.next();
			obj1.setObservacao(observacao);
			obj1.distribuirListagemNotaConceito();
		}
	}

}
