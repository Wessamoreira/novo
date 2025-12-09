package negocio.comuns.academico;

import negocio.comuns.academico.enumeradores.TipoContratoMatriculaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.TextoPadraoVO;

public class TurmaContratoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9080825851899643012L;
	private Integer codigo;
	private TurmaVO turmaVO;
	private TextoPadraoVO textoPadraoVO;
	private TipoContratoMatriculaEnum tipoContratoMatricula;
	private Boolean padrao;
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public TurmaVO getTurmaVO() {
		if(turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}
	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}
	public TextoPadraoVO getTextoPadraoVO() {
		if(textoPadraoVO == null) {
			textoPadraoVO = new TextoPadraoVO();  
		}
		return textoPadraoVO;
	}
	public void setTextoPadraoVO(TextoPadraoVO textoPadraoVO) {
		this.textoPadraoVO = textoPadraoVO;
	}
	
	public Boolean getPadrao() {
		if(padrao == null) {
			padrao =  false;
		}
		return padrao;
	}
	
	public void setPadrao(Boolean padrao) {
		this.padrao = padrao;
	}
	
	public TipoContratoMatriculaEnum getTipoContratoMatricula() {
		if(tipoContratoMatricula == null) {
			tipoContratoMatricula =  TipoContratoMatriculaEnum.NORMAL;
		}
		return tipoContratoMatricula;
	}
	
	public void setTipoContratoMatricula(TipoContratoMatriculaEnum tipoContratoMatricula) {
		this.tipoContratoMatricula = tipoContratoMatricula;
	}
	
	
	
	
}
