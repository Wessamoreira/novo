package controle.ead;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.ead.GraficoAproveitamentoAssuntoPBLVO;
import negocio.comuns.ead.MonitorConhecimentoPBLVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * 
 * @author Pedro Andrade 09/01/2017
 *
 */
@Controller("MonitorConhecimentoPBLControle")
@RequestMapping("/monitoramentopbl")
public class MonitorConhecimentoPBLControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1673048522995240711L;
	private MonitorConhecimentoPBLVO monitorConhecimentoPBLVO;
	private GraficoAproveitamentoAssuntoPBLVO graficoAproveitamentoAssuntoPBLVO;
	List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina;

	@PostConstruct
	public void init() {
		if (Uteis.isAtributoPreenchido((Boolean) context().getExternalContext().getSessionMap().get("navegarGestaoEventoMonitoramentoPbl"))) {
			carregarDadosGestaoEventoConteudoTurma();
		}
		setMensagemID("msg_dados_parametroConsulta", Uteis.ALERTA);
	}

	public void carregarDadosGestaoEventoConteudoTurma() {
		try {

			getMonitorConhecimentoPBLVO().getTurmaVO().setCodigo((Integer) context().getExternalContext().getSessionMap().get("turmaGestao"));
			getMonitorConhecimentoPBLVO().setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getMonitorConhecimentoPBLVO().getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getMonitorConhecimentoPBLVO().getDisciplinaVO().setCodigo((Integer) context().getExternalContext().getSessionMap().get("disciplinaGestao"));
			getMonitorConhecimentoPBLVO().setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getMonitorConhecimentoPBLVO().getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getMonitorConhecimentoPBLVO().setConteudoVO((ConteudoVO) context().getExternalContext().getSessionMap().get("conteudoGestao"));
			getMonitorConhecimentoPBLVO().setConteudoVO(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(getMonitorConhecimentoPBLVO().getConteudoVO().getCodigo(), NivelMontarDados.COMBOBOX, false, getUsuarioLogado()));
			getMonitorConhecimentoPBLVO().setAno((String) context().getExternalContext().getSessionMap().get("anoGestao"));
			getMonitorConhecimentoPBLVO().setSemestre((String) context().getExternalContext().getSessionMap().get("semestreGestao"));
			context().getExternalContext().getSessionMap().remove("navegarGestaoEventoMonitoramentoPbl");
			context().getExternalContext().getSessionMap().remove("conteudoGestao");
			context().getExternalContext().getSessionMap().remove("turmaGestao");
			context().getExternalContext().getSessionMap().remove("anoGestao");
			context().getExternalContext().getSessionMap().remove("semestreGestao");
			context().getExternalContext().getSessionMap().remove("disciplinaGestao");
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String consultar() {
		try {
			getMonitorConhecimentoPBLVO().getListaGraficoAproveitamentoAssuntoPBLVOs().clear();
			getFacadeFactory().getMonitorConhecimentoPBLFacade().montarGraficoAproveitamentoAssuntoPBLVO(getMonitorConhecimentoPBLVO(), false, getUsuarioLogado());			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public void visualizarMonitorConhecimentoPorNota() {
		try {
			setGraficoAproveitamentoAssuntoPBLVO((GraficoAproveitamentoAssuntoPBLVO) context().getExternalContext().getRequestMap().get("graficoAproveitamentoAssuntoPBLVO"));
			getGraficoAproveitamentoAssuntoPBLVO().setSerieGraficoBarraPorNotaEmJavaScript("");
			getGraficoAproveitamentoAssuntoPBLVO().setCategoriaGraficoBarraPorNotaEmJavaScript("");
			getGraficoAproveitamentoAssuntoPBLVO().setGraficoBarraPorNotaEmJavaScript(null);
			getFacadeFactory().getMonitorConhecimentoPBLFacade().montarGraficoAproveitamentoAssuntoPorTipoNota(getGraficoAproveitamentoAssuntoPBLVO(), false, getUsuarioLogado());
			getGraficoAproveitamentoAssuntoPBLVO().setParametroWidthGrafico(650);
			setMensagemID("", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void visualizarMonitorConhecimentoPorOutraTurma() {
		try {
			setGraficoAproveitamentoAssuntoPBLVO((GraficoAproveitamentoAssuntoPBLVO) context().getExternalContext().getRequestMap().get("graficoAproveitamentoAssuntoPBLVO"));
			getGraficoAproveitamentoAssuntoPBLVO().setSerieGraficoPizzaOutraTurmaEmJavaScript("");
			getGraficoAproveitamentoAssuntoPBLVO().setSerieGraficoBarraOutraTurmaEmJavaScript("");
			getGraficoAproveitamentoAssuntoPBLVO().setGraficoPizzaOutraTurmaEmJavaScript(null);
			getGraficoAproveitamentoAssuntoPBLVO().setGraficoBarraOutraTurmaEmJavaScript(null);
			getFacadeFactory().getMonitorConhecimentoPBLFacade().montarGraficoAproveitamentoAssuntoOutraTurma(getGraficoAproveitamentoAssuntoPBLVO(), true, getUsuarioLogado());
			getGraficoAproveitamentoAssuntoPBLVO().setParametroWidthGrafico(350);
			setMensagemID("", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	@ResponseBody    
    @RequestMapping(value = "/consultarAlunoPblGrafico/{origem}/{codigoparametro}/{conteudo}/{turma}/{disciplina}/{ano}/{semestre}/{tema}/{cupre}", method = RequestMethod.POST)
    public void consultarAlunoPblGrafico(@PathVariable String origem, @PathVariable Integer codigoparametro, @PathVariable Integer conteudo, @PathVariable Integer turma, @PathVariable Integer disciplina, @PathVariable String ano, @PathVariable String semestre, @PathVariable Integer tema, @PathVariable String cupre, HttpSession session) {
		try {
			MonitorConhecimentoPBLVO obj = new MonitorConhecimentoPBLVO();
			obj.getConteudoVO().setCodigo(conteudo);
			obj.getTurmaVO().setCodigo(turma);
			obj.getDisciplinaVO().setCodigo(disciplina);
			obj.setAno(ano);
			obj.setSemestre(semestre);			
			setListaMatriculaPeriodoTurmaDisciplina(getFacadeFactory().getMonitorConhecimentoPBLFacade().montarAlunosGestaoEventoContudoTurmaAvaliacao(obj, origem, cupre, codigoparametro, tema));
			session.setAttribute("listaMatriculaPeriodoTurmaDisciplina", getListaMatriculaPeriodoTurmaDisciplina());
			//context().getExternalContext().getSessionMap().put("listaMatriculaPeriodoTurmaDisciplina", getListaMatriculaPeriodoTurmaDisciplina());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}        
    }
	
	public void preencherListaMatriculaPeriodoTurmaDisciplina() {
		try {		
			getListaMatriculaPeriodoTurmaDisciplina().clear();
			setListaMatriculaPeriodoTurmaDisciplina((List<MatriculaPeriodoTurmaDisciplinaVO>) context().getExternalContext().getSessionMap().get("listaMatriculaPeriodoTurmaDisciplina"));
			context().getExternalContext().getSessionMap().remove("listaMatriculaPeriodoTurmaDisciplina");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}        
	}

	public String navegarParaGestaoEventoConteudo() {
		try {
			context().getExternalContext().getSessionMap().put("navegarGestaoEventoMonitoramentoPbl", true);
			context().getExternalContext().getSessionMap().put("turmaGestao", getMonitorConhecimentoPBLVO().getTurmaVO().getCodigo());
			context().getExternalContext().getSessionMap().put("anoGestao", getMonitorConhecimentoPBLVO().getAno());
			context().getExternalContext().getSessionMap().put("semestreGestao", getMonitorConhecimentoPBLVO().getSemestre());
			context().getExternalContext().getSessionMap().put("disciplinaGestao", getMonitorConhecimentoPBLVO().getDisciplinaVO().getCodigo());
			context().getExternalContext().getSessionMap().put("conteudoGestao", getMonitorConhecimentoPBLVO().getConteudoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("gestaoEventoConteudoCons.xhtml");
	}

	public MonitorConhecimentoPBLVO getMonitorConhecimentoPBLVO() {
		if (monitorConhecimentoPBLVO == null) {
			monitorConhecimentoPBLVO = new MonitorConhecimentoPBLVO();
		}
		return monitorConhecimentoPBLVO;
	}

	public void setMonitorConhecimentoPBLVO(MonitorConhecimentoPBLVO monitorConhecimentoPBLVO) {
		this.monitorConhecimentoPBLVO = monitorConhecimentoPBLVO;
	}

	public GraficoAproveitamentoAssuntoPBLVO getGraficoAproveitamentoAssuntoPBLVO() {
		if (graficoAproveitamentoAssuntoPBLVO == null) {
			graficoAproveitamentoAssuntoPBLVO = new GraficoAproveitamentoAssuntoPBLVO();
		}
		return graficoAproveitamentoAssuntoPBLVO;
	}

	public void setGraficoAproveitamentoAssuntoPBLVO(GraficoAproveitamentoAssuntoPBLVO graficoAproveitamentoAssuntoPBLVO) {
		this.graficoAproveitamentoAssuntoPBLVO = graficoAproveitamentoAssuntoPBLVO;
	}
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaMatriculaPeriodoTurmaDisciplina() {
		if(listaMatriculaPeriodoTurmaDisciplina == null){
			listaMatriculaPeriodoTurmaDisciplina = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>();
		}
		return listaMatriculaPeriodoTurmaDisciplina;
	}

	public void setListaMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina) {
		this.listaMatriculaPeriodoTurmaDisciplina = listaMatriculaPeriodoTurmaDisciplina;
	}
	

}
