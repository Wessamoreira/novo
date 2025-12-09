package controle.arquitetura;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.arquitetura.PesquisaPadraoAlunoVO;
import negocio.comuns.arquitetura.enumeradores.PesquisaPadraoEnum;

@Controller("PesquisaPadraoControle")
@Scope("session")
@Lazy
public class PesquisaPadraoControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PesquisaPadraoEnum pesquisaPadraoEnum;
	private String textoPesquisaPadrao;
	
	public List<PesquisaPadraoAlunoVO> autocompletePesquisaPadrao(Object texto) {
		try {
			return getFacadeFactory().getPesquisaPadraoAlunoFacade().consultarAlunoPorNomeCpfEmailResponsavelAutoComplete((String) texto, getUsuarioLogado());
		} catch (Exception e) {
			return new ArrayList<PesquisaPadraoAlunoVO>();
		}
	}

	public PesquisaPadraoEnum getPesquisaPadraoEnum() {
		if (pesquisaPadraoEnum == null) {
			pesquisaPadraoEnum = PesquisaPadraoEnum.ALUNO;
		}
		return pesquisaPadraoEnum;
	}

	public void setPesquisaPadraoEnum(PesquisaPadraoEnum pesquisaPadraoEnum) {
		this.pesquisaPadraoEnum = pesquisaPadraoEnum;
	}

	public String getPesquisaPadraoTipo() {
		return getPesquisaPadraoEnum().getName();
	}

	public String getPesquisaPadraoDescricao() {
		return getPesquisaPadraoEnum().getDescricao();
	}

	public String getPesquisaPadraoTooltip() {
		return getPesquisaPadraoEnum().getTooltip();
	}

	public List<SelectItem> getListaSelectItemPesquisa() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		for (PesquisaPadraoEnum obj : PesquisaPadraoEnum.values()) {
			lista.add(new SelectItem(obj.getName(), obj.getDescricao()));
		}
		return lista;
	}

	public String getTextoPesquisaPadrao() {
		if (textoPesquisaPadrao == null) {
			textoPesquisaPadrao = "";
		}
		return textoPesquisaPadrao;
	}

	public void setTextoPesquisaPadrao(String textoPesquisaPadrao) {
		this.textoPesquisaPadrao = textoPesquisaPadrao;
	}

}