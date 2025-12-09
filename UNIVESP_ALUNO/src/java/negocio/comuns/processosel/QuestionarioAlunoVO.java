package negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;

public class QuestionarioAlunoVO extends SuperVO {

    private Integer codigo;
    private QuestionarioVO questionario;
    private PessoaVO aluno;

    private List<RespostaPerguntaAlunoVO> listaRespostaPerguntaAlunoVO;

    private Integer peso;
    public static final long serialVersionUID = 1L;

    public QuestionarioAlunoVO() {
        super();
    }

    public static void validarDados(QuestionarioAlunoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
    }

    public Integer getCodigo() {
        if (codigo == null){
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getPeso() {
        if (peso == null){
            peso = 0;
        }
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public QuestionarioVO getQuestionario() {
        if (questionario == null){
            questionario = new QuestionarioVO();
        }
        return questionario;
    }

    public void setQuestionario(QuestionarioVO questionario) {
        this.questionario = questionario;
    }

    public PessoaVO getAluno() {
        if (aluno == null){
            aluno = new PessoaVO();
        }
        return aluno;
    }

    public void setAluno(PessoaVO aluno) {
        this.aluno = aluno;
    }

    public List<RespostaPerguntaAlunoVO> getListaRespostaPerguntaAlunoVO() {
        if (listaRespostaPerguntaAlunoVO == null){
            listaRespostaPerguntaAlunoVO = new ArrayList<RespostaPerguntaAlunoVO>(0);
        }
        return listaRespostaPerguntaAlunoVO;
    }

    public void setListaRespostaPerguntaAlunoVO(List<RespostaPerguntaAlunoVO> listaRespostaPerguntaAlunoVO) {
        this.listaRespostaPerguntaAlunoVO = listaRespostaPerguntaAlunoVO;
    }

}
