package controle.administrativo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.ExpedicaoDiplomaControle;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FuncionarioGrupoDestinatariosVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa; @Controller("GrupoDestinatariosControle")
@Scope("viewScope")
@Lazy
public class GrupoDestinatariosControle extends SuperControle implements Serializable {

	private GrupoDestinatariosVO grupoDestinatariosVO;
	private FuncionarioVO funcionarioVO;

	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	private List listaConsultaFuncionario;

	public GrupoDestinatariosControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
		novo();
	}

	public String novo() {        
		removerObjetoMemoria(this);
		limparCampos();
		getGrupoDestinatariosVO().setDataCadastro(new Date());
		getGrupoDestinatariosVO().setResponsavelCadastro(getUsuarioLogadoClone());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("grupoDestinatariosForm");
	}
	
	private void limparCampos() {
		setFuncionarioVO(null);
		setGrupoDestinatariosVO(null);
		setValorConsultaFuncionario(null);
		setCampoConsultaFuncionario(null);
		setListaConsultaFuncionario(null);
	}

	public String editar() {
		GrupoDestinatariosVO obj = (GrupoDestinatariosVO) context().getExternalContext().getRequestMap().get("grupoDestinatariosItem");
		try {
			obj.setNovoObj(Boolean.FALSE);
			setGrupoDestinatariosVO(getFacadeFactory().getGrupoDestinatariosFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("grupoDestinatariosForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		} finally {
			obj = null;
		}
	}

	public void gravar() {
		try {
			if (!Uteis.isAtributoPreenchido(getGrupoDestinatariosVO().getNomeGrupo())) {
				throw new ConsistirException("O campo NOME DO GRUPO deve ser informado.");
			}
			if (getGrupoDestinatariosVO().isNovoObj().booleanValue()) {
				getFacadeFactory().getGrupoDestinatariosFacade().incluir(getGrupoDestinatariosVO());
			} else {

				getFacadeFactory().getGrupoDestinatariosFacade().alterar(getGrupoDestinatariosVO());
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getGrupoDestinatariosFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeGrupo")) {
				objs = getFacadeFactory().getGrupoDestinatariosFacade().consultarPorNomeGrupo(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getGrupoDestinatariosFacade().excluir(getGrupoDestinatariosVO());
			setGrupoDestinatariosVO(null);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);		
		itens.add(new SelectItem("nomeGrupo", "Nome do Grupo"));
                itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void consultarFuncionario() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), TipoPessoa.FUNCIONARIO.getValor(), this.getUnidadeEnsinoLogado().getCodigo(),
						false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(),
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			executarMetodoControle(ExpedicaoDiplomaControle.class.getSimpleName(), "setMensagemID", "msg_dados_consultados");
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionario() {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
			setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getCodigo(), obj.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS,
					getUsuarioLogado()));
			getListaConsultaFuncionario().clear();
			setCampoConsultaFuncionario("");
			setValorConsultaFuncionario("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosFuncionario() throws Exception {
		setFuncionarioVO(null);
		getListaConsultaFuncionario().clear();
	}

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public String inicializarConsultar() {        
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("grupoDestinatariosCons");
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public List getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public void adicionarFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
		setFuncionarioVO(obj);
		FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO = new FuncionarioGrupoDestinatariosVO();
		try {
			setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(getFuncionarioVO().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			funcionarioGrupoDestinatariosVO.setFuncionario(getFuncionarioVO());

			for (FuncionarioGrupoDestinatariosVO fgdVO : getGrupoDestinatariosVO().getListaFuncionariosGrupoDestinatariosVOs()) {
				if (funcionarioGrupoDestinatariosVO.getFuncionario().getCodigo().equals(fgdVO.getFuncionario().getCodigo())) {
					throw new Exception("Esse funcionário já se encontra na lista.");
				}
			}
			getGrupoDestinatariosVO().getListaFuncionariosGrupoDestinatariosVOs().add(funcionarioGrupoDestinatariosVO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			funcionarioGrupoDestinatariosVO = null;
			setFuncionarioVO(null);
		}
	}

	public void removerFuncionario() {
		try {
			FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO = (FuncionarioGrupoDestinatariosVO) context().getExternalContext().getRequestMap().get("funcionarioGrupoDestinatariosItem");
			for (FuncionarioGrupoDestinatariosVO fgdVO : getGrupoDestinatariosVO().getListaFuncionariosGrupoDestinatariosVOs()) {
				if (funcionarioGrupoDestinatariosVO.getFuncionario().getCodigo().equals(fgdVO.getFuncionario().getCodigo())) {
					getGrupoDestinatariosVO().getListaFuncionariosGrupoDestinatariosVOs().remove(funcionarioGrupoDestinatariosVO);
					break;
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
		}
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setGrupoDestinatariosVO(GrupoDestinatariosVO grupoDestinatariosVO) {
		this.grupoDestinatariosVO = grupoDestinatariosVO;
	}

	public GrupoDestinatariosVO getGrupoDestinatariosVO() {
		if (grupoDestinatariosVO == null) {
			grupoDestinatariosVO = new GrupoDestinatariosVO();
		}
		return grupoDestinatariosVO;
	}
}