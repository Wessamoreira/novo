package controle.sad;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas receitaDWForm.jsp
 * receitaDWCons.jsp) com as funcionalidades da classe <code>ReceitaDW</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see ReceitaDW
 * @see ReceitaDWVO
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.sad.ReceitaDWVO;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis; @Controller("ReceitaDWControle")
@Scope("request")
@Lazy
public class ReceitaDWControle extends SuperControle implements Serializable {

	private ReceitaDWVO receitaDWVO;
	protected List listaSelectItemCentroReceita;
	protected List listaSelectItemUnidadeEnsino;
	protected List listaSelectItemCurso;
	protected List listaSelectItemTurno;
	protected List listaSelectItemProcessoMatricula;
	protected List listaSelectItemProcSeletivo;
	protected List listaSelectItemAreaConhecimento;

	public ReceitaDWControle() throws Exception {
		//obterUsuarioLogado();
		novo();
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>ReceitaDW</code> para edição pelo usuário da
	 * aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setReceitaDWVO(new ReceitaDWVO());
		consultarGraficoLinhaTempo();
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	public void consultarGraficoBarra() {
		try {
			SqlRowSet dadosSql = getFacadeFactory().getReceitaDWFacade().consultaGeracaoRelatorioPizzaBarra(receitaDWVO, false, getUsuarioLogado());
			getReceitaDWVO().criarGraficoBarra(dadosSql);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {

			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void consultarGraficoLinhaTempo() {
		try {
			SqlRowSet dadosSql = getFacadeFactory().getReceitaDWFacade().consultaGeracaoRelatorioLinhaTempo(receitaDWVO, false, getUsuarioLogado());
			getReceitaDWVO().criarGraficoLinhaTempo(dadosSql);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparNivelEducacional() {
		getReceitaDWVO().setNivelEducacional("");
	}

	public void limparUnidadeEnsino() {
		getReceitaDWVO().getUnidadeEnsino().setCodigo(0);
	}

	public void limparProcessoMatricula() {
		getReceitaDWVO().getProcessoMatricula().setCodigo(0);
	}

	public void limparProcSeletivo() {
		getReceitaDWVO().getProcSeletivo().setCodigo(0);
	}

	public void limparCentroReceita() {
		getReceitaDWVO().getCentroReceita().setCodigo(0);
	}

	public void limparCurso() {
		getReceitaDWVO().getCurso().setCodigo(0);
	}

	public void limparTurno() {
		getReceitaDWVO().getTurno().setCodigo(0);
	}

	public void limparAreaConhecimento() {
		getReceitaDWVO().getAreaConhecimento().setCodigo(0);
	}

	public void limparPeriodo() {
		getReceitaDWVO().setData(null);
		getReceitaDWVO().setDataFim(null);
	}

	public void limparAno() {
		getReceitaDWVO().setAno(0);
	}

	public void limparMes() {
		getReceitaDWVO().setMes(0);
	}

	public List getListaSelectMes() {
		List lista = new ArrayList(0);
		lista.add(new SelectItem(0, ""));
		lista.add(new SelectItem(1, "Janeiro"));
		lista.add(new SelectItem(2, "Fevereiro"));
		lista.add(new SelectItem(3, "Março"));
		lista.add(new SelectItem(4, "Abril"));
		lista.add(new SelectItem(5, "Maio"));
		lista.add(new SelectItem(6, "Junho"));
		lista.add(new SelectItem(7, "Julho"));
		lista.add(new SelectItem(8, "Agosto"));
		lista.add(new SelectItem(9, "Setembro"));
		lista.add(new SelectItem(10, "Outubro"));
		lista.add(new SelectItem(11, "Novembro"));
		lista.add(new SelectItem(12, "Dezembro"));
		return lista;
	}

	public Integer getAnoAtual() {
		return Integer.parseInt(Uteis.getAnoDataAtual4Digitos());
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>nivelEducacional</code>
	 */
	public List getListaSelectItemNivelEducacionalReceitaDW() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", "--- Nível Educacional ---"));
		Hashtable nivelEducacionalCursos = (Hashtable) Dominios.getNivelEducacionalCurso();
		Enumeration keys = nivelEducacionalCursos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) nivelEducacionalCursos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>AreaConhecimento</code>.
	 */
	public void montarListaSelectItemAreaConhecimento(String prm) throws Exception {
		List resultadoConsulta = consultarAreaConhecimentoPorNome(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, "--- Área de Conhecimento ---"));
		while (i.hasNext()) {
			AreaConhecimentoVO obj = (AreaConhecimentoVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
		}
		setListaSelectItemAreaConhecimento(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>AreaConhecimento</code>. Buscando todos os
	 * objetos correspondentes a entidade <code>AreaConhecimento</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
	 * requisições Ajax.
	 */
	public void montarListaSelectItemAreaConhecimento() {
		try {
			montarListaSelectItemAreaConhecimento("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarAreaConhecimentoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>PeriodoLetivo</code>.
	 */
	public void montarListaSelectItemProcessoMatricula(String prm) throws Exception {
		List resultadoConsulta = consultarProcessoMatriculaPorNome(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, "--- Processo de Matricula ---"));
		while (i.hasNext()) {
			ProcessoMatriculaVO obj = (ProcessoMatriculaVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
		}
		setListaSelectItemProcessoMatricula(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>PeriodoLetivo</code>. Buscando todos os
	 * objetos correspondentes a entidade <code>PeriodoLetivo</code>. Esta rotina não recebe parâmetros para filtragem
	 * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemProcessoMatricula() {
		try {
			montarListaSelectItemProcessoMatricula("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarProcessoMatriculaPorNome(String descricaoPrm) throws Exception {
		List lista = getFacadeFactory().getProcessoMatriculaFacade().consultarPorDescricao(descricaoPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>PeriodoLetivo</code>.
	 */
	public void montarListaSelectItemProcSeletivo(String prm) throws Exception {
		List resultadoConsulta = consultarProcSeletivoPorNome(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, "--- Processo de Seletivo ---"));
		while (i.hasNext()) {
			ProcSeletivoVO obj = (ProcSeletivoVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
		}
		setListaSelectItemProcSeletivo(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>PeriodoLetivo</code>. Buscando todos os
	 * objetos correspondentes a entidade <code>PeriodoLetivo</code>. Esta rotina não recebe parâmetros para filtragem
	 * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemProcSeletivo() {
		try {
			montarListaSelectItemProcSeletivo("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarProcSeletivoPorNome(String descricaoPrm) throws Exception {
		List lista = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(descricaoPrm,  getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>Turno</code>.
	 */
	public void montarListaSelectItemTurno(String prm) throws Exception {
		List resultadoConsulta = consultarTurnoPorNome(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, "--- Turno ---"));
		while (i.hasNext()) {
			TurnoVO obj = (TurnoVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
		}
		setListaSelectItemTurno(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>Turno</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>Turno</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
	 * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemTurno() {
		try {
			montarListaSelectItemTurno("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarTurnoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getTurnoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>Curso</code>.
	 */
	public void montarListaSelectItemCurso(String prm) throws Exception {
		List resultadoConsulta = consultarCursoPorNome(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, "--- Curso ---"));
		while (i.hasNext()) {
			CursoVO obj = (CursoVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
		}
		setListaSelectItemCurso(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>Curso</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>Curso</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
	 * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemCurso() {
		try {
			montarListaSelectItemCurso("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarCursoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getCursoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>UnidadeEnsino</code>.
	 */
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, "--- Unidade Ensino ---"));
		while (i.hasNext()) {
			UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
		}
		setListaSelectItemUnidadeEnsino(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os
	 * objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem
	 * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>CentroReceita</code>.
	 */
	public void montarListaSelectItemCentroReceita(String prm) throws Exception {
		List resultadoConsulta = consultarCentroReceitaPorIdentificadorCentroReceita(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, "--- Centro Receita ---"));
		while (i.hasNext()) {
			CentroReceitaVO obj = (CentroReceitaVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorCentroReceita().toString()));
		}
		setListaSelectItemCentroReceita(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>CentroReceita</code>. Buscando todos os
	 * objetos correspondentes a entidade <code>CentroReceita</code>. Esta rotina não recebe parâmetros para filtragem
	 * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemCentroReceita() {
		try {
			montarListaSelectItemCentroReceita("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>identificadorCentroReceita</code> Este atributo é uma lista (
	 * <code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List consultarCentroReceitaPorIdentificadorCentroReceita(String identificadorCentroReceitaPrm) throws Exception {
		List lista = getFacadeFactory().getCentroReceitaFacade().consultarPorIdentificadorCentroReceita(identificadorCentroReceitaPrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemCentroReceita();
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemCurso();
		montarListaSelectItemTurno();
		montarListaSelectItemProcSeletivo();
		montarListaSelectItemProcessoMatricula();
		montarListaSelectItemAreaConhecimento();
	}

	public List getListaSelectItemAreaConhecimento() {
		return (listaSelectItemAreaConhecimento);
	}

	public void setListaSelectItemAreaConhecimento(List listaSelectItemAreaConhecimento) {
		this.listaSelectItemAreaConhecimento = listaSelectItemAreaConhecimento;
	}

	public List getListaSelectItemProcSeletivo() {
		return listaSelectItemProcSeletivo;
	}

	public void setListaSelectItemProcSeletivo(List listaSelectItemProcSeletivo) {
		this.listaSelectItemProcSeletivo = listaSelectItemProcSeletivo;
	}

	public List getListaSelectItemProcessoMatricula() {
		return listaSelectItemProcessoMatricula;
	}

	public void setListaSelectItemProcessoMatricula(List listaSelectItemProcessoMatricula) {
		this.listaSelectItemProcessoMatricula = listaSelectItemProcessoMatricula;
	}

	public List getListaSelectItemTurno() {
		return (listaSelectItemTurno);
	}

	public void setListaSelectItemTurno(List listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	public List getListaSelectItemCurso() {
		return (listaSelectItemCurso);
	}

	public void setListaSelectItemCurso(List listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	public List getListaSelectItemUnidadeEnsino() {
		return (listaSelectItemUnidadeEnsino);
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List getListaSelectItemCentroReceita() {
		return (listaSelectItemCentroReceita);
	}

	public void setListaSelectItemCentroReceita(List listaSelectItemCentroReceita) {
		this.listaSelectItemCentroReceita = listaSelectItemCentroReceita;
	}

	public ReceitaDWVO getReceitaDWVO() {
		return receitaDWVO;
	}

	public void setReceitaDWVO(ReceitaDWVO receitaDWVO) {
		this.receitaDWVO = receitaDWVO;
	}
}