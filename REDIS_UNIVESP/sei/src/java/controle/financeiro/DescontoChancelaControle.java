package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas centroReceitaForm.jsp centroReceitaCons.jsp) com as funcionalidades
 * da classe <code>CentroReceita</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see CentroReceita
 * @see CentroReceitaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("DescontoChancelaControle")
@Scope("viewScope")
@Lazy
public class DescontoChancelaControle extends SuperControle implements Serializable {

	private MatriculaVO matriculaVO;
	private TurmaVO turmaVO;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	protected List listaSelectItemTurma;
	private Set<MatriculaVO> listaMatriculasAlteradas;

	public DescontoChancelaControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
	}
	/**
	 * 
	 */
	public String gravar() {
		try {
			getFacadeFactory().getDescontoChancelaFacade().executarGravarListaMatricula(getListaMatriculasAlteradas(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}
	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * ListagemAlunosDescontosChancelaCons.jsp.
	 * 
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getTurmaVO().getIdentificadorTurma().equals("")) {
					throw new Exception(getMensagemInternalizacao("msg_DescontoChancela_identificadorTurma"));
			}else{
				objs = getFacadeFactory().getDescontoChancelaFacade().executarConsultaEscolhaDescontoChancela(getTurmaVO().getCodigo(), getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade()
						.consultarPorIdentificadorTurma(
								getValorConsultaTurma(),
								getUnidadeEnsinoLogado().getCodigo(), false,
								Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade()
						.consultarPorNomeUnidadeEnsino(getValorConsultaTurma(),
								getUnidadeEnsinoLogado().getCodigo(), false,
								Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade()
						.consultarPorNomeTurno(getValorConsultaTurma(),
								getUnidadeEnsinoLogado().getCodigo(), false,
								Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade()
						.consultarPorNomeCurso(getValorConsultaTurma(),
								getUnidadeEnsinoLogado().getCodigo(), false,
								Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);
		} catch (Exception ex) {
			setListaSelectItemTurma(new ArrayList<SelectItem>(0));
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	public void selecionar() {
		MatriculaVO matriculaVO = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		getListaMatriculasAlteradas().add(matriculaVO);
		
	}

	public void limparDadosTurma() {
		setTurmaVO(new TurmaVO());
		getListaConsultaTurma().clear();
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
		
	}
	public void limparMensagem() {
        getListaConsultaTurma().clear();
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
    }

	/*
	 * GETTERS AND SETTERS
	 */
	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	/**
	 * @return the listaSelectItemTurma
	 */
	public List getListaSelectItemTurma() {
		return listaSelectItemTurma;
	}

	/**
	 * @param listaSelectItemTurma
	 *            the listaSelectItemTurma to set
	 */
	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}
	public Set<MatriculaVO> getListaMatriculasAlteradas() {
		if(listaMatriculasAlteradas == null){
			listaMatriculasAlteradas = new HashSet<MatriculaVO>(0);
		}
		return listaMatriculasAlteradas;
	}
	public void setListaMatriculasAlteradas(List<MatriculaVO> listaMatriculasAlteradas) {
		this.listaMatriculasAlteradas = (Set<MatriculaVO>) listaMatriculasAlteradas;
	}
	
	
}
