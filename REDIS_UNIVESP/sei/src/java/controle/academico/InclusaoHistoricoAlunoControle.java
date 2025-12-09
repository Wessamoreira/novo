package controle.academico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaComHistoricoAlunoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.InclusaoHistoricoAlunoDisciplinaVO;
import negocio.comuns.academico.InclusaoHistoricoAlunoVO;
import negocio.comuns.academico.MapaEquivalenciaMatrizCurricularVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("InclusaoHistoricoAlunoControle")
@Scope("viewScope")
@Lazy
public class InclusaoHistoricoAlunoControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8833447573693802324L;
	private InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO;
	private List<MatriculaVO> listaConsultaAluno;
	private List<MatriculaVO> matriculasAlunoValidasAproveitamento;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List<SelectItem> listaSelectItemSituacaoHistorico;
	private List<SelectItem> listaSelectItemMapaEquivalenciaMatrizCurricular;
	private List<SelectItem> tipoConsultaComboAluno;
	private List<SelectItem> tipoConsultaCombo;
	private List<SelectItem> listaSelectItemGradeCurricular;
	private Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad;
	private InclusaoHistoricoAlunoDisciplinaVO inclusaoHistoricoAlunoDisciplinaVO;
	private GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistoricoAlunoVO;
	private GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO;
	
	public InclusaoHistoricoAlunoControle() {
		super();
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
	}

	public void persistir(){
		try{
			getFacadeFactory().getInclusaoHistoricoAlunoFacade().persistir(getInclusaoHistoricoAlunoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void excluir(){
		try{
			getFacadeFactory().getInclusaoHistoricoAlunoFacade().excluir(getInclusaoHistoricoAlunoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarInclusaoHistoricoAlunoDisciplinaVO(){
		setInclusaoHistoricoAlunoDisciplinaVO(new InclusaoHistoricoAlunoDisciplinaVO());
		setInclusaoHistoricoAlunoDisciplinaVO((InclusaoHistoricoAlunoDisciplinaVO) getRequestMap().get("inclusaoHistoricoAlunoDisciplinaItem"));
	}
	
	public void excluirInclusaoHistoricoAlunoDisciplinaVO(){
		try{
			getFacadeFactory().getInclusaoHistoricoAlunoDisciplinaFacade().excluir( getInclusaoHistoricoAlunoVO(), getInclusaoHistoricoAlunoDisciplinaVO() , getUsuarioLogado());
			if(inclusaoHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVOs().isEmpty()){
				novo();
			}
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void inicializarDadosHistoricoAlunoPorGradeDisciplina(){
		try{
			limparMensagem();
			setGradeDisciplinaComHistoricoAlunoVO( (GradeDisciplinaComHistoricoAlunoVO) getRequestMap().get("gradeDisciplinaItem"));
			getFacadeFactory().getInclusaoHistoricoAlunoFacade().realizarPreparacaoDadosInclusaoHistorico(getGradeDisciplinaComHistoricoAlunoVO().getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO(), 
					getGradeDisciplinaComHistoricoAlunoVO(), null, 
					getInclusaoHistoricoAlunoVO().getMatriculaVO(), 
					getInclusaoHistoricoAlunoVO().getGradeCurricular(), getMapConfAcad(), getUsuarioLogado());
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
		
	
	public void inicializarDadosHistoricoAlunoPorGrupoOptativaDisciplina(){
		try{
			limparMensagem();
			setGradeCurricularGrupoOptativaDisciplinaVO((GradeCurricularGrupoOptativaDisciplinaVO) getRequestMap().get("gradeDisciplinaItem"));
			getFacadeFactory().getInclusaoHistoricoAlunoFacade().realizarPreparacaoDadosInclusaoHistorico(getGradeCurricularGrupoOptativaDisciplinaVO().getInclusaoHistoricoAlunoDisciplinaVO().getHistoricoVO(), 
					null, getGradeCurricularGrupoOptativaDisciplinaVO(), 
					getInclusaoHistoricoAlunoVO().getMatriculaVO(), 
					getInclusaoHistoricoAlunoVO().getGradeCurricular(), getMapConfAcad(), getUsuarioLogado());
			
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void adicionarDisciplinaHistoricoAlunoPorGradeDisciplina(){
		try{
			getFacadeFactory().getInclusaoHistoricoAlunoFacade().adicionarDisciplinaHistoricoAlunoPorGradeDisciplina(getInclusaoHistoricoAlunoVO(), getGradeDisciplinaComHistoricoAlunoVO(), getMapConfAcad(), getUsuarioLogado(), true);
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void adicionarDisciplinaHistoricoAlunoPorGrupoOptativaDisciplina(){
		try{
			getFacadeFactory().getInclusaoHistoricoAlunoFacade().adicionarDisciplinaHistoricoAlunoPorGrupoOptativaDisciplina(getInclusaoHistoricoAlunoVO(), getGradeCurricularGrupoOptativaDisciplinaVO(), getMapConfAcad(), getUsuarioLogado(), true);
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerDisciplinaHistoricoAlunoPorGradeDisciplina(){
		try{
			getFacadeFactory().getInclusaoHistoricoAlunoFacade().removerDisciplinaHistoricoAlunoPorGradeDisciplina(getInclusaoHistoricoAlunoVO(), getGradeDisciplinaComHistoricoAlunoVO(), (HistoricoVO)getRequestMap().get("historicoItem"));
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerDisciplinaHistoricoAlunoPorGrupoOptativaDisciplina(){
		try{
			getFacadeFactory().getInclusaoHistoricoAlunoFacade().removerDisciplinaHistoricoAlunoPorGrupoOptativaDisciplina(getInclusaoHistoricoAlunoVO(), getGradeCurricularGrupoOptativaDisciplinaVO(), (HistoricoVO)getRequestMap().get("historicoItem"));
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String consultar(){
		try{
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getInclusaoHistoricoAlunoFacade().consultar(getControleConsultaOtimizado().getCampoConsulta(), getControleConsultaOtimizado().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, true, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getInclusaoHistoricoAlunoFacade().consultarTotalRegistro(getControleConsultaOtimizado().getCampoConsulta(), getControleConsultaOtimizado().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoHistoricoAlunoCons.xhtml");
	}
	
	public String novo(){
		setInclusaoHistoricoAlunoVO(new InclusaoHistoricoAlunoVO());
		getInclusaoHistoricoAlunoVO().getResponsavelInclusao().setCodigo(getUsuarioLogado().getCodigo());
		getInclusaoHistoricoAlunoVO().getResponsavelInclusao().setNome(getUsuarioLogado().getNome());
		getListaSelectItemGradeCurricular().clear();
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoHistoricoAlunoForm.xhtml");
	}
	
	public String editar(){
		try{
			setInclusaoHistoricoAlunoVO(getFacadeFactory().getInclusaoHistoricoAlunoFacade().consultarPorChavePrimaria(((InclusaoHistoricoAlunoVO) getRequestMap().get("inclusaoHistoricoAlunoItem")).getCodigo(), NivelMontarDados.TODOS, true, getUsuarioLogado()));
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoHistoricoAlunoForm.xhtml");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoHistoricoAlunoCons.xhtml");
		}
	}
	
	public void paginarConsulta(DataScrollEvent dataScrollerEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
        getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());        
        consultar();
    }

	
	public String inicializarConsultar(){
		limparMensagem();
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoHistoricoAlunoCons.xhtml");
	}
	
	public String getAbrirModalHistoricoNaoAproveitado(){
		if(!getInclusaoHistoricoAlunoVO().getHistoricoNaoAproveitadoVOs().isEmpty() || !getInclusaoHistoricoAlunoVO().getHistoricoAproveitadoVOs().isEmpty() || !getInclusaoHistoricoAlunoVO().getHistoricoJaAdicionadoVOs().isEmpty()){
			return "RichFaces.$('panelNaoAproveitadasEntreMatriculas').show();";
		}
		return "";
	}
	
	public void realizarInclusaoDisciplinaApartirOutraMatricula(){
		try{
			limparMensagem();
			getInclusaoHistoricoAlunoVO().setMatriculaAproveitarDisciplinaVO((MatriculaVO)getRequestMap().get("matriculaAlunoOutrosCursoAproveitamento"));
			getFacadeFactory().getInclusaoHistoricoAlunoFacade().realizarInclusaoDisciplinaOutraMatriculaAluno(getInclusaoHistoricoAlunoVO(), getMapConfAcad(), getUsuarioLogado());
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void inicializarMatriculasAlunoOutrosCursosAproveitamentoDisciplina() {
		try {			
			getMatriculasAlunoValidasAproveitamento().clear();
			@SuppressWarnings("unchecked")
			List<MatriculaVO> matriculasPessoa = getFacadeFactory().getMatriculaFacade().consultaRapidaPorCodigoPessoaCurso(getInclusaoHistoricoAlunoVO().getMatriculaVO().getAluno().getCodigo(), null, null, false, getUsuarioLogado());
			for (MatriculaVO matriculaPessoa : matriculasPessoa) {
				if (!getInclusaoHistoricoAlunoVO().getMatriculaVO().getMatricula().equals(matriculaPessoa.getMatricula()) 
						&& getInclusaoHistoricoAlunoVO().getMatriculaVO().getCurso().getPeriodicidade().equals(matriculaPessoa.getCurso().getPeriodicidade())) {
					if(Uteis.isAtributoPreenchido(matriculaPessoa.getGradeCurricularAtual().getCodigo())){
						matriculaPessoa.setGradeCurricularAtual(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(matriculaPessoa.getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
					}
					getMatriculasAlunoValidasAproveitamento().add(matriculaPessoa);
				}
			}
			for(SelectItem itemMatriz : getListaSelectItemGradeCurricular()){
				if(Uteis.isAtributoPreenchido((Integer)itemMatriz.getValue()) && !getInclusaoHistoricoAlunoVO().getGradeCurricular().getCodigo().equals((Integer)itemMatriz.getValue())){
					MatriculaVO matriculaVO = (MatriculaVO)getInclusaoHistoricoAlunoVO().getMatriculaVO().clone();
					matriculaVO.setGradeCurricularAtual(new GradeCurricularVO());
					matriculaVO.getGradeCurricularAtual().setCodigo((Integer)itemMatriz.getValue());
					matriculaVO.getGradeCurricularAtual().setNome(itemMatriz.getLabel());
					getMatriculasAlunoValidasAproveitamento().add(matriculaVO);
				}
			}
			if(!getMatriculasAlunoValidasAproveitamento().isEmpty()){
				montarListaSelectItemMapaEquivalenciaMatrizCurricular();
			}else{
				getListaSelectItemMapaEquivalenciaMatrizCurricular().clear();
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getMatriculasAlunoValidasAproveitamento().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());				
				if (!obj.getMatricula().equals("")) {

					objs.add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, "","", getUsuarioLogado());															
			}			
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private void inicializarDadosMatricula(MatriculaVO matriculaVO) throws Exception{		
		getInclusaoHistoricoAlunoVO().setMatriculaVO(matriculaVO);
		getInclusaoHistoricoAlunoVO().setGradeCurricular(matriculaVO.getGradeCurricularAtual());
		montarListaSelectItemMatrizCurricular();
		realizarMontagemPainelMatrizCurricular();
		inicializarMatriculasAlunoOutrosCursosAproveitamentoDisciplina();
		
	}
	
	public void selecionarAluno() {
		try {
			limparMensagem();
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
			obj = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(obj.getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			inicializarDadosMatricula(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Matricula</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarMatriculaPorChavePrimaria() {		
		MatriculaVO matriculaVO = new MatriculaVO();
		try {			
			matriculaVO = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getInclusaoHistoricoAlunoVO().getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			if (!matriculaVO.getMatricula().trim().isEmpty()) {				
				getFacadeFactory().getCursoFacade().carregarDados(matriculaVO.getCurso(), NivelMontarDados.BASICO, getUsuarioLogado());
				inicializarDadosMatricula(matriculaVO);				
				setMensagemID("msg_dados_consultados");
			} else {
				novo();
				setMensagemDetalhada("msg_erro", UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));				
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
			novo();
		} finally {			

		}
	}
	
	public void realizarMontagemPainelMatrizCurricular() {
		try {
			getFacadeFactory().getInclusaoHistoricoAlunoFacade().realizarMontagemMatriculaComHistoricoAlunoVO(getInclusaoHistoricoAlunoVO(), getUsuarioLogado());			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public InclusaoHistoricoAlunoVO getInclusaoHistoricoAlunoVO() {
		if (inclusaoHistoricoAlunoVO == null) {
			inclusaoHistoricoAlunoVO = new InclusaoHistoricoAlunoVO();
		}
		return inclusaoHistoricoAlunoVO;
	}

	public void setInclusaoHistoricoAlunoVO(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO) {
		this.inclusaoHistoricoAlunoVO = inclusaoHistoricoAlunoVO;
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

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "nomePessoa";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public List<SelectItem> getListaSelectItemSituacaoHistorico() {
		if (listaSelectItemSituacaoHistorico == null) {
			listaSelectItemSituacaoHistorico = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.REPROVADO.getValor(), SituacaoHistorico.REPROVADO.getDescricao()));
			listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.REPROVADO_FALTA.getValor(), SituacaoHistorico.REPROVADO_FALTA.getDescricao()));
			listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.REPROVADO_PERIODO_LETIVO.getValor(), SituacaoHistorico.REPROVADO_PERIODO_LETIVO.getDescricao()));
		}
		return listaSelectItemSituacaoHistorico;
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		if (tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
		}
		return tipoConsultaComboAluno;
	}


	public List<MatriculaVO> getMatriculasAlunoValidasAproveitamento() {
		if(matriculasAlunoValidasAproveitamento == null){
			matriculasAlunoValidasAproveitamento = new ArrayList<MatriculaVO>(0);
		}
		return matriculasAlunoValidasAproveitamento;
	}


	public void setMatriculasAlunoValidasAproveitamento(List<MatriculaVO> matriculasAlunoValidasAproveitamento) {
		this.matriculasAlunoValidasAproveitamento = matriculasAlunoValidasAproveitamento;
	}

	public Map<Integer, ConfiguracaoAcademicoVO> getMapConfAcad() {
		if(mapConfAcad == null){
			mapConfAcad = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
		}
		return mapConfAcad;
	}

	public void setMapConfAcad(Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad) {
		this.mapConfAcad = mapConfAcad;
	}

	public InclusaoHistoricoAlunoDisciplinaVO getInclusaoHistoricoAlunoDisciplinaVO() {
		if(inclusaoHistoricoAlunoDisciplinaVO == null){
			inclusaoHistoricoAlunoDisciplinaVO = new InclusaoHistoricoAlunoDisciplinaVO();
		}
		return inclusaoHistoricoAlunoDisciplinaVO;
	}

	public void setInclusaoHistoricoAlunoDisciplinaVO(
			InclusaoHistoricoAlunoDisciplinaVO inclusaoHistoricoAlunoDisciplinaVO) {
		this.inclusaoHistoricoAlunoDisciplinaVO = inclusaoHistoricoAlunoDisciplinaVO;
	}

	public List<SelectItem> getTipoConsultaCombo() {
		if(tipoConsultaCombo == null){
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("matricula", "Matricula"));
			tipoConsultaCombo.add(new SelectItem("aluno", "Aluno"));
		}
		return tipoConsultaCombo;
	}

	public void setTipoConsultaCombo(List<SelectItem> tipoConsultaCombo) {
		this.tipoConsultaCombo = tipoConsultaCombo;
	}

	public GradeDisciplinaComHistoricoAlunoVO getGradeDisciplinaComHistoricoAlunoVO() {
		if(gradeDisciplinaComHistoricoAlunoVO == null){
			gradeDisciplinaComHistoricoAlunoVO = new GradeDisciplinaComHistoricoAlunoVO();
		}
		return gradeDisciplinaComHistoricoAlunoVO;
	}

	public void setGradeDisciplinaComHistoricoAlunoVO(
			GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistoricoAlunoVO) {
		this.gradeDisciplinaComHistoricoAlunoVO = gradeDisciplinaComHistoricoAlunoVO;
	}

	public GradeCurricularGrupoOptativaDisciplinaVO getGradeCurricularGrupoOptativaDisciplinaVO() {
		if(gradeCurricularGrupoOptativaDisciplinaVO == null){
			gradeCurricularGrupoOptativaDisciplinaVO = new GradeCurricularGrupoOptativaDisciplinaVO();
		}
		return gradeCurricularGrupoOptativaDisciplinaVO;
	}

	public void setGradeCurricularGrupoOptativaDisciplinaVO(
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO) {
		this.gradeCurricularGrupoOptativaDisciplinaVO = gradeCurricularGrupoOptativaDisciplinaVO;
	}
	
	public boolean getApresentarAno(){
		return !getInclusaoHistoricoAlunoVO().getMatriculaVO().getCurso().getPeriodicidade().equals("IN");
	}
	
	public boolean getApresentarSemestre(){
		return getInclusaoHistoricoAlunoVO().getMatriculaVO().getCurso().getPeriodicidade().equals("SE");
	}
	
	/**
	 * Método responsável por executar a montagem da lista de select item da matriz curricular do curso, setando como padrão a matriz curricular atual
	 * da matrícula
	 * 
	 * @throws Exception
	 */
	public void montarListaSelectItemMatrizCurricular() throws Exception {
		try {
			List<GradeCurricularVO> gradeCurricularVOs = getFacadeFactory().getGradeCurricularFacade().consultarPorMatriculaGradeCurricularVOsVinculadaHistoricoInclusaoExclusaoDisciplina(getInclusaoHistoricoAlunoVO().getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuario());
			getListaSelectItemGradeCurricular().clear();
			for (GradeCurricularVO grade : gradeCurricularVOs) {
				getListaSelectItemGradeCurricular().add(new SelectItem(grade.getCodigo(), grade.getNome()));				
			}			
		} catch (Exception e) {
			throw e;
		}
	}

	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if(listaSelectItemGradeCurricular == null){
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}	

	public void montarListaSelectItemMapaEquivalenciaMatrizCurricular() throws Exception {
		List<MapaEquivalenciaMatrizCurricularVO> listMapaEquivalenciaVOs = getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().consultarPorCodigoGradeCurricular(getInclusaoHistoricoAlunoVO().getGradeCurricular().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
		setListaSelectItemMapaEquivalenciaMatrizCurricular(UtilSelectItem.getListaSelectItem(listMapaEquivalenciaVOs, "codigo", "descricao", true));		
	}

	public List<SelectItem> getListaSelectItemMapaEquivalenciaMatrizCurricular() {
		if(listaSelectItemMapaEquivalenciaMatrizCurricular == null){
			listaSelectItemMapaEquivalenciaMatrizCurricular= new ArrayList<SelectItem>(0);
		}
		return listaSelectItemMapaEquivalenciaMatrizCurricular;
	}

	public void setListaSelectItemMapaEquivalenciaMatrizCurricular(
			List<SelectItem> listaSelectItemMapaEquivalenciaMatrizCurricular) {
		this.listaSelectItemMapaEquivalenciaMatrizCurricular = listaSelectItemMapaEquivalenciaMatrizCurricular;
	}
	
	
}


