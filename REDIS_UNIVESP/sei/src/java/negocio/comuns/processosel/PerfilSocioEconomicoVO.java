package negocio.comuns.processosel;

import java.util.Iterator;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.processosel.QuestionarioAluno;
import negocio.interfaces.processosel.QuestionarioAlunoInterfaceFacade;

/**
 * Reponsável por manter os dados da entidade PerfilSocioEconomico. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PerfilSocioEconomicoVO extends SuperVO {

    private Integer codigo;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO pessoa;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Questionario </code>.
     */
    private QuestionarioVO questionario;
    private String texto; // Atributo não faz persistência no Alunobanco. Será
    // gravado em QuestionarioAluno.
    private QuestionarioAlunoInterfaceFacade questionarioAlunoFacade = null;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PerfilSocioEconomico</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public PerfilSocioEconomicoVO() {
        super();
        inicializarFacade();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PerfilSocioEconomicoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PerfilSocioEconomicoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getPessoa() == null) || (obj.getPessoa().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PESSOA (Perfil Socio Econômico) deve ser informado.");
        }
        if ((obj.getQuestionario() == null) || (obj.getQuestionario().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo QUESTIONÁRIO (Perfil Socio Econômico) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setTexto("");
    }

    // public void validarDadosPergunta(RespostaPerguntaVO obj) {
    // Iterator i = getQuestionario().getPerguntaQuestionarioVOs().iterator();
    // while (i.hasNext()) {
    // PerguntaVO pergunta = (PerguntaVO) i.next();
    // if (pergunta.getCodigo() == obj.getPergunta()) {
    // validarDadosResposta(obj, pergunta);
    // return;
    // }
    // }
    //
    // }
    //
    // public void validarDadosResposta(RespostaPerguntaVO obj, PerguntaVO
    // pergunta) {
    // Iterator i = pergunta.getRespostaPerguntaVOs().iterator();
    // while (i.hasNext()) {
    // RespostaPerguntaVO objExistente = (RespostaPerguntaVO) i.next();
    // if (!objExistente.getCodigo().equals(obj.getCodigo())) {
    // objExistente.setSelecionado(Boolean.FALSE);
    // }
    // }
    // }
    public void varrerListaQuestionarioRetornarPerguntaRespondida(RespostaPerguntaVO obj) {

        Iterator i = getQuestionario().getPerguntaQuestionarioVOs().iterator();
        while (i.hasNext()) {
            PerguntaQuestionarioVO objExistente = (PerguntaQuestionarioVO) i.next();
            if (objExistente.getPergunta().getCodigo().equals(obj.getPergunta())) {
                if (objExistente.getPergunta().getTipoRespostaSimplesEscolha()) {
                    objExistente.getPergunta().setarValorFalsoSimplesEscolha(obj.getCodigo());
                }
                return;
            }
        }

    }

//    public List gerarListaQuestionarioAluno(PerfilSocioEconomicoVO obj) {
//        List QuestionarioAlunoVOs = new ArrayList();
//        QuestionarioAlunoVO novoObjeto = new QuestionarioAlunoVO();
//
//        Iterator i = obj.getQuestionario().getPerguntaQuestionarioVOs().iterator();
//        while (i.hasNext()) {
//            PerguntaQuestionarioVO objExistente = (PerguntaQuestionarioVO) i.next();
//
//            if (!objExistente.getPergunta().getTipoRespostaTextual()) {
//                Iterator j = objExistente.getPergunta().getRespostaPerguntaVOs().iterator();
//                while (j.hasNext()) {
//                    RespostaPerguntaVO objExistenteResposta = (RespostaPerguntaVO) j.next();
//                    if (objExistenteResposta.getSelecionado()) {
//                        novoObjeto.setPerfilSocioEconomico(obj.getCodigo());
//                        novoObjeto.setPerguntaQuestionario(objExistente.getPergunta());
//                        novoObjeto.setTipoResposta(objExistente.getPergunta().getTipoResposta());
//                        novoObjeto.setRespostaQuestionarioAluno(objExistenteResposta);
//                        QuestionarioAlunoVOs.add(novoObjeto);
//                        novoObjeto = new QuestionarioAlunoVO();
//                    }
//                }
//            } else {
//                novoObjeto.setTexto(obj.getTexto());
//                novoObjeto.setPerfilSocioEconomico(obj.getCodigo());
//                novoObjeto.setPerguntaQuestionario(objExistente.getPergunta());
//                novoObjeto.setTipoResposta(objExistente.getPergunta().getTipoResposta());
//                QuestionarioAlunoVOs.add(novoObjeto);
//                novoObjeto = new QuestionarioAlunoVO();
//            }
//        }
//        return QuestionarioAlunoVOs;
//    }

    public void inicializarFacade() {
        try {
            questionarioAlunoFacade = new QuestionarioAluno();
        } catch (Exception ex) {
           // //System.out.println("Erro:" + ex.getMessage());
        }
    }

    /**
     * Retorna o objeto da classe <code>Questionario</code> relacionado com (
     * <code>PerfilSocioEconomico</code>).
     */
    public QuestionarioVO getQuestionario() {
        if (questionario == null) {
            questionario = new QuestionarioVO();
        }

        return (questionario);
    }

    /**
     * Define o objeto da classe <code>Questionario</code> relacionado com (
     * <code>PerfilSocioEconomico</code>).
     */
    public void setQuestionario(QuestionarioVO obj) {
        this.questionario = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PerfilSocioEconomico</code>).
     */
    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }

        return (pessoa);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PerfilSocioEconomico</code>).
     */
    public void setPessoa(PessoaVO obj) {
        this.pessoa = obj;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
