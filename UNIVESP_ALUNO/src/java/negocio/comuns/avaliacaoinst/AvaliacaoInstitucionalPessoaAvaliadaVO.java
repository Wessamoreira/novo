package negocio.comuns.avaliacaoinst;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;

public class AvaliacaoInstitucionalPessoaAvaliadaVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5749035145749198281L;
	private Integer codigo;
	private PessoaVO pessoaVO;
	private AvaliacaoInstitucionalVO avaliacaoInstitucionalVO;
	
	
	
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public PessoaVO getPessoaVO() {
		if(pessoaVO == null){
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}
	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}
	public AvaliacaoInstitucionalVO getAvaliacaoInstitucionalVO() {
		if(avaliacaoInstitucionalVO == null){
			avaliacaoInstitucionalVO = new AvaliacaoInstitucionalVO();
		}
		return avaliacaoInstitucionalVO;
	}
	public void setAvaliacaoInstitucionalVO(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO) {
		this.avaliacaoInstitucionalVO = avaliacaoInstitucionalVO;
	}
	
	

}
