package controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CriterioAvaliacaoDisciplinaEixoIndicadorVO;
import negocio.comuns.academico.CriterioAvaliacaoDisciplinaVO;
import negocio.comuns.academico.CriterioAvaliacaoIndicadorVO;
import negocio.comuns.academico.CriterioAvaliacaoNotaConceitoVO;
import negocio.comuns.academico.CriterioAvaliacaoPeriodoLetivoVO;
import negocio.comuns.academico.CriterioAvaliacaoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.NotaConceitoIndicadorAvaliacaoVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.enumeradores.AvaliarNaoAvaliarEnum;
import negocio.comuns.academico.enumeradores.SituacaoCriterioAvaliacaoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;


@Controller("CriterioAvaliacaoControle")
@Scope("viewScope")
@Lazy
public class CriterioAvaliacaoControle extends SuperControleRelatorio {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -787059989274997483L;
	private CriterioAvaliacaoVO criterioAvaliacaoVO;
	private CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO;
	private CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO;
	private CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO;
	private CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO;
	private CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVOEditar;
	private NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO;
	private CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private List<SelectItem> listaSelectItemGradeCurricular;
	private List<SelectItem> listaSelectItemPeriodoLetivo;
	private List<SelectItem> listaSelectItemNotaConceito;
	private List<SelectItem> listaSelectItemDisciplina;
	private List<SelectItem> listaSelectItemOpcaoConsultaCurso;
	private List<SelectItem> listaSelectItemAvaliarNaoAvaliar;
	private Integer periodoLetivo;
	private Integer disciplina;	
	private Boolean indicadorGeral;
	private Integer unidadeEnsino;
	private List<SelectItem> listaSelectItemOpcaoConsulta;
	
