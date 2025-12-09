package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.EnumControle;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DisciplinaAproveitadaAlteradaMatriculaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.crm.enumerador.SexoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.TipoHistorico;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("DisciplinaAproveitadaAlteradaMatriculaControle")
@Scope("viewScope")
@Lazy
public class DisciplinaAproveitadaAlteradaMatriculaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;

	private DisciplinaAproveitadaAlteradaMatriculaVO disciplinaAproveitadaAlteradaMatriculaVOCidade;
	private List<DisciplinaAproveitadaAlteradaMatriculaVO> disciplinaAproveitadaAlteradaMatriculaVOs;
	private MatriculaVO matriculaVO;
	private GradeCurricularVO gradeCurricularVO;

	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;

	private CidadeVO cidadeVO;
	private String valorConsultaCidade;
	private String campoConsultaCidade;

	private List<CidadeVO> listaConsultaCidade;
    private String nomeProfessor;
	private String titulacaoProfessor;
	private List<SelectItem> comboboxSexoEnum;

	public DisciplinaAproveitadaAlteradaMatriculaControle() {
		super();
	}

	public void consultarAproveitamento() {
		try {
			setDisciplinaAproveitadaAlteradaMatriculaVOs(getFacadeFactory().getDisciplinaAproveitadaAlteradaMatriculaFacade().consultarAproveitamentoPorMatricula(getMatriculaVO(), getGradeCurricularVO(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String novo() {
		removerObjetoMemoria(this);
		setMatriculaVO(new MatriculaVO());
		setGradeCurricularVO(new GradeCurricularVO());
		getDisciplinaAproveitadaAlteradaMatriculaVOs().clear();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("disciplinaAproveitadaAlteradaMatriculaForm");
	}

	public String persistir() {
		try {
			getFacadeFactory().getDisciplinaAproveitadaAlteradaMatriculaFacade().incluirDisciplinaAproveitadaAlteradaMatricula(getDisciplinaAproveitadaAlteradaMatriculaVOs(), getMatriculaVO(), getGradeCurricularVO(), getUsuarioLogado());
			consultarAproveitamento();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}
	
	private List<SelectItem> listaSelectItemSituacaoHistorico;
	public List<SelectItem> getListaSelectItemSituacaoHistorico() throws Exception {
		if(listaSelectItemSituacaoHistorico == null){				
			listaSelectItemSituacaoHistorico = new ArrayList<SelectItem>(0);		
			listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.APROVADO_APROVEITAMENTO,					
			SituacaoHistorico.APROVADO_APROVEITAMENTO.getDescricao()));			
			listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.CONCESSAO_CREDITO,
			SituacaoHistorico.CONCESSAO_CREDITO.getDescricao()));			
			listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.CONCESSAO_CARGA_HORARIA,
			SituacaoHistorico.CONCESSAO_CARGA_HORARIA.getDescricao()));						
			listaSelectItemSituacaoHistorico
			.add(new SelectItem(SituacaoHistorico.APROVADO, SituacaoHistorico.APROVADO.getDescricao()));				
			listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.APROVEITAMENTO_BANCA,
			SituacaoHistorico.APROVEITAMENTO_BANCA.getDescricao()));
		}	
		return listaSelectItemSituacaoHistorico;
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<>(0);
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
			setListaConsultaAluno(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
	}
	
	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
		setMatriculaVO(obj);
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
	}


	public void limparDadosAluno() throws Exception {
		removerObjetoMemoria(getMatriculaVO());
		getDisciplinaAproveitadaAlteradaMatriculaVOs().clear();
	}
	
	public void validarNota() {
    	try {
    		DisciplinaAproveitadaAlteradaMatriculaVO obj = (DisciplinaAproveitadaAlteradaMatriculaVO) context().getExternalContext().getRequestMap().get("aproveitamentoItens");
    		if (obj.getMedia() > 10) {
    			obj.setMedia(0.0);
    			throw new Exception("A nota deve ser maior que 0 e menor ou igual a 10.");
    		} else {
    			setMensagemID("");
    		}
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
	
	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboSemestre() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("1", "1º Semestre"));
		itens.add(new SelectItem("2", "2º Semestre"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboAcao() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("NENHUMA", "Nenhuma"));
		itens.add(new SelectItem("ALTERACAO", "Alteração"));
		itens.add(new SelectItem("EXCLUSAO", "Exclusão"));
		return itens;
	}
	
	public void alterarAcaoAlteracao() {
		DisciplinaAproveitadaAlteradaMatriculaVO obj = (DisciplinaAproveitadaAlteradaMatriculaVO) context().getExternalContext().getRequestMap().get("aproveitamentoItens");
		if (obj.getAcao().equals("ALTERACAO")) {
			obj.setCssAcao("");
		} else if (obj.getAcao().equals("EXCLUSAO")) {
			obj.setCssAcao("background-color: #FF6464;");
		} else {
			obj.setCssAcao("");
		}
	}
	
	public void consultarCidade() {
        try {
            List<CidadeVO> objs = new ArrayList<>(0);
            if (getCampoConsultaCidade().equals("nome")) {
                if (getValorConsultaCidade().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getCidadeFacade().consultaRapidaPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
            }
            setListaConsultaCidade(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCidade(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }
	
	public void selecionarCidadeAproveitamento() {
		DisciplinaAproveitadaAlteradaMatriculaVO obj = (DisciplinaAproveitadaAlteradaMatriculaVO) context().getExternalContext().getRequestMap().get("aproveitamentoItens");
		setDisciplinaAproveitadaAlteradaMatriculaVOCidade(obj);
	}
	
	public void limparDadosCidade() {
		DisciplinaAproveitadaAlteradaMatriculaVO obj = (DisciplinaAproveitadaAlteradaMatriculaVO) context().getExternalContext().getRequestMap().get("aproveitamentoItens");
		obj.setCidadeVO(new CidadeVO());
    }
	
    public void selecionarCidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
        getDisciplinaAproveitadaAlteradaMatriculaVOCidade().setCidadeVO(obj);
        getListaConsultaCidade().clear();
        this.setValorConsultaCidade("");
        this.setCampoConsultaCidade("");
        setDisciplinaAproveitadaAlteradaMatriculaVOCidade(new DisciplinaAproveitadaAlteradaMatriculaVO());
    }

    public List<SelectItem> getTipoConsultaCidade() {
        List<SelectItem> itens = new ArrayList<>(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
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

	public List<DisciplinaAproveitadaAlteradaMatriculaVO> getDisciplinaAproveitadaAlteradaMatriculaVOs() {
		if (disciplinaAproveitadaAlteradaMatriculaVOs == null) {
			disciplinaAproveitadaAlteradaMatriculaVOs = new ArrayList<DisciplinaAproveitadaAlteradaMatriculaVO>(0);
		}
		return disciplinaAproveitadaAlteradaMatriculaVOs;
	}

	public void setDisciplinaAproveitadaAlteradaMatriculaVOs(List<DisciplinaAproveitadaAlteradaMatriculaVO> disciplinaAproveitadaAlteradaMatriculaVOs) {
		this.disciplinaAproveitadaAlteradaMatriculaVOs = disciplinaAproveitadaAlteradaMatriculaVOs;
	}

	public GradeCurricularVO getGradeCurricularVO() {
		if (gradeCurricularVO == null) {
			gradeCurricularVO = new GradeCurricularVO();
		}
		return gradeCurricularVO;
	}

	public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
		this.gradeCurricularVO = gradeCurricularVO;
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
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public CidadeVO getCidadeVO() {
		if (cidadeVO == null) {
			cidadeVO =  new CidadeVO();
		}
		return cidadeVO;
	}

	public void setCidadeVO(CidadeVO cidadeVO) {
		this.cidadeVO = cidadeVO;
	}

	public String getValorConsultaCidade() {
		if (valorConsultaCidade == null) {
			valorConsultaCidade = "";
		}
		return valorConsultaCidade;
	}

	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}

	public String getCampoConsultaCidade() {
		if (campoConsultaCidade == null) {
			campoConsultaCidade = "";
		}
		return campoConsultaCidade;
	}

	public void setCampoConsultaCidade(String campoConsultaCidade) {
		this.campoConsultaCidade = campoConsultaCidade;
	}

	public List<CidadeVO> getListaConsultaCidade() {
		if (listaConsultaCidade == null) {
			listaConsultaCidade = new ArrayList<>(0);
		}
		return listaConsultaCidade;
	}

	public void setListaConsultaCidade(List<CidadeVO> listaConsultaCidade) {
		this.listaConsultaCidade = listaConsultaCidade;
	}

	public DisciplinaAproveitadaAlteradaMatriculaVO getDisciplinaAproveitadaAlteradaMatriculaVOCidade() {
		if (disciplinaAproveitadaAlteradaMatriculaVOCidade == null) {
			disciplinaAproveitadaAlteradaMatriculaVOCidade = new DisciplinaAproveitadaAlteradaMatriculaVO();
		}
		return disciplinaAproveitadaAlteradaMatriculaVOCidade;
	}

	public void setDisciplinaAproveitadaAlteradaMatriculaVOCidade(DisciplinaAproveitadaAlteradaMatriculaVO disciplinaAproveitadaAlteradaMatriculaVOCidade) {
		this.disciplinaAproveitadaAlteradaMatriculaVOCidade = disciplinaAproveitadaAlteradaMatriculaVOCidade;
	}
	
	public List<SelectItem> getListaSelectItemTipoHistorico() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoHistorico.class, false);
	}
	
	public boolean getIsApresentarAno() {
		if (getMatriculaVO().getCurso().getSemestral() || getMatriculaVO().getCurso().getAnual()) {
			getDisciplinaAproveitadaAlteradaMatriculaVOCidade().setAno(Uteis.getAnoDataAtual4Digitos());
			return true;
		} else {
			getDisciplinaAproveitadaAlteradaMatriculaVOCidade().setAno("");
			getDisciplinaAproveitadaAlteradaMatriculaVOCidade().setSemestre("");
		}
		return false;
	}

	public boolean getIsApresentarSemestre() {
		if (getMatriculaVO().getCurso().getSemestral()) {
			getDisciplinaAproveitadaAlteradaMatriculaVOCidade().setSemestre(Uteis.getSemestreAtual());
			return true;
		} else {
			getDisciplinaAproveitadaAlteradaMatriculaVOCidade().setSemestre("");
		}
		return false;
	}

	public String getNomeProfessor() {
		if (nomeProfessor == null) {
			nomeProfessor = "";
		}
		return nomeProfessor;
	}

	public void setNomeProfessor(String nomeProfessor) {
		this.nomeProfessor = nomeProfessor;
	}

	public String getTitulacaoProfessor() {
		if (titulacaoProfessor == null) {
			titulacaoProfessor = "";
		}
		return titulacaoProfessor;
	}

	public void setTitulacaoProfessor(String titulacaoProfessor) {
		this.titulacaoProfessor = titulacaoProfessor;
	}

	public List<SelectItem> getComboboxSexoEnum() {
		if (comboboxSexoEnum == null) {
			comboboxSexoEnum = new ArrayList<SelectItem>(0);
			comboboxSexoEnum.add(new SelectItem("", ""));
			comboboxSexoEnum.add(new SelectItem(SexoEnum.F.name(), "Feminino"));
			comboboxSexoEnum.add(new SelectItem(SexoEnum.M.name(), "Masculino"));
		}
		return comboboxSexoEnum;
	}

	public void setComboboxSexoEnum(List<SelectItem> comboboxSexoEnum) {
		this.comboboxSexoEnum = comboboxSexoEnum;
	}
	
	
}
