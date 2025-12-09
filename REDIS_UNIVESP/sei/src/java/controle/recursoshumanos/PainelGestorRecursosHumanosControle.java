package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;

@Controller("PainelGestorRecursosHumanosControle")
@Scope("request")
@Lazy
public class PainelGestorRecursosHumanosControle extends SuperControle {

	private static final long serialVersionUID = -5837322716985527719L;

	private List<Map<String, Object>> listaFuncionariosPorSecao;
	private List<Map<String, Object>> listaFuncionariosPorSituacao;
	private List<Map<String, Object>> listaFuncionariosPorFormaContratacao;

	@PostConstruct
	public void init() {
		try {
			setListaFuncionariosPorSecao(getFacadeFactory().getFuncionarioCargoFacade().consultarPorSecao());
			setListaFuncionariosPorFormaContratacao(getFacadeFactory().getFuncionarioCargoFacade().consultarPorFormaContratacao());
			setListaFuncionariosPorSituacao(getFacadeFactory().getFuncionarioCargoFacade().consultarPorSituacaoFuncionarioCargo());
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}

	public List<Map<String, Object>> getListaFuncionariosPorSecao() {
		if (listaFuncionariosPorSecao == null) {
			listaFuncionariosPorSecao = new ArrayList<>();
		}
		return listaFuncionariosPorSecao;
	}

	public void setListaFuncionariosPorSecao(List<Map<String, Object>> listaFuncionariosPorSecao) {
		this.listaFuncionariosPorSecao = listaFuncionariosPorSecao;
	}

	public List<Map<String, Object>> getListaFuncionariosPorSituacao() {
		if (listaFuncionariosPorSituacao == null) {
			listaFuncionariosPorSituacao = new ArrayList<>();
		}
		return listaFuncionariosPorSituacao;
	}

	public void setListaFuncionariosPorSituacao(List<Map<String, Object>> listaFuncionariosPorSituacao) {
		this.listaFuncionariosPorSituacao = listaFuncionariosPorSituacao;
	}

	public List<Map<String, Object>> getListaFuncionariosPorFormaContratacao() {
		if (listaFuncionariosPorFormaContratacao == null) {
			listaFuncionariosPorFormaContratacao = new ArrayList<>();
		}
		return listaFuncionariosPorFormaContratacao;
	}

	public void setListaFuncionariosPorFormaContratacao(
			List<Map<String, Object>> listaFuncionariosPorFormaContratacao) {
		this.listaFuncionariosPorFormaContratacao = listaFuncionariosPorFormaContratacao;
	}

}
