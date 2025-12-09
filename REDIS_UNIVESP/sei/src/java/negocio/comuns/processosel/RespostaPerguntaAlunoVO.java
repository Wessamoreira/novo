package negocio.comuns.processosel;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

public class RespostaPerguntaAlunoVO extends SuperVO {

    private Integer codigo;
    private RespostaPerguntaVO respostaQuestionarioAluno;
    private String texto;
    private String tipoResposta;
    private Integer questionarioAluno;
    private PerguntaVO perguntaQuestionario;
    public static final long serialVersionUID = 1L;

    public RespostaPerguntaAlunoVO() {
        super();
    }

    public static void validarDados(RespostaPerguntaAlunoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getQuestionarioAluno().intValue() == 0) {
            throw new ConsistirException("O campo QUESTIONARIO ALUNO (Resposta Pergunta Aluno) deve ser informado.");
        }
        if (obj.getPerguntaQuestionario().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo PERGUNTA QUESTIONARIO (Resposta Pergunta Aluno) deve ser informado.");
        }
        if (obj.getTipoResposta().equals("")) {
            throw new ConsistirException("O campo TIPO DE RESPOSTA (Resposta Pergunta Aluno) deve ser informado.");
        }
    }

    public PerguntaVO getPerguntaQuestionario() {
        if (perguntaQuestionario == null) {
            perguntaQuestionario = new PerguntaVO();
        }
        return (perguntaQuestionario);
    }

    public void setPerguntaQuestionario(PerguntaVO obj) {
        this.perguntaQuestionario = obj;
    }

    public String getTipoResposta() {
        if (tipoResposta == null) {
            tipoResposta = "";
        }
        return tipoResposta;
    }

    public String getTipoResposta_Apresentar() {
        if (tipoResposta.equals("ME")) {
            return "Múltipla-Escolha";
        }
        if (tipoResposta.equals("SE")) {
            return "Simples Escolha";
        }
        if (tipoResposta.equals("TE")) {
            return "Textual";
        }
        return tipoResposta;
    }

    public void setTipoResposta(String tipoResposta) {
        this.tipoResposta = tipoResposta;
    }

    public String getTexto() {
        if (texto == null) {
            texto = "";
        }
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public RespostaPerguntaVO getRespostaQuestionarioAluno() {
        if (respostaQuestionarioAluno == null) {
            respostaQuestionarioAluno = new RespostaPerguntaVO();
        }
        return respostaQuestionarioAluno;
    }

    public void setRespostaQuestionarioAluno(RespostaPerguntaVO respostaQuestionarioAluno) {
        this.respostaQuestionarioAluno = respostaQuestionarioAluno;
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

    public Boolean getQuestaoDoTipoTexto() {
        if (getTipoResposta().equals("TE")) {
            return true;
        }
        return false;
    }

    public Integer getQuestionarioAluno() {
        return questionarioAluno;
    }

    public void setQuestionarioAluno(Integer questionarioAluno) {
        this.questionarioAluno = questionarioAluno;
    }

}
