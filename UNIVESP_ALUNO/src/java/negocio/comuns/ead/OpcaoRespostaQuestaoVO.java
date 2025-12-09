package negocio.comuns.ead;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class OpcaoRespostaQuestaoVO extends SuperVO {

    /**
     * 
     */
    private static final long serialVersionUID = 2609128478029817399L;

    private Integer codigo;
    private String opcaoResposta;
    private Boolean correta;
    private QuestaoVO questaoVO;
    private Integer ordemApresentacao;
    private String letraCorrespondente;
    
    /*
     * Variaveis de controle não deve ser persistido;
     */
    private Boolean marcada;    
    private Boolean editar;

    public OpcaoRespostaQuestaoVO clone() throws CloneNotSupportedException {
        OpcaoRespostaQuestaoVO clone = (OpcaoRespostaQuestaoVO) super.clone();
        clone.setCodigo(0);
        clone.setNovoObj(true);
        clone.setQuestaoVO(null);
        clone.setEditar(false);
        return clone;
    }

    public String getOpcaoResposta() {
        if (opcaoResposta == null) {
            opcaoResposta = "";
        }
        return opcaoResposta;
    }

    public void setOpcaoResposta(String opcaoResposta) {
        this.opcaoResposta = opcaoResposta;
    }

    public Boolean getCorreta() {
        if (correta == null) {
            correta = false;
        }
        return correta;
    }

    public void setCorreta(Boolean correta) {
        this.correta = correta;
    }

    public QuestaoVO getQuestaoVO() {
        if (questaoVO == null) {
            questaoVO = new QuestaoVO();
        }
        return questaoVO;
    }

    public void setQuestaoVO(QuestaoVO questaoVO) {
        this.questaoVO = questaoVO;
    }

    public Integer getOrdemApresentacao() {
        if (ordemApresentacao == null) {
            ordemApresentacao = 0;
        }
        return ordemApresentacao;
    }

    public void setOrdemApresentacao(Integer ordemApresentacao) {
        this.ordemApresentacao = ordemApresentacao;
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

    private String getLetra(Integer ordem) {
        switch (ordem) {
            case 1:
                return "A)";
            case 2:
                return "B)";
            case 3:
                return "C)";
            case 4:
                return "D)";
            case 5:
                return "E)";
            case 6:
                return "F)";
            case 7:
                return "G)";
            case 8:
                return "H)";
            case 9:
                return "I)";
            case 10:
                return "J)";
            case 11:
                return "K)";
            case 12:
                return "L)";
            case 13:
                return "M)";
            case 14:
                return "N)";
            case 15:
                return "O)";
            case 16:
                return "P)";
            case 17:
                return "Q)";
            case 18:
                return "R)";
            case 19:
                return "S)";
            case 20:
                return "T)";
            case 21:
                return "U)";
            case 22:
                return "V)";
            case 23:
                return "X)";
            case 24:
                return "Y)";
            case 25:
                return "Z)";
            case 26:
                return "W)";
            default:
                return "A)";
        }
    }

    
                   

    public String getLetraCorrespondente() {
        if (letraCorrespondente == null) {
            if (getOrdemApresentacao() > 23) {
                letraCorrespondente = "";
                for (int x = 0; x < getOrdemApresentacao().toString().length(); x++) {
                    letraCorrespondente += letraCorrespondente + getLetra(Integer.valueOf(getOrdemApresentacao().toString().substring(x, x + 1)));
                }
            } else {
                letraCorrespondente = getLetra(getOrdemApresentacao());
            }
        }
        return letraCorrespondente;
    }

    
    public void setLetraCorrespondente(String letraCorrespondente) {
        this.letraCorrespondente = letraCorrespondente;
    }

    
    public Boolean getEditar() {
        if(editar == null){
            editar = false;
        }
        return editar;
    }

    
    public void setEditar(Boolean editar) {
        this.editar = editar;
    }

    
    public Boolean getMarcada() {
        if(marcada == null){
            marcada = false;
        }
        return marcada;
    }

    
    public void setMarcada(Boolean marcada) {
        this.marcada = marcada;
    }
    
    public Boolean getTextoInformado() {
    	return !Uteis.removeHTML(getOpcaoResposta()).trim().isEmpty();
    }

}
