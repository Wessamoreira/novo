package controle.academico;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.RegistroAtividadeComplementarMatriculaVO;
import negocio.comuns.academico.RegistroAtividadeComplementarVO;
import negocio.comuns.academico.TipoAtividadeComplementarVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.academico.RegistroAtividadeComplementarMatricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("RegistroAtividadeComplementarControle")
@Scope("viewScope")
@Lazy
public class RegistroAtividadeComplementarControle extends SuperControle implements Serializable {

	private RegistroAtividadeComplementarVO registroAtividadeComplementarVO;
	private RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO;
	private RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaTempVO;
	private List<SelectItem> listaSelectItemTipoAtividadeComplementar;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<SelectItem> tipoConsultaComboCurso;
	private List<SelectItem> tipoConsultaComboTurma;
	private List<TurmaVO> listaConsultaTurma;
	private TurmaVO turmaVO;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private List<SelectItem> listaSelectItemHistoricoAluno;
	private String campoConsultaAtividadeComplementar;
	private String valorConsultaAtividadeComplementar;
	private List<TipoAtividadeComplementarVO> listaConsultaAtividadeComplementar;
	private List<SelectItem> tipoConsultaComboAtividadeComplementar;
	

	private String campoConsultaNomeEvento;
	private String campoConsultaLocal;
	private String campoConsultaMatricula;
	private String campoConsultaInstituicaoResponsavel;
	private Date campoConsultaDataInicio;
	private Date campoConsultaDataFinal;

	private String campoSemestre;
	private String campoAno;
	private String campoSituacao;

	private List<RegistroAtividadeComplementarVO> listaConsultaRegistroAtividadeComplementarVOs;
	
	private String usernameLiberarOperacaoFuncionalidade;
	private String senhaLiberarOperacaoFuncionalidade;
	private String oncompleteOperacaoFuncionalidade;
	private String tipoOperacaoFuncionalidadeLiberar;
	private boolean liberarTodosRegistroAtividadesComplementar = false;
	private List<TipoAtividadeComplementarVO> listaTipoAtividadeComplementarLiberacaoOperacao;
	private Integer totalCargaHoraMaximaAtividade;
	private String unidadeEnsinoApresentar;
	private String cursoApresentar;
	private List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs;
	private static final long serialVersionUID = 1L;

	public RegistroAtividadeComplementarControle() throws Exception {
		super();
		this.montarListaSelectItemTipoAtividadeComplementar();
		montarListaUnidadeEnsino();
	}

	public String novo() throws Exception {
		removerObjetoMemoria(this);
		this.setRegistroAtividadeComplementarVO(null);
		montarListaSelectItemTipoAtividadeComplementar();
		this.setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarForm.xhtml");
	}

	public String novoCoordenador() throws Exception {
		removerObjetoMemoria(this);
		this.setRegistroAtividadeComplementarVO(null);
		this.getRegistroAtividadeComplementarVO().setCoordenador(getUsuarioLogado().getPessoa().getCodigo());
		montarListaSelectItemTipoAtividadeComplementar();
		this.setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarCoordenador.xhtml");
	}

