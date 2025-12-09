package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ContaReceberVO;


public class ContaReceberCobrancaRelVO {
    
    private String matricula;
    private String tipoDestinatario;
    private String unidadeEnsino;    
    private Integer codigoUnidadeEnsino;    
    private Double valorTotalParcela;
    private PessoaVO pessoaVO;
    private List<ContaReceberVO> contaReceberVOs;
    private GrupoDestinatariosVO grupoDestinatariosVO;
    private String emailResponsavelCobranca;
    
    public String getMatricula() {
        if(matricula == null){
            matricula = "";
        }
        return matricula;
    }
    
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
    public String getUnidadeEnsino() {
        if(unidadeEnsino == null){
            unidadeEnsino = "";
        }
        return unidadeEnsino;
    }
    
    public void setUnidadeEnsino(String unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }
    
    public Integer getQuantidadeParcela() {
        return getContaReceberVOs().size();
    }
       
    
    public Double getValorTotalParcela() {
        if(valorTotalParcela == null){
            valorTotalParcela = 0.0;
        }
        return valorTotalParcela;
    }
    
    public void setValorTotalParcela(Double valorTotalParcela) {
        this.valorTotalParcela = valorTotalParcela;
    }
    
    public PessoaVO getPessoaVO() {
        if(pessoaVO == null){
            pessoaVO = new PessoaVO();
        }
        return pessoaVO;
    }
    
    public void setPessoaVO(PessoaVO pessoaVO) {
        this.pessoaVO = pessoaVO;
    }

    
    public List<ContaReceberVO> getContaReceberVOs() {
        if(contaReceberVOs == null){
            contaReceberVOs = new ArrayList<ContaReceberVO>(0);
        }
        return contaReceberVOs;
    }

    
    public void setContaReceberVOs(List<ContaReceberVO> contaReceberVOs) {
        this.contaReceberVOs = contaReceberVOs;
    }

    
    public GrupoDestinatariosVO getGrupoDestinatariosVO() {
        if(grupoDestinatariosVO == null){
            grupoDestinatariosVO = new GrupoDestinatariosVO();
        }
        return grupoDestinatariosVO;
    }

    
    public void setGrupoDestinatariosVO(GrupoDestinatariosVO grupoDestinatariosVO) {
        this.grupoDestinatariosVO = grupoDestinatariosVO;
    }

    
    public String getTipoDestinatario() {
        if(tipoDestinatario == null){
            tipoDestinatario = "";
        }
        return tipoDestinatario;
    }

    
    public void setTipoDestinatario(String tipoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
    }

    
    public Integer getCodigoUnidadeEnsino() {
        return codigoUnidadeEnsino;
    }

    
    public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
        this.codigoUnidadeEnsino = codigoUnidadeEnsino;
    }

	public String getEmailResponsavelCobranca() {
		if(emailResponsavelCobranca == null){
			emailResponsavelCobranca = "";
		}
		return emailResponsavelCobranca;
	}

	public void setEmailResponsavelCobranca(String emailResponsavelCobranca) {
		this.emailResponsavelCobranca = emailResponsavelCobranca;
	}

    
    
    
    

}