	public void persistir(){
		try {
			getFacadeFactory().getCriterioAvaliacaoFacade().persistir(getCriterioAvaliacaoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void novoNotaConceito(){
		setNotaConceitoIndicadorAvaliacaoVO(new NotaConceitoIndicadorAvaliacaoVO());
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
	}
	
	public void persistirNotaConceito(){
		try {
			getFacadeFactory().getNotaConceitoIndicadorAvaliacaoFacade().persistir(getNotaConceitoIndicadorAvaliacaoVO(), getConfiguracaoGeralPadraoSistema(), true, getUsuarioLogado());
			setListaSelectItemNotaConceito(null);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void ativar(){
		try {
			getFacadeFactory().getCriterioAvaliacaoFacade().ativar(getCriterioAvaliacaoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String novo(){
		try {
			setCriterioAvaliacaoVO(new CriterioAvaliacaoVO());
			setPeriodoLetivo(0);
			setDisciplina(0);			
			limparConsultaCurso();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoForm");
	}
	
	public void inativar(){
		try {
			getFacadeFactory().getCriterioAvaliacaoFacade().inativar(getCriterioAvaliacaoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_inativado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void excluir(){
		try {
			getFacadeFactory().getCriterioAvaliacaoFacade().excluir(getCriterioAvaliacaoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String editar(){
		try {
			setCriterioAvaliacaoVO(getFacadeFactory().getCriterioAvaliacaoFacade().consultarPorChavePrimaria(((CriterioAvaliacaoVO) getRequestMap().get("criterioAvaliacaoItem")).getCodigo(), Uteis.NIVELMONTARDADOS_TODOS));
			montarListaSelectItemGradeCurricular();
			if(getCriterioAvaliacaoVO().getSituacao().equals(SituacaoCriterioAvaliacaoEnum.EM_CONSTRUCAO)){				
				montarListaSelectItemPeriodoLetivo();
			}
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoForm");
	}
	
	public String consultar(){
		try{
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getCriterioAvaliacaoFacade().consultar(getControleConsultaOtimizado().getCampoConsulta(), getControleConsultaOtimizado().getValorConsulta(), getUnidadeEnsino(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCriterioAvaliacaoFacade().consultarTotalRegistro(getControleConsultaOtimizado().getCampoConsulta(), getControleConsultaOtimizado().getValorConsulta(), getUnidadeEnsino()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}
	
	public void paginarConsulta(DataScrollEvent DataScrollEvent){
		try{
			getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
	        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
	        consultar();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public String irPaginaConsulta(){
		setControleConsultaOtimizado(new DataModelo());
		return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoCons");
	}
	
	public void consultarCurso(){
		try {
			setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getCriterioAvaliacaoVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,  getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarCurso(){
		try{
			CursoVO curso = (CursoVO) getRequestMap().get("cursoItem");
			curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(curso.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
			getCriterioAvaliacaoVO().setCurso(curso);
			montarListaSelectItemGradeCurricular();
			setMensagemID("msg_dados_selecionados", Uteis.ALERTA);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public void limparCurso(){
		getCriterioAvaliacaoVO().setCurso(null);
		getCriterioAvaliacaoVO().setGradeCurricularVO(null);
		getListaSelectItemGradeCurricular().clear();
		getListaSelectItemDisciplina().clear();
		getListaSelectItemPeriodoLetivo().clear();
		getCriterioAvaliacaoVO().getCriterioAvaliacaoPeriodoLetivoVOs().clear();
	}
	
	public void montarListaSelectItemGradeCurricular() throws Exception{
		@SuppressWarnings("unchecked")
		List<GradeCurricularVO> gradeCurricularVOs = getFacadeFactory().getGradeCurricularFacade().consultarPorCodigoCurso(getCriterioAvaliacaoVO().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		getListaSelectItemGradeCurricular().clear();
		getListaSelectItemGradeCurricular().add(new SelectItem(0, ""));
		for(GradeCurricularVO gradeCurricularVO : gradeCurricularVOs){
			getListaSelectItemGradeCurricular().add(new SelectItem(gradeCurricularVO.getCodigo(), gradeCurricularVO.getNome()+"("+gradeCurricularVO.getSituacao_Apresentar()+")"));			
		}
	}
	
	public void montarListaSelectItemPeriodoLetivo(){
		try{					
			List<PeriodoLetivoVO> periodoLetivoVOs = getFacadeFactory().getPeriodoLetivoFacade().consultarPorGradeCurricular(getCriterioAvaliacaoVO().getGradeCurricularVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemPeriodoLetivo().clear();			
			getListaSelectItemPeriodoLetivo().add(new SelectItem(0, "Todos"));
			for(PeriodoLetivoVO periodoLetivoVO: periodoLetivoVOs){
				getListaSelectItemPeriodoLetivo().add(new SelectItem(periodoLetivoVO.getCodigo(), periodoLetivoVO.getDescricao()));
			}
			if(getCriterioAvaliacaoVO().getCriterioAvaliacaoPeriodoLetivoVOs().isEmpty()){
				adicionarCriterioAvaliacaoPeriodoLetivo();
			}
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void adicionarCriterioAvaliacaoPeriodoLetivo(){
		try {
			if(getPeriodoLetivo() > 0){
				getFacadeFactory().getCriterioAvaliacaoFacade().adicionarCriterioAvaliacaoPeriodoLetivo(getCriterioAvaliacaoVO(), getPeriodoLetivo(), getUsuarioLogado());
			}else{
				getFacadeFactory().getCriterioAvaliacaoFacade().adicionarTodosCriterioAvaliacaoPeriodoLetivo(getCriterioAvaliacaoVO(), getUsuarioLogado());
			}
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void excluirCriterioAvaliacaoPeriodoLetivo(){
		try {
			getFacadeFactory().getCriterioAvaliacaoFacade().excluirCriterioAvaliacaoPeriodoLetivo(getCriterioAvaliacaoVO(), (CriterioAvaliacaoPeriodoLetivoVO)getRequestMap().get("criterioAvaliacaoPeriodoLetivoItem"), getUsuarioLogado());
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void editarCriterioAvaliacaoPeriodoLetivo(){
		try {
			setIndicadorGeral(true);		
			setCriterioAvaliacaoIndicadorVO(new CriterioAvaliacaoIndicadorVO());			
			setCriterioAvaliacaoPeriodoLetivoVO(new CriterioAvaliacaoPeriodoLetivoVO());
			setCriterioAvaliacaoPeriodoLetivoVO((CriterioAvaliacaoPeriodoLetivoVO)getRequestMap().get("criterioAvaliacaoPeriodoLetivoItem"));
			montarListaSelectItemDisciplina();
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void montarListaSelectItemDisciplina() throws Exception{
		List<DisciplinaVO> disciplinaVOs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorPeriodoLetivo(getCriterioAvaliacaoPeriodoLetivoVO().getPeriodoLetivoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		getListaSelectItemDisciplina().clear();
		for(DisciplinaVO disciplinaVO:disciplinaVOs){
			boolean existe = false;
			for(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO: getCriterioAvaliacaoPeriodoLetivoVO().getCriterioAvaliacaoDisciplinaVOs()){
				if(criterioAvaliacaoDisciplinaVO.getDisciplina().getCodigo().equals(disciplinaVO.getCodigo())){
					existe = true;
				}
			}
			if(!existe){
				getListaSelectItemDisciplina().add(new SelectItem(disciplinaVO.getCodigo(), disciplinaVO.getNome()));
			}
		}
	}
	
	public void adicionarCriterioAvaliacaoDisciplina(){
		try{
			getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().adicionarCriterioAvaliacaoDisciplinaVO(getCriterioAvaliacaoPeriodoLetivoVO(), getDisciplina(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionado", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerCriterioAvaliacaoDisciplina(){
		try{
			getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().removerCriterioAvaliacaoDisciplinaVO(getCriterioAvaliacaoPeriodoLetivoVO(), (CriterioAvaliacaoDisciplinaVO) getRequestMap().get("criterioAvaliacaoDisciplinaItem"));
			setMensagemID("msg_dados_adicionado", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void editarCriterioAvaliacaoDisciplina(){
		try{
			setCriterioAvaliacaoDisciplinaVO(new CriterioAvaliacaoDisciplinaVO());			
			setCriterioAvaliacaoDisciplinaVO((CriterioAvaliacaoDisciplinaVO) getRequestMap().get("criterioAvaliacaoDisciplinaItem"));
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void subirCriterioAvaliacaoDisciplina(){
		try{			
			getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().alterarOrdemCriterioAvaliacaoDisciplinaVO(getCriterioAvaliacaoPeriodoLetivoVO(), (CriterioAvaliacaoDisciplinaVO) getRequestMap().get("criterioAvaliacaoDisciplinaItem"), true);
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void descerCriterioAvaliacaoDisciplina(){
		try{			
			getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().alterarOrdemCriterioAvaliacaoDisciplinaVO(getCriterioAvaliacaoPeriodoLetivoVO(), (CriterioAvaliacaoDisciplinaVO) getRequestMap().get("criterioAvaliacaoDisciplinaItem"), false);
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void adicionarCriterioAvaliacaoDisciplinaEixoIndicadorVO(){
		try{
			getFacadeFactory().getCriterioAvaliacaoDisciplinaFacade().adicionarCriterioAvaliacaoDisciplinaEixoIndicadorVO(getCriterioAvaliacaoDisciplinaVO(), getCriterioAvaliacaoDisciplinaEixoIndicadorVO());
			setCriterioAvaliacaoDisciplinaEixoIndicadorVO(new CriterioAvaliacaoDisciplinaEixoIndicadorVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerCriterioAvaliacaoDisciplinaEixoIndicadorVO(){
		try{
			getFacadeFactory().getCriterioAvaliacaoDisciplinaFacade().removerCriterioAvaliacaoDisciplinaEixoIndicadorVO(getCriterioAvaliacaoDisciplinaVO(), (CriterioAvaliacaoDisciplinaEixoIndicadorVO) getRequestMap().get("criterioAvaliacaoDisciplinaEixoIndicadorItem"));
			setCriterioAvaliacaoDisciplinaEixoIndicadorVO(new CriterioAvaliacaoDisciplinaEixoIndicadorVO());
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void editarCriterioAvaliacaoDisciplinaEixoIndicadorVO(){
		try{			
			setCriterioAvaliacaoDisciplinaEixoIndicadorVO(new CriterioAvaliacaoDisciplinaEixoIndicadorVO());
			setCriterioAvaliacaoDisciplinaEixoIndicadorVO((CriterioAvaliacaoDisciplinaEixoIndicadorVO) getRequestMap().get("criterioAvaliacaoDisciplinaEixoIndicadorItem"));	
			getCriterioAvaliacaoDisciplinaEixoIndicadorVO().setEixoIndicadorAnt(getCriterioAvaliacaoDisciplinaEixoIndicadorVO().getEixoIndicador());
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void adicionarIndicadorCriterioAvaliacaoDisciplinaEixoIndicadorVO(){
		try{
			setCriterioAvaliacaoIndicadorVO(new CriterioAvaliacaoIndicadorVO());
			setCriterioAvaliacaoDisciplinaEixoIndicadorVOEditar(new CriterioAvaliacaoDisciplinaEixoIndicadorVO());
			setCriterioAvaliacaoDisciplinaEixoIndicadorVOEditar((CriterioAvaliacaoDisciplinaEixoIndicadorVO) getRequestMap().get("criterioAvaliacaoDisciplinaEixoIndicadorItem"));
			setIndicadorGeral(false);
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void subirCriterioAvaliacaoDisciplinaEixoIndicadorVO(){
		try{
			getFacadeFactory().getCriterioAvaliacaoDisciplinaFacade().alterarOrdemCriterioAvaliacaoDisciplinaEixoIndicadorVO(getCriterioAvaliacaoDisciplinaVO(), (CriterioAvaliacaoDisciplinaEixoIndicadorVO) getRequestMap().get("criterioAvaliacaoDisciplinaEixoIndicadorItem"), true);			
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void descerCriterioAvaliacaoDisciplinaEixoIndicadorVO(){
		try{
			getFacadeFactory().getCriterioAvaliacaoDisciplinaFacade().alterarOrdemCriterioAvaliacaoDisciplinaEixoIndicadorVO(getCriterioAvaliacaoDisciplinaVO(), (CriterioAvaliacaoDisciplinaEixoIndicadorVO) getRequestMap().get("criterioAvaliacaoDisciplinaEixoIndicadorItem"), false);			
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<CriterioAvaliacaoIndicadorVO> getCriterioAvaliacaoIndicadorVOs(){
		if(getIndicadorGeral()){
			return getCriterioAvaliacaoPeriodoLetivoVO().getCriterioAvaliacaoIndicadorVOs();
		}
		return getCriterioAvaliacaoDisciplinaEixoIndicadorVOEditar().getCriterioAvaliacaoIndicadorVOs();
	}
	public Integer getQtdeIndicador(){
		if(getIndicadorGeral()){
			return getCriterioAvaliacaoPeriodoLetivoVO().getCriterioAvaliacaoIndicadorVOs().size();
		}
		return getCriterioAvaliacaoDisciplinaEixoIndicadorVOEditar().getCriterioAvaliacaoIndicadorVOs().size();
	}
	
	public void adicionarCriterioAvalicaoIndicador(){
		try{
			if(getIndicadorGeral()){
				getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().adicionarCriterioAvaliacaoIndicadorVO(getCriterioAvaliacaoPeriodoLetivoVO(), getCriterioAvaliacaoIndicadorVO());
			}else{
				getFacadeFactory().getCriterioAvaliacaoDisciplinaEixoIndicadorFacade().adicionarCriterioAvaliacaoIndicadorVO(getCriterioAvaliacaoDisciplinaEixoIndicadorVOEditar(), getCriterioAvaliacaoIndicadorVO());
			}
			setCriterioAvaliacaoIndicadorVO(new CriterioAvaliacaoIndicadorVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerCriterioAvalicaoIndicador(){
		try{
			if(getIndicadorGeral()){
				getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().excluirCriterioAvaliacaoIndicadorVO(getCriterioAvaliacaoPeriodoLetivoVO(), (CriterioAvaliacaoIndicadorVO) getRequestMap().get("criterioAvaliacaoIndicadorItem"));
			}else{
				getFacadeFactory().getCriterioAvaliacaoDisciplinaEixoIndicadorFacade().excluirCriterioAvaliacaoIndicadorVO(getCriterioAvaliacaoDisciplinaEixoIndicadorVOEditar(), (CriterioAvaliacaoIndicadorVO) getRequestMap().get("criterioAvaliacaoIndicadorItem"));
			}
			setCriterioAvaliacaoIndicadorVO(new CriterioAvaliacaoIndicadorVO());
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void editarCriterioAvalicaoIndicador(){
		try{			
			setCriterioAvaliacaoIndicadorVO(new CriterioAvaliacaoIndicadorVO());
			setCriterioAvaliacaoIndicadorVO((CriterioAvaliacaoIndicadorVO) getRequestMap().get("criterioAvaliacaoIndicadorItem"));
			getCriterioAvaliacaoIndicadorVO().setDescricaoAnt(getCriterioAvaliacaoIndicadorVO().getDescricao());
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void subirCriterioAvalicaoIndicador(){
		try{
			if(getIndicadorGeral()){
				getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().alterarOrdemCriterioAvaliacaoIndicadorVO(getCriterioAvaliacaoPeriodoLetivoVO(), (CriterioAvaliacaoIndicadorVO) getRequestMap().get("criterioAvaliacaoIndicadorItem"), true);
			}else{
				getFacadeFactory().getCriterioAvaliacaoDisciplinaEixoIndicadorFacade().alterarOrdemCriterioAvaliacaoIndicadorVO(getCriterioAvaliacaoDisciplinaEixoIndicadorVOEditar(), (CriterioAvaliacaoIndicadorVO) getRequestMap().get("criterioAvaliacaoIndicadorItem"), true);
			}						
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void descerCriterioAvalicaoIndicador(){
		try{
			if(getIndicadorGeral()){
				getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().alterarOrdemCriterioAvaliacaoIndicadorVO(getCriterioAvaliacaoPeriodoLetivoVO(), (CriterioAvaliacaoIndicadorVO) getRequestMap().get("criterioAvaliacaoIndicadorItem"), false);
			}else{
				getFacadeFactory().getCriterioAvaliacaoDisciplinaEixoIndicadorFacade().alterarOrdemCriterioAvaliacaoIndicadorVO(getCriterioAvaliacaoDisciplinaEixoIndicadorVOEditar(), (CriterioAvaliacaoIndicadorVO) getRequestMap().get("criterioAvaliacaoIndicadorItem"), false);
			}			
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String getUrlImagemCriterioNotaConceito(){
		CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO = ((CriterioAvaliacaoNotaConceitoVO) getRequestMap().get("criterioAvaliacaoNotaConceitoItem"));
		 if(criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().getUrlImagem().trim().isEmpty() && !criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().getNomeArquivo().trim().isEmpty()){			 
			 criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().setUrlImagem(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo()+"/"+criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().getPastaBaseArquivo().getValue()+"/"+criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().getNomeArquivo());
		 }
		 return criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().getUrlImagem();
	}	
	
	public void uploadImagem(FileUploadEvent uploadEvent){
		try {
			getFacadeFactory().getNotaConceitoIndicadorAvaliacaoFacade().uploadArquivo(uploadEvent, getNotaConceitoIndicadorAvaliacaoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void adicionarCriterioAvaliacaoNotaConceito(){
		try{
			getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().adicionarCriterioAvaliacaoNotaConceitoVO(getCriterioAvaliacaoPeriodoLetivoVO(), getCriterioAvaliacaoNotaConceitoVO(), getUsuarioLogado());
			setCriterioAvaliacaoNotaConceitoVO(new CriterioAvaliacaoNotaConceitoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerCriterioAvaliacaoNotaConceito(){
		try{
			getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().removerCriterioAvaliacaoNotaConceitoVO(getCriterioAvaliacaoPeriodoLetivoVO(), (CriterioAvaliacaoNotaConceitoVO) getRequestMap().get("criterioAvaliacaoNotaConceitoItem"));			
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void editarCriterioAvaliacaoNotaConceito(){
		try{
			setCriterioAvaliacaoNotaConceitoVO(new CriterioAvaliacaoNotaConceitoVO());
			setCriterioAvaliacaoNotaConceitoVO((CriterioAvaliacaoNotaConceitoVO) getRequestMap().get("criterioAvaliacaoNotaConceitoItem"));			
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void subirCriterioAvaliacaoNotaConceito(){
		try{
			getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().alterarOrdemCriterioAvaliacaoNotaConceitoVO(getCriterioAvaliacaoPeriodoLetivoVO(), (CriterioAvaliacaoNotaConceitoVO) getRequestMap().get("criterioAvaliacaoNotaConceitoItem"), true);			
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void descerCriterioAvaliacaoNotaConceito(){
		try{
			getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().alterarOrdemCriterioAvaliacaoNotaConceitoVO(getCriterioAvaliacaoPeriodoLetivoVO(), (CriterioAvaliacaoNotaConceitoVO) getRequestMap().get("criterioAvaliacaoNotaConceitoItem"), false);			
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	
	public List<SelectItem> getListaSelectItemOpcaoConsultaCurso() {
		if (listaSelectItemOpcaoConsultaCurso == null) {
			listaSelectItemOpcaoConsultaCurso = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsultaCurso.add(new SelectItem("nome", "Nome"));
		}
		return listaSelectItemOpcaoConsultaCurso;
	}



	public void setListaSelectItemOpcaoConsultaCurso(List<SelectItem> listaSelectItemOpcaoConsultaCurso) {
		this.listaSelectItemOpcaoConsultaCurso = listaSelectItemOpcaoConsultaCurso;
	}



	public CriterioAvaliacaoVO getCriterioAvaliacaoVO() {
		if (criterioAvaliacaoVO == null) {
			criterioAvaliacaoVO = new CriterioAvaliacaoVO();
		}
		return criterioAvaliacaoVO;
	}
	public void setCriterioAvaliacaoVO(CriterioAvaliacaoVO criterioAvaliacaoVO) {
		this.criterioAvaliacaoVO = criterioAvaliacaoVO;
	}
	public CriterioAvaliacaoPeriodoLetivoVO getCriterioAvaliacaoPeriodoLetivoVO() {
		if (criterioAvaliacaoPeriodoLetivoVO == null) {
			criterioAvaliacaoPeriodoLetivoVO = new CriterioAvaliacaoPeriodoLetivoVO();
		}
		return criterioAvaliacaoPeriodoLetivoVO;
	}
	public void setCriterioAvaliacaoPeriodoLetivoVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) {
		this.criterioAvaliacaoPeriodoLetivoVO = criterioAvaliacaoPeriodoLetivoVO;
	}
	public CriterioAvaliacaoDisciplinaVO getCriterioAvaliacaoDisciplinaVO() {
		if (criterioAvaliacaoDisciplinaVO == null) {
			criterioAvaliacaoDisciplinaVO = new CriterioAvaliacaoDisciplinaVO();
		}
		return criterioAvaliacaoDisciplinaVO;
	}
	public void setCriterioAvaliacaoDisciplinaVO(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO) {
		this.criterioAvaliacaoDisciplinaVO = criterioAvaliacaoDisciplinaVO;
	}
	public CriterioAvaliacaoIndicadorVO getCriterioAvaliacaoIndicadorVO() {
		if (criterioAvaliacaoIndicadorVO == null) {
			criterioAvaliacaoIndicadorVO = new CriterioAvaliacaoIndicadorVO();
		}
		return criterioAvaliacaoIndicadorVO;
	}
	public void setCriterioAvaliacaoIndicadorVO(CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO) {
		this.criterioAvaliacaoIndicadorVO = criterioAvaliacaoIndicadorVO;
	}
	public CriterioAvaliacaoDisciplinaEixoIndicadorVO getCriterioAvaliacaoDisciplinaEixoIndicadorVO() {
		if (criterioAvaliacaoDisciplinaEixoIndicadorVO == null) {
			criterioAvaliacaoDisciplinaEixoIndicadorVO = new CriterioAvaliacaoDisciplinaEixoIndicadorVO();
		}
		return criterioAvaliacaoDisciplinaEixoIndicadorVO;
	}
	public void setCriterioAvaliacaoDisciplinaEixoIndicadorVO(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) {
		this.criterioAvaliacaoDisciplinaEixoIndicadorVO = criterioAvaliacaoDisciplinaEixoIndicadorVO;
	}
	public NotaConceitoIndicadorAvaliacaoVO getNotaConceitoIndicadorAvaliacaoVO() {
		if (notaConceitoIndicadorAvaliacaoVO == null) {
			notaConceitoIndicadorAvaliacaoVO = new NotaConceitoIndicadorAvaliacaoVO();
		}
		return notaConceitoIndicadorAvaliacaoVO;
	}
	public void setNotaConceitoIndicadorAvaliacaoVO(NotaConceitoIndicadorAvaliacaoVO notaConceitoIndicadorAvaliacaoVO) {
		this.notaConceitoIndicadorAvaliacaoVO = notaConceitoIndicadorAvaliacaoVO;
	}
	public CriterioAvaliacaoNotaConceitoVO getCriterioAvaliacaoNotaConceitoVO() {
		if (criterioAvaliacaoNotaConceitoVO == null) {
			criterioAvaliacaoNotaConceitoVO = new CriterioAvaliacaoNotaConceitoVO();
		}
		return criterioAvaliacaoNotaConceitoVO;
	}
	public void setCriterioAvaliacaoNotaConceitoVO(CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO) {
		this.criterioAvaliacaoNotaConceitoVO = criterioAvaliacaoNotaConceitoVO;
	}
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
			try {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				listaSelectItemUnidadeEnsino.add(new SelectItem(0, ""));
				for(UnidadeEnsinoVO unidadeEnsinoVO:unidadeEnsinoVOs){
					listaSelectItemUnidadeEnsino.add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getCodigo()+" - "+unidadeEnsinoVO.getNome()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listaSelectItemUnidadeEnsino;
	}
	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}
	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}
	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}
	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}
	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}
	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}
	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGradeCurricular;
	}
	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}
	public List<SelectItem> getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPeriodoLetivo;
	}
	public void setListaSelectItemPeriodoLetivo(List<SelectItem> listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
	}
	public List<SelectItem> getListaSelectItemNotaConceito() {
		if (listaSelectItemNotaConceito == null) {
			listaSelectItemNotaConceito = new ArrayList<SelectItem>(0);
			try {
				List<NotaConceitoIndicadorAvaliacaoVO> notaConceitoIndicadorAvaliacaoVOs = getFacadeFactory().getNotaConceitoIndicadorAvaliacaoFacade().consultar("situacao", StatusAtivoInativoEnum.ATIVO.name(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				for(NotaConceitoIndicadorAvaliacaoVO obj: notaConceitoIndicadorAvaliacaoVOs){
					listaSelectItemNotaConceito.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listaSelectItemNotaConceito;
	}
	public void setListaSelectItemNotaConceito(List<SelectItem> listaSelectItemNotaConceito) {
		this.listaSelectItemNotaConceito = listaSelectItemNotaConceito;
	}
	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}
	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public Integer getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = 0;
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(Integer periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public Integer getDisciplina() {
		if (disciplina == null) {
			disciplina = 0;
		}
		return disciplina;
	}

	public void setDisciplina(Integer disciplina) {
		this.disciplina = disciplina;
	}

	

	public CriterioAvaliacaoDisciplinaEixoIndicadorVO getCriterioAvaliacaoDisciplinaEixoIndicadorVOEditar() {
		if (criterioAvaliacaoDisciplinaEixoIndicadorVOEditar == null) {
			criterioAvaliacaoDisciplinaEixoIndicadorVOEditar = new CriterioAvaliacaoDisciplinaEixoIndicadorVO();
		}
		return criterioAvaliacaoDisciplinaEixoIndicadorVOEditar;
	}

	public void setCriterioAvaliacaoDisciplinaEixoIndicadorVOEditar(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVOEditar) {
		this.criterioAvaliacaoDisciplinaEixoIndicadorVOEditar = criterioAvaliacaoDisciplinaEixoIndicadorVOEditar;
	}

	public Boolean getIndicadorGeral() {
		if (indicadorGeral == null) {
			indicadorGeral = false;
		}
		return indicadorGeral;
	}

	public void setIndicadorGeral(Boolean indicadorGeral) {
		this.indicadorGeral = indicadorGeral;
	}

	public Integer getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = getUnidadeEnsinoLogado().getCodigo();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(Integer unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemOpcaoConsulta() {
		if (listaSelectItemOpcaoConsulta == null) {
			listaSelectItemOpcaoConsulta = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsulta.add(new SelectItem("curso", "Curso"));
			listaSelectItemOpcaoConsulta.add(new SelectItem("gradeCurricular", "Matriz Curricular"));
			listaSelectItemOpcaoConsulta.add(new SelectItem("anoVigencia", "Ano Vigência"));
		}
		return listaSelectItemOpcaoConsulta;
	}

	public void setListaSelectItemOpcaoConsulta(List<SelectItem> listaSelectItemOpcaoConsulta) {
		this.listaSelectItemOpcaoConsulta = listaSelectItemOpcaoConsulta;
	}

	public List<SelectItem> getListaSelectItemAvaliarNaoAvaliar() {
		if (listaSelectItemAvaliarNaoAvaliar == null) {
			listaSelectItemAvaliarNaoAvaliar = new ArrayList<SelectItem>(0);
			listaSelectItemAvaliarNaoAvaliar.add(new SelectItem(AvaliarNaoAvaliarEnum.AVALIAR, AvaliarNaoAvaliarEnum.AVALIAR.getValorApresentar()));
			listaSelectItemAvaliarNaoAvaliar.add(new SelectItem(AvaliarNaoAvaliarEnum.NAO_AVALIAR, AvaliarNaoAvaliarEnum.NAO_AVALIAR.getValorApresentar()));
		}
		return listaSelectItemAvaliarNaoAvaliar;
	}

	public void setListaSelectItemAvaliarNaoAvaliar(List<SelectItem> listaSelectItemAvaliarNaoAvaliar) {
		this.listaSelectItemAvaliarNaoAvaliar = listaSelectItemAvaliarNaoAvaliar;
	}
	
	 public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
	        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
	        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
	        consultar();
	    }
	
	@SuppressWarnings("unchecked")
	public void imprimir(){
		try{
			getCriterioAvaliacaoVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getCriterioAvaliacaoVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
			getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator+"CriterioAvaliacaoRel.jrxml");
	        getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
	        getSuperParametroRelVO().setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	        getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
	        getSuperParametroRelVO().setTituloRelatorio("Avaliação Individual");
	        getSuperParametroRelVO().getListaObjetos().clear();
	        getSuperParametroRelVO().getListaObjetos().add(getCriterioAvaliacaoVO());
	        getSuperParametroRelVO().setCaminhoBaseRelatorio("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	        getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
	        getSuperParametroRelVO().setNomeEmpresa(getUnidadeEnsinoLogado().getNome());
	        getSuperParametroRelVO().adicionarParametro("caminhoArquivoFixo", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo());
	        realizarImpressaoRelatorio();
			}catch(Exception e){
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
	}
	
	public void limparConsultaCurso() {
		setValorConsultaCurso("");
		setListaConsultaCurso(new ArrayList<>());
	}
}