	public String editar() {
		try {
			RegistroAtividadeComplementarMatriculaVO obj = (RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarItens");
			getRegistroAtividadeComplementarVO().setCodigo(obj.getRegistroAtividadeComplementar().getCodigo());
			List<RegistroAtividadeComplementarMatriculaVO> listaAtividadeComplementarMatricula = (getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarPorRegistroAtividadeComplementar(obj.getRegistroAtividadeComplementar().getCodigo(),null, false, null, null, getUsuarioLogado()));
			getRegistroAtividadeComplementarVO().setListaRegistroAtividadeComplementarMatriculaVOs(listaAtividadeComplementarMatricula);
			montarListaSelectItemTipoAtividadeComplementar();
			getRegistroAtividadeComplementarVO().setNovoObj(Boolean.FALSE);
			this.setMensagemID("msg_dados_editar");
			if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarCoordenador.xhtml");	
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarForm.xhtml");
		} catch (Exception e) {
			this.setMensagemDetalhada("msg_erro", e.getMessage());
			if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarCoordenador.xhtml");	
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarForm.xhtml");
		}
	}

	public String gravar() {
		try {
			getFacadeFactory().getRegistroAtividadeComplementarFacade().validarTipoAtividadeComplementar(this.getRegistroAtividadeComplementarVO().getListaRegistroAtividadeComplementarMatriculaVOs(), getUsuario());
			RegistroAtividadeComplementarMatricula.validarDadosLista(this.getRegistroAtividadeComplementarVO());
			if (getRegistroAtividadeComplementarVO().getNovoObj()) {
				getFacadeFactory().getRegistroAtividadeComplementarFacade().incluir(this.getRegistroAtividadeComplementarVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			} else {
				getFacadeFactory().getRegistroAtividadeComplementarFacade().alterar(this.getRegistroAtividadeComplementarVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			}
			this.setMensagemID("msg_dados_gravados");
			for (RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO : getRegistroAtividadeComplementarVO().getListaRegistroAtividadeComplementarMatriculaVOs()) {
				registroAtividadeComplementarMatriculaVO.getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR);
				registroAtividadeComplementarMatriculaVO.setCaminhoArquivoWeb("abrirPopup('"+getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(registroAtividadeComplementarMatriculaVO.getArquivoVO(), registroAtividadeComplementarMatriculaVO.getArquivoVO().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema())+"', '"+registroAtividadeComplementarMatriculaVO.getMatriculaVO().getMatricula()+"', 100, 100);");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarForm.xhtml");
		} catch (Exception e) {
			this.setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarForm.xhtml");
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getRegistroAtividadeComplementarFacade().excluir(this.getRegistroAtividadeComplementarVO(), getUsuarioLogado());
			this.setRegistroAtividadeComplementarVO(new RegistroAtividadeComplementarVO());
			this.setTurmaVO(new TurmaVO());
			this.setRegistroAtividadeComplementarMatriculaVO(new RegistroAtividadeComplementarMatriculaVO());
			this.setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarForm.xhtml");
		} catch (Exception e) {
			this.setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarForm.xhtml");
		}
	}

	public RegistroAtividadeComplementarVO getRegistroAtividadeComplementarVO() {
		if (this.registroAtividadeComplementarVO == null) {
			this.registroAtividadeComplementarVO = new RegistroAtividadeComplementarVO();
		}
		return this.registroAtividadeComplementarVO;
	}

	public void setRegistroAtividadeComplementarVO(RegistroAtividadeComplementarVO registroAtividadeComplementarVO) {
		this.registroAtividadeComplementarVO = registroAtividadeComplementarVO;
	}

	public RegistroAtividadeComplementarMatriculaVO getRegistroAtividadeComplementarMatriculaVO() {
		if (this.registroAtividadeComplementarMatriculaVO == null) {
			this.registroAtividadeComplementarMatriculaVO = new RegistroAtividadeComplementarMatriculaVO();
		}
		return this.registroAtividadeComplementarMatriculaVO;
	}

	public void setRegistroAtividadeComplementarMatriculaVO(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO) {
		this.registroAtividadeComplementarMatriculaVO = registroAtividadeComplementarMatriculaVO;
	}

	public List<SelectItem> getListaSelectItemTipoAtividadeComplementar() {
		if (this.listaSelectItemTipoAtividadeComplementar == null) {
			this.listaSelectItemTipoAtividadeComplementar = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoAtividadeComplementar;
	}

	public void setListaSelectItemTipoAtividadeComplementar(List<SelectItem> listaSelectItemTipoAtividadeComplementar) {
		this.listaSelectItemTipoAtividadeComplementar = listaSelectItemTipoAtividadeComplementar;
	}

	public void montarListaSelectItemTipoAtividadeComplementar() throws Exception {
		List<TipoAtividadeComplementarVO> resultadoConsulta = getFacadeFactory().getTipoAtividadeComplementarFacade()
				.consultarPorCursoTurmaMatricula(getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getCurso().getCodigo(),
						getTurmaVO().getIdentificadorTurma(), getTurmaVO().getTurmaAgrupada(),
						getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getMatricula(), null, false, getUsuarioLogado());
		List<SelectItem> lista = UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome");
		this.setListaSelectItemTipoAtividadeComplementar(lista);
	}

	public String getCampoConsultaCurso() {
		if (this.campoConsultaCurso == null) {
			this.campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (this.valorConsultaCurso == null) {
			this.valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCursoCoordenador() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarListaCursoPorNomeCursoCodigoPessoaCoordenador(this.getValorConsultaCurso(), this.getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().setCurso(obj);
			setTurmaVO(null);
			this.setCampoConsultaCurso("");
			this.setValorConsultaCurso("");
			this.getListaConsultaCurso().clear();
			montarListaSelectItemTipoAtividadeComplementar();
		} catch (Exception e) {
		}
	}
	
	public void selecionarHistorico(){
		getRegistroAtividadeComplementarMatriculaTempVO().getHistoricoAnt();
		for(SelectItem item:getListaSelectItemHistoricoAluno()){
			if(item.getValue() instanceof Integer && ((Integer)item.getValue()).equals(getRegistroAtividadeComplementarMatriculaTempVO().getHistoricoVO().getCodigo())){
				getRegistroAtividadeComplementarMatriculaTempVO().getHistoricoVO().getDisciplina().setNome(item.getLabel());
				break;
			}
		}
	}
	
	public void editarParaExcluirArquivo(){
		RegistroAtividadeComplementarMatriculaVO obj = (RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarMatriculaVOItens");
		setRegistroAtividadeComplementarMatriculaTempVO(new RegistroAtividadeComplementarMatriculaVO());
		setRegistroAtividadeComplementarMatriculaTempVO(obj);
	}
	
	public void excluirArquivo(){
		try{			
			getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().excluirArquivo(getRegistroAtividadeComplementarMatriculaTempVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());			
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String getCaminhoServidorDownload() {
		try {
			RegistroAtividadeComplementarMatriculaVO obj = (RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarMatriculaVOItens");
			if (obj.getCaminhoArquivoWeb() == null && !obj.getArquivoVO().getNome().trim().isEmpty()) {
				if (obj.getArquivoVO().getCodigo() > 0) {
					obj.getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR);
				} else {
					obj.getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR_TMP);
				}
				obj.setCaminhoArquivoWeb("abrirPopup('"+getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(obj.getArquivoVO(), obj.getArquivoVO().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema())+"', '"+obj.getMatriculaVO().getMatricula()+"', 100, 100);");
			}
			return obj.getCaminhoArquivoWeb();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public List<CursoVO> getListaConsultaCurso() {
		if (this.listaConsultaCurso == null) {
			this.listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public TurmaVO getTurmaVO() {
		if (this.turmaVO == null) {
			this.turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public String getCampoConsultaTurma() {
		if (this.campoConsultaTurma == null) {
			this.campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (this.valorConsultaTurma == null) {
			this.valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}

	public void setTipoConsultaComboCurso(List<SelectItem> tipoConsultaComboCurso) {
		this.tipoConsultaComboCurso = tipoConsultaComboCurso;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		}
		return tipoConsultaComboTurma;
	}

	public void setTipoConsultaComboTurma(List<SelectItem> tipoConsultaComboTurma) {
		this.tipoConsultaComboTurma = tipoConsultaComboTurma;
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getCurso().getCodigo(), 0, false, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaCoordenador() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCoordenador(getUsuarioLogado().getPessoa().getCodigo(), this.getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false,true, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (this.listaConsultaTurma == null) {
			this.listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public void adicionarRegistroComplementarMatriculaVOs() {
		try {
			RegistroAtividadeComplementarMatricula.validarDadosFiltroConsulta(this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getCurso().getCodigo(), this.getTurmaVO().getCodigo(), this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getMatricula(), this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getCurso().getPeriodicidade(), this.getCampoSemestre(), this.getCampoAno());
			RegistroAtividadeComplementarMatricula.validarDados(this.getRegistroAtividadeComplementarMatriculaVO());
			this.getRegistroAtividadeComplementarMatriculaVO().setDataCriacao(new Date());
			List<RegistroAtividadeComplementarMatriculaVO> listaConsulaRegistroAtividadeComplementarMatriculaVOs = (getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarListaMatriculaVOs(this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getCurso().getCodigo(), this.getTurmaVO().getCodigo(), this.getTurmaVO().getTurmaAgrupada(), this.getTurmaVO().getSubturma(), this.getCampoAno(), this.getCampoSemestre(), this.getCampoSituacao(), this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getMatricula(), this.getRegistroAtividadeComplementarMatriculaVO().getTipoAtividadeComplementarVO().getCodigo(), this.getRegistroAtividadeComplementarMatriculaVO().getCargaHorariaEvento(), this.getRegistroAtividadeComplementarMatriculaVO().getCargaHorariaConsiderada(), false, getUsuarioLogado(),this.getRegistroAtividadeComplementarMatriculaVO().getObservacao(), getRegistroAtividadeComplementarVO().getData()));
			setMensagemID("msg_dados_consultados");
			if(listaConsulaRegistroAtividadeComplementarMatriculaVOs.size() == 0){
				setMensagemID("msg_RegistroAtividadeComplementarControle");
			}else{
				setMensagemID("msg_dados_consultados");
			}
			this.getRegistroAtividadeComplementarVO().setListaRegistroAtividadeComplementarMatriculaVOs(getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().adicionarItemListaRegistroAtividadeComplementarMatriculaVOs(listaConsulaRegistroAtividadeComplementarMatriculaVOs, this.getRegistroAtividadeComplementarVO().getListaRegistroAtividadeComplementarMatriculaVOs()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		setTurmaVO(obj);
		this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().setCurso(obj.getCurso());
		obj = null;
		setValorConsultaTurma(null);
		setCampoConsultaTurma(null);
		this.getListaConsultaTurma().clear();
		montarListaSelectItemTipoAtividadeComplementar();
		Uteis.liberarListaMemoria(getListaConsultaTurma());
	}

	public String getValorConsultaAluno() {
		if (this.valorConsultaAluno == null) {
			this.valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public String getCampoConsultaAluno() {
		if (this.campoConsultaAluno == null) {
			this.campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorCoordenador() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaPorCoordenador(getValorConsultaAluno(), getUsuarioLogado().getPessoa().getCodigo(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoaPorCoordenador(getValorConsultaAluno(), getUsuarioLogado().getPessoa().getCodigo(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCursoPorCoordenador(getValorConsultaAluno(), getUsuarioLogado().getPessoa().getCodigo(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void editarRegistroAtividadeComplementarMatriculaVO() {
		RegistroAtividadeComplementarMatriculaVO obj = (RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarMatriculaVOItens");
		obj.getHistoricoAnt();
		setRegistroAtividadeComplementarMatriculaTempVO(new RegistroAtividadeComplementarMatriculaVO());
		setRegistroAtividadeComplementarMatriculaTempVO(obj);
		montarListaSelectItemHistorico(obj.getMatriculaVO().getMatricula());
	}
	
	public void inicializarUploadAtividadeComplementar() {
		RegistroAtividadeComplementarMatriculaVO obj = (RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarMatriculaVOItens");
		setRegistroAtividadeComplementarMatriculaTempVO(new RegistroAtividadeComplementarMatriculaVO());
		setRegistroAtividadeComplementarMatriculaTempVO(obj);
	}

	public String selecionarArquivoParaDownload() {
		try {
			RegistroAtividadeComplementarMatriculaVO obj = (RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarMatriculaVOItens");
			ArquivoVO arquivoVO = obj.getArquivoVO();
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			if(Uteis.isAtributoPreenchido(arquivoVO.getCodigo())){
				request.setAttribute("codigoArquivo", arquivoVO.getCodigo());	
			}else{
				File arquivo = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + arquivoVO.getPastaBaseArquivoEnum().getValue() + File.separator  + arquivoVO.getNome());
				if(arquivo.exists()){
					request.getSession().setAttribute("nomeArquivo", arquivo.getName());
					request.getSession().setAttribute("pastaBaseArquivo", arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator)));
					request.getSession().setAttribute("deletarArquivo", false);	
				}else{
					throw new Exception("Não foi encontrado o arquivo no caminho infomrado:"+ getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + arquivoVO.getPastaBaseArquivoEnum().getValue() + File.separator  + arquivoVO.getNome());
				}
			}
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}
		
	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			if(!getRegistroAtividadeComplementarMatriculaTempVO().getArquivoVO().getNome().trim().isEmpty() && getRegistroAtividadeComplementarMatriculaTempVO().getArquivoVO().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR_TMP)) {
				getFacadeFactory().getArquivoHelper().removerArquivoDiretorio(false, getRegistroAtividadeComplementarMatriculaTempVO().getArquivoVO(), "atividadeComplementar", getConfiguracaoGeralPadraoSistema());
			}
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getRegistroAtividadeComplementarMatriculaTempVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR_TMP, getUsuarioLogado());
			getRegistroAtividadeComplementarMatriculaTempVO().setCaminhoArquivoWeb(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public List<MatriculaVO> getListaConsultaAluno() {
		if (this.listaConsultaAluno == null) {
			this.listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
		this.getRegistroAtividadeComplementarMatriculaVO().setMatriculaVO(obj);
		this.setValorConsultaAluno("");
		this.setCampoConsultaAluno("");
		setCampoAno("");
		setCampoSemestre("");
		this.setTurmaVO(null);
		this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().setCurso(obj.getCurso());
		getListaConsultaAluno().clear();
		montarListaSelectItemTipoAtividadeComplementar();
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			this.getRegistroAtividadeComplementarMatriculaVO().setMatriculaVO(objAluno);
			this.setTurmaVO(null);
			montarListaSelectItemTipoAtividadeComplementar();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.getRegistroAtividadeComplementarMatriculaVO().setMatriculaVO(new MatriculaVO());
		}
	}

	public void consultarAlunoPorMatriculaPorCoordenador() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimariaPorCoordenador(this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getMatricula(), getUsuarioLogado().getPessoa().getCodigo(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			this.getRegistroAtividadeComplementarMatriculaVO().setMatriculaVO(objAluno);
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.getRegistroAtividadeComplementarMatriculaVO().setMatriculaVO(new MatriculaVO());
		}
	}

	public void limparDadosAluno() throws Exception {
		removerObjetoMemoria(this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO());
		montarListaSelectItemTipoAtividadeComplementar();
	}

	public void limparCurso() throws Exception {
		this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().setCurso(null);
		montarListaSelectItemTipoAtividadeComplementar();
	}

	public void limparTurma() throws Exception {
		this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().setCurso(null);
		this.setTurmaVO(null);
		montarListaSelectItemTipoAtividadeComplementar();
	}

	public String getCampoConsultaNomeEvento() {
		return campoConsultaNomeEvento;
	}

	public void setCampoConsultaNomeEvento(String campoConsultaNomeEvento) {
		this.campoConsultaNomeEvento = campoConsultaNomeEvento;
	}

	public String getCampoConsultaLocal() {
		return campoConsultaLocal;
	}

	public void setCampoConsultaLocal(String campoConsultaLocal) {
		this.campoConsultaLocal = campoConsultaLocal;
	}

	public String getCampoConsultaMatricula() {
		return campoConsultaMatricula;
	}

	public void setCampoConsultaMatricula(String campoConsultaMatricula) {
		this.campoConsultaMatricula = campoConsultaMatricula;
	}

	public String getCampoConsultaInstituicaoResponsavel() {
		return campoConsultaInstituicaoResponsavel;
	}

	public void setCampoConsultaInstituicaoResponsavel(String campoConsultaInstituicaoResponsavel) {
		this.campoConsultaInstituicaoResponsavel = campoConsultaInstituicaoResponsavel;
	}

	public Date getCampoConsultaDataInicio() {
//		if (this.campoConsultaDataInicio == null) {
//			this.campoConsultaDataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
//		}
		return campoConsultaDataInicio;
	}

	public void setCampoConsultaDataInicio(Date campoConsultaDataInicio) {
		this.campoConsultaDataInicio = campoConsultaDataInicio;
	}

	public Date getCampoConsultaDataFinal() {
//		if (this.campoConsultaDataFinal == null) {
//			this.campoConsultaDataFinal = Uteis.getDataUltimoDiaMes(new Date());
//		}
		return campoConsultaDataFinal;
	}

	public void setCampoConsultaDataFinal(Date campoConsultaDataFinal) {
		this.campoConsultaDataFinal = campoConsultaDataFinal;
	}

	public String consultarRegistroAtividadeComplementar() {
		try {
			setListaRegistroAtividadeComplementarMatriculaVOs(new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0));
			this.setListaConsultaRegistroAtividadeComplementarVOs(getFacadeFactory().getRegistroAtividadeComplementarFacade().consultar(this.getCampoConsultaNomeEvento(), getUnidadeEnsinoVOMarcadasParaSeremUtilizadas(), getCursoVOsMarcadosParaSeremUtilizados(), this.getCampoConsultaInstituicaoResponsavel(), this.getCampoConsultaLocal(), this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getMatricula(), this.getCampoConsultaDataInicio(), this.getCampoConsultaDataFinal(), false, this.getUsuarioLogado()));
			for(RegistroAtividadeComplementarVO obj : getListaConsultaRegistroAtividadeComplementarVOs()) {
				getListaRegistroAtividadeComplementarMatriculaVOs().addAll(getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarPorRegistroAtividadeComplementar(obj.getCodigo(), this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getMatricula(), false, this.getCampoConsultaDataInicio(), this.getCampoConsultaDataFinal(), getUsuario()));				
			}
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarCons.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarCons.xhtml");
		}
	}
	
	public String consultarRegistroAtividadeComplementarCoordenador() {
		try {
			setListaRegistroAtividadeComplementarMatriculaVOs(new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0));
			this.getRegistroAtividadeComplementarVO().setCoordenador(getUsuarioLogado().getPessoa().getCodigo());
			this.setListaConsultaRegistroAtividadeComplementarVOs(getFacadeFactory().getRegistroAtividadeComplementarFacade().consultarPorCoordenador(getUsuarioLogado().getPessoa().getCodigo(), this.getCampoConsultaNomeEvento(), getUnidadeEnsinoVOMarcadasParaSeremUtilizadas(), getCursoVOsMarcadosParaSeremUtilizados(), this.getCampoConsultaInstituicaoResponsavel(), this.getCampoConsultaLocal(), this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getMatricula(), this.getCampoConsultaDataInicio(), this.getCampoConsultaDataFinal(), false, this.getUsuarioLogado()));
			for(RegistroAtividadeComplementarVO obj : getListaConsultaRegistroAtividadeComplementarVOs()) {
				getListaRegistroAtividadeComplementarMatriculaVOs().addAll(getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarPorRegistroAtividadeComplementar(obj.getCodigo(), this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getMatricula(), false, this.getCampoConsultaDataInicio(), this.getCampoConsultaDataFinal(), getUsuario()));				
			}
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarCons.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarCons.xhtml");
		}
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		this.setListaConsultaRegistroAtividadeComplementarVOs(new ArrayList<RegistroAtividadeComplementarVO>(0));
		montarListaUnidadeEnsino();
		montarListaCurso();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarCons.xhtml");
	}
	
	public String inicializarConsultarCoordenador() {
		removerObjetoMemoria(this);
		this.setListaConsultaRegistroAtividadeComplementarVOs(new ArrayList<RegistroAtividadeComplementarVO>(0));
		montarListaUnidadeEnsino();
		montarListaCurso();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroAtividadeComplementarConsCoordenador.xhtml");
	}

	public List<RegistroAtividadeComplementarVO> getListaConsultaRegistroAtividadeComplementarVOs() {
		if (this.listaConsultaRegistroAtividadeComplementarVOs == null) {
			this.listaConsultaRegistroAtividadeComplementarVOs = new ArrayList<RegistroAtividadeComplementarVO>(0);
		}
		return listaConsultaRegistroAtividadeComplementarVOs;
	}

	public void setListaConsultaRegistroAtividadeComplementarVOs(List<RegistroAtividadeComplementarVO> listaConsultaRegistroAtividadeComplementarVOs) {
		this.listaConsultaRegistroAtividadeComplementarVOs = listaConsultaRegistroAtividadeComplementarVOs;
	}

	public RegistroAtividadeComplementarMatriculaVO getRegistroAtividadeComplementarMatriculaTempVO() {
		if (registroAtividadeComplementarMatriculaTempVO == null) {
			registroAtividadeComplementarMatriculaTempVO = new RegistroAtividadeComplementarMatriculaVO();
		}
		return registroAtividadeComplementarMatriculaTempVO;
	}

	public void setRegistroAtividadeComplementarMatriculaTempVO(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaTempVO) {
		this.registroAtividadeComplementarMatriculaTempVO = registroAtividadeComplementarMatriculaTempVO;
	}

	public void removerRegistroAtividadeComplementarMatriculaVO() throws Exception {
		try {
			RegistroAtividadeComplementarMatriculaVO obj = (RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarMatriculaVOItens");
			getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().removerRegistroAtividadeComplementarListaVOs(obj, this.getRegistroAtividadeComplementarVO().getListaRegistroAtividadeComplementarMatriculaVOs(), getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getApresentarAnoMatricula() {
		return ((this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getCurso().getPeriodicidade().equals("AN") || this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getCurso().getPeriodicidade().equals("SE")) && !Uteis.isAtributoPreenchido(getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getMatricula()));
	}

	public Boolean getApresentarSemestreMatricula() {
		return (this.getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getCurso().getPeriodicidade().equals("SE") && !Uteis.isAtributoPreenchido(getRegistroAtividadeComplementarMatriculaVO().getMatriculaVO().getMatricula()));
	}
	
	public void montarListaSelectItemHistorico(String matricula){
		try{
			List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultarHistoricoAptoVincularAtividadeComplementarPorMatricula(matricula);
			getListaSelectItemHistoricoAluno().clear();
			getListaSelectItemHistoricoAluno().add(new SelectItem(0, ""));
			if(getRegistroAtividadeComplementarMatriculaTempVO().getHistoricoVO().getCodigo() > 0){
				getListaSelectItemHistoricoAluno().add(new SelectItem(getRegistroAtividadeComplementarMatriculaTempVO().getHistoricoVO().getCodigo(), getRegistroAtividadeComplementarMatriculaTempVO().getHistoricoVO().getDisciplina().getNome()));
			}
			for(HistoricoVO historicoVO: historicoVOs){
				if(!getRegistroAtividadeComplementarMatriculaTempVO().getHistoricoVO().getCodigo().equals(historicoVO.getCodigo())) {
					getListaSelectItemHistoricoAluno().add(new SelectItem(historicoVO.getCodigo(), historicoVO.getDisciplina().getNome()));
				}
			}
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	} 

	public List<SelectItem> getListaSelectSemestre() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

	public List<SelectItem> getListaSelectSituacao() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("AT", "Ativo/Concluído"));
		lista.add(new SelectItem("PF", "Ativo - Possível Formando"));
		lista.add(new SelectItem("CO", "Concluído - Possível Formando"));
		return lista;
	}

	public String getCampoSemestre() {
		return campoSemestre;
	}

	public void setCampoSemestre(String campoSemestre) {
		this.campoSemestre = campoSemestre;
	}

	public String getCampoAno() {
		return campoAno;
	}

	public void setCampoAno(String campoAno) {
		this.campoAno = campoAno;
	}

	public String getCampoSituacao() {
		return campoSituacao;
	}

	public void setCampoSituacao(String campoSituacao) {
		this.campoSituacao = campoSituacao;
	}

	public List<SelectItem> getListaSelectItemHistoricoAluno() {
		if (listaSelectItemHistoricoAluno == null) {
			listaSelectItemHistoricoAluno = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemHistoricoAluno;
	}

	public void setListaSelectItemHistoricoAluno(List<SelectItem> listaSelectItemHistoricoAluno) {
		this.listaSelectItemHistoricoAluno = listaSelectItemHistoricoAluno;
	}
	
	public String getCampoConsultaAtividadeComplementar() {
		if (this.campoConsultaAtividadeComplementar == null) {
			this.campoConsultaAtividadeComplementar = "";
		}
		return campoConsultaAtividadeComplementar;
	}

	public void setCampoConsultaAtividadeComplementar(
			String campoConsultaAtividadeComplementar) {
		this.campoConsultaAtividadeComplementar = campoConsultaAtividadeComplementar;
	}

	public String getValorConsultaAtividadeComplementar() {
		if (this.valorConsultaAtividadeComplementar == null) {
			this.valorConsultaAtividadeComplementar = "";
		}
		return valorConsultaAtividadeComplementar;
	}

	public void setValorConsultaAtividadeComplementar(
			String valorConsultaAtividadeComplementar) {
		this.valorConsultaAtividadeComplementar = valorConsultaAtividadeComplementar;
	}

	public List<TipoAtividadeComplementarVO> getListaConsultaAtividadeComplementar() {
		if (this.listaConsultaAtividadeComplementar == null) {
			this.listaConsultaAtividadeComplementar = new ArrayList<TipoAtividadeComplementarVO>(0);
		}
		return listaConsultaAtividadeComplementar;
	}

	public void setListaConsultaAtividadeComplementar(List<TipoAtividadeComplementarVO> listaConsultaAtividadeComplementar) {
		this.listaConsultaAtividadeComplementar = listaConsultaAtividadeComplementar;
	}
	
	public List<SelectItem> getTipoConsultaComboAtividadeComplementar() {
		if (tipoConsultaComboAtividadeComplementar == null) {
			tipoConsultaComboAtividadeComplementar = new ArrayList<SelectItem>(0);
			tipoConsultaComboAtividadeComplementar.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboAtividadeComplementar;
	}

	public void setTipoConsultaComboAtividadeComplementar(List<SelectItem> tipoConsultaComboAtividadeComplementar) {
		this.tipoConsultaComboAtividadeComplementar = tipoConsultaComboAtividadeComplementar;
	}
	
	public String getUsernameLiberarOperacaoFuncionalidade() {
		if (usernameLiberarOperacaoFuncionalidade == null) {
			usernameLiberarOperacaoFuncionalidade = "";
		}
		return usernameLiberarOperacaoFuncionalidade;
	}

	public void setUsernameLiberarOperacaoFuncionalidade(String usernameLiberarOperacaoFuncionalidade) {
		this.usernameLiberarOperacaoFuncionalidade = usernameLiberarOperacaoFuncionalidade;
	}

	public String getSenhaLiberarOperacaoFuncionalidade() {
		if (senhaLiberarOperacaoFuncionalidade == null) {
			senhaLiberarOperacaoFuncionalidade = "";
		}
		return senhaLiberarOperacaoFuncionalidade;
	}

	public void setSenhaLiberarOperacaoFuncionalidade(String senhaLiberarOperacaoFuncionalidade) {
		this.senhaLiberarOperacaoFuncionalidade = senhaLiberarOperacaoFuncionalidade;
	}

	/**
	 * @return the oncompleteOperacaoFuncionalidade
	 */
	public String getOncompleteOperacaoFuncionalidade() {
		if (oncompleteOperacaoFuncionalidade == null) {
			oncompleteOperacaoFuncionalidade = "";
		}
		return oncompleteOperacaoFuncionalidade;
	}

	/**
	 * @param oncompleteOperacaoFuncionalidade
	 *            the oncompleteOperacaoFuncionalidade to set
	 */
	public void setOncompleteOperacaoFuncionalidade(String oncompleteOperacaoFuncionalidade) {
		this.oncompleteOperacaoFuncionalidade = oncompleteOperacaoFuncionalidade;
	}
	
	
	
	public String getTipoOperacaoFuncionalidadeLiberar() {
		if (tipoOperacaoFuncionalidadeLiberar == null) {
			tipoOperacaoFuncionalidadeLiberar = "";
		}
		return tipoOperacaoFuncionalidadeLiberar;
	}

	public void setTipoOperacaoFuncionalidadeLiberar(String tipoOperacaoFuncionalidadeLiberar) {
		this.tipoOperacaoFuncionalidadeLiberar = tipoOperacaoFuncionalidadeLiberar;
	}

	public boolean isLiberarTodosRegistroAtividadesComplementar() {
		return liberarTodosRegistroAtividadesComplementar;
	}

	public void setLiberarTodosRegistroAtividadesComplementar(boolean liberarTodosRegistroAtividadesComplementar) {
		this.liberarTodosRegistroAtividadesComplementar = liberarTodosRegistroAtividadesComplementar;
	}
	
	

	public List<TipoAtividadeComplementarVO> getListaTipoAtividadeComplementarLiberacaoOperacao() {
		if(listaTipoAtividadeComplementarLiberacaoOperacao == null){
			listaTipoAtividadeComplementarLiberacaoOperacao = new ArrayList<TipoAtividadeComplementarVO>();
		}
		return listaTipoAtividadeComplementarLiberacaoOperacao;
	}

	public void setListaTipoAtividadeComplementarLiberacaoOperacao(List<TipoAtividadeComplementarVO> listaTipoAtividadeComplementarLiberacaoOperacao) {
		this.listaTipoAtividadeComplementarLiberacaoOperacao = listaTipoAtividadeComplementarLiberacaoOperacao;
	}

	
	public Integer getTotalCargaHoraMaximaAtividade() {
		if(totalCargaHoraMaximaAtividade == null){
			totalCargaHoraMaximaAtividade = 0;
		}
		return totalCargaHoraMaximaAtividade;
	}

	public void setTotalCargaHoraMaximaAtividade(Integer totalCargaHoraMaximaAtividade) {
		this.totalCargaHoraMaximaAtividade = totalCargaHoraMaximaAtividade;
	}
	
	public void consultarAtividadeComplementar() {
		try {
			List<TipoAtividadeComplementarVO> objs = new ArrayList<TipoAtividadeComplementarVO>(0);
			if (getCampoConsultaAtividadeComplementar().equals("nome")) {
				objs = getFacadeFactory().getTipoAtividadeComplementarFacade().consultarPorNome(getValorConsultaAtividadeComplementar(), false, getUsuarioLogado());
			}
			setListaConsultaAtividadeComplementar(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAtividadeComplementar(new ArrayList<TipoAtividadeComplementarVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarAtividadeComplementar() throws Exception {
		try {
			TipoAtividadeComplementarVO obj = (TipoAtividadeComplementarVO) context().getExternalContext().getRequestMap().get("tac");
			boolean possuiNaCombobox = false;
			for (SelectItem item : this.getListaSelectItemTipoAtividadeComplementar()) {
				if (item.getLabel().equals(obj.getNome()) && Integer.parseInt(item.getValue().toString()) == obj.getCodigo().intValue()) {
					possuiNaCombobox = true;
					break;
				}
			}
			if (!possuiNaCombobox) {
				this.getListaSelectItemTipoAtividadeComplementar().add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			getRegistroAtividadeComplementarMatriculaVO().getTipoAtividadeComplementarVO().setCodigo(obj.getCodigo());
			this.setCampoConsultaAtividadeComplementar("");
			this.setValorConsultaAtividadeComplementar("");
			this.getListaConsultaAtividadeComplementar().clear();
		} catch (Exception e) {
		}
	}
	
	public void atualizarCargaHorariaRealizada(){
		try {
			RegistroAtividadeComplementarMatriculaVO obj = (RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarMatriculaVOItens");
			if(!obj.isChLiberadaPorOperacaoFuncionalidade()){
				obj.setTipoAtividadeComplementarVO(getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().consultarCargaHorasMaximaPermitidoPeriodoLetivoDoTipoAtividadeComplementar(obj.getTipoAtividadeComplementarVO(), obj.getMatriculaVO().getCurso().getCodigo(), obj.getMatriculaVO().getMatricula(), PeriodicidadeEnum.getEnumPorValor(obj.getMatriculaVO().getCurso().getPeriodicidade()), getRegistroAtividadeComplementarVO().getData(), obj.getCodigo()));	
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}	
	}
	
	public void carregarModalLiberarRegistroAtividadeComplementarMatricula(){
		try {
			setTotalCargaHoraMaximaAtividade(0);
			setRegistroAtividadeComplementarMatriculaTempVO((RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarMatriculaVOItens"));
			setListaTipoAtividadeComplementarLiberacaoOperacao(getFacadeFactory().getTipoAtividadeComplementarFacade().consultarTipoAtividadeComplementarComCargaHorariasPeriodoLetivo(getRegistroAtividadeComplementarMatriculaTempVO().getTipoAtividadeComplementarVO().getCodigo(), getRegistroAtividadeComplementarMatriculaTempVO().getMatriculaVO().getCurso().getCodigo(), getRegistroAtividadeComplementarMatriculaTempVO().getMatriculaVO().getMatricula(), PeriodicidadeEnum.getEnumPorValor(getRegistroAtividadeComplementarMatriculaTempVO().getMatriculaVO().getCurso().getPeriodicidade()), getRegistroAtividadeComplementarVO().getData(), getRegistroAtividadeComplementarMatriculaTempVO().getCodigo()));
			for (TipoAtividadeComplementarVO obj : getListaTipoAtividadeComplementarLiberacaoOperacao()) {
				if(obj.getCodigo().equals(getRegistroAtividadeComplementarMatriculaTempVO().getTipoAtividadeComplementarVO().getCodigo())){
					obj.setCargaHorasJaRealizadaPeriodoLetivo(obj.getCargaHorasJaRealizadaPeriodoLetivo() + getRegistroAtividadeComplementarMatriculaTempVO().getCargaHorariaConsiderada());
				}
				setTotalCargaHoraMaximaAtividade(getTotalCargaHoraMaximaAtividade() + obj.getCargaHorasJaRealizadaPeriodoLetivo());	
			}
			
			setTipoOperacaoFuncionalidadeLiberar("Liberar Carga Horária para Registro de Atividade Complementar Matricula.");
			setLiberarTodosRegistroAtividadesComplementar(false);
			inicializarMensagemVazia();			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}	
	}
	
	public void carregarModalLiberarTodosRegistroAtividadeComplementarMatricula(){
		try {
			getListaTipoAtividadeComplementarLiberacaoOperacao().clear();
			setTipoOperacaoFuncionalidadeLiberar("Liberar Todos as Cargas Horarias Excedidas dos Registros de Atividade Complementar Matricula.");
			setLiberarTodosRegistroAtividadesComplementar(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}	
	}
	
	public void validarLiberarOperacaoFuncionalidade() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberarOperacaoFuncionalidade(), this.getSenhaLiberarOperacaoFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirLiberarCargaHorariaMaximaPeriodoLetivo", usuarioVerif);
			if(isLiberarTodosRegistroAtividadesComplementar()){
				for (RegistroAtividadeComplementarMatriculaVO obj : getRegistroAtividadeComplementarVO().getListaRegistroAtividadeComplementarMatriculaVOs()) {
					if(obj.isChRealizadaMaiorQueHorasPermitidaPeriodoLetivo()){
						obj.getTipoAtividadeComplementarVO().setCargaHorasPermitidasPeriodoLetivo(0);
						obj.getTipoAtividadeComplementarVO().getTipoAtividadeComplementarSuperior().setCargaHorasPermitidasPeriodoLetivo(0);
						obj.setOperacaoFuncionalidadeVO(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.REGISTRO_ATIVIDADE_COMPLEMENTAR_MATRICULA, "", OperacaoFuncionalidadeEnum.REGISTROATIVIDADECOMPLEMENTARMATRICULA_LIBERARCARGAHORARIAMAXIMAPERIODOLETIVO, usuarioVerif, ""));	
					}				
				}				
			}else{
				getRegistroAtividadeComplementarMatriculaTempVO().getTipoAtividadeComplementarVO().setCargaHorasPermitidasPeriodoLetivo(0);
				getRegistroAtividadeComplementarMatriculaTempVO().getTipoAtividadeComplementarVO().getTipoAtividadeComplementarSuperior().setCargaHorasPermitidasPeriodoLetivo(0);
				getRegistroAtividadeComplementarMatriculaTempVO().setOperacaoFuncionalidadeVO(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.REGISTRO_ATIVIDADE_COMPLEMENTAR_MATRICULA, "", OperacaoFuncionalidadeEnum.REGISTROATIVIDADECOMPLEMENTARMATRICULA_LIBERARCARGAHORARIAMAXIMAPERIODOLETIVO, usuarioVerif, ""));
			}
			setOncompleteOperacaoFuncionalidade("RichFaces.$('liberarOperacaoFuncionalidade').hide();");
			setMensagemID("msg_funcionalidadeLiberadaComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setOncompleteOperacaoFuncionalidade("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void editarObservacaoRegistroAtividadeComplementarMatriculaVO() {
		RegistroAtividadeComplementarMatriculaVO obj = (RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarMatriculaVOItens");
		setRegistroAtividadeComplementarMatriculaTempVO(new RegistroAtividadeComplementarMatriculaVO());
		setRegistroAtividadeComplementarMatriculaTempVO(obj);
	}
	

	public void realizarVerificacaoCargaHorariaMaximaPeriodoLetivoAtingida(){
		try {
			for(RegistroAtividadeComplementarMatriculaVO obj: getRegistroAtividadeComplementarVO().getListaRegistroAtividadeComplementarMatriculaVOs()) {			
				if(!obj.isChLiberadaPorOperacaoFuncionalidade()){
					obj.setTipoAtividadeComplementarVO(getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().consultarCargaHorasMaximaPermitidoPeriodoLetivoDoTipoAtividadeComplementar(obj.getTipoAtividadeComplementarVO(), obj.getMatriculaVO().getCurso().getCodigo(), obj.getMatriculaVO().getMatricula(), PeriodicidadeEnum.getEnumPorValor(obj.getMatriculaVO().getCurso().getPeriodicidade()), getRegistroAtividadeComplementarVO().getData(), obj.getCodigo()));	
				}
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}	
	}
	
	public void realizarProcessamentoArquivoExcelRegistroAtividadeComplementar(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getRegistroAtividadeComplementarFacade().realizarProcessamentoExcelPlanilhaAtividadeComplementar(uploadEvent, getRegistroAtividadeComplementarVO() , getRegistroAtividadeComplementarVO().getListaRegistroAtividadeComplementarMatriculaVOs(),getUnidadeEnsinoLogadoClone().getCodigo(), getUsuarioLogadoClone());	
			setMensagemID("msg_dados_importados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void downloadImportacaoAtividadeComplementarLayoutPadraoExcel() throws Exception {
		try {
			File arquivo = new File(UteisJSF.getCaminhoWeb() + "arquivos" +  File.separator + "ModeloImportacaoAtividadeComplementares.xlsx");
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", arquivo.getName());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaUnidadeEnsino() {
		try {
			setUnidadeEnsinoVOs(null);
			if (Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo())) {
				setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuario(getUsuarioLogado()));
			}else {
				setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(0, false, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void marcarTodasUnidadesEnsinosAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			if (getMarcarTodasUnidadeEnsino()) {
				unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
		verificarTodasUnidadesEnsinoSelecionado();	
	}
	
	public void verificarTodasUnidadesEnsinoSelecionado() {
		StringBuilder unidades = new StringBuilder();
		for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
			if (obj.getFiltrarUnidadeEnsino()) {				
				unidades.append(obj.getNome()).append("; ");
			}
		}
		setUnidadeEnsinosApresentar(unidades.toString());
		montarListaCurso();
	}
	
	public void limparFiltroUnidadeEnsino() {
		try {
			setUnidadeEnsinosApresentar(null);
			setCursosApresentar(null);
			setMarcarTodasUnidadeEnsino(false);
			marcarTodasUnidadesEnsinosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaCurso() {
		try {
			setCursoVOs(null);
			setCursoVOs(getFacadeFactory().getCursoFacade().consultarCursoPorNomePeriodicidadeEUnidadeEnsinoVOs("", "", null, getUnidadeEnsinoVOs(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<RegistroAtividadeComplementarMatriculaVO> getListaRegistroAtividadeComplementarMatriculaVOs() {
		if (listaRegistroAtividadeComplementarMatriculaVOs == null) {
			listaRegistroAtividadeComplementarMatriculaVOs = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		}
		return listaRegistroAtividadeComplementarMatriculaVOs;
	}
	
	public void setListaRegistroAtividadeComplementarMatriculaVOs(
			List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs) {
		this.listaRegistroAtividadeComplementarMatriculaVOs = listaRegistroAtividadeComplementarMatriculaVOs;
	}
	
	public Boolean getIsAnexoPDF() {
		return Uteis.isAtributoPreenchido(getCaminhoPreview()) && (getCaminhoPreview().endsWith(".pdf?embedded=true") || getCaminhoPreview().endsWith(".PDF?embedded=true")) ;	
	}
	
	public Boolean getIsAnexoImagem() {
		return Uteis.isAtributoPreenchido(getCaminhoPreview()) && (
				getCaminhoPreview().endsWith(".jpeg?embedded=true") || getCaminhoPreview().endsWith(".JPEG?embedded=true") || getCaminhoPreview().endsWith(".jpg?embedded=true")
		        || getCaminhoPreview().endsWith(".JPG?embedded=true") || getCaminhoPreview().endsWith(".png?embedded=true") || getCaminhoPreview().endsWith(".PNG?embedded=true")
		        || getCaminhoPreview().endsWith(".gif?embedded=true") || getCaminhoPreview().endsWith(".GIF?embedded=true") || getCaminhoPreview().endsWith(".bmp?embedded=true")
		        || getCaminhoPreview().endsWith(".BMP?embedded=true") || getCaminhoPreview().endsWith(".ico?embedded=true") || getCaminhoPreview().endsWith(".ICO?embedded=true"));	
	}
}
