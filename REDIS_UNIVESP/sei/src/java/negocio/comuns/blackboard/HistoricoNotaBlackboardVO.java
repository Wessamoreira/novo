package negocio.comuns.blackboard;

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.blackboard.enumeradores.SituacaoHistoricoNotaBlackboardEnum;

import java.util.Date;

public class HistoricoNotaBlackboardVO extends SuperVO {

	/**
	 *
	 */
	private static final long serialVersionUID = 9123624657154997484L;
	private Integer codigo;
	@ExcluirJsonAnnotation
	private SalaAulaBlackboardVO salaAulaBlackboardVO;
	private String nomePessoaBlackboard;
	private String emailPessoaBlackboard;
	@ExcluirJsonAnnotation


	private SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO;
	private Double notaAnterior;
	private Double nota;
	private HistoricoVO historicoVO;
	private SituacaoHistoricoNotaBlackboardEnum situacaoHistoricoNotaBlackboardEnum;
	private String motivoDeferimentoIndeferimento;
	private String motivo;
	private UsuarioVO usuarioResponsavel;
	private Date dataDeferimentoIndeferimento;
	private Date dataDeferimentoIndeferimentoFiltro;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public SalaAulaBlackboardVO getSalaAulaBlackboardVO() {
		if (salaAulaBlackboardVO == null) {
			salaAulaBlackboardVO = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardVO;
	}

	public void setSalaAulaBlackboardVO(SalaAulaBlackboardVO salaAulaBlackboardVO) {
		this.salaAulaBlackboardVO = salaAulaBlackboardVO;
	}

	public String getNomePessoaBlackboard() {
		if (nomePessoaBlackboard == null) {
			nomePessoaBlackboard = "";
		}
		return nomePessoaBlackboard;
	}

	public void setNomePessoaBlackboard(String nomePessoaAva) {
		this.nomePessoaBlackboard = nomePessoaAva;
	}

	public String getEmailPessoaBlackboard() {
		if (emailPessoaBlackboard == null) {
			emailPessoaBlackboard = "";
		}
		return emailPessoaBlackboard;
	}

	public void setEmailPessoaBlackboard(String emailPessoaAva) {
		this.emailPessoaBlackboard = emailPessoaAva;
	}

	public SalaAulaBlackboardPessoaVO getSalaAulaBlackboardPessoaVO() {
		if (salaAulaBlackboardPessoaVO == null) {
			salaAulaBlackboardPessoaVO = new SalaAulaBlackboardPessoaVO();
		}
		return salaAulaBlackboardPessoaVO;
	}

	public void setSalaAulaBlackboardPessoaVO(SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO) {
		this.salaAulaBlackboardPessoaVO = salaAulaBlackboardPessoaVO;
	}

	public Double getNotaAnterior() {
		return (notaAnterior);
	}

	public void setNotaAnterior(Double notaAnterior) {
		this.notaAnterior = notaAnterior;
	}

	public Double getNota() {
		return (nota);
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}

	public HistoricoVO getHistoricoVO() {
		if (historicoVO == null) {
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}

	public SituacaoHistoricoNotaBlackboardEnum getSituacaoHistoricoNotaBlackboardEnum() {
		return situacaoHistoricoNotaBlackboardEnum;
	}

	public void setSituacaoHistoricoNotaBlackboardEnum(SituacaoHistoricoNotaBlackboardEnum situacaoHistoricoNotaBlackboardEnum) {
		this.situacaoHistoricoNotaBlackboardEnum = situacaoHistoricoNotaBlackboardEnum;
	}

	public String getMotivoDeferimentoIndeferimento() {
		if (motivoDeferimentoIndeferimento == null) {
			motivoDeferimentoIndeferimento = "";
		}
		return motivoDeferimentoIndeferimento;
	}

	public void setMotivoDeferimentoIndeferimento(String motivoDeferimentoIndeferimento) {
		this.motivoDeferimentoIndeferimento = motivoDeferimentoIndeferimento;
	}

	public String getMotivo() {
		if (motivo == null) {
			motivo = "";
		}
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public UsuarioVO getUsuarioResponsavel() {
		if (usuarioResponsavel == null) {
			usuarioResponsavel = new UsuarioVO();
		}
		return usuarioResponsavel;
	}

	public void setUsuarioResponsavel(UsuarioVO usuarioResponsavel) {
		this.usuarioResponsavel = usuarioResponsavel;
	}

	public Date getDataDeferimentoIndeferimento() {
		if (dataDeferimentoIndeferimento == null) {
			dataDeferimentoIndeferimento = new Date();
		}
		return dataDeferimentoIndeferimento;
	}

	public void setDataDeferimentoIndeferimento(Date dataDeferimentoIndeferimento) {
		this.dataDeferimentoIndeferimento = dataDeferimentoIndeferimento;
	}
	
	public Date getDataDeferimentoIndeferimentoFiltro() {
		return dataDeferimentoIndeferimentoFiltro;
	}

	public void setDataDeferimentoIndeferimentoFiltro(Date dataDeferimentoIndeferimentoFiltro) {
		this.dataDeferimentoIndeferimentoFiltro = dataDeferimentoIndeferimentoFiltro;
	}

}
