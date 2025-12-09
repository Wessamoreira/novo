package controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.EixoCursoVO;
import negocio.comuns.utilitarias.Uteis;

@Controller("EixoCursoControle")
@Scope("viewScope")
public class EixoCursoControle extends SuperControle  {

	private static final long serialVersionUID = 1L;
	
	private static EixoCursoVO eixoCursoVO;
	private static List<SelectItem> tipoConsultaCombo;
	
	
	
	public String novo() throws Exception{
		removerObjetoMemoria(this);
		setEixoCursoVO(new EixoCursoVO());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("eixoCursoForm");
	}
	
	public void gravar() throws Exception {
		try {
			getFacadeFactory().getEixoCursoFacade().persistir(getEixoCursoVO()/*, getUsuarioLogado()*/);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String consultar() {
		try {
			super.consultar();
			List<EixoCursoVO> objs = new ArrayList<>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				if (getControleConsulta().getValorConsulta().trim() != null || !getControleConsulta().getValorConsulta().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta().trim());
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEixoCursoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
				if(!Uteis.isAtributoPreenchido(objs)) {
					throw new Exception("Nenhum resultado encontrado para: " + getControleConsulta().getValorConsulta());
				}
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getEixoCursoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true ,Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}
	
	public String editar() throws Exception {
		EixoCursoVO obj = (EixoCursoVO) context().getExternalContext().getRequestMap().get("eixoCursoItem");
		try {
			obj = getFacadeFactory().getEixoCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS/*, getUsuarioLogado()*/);
			obj.setNovoObj(Boolean.FALSE);
			listaConsulta.clear();
			setEixoCursoVO(obj);
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("eixoCursoForm");
	}
	
	public void excluir() {
		try {
			getFacadeFactory().getEixoCursoFacade().excluir(getEixoCursoVO());
			setEixoCursoVO(new EixoCursoVO());
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}
	
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("eixoCursoCons");
	}
	
	public EixoCursoVO getEixoCursoVO() {
		if(eixoCursoVO == null) {
			eixoCursoVO = new EixoCursoVO();
		}
		return eixoCursoVO;
	}

	public void setEixoCursoVO(EixoCursoVO eixoCursoVO) {
		EixoCursoControle.eixoCursoVO = eixoCursoVO;
	}

}
