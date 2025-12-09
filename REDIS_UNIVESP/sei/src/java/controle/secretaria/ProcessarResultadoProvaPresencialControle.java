/**
 * 
 */
package controle.secretaria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.enumeradores.TipoGabaritoEnum;
import negocio.comuns.academico.enumeradores.TipoRespostaGabaritoEnum;
import negocio.comuns.processosel.GabaritoRespostaVO;
import negocio.comuns.processosel.GabaritoVO;
import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaProvaPresencialVO;
import negocio.comuns.processosel.enumeradores.TipoProcessamentoProvaPresencial;
import negocio.comuns.secretaria.MatriculaProvaPresencialDisciplinaVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialRespostaVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialVO;
import negocio.comuns.secretaria.enumeradores.SituacaoMatriculaProvaPresencialDisciplinaEnum;
import negocio.comuns.secretaria.enumeradores.TipoAlteracaoSituacaoHistoricoEnum;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

/**
 * @author Carlos Eugênio
 *
 */
@Controller("ProcessarResultadoProvaPresencialControle")
@Scope("viewScope")
@Lazy
public class ProcessarResultadoProvaPresencialControle extends SuperControle implements Serializable {

	private GabaritoVO gabaritoVO;
	private MatriculaProvaPresencialVO matriculaProvaPresencialVO;
	private ResultadoProcessamentoArquivoRespostaProvaPresencialVO resultadoProcessamentoArquivoRespostaProvaPresencialVO;

	private String campoConsultaGabarito;
	private String valorConsultaGabarito;
	private List<GabaritoVO> listaConsultaGabarito;
	private List<MatriculaProvaPresencialDisciplinaVO> matriculaProvaPresencialDisciplinaVOs;
	private ProgressBarVO progressBarVO;
	private Boolean deveApresentarModalErro;
	private List<SelectItem> listaSelectItemVariavelNota;
	private List<SelectItem> listaSelectItemConfiguracaoAcademico;
	private List<MatriculaProvaPresencialRespostaVO> matriculaProvaPresencialRespostaVOs;
	private List<GabaritoRespostaVO> listaGabaritorespostaVO;

	private static final long serialVersionUID = 1L;

	public ProcessarResultadoProvaPresencialControle() {
		super();
		getResultadoProcessamentoArquivoRespostaProvaPresencialVO().setTipoProcessamentoProvaPresencialEnum(TipoProcessamentoProvaPresencial.LEITURA_ARQUIVO_NOTA_LANCADA);
		montarListaSelectItemConfiguracaoAcademico();
	}

	public String novo() {
		setGabaritoVO(new GabaritoVO());
		setMatriculaProvaPresencialVO(null);
		setResultadoProcessamentoArquivoRespostaProvaPresencialVO(null);
		getListaConsultaGabarito().clear();
		getMatriculaProvaPresencialDisciplinaVOs().clear();
		montarListaSelectItemConfiguracaoAcademico();
		setProgressBarVO(new ProgressBarVO());
		setDeveApresentarModalErro(Boolean.FALSE);
		getResultadoProcessamentoArquivoRespostaProvaPresencialVO().setUsuarioVO(getUsuarioLogadoClone());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("processarResultadoProvaPresencialForm");
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("processarResultadoProvaPresencialCons");
	}
	
