package negocio.comuns.academico;


import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;

public class RegraEmissaoUnidadeEnsinoVO extends SuperVO {

	private static final long serialVersionUID = -4895575149910710389L;
	
	private Integer codigo;
	private RegraEmissaoVO regraEmissaoVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public RegraEmissaoVO getRegraEmissaoVO() {
		if (regraEmissaoVO == null) {
			regraEmissaoVO = new RegraEmissaoVO();
		}
		return regraEmissaoVO;
	}
	public void setRegraEmissaoVO(RegraEmissaoVO regraEmissaoVO) {
		this.regraEmissaoVO = regraEmissaoVO;
	}
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}
	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
}
