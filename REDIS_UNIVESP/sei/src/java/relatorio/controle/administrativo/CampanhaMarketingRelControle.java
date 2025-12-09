package relatorio.controle.administrativo;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.jdbc.administrativo.CampanhaMarketingRel;

@SuppressWarnings("unchecked")
@Controller("CampanhaMarketingRelControle")
@Scope("request")
@Lazy
public class CampanhaMarketingRelControle extends SuperControleRelatorio {

	protected CampanhaMarketingRel campanhaMarketingRel;
	protected String dataInicioVinculacao_erro;
	protected String dataFimVinculacao_erro;
	protected String requisitante_erro;
	protected String dataAutorizacao_erro;
	protected String dataFimAutorizacao_erro;
	protected String situacao_erro;
	protected String tipoMidia_erro;
	protected String nomeTipoMidia_erro;
	private List ListaSelectItemSituacaoCampanhaMarketing;
	private List listaSelectItemTipoMidia;

	public CampanhaMarketingRelControle() throws Exception {
		setCampanhaMarketingRel(new CampanhaMarketingRel());
		inicializarListasSelectItemTodosComboBox();
		//obterUsuarioLogado();
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void imprimirPDF() {     
		try {
			campanhaMarketingRel.setOrdenarPor(getOpcaoOrdenacao().intValue());
			campanhaMarketingRel.setDescricaoFiltros("");
			String titulo = " Relatório de Campanha de Marketing";
			String xml = campanhaMarketingRel.emitirRelatorio( getUsuarioLogado());
			String design = campanhaMarketingRel.getDesignIReportRelatorio();
			apresentarRelatorio(campanhaMarketingRel.getIdEntidade(), xml, titulo, "", "", "PDF", "/" + campanhaMarketingRel.getIdEntidade() + "/registros", design, getUsuarioLogado().getNome(),
					campanhaMarketingRel.getDescricaoFiltros());
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {  removerObjetoMemoria(this);}
	}

	/**
	 * Método responsável por gerar o relatório de <code>ServidorRel</code> no formato HTML
	 */
	public void imprimirHTML() {
		try {
			campanhaMarketingRel.setOrdenarPor(getOpcaoOrdenacao().intValue());
			campanhaMarketingRel.setDescricaoFiltros("");
			String titulo = " Relatório de Campanha de Marketing";
			String xml = campanhaMarketingRel.emitirRelatorio(getUsuarioLogadoClone());
			String design = campanhaMarketingRel.getDesignIReportRelatorio();
			apresentarRelatorio(campanhaMarketingRel.getIdEntidade(), xml, titulo, "", "", "HTML", "/" + campanhaMarketingRel.getIdEntidade() + "/registros", design, getUsuarioLogado().getNome(),
					campanhaMarketingRel.getDescricaoFiltros());
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemOrdenacao();
		montarListaSelectItemTipoMidia();
	}

	public void montarListaSelectItemOrdenacao() {
		Vector opcoes = getCampanhaMarketingRel().getOrdenacoesRelatorio();
		Enumeration i = opcoes.elements();
		List objs = new ArrayList(0);
		int contador = 0;
		while (i.hasMoreElements()) {
			String opcao = (String) i.nextElement();
			objs.add(new SelectItem(new Integer(contador), opcao));
			contador++;
		}
		setListaSelectItemOrdenacoesRelatorio(objs);

	}

	public void montarListaSelectItemTipoMidia(String prm) throws Exception {
            List resultadoConsulta = null;
            Iterator i = null;
            try {
                resultadoConsulta = consultarTipoMidiaCaptacaoPorNomeMidia(prm);
		i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			TipoMidiaCaptacaoVO obj = (TipoMidiaCaptacaoVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNomeMidia()));
		}
		setListaSelectItemTipoMidia(objs);
            } catch (Exception e) {
                throw e;
            } finally {
                Uteis.liberarListaMemoria(resultadoConsulta);
                i = null;
            }
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>TipoMidiaCaptacao</code>. Buscando todos
	 * os objetos correspondentes a entidade <code>TipoMidiaCaptacao</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
	 * requisições Ajax.
	 */
	public void montarListaSelectItemTipoMidia() {
		try {
			montarListaSelectItemTipoMidia("");

		} catch (Exception e) {
			////System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nomeMidia</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarTipoMidiaCaptacaoPorNomeMidia(String nomeMidiaPrm) throws Exception {
		List lista = getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorNomeMidia(nomeMidiaPrm, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
		return lista;
	}

	public void consultarPorMatricula() {
		try {
			String campoConsulta = campanhaMarketingRel.getRequisitante().getMatricula();
			FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorRequisitanteMatricula(campoConsulta, this.getUnidadeEnsinoLogado().getCodigo(), false,
					Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			campanhaMarketingRel.setRequisitante(funcionario);
			funcionario = null;
			this.setRequisitante_erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			campanhaMarketingRel.getRequisitante().getPessoa().setNome("");
			campanhaMarketingRel.getRequisitante().setMatricula("");
			this.setRequisitante_erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	public CampanhaMarketingRel getCampanhaMarketingRel() {
		return campanhaMarketingRel;
	}

	public void setCampanhaMarketingRel(CampanhaMarketingRel ampanhaMarketingRel) {
		this.campanhaMarketingRel = ampanhaMarketingRel;
	}

	public String getDataAutorizacao_erro() {
		return dataAutorizacao_erro;
	}

	public void setDataAutorizacao_erro(String dataAutorizacao_erro) {
		this.dataAutorizacao_erro = dataAutorizacao_erro;
	}

	public String getDataFimAutorizacao_erro() {
		return dataFimAutorizacao_erro;
	}

	public void setDataFimAutorizacao_erro(String dataFimAutorizacao_erro) {
		this.dataFimAutorizacao_erro = dataFimAutorizacao_erro;
	}

	public String getDataFimVinculacao_erro() {
		return dataFimVinculacao_erro;
	}

	public void setDataFimVinculacao_erro(String dataFimVinculacao_erro) {
		this.dataFimVinculacao_erro = dataFimVinculacao_erro;
	}

	public String getDataInicioVinculacao_erro() {
		return dataInicioVinculacao_erro;
	}

	public void setDataInicioVinculacao_erro(String dataInicioVinculacao_erro) {
		this.dataInicioVinculacao_erro = dataInicioVinculacao_erro;
	}

	public List getListaSelectItemSituacaoCampanhaMarketing() throws Exception {
		List objs = new ArrayList(0);
		Hashtable situacaoCampanhaMarketings = (Hashtable) Dominios.getSituacaoCampanhaMarketing();
		Enumeration keys = situacaoCampanhaMarketings.keys();
		objs.add(new SelectItem("", ""));
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoCampanhaMarketings.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	public void setListaSelectItemSituacaoCampanhaMarketing(List ListaSelectItemSituacaoCampanhaMarketing) {
		this.ListaSelectItemSituacaoCampanhaMarketing = ListaSelectItemSituacaoCampanhaMarketing;
	}

	public List getListaSelectItemTipoMidia() {
		return listaSelectItemTipoMidia;
	}

	public void setListaSelectItemTipoMidia(List listaSelectItemTipoMidia) {
		this.listaSelectItemTipoMidia = listaSelectItemTipoMidia;
	}

	public String getNomeTipoMidia_erro() {
		return nomeTipoMidia_erro;
	}

	public void setNomeTipoMidia_erro(String nomeTipoMidia_erro) {
		this.nomeTipoMidia_erro = nomeTipoMidia_erro;
	}

	public String getRequisitante_erro() {
		return requisitante_erro;
	}

	public void setRequisitante_erro(String requisitante_erro) {
		this.requisitante_erro = requisitante_erro;
	}

	public String getTipoMidia_erro() {
		return tipoMidia_erro;
	}

	public void setTipoMidia_erro(String tipoMidia_erro) {
		this.tipoMidia_erro = tipoMidia_erro;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		campanhaMarketingRel = null;
		dataInicioVinculacao_erro = null;
		dataFimVinculacao_erro = null;
		requisitante_erro = null;
		dataAutorizacao_erro = null;
		dataFimAutorizacao_erro = null;
		situacao_erro = null;
		tipoMidia_erro = null;
		nomeTipoMidia_erro = null;
		Uteis.liberarListaMemoria(ListaSelectItemSituacaoCampanhaMarketing);
		Uteis.liberarListaMemoria(listaSelectItemTipoMidia);
	}
}
