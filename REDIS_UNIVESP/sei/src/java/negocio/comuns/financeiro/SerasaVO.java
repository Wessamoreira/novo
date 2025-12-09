package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class SerasaVO extends SuperVO {

    private Integer codigo;
    private Date dataGeracao;
    private UsuarioVO responsavel;
    private UnidadeEnsinoVO unidadeEnsino;
    private ArquivoVO arquivoSerasa;
    private String logon;
    private String contatoInstituicao;
    private String telefoneInstituicao;
    private String ramal;
    public static final long serialVersionUID = 1L;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Date getDataGeracao() {
        if (dataGeracao == null) {
            dataGeracao = new Date();
        }
        return dataGeracao;
    }

    public void setDataGeracao(Date dataGeracao) {
        this.dataGeracao = dataGeracao;
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

    public void setArquivoSerasa(ArquivoVO arquivoSerasa) {
        this.arquivoSerasa = arquivoSerasa;
    }

    public ArquivoVO getArquivoSerasa() {
        if (arquivoSerasa == null) {
            arquivoSerasa = new ArquivoVO();
        }
        return arquivoSerasa;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setLogon(String logon) {
        this.logon = logon;
    }

    public String getLogon() {
        if (logon == null) {
            logon = "";
        }
        return logon;
    }

    public void setContatoInstituicao(String contatoInstituicao) {
        this.contatoInstituicao = contatoInstituicao;
    }

    public String getContatoInstituicao() {
        if (contatoInstituicao == null) {
            contatoInstituicao = "";
        }
        return contatoInstituicao;
    }

    public String getTelefoneInstituicao() {
        if (telefoneInstituicao == null) {
            telefoneInstituicao = "";
        }
        return telefoneInstituicao;
    }

    public void setTelefoneInstituicao(String telefoneInstituicao) {
        this.telefoneInstituicao = telefoneInstituicao;
    }

    public String getRamal() {
        if (ramal == null) {
            ramal = "";
        }
        return ramal;
    }

    public void setRamal(String ramal) {
        this.ramal = ramal;
    }
}
