package controle.biblioteca;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas emprestimoForm.jsp
 * emprestimoCons.jsp) com as funcionalidades da classe <code>Emprestimo</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Emprestimo
 * @see EmprestimoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.BloqueioBibliotecaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("BloqueioBibliotecaControle")
@Scope("viewScope")
@Lazy
public class BloqueioBibliotecaControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5414774661154209178L;

	private BloqueioBibliotecaVO bloqueioBibliotecaVO;
	private BloqueioBibliotecaVO bloqueioBibliotecaEdicaoVO;	

	private List<SelectItem> listaSelectItemBiblioteca;

	private Boolean tipoPessoaAluno;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;

	private Boolean tipoPessoaProfessor;
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	private List<FuncionarioVO> listaConsultaProfessor;

	private Boolean tipoPessoaFuncionario;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	private Boolean existeBloqueio;
	private String valorConsultaMembroComunidade;
	private String campoConsultaMembroComunidade;
	private List<PessoaVO> listaConsultaMembroComunidade;
	
	private BibliotecaVO bibliotecaVO;
	
	private Date dataInicioDataBloqueio;
	private Date dataFimDataBloqueio;
	private Date dataInicioDataLimiteBloqueio;
	private Date dataFimDataLimiteBloqueio;
	
	private Boolean consultaDataScroller;
	
	private FuncionarioVO funcionarioVO;
	private List<FuncionarioVO> listaConsultaFuncionario;
	
	public BloqueioBibliotecaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(1);
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
	}

	public String novo() {
		removerObjetoMemoria(this);
		try {
			limparCampoAluno();
			getFacadeFactory().getBloqueioBibliotecaFacade().inicializarDadosBloqueioBibliotecaNovo(getBloqueioBibliotecaVO(), getUsuarioLogado());
			setMensagemID("msg_entre_dados", Uteis.ALERTA);		
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);			
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("bloqueioBibliotecaForm.xhtml");
	}
	
	public void persistir() {
		try {
			List<BloqueioBibliotecaVO> listaBloqueioBiblioteca = new ArrayList<BloqueioBibliotecaVO>();			
			listaBloqueioBiblioteca = getFacadeFactory().getBloqueioBibliotecaFacade().consultarPorCodigoPessoa(getBloqueioBibliotecaVO().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			for (BloqueioBibliotecaVO bloqueioBliblioteca : listaBloqueioBiblioteca) {
				if(bloqueioBliblioteca.getPessoa().getCodigo().equals(getBloqueioBibliotecaVO().getPessoa().getCodigo()) 
					&& bloqueioBliblioteca.getMotivoBloqueio().equals(getBloqueioBibliotecaVO().getMotivoBloqueio()) 
					&& ((!Uteis.isAtributoPreenchido(bloqueioBliblioteca.getDataLimiteBloqueio()) && !Uteis.isAtributoPreenchido(getBloqueioBibliotecaVO().getDataLimiteBloqueio())) || (Uteis.isAtributoPreenchido(bloqueioBliblioteca.getDataLimiteBloqueio()) && bloqueioBliblioteca.getDataLimiteBloqueio().equals(getBloqueioBibliotecaVO().getDataLimiteBloqueio())))) {
					
					throw new ConsistirException("Já existe um Bloqueio para essa pessoa, com o mesmo motivo e mesma data limite.");
				}
			}
			
			getFacadeFactory().getBloqueioBibliotecaFacade().persistir(getBloqueioBibliotecaVO(), getUsuarioLogado());
			getBloqueioBibliotecaVO().setCodigo(0);
			getBloqueioBibliotecaVO().setNovoObj(true);
			getBloqueioBibliotecaVO().setMotivoBloqueio("");
			getBloqueioBibliotecaVO().setDataLimiteBloqueio(null);
			setListaConsulta(getFacadeFactory().getBloqueioBibliotecaFacade().consultarPorCodigoPessoa(getBloqueioBibliotecaVO().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_bloqueioBiblioteca_bloqueado", Uteis.SUCESSO);					
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);			
		}
	}
	
	public void persistirEdicao() {
		try {
			List<BloqueioBibliotecaVO> listaBloqueioBiblioteca = new ArrayList<BloqueioBibliotecaVO>();			
			listaBloqueioBiblioteca = getFacadeFactory().getBloqueioBibliotecaFacade().consultarPorCodigoPessoa(getBloqueioBibliotecaVO().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			for (BloqueioBibliotecaVO bloqueioBliblioteca : listaBloqueioBiblioteca) {
				if(bloqueioBliblioteca.getPessoa().getCodigo().equals(getBloqueioBibliotecaVO().getPessoa().getCodigo()) 
					&& bloqueioBliblioteca.getMotivoBloqueio().equals(getBloqueioBibliotecaVO().getMotivoBloqueio()) 
					&& bloqueioBliblioteca.getDataLimiteBloqueio().equals(getBloqueioBibliotecaVO().getDataLimiteBloqueio())) {
					
					throw new ConsistirException("Já existe um Bloqueio para essa pessoa, com o mesmo motivo e mesma data limite bloqueio.");
				}
			}
			
			getFacadeFactory().getBloqueioBibliotecaFacade().persistir(getBloqueioBibliotecaEdicaoVO(), getUsuarioLogado());
			setListaConsulta(getFacadeFactory().getBloqueioBibliotecaFacade().consultarPorCodigoPessoa(getBloqueioBibliotecaEdicaoVO().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);						
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);			
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getBloqueioBibliotecaFacade().excluir(getBloqueioBibliotecaEdicaoVO(), getUsuarioLogado());
			consultar();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);					
		}
	}
	
	public void editar() {
		try {
			setBloqueioBibliotecaEdicaoVO((BloqueioBibliotecaVO)getRequestMap().get("bloqueio"));
			setMensagemID("msg_entre_dados", Uteis.ALERTA);						
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);			
		}
	}

	public void selecionarFiltro() throws Exception {
		setarFalseNosFiltros();
		limparCampoAluno();			
		if (getBloqueioBibliotecaVO().getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
			setTipoPessoaAluno(true);
		} else if (getBloqueioBibliotecaVO().getTipoPessoa().equals(TipoPessoa.PROFESSOR.getValor())) {
			setTipoPessoaProfessor(true);
		} else if (getBloqueioBibliotecaVO().getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
			setTipoPessoaFuncionario(true);
		}
	}

	private void setarFalseNosFiltros() {
		setTipoPessoaAluno(false);
		setTipoPessoaProfessor(false);
		setTipoPessoaFuncionario(false);
	}

	public List<SelectItem> tipoConsultaComboTipoPessoa;
	public List<SelectItem> getTipoConsultaComboTipoPessoa() {
		if(tipoConsultaComboTipoPessoa == null){
			tipoConsultaComboTipoPessoa = new ArrayList<SelectItem>(0);
			tipoConsultaComboTipoPessoa.add(new SelectItem("", ""));
			tipoConsultaComboTipoPessoa.add(new SelectItem(TipoPessoa.ALUNO.getValor(), "Aluno"));
			tipoConsultaComboTipoPessoa.add(new SelectItem(TipoPessoa.PROFESSOR.getValor(), "Professor"));
			tipoConsultaComboTipoPessoa.add(new SelectItem(TipoPessoa.FUNCIONARIO.getValor(), "Funcionário"));
			tipoConsultaComboTipoPessoa.add(new SelectItem(TipoPessoa.MEMBRO_COMUNIDADE.getValor(), "Visitante"));
		}
		return tipoConsultaComboTipoPessoa;
	}

	/**
	 * Métodos do rich:modalPanel de Aluno.
	 * */

	public void consultarAlunoPorMatricula(){
		try{
			getBloqueioBibliotecaVO().setPessoa(null);
			getListaConsulta().clear();
			getBloqueioBibliotecaVO().setMatricula(getFacadeFactory().getMatriculaFacade().consultarAlunoPorMatricula(getBloqueioBibliotecaVO().getMatricula().getMatricula(), "", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getUsuarioLogado()));
			getBloqueioBibliotecaVO().setPessoa(getBloqueioBibliotecaVO().getMatricula().getAluno());
			setListaConsulta(getFacadeFactory().getBloqueioBibliotecaFacade().consultarPorCodigoPessoa(getBloqueioBibliotecaVO().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));					
			existeBloqueio = !getListaConsulta().isEmpty();
			setMensagemID("msg_dados_consultados",Uteis.ALERTA);
		}catch(Exception e){
			limparCampoAluno();
			setMensagemDetalhada("msg_erro", e.getMessage(),Uteis.ERRO);
		}
	}
	
	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getCampoConsultaAluno().equals("nome")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs.add(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado()));
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		limparCampoAluno();
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
		setListaConsulta(getFacadeFactory().getBloqueioBibliotecaFacade().consultarPorCodigoPessoa(obj.getAluno().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		getBloqueioBibliotecaVO().setPessoa(obj.getAluno());
		getBloqueioBibliotecaVO().setMatricula(obj);
		existeBloqueio = !getListaConsulta().isEmpty();
		Uteis.liberarListaMemoria(this.getListaConsultaAluno());
		this.setValorConsultaAluno(null);
		this.setCampoConsultaAluno(null);
	}

	public void limparCampoAluno() {
		this.getBloqueioBibliotecaVO().setPessoa(new PessoaVO());
		this.getBloqueioBibliotecaVO().setMatricula(null);
		this.getBloqueioBibliotecaVO().setCodigo(0);
		this.getBloqueioBibliotecaVO().setMotivoBloqueio("");
		this.getBloqueioBibliotecaVO().setData(new Date());
		this.getBloqueioBibliotecaVO().setDataLimiteBloqueio(null);
		this.getBloqueioBibliotecaVO().setNovoObj(true);
		getListaConsulta().clear();
	}

	public List<SelectItem> tipoConsultaComboAluno;
	public List<SelectItem> getTipoConsultaComboAluno() {
		if(tipoConsultaComboAluno == null){
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matricula"));
		}
		return tipoConsultaComboAluno;
	}

	/**
	 * Métodos do rich:modalPanel de Professor.
	 * */

	public void consultarProfessor() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getCampoConsultaProfessor().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), null, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaProfessor(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaProfessor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProfessor(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarProfessor() throws Exception {
		limparCampoAluno();
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorItens");
		setListaConsulta(getFacadeFactory().getBloqueioBibliotecaFacade().consultarPorCodigoPessoa(obj.getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		getBloqueioBibliotecaVO().setPessoa(obj.getPessoa());		
		existeBloqueio = !getListaConsulta().isEmpty();
		Uteis.liberarListaMemoria(this.getListaConsultaProfessor());
		this.setValorConsultaProfessor(null);
		this.setCampoConsultaProfessor(null);
	}

	public void limparCampoProfessor() {
		limparCampoAluno();
	}

	public List<SelectItem> tipoConsultaComboProfessor;
	public List<SelectItem> getTipoConsultaComboProfessor() {
		if(tipoConsultaComboProfessor == null){
			tipoConsultaComboProfessor = new ArrayList<SelectItem>(0);
			tipoConsultaComboProfessor.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboProfessor.add(new SelectItem("matricula", "Matricula"));
		}
		return tipoConsultaComboProfessor;
	}

	/**
	 * Métodos do rich:modalPanel de Funcionário.
	 * */

	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarFuncionarioAtivoPorMatricula(getValorConsultaFuncionario(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), false);
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		setFuncionarioVO(obj);
		setListaConsulta(getFacadeFactory().getBloqueioBibliotecaFacade().consultarPorCodigoPessoa(obj.getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		getBloqueioBibliotecaVO().setPessoa(obj.getPessoa());		
		existeBloqueio = !getListaConsulta().isEmpty();
		Uteis.liberarListaMemoria(this.getListaConsultaProfessor());
		setValorConsultaFuncionario(null);
		setCampoConsultaFuncionario(null);
	}

	public void limparCampoFuncionario() {
		setFuncionarioVO(new FuncionarioVO());
		getBloqueioBibliotecaVO().setPessoa(new PessoaVO());
	}

	public List<SelectItem> tipoConsultaComboFuncionario;
	public List<SelectItem> getTipoConsultaComboFuncionario() {
		if(tipoConsultaComboFuncionario == null){
			tipoConsultaComboFuncionario = new ArrayList<SelectItem>(0);
			tipoConsultaComboFuncionario.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFuncionario.add(new SelectItem("matricula", "Matricula"));
		}
		return tipoConsultaComboFuncionario;
	}

	/**
	 * Métodos que montam a comboBox de Bibliotecas.
	 * */

	public List<SelectItem> getListaSelectItemBiblioteca() throws Exception {
		try {
			List<BibliotecaVO> listaBibliotecaVO = getFacadeFactory().getBibliotecaFacade().consultarPorUnidadeEnsino(
					getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
			
			List<SelectItem> itens = new ArrayList<SelectItem>(0);
			itens.add(new SelectItem("", ""));
			
			for(BibliotecaVO bibliotecaVO : listaBibliotecaVO) {
				itens.add(new SelectItem(bibliotecaVO.getCodigo(), bibliotecaVO.getNome()));
			}
			
			return itens;
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return new ArrayList<SelectItem>(0);
		}
	}

	/**
	 * Rotina responsável por atribui um javascript com o método de mascara para
	 * campos do tipo Data, CPF, CNPJ, etc.
	 */
	public String getMascaraConsulta() {
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> tipoConsultaCombo;
	
	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("bloqueioBibliotecaCons.xhtml");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
	}

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	public List<FuncionarioVO> getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List<FuncionarioVO> listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
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

	public void setListaSelectItemBiblioteca(List<SelectItem> listaSelectItemBiblioteca) {
		if (listaSelectItemBiblioteca == null) {
			listaSelectItemBiblioteca = new ArrayList<SelectItem>(0);
		}
		this.listaSelectItemBiblioteca = listaSelectItemBiblioteca;
	}

	public boolean getTipoPessoaAluno() {
		if (tipoPessoaAluno == null) {
			tipoPessoaAluno = false;
		}
		return tipoPessoaAluno;
	}

	public void setTipoPessoaAluno(boolean tipoPessoaAluno) {
		this.tipoPessoaAluno = tipoPessoaAluno;
	}

	public boolean getTipoPessoaProfessor() {
		if (tipoPessoaProfessor == null) {
			tipoPessoaProfessor = false;
		}
		return tipoPessoaProfessor;
	}

	public void setTipoPessoaProfessor(boolean tipoPessoaProfessor) {
		this.tipoPessoaProfessor = tipoPessoaProfessor;
	}

	public boolean getTipoPessoaFuncionario() {
		if (tipoPessoaFuncionario == null) {
			tipoPessoaFuncionario = false;
		}
		return tipoPessoaFuncionario;
	}

	public void setTipoPessoaFuncionario(boolean tipoPessoaFuncionario) {
		this.tipoPessoaFuncionario = tipoPessoaFuncionario;
	}

	public BloqueioBibliotecaVO getBloqueioBibliotecaVO() {
		if (bloqueioBibliotecaVO == null) {
			bloqueioBibliotecaVO = new BloqueioBibliotecaVO();
		}
		return bloqueioBibliotecaVO;
	}

	public void setBloqueioBibliotecaVO(BloqueioBibliotecaVO bloqueioBibliotecaVO) {
		this.bloqueioBibliotecaVO = bloqueioBibliotecaVO;
	}

	public Boolean getExisteBloqueio() {
		if (existeBloqueio == null) {
			existeBloqueio = false;
		}
		return existeBloqueio;
	}

	public void setExisteBloqueio(Boolean existeBloqueio) {
		this.existeBloqueio = existeBloqueio;
	}

	public BloqueioBibliotecaVO getBloqueioBibliotecaEdicaoVO() {
		if(bloqueioBibliotecaEdicaoVO == null){
			bloqueioBibliotecaEdicaoVO = new BloqueioBibliotecaVO();
		}
		return bloqueioBibliotecaEdicaoVO;
	}

	public void setBloqueioBibliotecaEdicaoVO(BloqueioBibliotecaVO bloqueioBibliotecaEdicaoVO) {
		this.bloqueioBibliotecaEdicaoVO = bloqueioBibliotecaEdicaoVO;
	}
	

	@PostConstruct
	public void inicializarDadosNavegacaoPagina(){
		try{
			if(context().getExternalContext().getRequestParameterMap().get("pessoa") != null && context().getExternalContext().getRequestParameterMap().get("biblioteca") != null &&  context().getExternalContext().getRequestParameterMap().get("tipoPessoa") != null){
			Integer pessoa = Integer.valueOf(context().getExternalContext().getRequestParameterMap().get("pessoa")); 
			Integer biblioteca = Integer.valueOf(context().getExternalContext().getRequestParameterMap().get("biblioteca"));
			String tipoPessoa = context().getExternalContext().getRequestParameterMap().get("tipoPessoa");
			if(Uteis.isAtributoPreenchido(pessoa) && Uteis.isAtributoPreenchido(biblioteca)){
				getBloqueioBibliotecaVO().getBiblioteca().setCodigo(biblioteca);
				getBloqueioBibliotecaVO().setTipoPessoa(tipoPessoa);
				if(context().getExternalContext().getRequestParameterMap().get("matricula") != null){
					getBloqueioBibliotecaVO().getMatricula().setMatricula(context().getExternalContext().getRequestParameterMap().get("matricula"));	
				}
				getBloqueioBibliotecaVO().setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(pessoa, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				setListaConsulta(getFacadeFactory().getBloqueioBibliotecaFacade().consultarPorCodigoPessoa(getBloqueioBibliotecaVO().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));							
				existeBloqueio = !getListaConsulta().isEmpty();
			}	
			}
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarMembroComunidade() {
		try {
			List<PessoaVO> objs = new ArrayList<PessoaVO>(0);
			if (getValorConsultaMembroComunidade().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaMembroComunidade().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorUnidadeEnsinoPorNome(getValorConsultaMembroComunidade(), 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			if (getCampoConsultaMembroComunidade().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorUnidadeEnsinoPorCPF(getValorConsultaMembroComunidade(), 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaMembroComunidade().equals("RG")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorUnidadeEnsinoPorRG(getValorConsultaMembroComunidade(), 0, "PR", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaMembroComunidade(objs);
			if (objs.isEmpty()) {
				setMensagemID("msg_erro_dadosnaoencontrados");
			} else {
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			getListaConsultaMembroComunidade().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	

	public List<PessoaVO> getListaConsultaMembroComunidade() {
		if (listaConsultaMembroComunidade == null) {
			listaConsultaMembroComunidade = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaMembroComunidade;
	}

	public void setListaConsultaMembroComunidade(List<PessoaVO> listaConsultaMembroComunidade) {
		this.listaConsultaMembroComunidade = listaConsultaMembroComunidade;
	}

	public String getValorConsultaMembroComunidade() {
		if (valorConsultaMembroComunidade == null) {
			valorConsultaMembroComunidade = "";
		}
		return valorConsultaMembroComunidade;
	}

	public void setValorConsultaMembroComunidade(String valorConsultaMembroComunidade) {
		this.valorConsultaMembroComunidade = valorConsultaMembroComunidade;
	}

	public String getCampoConsultaMembroComunidade() {
		if (campoConsultaMembroComunidade == null) {
			campoConsultaMembroComunidade = "";
		}
		return campoConsultaMembroComunidade;
	}

	public void setCampoConsultaMembroComunidade(String campoConsultaMembroComunidade) {
		this.campoConsultaMembroComunidade = campoConsultaMembroComunidade;
	}

	public List<SelectItem> tipoConsultaComboMembroComunidade;
	public List<SelectItem>  getTipoConsultaComboMembroComunidade() {
		if(tipoConsultaComboMembroComunidade == null){
			tipoConsultaComboMembroComunidade = new ArrayList<SelectItem>(0);
			tipoConsultaComboMembroComunidade.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboMembroComunidade.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboMembroComunidade.add(new SelectItem("RG", "RG"));
		}
		return tipoConsultaComboMembroComunidade;
	}
	
	public void selecionarMembroComunidade() {
		try {			
			limparCampoAluno();
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("membroComunidadeItens");			
			setListaConsulta(getFacadeFactory().getBloqueioBibliotecaFacade().consultarPorCodigoPessoa(obj.getCodigo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getBloqueioBibliotecaVO().setPessoa(obj);
			getBloqueioBibliotecaVO().setMatricula(null);
			existeBloqueio = !getListaConsulta().isEmpty();
			Uteis.liberarListaMemoria(this.getListaConsultaMembroComunidade());
			this.setValorConsultaMembroComunidade(null);
			this.setCampoConsultaMembroComunidade(null);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getMascaraConsultaMembroComunidade() {
		if (getCampoConsultaMembroComunidade().equals("CPF")) {
			return "return mascara(this.form,'formRequerente:valorConsultaRequerente','999.999.999-99',event);";
		}
		return "";
	}
	
	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		setConsultaDataScroller(true);
		consultar();
	}
	
	public Boolean getConsultaDataScroller() {
		if (consultaDataScroller == null) {
			consultaDataScroller = false;
		}
		return consultaDataScroller;
	}
	
	public void setConsultaDataScroller(Boolean consultaDataScroller) {
		this.consultaDataScroller = consultaDataScroller;
	}
	
	public String consultar() {
		try {
			getControleConsultaOtimizado().getListaConsulta().clear();
			getControleConsultaOtimizado().setLimitePorPagina(5);
			
			List<BloqueioBibliotecaVO> objs = getFacadeFactory().getBloqueioBibliotecaFacade().consultar(getFuncionarioVO().getPessoa().getCodigo(), getBibliotecaVO().getCodigo(), 
					getDataInicioDataBloqueio(), getDataFimDataBloqueio(), getDataInicioDataLimiteBloqueio(), getDataFimDataLimiteBloqueio(), Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado());
			
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getBloqueioBibliotecaFacade().consultarQuantidadeRegistros(getFuncionarioVO().getPessoa().getCodigo(),
					getBibliotecaVO().getCodigo(), getDataInicioDataBloqueio(), getDataFimDataBloqueio(), getDataInicioDataLimiteBloqueio(), getDataFimDataLimiteBloqueio(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			
			getControleConsultaOtimizado().setListaConsulta(objs);	
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
		return Uteis.getCaminhoRedirecionamentoNavegacao("bloqueioBibliotecaCons.xhtml");
	}

	public BibliotecaVO getBibliotecaVO() {
		if(bibliotecaVO == null) {
			bibliotecaVO = new BibliotecaVO();
		}
		return bibliotecaVO;
	}

	public void setBibliotecaVO(BibliotecaVO bibliotecaVO) {
		this.bibliotecaVO = bibliotecaVO;
	}

	public Date getDataInicioDataBloqueio() {
		return dataInicioDataBloqueio;
	}

	public void setDataInicioDataBloqueio(Date dataInicioDataBloqueio) {
		this.dataInicioDataBloqueio = dataInicioDataBloqueio;
	}

	public Date getDataFimDataBloqueio() {
		return dataFimDataBloqueio;
	}

	public void setDataFimDataBloqueio(Date dataFimDataBloqueio) {
		this.dataFimDataBloqueio = dataFimDataBloqueio;
	}

	public Date getDataInicioDataLimiteBloqueio() {
		return dataInicioDataLimiteBloqueio;
	}

	public void setDataInicioDataLimiteBloqueio(Date dataInicioDataLimiteBloqueio) {
		this.dataInicioDataLimiteBloqueio = dataInicioDataLimiteBloqueio;
	}

	public Date getDataFimDataLimiteBloqueio() {
		return dataFimDataLimiteBloqueio;
	}

	public void setDataFimDataLimiteBloqueio(Date dataFimDataLimiteBloqueio) {
		this.dataFimDataLimiteBloqueio = dataFimDataLimiteBloqueio;
	}

	public FuncionarioVO getFuncionarioVO() {
		if(funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<FuncionarioVO>(0);
		} 
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}
	
}