package controle.ead;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces. model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.ead.AnotacaoDisciplinaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;


/**
 * @author Victor Hugo 08/09/2014
 */
@Controller("AnotacaoDisciplinaControle")
@Scope("viewScope")
@Lazy
public class AnotacaoDisciplinaControle extends SuperControle {
	
	private AnotacaoDisciplinaVO anotacaoDisciplinaVO;
	private List<AnotacaoDisciplinaVO> listaAnotacaoDisciplinaVO;
	
	@PostConstruct
	public void init() {
		this.anotacaoDisciplinaVO = new AnotacaoDisciplinaVO();
		this.listaAnotacaoDisciplinaVO = new ArrayList<AnotacaoDisciplinaVO>();
	}
	
	public String novo() {
		this.init();
		return Uteis.getCaminhoRedirecionamentoNavegacao("anotacaoDisciplinaCons.xhtml");
	}
	
	public String editar() {
		AnotacaoDisciplinaVO anotacaoDisciplinaVO = (AnotacaoDisciplinaVO) context().getExternalContext()
				.getRequestMap().get("items");
		setAnotacaoDisciplinaVO(anotacaoDisciplinaVO);
		return Uteis.getCaminhoRedirecionamentoNavegacao("anotacaoDisciplinaCons.xhtml");
	}

	public String consultar() {
		try {
			setListaAnotacaoDisciplinaVO(getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().consultar(getControleConsulta().getValorConsulta(), 
					getControleConsulta().getCampoConsulta(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if(getListaAnotacaoDisciplinaVO().isEmpty()){
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("anotacaoDisciplinaCons.xhtml");
	}

	public List<SelectItem> getCamposConsulta() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);

		objs.add(new SelectItem("codigo", "Código"));
		objs.add(new SelectItem("palavrachave", "Palavra Chave"));

		return objs;
	}

	//Getters and Setters
	public AnotacaoDisciplinaVO getAnotacaoDisciplinaVO() {
		if (anotacaoDisciplinaVO == null) {
			anotacaoDisciplinaVO = new AnotacaoDisciplinaVO();
		}
		return anotacaoDisciplinaVO;
	}

	public void setAnotacaoDisciplinaVO(AnotacaoDisciplinaVO anotacaoDisciplinaVO) {
		this.anotacaoDisciplinaVO = anotacaoDisciplinaVO;
	}

	public List<AnotacaoDisciplinaVO> getListaAnotacaoDisciplinaVO() {
		if (listaAnotacaoDisciplinaVO == null) {
			listaAnotacaoDisciplinaVO = new ArrayList<AnotacaoDisciplinaVO>();
		}
		return listaAnotacaoDisciplinaVO;
	}

	public void setListaAnotacaoDisciplinaVO(List<AnotacaoDisciplinaVO> listaAnotacaoDisciplinaVO) {
		this.listaAnotacaoDisciplinaVO = listaAnotacaoDisciplinaVO;
	}
}
