package negocio.comuns.secretaria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Carlos
 */
public class AtividadeComplementarVO extends SuperVO{

    private Integer codigo;
    private TurmaVO turmaVO;
    private DisciplinaVO disciplinaVO;
    private RegistroAulaVO registroAulaVO;
    private Integer qtdeHoraComplementar;
    private Date dataAtividade;
    private String ano;
    private String semestre;
    private UsuarioVO responsavel;
    private String conteudo;
    private List<AtividadeComplementarMatriculaVO> listaAtividadeComplementarMatriculaVOs;

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    public DisciplinaVO getDisciplinaVO() {
        if (disciplinaVO == null) {
            disciplinaVO = new DisciplinaVO();
        }
        return disciplinaVO;
    }

    public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
        this.disciplinaVO = disciplinaVO;
    }

    public Integer getQtdeHoraComplementar() {
        if (qtdeHoraComplementar == null) {
            qtdeHoraComplementar = 0;
        }
        return qtdeHoraComplementar;
    }

    public void setQtdeHoraComplementar(Integer qtdeHoraComplementar) {
        this.qtdeHoraComplementar = qtdeHoraComplementar;
    }

    public Date getDataAtividade() {
        if (dataAtividade == null) {
            dataAtividade = new Date();
        }
        return dataAtividade;
    }

    public String getDataAtividade_Apresentar() {
        return Uteis.getDataAno4Digitos(dataAtividade);
    }

    public void setDataAtividade(Date dataAtividade) {
        this.dataAtividade = dataAtividade;
    }

    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return responsavel;
    }

    public void setResponsavel(UsuarioVO responsavel) {
        this.responsavel = responsavel;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public List<AtividadeComplementarMatriculaVO> getListaAtividadeComplementarMatriculaVOs() {
        if (listaAtividadeComplementarMatriculaVOs == null) {
            listaAtividadeComplementarMatriculaVOs = new ArrayList(0);
        }
        return listaAtividadeComplementarMatriculaVOs;
    }

    public void setListaAtividadeComplementarMatriculaVOs(List<AtividadeComplementarMatriculaVO> listaAtividadeComplementarMatriculaVOs) {
        this.listaAtividadeComplementarMatriculaVOs = listaAtividadeComplementarMatriculaVOs;
    }

    public RegistroAulaVO getRegistroAulaVO() {
        if (registroAulaVO == null) {
            registroAulaVO = new RegistroAulaVO();
        }
        return registroAulaVO;
    }

    public void setRegistroAulaVO(RegistroAulaVO registroAulaVO) {
        this.registroAulaVO = registroAulaVO;
    }

    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

	public String getConteudo() {
		if (conteudo == null) {
			conteudo = "";
		}
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

    
}