	public String consultar() {
		try {
			super.consultar();
			List<ResultadoProcessamentoArquivoRespostaProvaPresencialVO> objs = new ArrayList<>(0);
			if (getControleConsulta().getCampoConsulta().equals("gabarito")) {
				objs = getFacadeFactory().getResultadoProcessamentoArquivoRespostaProvaPresencialFacade().consultarPorGabarito(getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				objs = getFacadeFactory().getResultadoProcessamentoArquivoRespostaProvaPresencialFacade().consultarPorMatricula(getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("aluno")) {
				objs = getFacadeFactory().getResultadoProcessamentoArquivoRespostaProvaPresencialFacade().consultarPorAluno(getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("processarResultadoProvaPresencialCons");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("processarResultadoProvaPresencialCons");
		}
	}

	public void consultarGabarito() {
		try {
			List<GabaritoVO> objs = new ArrayList<>(0);
			if (getCampoConsultaGabarito().equals("descricao")) {
				objs = getFacadeFactory().getGabaritoFacade().consultaRapidaPorDescricaoTipoGabarito(getValorConsultaGabarito(), TipoGabaritoEnum.PROVA_PRESENCIAL, getUsuarioLogado());
			}
			setListaConsultaGabarito(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaGabarito(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarGabarito() {
		try {
			GabaritoVO obj = (GabaritoVO) context().getExternalContext().getRequestMap().get("gabaritoItens");
			setGabaritoVO(getFacadeFactory().getGabaritoFacade().consultaRapidaPorChavePrimaria(obj.getCodigo(), getUsuarioLogado()));
			getResultadoProcessamentoArquivoRespostaProvaPresencialVO().setGabaritoVO(getGabaritoVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String editar() {
		ResultadoProcessamentoArquivoRespostaProvaPresencialVO obj = (ResultadoProcessamentoArquivoRespostaProvaPresencialVO) context().getExternalContext().getRequestMap().get("resultado");
		try {
			getFacadeFactory().getResultadoProcessamentoArquivoRespostaProvaPresencialFacade().carregarDados(obj, getUsuarioLogado());
			setResultadoProcessamentoArquivoRespostaProvaPresencialVO(obj);
			montarListaSelectItemConfiguracaoAcademico();
			montarListaSelectItemVariavelNota();
			setGabaritoVO(getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getGabaritoVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("processarResultadoProvaPresencialForm");
	}

	public void selecionarMatriculaProvaPresencial() {
		MatriculaProvaPresencialVO obj = (MatriculaProvaPresencialVO) context().getExternalContext().getRequestMap().get("resultado");
		setListaGabaritorespostaVO(getFacadeFactory().getGabaritoRespostaFacade().consultaRapidaPorGabarito(gabaritoVO.getCodigo(), getUsuario()));
		for (int i = 0; i < obj.getMatriculaProvaPresencialRespostaVOs().size(); i++) {					
			for (GabaritoRespostaVO objGabaritoRespostaVO : getListaGabaritorespostaVO()) {				
				 if (obj.getMatriculaProvaPresencialRespostaVOs().get(i).getNrQuestao().equals(objGabaritoRespostaVO.getNrQuestao())) {					
					 if (obj.getMatriculaProvaPresencialRespostaVOs().get(i).getRespostaAluno().equalsIgnoreCase(obj.getMatriculaProvaPresencialRespostaVOs().get(i).getRespostaGabarito()) 
								&& !obj.getMatriculaProvaPresencialRespostaVOs().get(i).getRespostaAluno().equalsIgnoreCase("W") && !obj.getMatriculaProvaPresencialRespostaVOs().get(i).getRespostaAluno().equalsIgnoreCase("Z") && !objGabaritoRespostaVO.getAnulado()) {
						 obj.getMatriculaProvaPresencialRespostaVOs().get(i).setSituacaoQuestao("Correta");
					 }
					 else if (!obj.getMatriculaProvaPresencialRespostaVOs().get(i).getRespostaAluno().equalsIgnoreCase(obj.getMatriculaProvaPresencialRespostaVOs().get(i).getRespostaGabarito()) && !objGabaritoRespostaVO.getAnulado()) {
							obj.getMatriculaProvaPresencialRespostaVOs().get(i).setSituacaoQuestao("Errada");
					}else {
							obj.getMatriculaProvaPresencialRespostaVOs().get(i).setSituacaoQuestao("Anulada");
					}						
					}
				}												
			}			
		setMatriculaProvaPresencialVO(obj);			
		}
	

	public void selecionarMatriculaProvaPresencialDisciplinaLocalizada() {
		MatriculaProvaPresencialVO obj = (MatriculaProvaPresencialVO) context().getExternalContext().getRequestMap().get("resultado");
		setMatriculaProvaPresencialDisciplinaVOs(new ArrayList<MatriculaProvaPresencialDisciplinaVO>(0));
		setMatriculaProvaPresencialDisciplinaVOs(getFacadeFactory().getProcessarResultadoProvaPresencialFacade().realizarObtencaoListaPorSituacaoLocalizacao(obj, SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_LOCALIZADA, getUsuarioLogado()));
		setMatriculaProvaPresencialVO(obj);
	}

	public void selecionarMatriculaProvaPresencialDisciplinaNaoLocalizada() {
		MatriculaProvaPresencialVO obj = (MatriculaProvaPresencialVO) context().getExternalContext().getRequestMap().get("resultado");
		setMatriculaProvaPresencialDisciplinaVOs(new ArrayList<MatriculaProvaPresencialDisciplinaVO>(0));
		setMatriculaProvaPresencialDisciplinaVOs(getFacadeFactory().getProcessarResultadoProvaPresencialFacade().realizarObtencaoListaPorSituacaoLocalizacao(obj, SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_NAO_LOCALIZADA, getUsuarioLogado()));
		setMatriculaProvaPresencialVO(obj);
	}
	
	public void selecionarMatriculaProvaPresencialDisciplina() {
		MatriculaProvaPresencialVO obj = (MatriculaProvaPresencialVO) context().getExternalContext().getRequestMap().get("resultado");
		setMatriculaProvaPresencialDisciplinaVOs(new ArrayList<MatriculaProvaPresencialDisciplinaVO>(0));
		setMatriculaProvaPresencialDisciplinaVOs(getFacadeFactory().getProcessarResultadoProvaPresencialFacade().realizarObtencaoListaPorSituacaoLocalizacao(obj, SituacaoMatriculaProvaPresencialDisciplinaEnum.TODAS_DISCIPLINAS, getUsuarioLogado()));
		setMatriculaProvaPresencialVO(obj);
	}
	
	public void selecionarMatriculaProvaPresencialDisciplinaNaoLocalizadaConfiguracaoAcademica() {
		MatriculaProvaPresencialVO obj = (MatriculaProvaPresencialVO) context().getExternalContext().getRequestMap().get("resultado");
		setMatriculaProvaPresencialDisciplinaVOs(new ArrayList<MatriculaProvaPresencialDisciplinaVO>(0));
		setMatriculaProvaPresencialDisciplinaVOs(getFacadeFactory().getProcessarResultadoProvaPresencialFacade().realizarObtencaoListaPorSituacaoLocalizacao(obj, SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_OUTRA_CONFIGURACAO_ACADEMICA, getUsuarioLogado()));
		setMatriculaProvaPresencialVO(obj);
	}



	public void limparGabarito() {
		setGabaritoVO(null);
		setMatriculaProvaPresencialVO(null);
		setResultadoProcessamentoArquivoRespostaProvaPresencialVO(null);
		getListaConsultaGabarito().clear();
		getMatriculaProvaPresencialDisciplinaVOs().clear();

	}

	public void processarResultadoProvaPresencial(FileUploadEvent event) {
		try {
			setResultadoProcessamentoArquivoRespostaProvaPresencialVO(getFacadeFactory().getProcessarResultadoProvaPresencialFacade().realizarProcessamentoArquivoResposta(event, getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoProcessamentoProvaPresencialEnum(), getGabaritoVO(), getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getAno(), getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getSemestre(), getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getConfiguracaoAcademicoVO().getCodigo(), getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getPeriodicidadeCurso(), getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getRealizarCalculoMediaLancamentoNota(), getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoAlteracaoSituacaoHistorico(), getUsuarioLogado()));
			getResultadoProcessamentoArquivoRespostaProvaPresencialVO().setGabaritoVO(getGabaritoVO());
			setMensagemID("msg_arquivoProcessado");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			this.getMatriculaProvaPresencialDisciplinaVOs().clear();
		}
	}

	public void persistir() {
		boolean adicionado = false;
		try {
			executarBloqueioTurmaProgramacaoAula(true, true);
			adicionado = true;
			getResultadoProcessamentoArquivoRespostaProvaPresencialVO().setGabaritoVO(getGabaritoVO());
			getFacadeFactory().getResultadoProcessamentoArquivoRespostaProvaPresencialFacade().persistir(getResultadoProcessamentoArquivoRespostaProvaPresencialVO(), getProgressBarVO(), getProgressBarVO().getUsuarioVO());
			if (!getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getListaResultadoProcessamentoProvaPresencialMotivoErroVOs().isEmpty()) {
				setDeveApresentarModalErro(Boolean.TRUE);
			}
			if(getProgressBarVO().getSuperControle() != null) {
				getProgressBarVO().getSuperControle().setMensagemID("msg_dados_gravados");
			}else {
				setMensagemID("msg_dados_gravados");
			}			
		} catch (Exception e) {
			e.printStackTrace();
			if(getProgressBarVO().getSuperControle() != null) {
				getProgressBarVO().getSuperControle().setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}else {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally{
			if(adicionado){
				try {
					executarBloqueioTurmaProgramacaoAula(false, false);
				} catch (Exception e) {
				
				}				
			}
			getProgressBarVO().incrementar();
			getProgressBarVO().setForcarEncerramento(true);			
		}
	}
	
	private void executarBloqueioTurmaProgramacaoAula(boolean adicionar, boolean retornarExcecao) throws Exception {
		getProgressBarVO().getAplicacaoControle().executarBloqueioProcessamentoArquivoProvaPresencial(adicionar, retornarExcecao);
	}

	public String excluir() {
		try {
			getFacadeFactory().getResultadoProcessamentoArquivoRespostaProvaPresencialFacade().excluir(getResultadoProcessamentoArquivoRespostaProvaPresencialVO(), getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("processarResultadoProvaPresencialForm");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("processarResultadoProvaPresencialForm");
		}
	}

	public List<SelectItem> getTipoConsultaComboGabarito() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("matricula", "Matricula"));
		itens.add(new SelectItem("gabarito", "Gabarito"));
		return itens;
	}

	public String getCampoConsultaGabarito() {
		if (campoConsultaGabarito == null) {
			campoConsultaGabarito = "";
		}
		return campoConsultaGabarito;
	}

	public void setCampoConsultaGabarito(String campoConsultaGabarito) {
		this.campoConsultaGabarito = campoConsultaGabarito;
	}

	public String getValorConsultaGabarito() {
		if (valorConsultaGabarito == null) {
			valorConsultaGabarito = "";
		}
		return valorConsultaGabarito;
	}

	public void setValorConsultaGabarito(String valorConsultaGabarito) {
		this.valorConsultaGabarito = valorConsultaGabarito;
	}

	public List<GabaritoVO> getListaConsultaGabarito() {
		if (listaConsultaGabarito == null) {
			listaConsultaGabarito = new ArrayList<GabaritoVO>(0);
		}
		return listaConsultaGabarito;
	}

	public void setListaConsultaGabarito(List<GabaritoVO> listaConsultaGabarito) {
		this.listaConsultaGabarito = listaConsultaGabarito;
	}

	public GabaritoVO getGabaritoVO() {
		if (gabaritoVO == null) {
			gabaritoVO = new GabaritoVO();
		}
		return gabaritoVO;
	}

	public void setGabaritoVO(GabaritoVO gabaritoVO) {
		this.gabaritoVO = gabaritoVO;
	}

	public ResultadoProcessamentoArquivoRespostaProvaPresencialVO getResultadoProcessamentoArquivoRespostaProvaPresencialVO() {
		if (resultadoProcessamentoArquivoRespostaProvaPresencialVO == null) {
			resultadoProcessamentoArquivoRespostaProvaPresencialVO = new ResultadoProcessamentoArquivoRespostaProvaPresencialVO();
		}
		return resultadoProcessamentoArquivoRespostaProvaPresencialVO;
	}

	public void setResultadoProcessamentoArquivoRespostaProvaPresencialVO(ResultadoProcessamentoArquivoRespostaProvaPresencialVO resultadoProcessamentoArquivoRespostaProvaPresencialVO) {
		this.resultadoProcessamentoArquivoRespostaProvaPresencialVO = resultadoProcessamentoArquivoRespostaProvaPresencialVO;
	}

	public MatriculaProvaPresencialVO getMatriculaProvaPresencialVO() {
		if (matriculaProvaPresencialVO == null) {
			matriculaProvaPresencialVO = new MatriculaProvaPresencialVO();
		}
		return matriculaProvaPresencialVO;
	}

	public void setMatriculaProvaPresencialVO(MatriculaProvaPresencialVO matriculaProvaPresencialVO) {
		this.matriculaProvaPresencialVO = matriculaProvaPresencialVO;
	}

	public List<MatriculaProvaPresencialDisciplinaVO> getMatriculaProvaPresencialDisciplinaVOs() {
		if (matriculaProvaPresencialDisciplinaVOs == null) {
			matriculaProvaPresencialDisciplinaVOs = new ArrayList<MatriculaProvaPresencialDisciplinaVO>(0);
		}
		return matriculaProvaPresencialDisciplinaVOs;
	}

	public void setMatriculaProvaPresencialDisciplinaVOs(List<MatriculaProvaPresencialDisciplinaVO> matriculaProvaPresencialDisciplinaVOs) {
		this.matriculaProvaPresencialDisciplinaVOs = matriculaProvaPresencialDisciplinaVOs;
	}

	public Boolean getApresentarAno() {
		return getGabaritoVO().getCursoVO().getPeriodicidade().equals("AN") || getGabaritoVO().getCursoVO().getPeriodicidade().equals("SE");
	}

	public Boolean getApresentarSemestre() {
		return getGabaritoVO().getCursoVO().getPeriodicidade().equals("SE");
	}

	public Boolean getIsTipoRespostaGabaritoDisciplina() {
		return getGabaritoVO().getTipoRespostaGabaritoEnum().name().equals(TipoRespostaGabaritoEnum.DISCIPLINA.name());
	}

	public Boolean getIsTipoRespostaGabaritoAreaConhecimento() {
		return getGabaritoVO().getTipoRespostaGabaritoEnum().name().equals(TipoRespostaGabaritoEnum.AREA_CONHECIMENTO.name());
	}
	
	private List<SelectItem> listaSelectItemTipoProcessamentoProvaPresencial;

	public List<SelectItem> getListaSelectItemTipoProcessamentoProvaPresencial() {
		if (listaSelectItemTipoProcessamentoProvaPresencial == null) {
			listaSelectItemTipoProcessamentoProvaPresencial = new ArrayList<SelectItem>(0);
			listaSelectItemTipoProcessamentoProvaPresencial.add(new SelectItem(TipoProcessamentoProvaPresencial.LEITURA_ARQUIVO_NOTA_LANCADA, "Leitura Arquivo com Nota Lançada"));
			listaSelectItemTipoProcessamentoProvaPresencial.add(new SelectItem(TipoProcessamentoProvaPresencial.LEITURA_ARQUIVO_RESPOSTA_GABARITO, "Leitura Arquivo com Resposta do Gabarito"));

		}
		return listaSelectItemTipoProcessamentoProvaPresencial;
	}
	
	public Boolean getApresentarDadosLeituraArquivoNotaLancada() {
		return getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoProcessamentoProvaPresencialEnum().name().equals(TipoProcessamentoProvaPresencial.LEITURA_ARQUIVO_NOTA_LANCADA.name());
	}

	public void montarListaSelectItemConfiguracaoAcademico() {
		try {
			List<ConfiguracaoAcademicoVO> resultadoConsulta = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracaoAcademicoDeTodasConfiguracoesNivelCombobox(false, getUsuarioLogado());
			setListaSelectItemConfiguracaoAcademico(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void montarListaSelectItemVariavelNota() {
		try {
			List<String> resultadoConsulta = null;
			if (!getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
				resultadoConsulta = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarVariavelNotaPorConfiguracaoAcademico(getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getConfiguracaoAcademicoVO().getCodigo(), getUsuarioLogado());

				List<SelectItem> objs = new ArrayList<>(0);
				Iterator<String> j = resultadoConsulta.iterator();
				while (j.hasNext()) {
					String item = (String) j.next();
					objs.add(new SelectItem(item.toUpperCase().toString(), item.toUpperCase().toString()));
				}
				SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
				Collections.sort((List<SelectItem>) objs, ordenador);
				setListaSelectItemVariavelNota(objs);
			} else {
				getListaSelectItemVariavelNota().clear();
			}

		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemConfiguracaoAcademico() {
		if (listaSelectItemConfiguracaoAcademico == null) {
			listaSelectItemConfiguracaoAcademico = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemConfiguracaoAcademico);
	}

	public void setListaSelectItemConfiguracaoAcademico(List<SelectItem> listaSelectItemConfiguracaoAcademico) {
		this.listaSelectItemConfiguracaoAcademico = listaSelectItemConfiguracaoAcademico;
	}
	
	public List<SelectItem> getListaSelectItemVariavelNota() {
		if (listaSelectItemVariavelNota == null) {
			listaSelectItemVariavelNota = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemVariavelNota;
	}

	public void setListaSelectItemVariavelNota(List<SelectItem> listaSelectItemVariavelNota) {
		this.listaSelectItemVariavelNota = listaSelectItemVariavelNota;
	}
	
	private List<SelectItem> listaSelectItemPeriodicidadeCurso;

	public List<SelectItem> getListaSelectItemPeriodicidadeCurso() {
		if (listaSelectItemPeriodicidadeCurso == null) {
			listaSelectItemPeriodicidadeCurso = new ArrayList<SelectItem>(0);
			listaSelectItemPeriodicidadeCurso.add(new SelectItem("SE", "Semestral"));
			listaSelectItemPeriodicidadeCurso.add(new SelectItem("AN", "Anual"));
			listaSelectItemPeriodicidadeCurso.add(new SelectItem("IN", "Integral"));

		}
		return listaSelectItemPeriodicidadeCurso;
	}

	public Boolean getApresentarAnoArquivoNotaLancada() {
		return getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoProcessamentoProvaPresencialEnum().name().equals(TipoProcessamentoProvaPresencial.LEITURA_ARQUIVO_NOTA_LANCADA.name()) 
				&& (getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getPeriodicidadeCurso().equals("SE") || getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getPeriodicidadeCurso().equals("AN"));
	}

	public Boolean getApresentarSemestreArquivoNotaLancada() {
		return getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoProcessamentoProvaPresencialEnum().name().equals(TipoProcessamentoProvaPresencial.LEITURA_ARQUIVO_NOTA_LANCADA.name()) 
				&& (getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getPeriodicidadeCurso().equals("SE"));
	}
	
	public Boolean getDesabilitarCampoLeituraArquivoComNotaLancada() {
		return getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoProcessamentoProvaPresencialEnum().name().equals(TipoProcessamentoProvaPresencial.LEITURA_ARQUIVO_NOTA_LANCADA.name())
				&& !this.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getCodigo().equals(0);
	}
	
	public Boolean getApresentarBotaoGravar() {
		return this.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().isNovoObj();
	}
	
	public void realizarInicioProgressBar(){
		setProgressBarVO(new ProgressBarVO());		
		try {
			getProgressBarVO().resetar();
			getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().iniciar(0l, getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getMatriculaProvaPresencialVOs().size() + 1, "Iniciando Processamento do(s) resultado(s).", true, this, "persistir");			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public ProgressBarVO getProgressBarVO() {
		if (progressBarVO == null) {
			progressBarVO = new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}

	public Boolean getDeveApresentarModalErro() {
		if (deveApresentarModalErro == null) {
			deveApresentarModalErro = Boolean.FALSE;
		}
		return deveApresentarModalErro;
	}

	public void setDeveApresentarModalErro(Boolean deveApresentarModalErro) {
		this.deveApresentarModalErro = deveApresentarModalErro;
	}
	
	public void preencherTodosListaAluno(String situacao) {
		getFacadeFactory().getMatriculaProvaPresencialFacade().preencherTodosListaAluno(getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getMatriculaProvaPresencialVOs(), situacao);
	}
	
	public void desmarcarTodosListaAluno(String situacao) {
		getFacadeFactory().getMatriculaProvaPresencialFacade().desmarcarTodosListaAluno(getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getMatriculaProvaPresencialVOs(), situacao);
	}

	public Boolean getApresentarBotaoMensagemErro() {
		return !getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getCodigo().equals(0);
	}

	private List<SelectItem> listaSelectItemTipoAlteracaoSituacaoHistorico;
	
	public List<SelectItem> getListaSelectItemTipoAlteracaoSituacaoHistorico() {
		if (listaSelectItemTipoAlteracaoSituacaoHistorico == null) {
			listaSelectItemTipoAlteracaoSituacaoHistorico = new ArrayList<SelectItem>(0);
			listaSelectItemTipoAlteracaoSituacaoHistorico.add(new SelectItem(TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS, "Todos Históricos"));
			listaSelectItemTipoAlteracaoSituacaoHistorico.add(new SelectItem(TipoAlteracaoSituacaoHistoricoEnum.APENAS_APROVADOS, "Apenas Aprovados"));
			listaSelectItemTipoAlteracaoSituacaoHistorico.add(new SelectItem(TipoAlteracaoSituacaoHistoricoEnum.APENAS_REPROVADOS, "Apenas Reprovados"));
			listaSelectItemTipoAlteracaoSituacaoHistorico.add(new SelectItem(TipoAlteracaoSituacaoHistoricoEnum.APENAS_REPROVADOS_POR_FALTA, "Apenas Reprovados Por Falta"));
			listaSelectItemTipoAlteracaoSituacaoHistorico.add(new SelectItem(TipoAlteracaoSituacaoHistoricoEnum.NENHUM, "Nenhum"));

		}
		return listaSelectItemTipoAlteracaoSituacaoHistorico;
	}

	public List<MatriculaProvaPresencialRespostaVO> getMatriculaProvaPresencialRespostaVOs() {
		return matriculaProvaPresencialRespostaVOs;
	}

	public void setMatriculaProvaPresencialRespostaVOs(
			List<MatriculaProvaPresencialRespostaVO> matriculaProvaPresencialRespostaVOs) {
		this.matriculaProvaPresencialRespostaVOs = matriculaProvaPresencialRespostaVOs;
	}

	public List<GabaritoRespostaVO> getListaGabaritorespostaVO() {
		if (listaGabaritorespostaVO == null) {
			listaGabaritorespostaVO = new ArrayList<GabaritoRespostaVO>();
		}
		return listaGabaritorespostaVO;
	}

	public void setListaGabaritorespostaVO(List<GabaritoRespostaVO> listaGabaritorespostaVO) {
		this.listaGabaritorespostaVO = listaGabaritorespostaVO;
	}

		
	
}
