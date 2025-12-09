package negocio.comuns.ead;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.InteragidoPorEnum;

/*
 * @author Victor Hugo 17/09/2014
 */
public class AtividadeDiscursivaInteracaoVO extends SuperVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String interacao;
	private Date dataInteracao;
	private InteragidoPorEnum interagidoPorEnum;
	private AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRepostaAlunoVO;
	private Boolean interacaoJaLida;
	private UsuarioVO usuarioVO;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getInteracao() {
		if (interacao == null) {
			interacao = "";
		}
		return interacao;
	}
	public void setInteracao(String interacao) {
		this.interacao = interacao;
	}
	public Date getDataInteracao() {
		if (dataInteracao == null) {
			dataInteracao = new Date();
		}
		return dataInteracao;
	}
	public void setDataInteracao(Date dataInteracao) {
		this.dataInteracao = dataInteracao;
	}

	public AtividadeDiscursivaRespostaAlunoVO getAtividadeDiscursivaRepostaAlunoVO() {
		if (atividadeDiscursivaRepostaAlunoVO == null) {
			atividadeDiscursivaRepostaAlunoVO = new AtividadeDiscursivaRespostaAlunoVO();
		}
		return atividadeDiscursivaRepostaAlunoVO;
	}
	public void setAtividadeDiscursivaRepostaAlunoVO(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRepostaAlunoVO) {
		this.atividadeDiscursivaRepostaAlunoVO = atividadeDiscursivaRepostaAlunoVO;
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
	public InteragidoPorEnum getInteragidoPorEnum() {
		if (interagidoPorEnum == null) {
			interagidoPorEnum = InteragidoPorEnum.ALUNO;
		}
		return interagidoPorEnum;
	}
	public void setInteragidoPorEnum(InteragidoPorEnum interagidoPorEnum) {
		this.interagidoPorEnum = interagidoPorEnum;
	}
	public Boolean getInteracaoJaLida() {
		if (interacaoJaLida == null) {
			interacaoJaLida = false;
		}
		return interacaoJaLida;
	}
	public void setInteracaoJaLida(Boolean interacaoJaLida) {
		this.interacaoJaLida = interacaoJaLida;
	}
}
