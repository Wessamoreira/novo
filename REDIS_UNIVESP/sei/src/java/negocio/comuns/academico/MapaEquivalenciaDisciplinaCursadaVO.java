package negocio.comuns.academico;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;

@XmlRootElement(name = "mapaEquivalenciaDisciplinaCursadaVO")
public class MapaEquivalenciaDisciplinaCursadaVO extends SuperVO {

    private static final long serialVersionUID = 7175058869705741722L;
    private Integer codigo;
    private DisciplinaVO disciplinaVO;
    /**
     * Uma vez que a discilpina registra agora somente o nome, então deve
     * ser possível ao usuário, quando for registrar uma mapa de equivalencia,
     * determina a carga horária da disciplina que o aluno irá estudar para
     * cumprir um determinado mapa. Esta carga horaria será importante para
     * localizarmos uma turma (grade) na qual o aluno poderá cursar esta disciplina.
     */
    private Integer cargaHoraria;
    private Integer numeroCreditos;
    private MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplina;
    private String variavelNota;
    /**
     * TRANSIENT
     * atributo transient que é carregado com o histórico associado ao mapa de equivalência.
     * Assim, é possível, ver a situação atual de cada disciplina que precisa ser cursada do
     * mapa de equivalencia. 
     */
    private HistoricoVO historico;
    /**
     * TRANSIENTE UTILIZADO PARA MONTAR DADOS NA TELA DE APROVEITAMENTO DE
     * DISCIPLINA
     */
    private DisciplinasAproveitadasVO disciplinasAproveitadasVO;
    private Boolean selecionadoAproveitamento;

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @XmlElement(name = "disciplinaVO")
    public DisciplinaVO getDisciplinaVO() {
        if (disciplinaVO == null) {
            disciplinaVO = new DisciplinaVO();
        }
        return disciplinaVO;
    }

    public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
        this.disciplinaVO = disciplinaVO;
    }

    public MapaEquivalenciaDisciplinaVO getMapaEquivalenciaDisciplina() {
        if (mapaEquivalenciaDisciplina == null) {
            mapaEquivalenciaDisciplina = new MapaEquivalenciaDisciplinaVO();
        }
        return mapaEquivalenciaDisciplina;
    }

    public void setMapaEquivalenciaDisciplina(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplina) {
        this.mapaEquivalenciaDisciplina = mapaEquivalenciaDisciplina;
    }

    public String getVariavelNota() {
        if (variavelNota == null) {
            variavelNota = "";
        }
        return variavelNota;
    }

    public void setVariavelNota(String variavelNota) {
        this.variavelNota = variavelNota;
    }

    /**
     * @return the cargaHoraria
     */
    @XmlElement(name = "cargaHoraria")
    public Integer getCargaHoraria() {
        if (cargaHoraria == null) {
            cargaHoraria = 0;
        }
        return cargaHoraria;
    }

    /**
     * @param cargaHoraria the cargaHoraria to set
     */
    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    /**
     * @return the historico
     */
    public HistoricoVO getHistorico() {
        if (historico == null) {
            historico = new HistoricoVO();
        }
        return historico;
    }

    /**
     * @param historico the historico to set
     */
    public void setHistorico(HistoricoVO historico) {
        this.historico = historico;
    }

    /**
     * @return the numeroCreditos
     */
    public Integer getNumeroCreditos() {
        if (numeroCreditos == null) {
            numeroCreditos = 0;
        }
        return numeroCreditos;
    }

    /**
     * @param numeroCreditos the numeroCreditos to set
     */
    public void setNumeroCreditos(Integer numeroCreditos) {
        this.numeroCreditos = numeroCreditos;
    }
    
    public Boolean getDisciplinaAprovada() {
        if (getHistorico().getAprovado()) {
            return true;
        }
        return false;
    }
    
    public DisciplinasAproveitadasVO getDisciplinasAproveitadasVO() {
        if (disciplinasAproveitadasVO == null) {
            disciplinasAproveitadasVO = new DisciplinasAproveitadasVO();
        }
        return disciplinasAproveitadasVO;
    }

    public void setDisciplinasAproveitadasVO(DisciplinasAproveitadasVO disciplinasAproveitadasVO) {
        this.disciplinasAproveitadasVO = disciplinasAproveitadasVO;
    }

    public Boolean getSelecionadoAproveitamento() {
        if (selecionadoAproveitamento == null) {
            selecionadoAproveitamento = Boolean.FALSE;
        }
        return selecionadoAproveitamento;
    }

    public void setSelecionadoAproveitamento(Boolean selecionadoAproveitamento) {
        this.selecionadoAproveitamento = selecionadoAproveitamento;
    }
    
    public String getOrdenacao(){
    	return getDisciplinaVO().getNome()+"COD:"+getDisciplinaVO().getCodigo()+"CH:"+getCargaHoraria().toString();
    }
    
    public String getDisciplinaApresentar(){
		return getDisciplinaVO().getAbreviatura()+" - "+getDisciplinaVO().getNome()+" - CH: "+getCargaHoraria();
	}

}
