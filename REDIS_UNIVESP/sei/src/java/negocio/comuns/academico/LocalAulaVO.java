package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;

@XmlRootElement(name = "localAula")
public class LocalAulaVO extends SuperVO {

    /**
     *
     */
    private static final long serialVersionUID = -8929931570016943146L;
    private UnidadeEnsinoVO unidadeEnsino;
    private String local;
    private String endereco;
    private String telefone;
    private String observacao;
    private Integer codigo;
    private StatusAtivoInativoEnum situacao;
    private List<SalaLocalAulaVO> salaLocalAulaVOs;
    private List<MaterialAlunoVO> materialAlunoVOs;
    private List<MaterialProfessorVO> materialProfessorVOs;

    @XmlElement(name = "local")
    public String getLocal() {
        if (local == null) {
            local = "";
        }
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getEndereco() {
        if (endereco == null) {
            endereco = "";
        }
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        if (telefone == null) {
            telefone = "";
        }
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getObservacao() {
        if (observacao == null) {
            observacao = "";
        }
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

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

    public List<SalaLocalAulaVO> getSalaLocalAulaVOs() {
        if (salaLocalAulaVOs == null) {
            salaLocalAulaVOs = new ArrayList<SalaLocalAulaVO>(0);
        }
        return salaLocalAulaVOs;
    }

    public void setSalaLocalAulaVOs(List<SalaLocalAulaVO> salaLocalAulaVOs) {
        this.salaLocalAulaVOs = salaLocalAulaVOs;
    }

    public StatusAtivoInativoEnum getSituacao() {
        if (situacao == null) {
            situacao = StatusAtivoInativoEnum.ATIVO;
        }
        return situacao;
    }

    public void setSituacao(StatusAtivoInativoEnum situacao) {
        this.situacao = situacao;
    }

    public List<MaterialAlunoVO> getMaterialAlunoVOs() {
        if (materialAlunoVOs == null) {
            materialAlunoVOs = new ArrayList<MaterialAlunoVO>(0);
        }
        return materialAlunoVOs;
    }

    public void setMaterialAlunoVOs(List materialAlunoVOs) {
        this.materialAlunoVOs = materialAlunoVOs;
    }

    public List<MaterialProfessorVO> getMaterialProfessorVOs() {
        if (materialProfessorVOs == null) {
            materialProfessorVOs = new ArrayList<MaterialProfessorVO>(0);
        }
        return materialProfessorVOs;
    }

    public void setMaterialProfessorVOs(List materialProfessorVOs) {
        this.materialProfessorVOs = materialProfessorVOs;
    }

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
}
