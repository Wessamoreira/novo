package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.enumeradores.SituacaoPBLEnum;
import negocio.comuns.ead.enumeradores.TipoAvaliacaoPBLEnum;

/**
 * @author Victor Hugo de Paula Costa - 30 de jun de 2016
 *
 */
public class GestaoEventoConteudoTurmaAvaliacaoPBLVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	/**
	 * @author Victor Hugo de Paula Costa - 30 de jun de 2016
	 */
	private Integer codigo;
	private GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO;
	private TipoAvaliacaoPBLEnum tipoAvaliacao;
	private PessoaVO avaliador;
	private PessoaVO avaliado;
	private Double nota;
	private NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO;
	private SituacaoPBLEnum situacao;
	private MatriculaVO matriculaVO;
	private List<SelectItem> comboBoxNotaConceito;
	private GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO;
	private GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO;
	private List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> gestaoEventoConteudoTurmaAvaliacaoPBLVOs;
	private Boolean notaLancada;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaAvaliadoVO;
	// Transient
	private String nomeRecursoEducacional;
	private String nomeAssuntoUnidadeConteudo;
	private Boolean apresentarIconeAviso;
	private Double mediaColegas;
	private String porcentagemAvaliacoesColegas;
	private String formulaResolvidaMediaFinal;
	private boolean existePendenciaAlunoAvaliaAluno;
	private boolean existePendenciaAutoAvaliacao;

	public GestaoEventoConteudoTurmaAvaliacaoPBLVO clone() throws CloneNotSupportedException {
		GestaoEventoConteudoTurmaAvaliacaoPBLVO clone = (GestaoEventoConteudoTurmaAvaliacaoPBLVO) super.clone();
		clone.setCodigo(0);
		clone.setNovoObj(true);
		clone.setGestaoEventoConteudoTurmaVO(new GestaoEventoConteudoTurmaVO());
		clone.setMatriculaPeriodoTurmaDisciplinaAvaliadoVO(new MatriculaPeriodoTurmaDisciplinaVO());
		clone.setMatriculaPeriodoTurmaDisciplinaAvaliadoVO((MatriculaPeriodoTurmaDisciplinaVO) this.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO().clone());
		return clone;
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

	public GestaoEventoConteudoTurmaVO getGestaoEventoConteudoTurmaVO() {
		if (gestaoEventoConteudoTurmaVO == null) {
			gestaoEventoConteudoTurmaVO = new GestaoEventoConteudoTurmaVO();
		}
		return gestaoEventoConteudoTurmaVO;
	}

	public void setGestaoEventoConteudoTurmaVO(GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO) {
		this.gestaoEventoConteudoTurmaVO = gestaoEventoConteudoTurmaVO;
	}

	public TipoAvaliacaoPBLEnum getTipoAvaliacao() {
		if (tipoAvaliacao == null) {
			tipoAvaliacao = TipoAvaliacaoPBLEnum.AUTO_AVALIACAO;
		}
		return tipoAvaliacao;
	}

	public void setTipoAvaliacao(TipoAvaliacaoPBLEnum tipoAvaliacao) {
		this.tipoAvaliacao = tipoAvaliacao;
	}

	public PessoaVO getAvaliador() {
		if (avaliador == null) {
			avaliador = new PessoaVO();
		}
		return avaliador;
	}

	public void setAvaliador(PessoaVO avaliador) {
		this.avaliador = avaliador;
	}

	public PessoaVO getAvaliado() {
		if (avaliado == null) {
			avaliado = new PessoaVO();
		}
		return avaliado;
	}

	public void setAvaliado(PessoaVO avaliado) {
		this.avaliado = avaliado;
	}

	public Double getNota() {
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}

	public NotaConceitoAvaliacaoPBLVO getNotaConceitoAvaliacaoPBLVO() {
		if (notaConceitoAvaliacaoPBLVO == null) {
			notaConceitoAvaliacaoPBLVO = new NotaConceitoAvaliacaoPBLVO();
		}
		return notaConceitoAvaliacaoPBLVO;
	}

	public void setNotaConceitoAvaliacaoPBLVO(NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO) {
		this.notaConceitoAvaliacaoPBLVO = notaConceitoAvaliacaoPBLVO;
	}

	public SituacaoPBLEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoPBLEnum.PENDENTE;
		}
		return situacao;
	}

	public void setSituacao(SituacaoPBLEnum situacao) {
		this.situacao = situacao;
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

	public List<SelectItem> getComboBoxNotaConceito() {
		if (comboBoxNotaConceito == null) {
			comboBoxNotaConceito = new ArrayList<SelectItem>();
		}
		return comboBoxNotaConceito;
	}

	public void setComboBoxNotaConceito(List<SelectItem> comboBoxNotaConceito) {
		this.comboBoxNotaConceito = comboBoxNotaConceito;
	}

	public List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> getGestaoEventoConteudoTurmaAvaliacaoPBLVOs() {
		if (gestaoEventoConteudoTurmaAvaliacaoPBLVOs == null) {
			gestaoEventoConteudoTurmaAvaliacaoPBLVOs = new ArrayList<GestaoEventoConteudoTurmaAvaliacaoPBLVO>();
		}
		return gestaoEventoConteudoTurmaAvaliacaoPBLVOs;
	}

	public void setGestaoEventoConteudoTurmaAvaliacaoPBLVOs(List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> gestaoEventoConteudoTurmaAvaliacaoPBLVOs) {
		this.gestaoEventoConteudoTurmaAvaliacaoPBLVOs = gestaoEventoConteudoTurmaAvaliacaoPBLVOs;
	}

	public GestaoEventoConteudoTurmaAvaliacaoPBLVO getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO() {
		if (gestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO == null) {
			gestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
		}
		return gestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO;
	}

	public void setGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO) {
		this.gestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO = gestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO;
	}

	public GestaoEventoConteudoTurmaAvaliacaoPBLVO getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO() {
		if (gestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO == null) {
			gestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
		}
		return gestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO;
	}

	public void setGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO) {
		this.gestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO = gestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO;
	}

	public String getNomeRecursoEducacional() {
		if (nomeRecursoEducacional == null) {
			nomeRecursoEducacional = "";
		}
		return nomeRecursoEducacional;
	}

	public void setNomeRecursoEducacional(String nomeRecursoEducacional) {
		this.nomeRecursoEducacional = nomeRecursoEducacional;
	}

	public String getNomeAssuntoUnidadeConteudo() {
		if (nomeAssuntoUnidadeConteudo == null) {
			nomeAssuntoUnidadeConteudo = "";
		}
		return nomeAssuntoUnidadeConteudo;
	}

	public void setNomeAssuntoUnidadeConteudo(String nomeAssuntoUnidadeConteudo) {
		this.nomeAssuntoUnidadeConteudo = nomeAssuntoUnidadeConteudo;
	}

	public Boolean getIsSituacaoPendenteOuLiberado() {
		if (getSituacao().equals(SituacaoPBLEnum.PENDENTE) || getSituacao().equals(SituacaoPBLEnum.LIBERADO)) {
			return true;
		}
		return false;
	}

	public Boolean getApresentarIconeAviso() {
		if (apresentarIconeAviso == null) {
			apresentarIconeAviso = false;
		}
		return apresentarIconeAviso;
	}

	public void setApresentarIconeAviso(Boolean apresentarIconeAviso) {
		this.apresentarIconeAviso = apresentarIconeAviso;
	}

	public Boolean getNotaLancada() {
		if (notaLancada == null) {
			notaLancada = false;
		}
		return notaLancada;
	}

	public void setNotaLancada(Boolean notaLancada) {
		this.notaLancada = notaLancada;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaAvaliadoVO() {
		if (matriculaPeriodoTurmaDisciplinaAvaliadoVO == null) {
			matriculaPeriodoTurmaDisciplinaAvaliadoVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaAvaliadoVO;
	}

	public void setMatriculaPeriodoTurmaDisciplinaAvaliadoVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaAvaliadoVO) {
		this.matriculaPeriodoTurmaDisciplinaAvaliadoVO = matriculaPeriodoTurmaDisciplinaAvaliadoVO;
	}

	public Double getMediaColegas() {
		return mediaColegas;
	}

	public void setMediaColegas(Double mediaColegas) {
		this.mediaColegas = mediaColegas;
	}

	public String getPorcentagemAvaliacoesColegas() {
		if (porcentagemAvaliacoesColegas == null) {
			porcentagemAvaliacoesColegas = "";
		}
		return porcentagemAvaliacoesColegas;
	}

	public void setPorcentagemAvaliacoesColegas(String porcentagemAvaliacoesColegas) {
		this.porcentagemAvaliacoesColegas = porcentagemAvaliacoesColegas;
	}

	public boolean isExistePendenciaAlunoAvaliaAluno() {
		return existePendenciaAlunoAvaliaAluno;
	}

	public void setExistePendenciaAlunoAvaliaAluno(boolean existePendenciaAlunoAvaliaAluno) {
		this.existePendenciaAlunoAvaliaAluno = existePendenciaAlunoAvaliaAluno;
	}

	public boolean isExistePendenciaAutoAvaliacao() {
		return existePendenciaAutoAvaliacao;
	}

	public void setExistePendenciaAutoAvaliacao(boolean existePendenciaAutoAvaliacao) {
		this.existePendenciaAutoAvaliacao = existePendenciaAutoAvaliacao;
	}

	public String getFormulaResolvidaMediaFinal() {
		if(formulaResolvidaMediaFinal == null){
			formulaResolvidaMediaFinal = "";
		}
		return formulaResolvidaMediaFinal;
	}

	public void setFormulaResolvidaMediaFinal(String formulaResolvidaMediaFinal) {
		this.formulaResolvidaMediaFinal = formulaResolvidaMediaFinal;
	}
}
