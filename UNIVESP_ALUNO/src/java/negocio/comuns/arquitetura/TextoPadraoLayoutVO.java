package negocio.comuns.arquitetura;

import java.util.HashMap;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.enumeradores.AlinhamentoAssinaturaDigitalEnum;
import negocio.comuns.arquitetura.enumeradores.TipoDesigneTextoEnum;
import negocio.comuns.utilitarias.dominios.OrientacaoPaginaEnum;

public abstract class TextoPadraoLayoutVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -210482474358931790L;  

	public abstract Integer getCodigo();
	
	public abstract OrientacaoPaginaEnum getOrientacaoDaPaginaEnum();
	
	public abstract TipoDesigneTextoEnum getTipoDesigneTextoEnum();

	public abstract ArquivoVO getArquivoIreport();
	
	public abstract List<ArquivoVO> getListaArquivoIreport();
	
	public abstract Boolean getAssinarDigitalmenteTextoPadrao();
	
	public abstract AlinhamentoAssinaturaDigitalEnum getAlinhamentoAssinaturaDigitalEnum();
	
	public abstract String getCorAssinaturaDigitalmente();
	
	public abstract Float getLarguraAssinatura();
	
	public abstract Float getAlturaAssinatura();
	
	public abstract String getDescricao();
	
	public abstract String getMargemDireita();

	public abstract String getMargemEsquerda();

	public abstract String getMargemSuperior();

	public abstract String getMargemInferior();
	
	public abstract HashMap<String, String> getParametrosRel();
	
	public abstract String getTipo();
	
	public abstract List getObjetos();

}
