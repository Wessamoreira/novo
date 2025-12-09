package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.academico.enumeradores.TipoInscricaoEnadeEnum;
import negocio.comuns.academico.enumeradores.TipoTextoEnadeEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class MatriculaEnadeVO extends SuperVO{
	
	private Integer codigo;
	private MatriculaVO matriculaVO;
//	private EnadeVO enadeVO;
	private Date dataEnade;	
	private TextoEnadeVO textoEnade;
	private TipoInscricaoEnadeEnum tipoInscricaoEnade;
	
	// transient
	private String erro;	
	private Boolean possuiErro;	
	private Boolean selecionado;
	
	
	private static final long serialVersionUID = 1L;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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
//	public EnadeVO getEnadeVO() {
//		if (enadeVO == null) {
//			enadeVO = new EnadeVO();
//		}
//		return enadeVO;
//	}
//	public void setEnadeVO(EnadeVO enadeVO) {
//		this.enadeVO = enadeVO;
//	}
	public Date getDataEnade() {
		return dataEnade;
	}
	public String getDataEnade_Apresentar() {
		return Uteis.getData(dataEnade, "dd/MM/yyyy");
	}
	public void setDataEnade(Date dataEnade) {
		this.dataEnade = dataEnade;
	}
	
	public String getFezEnade_Apresentar() {
		if (getTextoEnade().getTipoTextoEnade().equals(TipoTextoEnadeEnum.REALIZACAO)) {
			return "Sim";
		}
		return "Não";
	}
	
	public TextoEnadeVO getTextoEnade() {
		if (textoEnade == null) {
			textoEnade = new TextoEnadeVO();
		}
		return textoEnade;
	}
	public void setTextoEnade(TextoEnadeVO textoEnade) {
		this.textoEnade = textoEnade;
	}
	public TipoInscricaoEnadeEnum getTipoInscricaoEnade() {
		if(tipoInscricaoEnade == null ) {
			tipoInscricaoEnade = TipoInscricaoEnadeEnum.NENHUM;
		}
		return tipoInscricaoEnade;
	}
	public void setTipoInscricaoEnade(TipoInscricaoEnadeEnum tipoInscricaoEnade) {
		this.tipoInscricaoEnade = tipoInscricaoEnade;
	}
	
	
	public Boolean getPossuiErro() {
		if (possuiErro == null) {
			possuiErro = false;
		}
		return possuiErro;
	}
	public void setPossuiErro(Boolean possuiErro) {
		this.possuiErro = possuiErro;
	}
	
	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = true;
		}
		return selecionado;
	}
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	public String getErro() {
		if (erro == null) {
			erro = "";
		}
		return erro;
	}
	public void setErro(String erro) {
		this.erro = erro;
	}
	
	public String getTipoInscricaoEnadeApresentar(){		
		return getTipoInscricaoEnade().getDescricao();
	}
	
	
}
