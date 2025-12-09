package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import webservice.certisign.comuns.DocumentPessoaRSVO;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "integracaoTechCertVO")
public class IntegracaoTechCertVO extends SuperVO {

    private String id;
    private Long codigoDocumentoAssinado;
    private String nome;
    private String parentId;
    private String organizacaoId;
    private String organizacaoNome;
    private String organizacaoIdentificador;
    private String owner;
    private String codigoprovedordeassinatura;
    private ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum;
    private String fileCodigo;
    private String documentoId;
    private String uploadId;

    private String ra;
    private String nomeAluno;
    private String matricula;
    private String nomeCurso;
    private String nomePolo;
    private String ano;
    private String semestre;
    private String estagioNome;
    private String concedenteDoEstagioNome;
    private String responsavelDoConcendenteNome;
    private String tituloColacaoGrau;
    private String infoAtaColacaoGrau;
    private OffsetDateTime expirationDate;
    private Date dataColacaoGrau;

    private List<DocumentPessoaRSVO> signers;
    private List<DocumentPessoaRSVO> electronicSigners;

    public String getId() {
        if (id == null) {
            id = "";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCodigoDocumentoAssinado() {
        if (codigoDocumentoAssinado == null) {
            codigoDocumentoAssinado = 0L;
        }
        return codigoDocumentoAssinado;
    }

    public void setCodigoDocumentoAssinado(Long codigoDocumentoAssinado) {
        this.codigoDocumentoAssinado = codigoDocumentoAssinado;
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getParentId() {
        if (parentId == null) {
            parentId = "";
        }
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrganizacaoId() {
        if (organizacaoId == null) {
            organizacaoId = "";
        }
        return organizacaoId;
    }

    public void setOrganizacaoId(String organizacaoId) {
        this.organizacaoId = organizacaoId;
    }

    public String getOrganizacaoNome() {
        if (organizacaoNome == null) {
            organizacaoNome = "";
        }
        return organizacaoNome;
    }

    public void setOrganizacaoNome(String organizacaoNome) {
        this.organizacaoNome = organizacaoNome;
    }

    public String getOrganizacaoIdentificador() {
        if (organizacaoIdentificador == null) {
            organizacaoIdentificador = "";
        }
        return organizacaoIdentificador;
    }

    public void setOrganizacaoIdentificador(String organizacaoIdentificador) {
        this.organizacaoIdentificador = organizacaoIdentificador;
    }

    public String getOwner() {
        if (owner == null) {
            owner = "";
        }
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCodigoprovedordeassinatura() {
        if (codigoprovedordeassinatura == null) {
            codigoprovedordeassinatura = "";
        }
        return codigoprovedordeassinatura;
    }

    public void setCodigoprovedordeassinatura(String codigoprovedordeassinatura) {
        this.codigoprovedordeassinatura = codigoprovedordeassinatura;
    }

    public ProvedorDeAssinaturaEnum getProvedorDeAssinaturaEnum() {
        if (provedorDeAssinaturaEnum == null) {
            provedorDeAssinaturaEnum = ProvedorDeAssinaturaEnum.TECHCERT;
        }
        return provedorDeAssinaturaEnum;
    }

    public void setProvedorDeAssinaturaEnum(ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum) {
        this.provedorDeAssinaturaEnum = provedorDeAssinaturaEnum;
    }

    public String getFileCodigo() {
        if (fileCodigo == null) {
            fileCodigo = "";
        }
        return fileCodigo;
    }

    public void setFileCodigo(String fileCodigo) {
        this.fileCodigo = fileCodigo;
    }

    public String getDocumentoId() {
        if (documentoId == null) {
            documentoId = "";
        }
        return documentoId;
    }

    public void setDocumentoId(String documentoId) {
        this.documentoId = documentoId;
    }

    public String getUploadId() {
        if (uploadId == null) {
            uploadId = "";
        }
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getRa() {
        if (ra == null) {
            ra = "";
        }
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getNomeAluno() {
        if (nomeAluno == null) {
            nomeAluno = "";
        }
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getMatricula() {
        if (matricula == null) {
            matricula = "";
        }
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNomeCurso() {
        if (nomeCurso == null) {
            nomeCurso = "";
        }
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public String getNomePolo() {
        if (nomePolo == null) {
            nomePolo = "";
        }
        return nomePolo;
    }

    public void setNomePolo(String nomePolo) {
        this.nomePolo = nomePolo;
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

    public List<DocumentPessoaRSVO> getSigners() {
        if (signers == null) {
            signers = new ArrayList<>(0);
        }
        return signers;
    }

    public void setSigners(List<DocumentPessoaRSVO> signers) {
        this.signers = signers;
    }

    public List<DocumentPessoaRSVO> getElectronicSigners() {
        if (electronicSigners == null) {
            electronicSigners = new ArrayList<>(0);
        }
        return electronicSigners;
    }

    public void setElectronicSigners(List<DocumentPessoaRSVO> electronicSigners) {
        this.electronicSigners = electronicSigners;
    }

    public String getEstagioNome() {
        if (estagioNome == null) {
            estagioNome = "";
        }
        return estagioNome;
    }

    public void setEstagioNome(String estagioNome) {
        this.estagioNome = estagioNome;
    }

    public String getConcedenteDoEstagioNome() {
        if (concedenteDoEstagioNome == null) {
            concedenteDoEstagioNome = "";
        }
        return concedenteDoEstagioNome;
    }

    public void setConcedenteDoEstagioNome(String concedenteDoEstagioNome) {
        this.concedenteDoEstagioNome = concedenteDoEstagioNome;
    }

    public String getResponsavelDoConcendenteNome() {
        if (responsavelDoConcendenteNome == null) {
            responsavelDoConcendenteNome = "";
        }
        return responsavelDoConcendenteNome;
    }

    public void setResponsavelDoConcendenteNome(String responsavelDoConcendenteNome) {
        this.responsavelDoConcendenteNome = responsavelDoConcendenteNome;
    }

    public String getTituloColacaoGrau() {
        if (tituloColacaoGrau == null) {
            tituloColacaoGrau = "";
        }
        return tituloColacaoGrau;
    }

    public void setTituloColacaoGrau(String tituloColacaoGrau) {
        this.tituloColacaoGrau = tituloColacaoGrau;
    }

    public String getInfoAtaColacaoGrau() {
        if (infoAtaColacaoGrau == null) {
            infoAtaColacaoGrau = "";
        }
        return infoAtaColacaoGrau;
    }

    public void setInfoAtaColacaoGrau(String infoAtaColacaoGrau) {
        this.infoAtaColacaoGrau = infoAtaColacaoGrau;
    }

    public OffsetDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(OffsetDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getDataColacaoGrau() {
        return dataColacaoGrau;
    }

    public void setDataColacaoGrau(Date dataColacaoGrau) {
        this.dataColacaoGrau = dataColacaoGrau;
    }
}
