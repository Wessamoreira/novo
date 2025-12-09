/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FormacaoExtraCurricularVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.bancocurriculum.CandidatosVagasVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;

/**
 *
 * @author Rogerio
 */
public class BuscaCandidatoVagaVO extends SuperVO {

    private List<FormacaoAcademicaVO> formacaoAcademicaVOs;
    private CidadeVO cidade;
    /** Atributo responsável por manter os objetos da classe <code>DadosComerciaisVO</code>. */
    private Boolean ingles;
    private Boolean espanhol;
    private Boolean frances;
    private String inglesNivel;
    private String espanholNivel;
    private String francesNivel;
    private String outrosIdiomas;
    private String outrosIdiomasNivel;
    private Boolean windows;
    private Boolean word;
    private Boolean excel;
    private Boolean access;
    private Boolean powerPoint;
    private Boolean internet;
    private Boolean sap;
    private Boolean corelDraw;
    private Boolean autoCad;
    private Boolean photoshop;
    private Boolean microsiga;
    private String outrosSoftwares;
    private Boolean possuiFilho;
    private AreaProfissionalVO areaProfissional;
    private List<AreaProfissionalVO> listaAreaProfissional;
    private FormacaoAcademicaVO formacaoAcademica;
    private List<FormacaoAcademicaVO> listaFormacaoAcademica;
    private FormacaoExtraCurricularVO formacaoExtraCurricular;
    private List<FormacaoExtraCurricularVO> listaFormacaoExtraCurricular;
    private String sexo;
    private CursoVO curso;
    private PessoaVO pessoa;
    private String trabalha;
    private CandidatosVagasVO candidatosVagas;

    public Boolean getAccess() {
        return access;
    }

    public void setAccess(Boolean access) {
        this.access = access;
    }

    public Boolean getAutoCad() {
        return autoCad;
    }

    public void setAutoCad(Boolean autoCad) {
        this.autoCad = autoCad;
    }

    public CidadeVO getCidade() {
        if (cidade == null) {
            cidade = new CidadeVO();
        }
        return cidade;
    }

    public void setCidade(CidadeVO cidade) {
        this.cidade = cidade;
    }

    public Boolean getCorelDraw() {
        return corelDraw;
    }

    public void setCorelDraw(Boolean corelDraw) {
        this.corelDraw = corelDraw;
    }

    public Boolean getEspanhol() {
        return espanhol;
    }

    public void setEspanhol(Boolean espanhol) {
        this.espanhol = espanhol;
    }

    public String getEspanholNivel() {
        if (espanholNivel == null) {
            espanholNivel = "";
        }
        return espanholNivel;
    }

    public void setEspanholNivel(String espanholNivel) {
        this.espanholNivel = espanholNivel;
    }

    public Boolean getExcel() {
        return excel;
    }

    public void setExcel(Boolean excel) {
        this.excel = excel;
    }

    public List<FormacaoAcademicaVO> getFormacaoAcademicaVOs() {
        return formacaoAcademicaVOs;
    }

    public void setFormacaoAcademicaVOs(List<FormacaoAcademicaVO> formacaoAcademicaVOs) {
        this.formacaoAcademicaVOs = formacaoAcademicaVOs;
    }

    public Boolean getFrances() {
        return frances;
    }

    public void setFrances(Boolean frances) {
        this.frances = frances;
    }

    public String getFrancesNivel() {
        if (francesNivel == null) {
            francesNivel = "";
        }
        return francesNivel;
    }

    public void setFrancesNivel(String francesNivel) {
        this.francesNivel = francesNivel;
    }

    public Boolean getIngles() {
        return ingles;
    }

    public void setIngles(Boolean ingles) {
        this.ingles = ingles;
    }

    public String getInglesNivel() {
        if (inglesNivel == null) {
            inglesNivel = "";
        }
        return inglesNivel;
    }

    public void setInglesNivel(String inglesNivel) {
        this.inglesNivel = inglesNivel;
    }

    public Boolean getInternet() {
        return internet;
    }

    public void setInternet(Boolean internet) {
        this.internet = internet;
    }

    public Boolean getMicrosiga() {
        return microsiga;
    }

    public void setMicrosiga(Boolean microsiga) {
        this.microsiga = microsiga;
    }

    public String getOutrosIdiomas() {
        if (outrosIdiomas == null) {
            outrosIdiomas = "";
        }
        return outrosIdiomas;
    }

    public void setOutrosIdiomas(String outrosIdiomas) {
        this.outrosIdiomas = outrosIdiomas;
    }

    public String getOutrosIdiomasNivel() {
        if (outrosIdiomasNivel == null) {
            outrosIdiomasNivel = "";
        }
        return outrosIdiomasNivel;
    }

    public void setOutrosIdiomasNivel(String outrosIdiomasNivel) {
        this.outrosIdiomasNivel = outrosIdiomasNivel;
    }

