package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;

public class ForumRegistrarNotaVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2374277355693822892L;
	private Integer codigo;
	private Date dataRegistro;
	private ForumVO forumVO;
	private PessoaVO pessoaVO;
	private UsuarioVO usuarioVO;
	private HistoricoVO historicoVO;
	private ConfiguracaoAcademicoNotaConceitoVO notaConceito;
	private Double nota;
	private Double notaHistorico;
	private ConfiguracaoAcademicoNotaConceitoVO notaConceitoHistorico;
	private String variavelTipoNota;	
	/**
	 * Transient
	 */
	private Integer qtdRegistroForum;
	
	

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = new Integer(0);
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ForumVO getForumVO() {
		if (forumVO == null) {
			forumVO = new ForumVO();
		}
		return forumVO;
	}

	public void setForumVO(ForumVO forumVO) {
		this.forumVO = forumVO;
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public boolean equalsForumRegistrarNotaVO(ForumRegistrarNotaVO obj) {
		return getPessoaVO().getCodigo().equals(obj.getPessoaVO().getCodigo());
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Double getNota() {		
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}

	public UsuarioVO getUsuarioVO() {
		if (usuarioVO == null) {
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}

	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
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

	public ConfiguracaoAcademicoNotaConceitoVO getNotaConceito() {
		if (notaConceito == null) {
			notaConceito = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return notaConceito;
	}

	public void setNotaConceito(ConfiguracaoAcademicoNotaConceitoVO notaConceito) {
		this.notaConceito = notaConceito;
	}
	
	public ConfiguracaoAcademicoNotaConceitoVO getNotaConceitoHistorico() {
		if (notaConceitoHistorico == null) {
			notaConceitoHistorico = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return notaConceitoHistorico;
	}

	public void setNotaConceitoHistorico(ConfiguracaoAcademicoNotaConceitoVO notaConceitoHistorico) {
		this.notaConceitoHistorico = notaConceitoHistorico;
	}

	public String getVariavelTipoNota() {
		if (variavelTipoNota == null) {
			variavelTipoNota = "";
		}
		return variavelTipoNota;
	}

	public void setVariavelTipoNota(String variavelTipoNota) {
		this.variavelTipoNota = variavelTipoNota;
	}

	public Double getNotaHistorico() {
		return notaHistorico;
	}

	public void setNotaHistorico(Double notaHistorico) {
		this.notaHistorico = notaHistorico;
	}

	public Integer getQtdRegistroForum() {
		return qtdRegistroForum;
	}

	public void setQtdRegistroForum(Integer qtdRegistroForum) {
		this.qtdRegistroForum = qtdRegistroForum;
	}

	@Override
	public String toString() {
		return "ForumRegistrarNotaVO [codigo=" + codigo + ", pessoaVO=" + pessoaVO.getCodigo() + "]";
	}

}
