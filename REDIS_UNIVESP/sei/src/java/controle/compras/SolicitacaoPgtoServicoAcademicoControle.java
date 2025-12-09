package controle.compras;

/**
 * Classe respons?vel por implementar a intera??o entre os componentes JSF das p?ginas
 * solicitacaoPgtoServicoAcademicoForm.jsp solicitacaoPgtoServicoAcademicoCons.jsp) com as funcionalidades da classe
 * <code>SolicitacaoPgtoServicoAcademico</code>. Implemta??o da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see SolicitacaoPgtoServicoAcademico
 * @see SolicitacaoPgtoServicoAcademicoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.PrevisaoCustosVO;
import negocio.comuns.compras.SolicitacaoPgtoServicoAcademicoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis; @Controller("SolicitacaoPgtoServicoAcademicoControle")
@Scope("request")
@Lazy
public class SolicitacaoPgtoServicoAcademicoControle extends SuperControle implements Serializable {

	private SolicitacaoPgtoServicoAcademicoVO solicitacaoPgtoServicoAcademicoVO;
	private String responsavelAutorizacao_Erro;
	private String previsaoCustosCurso_Erro;
	private String pessoaPgtoServico_Erro;
	protected List listaSelectItemCentroDespesa;

	public SolicitacaoPgtoServicoAcademicoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina respons?vel por disponibilizar um novo objeto da classe <code>SolicitacaoPgtoServicoAcademico</code> para
	 * edi??o pelo usu?rio da aplica??o.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setSolicitacaoPgtoServicoAcademicoVO(new SolicitacaoPgtoServicoAcademicoVO());
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina respons?vel por disponibilizar os dados de um objeto da classe
	 * <code>SolicitacaoPgtoServicoAcademico</code> para altera??o. O objeto desta classe ? disponibilizado na session
	 * da p?gina (request) para que o JSP correspondente possa disponibiliz?-lo para edi??o.
	 */
	public String editar() {
		SolicitacaoPgtoServicoAcademicoVO obj = (SolicitacaoPgtoServicoAcademicoVO) context().getExternalContext().getRequestMap().get("solicitacaoPgtoServicoAcademico");
		obj.setNovoObj(Boolean.FALSE);
		setSolicitacaoPgtoServicoAcademicoVO(obj);
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina respons?vel por gravar no BD os dados editados de um novo objeto da classe
	 * <code>SolicitacaoPgtoServicoAcademico</code>. Caso o objeto seja novo (ainda n?o gravado no BD) ? acionado a
	 * opera??o <code>incluir()</code>. Caso contr?rio ? acionado o <code>alterar()</code>. Se houver alguma
	 * inconsist?ncia o objeto n?o ? gravado, sendo re-apresentado para o usu?rio juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (solicitacaoPgtoServicoAcademicoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().incluir(solicitacaoPgtoServicoAcademicoVO);
			} else {
				getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().alterar(solicitacaoPgtoServicoAcademicoVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP SolicitacaoPgtoServicoAcademicoCons.jsp. Define o
	 * tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("date")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().consultarPorDate(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("descricao")) {
				objs = getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("quantidadeHoras")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().consultarPorQuantidadeHoras(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorHora")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().consultarPorValorHora(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorTotal")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().consultarPorValorTotal(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataAutorizacao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().consultarPorDataAutorizacao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				objs = getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("parecerResponsavel")) {
				objs = getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().consultarPorParecerResponsavel(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
				objs = getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("descricaoPrevisaoCustos")) {
				objs = getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().consultarPorDescricaoPrevisaoCustos(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoDestinatarioPagamento")) {
				objs = getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().consultarPorTipoDestinatarioPagamento(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("identificadorCentroDespesaCentroDespesa")) {
				objs = getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().consultarPorIdentificadorCentroDespesaCentroDespesa(getControleConsulta().getValorConsulta(), true,
						Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
			definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	/**
	 * Opera??o respons?vel por processar a exclus?o um objeto da classe <code>SolicitacaoPgtoServicoAcademicoVO</code>
	 * Ap?s a exclus?o ela automaticamente aciona a rotina para uma nova inclus?o.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().excluir(solicitacaoPgtoServicoAcademicoVO);
			setSolicitacaoPgtoServicoAcademicoVO(new SolicitacaoPgtoServicoAcademicoVO());
			setMensagemID("msg_dados_excluidos");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	public void irPaginaInicial() throws Exception {
		controleConsulta.setPaginaAtual(1);
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	/*
	 * M?todo respons?vel por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>tipoDestinatarioPagamento</code>
	 */
	public List getListaSelectItemTipoDestinatarioPagamentoSolicitacaoPgtoServicoAcademico() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoDestinatarioPgtoSolicitacaoPgtoServicoAcademics = (Hashtable) Dominios.getTipoDestinatarioPgtoSolicitacaoPgtoServicoAcademic();
		Enumeration keys = tipoDestinatarioPgtoSolicitacaoPgtoServicoAcademics.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoDestinatarioPgtoSolicitacaoPgtoServicoAcademics.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * M?todo respons?vel por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>situacao</code>
	 */
	public List getListaSelectItemSituacaoSolicitacaoPgtoServicoAcademico() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoSolicitacaoPgtoServicoAcademicos = (Hashtable) Dominios.getSituacaoSolicitacaoPgtoServicoAcademico();
		Enumeration keys = situacaoSolicitacaoPgtoServicoAcademicos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoSolicitacaoPgtoServicoAcademicos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/**
	 * M?todo respons?vel por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>CentroDespesa</code>.
	 */
	public void montarListaSelectItemCentroDespesa(String prm) throws Exception {
            List resultadoConsulta = null;
            Iterator i = null;
            try {
                resultadoConsulta = consultarCentroDespesaPorIdentificadorCentroDespesa(prm);
		i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			CategoriaDespesaVO obj = (CategoriaDespesaVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorCategoriaDespesa().toString()));
		}
		setListaSelectItemCentroDespesa(objs);
            } catch (Exception e) {
                throw e;
            } finally {
                Uteis.liberarListaMemoria(resultadoConsulta);
                i = null;
            }
	}

	/**
	 * M?todo respons?vel por atualizar o ComboBox relativo ao atributo <code>CentroDespesa</code>. Buscando todos os
	 * objetos correspondentes a entidade <code>CentroDespesa</code>. Esta rotina n?o recebe par?metros para filtragem
	 * de dados, isto ? importante para a inicializa??o dos dados da tela para o acionamento por meio requisi??es Ajax.
	 */
	public void montarListaSelectItemCentroDespesa() {
		try {
			montarListaSelectItemCentroDespesa("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * M?todo respons?vel por consultar dados da entidade
	 * <code><code> e montar o atributo <code>identificadorCentroDespesa</code> Este atributo ? uma lista (
	 * <code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List consultarCentroDespesaPorIdentificadorCentroDespesa(String identificadorCentroDespesaPrm) throws Exception {
		List lista = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(identificadorCentroDespesaPrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * M?todo respons?vel por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemCentroDespesa();
	}

	/**
	 * M?todo respons?vel por processar a consulta na entidade <code>PrevisaoCustos</code> por meio de sua respectiva
	 * chave prim?ria. Esta rotina ? utilizada fundamentalmente por requisi??es Ajax, que realizam busca pela chave
	 * prim?ria da entidade montando automaticamente o resultado da consulta para apresenta??o.
	 */
	public void consultarPrevisaoCustosPorChavePrimaria() {
		try {
			Integer campoConsulta = solicitacaoPgtoServicoAcademicoVO.getPrevisaoCustosCurso().getCodigo();
			PrevisaoCustosVO previsaoCustos = getFacadeFactory().getPrevisaoCustosFacade().consultarPorChavePrimaria(campoConsulta, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			solicitacaoPgtoServicoAcademicoVO.getPrevisaoCustosCurso().setDescricao(previsaoCustos.getDescricao());
			this.setPrevisaoCustosCurso_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			solicitacaoPgtoServicoAcademicoVO.getPrevisaoCustosCurso().setDescricao("");
			solicitacaoPgtoServicoAcademicoVO.getPrevisaoCustosCurso().setCodigo(0);
			this.setPrevisaoCustosCurso_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * M?todo respons?vel por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
	 * prim?ria. Esta rotina ? utilizada fundamentalmente por requisi??es Ajax, que realizam busca pela chave prim?ria
	 * da entidade montando automaticamente o resultado da consulta para apresenta??o.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			Integer campoConsulta = solicitacaoPgtoServicoAcademicoVO.getResponsavelAutorizacao().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			solicitacaoPgtoServicoAcademicoVO.getResponsavelAutorizacao().setNome(pessoa.getNome());
			this.setResponsavelAutorizacao_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			solicitacaoPgtoServicoAcademicoVO.getResponsavelAutorizacao().setNome("");
			solicitacaoPgtoServicoAcademicoVO.getResponsavelAutorizacao().setCodigo(0);
			this.setResponsavelAutorizacao_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina respons?vel por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "C?digo"));
		itens.add(new SelectItem("date", "Date"));
		itens.add(new SelectItem("descricao", "Descri??o"));
		itens.add(new SelectItem("quantidadeHoras", "Quantidade Horas"));
		itens.add(new SelectItem("valorHora", "Valor Hora"));
		itens.add(new SelectItem("valorTotal", "Valor Total"));
		itens.add(new SelectItem("dataAutorizacao", "Data Autoriza??o"));
		itens.add(new SelectItem("nomePessoa", "Respons?vel Autoriza??o"));
		itens.add(new SelectItem("parecerResponsavel", "Parecer Respons?vel"));
		itens.add(new SelectItem("situacao", "Situa??o"));
		itens.add(new SelectItem("descricaoPrevisaoCustos", "Previs?o Custos Curso"));
		itens.add(new SelectItem("tipoDestinatarioPagamento", "Tipo Destinat?rio Pagamento"));
		itens.add(new SelectItem("identificadorCentroDespesaCentroDespesa", "Centro de Despesa"));
		return itens;
	}

	/**
	 * Rotina respons?vel por organizar a pagina??o entre as p?ginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {         removerObjetoMemoria(this);
		setPaginaAtualDeTodas("0/0");
		setListaConsulta(new ArrayList(0));
		definirVisibilidadeLinksNavegacao(0, 0);
		setMensagemID("msg_entre_prmconsulta");
		return "consultar";
	}

	public List getListaSelectItemCentroDespesa() {
		return (listaSelectItemCentroDespesa);
	}

	public void setListaSelectItemCentroDespesa(List listaSelectItemCentroDespesa) {
		this.listaSelectItemCentroDespesa = listaSelectItemCentroDespesa;
	}

	public String getPessoaPgtoServico_Erro() {
		return pessoaPgtoServico_Erro;
	}

	public void setPessoaPgtoServico_Erro(String pessoaPgtoServico_Erro) {
		this.pessoaPgtoServico_Erro = pessoaPgtoServico_Erro;
	}

	public String getPrevisaoCustosCurso_Erro() {
		return previsaoCustosCurso_Erro;
	}

	public void setPrevisaoCustosCurso_Erro(String previsaoCustosCurso_Erro) {
		this.previsaoCustosCurso_Erro = previsaoCustosCurso_Erro;
	}

	public String getResponsavelAutorizacao_Erro() {
		return responsavelAutorizacao_Erro;
	}

	public void setResponsavelAutorizacao_Erro(String responsavelAutorizacao_Erro) {
		this.responsavelAutorizacao_Erro = responsavelAutorizacao_Erro;
	}

	public SolicitacaoPgtoServicoAcademicoVO getSolicitacaoPgtoServicoAcademicoVO() {
		return solicitacaoPgtoServicoAcademicoVO;
	}

	public void setSolicitacaoPgtoServicoAcademicoVO(SolicitacaoPgtoServicoAcademicoVO solicitacaoPgtoServicoAcademicoVO) {
		this.solicitacaoPgtoServicoAcademicoVO = solicitacaoPgtoServicoAcademicoVO;
	}
}