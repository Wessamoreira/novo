package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;

public class ForumPessoaVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3150772177524425300L;
	private Integer codigo;
	private ForumVO forumVO;
	private PessoaVO pessoaVO;

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

	public boolean equalsForumPessoaVO(ForumPessoaVO obj) {
		return getPessoaVO().getCodigo().equals(obj.getPessoaVO().getCodigo());
	}

	@Override
	public String toString() {
		return "ForumPessoaVO [codigo=" + codigo + ", pessoaVO=" + pessoaVO.getCodigo() + "]";
	}

}