    public String getOutrosSoftwares() {
        return outrosSoftwares;
    }

    public void setOutrosSoftwares(String outrosSoftwares) {
        this.outrosSoftwares = outrosSoftwares;
    }

    public Boolean getPhotoshop() {
        return photoshop;
    }

    public void setPhotoshop(Boolean photoshop) {
        this.photoshop = photoshop;
    }

    public Boolean getPossuiFilho() {
        return possuiFilho;
    }

    public void setPossuiFilho(Boolean possuiFilho) {
        this.possuiFilho = possuiFilho;
    }

    public Boolean getPowerPoint() {
        return powerPoint;
    }

    public void setPowerPoint(Boolean powerPoint) {
        this.powerPoint = powerPoint;
    }

    public Boolean getSap() {
        return sap;
    }

    public void setSap(Boolean sap) {
        this.sap = sap;
    }

    public Boolean getWindows() {
        return windows;
    }

    public void setWindows(Boolean windows) {
        this.windows = windows;
    }

    public Boolean getWord() {
        return word;
    }

    public void setWord(Boolean word) {
        this.word = word;
    }

    public AreaProfissionalVO getAreaProfissional() {
        if (areaProfissional == null) {
            areaProfissional = new AreaProfissionalVO();
        }
        return areaProfissional;
    }

    public void setAreaProfissional(AreaProfissionalVO areaProfissional) {
        this.areaProfissional = areaProfissional;
    }

    public FormacaoAcademicaVO getFormacaoAcademica() {
        if (formacaoAcademica == null) {
            formacaoAcademica = new FormacaoAcademicaVO();
        }
        return formacaoAcademica;
    }

    public void setFormacaoAcademica(FormacaoAcademicaVO formacaoAcademica) {
        this.formacaoAcademica = formacaoAcademica;
    }

    public List<AreaProfissionalVO> getListaAreaProfissional() {
        if (listaAreaProfissional == null) {
            listaAreaProfissional = new ArrayList<AreaProfissionalVO>(0);
        }
        return listaAreaProfissional;
    }

    public void setListaAreaProfissional(List<AreaProfissionalVO> listaAreaProfissional) {
        this.listaAreaProfissional = listaAreaProfissional;
    }

    public List<FormacaoAcademicaVO> getListaFormacaoAcademica() {
        if (listaFormacaoAcademica == null) {
            listaFormacaoAcademica = new ArrayList<FormacaoAcademicaVO>(0);
        }
        return listaFormacaoAcademica;
    }

    public void setListaFormacaoAcademica(List<FormacaoAcademicaVO> listaFormacaoAcademica) {
        this.listaFormacaoAcademica = listaFormacaoAcademica;
    }

    public String getSexo() {
        if (sexo == null) {
            sexo = "";
        }
        return (sexo);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico.
     * Com base no valor de armazenamento do atributo esta função é capaz de retornar o
     * de apresentação correspondente. Útil para campos como sexo, escolaridade, etc.
     */
    public String getSexo_Apresentar() {
        if (sexo.equals("F")) {
            return "Feminino";
        }
        if (sexo.equals("M")) {
            return "Masculino";
        }
        return (sexo);
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public FormacaoExtraCurricularVO getFormacaoExtraCurricular() {
        if (formacaoExtraCurricular == null) {
            formacaoExtraCurricular = new FormacaoExtraCurricularVO();
        }
        return formacaoExtraCurricular;
    }

    public void setFormacaoExtraCurricular(FormacaoExtraCurricularVO formacaoExtraCurricular) {
        this.formacaoExtraCurricular = formacaoExtraCurricular;
    }

    public List<FormacaoExtraCurricularVO> getListaFormacaoExtraCurricular() {
        if (listaFormacaoExtraCurricular == null) {
            listaFormacaoExtraCurricular = new ArrayList<FormacaoExtraCurricularVO>(0);
        }
        return listaFormacaoExtraCurricular;
    }

    public void setListaFormacaoExtraCurricular(List<FormacaoExtraCurricularVO> listaFormacaoExtraCurricular) {
        this.listaFormacaoExtraCurricular = listaFormacaoExtraCurricular;
    }

    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }

    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return pessoa;
    }

    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }

    public String getTrabalha() {
        if (trabalha == null) {
            trabalha = "";
        }
        return trabalha;
    }

    public void setTrabalha(String trabalha) {
        this.trabalha = trabalha;
    }

	public CandidatosVagasVO getCandidatosVagas() {
		if(candidatosVagas == null){
			candidatosVagas = new CandidatosVagasVO();
		}
		return candidatosVagas;
	}

	public void setCandidatosVagas(CandidatosVagasVO candidatosVagas) {
		this.candidatosVagas = candidatosVagas;
	}
    
    
}
