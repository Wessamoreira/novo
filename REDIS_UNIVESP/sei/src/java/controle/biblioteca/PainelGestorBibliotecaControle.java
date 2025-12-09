/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.biblioteca;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.administrativo.ComunicacaoInternaControle;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoCoordenadorVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ExemplarPainelGestorBibliotecaVO;
import negocio.comuns.utilitarias.DashboardVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoDashboardEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("PainelGestorBibliotecaControle")
@Scope("session")
@Lazy
public class PainelGestorBibliotecaControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2147542972535080960L;
	private List<EmprestimoVO> emprestimoVOs;
	private List<EmprestimoVO> listaEmprestimoPorCurso;
	private List listaSelectItemUnidadeEnsino;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private UnidadeEnsinoVO unidadeEnsinoAcervoPorDisciplina;
	private List listaSelectItemUnidadeEnsinoAcervoPorDisciplina;
	private CursoVO cursoVO;
	private List listaNivelEducacional;
	private Date dataInicio;
	private Date dataFim;
	private Date dataInicioAcervoPorDisciplina;
	private Date dataFimAcervoPorDisciplina;
	private List<ExemplarPainelGestorBibliotecaVO> listaExemplarPainelGestorBiblioteca;
	private DashboardVO dashboardEmprestimo;
	
	

	public DashboardVO getDashboardEmprestimo() {
		return dashboardEmprestimo;
	}

	public void setDashboardEmprestimo(DashboardVO dashboardEmprestimo) {
		this.dashboardEmprestimo = dashboardEmprestimo;
	}

	public PainelGestorBibliotecaControle() throws Exception {
//        inicializarListasSelectItemTodosComboBox();
//        consultarAtivosEAtrasadosPorUnidadeEnsinoENivelEducacional();
//        consultarAcervoPeriodo();
	}

	@PostConstruct
	public void init() {
		inicializarDadosPainelBiblioteca();
	}
	
	public void inicializarDadosPainelBiblioteca() {
		if (getPerfilAcessoPainelEmpretimoBiblioteca() && getAplicacaoControle().getCliente().getPermitirAcessoModuloBiblioteca()) {
			if (!getLoginControle().getMapDashboards().containsKey(TipoDashboardEnum.EMPRESTIMOS_BIBLIOTECA.name())) {
				dashboardEmprestimo = new DashboardVO(TipoDashboardEnum.EMPRESTIMOS_BIBLIOTECA, false, 10, TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.ADMINISTRATIVO, getUsuarioLogado());
				getLoginControle().getMapDashboards().put(TipoDashboardEnum.EMPRESTIMOS_BIBLIOTECA.name(), dashboardEmprestimo);
			}else {
				dashboardEmprestimo = getLoginControle().getMapDashboards().get(TipoDashboardEnum.EMPRESTIMOS_BIBLIOTECA.name());
			}			
			dashboardEmprestimo.setUnidadeEnsinoVO(getUnidadeEnsinoLogadoClone());
			dashboardEmprestimo.iniciar(1l, 2, "Carregando..", true, this, "consultarEmprestimosAtivosEAtrasados");
			dashboardEmprestimo.iniciarAssincrono();
			
		} else {
			if (getLoginControle().getMapDashboards().containsKey(TipoDashboardEnum.EMPRESTIMOS_BIBLIOTECA.name())) {
				getLoginControle().getMapDashboards().remove(TipoDashboardEnum.EMPRESTIMOS_BIBLIOTECA.name());
			}
		}
	}

	public void consultarEmprestimosAtivosEAtrasados() {
		if (getPerfilAcessoPainelEmpretimoBiblioteca()) {
			try {
				setEmprestimoVOs(getFacadeFactory().getEmprestimoFacade()
						.consultarAtivosEAtrasados(getDashboardEmprestimo().getUnidadeEnsinoVO().getCodigo()));				
			} catch (Exception e) {
				
			}finally {
				getDashboardEmprestimo().incrementar();
				getDashboardEmprestimo().encerrar();
			}
		}
	}

	public void consultarAtivosEAtrasadosPorUnidadeEnsinoENivelEducacional() {
		try {
			setListaEmprestimoPorCurso(
					getFacadeFactory().getEmprestimoFacade().consultarAtivosEAtrasadosPorUnidadeEnsinoENivelEducacional(
							getUnidadeEnsinoVO().getCodigo(), getCursoVO().getNivelEducacional()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAcervoPeriodo() {
		try {
			setListaExemplarPainelGestorBiblioteca(
					getFacadeFactory().getExemplarFacade().consultarAcervoPeriodo(getDataInicio(), getDataFim()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void editarEmprestimoAtrasado() {
		removerControleMemoriaFlashTela("EmprestimoControle");
		EmprestimoVO emprestimo = (EmprestimoVO) context().getExternalContext().getRequestMap().get("emprestimoItens");
		context().getExternalContext().getSessionMap().put("EmprestimoVO", emprestimo);
		// consultarEmprestimoAtrasado(emprestimo);
	}

	public void consultarEmprestimoAtrasado(EmprestimoVO obj) {
		try {
			List lista = getFacadeFactory().getEmprestimoFacade().consultarAtrasadosPorBibliotecaSituacaoEmExecucao(
					obj.getBiblioteca().getCodigo(), getUnidadeEnsinoLogado().getCodigo(),
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			EmprestimoControle EmprestimoControle = (EmprestimoControle) context().getExternalContext().getSessionMap()
					.get("EmprestimoControle");
			if (EmprestimoControle == null) {
				EmprestimoControle = new EmprestimoControle();
			}
			EmprestimoControle.setListaConsulta(lista);
			context().getExternalContext().getSessionMap().put("EmprestimoControle", EmprestimoControle);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void editarEmprestimoAtrasadoPorCurso() {
		EmprestimoVO emprestimo = (EmprestimoVO) context().getExternalContext().getRequestMap().get("emprestimoItens");
		consultarEmprestimoAtrasadoPorCurso(emprestimo);
	}

	public void consultarEmprestimoAtrasadoPorCurso(EmprestimoVO obj) {
		try {
			List lista = getFacadeFactory().getEmprestimoFacade().consultarAtrasadosPorCursoSituacaoEmExecucao(
					obj.getNomeCurso(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			EmprestimoControle EmprestimoControle = (EmprestimoControle) context().getExternalContext().getSessionMap()
					.get("EmprestimoControle");
			if (EmprestimoControle == null) {
				EmprestimoControle = new EmprestimoControle();
			}
			EmprestimoControle.setListaConsulta(lista);
			context().getExternalContext().getSessionMap().put("EmprestimoControle", EmprestimoControle);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void realizarEnvioEmailCoordenador() {
		try {
			EmprestimoVO emprestimo = (EmprestimoVO) context().getExternalContext().getRequestMap()
					.get("emprestimoItens");
			List lista = getFacadeFactory().getCursoCoordenadorFacade().consultarPorCodigoCurso(
					emprestimo.getCodigoCurso(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			ComunicacaoInternaControle ComunicacaoInternaControle = (ComunicacaoInternaControle) context()
					.getExternalContext().getSessionMap().get("ComunicacaoInternaControle");
			if (ComunicacaoInternaControle == null) {
				ComunicacaoInternaControle = new ComunicacaoInternaControle();
			}
			ComunicacaoInternaControle.novo();
			for (CursoCoordenadorVO cursoCoordenador : (List<CursoCoordenadorVO>) lista) {
				ComunicacaoInternaControle.getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(
						ComunicacaoInternaControle.getComunicacaoInternaVO().getTipoComunicadoInterno());
				ComunicacaoInternaControle.getComunicadoInternoDestinatarioVO()
						.setComunicadoInterno(ComunicacaoInternaControle.getComunicacaoInternaVO().getCodigo());
				ComunicacaoInternaControle.getComunicadoInternoDestinatarioVO()
						.setDestinatario(cursoCoordenador.getFuncionario().getPessoa());
				ComunicacaoInternaControle.getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs()
						.add(ComunicacaoInternaControle.getComunicadoInternoDestinatarioVO());
				ComunicacaoInternaControle.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			}
			context().getExternalContext().getSessionMap().put("ComunicacaoInternaControle",
					ComunicacaoInternaControle);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemNivelEducacionalCurso();
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			getListaSelectItemUnidadeEnsino().set(0, new SelectItem(0, "Todas Unidades de Ensino"));
			setListaSelectItemUnidadeEnsinoAcervoPorDisciplina(getListaSelectItemUnidadeEnsino());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemNivelEducacionalCurso() throws Exception {
		setListaNivelEducacional(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, true));
		getListaNivelEducacional().set(0, new SelectItem(0, "Todos Níveis Educacionais"));
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
				super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void visualizarExemplares() {
		ExemplarPainelGestorBibliotecaVO obj = (ExemplarPainelGestorBibliotecaVO) context().getExternalContext()
				.getRequestMap().get("exemplarPainelGestorBiblioteca");
		String tipoSaida = context().getExternalContext().getRequestParameterMap().get("tipoSaida");
		consultarExemplares(obj, tipoSaida);
	}

	public void consultarExemplares(ExemplarPainelGestorBibliotecaVO obj, String tipoSaida) {
		try {
			List lista = getFacadeFactory().getExemplarFacade().consultarPorNomeBibliotecaETipoSaida(
					obj.getNomeBiblioteca(), tipoSaida, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			ExemplarControle ExemplarControle = (ExemplarControle) context().getExternalContext().getSessionMap()
					.get("ExemplarControle");
			if (ExemplarControle == null) {
				ExemplarControle = new ExemplarControle();
			}
			ExemplarControle.getControleConsultaOtimizado().setListaConsulta(lista);
			ExemplarControle.getControleConsultaOtimizado().setTotalRegistrosEncontrados(lista.size());
			context().getExternalContext().getSessionMap().put("ExemplarControle", ExemplarControle);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void visualizarExemplaresDefasados() {
		ExemplarPainelGestorBibliotecaVO obj = (ExemplarPainelGestorBibliotecaVO) context().getExternalContext()
				.getRequestMap().get("exemplarPainelGestorBiblioteca");
		String tipoDefasagem = context().getExternalContext().getRequestParameterMap().get("tipoDefasagem");
		consultarExemplaresDefasados(obj, tipoDefasagem);
	}

	public void consultarExemplaresDefasados(ExemplarPainelGestorBibliotecaVO obj, String tipoDefasagem) {
		try {
			List lista = getFacadeFactory().getExemplarFacade().consultarExemplaresDefasados(obj.getCodigoBiblioteca(),
					tipoDefasagem, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			ExemplarControle ExemplarControle = (ExemplarControle) context().getExternalContext().getSessionMap()
					.get("ExemplarControle");
			if (ExemplarControle == null) {
				ExemplarControle = new ExemplarControle();
			}
			ExemplarControle.getControleConsultaOtimizado().setListaConsulta(lista);
			ExemplarControle.getControleConsultaOtimizado().setTotalRegistrosEncontrados(lista.size());
			context().getExternalContext().getSessionMap().put("ExemplarControle", ExemplarControle);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public List<EmprestimoVO> getEmprestimoVOs() {
		if (emprestimoVOs == null) {
			emprestimoVOs = new ArrayList<EmprestimoVO>(0);
		}
		return emprestimoVOs;
	}

	public void setEmprestimoVOs(List<EmprestimoVO> emprestimoVOs) {
		this.emprestimoVOs = emprestimoVOs;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public void setListaNivelEducacional(List listaNivelEducacional) {
		this.listaNivelEducacional = listaNivelEducacional;
	}

	public List getListaNivelEducacional() {
		if (listaNivelEducacional == null) {
			listaNivelEducacional = new ArrayList(0);
		}
		return listaNivelEducacional;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public List<EmprestimoVO> getListaEmprestimoPorCurso() {
		if (listaEmprestimoPorCurso == null) {
			listaEmprestimoPorCurso = new ArrayList<EmprestimoVO>(0);
		}
		return listaEmprestimoPorCurso;
	}

	public void setListaEmprestimoPorCurso(List<EmprestimoVO> listaEmprestimoPorCurso) {
		this.listaEmprestimoPorCurso = listaEmprestimoPorCurso;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = Uteis.getNewDateComMesesAMenos(6);
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public List<ExemplarPainelGestorBibliotecaVO> getListaExemplarPainelGestorBiblioteca() {
		if (listaExemplarPainelGestorBiblioteca == null) {
			listaExemplarPainelGestorBiblioteca = new ArrayList<ExemplarPainelGestorBibliotecaVO>(0);
		}
		return listaExemplarPainelGestorBiblioteca;
	}

	public void setListaExemplarPainelGestorBiblioteca(
			List<ExemplarPainelGestorBibliotecaVO> listaExemplarPainelGestorBiblioteca) {
		this.listaExemplarPainelGestorBiblioteca = listaExemplarPainelGestorBiblioteca;
	}

	public List getListaSelectItemUnidadeEnsinoAcervoPorDisciplina() {
		if (listaSelectItemUnidadeEnsinoAcervoPorDisciplina == null) {
			listaSelectItemUnidadeEnsinoAcervoPorDisciplina = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsinoAcervoPorDisciplina;
	}

	public void setListaSelectItemUnidadeEnsinoAcervoPorDisciplina(
			List listaSelectItemUnidadeEnsinoAcervoPorDisciplina) {
		this.listaSelectItemUnidadeEnsinoAcervoPorDisciplina = listaSelectItemUnidadeEnsinoAcervoPorDisciplina;
	}

	public Date getDataFimAcervoPorDisciplina() {
		if (dataFimAcervoPorDisciplina == null) {
			dataFimAcervoPorDisciplina = new Date();
		}
		return dataFimAcervoPorDisciplina;
	}

	public void setDataFimAcervoPorDisciplina(Date dataFimAcervoPorDisciplina) {
		this.dataFimAcervoPorDisciplina = dataFimAcervoPorDisciplina;
	}

	public Date getDataInicioAcervoPorDisciplina() {
		if (dataInicioAcervoPorDisciplina == null) {
			dataInicioAcervoPorDisciplina = Uteis.getNewDateComMesesAMenos(6);
		}
		return dataInicioAcervoPorDisciplina;
	}

	public void setDataInicioAcervoPorDisciplina(Date dataInicioAcervoPorDisciplina) {
		this.dataInicioAcervoPorDisciplina = dataInicioAcervoPorDisciplina;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoAcervoPorDisciplina() {
		if (unidadeEnsinoAcervoPorDisciplina == null) {
			unidadeEnsinoAcervoPorDisciplina = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoAcervoPorDisciplina;
	}

	public void setUnidadeEnsinoAcervoPorDisciplina(UnidadeEnsinoVO unidadeEnsinoAcervoPorDisciplina) {
		this.unidadeEnsinoAcervoPorDisciplina = unidadeEnsinoAcervoPorDisciplina;
	}

	private Boolean perfilAcessoPainelEmpretimoBiblioteca;

	public Boolean getPerfilAcessoPainelEmpretimoBiblioteca() {
		if (perfilAcessoPainelEmpretimoBiblioteca == null) {
			try {
				ControleAcesso.consultar("PainelGestorEmprestimoBiblioteca", true, getUsuarioLogado());
				perfilAcessoPainelEmpretimoBiblioteca = true;
			} catch (Exception e) {
				perfilAcessoPainelEmpretimoBiblioteca = false;
			}
		}
		return perfilAcessoPainelEmpretimoBiblioteca;
	}
}
