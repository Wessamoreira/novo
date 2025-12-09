/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.bancocurriculum;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.bancocurriculum.enumeradores.SituacaoReferenteVagaEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;

/**
 *
 * @author PEDRO
 */
public class CandidatosVagasVO extends SuperVO {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5971393691462005889L;
	private Integer codigo;
    private VagasVO vaga;
    private PessoaVO pessoa;
    private SituacaoReferenteVagaEnum situacaoReferenteVaga;
    private Integer idade;
    private String situacaoCurso;
    private String nomeCurso;
    private List<CandidatoVagaQuestaoVO> candidatoVagaQuestaoVOs;
    private Boolean existeArquivoAdicional;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
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

    public Boolean getIsCandidatoDesqualificado() {
        if (getSituacaoReferenteVaga().equals(SituacaoReferenteVagaEnum.DESQUALIFICADO)) {
            return true;
        }
        return false;
    }

    public Boolean getIsCandidatoSelecionado() {
        if (getSituacaoReferenteVaga().equals(SituacaoReferenteVagaEnum.SELECIONADO)) {
            return true;
        }
        return false;
    }

    public Boolean getIsCandidatoProcessoSeletivo() {
        if (getSituacaoReferenteVaga().equals(SituacaoReferenteVagaEnum.PROCESSO_SELETIVO)) {
            return true;
        }
        return false;
    }

    public SituacaoReferenteVagaEnum getSituacaoReferenteVaga() {
        if (situacaoReferenteVaga == null) {
            situacaoReferenteVaga = SituacaoReferenteVagaEnum.NENHUM;
        }
        return situacaoReferenteVaga;
    }

    public void setSituacaoReferenteVaga(SituacaoReferenteVagaEnum situacaoReferenteVaga) {
        this.situacaoReferenteVaga = situacaoReferenteVaga;
    }

    public VagasVO getVaga() {
        if (vaga == null) {
            vaga = new VagasVO();
        }
        return vaga;
    }

    public void setVaga(VagasVO vaga) {
        this.vaga = vaga;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public String getSituacaoCurso() {
        return situacaoCurso;
    }

    public void setSituacaoCurso(String situacaoCurso) {
        this.situacaoCurso = situacaoCurso;
    }

    public String getSituacaoCurso_Apresentar() {
        try {
            return SituacaoVinculoMatricula.getDescricao(getSituacaoCurso());
        } catch (Exception e) {
            return SituacaoVinculoMatricula.getDescricao("ER");
        }
    }

	public List<CandidatoVagaQuestaoVO> getCandidatoVagaQuestaoVOs() {
		if(candidatoVagaQuestaoVOs == null){
			candidatoVagaQuestaoVOs = new ArrayList<CandidatoVagaQuestaoVO>(0);
		}
		return candidatoVagaQuestaoVOs;
	}

	public void setCandidatoVagaQuestaoVOs(List<CandidatoVagaQuestaoVO> candidatoVagaQuestaoVOs) {
		this.candidatoVagaQuestaoVOs = candidatoVagaQuestaoVOs;
	}
	
	public Boolean getExisteQuestionario(){
		return !getCandidatoVagaQuestaoVOs().isEmpty();
	}

	public Boolean getExisteArquivoAdicional() {
		if(existeArquivoAdicional == null){
			existeArquivoAdicional = false;
		}
		return existeArquivoAdicional;
	}

	public void setExisteArquivoAdicional(Boolean existeArquivoAdicional) {
		this.existeArquivoAdicional = existeArquivoAdicional;
	}
    
    
}
