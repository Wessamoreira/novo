package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.AlunosPorUnidadeCursoTurmaRelVO;
import relatorio.negocio.comuns.academico.GestaoTurmaDisciplinaRelVO;
import relatorio.negocio.comuns.academico.GestaoTurmaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.AlunosPorUnidadeCursoTurmaRel;
import relatorio.negocio.jdbc.academico.GestaoTurmaRel;

@Controller("GestaoTurmaRelControle")
@Lazy
@Scope("viewScope")
public class GestaoTurmaRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7901539747530089652L;
	private PeriodicidadeEnum periodicidade;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private String filtrarPor;	
	private Integer periodoLetivoDe;
	private Integer periodoLetivoAte;
	private String situacaoTurma;
	private String situacaoTurmaSubTurma;
	private String situacaoTurmaAgrupada;
	private String situacaoProgramacaoAula;
	private String situacaoProgramacaoAulaSubTurma;
	private String situacaoProgramacaoAulaTurmaAgrupada;
	private String situacaoVaga;
	private String situacaoVagaSubTurma;	
	private String situacaoMatricula;
	private String situacaoMatriculaSubTurma;	
	private String situacaoMatriculaTurmaAgrupada;	
	private String ano;
	private String semestre;
	private DisciplinaVO disciplinaVO;
	private TurmaVO turmaVO;
	private List<SelectItem> listaSelectItemPeriodicidade;
	private List<SelectItem> listaSelectItemSemestre;
	private List<SelectItem> listaSelectItemFiltrarPor;
	private List<SelectItem> listaSelectItemUnidadeEnsino;	
	private List<SelectItem> listaSelectDisciplina;
	private List<SelectItem> listaSelectSituacaoTurma;	
	private List<SelectItem> listaSelectSituacaoProgramacaoAula;	
	private List<SelectItem> listaSelectSituacaoMatricula;	
	private List<SelectItem> listaSelectSituacaoVaga;	
	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<SelectItem> tipoConsultaComboTurma;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<SelectItem> tipoConsultaComboDisciplina;
	private List<SelectItem> tipoConsultaComboCurso;
	private List<GestaoTurmaRelVO> gestaoTurmaRelVOs;
	private GestaoTurmaRelVO gestaoTurmaRelVO;
	private GestaoTurmaDisciplinaRelVO gestaoTurmaDisciplinaRelVO;
	private List<AlunosPorUnidadeCursoTurmaRelVO> alunosPorUnidadeCursoTurmaRelVOs; 	
	private List<SelectItem> listaSelectItemConsiderarFiltroSubTurma;
	private List<SelectItem> listaSelectItemConsiderarFiltroTurmaAgrupada;
	private String considerarFiltroTurmaAgrupada;
	private String considerarFiltroSubTurma;
	private List<LayoutPadraoVO> layoutPadraoVOs;
	private String nomeAgrupadorFiltros;
	
	public void limparDadosPertinenteUnidadeEnsino(){
		limparCursos();		
		setTurmaVO(null);		
	}
	
	public void consultarDadosRelatorio(){
		try{
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getGestaoTurmaRelFacade().consultarDadosRelatorio(getUnidadeEnsinoVO(), getPeriodicidade(), getAno(), getSemestre(), getFiltrarPor(), getCursoVOs(), getTurnoVOs(), getTurmaVO(), getDisciplinaVO(), getSituacaoTurma(), getSituacaoTurmaSubTurma(), getSituacaoTurmaAgrupada(), getSituacaoProgramacaoAula(), getSituacaoProgramacaoAulaSubTurma(), getSituacaoProgramacaoAulaTurmaAgrupada(), getSituacaoVaga(), getSituacaoVagaSubTurma(),
					getSituacaoMatricula(),getSituacaoMatriculaSubTurma(), getSituacaoMatriculaTurmaAgrupada(), getPeriodoLetivoDe(), getPeriodoLetivoAte(), getConsiderarFiltroSubTurma(), getConsiderarFiltroTurmaAgrupada(), getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getGestaoTurmaRelFacade().consultarTotalRegistroEncontrado(getUnidadeEnsinoVO(), getPeriodicidade(), getAno(), getSemestre(), getFiltrarPor(), 
					getCursoVOs(), getTurnoVOs(), getTurmaVO(), getDisciplinaVO(), getSituacaoTurma(), getSituacaoTurmaSubTurma(), getSituacaoTurmaAgrupada(),  getSituacaoProgramacaoAula(), getSituacaoProgramacaoAulaSubTurma(), getSituacaoProgramacaoAulaTurmaAgrupada(), 
					getSituacaoVaga(), getSituacaoVagaSubTurma(), getSituacaoMatricula(), getSituacaoMatriculaSubTurma(), getSituacaoMatriculaTurmaAgrupada(), getPeriodoLetivoDe(), getPeriodoLetivoAte(), getConsiderarFiltroSubTurma(), getConsiderarFiltroTurmaAgrupada(), getUsuarioLogado()));
			persistirFiltrosBasicos();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void scrollerListener(DataScrollEvent dataScrollerEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
        getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());        
        consultarDadosRelatorio();
    }

	public List<GestaoTurmaRelVO> getGestaoTurmaRelVOs() {
		return gestaoTurmaRelVOs;
	}


	public void setGestaoTurmaRelVOs(List<GestaoTurmaRelVO> gestaoTurmaRelVOs) {
		this.gestaoTurmaRelVOs = gestaoTurmaRelVOs;
	}


	public GestaoTurmaRelVO getGestaoTurmaRelVO() {
		return gestaoTurmaRelVO;
	}


	public void setGestaoTurmaRelVO(GestaoTurmaRelVO gestaoTurmaRelVO) {
		this.gestaoTurmaRelVO = gestaoTurmaRelVO;
	}


	public GestaoTurmaDisciplinaRelVO getGestaoTurmaDisciplinaRelVO() {
		return gestaoTurmaDisciplinaRelVO;
	}


	public void setGestaoTurmaDisciplinaRelVO(GestaoTurmaDisciplinaRelVO gestaoTurmaDisciplinaRelVO) {
		this.gestaoTurmaDisciplinaRelVO = gestaoTurmaDisciplinaRelVO;
	}


	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if(listaSelectItemPeriodicidade == null){
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>(0);
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.ANUAL, PeriodicidadeEnum.ANUAL.getDescricao()));
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.INTEGRAL, PeriodicidadeEnum.INTEGRAL.getDescricao()));
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.SEMESTRAL, PeriodicidadeEnum.SEMESTRAL.getDescricao()));			
		}
		return listaSelectItemPeriodicidade;
	}

	public void setListaSelectItemPeriodicidade(List<SelectItem> listaSelectItemPeriodicidade) {
		this.listaSelectItemPeriodicidade = listaSelectItemPeriodicidade;
	}

	public PeriodicidadeEnum getPeriodicidade() {
		if(periodicidade == null){
			periodicidade = PeriodicidadeEnum.SEMESTRAL;
		}
		return periodicidade;
	}

	public void setPeriodicidade(PeriodicidadeEnum periodicidade) {
		this.periodicidade = periodicidade;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if(unidadeEnsinoVO == null){
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public String getFiltrarPor() {
		if(filtrarPor == null){
			filtrarPor = "curso";
		}
		return filtrarPor;
	}

	public void setFiltrarPor(String filtrarPor) {
		this.filtrarPor = filtrarPor;
	}

	

	public String getSituacaoTurma() {
		if(situacaoTurma == null){
			situacaoTurma = "";
		}
		return situacaoTurma;
	}

	public void setSituacaoTurma(String situacaoTurma) {
		this.situacaoTurma = situacaoTurma;
	}

	public String getSituacaoProgramacaoAula() {
		if(situacaoProgramacaoAula == null){
			situacaoProgramacaoAula = "";
		}
		return situacaoProgramacaoAula;
	}

	public void setSituacaoProgramacaoAula(String situacaoProgramacaoAula) {
		this.situacaoProgramacaoAula = situacaoProgramacaoAula;
	}

	public String getSituacaoVaga() {
		if(situacaoVaga == null){
			situacaoVaga = "";
		}
		return situacaoVaga;
	}

	public void setSituacaoVaga(String situacaoVaga) {
		this.situacaoVaga = situacaoVaga;
	}

	public String getSituacaoMatricula() {
		if(situacaoMatricula == null){
			situacaoMatricula = "";
		}
		return situacaoMatricula;
	}

	public void setSituacaoMatricula(String situacaoMatricula) {
		this.situacaoMatricula = situacaoMatricula;
	}

	public String getAno() {
		if(ano == null){
			ano = Uteis.getAnoDataAtual4Digitos();
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if(semestre == null){
			semestre = Uteis.getSemestreAtual();
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public DisciplinaVO getDisciplinaVO() {
		if(disciplinaVO == null){
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public TurmaVO getTurmaVO() {
		if(turmaVO == null){
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public List<SelectItem> getListaSelectItemFiltrarPor() {
		if(listaSelectItemFiltrarPor == null){
			listaSelectItemFiltrarPor = new ArrayList<SelectItem>(0);			
			listaSelectItemFiltrarPor.add(new SelectItem("curso", "Curso"));
			listaSelectItemFiltrarPor.add(new SelectItem("turma", "Turma"));
		}
		return listaSelectItemFiltrarPor;
	}

	public void setListaSelectItemFiltrarPor(List<SelectItem> listaSelectItemFiltrarPor) {
		this.listaSelectItemFiltrarPor = listaSelectItemFiltrarPor;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if(listaSelectItemUnidadeEnsino == null){
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);	
			try {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				if(!unidadeEnsinoVOs.isEmpty()){
					getUnidadeEnsinoVO().setCodigo(unidadeEnsinoVOs.get(0).getCodigo());
				}
				listaSelectItemUnidadeEnsino = UtilSelectItem.getListaSelectItem(unidadeEnsinoVOs, "codigo", "nome", false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectDisciplina() {
		if(listaSelectDisciplina == null){
			listaSelectDisciplina = new ArrayList<SelectItem>(0);			
		}
		return listaSelectDisciplina;
	}

	public void setListaSelectDisciplina(List<SelectItem> listaSelectDisciplina) {
		this.listaSelectDisciplina = listaSelectDisciplina;
	}

	public List<SelectItem> getListaSelectSituacaoTurma() {
		if(listaSelectSituacaoTurma == null){
			listaSelectSituacaoTurma = new ArrayList<SelectItem>(0);
			listaSelectSituacaoTurma.add(new SelectItem("", ""));
			listaSelectSituacaoTurma.add(new SelectItem("AB", "Aberta"));
			listaSelectSituacaoTurma.add(new SelectItem("FE", "Fechada"));
		}
		return listaSelectSituacaoTurma;
	}

	public void setListaSelectSituacaoTurma(List<SelectItem> listaSelectSituacaoTurma) {
		this.listaSelectSituacaoTurma = listaSelectSituacaoTurma;
	}

	public List<SelectItem> getListaSelectSituacaoProgramacaoAula() {
		if(listaSelectSituacaoProgramacaoAula == null){
			listaSelectSituacaoProgramacaoAula = new ArrayList<SelectItem>(0);
			listaSelectSituacaoProgramacaoAula.add(new SelectItem("", ""));
			listaSelectSituacaoProgramacaoAula.add(new SelectItem("naoPossui", "Sem Aula Programada"));
			listaSelectSituacaoProgramacaoAula.add(new SelectItem("possui", "Com Aula Programada"));
			listaSelectSituacaoProgramacaoAula.add(new SelectItem("aulaNaoRegistrada", "Aula Não Registrada"));
		}
		return listaSelectSituacaoProgramacaoAula;
	}

	public void setListaSelectSituacaoProgramacaoAula(List<SelectItem> listaSelectSituacaoProgramacaoAula) {
		this.listaSelectSituacaoProgramacaoAula = listaSelectSituacaoProgramacaoAula;
	}

	public List<SelectItem> getListaSelectSituacaoVaga() {
		if(listaSelectSituacaoVaga == null){
			listaSelectSituacaoVaga = new ArrayList<SelectItem>(0);
			listaSelectSituacaoVaga.add(new SelectItem("", ""));
			listaSelectSituacaoVaga.add(new SelectItem("naoPossui", "Sem Vaga Cadastrada"));
			listaSelectSituacaoVaga.add(new SelectItem("possui", "Com Vaga Cadastrada"));
			listaSelectSituacaoVaga.add(new SelectItem("vagaPreenchida", "Com Vaga Preenchida"));
			listaSelectSituacaoVaga.add(new SelectItem("vagaAcima", "Com Vaga Acima Limite"));
			listaSelectSituacaoVaga.add(new SelectItem("vagaDisponivel", "Com Vaga Disponível"));
		}
		return listaSelectSituacaoVaga;
	}
	
	public List<SelectItem> getListaSelectMatricula() {
		if(listaSelectSituacaoMatricula == null){
			listaSelectSituacaoMatricula = new ArrayList<SelectItem>(0);
			listaSelectSituacaoMatricula.add(new SelectItem("", ""));
			listaSelectSituacaoMatricula.add(new SelectItem("naoPossui", "Sem Alunos Matriculados"));
			listaSelectSituacaoMatricula.add(new SelectItem("possui", "Com Alunos Matriculados"));
		}
		return listaSelectSituacaoMatricula;
	}

	public void setListaSelectSituacaoVaga(List<SelectItem> listaSelectSituacaoVaga) {
		this.listaSelectSituacaoVaga = listaSelectSituacaoVaga;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if(listaConsultaTurma == null){
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getCampoConsultaTurma() {
		if(campoConsultaTurma == null){
			campoConsultaTurma ="identificadorTurma";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if(valorConsultaTurma == null){
			valorConsultaTurma ="";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		if(tipoConsultaComboTurma == null){
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador Turma"));
			tipoConsultaComboTurma.add(new SelectItem("curso", "Curso"));
		}
		return tipoConsultaComboTurma;
	}

	public void setTipoConsultaComboTurma(List<SelectItem> tipoConsultaComboTurma) {
		this.tipoConsultaComboTurma = tipoConsultaComboTurma;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		if(tipoConsultaComboCurso == null){
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
	}

	public void setTipoConsultaComboCurso(List<SelectItem> tipoConsultaComboCurso) {
		this.tipoConsultaComboCurso = tipoConsultaComboCurso;
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		if(listaSelectItemSemestre == null){
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
	}

	public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
		this.listaSelectItemSemestre = listaSelectItemSemestre;
	}

	public void realizarNavegacaoVagaTurmaPorGestaoDisciplina(){
		GestaoTurmaDisciplinaRelVO gestaoTurmaDisciplinaRelVO = (GestaoTurmaDisciplinaRelVO) getRequestMap().get("gestaoDisciplina");
		context().getExternalContext().getSessionMap().put("vagaTurma.turma", gestaoTurmaDisciplinaRelVO.getGestaoTurmaRelVO().getTurmaVO().getCodigo());
		if(!getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL)){
			context().getExternalContext().getSessionMap().put("vagaTurma.ano", getAno());
		}
		if(getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)){
			context().getExternalContext().getSessionMap().put("vagaTurma.semestre", getSemestre());
		}
	}

	public void realizarNavegacaoVagaTurmaPorGestaoTurma(){
		GestaoTurmaRelVO gestaoTurmaRelVO = (GestaoTurmaRelVO) getRequestMap().get("gestaoTurma");
		context().getExternalContext().getSessionMap().put("vagaTurma.turma", gestaoTurmaRelVO.getTurmaVO().getCodigo());
		if(!getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL)){
			context().getExternalContext().getSessionMap().put("vagaTurma.ano", getAno());
		}
		if(getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)){
			context().getExternalContext().getSessionMap().put("vagaTurma.semestre", getSemestre());
		}
	}
	
	public void realizarNavegacaoVagaTurmaPorGestaoSubTurma(){
		GestaoTurmaRelVO gestaoTurmaRelVO = (GestaoTurmaRelVO) getRequestMap().get("gestaoSubTurma");
		context().getExternalContext().getSessionMap().put("vagaTurma.turma", gestaoTurmaRelVO.getTurmaVO().getCodigo());
		if(!getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL)){
			context().getExternalContext().getSessionMap().put("vagaTurma.ano", getAno());
		}
		if(getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)){
			context().getExternalContext().getSessionMap().put("vagaTurma.semestre", getSemestre());
		}
	}
	
	public void realizarNavegacaoHorarioTurmaPorGestaoDisciplina(){
		GestaoTurmaDisciplinaRelVO gestaoTurmaDisciplinaRelVO = (GestaoTurmaDisciplinaRelVO) getRequestMap().get("gestaoDisciplina");
		context().getExternalContext().getSessionMap().put("programacaoAula.turma", gestaoTurmaDisciplinaRelVO.getGestaoTurmaRelVO().getTurmaVO().getCodigo());
		if(!getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL)){
			context().getExternalContext().getSessionMap().put("programacaoAula.ano", getAno());
		}
		if(getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)){
			context().getExternalContext().getSessionMap().put("programacaoAula.semestre", getSemestre());
		}
	}

	public void realizarNavegacaoHorarioTurmaPorGestaoTurma(){
		GestaoTurmaRelVO gestaoTurmaRelVO = (GestaoTurmaRelVO) getRequestMap().get("gestaoTurma");
		context().getExternalContext().getSessionMap().put("programacaoAula.turma", gestaoTurmaRelVO.getTurmaVO().getCodigo());
		if(!getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL)){
			context().getExternalContext().getSessionMap().put("programacaoAula.ano", getAno());
		}
		if(getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)){
			context().getExternalContext().getSessionMap().put("programacaoAula.semestre", getSemestre());
		}
	}
	
	public void realizarNavegacaoHorarioTurmaPorGestaoSubTurma(){
		GestaoTurmaRelVO gestaoTurmaRelVO = (GestaoTurmaRelVO) getRequestMap().get("gestaoSubTurma");
		context().getExternalContext().getSessionMap().put("programacaoAula.turma", gestaoTurmaRelVO.getTurmaVO().getCodigo());
		if(!getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL)){
			context().getExternalContext().getSessionMap().put("programacaoAula.ano", getAno());
		}
		if(getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)){
			context().getExternalContext().getSessionMap().put("programacaoAula.semestre", getSemestre());
		}
	}
	
	public void realizarNavegacaoRegistroAulaPorGestaoDisciplina(){
		GestaoTurmaDisciplinaRelVO gestaoTurmaDisciplinaRelVO = (GestaoTurmaDisciplinaRelVO) getRequestMap().get("gestaoDisciplina");
		context().getExternalContext().getSessionMap().put("registroAula.turma", gestaoTurmaDisciplinaRelVO.getGestaoTurmaRelVO().getTurmaVO().getCodigo());
		context().getExternalContext().getSessionMap().put("registroAula.disciplina", gestaoTurmaDisciplinaRelVO.getDisciplinaVO().getCodigo());
		if(!getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL)){
			context().getExternalContext().getSessionMap().put("registroAula.ano", getAno());
		}
		if(getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)){
			context().getExternalContext().getSessionMap().put("registroAula.semestre", getSemestre());
		}
	}

	public void realizarNavegacaoRegistroAulaPorGestaoTurma(){
		GestaoTurmaRelVO gestaoTurmaRelVO = (GestaoTurmaRelVO) getRequestMap().get("gestaoTurma");
		context().getExternalContext().getSessionMap().put("registroAula.turma", gestaoTurmaRelVO.getTurmaVO().getCodigo());
		context().getExternalContext().getSessionMap().remove("registroAula.disciplina");
		if(!getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL)){
			context().getExternalContext().getSessionMap().put("registroAula.ano", getAno());
		}
		if(getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)){
			context().getExternalContext().getSessionMap().put("registroAula.semestre", getSemestre());
		}
	}
	
	public void realizarNavegacaoRegistroAulaPorGestaoSubTurma(){
		GestaoTurmaDisciplinaRelVO gestaoTurmaDisciplinaRelVO = (GestaoTurmaDisciplinaRelVO) getRequestMap().get("gestaoDisciplina");
		GestaoTurmaRelVO gestaoTurmaRelVO = (GestaoTurmaRelVO) getRequestMap().get("gestaoSubTurma");
		context().getExternalContext().getSessionMap().put("registroAula.turma", gestaoTurmaRelVO.getTurmaVO().getCodigo());
		context().getExternalContext().getSessionMap().put("registroAula.disciplina", gestaoTurmaDisciplinaRelVO.getDisciplinaVO().getCodigo());
		if(!getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL)){
			context().getExternalContext().getSessionMap().put("registroAula.ano", getAno());
		}
		if(getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)){
			context().getExternalContext().getSessionMap().put("registroAula.semestre", getSemestre());
		}
	}
	
	public void realizarNavegacaoLancamentoNotaPorGestaoDisciplina(){
		GestaoTurmaDisciplinaRelVO gestaoTurmaDisciplinaRelVO = (GestaoTurmaDisciplinaRelVO) getRequestMap().get("gestaoDisciplina");
		context().getExternalContext().getSessionMap().put("lancamentoNota.turma", gestaoTurmaDisciplinaRelVO.getGestaoTurmaRelVO().getTurmaVO().getIdentificadorTurma());
		context().getExternalContext().getSessionMap().put("lancamentoNota.disciplina", gestaoTurmaDisciplinaRelVO.getDisciplinaVO().getCodigo());
		if(!getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL)){
			context().getExternalContext().getSessionMap().put("lancamentoNota.ano", getAno());
		}
		if(getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)){
			context().getExternalContext().getSessionMap().put("lancamentoNota.semestre", getSemestre());
		}
	}

	public void realizarNavegacaoLancamentoNotaPorGestaoTurma(){
		GestaoTurmaRelVO gestaoTurmaRelVO = (GestaoTurmaRelVO) getRequestMap().get("gestaoTurma");
		context().getExternalContext().getSessionMap().put("lancamentoNota.turma", gestaoTurmaRelVO.getTurmaVO().getIdentificadorTurma());
		context().getExternalContext().getSessionMap().remove("lancamentoNota.disciplina");
		if(!getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL)){
			context().getExternalContext().getSessionMap().put("lancamentoNota.ano", getAno());
		}
		if(getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)){
			context().getExternalContext().getSessionMap().put("lancamentoNota.semestre", getSemestre());
		}
	}
	
	public void realizarNavegacaoLancamentoNotaPorGestaoSubTurma(){
		GestaoTurmaDisciplinaRelVO gestaoTurmaDisciplinaRelVO = (GestaoTurmaDisciplinaRelVO) getRequestMap().get("gestaoDisciplina");
		GestaoTurmaRelVO gestaoTurmaRelVO = (GestaoTurmaRelVO) getRequestMap().get("gestaoSubTurma");
		context().getExternalContext().getSessionMap().put("lancamentoNota.turma", gestaoTurmaRelVO.getTurmaVO().getIdentificadorTurma());
		context().getExternalContext().getSessionMap().put("lancamentoNota.disciplina", gestaoTurmaDisciplinaRelVO.getDisciplinaVO().getCodigo());
		if(!getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL)){
			context().getExternalContext().getSessionMap().put("lancamentoNota.ano", getAno());
		}
		if(getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)){
			context().getExternalContext().getSessionMap().put("lancamentoNota.semestre", getSemestre());
		}
	}
	
	public void selecionarGestaoTurma(){
		setGestaoTurmaRelVO((GestaoTurmaRelVO) getRequestMap().get("gestaoTurma"));		
	}
	
	public void selecionarGestaoTurmaDisciplina(){
		setGestaoTurmaDisciplinaRelVO((GestaoTurmaDisciplinaRelVO) getRequestMap().get("gestaoDisciplina"));		
	}
	
	public void selecionarGestaoSubTurma(){
		setGestaoTurmaRelVO((GestaoTurmaRelVO) getRequestMap().get("gestaoSubTurma"));	
	}
	
	public void alteracaoSituacaoTurma() {
		String situacaoAtual = getGestaoTurmaRelVO().getTurmaVO().getSituacao();
		try{			
			getGestaoTurmaRelVO().getTurmaVO().setSituacao(getGestaoTurmaRelVO().getTurmaVO().getSituacao().equals("AB") ? "FE" : "AB");			
			getFacadeFactory().getTurmaFacade().alterarSituacaoTurma(getGestaoTurmaRelVO().getTurmaVO(), getUsuarioLogado());		
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			getGestaoTurmaRelVO().getTurmaVO().setSituacao(situacaoAtual);
		}
	}
	
	
	public void consultarCurso() {		
		try {			
			getUnidadeEnsinoVOs().clear();
			getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(true);
			getUnidadeEnsinoVOs().add(getUnidadeEnsinoVO());
			setCursosApresentar("");
			limparTurnos();
			consultarCursoFiltroRelatorio(getPeriodicidade().getValor());			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
		
	public void consultarTurnoFiltroRelatorio(){
		super.consultarTurnoFiltroRelatorio();
	}
	
	public void limparListaGestaoTurma(){
		getGestaoTurmaRelVOs().clear();
	}	

	public String getSituacaoTurmaSubTurma() {
		if(situacaoTurmaSubTurma == null){
			situacaoTurmaSubTurma = "";
		}
		return situacaoTurmaSubTurma;
	}

	public void setSituacaoTurmaSubTurma(String situacaoTurmaSubTurma) {
		this.situacaoTurmaSubTurma = situacaoTurmaSubTurma;
	}

	public String getSituacaoTurmaAgrupada() {
		if(situacaoTurmaAgrupada == null){
			situacaoTurmaAgrupada = "";
		}
		return situacaoTurmaAgrupada;
	}

	public void setSituacaoTurmaAgrupada(String situacaoTurmaAgrupada) {
		this.situacaoTurmaAgrupada = situacaoTurmaAgrupada;
	}

	public String getSituacaoProgramacaoAulaSubTurma() {
		if(situacaoProgramacaoAulaSubTurma == null){
			situacaoProgramacaoAulaSubTurma = "";
		}
		return situacaoProgramacaoAulaSubTurma;
	}

	public void setSituacaoProgramacaoAulaSubTurma(String situacaoProgramacaoAulaSubTurma) {
		this.situacaoProgramacaoAulaSubTurma = situacaoProgramacaoAulaSubTurma;
	}

	public String getSituacaoProgramacaoAulaTurmaAgrupada() {
		if(situacaoProgramacaoAulaTurmaAgrupada == null){
			situacaoProgramacaoAulaTurmaAgrupada = "";
		}
		return situacaoProgramacaoAulaTurmaAgrupada;
	}

	public void setSituacaoProgramacaoAulaTurmaAgrupada(String situacaoProgramacaoAulaTurmaAgrupada) {
		this.situacaoProgramacaoAulaTurmaAgrupada = situacaoProgramacaoAulaTurmaAgrupada;
	}

	public String getSituacaoVagaSubTurma() {
		if(situacaoVagaSubTurma == null){
			situacaoVagaSubTurma = "";
		}
		return situacaoVagaSubTurma;
	}

	public void setSituacaoVagaSubTurma(String situacaoVagaSubTurma) {
		this.situacaoVagaSubTurma = situacaoVagaSubTurma;
	}

	public String getSituacaoMatriculaSubTurma() {
		if(situacaoMatriculaSubTurma == null){
			situacaoMatriculaSubTurma = "";
		}
		return situacaoMatriculaSubTurma;
	}

	public void setSituacaoMatriculaSubTurma(String situacaoMatriculaSubTurma) {
		this.situacaoMatriculaSubTurma = situacaoMatriculaSubTurma;
	} 
	
	public void consultarAlunoPreMatriculaGestaoTurmaDisciplina(){
		selecionarGestaoTurma();
		selecionarGestaoTurmaDisciplina();
		getFiltroRelatorioAcademicoVO().realizarDesmarcarTodasSituacoes();
		getFiltroRelatorioAcademicoVO().setPreMatricula(true);
		getFiltroRelatorioAcademicoVO().setDataInicio(null);
		getFiltroRelatorioAcademicoVO().setDataTermino(null);
		consultarAlunos();
	}
	
	public void consultarAlunoPreMatriculaGestaoSubTurma(){
		selecionarGestaoTurmaDisciplina();
		selecionarGestaoSubTurma();
		getFiltroRelatorioAcademicoVO().realizarDesmarcarTodasSituacoes();
		getFiltroRelatorioAcademicoVO().setPreMatricula(true);
		getFiltroRelatorioAcademicoVO().setDataInicio(null);
		getFiltroRelatorioAcademicoVO().setDataTermino(null);
		consultarAlunos();
	}
	
	public void consultarAlunoAtivoGestaoTurmaDisciplina(){
		selecionarGestaoTurma();
		selecionarGestaoTurmaDisciplina();
		getFiltroRelatorioAcademicoVO().realizarDesmarcarTodasSituacoes();
		getFiltroRelatorioAcademicoVO().setAtivo(true);
		getFiltroRelatorioAcademicoVO().setConcluido(true);
		getFiltroRelatorioAcademicoVO().setFormado(true);
		getFiltroRelatorioAcademicoVO().setDataInicio(null);
		getFiltroRelatorioAcademicoVO().setDataTermino(null);
		consultarAlunos();
	}
	
	public void consultarAlunoAtivoGestaoSubTurma(){
		getFiltroRelatorioAcademicoVO().realizarDesmarcarTodasSituacoes();
		getFiltroRelatorioAcademicoVO().setDataInicio(null);
		getFiltroRelatorioAcademicoVO().setDataTermino(null);
		getFiltroRelatorioAcademicoVO().setAtivo(true);
		getFiltroRelatorioAcademicoVO().setConcluido(true);
		getFiltroRelatorioAcademicoVO().setFormado(true);		
		selecionarGestaoTurmaDisciplina();
		selecionarGestaoSubTurma();
		consultarAlunos();
	}
	
	private void consultarAlunos(){
		try {
			setSemestre(getPeriodicidade().equals(PeriodicidadeEnum.ANUAL) ? "" : getSemestre());
			limparMensagem();
			getUnidadeEnsinoVOs().clear();
			getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(true);
			getUnidadeEnsinoVOs().add(getUnidadeEnsinoVO());
			setAlunosPorUnidadeCursoTurmaRelVOs(getFacadeFactory().getAlunosPorUnidadeCursoTurmaRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getCursoVOs(), getTurnoVOs(), getGestaoTurmaRelVO().getTurmaVO(), getGestaoTurmaDisciplinaRelVO().getDisciplinaVO(), 
					getAno(), getSemestre(), "", false, false, "todos", getConfiguracaoFinanceiroPadraoSistema(), getFiltroRelatorioAcademicoVO(), getUsuarioLogado(), "ambos", false, ""));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<AlunosPorUnidadeCursoTurmaRelVO> getAlunosPorUnidadeCursoTurmaRelVOs() {
		if(alunosPorUnidadeCursoTurmaRelVOs == null){
			alunosPorUnidadeCursoTurmaRelVOs = new ArrayList<AlunosPorUnidadeCursoTurmaRelVO>(0);
		}
		return alunosPorUnidadeCursoTurmaRelVOs;
	}

	public void setAlunosPorUnidadeCursoTurmaRelVOs(
			List<AlunosPorUnidadeCursoTurmaRelVO> alunosPorUnidadeCursoTurmaRelVOs) {
		this.alunosPorUnidadeCursoTurmaRelVOs = alunosPorUnidadeCursoTurmaRelVOs;
	}
	
	public void imprimirListaAlunosPdf(){
		imprimirListaAlunos(TipoRelatorioEnum.PDF, AlunosPorUnidadeCursoTurmaRel.getDesignIReportRelatorio());
	}
	
	public void imprimirListaAlunosExcel(){
		imprimirListaAlunos(TipoRelatorioEnum.EXCEL, AlunosPorUnidadeCursoTurmaRel.getDesignIReportRelatorioExcel());
	}
	
	public void imprimirListaAlunosSinteticoPdf(){
		imprimirListaAlunos(TipoRelatorioEnum.PDF, AlunosPorUnidadeCursoTurmaRel.getDesignIReportRelatorioSintetico());
	}
	
	public void imprimirListaAlunosSinteticoExcel(){
		imprimirListaAlunos(TipoRelatorioEnum.EXCEL, AlunosPorUnidadeCursoTurmaRel.getDesignIReportRelatorioSinteticoExcel());
	}
	
	public void imprimirListaAlunos(TipoRelatorioEnum tipoRelatorioEnum, String designer){
		try {
		if (!getAlunosPorUnidadeCursoTurmaRelVOs().isEmpty()) {
			getSuperParametroRelVO().limparParametros();
			getSuperParametroRelVO().setNomeDesignIreport(designer);
			getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
			getSuperParametroRelVO().setSubReport_Dir(AlunosPorUnidadeCursoTurmaRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos");
			getSuperParametroRelVO().setListaObjetos(getAlunosPorUnidadeCursoTurmaRelVOs());
			getSuperParametroRelVO().setQuantidade(0);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(AlunosPorUnidadeCursoTurmaRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setNomeEmpresa("");
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());			
			getSuperParametroRelVO().adicionarParametro("tipoAluno", "Todos");			
			getSuperParametroRelVO().adicionarParametro("ano", getAno());
			getSuperParametroRelVO().adicionarParametro("semestre", getSemestre());
			getSuperParametroRelVO().adicionarParametro("situacaoAlunoCurso", "Ambos");			
			getSuperParametroRelVO().adicionarParametro("disciplina", getGestaoTurmaDisciplinaRelVO().getDisciplinaVO().getNome());
			if(Uteis.isAtributoPreenchido(getUnidadeEnsinoVO().getCodigo())){
				setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
				getSuperParametroRelVO().adicionarParametro("unidadeEnsinoFiltro", getUnidadeEnsinoVO().getNome());
			}
			getSuperParametroRelVO().adicionarParametro("cursoFiltro", getCursosApresentar());
			getSuperParametroRelVO().adicionarParametro("turnoFiltro", getTurnosApresentar());
			getSuperParametroRelVO().adicionarParametro("disciplinaFiltro",getGestaoTurmaDisciplinaRelVO().getDisciplinaVO().getNome());			
			getSuperParametroRelVO().adicionarParametro("turmaFiltro", getGestaoTurmaRelVO().getTurmaVO().getIdentificadorTurma());
				realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
		} else {
			setMensagemID("msg_relatorio_sem_dados");
		}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if(listaConsultaDisciplina == null){
			listaConsultaDisciplina = new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	public String getCampoConsultaDisciplina() {
		if(campoConsultaDisciplina == null){
			campoConsultaDisciplina = "nome";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		if(valorConsultaDisciplina == null){
			valorConsultaDisciplina = "nome";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if(tipoConsultaComboDisciplina == null){
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}
	
	@SuppressWarnings("unchecked")
	public void consultarDisciplina(){
		try{
			if(getCampoConsultaDisciplina().equals("codigo")){
				Uteis.validarSomenteNumeroString(getValorConsultaDisciplina());
				setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(Integer.valueOf(getValorConsultaDisciplina()), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}else{
				setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarDisciplina(){
		setDisciplinaVO((DisciplinaVO)getRequestMap().get("disciplinaItem"));
	}
	
	public void limparDisciplina(){
		setDisciplinaVO(null);
	}

	public void setTipoConsultaComboDisciplina(List<SelectItem> tipoConsultaComboDisciplina) {
		this.tipoConsultaComboDisciplina = tipoConsultaComboDisciplina;
	}
		
	public void consultarTurma(){
		try{
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(), getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));			
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarTurmaPorIdentificadorTurma(){
		try{
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurmaEspecifico(getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			inicializarDadosTurma();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		}catch(Exception e){
			limparTurma();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private void inicializarDadosTurma(){
		setPeriodicidade(getTurmaVO().getAnual()?PeriodicidadeEnum.ANUAL: getTurmaVO().getSemestral() ? PeriodicidadeEnum.SEMESTRAL : PeriodicidadeEnum.INTEGRAL);
		setDisciplinaVO(null);
		setConsiderarFiltroSubTurma("filtrar");
		setConsiderarFiltroTurmaAgrupada("filtrar");
		setSituacaoMatricula("");
		setSituacaoMatriculaSubTurma("");
		setSituacaoMatriculaTurmaAgrupada("");
		setSituacaoProgramacaoAula("");
		setSituacaoProgramacaoAulaSubTurma("");
		setSituacaoProgramacaoAulaTurmaAgrupada("");
		setSituacaoTurma("");
		setSituacaoTurmaAgrupada("");
		setSituacaoTurmaSubTurma("");
		setSituacaoVaga("");
		setSituacaoVagaSubTurma("");
		setPeriodoLetivoDe(0);
		setPeriodoLetivoAte(0);
		montarListaSelectItemDisciplina();
		
	}
	
	public void selecionarTurma(){
		setTurmaVO((TurmaVO)getRequestMap().get("turmaItem"));
		inicializarDadosTurma();
	}
	
	public void limparTurma(){
		setTurmaVO(null);
		setDisciplinaVO(null);
	}
	
	public void montarListaSelectItemDisciplina(){
		try{
			setListaSelectDisciplina(UtilSelectItem.getListaSelectItem(getFacadeFactory().getDisciplinaFacade().consultarDisciplinaGradeEOptativaPorTurmaFazParteComposicao(
					getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()), "codigo", "nome"));
		}catch(Exception e){
			getListaSelectDisciplina().clear();
		}
	}

	public Integer getPeriodoLetivoDe() {
		if(periodoLetivoDe == null){
			periodoLetivoDe = 0;
		}
		return periodoLetivoDe;
	}

	public void setPeriodoLetivoDe(Integer periodoLetivoDe) {
		this.periodoLetivoDe = periodoLetivoDe;
	}

	public Integer getPeriodoLetivoAte() {
		if(periodoLetivoAte == null){
			periodoLetivoAte = 0;
		}
		return periodoLetivoAte;
	}

	public void setPeriodoLetivoAte(Integer periodoLetivoAte) {
		this.periodoLetivoAte = periodoLetivoAte;
	}

	public String getSituacaoMatriculaTurmaAgrupada() {
		if(situacaoMatriculaTurmaAgrupada == null){
			situacaoMatriculaTurmaAgrupada= "";
		}
		return situacaoMatriculaTurmaAgrupada;
	}

	public void setSituacaoMatriculaTurmaAgrupada(String situacaoMatriculaTurmaAgrupada) {
		this.situacaoMatriculaTurmaAgrupada = situacaoMatriculaTurmaAgrupada;
	}

	public List<SelectItem> getListaSelectSituacaoMatricula() {
		if(listaSelectSituacaoMatricula == null){
			listaSelectSituacaoMatricula = new ArrayList<SelectItem>(0);
			listaSelectSituacaoMatricula.add(new SelectItem("", ""));
			listaSelectSituacaoMatricula.add(new SelectItem("naoPossui", "Sem Alunos Matriculados"));
			listaSelectSituacaoMatricula.add(new SelectItem("possui", "Com Alunos Matriculados"));
		}
		return listaSelectSituacaoMatricula;
	}

	public void setListaSelectSituacaoMatricula(List<SelectItem> listaSelectSituacaoMatricula) {
		this.listaSelectSituacaoMatricula = listaSelectSituacaoMatricula;
	}

    public void imprimirPDF(){
    	try {    		
    		 setGestaoTurmaRelVOs(getFacadeFactory().getGestaoTurmaRelFacade().consultarDadosRelatorio(getUnidadeEnsinoVO(), getPeriodicidade(), getAno(), getSemestre(), getFiltrarPor(), getCursoVOs(), getTurnoVOs(), getTurmaVO(), getDisciplinaVO(), getSituacaoTurma(), getSituacaoTurmaSubTurma(), getSituacaoTurmaAgrupada(), getSituacaoProgramacaoAula(), getSituacaoProgramacaoAulaSubTurma(), getSituacaoProgramacaoAulaTurmaAgrupada(), getSituacaoVaga(), getSituacaoVagaSubTurma(),
    					getSituacaoMatricula(),getSituacaoMatriculaSubTurma(), getSituacaoMatriculaTurmaAgrupada(), getPeriodoLetivoDe(), getPeriodoLetivoAte(), getConsiderarFiltroSubTurma(), getConsiderarFiltroTurmaAgrupada(), getUsuarioLogado(), 0, 0));
			if (!getGestaoTurmaRelVOs().isEmpty()) {
				getSuperParametroRelVO().limparParametros();
				getSuperParametroRelVO().setNomeDesignIreport(GestaoTurmaRel.getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(GestaoTurmaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setTituloRelatorio("Gestão de Turmas");
				getSuperParametroRelVO().setListaObjetos(getGestaoTurmaRelVOs());
				getSuperParametroRelVO().setQuantidade(0);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(GestaoTurmaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());

				getSuperParametroRelVO().adicionarParametro("ano", getAno());
				getSuperParametroRelVO().adicionarParametro("semestre", getSemestre());
				getSuperParametroRelVO().adicionarParametro("disciplina", getDisciplinaVO().getNome());
				getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(getUnidadeEnsinoVO(), NivelMontarDados.BASICO, getUsuarioLogado());
				if(Uteis.isAtributoPreenchido(getUnidadeEnsinoVO().getCodigo())){
					setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
					getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				}
				if(getFiltrarPor().equals("curso")){
					getSuperParametroRelVO().adicionarParametro("curso", getCursosApresentar());
					getSuperParametroRelVO().adicionarParametro("turno", getTurnosApresentar());
				}
				getSuperParametroRelVO().adicionarParametro("periodicidade", getPeriodicidade().getDescricao());
				getSuperParametroRelVO().adicionarParametro("periodoLetivoDe", getPeriodoLetivoDe());
				getSuperParametroRelVO().adicionarParametro("periodoLetivoAte", getPeriodoLetivoAte());
				getSuperParametroRelVO().adicionarParametro("situacaoTurmaBase", getSituacaoTurmaApresentar(getSituacaoTurma()));
				getSuperParametroRelVO().adicionarParametro("situacaoTurmaSubTurma", getSituacaoTurmaApresentar(getSituacaoTurmaSubTurma()));
				getSuperParametroRelVO().adicionarParametro("situacaoTurmaAgrupada", getSituacaoTurmaApresentar(getSituacaoTurmaAgrupada()));
				getSuperParametroRelVO().adicionarParametro("situacaoAulaTurmaBase", getSituacaoAulaApresentar(getSituacaoProgramacaoAula()));
				getSuperParametroRelVO().adicionarParametro("situacaoAulaSubTurma", getSituacaoAulaApresentar(getSituacaoProgramacaoAulaSubTurma()));
				getSuperParametroRelVO().adicionarParametro("situacaoAulaTurmaAgrupada", getSituacaoAulaApresentar(getSituacaoProgramacaoAulaTurmaAgrupada()));					
				getSuperParametroRelVO().adicionarParametro("situacaoVagaTurmaBase", getSituacaoVagaApresentar(getSituacaoVaga()));					
				getSuperParametroRelVO().adicionarParametro("situacaoVagaSubTurma", getSituacaoVagaApresentar(getSituacaoVagaSubTurma()));
				getSuperParametroRelVO().adicionarParametro("situacaoMatriculaTurmaBase", getSituacaoMatriculaApresentar("Possui"));
				getSuperParametroRelVO().adicionarParametro("situacaoMatriculaSubTurma", getSituacaoMatriculaApresentar("Possui"));
				getSuperParametroRelVO().adicionarParametro("situacaoMatriculaTurmaAgrupada", getSituacaoMatriculaApresentar(getSituacaoMatriculaTurmaAgrupada()));
				getSuperParametroRelVO().adicionarParametro("apresentarApenasDisciplinaComSubTurma", getConsiderarFiltroSubTurmaApresentar());
				getSuperParametroRelVO().adicionarParametro("apresentarApenasDisciplinaComTurmaAgrupada", getConsiderarFiltroTurmaAgrupadaApresentar());

					if(getFiltrarPor().equals("turma")){
					getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
				}
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
    		} catch (Exception e) {
    			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
    		}
    }
    
    private String getSituacaoMatriculaApresentar(String situacao){
    	if(situacao.equals("possui")){
    		return "Com Alunos Matriculados";
    	}else if(situacao.equals("naoPossui")){
    		return "Sem Alunos Matriculados";
    	}else if(situacao.equals("possuiMatriculaSemDistribuicao")){
    		return "Possui Aluno Não Distribuído";
    	}
    	return "";
    }
    private String getSituacaoAulaApresentar(String situacao){
    	if(situacao.equals("possui")){
    		return "Com Aula Programada";
    	}else if(situacao.equals("naoPossui")){
    		return "Sem Aula Programada";
    	}else if(situacao.equals("aulaNaoRegistrada")){
    		return "Aula Não Registrada";
    	}
    	return "";
    }
    
    private String getSituacaoTurmaApresentar(String situacao){
    	if(situacao.equals("AB")){
    		return "Aberta";
    	}else if(situacao.equals("FE")){
    		return "Fechada";
    	}
    	return "";
    }
    
    private String getSituacaoVagaApresentar(String situacao){
    	String situacaoVagaApresentar = "";
    	if(situacao.equals("possui")){
			situacaoVagaApresentar = "Possui";
		}else if(situacao.equals("possui")){
			situacaoVagaApresentar = "Não Possui";
		}else if(situacao.equals("vagaPreenchida")){
			situacaoVagaApresentar = "Vaga Preenchida";
		}else if(situacao.equals("vagaAcima")){
			situacaoVagaApresentar = "Vaga Acima Limite Permitido";
		}else if(situacao.equals("vagaDisponivel")){
			situacaoVagaApresentar = "Vaga Disponível";
		}
    	return situacaoVagaApresentar;
    }
    
    private String getConsiderarFiltroTurmaAgrupadaApresentar(){
    	if(getConsiderarFiltroTurmaAgrupada().equals("filtrar")){
    		return "Filtrar";
    	} else if(getConsiderarFiltroTurmaAgrupada().equals("naofiltrar")){
    			return "Não Filtrar";
    	} else if(getConsiderarFiltroTurmaAgrupada().equals("apenas")){
    		return "Apenas Turma Agrupada";
    	}
    	return "";
    }
    private String getConsiderarFiltroSubTurmaApresentar(){
    	if(getConsiderarFiltroSubTurma().equals("filtrar")){
    		return "Filtrar";
    	} else if(getConsiderarFiltroSubTurma().equals("naofiltrar")){
    		return "Não Filtrar";
    	} else if(getConsiderarFiltroSubTurma().equals("apenas")){
    		return "Apenas Subturma";
    	}
    	return "";
    }

    public Boolean getDesabilitarPeriodicidade(){
    	return (getFiltrarPor().equals("turma") && Uteis.isAtributoPreenchido(getTurmaVO())) || (getFiltrarPor().equals("curso") && !getCursosApresentar().trim().isEmpty());     
    }
    
    public void realizarDesmarcacaoDisciplinaTurmaAgrupada(){
    	if(getConsiderarFiltroTurmaAgrupada().equals("apenas")){
    		setConsiderarFiltroSubTurma("naofiltrar");
    		setSituacaoMatriculaSubTurma("");
    		setSituacaoProgramacaoAulaSubTurma("");
    		setSituacaoVagaSubTurma("");
    		setSituacaoTurmaSubTurma("");
    		setSituacaoMatricula("");
    		setSituacaoProgramacaoAula("");
    		setSituacaoTurma("");
    		setSituacaoVaga("");
    	}
    	if(getConsiderarFiltroTurmaAgrupada().equals("naofiltrar")){
    		setSituacaoProgramacaoAulaTurmaAgrupada("");    		
    		setSituacaoTurmaAgrupada("");
    	}
    }
    
    public void realizarDesmarcacaoDisciplinaSubTurma(){
    	if(getConsiderarFiltroSubTurma().equals("apenas")){
    		setConsiderarFiltroTurmaAgrupada("naofiltrar");    		
    		setSituacaoProgramacaoAulaTurmaAgrupada("");    		
    		setSituacaoTurmaAgrupada("");
    		setSituacaoMatricula("");
    		setSituacaoProgramacaoAula("");
    		setSituacaoTurma("");
    		setSituacaoVaga("");
    	}
    	
    	if(getConsiderarFiltroSubTurma().equals("naofiltrar")){
    		setSituacaoMatriculaSubTurma("");
    		setSituacaoProgramacaoAulaSubTurma("");
    		setSituacaoVagaSubTurma("");
    		setSituacaoTurmaSubTurma("");
    	}    	
    }
        

	public List<SelectItem> getListaSelectItemConsiderarFiltroTurmaAgrupada() {
		if(listaSelectItemConsiderarFiltroTurmaAgrupada== null){
			listaSelectItemConsiderarFiltroTurmaAgrupada = new ArrayList<SelectItem>(0);
			listaSelectItemConsiderarFiltroTurmaAgrupada.add(new SelectItem("filtrar", "Filtrar"));
			listaSelectItemConsiderarFiltroTurmaAgrupada.add(new SelectItem("naofiltrar", "Não Filtrar"));
			listaSelectItemConsiderarFiltroTurmaAgrupada.add(new SelectItem("apenas", "Apenas Turma Agrupada"));
		}
		return listaSelectItemConsiderarFiltroTurmaAgrupada;
	}

	public void setListaSelectItemConsiderarFiltroTurmaAgrupada(
			List<SelectItem> listaSelectItemConsiderarFiltroTurmaAgrupada) {
		this.listaSelectItemConsiderarFiltroTurmaAgrupada = listaSelectItemConsiderarFiltroTurmaAgrupada;
	}

	public List<SelectItem> getListaSelectItemConsiderarFiltroSubTurma() {
		if(listaSelectItemConsiderarFiltroSubTurma== null){
			listaSelectItemConsiderarFiltroSubTurma = new ArrayList<SelectItem>(0);
			listaSelectItemConsiderarFiltroSubTurma.add(new SelectItem("filtrar", "Filtrar"));
			listaSelectItemConsiderarFiltroSubTurma.add(new SelectItem("naofiltrar", "Não Filtrar"));
			listaSelectItemConsiderarFiltroSubTurma.add(new SelectItem("apenas", "Apenas Subturma"));
		}
		return listaSelectItemConsiderarFiltroSubTurma;
	}

	public void setListaSelectItemConsiderarFiltroSubTurma(List<SelectItem> listaSelectItemConsiderarFiltroSubTurma) {
		this.listaSelectItemConsiderarFiltroSubTurma = listaSelectItemConsiderarFiltroSubTurma;
	}

	public String getConsiderarFiltroTurmaAgrupada() {
		if(considerarFiltroTurmaAgrupada == null){
			considerarFiltroTurmaAgrupada = "filtrar";
		}
		return considerarFiltroTurmaAgrupada;
	}

	public void setConsiderarFiltroTurmaAgrupada(String considerarFiltroTurmaAgrupada) {
		this.considerarFiltroTurmaAgrupada = considerarFiltroTurmaAgrupada;
	}

	public String getConsiderarFiltroSubTurma() {
		if(considerarFiltroSubTurma == null){
			considerarFiltroSubTurma = "filtrar";
		}
		return considerarFiltroSubTurma;
	}

	public void setConsiderarFiltroSubTurma(String considerarFiltroSubTurma) {
		this.considerarFiltroSubTurma = considerarFiltroSubTurma;
	}	    
    

	public void realizarApresentacaoDisciplinaTurma(){
		GestaoTurmaRelVO gestaoTurmaRelVO = ((GestaoTurmaRelVO) getRequestMap().get("gestaoTurma"));
		gestaoTurmaRelVO.getTurmaVO().setTurmaSelecionada(!gestaoTurmaRelVO.getTurmaVO().getTurmaSelecionada());
		
	}
	
	public List<LayoutPadraoVO> getLayoutPadraoVOs() {
		return layoutPadraoVOs;
	}

	public void setLayoutPadraoVOs(List<LayoutPadraoVO> layoutPadraoVOs) {
		this.layoutPadraoVOs = layoutPadraoVOs;
	}
	
	@PostConstruct
	public void consultarLayoutPadrao(){
		setLayoutPadraoVOs(getFacadeFactory().getLayoutPadraoFacade().consultarAgrupadoresPorEntidade(GestaoTurmaRel.class.getSimpleName()));
	}

	public void persistirFiltros(){
		try{
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadraoComAgrupador(getConsiderarFiltroSubTurma(), GestaoTurmaRel.class.getSimpleName(), "considerarFiltroSubTurma", getNomeAgrupadorFiltros(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadraoComAgrupador(getConsiderarFiltroTurmaAgrupada(), GestaoTurmaRel.class.getSimpleName(), "considerarFiltroTurmaAgrupada", getNomeAgrupadorFiltros(), getUsuarioLogado());
			
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadraoComAgrupador(getSituacaoTurma(), GestaoTurmaRel.class.getSimpleName(), "situacaoTurma", getNomeAgrupadorFiltros(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadraoComAgrupador(getSituacaoTurmaAgrupada(), GestaoTurmaRel.class.getSimpleName(), "situacaoTurmaAgrupada", getNomeAgrupadorFiltros(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadraoComAgrupador(getSituacaoTurmaSubTurma(), GestaoTurmaRel.class.getSimpleName(), "situacaoTurmaSubTurma", getNomeAgrupadorFiltros(), getUsuarioLogado());
			
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadraoComAgrupador(getSituacaoProgramacaoAula(), GestaoTurmaRel.class.getSimpleName(), "situacaoProgramacaoAula", getNomeAgrupadorFiltros(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadraoComAgrupador(getSituacaoProgramacaoAulaSubTurma(), GestaoTurmaRel.class.getSimpleName(), "situacaoProgramacaoAulaSubTurma", getNomeAgrupadorFiltros(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadraoComAgrupador(getSituacaoProgramacaoAulaTurmaAgrupada(), GestaoTurmaRel.class.getSimpleName(), "situacaoProgramacaoAulaTurmaAgrupada", getNomeAgrupadorFiltros(), getUsuarioLogado());
			
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadraoComAgrupador(getSituacaoMatricula(), GestaoTurmaRel.class.getSimpleName(), "situacaoMatricula", getNomeAgrupadorFiltros(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadraoComAgrupador(getSituacaoMatriculaSubTurma(), GestaoTurmaRel.class.getSimpleName(), "situacaoMatriculaSubTurma", getNomeAgrupadorFiltros(), getUsuarioLogado());
			
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadraoComAgrupador(getSituacaoVaga(), GestaoTurmaRel.class.getSimpleName(), "situacaoVaga", getNomeAgrupadorFiltros(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadraoComAgrupador(getSituacaoVagaSubTurma(), GestaoTurmaRel.class.getSimpleName(), "situacaoVagaSubTurma", getNomeAgrupadorFiltros(), getUsuarioLogado());
			
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadraoComAgrupador(getPeriodoLetivoDe().toString(), GestaoTurmaRel.class.getSimpleName(), "periodoLetivoDe", getNomeAgrupadorFiltros(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadraoComAgrupador(getPeriodoLetivoAte().toString(), GestaoTurmaRel.class.getSimpleName(), "periodoLetivoAte", getNomeAgrupadorFiltros(), getUsuarioLogado());
			consultarLayoutPadrao();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);			
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String getNomeAgrupadorFiltros() {
		if(nomeAgrupadorFiltros == null){
			nomeAgrupadorFiltros = "";
		}
		return nomeAgrupadorFiltros;
	}

	public void setNomeAgrupadorFiltros(String nomeAgrupadorFiltros) {
		this.nomeAgrupadorFiltros = nomeAgrupadorFiltros;
	} 
	
	
	private void persistirFiltrosBasicos(){
		try{
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getPeriodicidade().getValor(), GestaoTurmaRel.class.getSimpleName(), "periodicidade", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getAno(), GestaoTurmaRel.class.getSimpleName(), "ano", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSemestre(), GestaoTurmaRel.class.getSimpleName(), "semestre", getUsuarioLogado());			
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}  
	
	@PostConstruct
	private void carregarFiltrosBasicos(){
		try{
			Map<String, String> mapFiltros = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[]{"periodicidade",  "ano", "semestre"}, GestaoTurmaRel.class.getSimpleName());
			realizarVinculoFiltros(mapFiltros);
			limparMensagem();
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}  
	
	public void excluirFiltros(){		
		try{			
			getFacadeFactory().getLayoutPadraoFacade().excluirLayoutPorAgrupadorEntidade(GestaoTurmaRel.class.getSimpleName(), ((LayoutPadraoVO)getRequestMap().get("layout")).getAgrupador(), getUsuarioLogado());
			consultarLayoutPadrao();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarFiltros(){
		setNomeAgrupadorFiltros(((LayoutPadraoVO)getRequestMap().get("layout")).getAgrupador());
		realizarVinculoFiltros(((LayoutPadraoVO)getRequestMap().get("layout")).getMapCampoValores());
		realizarDesmarcacaoDisciplinaSubTurma();
		realizarDesmarcacaoDisciplinaTurmaAgrupada();
	}
	
	public void realizarVinculoFiltros(Map<String, String> mapFiltros){
		if(mapFiltros == null || mapFiltros.isEmpty()){
			return;
		}
		if(mapFiltros.containsKey("ano") && !mapFiltros.get("ano").trim().isEmpty()){
			setAno(mapFiltros.get("ano"));
		}
		if(mapFiltros.containsKey("semestre") && !mapFiltros.get("semestre").trim().isEmpty()){
			setSemestre(mapFiltros.get("semestre"));
		}
		if(mapFiltros.containsKey("periodicidade") && !mapFiltros.get("periodicidade").trim().isEmpty()){
			setPeriodicidade(PeriodicidadeEnum.getEnumPorValor(mapFiltros.get("periodicidade")));
		}
		if(mapFiltros.containsKey("considerarFiltroSubTurma")){
			setConsiderarFiltroSubTurma(mapFiltros.get("considerarFiltroSubTurma"));
		}
		if(mapFiltros.containsKey("considerarFiltroTurmaAgrupada")){
			setConsiderarFiltroTurmaAgrupada(mapFiltros.get("considerarFiltroTurmaAgrupada"));
		}		
		if(mapFiltros.containsKey("situacaoTurma")){
			setSituacaoTurma(mapFiltros.get("situacaoTurma"));
		}
		if(mapFiltros.containsKey("situacaoTurmaAgrupada")){
			setSituacaoTurmaAgrupada(mapFiltros.get("situacaoTurmaAgrupada"));
		}
		if(mapFiltros.containsKey("situacaoTurmaSubTurma")){
			setSituacaoTurmaSubTurma(mapFiltros.get("situacaoTurmaSubTurma"));
		}
		if(mapFiltros.containsKey("situacaoProgramacaoAula")){
			setSituacaoProgramacaoAula(mapFiltros.get("situacaoProgramacaoAula"));
		}
		if(mapFiltros.containsKey("situacaoProgramacaoAulaTurmaAgrupada")){
			setSituacaoProgramacaoAulaTurmaAgrupada(mapFiltros.get("situacaoProgramacaoAulaTurmaAgrupada"));
		}
		if(mapFiltros.containsKey("situacaoProgramacaoAulaSubTurma")){
			setSituacaoProgramacaoAulaSubTurma(mapFiltros.get("situacaoProgramacaoAulaSubTurma"));
		}
		
		if(mapFiltros.containsKey("situacaoMatricula")){
			setSituacaoMatricula(mapFiltros.get("situacaoMatricula"));
		}
		if(mapFiltros.containsKey("situacaoMatriculaSubTurma")){
			setSituacaoMatriculaSubTurma(mapFiltros.get("situacaoMatriculaSubTurma"));
		}
		if(mapFiltros.containsKey("situacaoVaga")){
			setSituacaoVaga(mapFiltros.get("situacaoVaga"));
		}
		if(mapFiltros.containsKey("situacaoVagaSubTurma")){
			setSituacaoVagaSubTurma(mapFiltros.get("situacaoVagaSubTurma"));
		}
		if(mapFiltros.containsKey("periodoLetivoDe")  && Uteis.getIsValorNumerico(mapFiltros.get("periodoLetivoDe"))){
			setPeriodoLetivoDe(Integer.valueOf(mapFiltros.get("periodoLetivoDe")));
		}
		if(mapFiltros.containsKey("periodoLetivoAte") && Uteis.getIsValorNumerico(mapFiltros.get("periodoLetivoAte"))){
			setPeriodoLetivoAte(Integer.valueOf(mapFiltros.get("periodoLetivoAte")));
		}
		
	}
	
}
