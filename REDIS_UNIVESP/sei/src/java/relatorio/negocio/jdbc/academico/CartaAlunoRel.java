package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.CartaAlunoVO;
import relatorio.negocio.interfaces.academico.CartaAlunoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class CartaAlunoRel extends SuperRelatorio implements CartaAlunoRelInterfaceFacade {

    public CartaAlunoRel() throws Exception {
    }

    /**
     * @see
     * relatorio.negocio.jdbc.academico.CartaAlunoRelInterfaceFacade#consultarPorCodigoAluno(negocio.comuns.academico
     * .MatriculaVO, java.lang.Integer)
     */
    public CartaAlunoVO consultarPorCodigoAluno(MatriculaVO matricula, Integer nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        PessoaVO obj = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(
                matricula.getAluno().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
        List lista = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(
                matricula.getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuarioVO);
        MatriculaPeriodoVO matPeriodo = (MatriculaPeriodoVO) lista.get(0);
        return montarDados(obj, matPeriodo, matricula);
    }

    /*
     * (non-Javadoc)
     *
     * @see relatorio.negocio.jdbc.academico.CartaAlunoRelInterfaceFacade#montarDados(negocio.comuns.basico.PessoaVO,
     * negocio.comuns.academico.MatriculaPeriodoVO, negocio.comuns.academico.MatriculaVO)
     */
    public CartaAlunoVO montarDados(PessoaVO pessoa, MatriculaPeriodoVO matPeriodo, MatriculaVO matricula) throws Exception {
        CartaAlunoVO obj = new CartaAlunoVO();
        obj.setMatricula(matPeriodo.getMatricula());
        obj.setNome(pessoa.getNome());
        obj.setData(Uteis.getData(pessoa.getDataNasc(), "dd 'de' MMMM"));
        obj.setEndereco(matricula.getUnidadeEnsino().getEndereco());
        obj.setBairro(matricula.getUnidadeEnsino().getSetor());
        obj.setCep(matricula.getUnidadeEnsino().getCEP());
        obj.setCidade(matricula.getUnidadeEnsino().getCidade().getNome());
        obj.setEstado(matricula.getUnidadeEnsino().getCidade().getEstado().getNome());
        obj.setEmail(matricula.getUnidadeEnsino().getEmail());
        obj.setUnidadeEnsino(matricula.getUnidadeEnsino().getNome());
        obj.setFone(matricula.getUnidadeEnsino().getTelComercial1());
        return obj;
    }

    /**
     * @author rodrigo.arantes
     * @return void
     * @throws Exception - caso dados nao sejam validados
     */
    public static void validarDados(MatriculaVO matricula) throws Exception {
        if (matricula.getMatricula() == null || matricula.getMatricula().equals("")) {
            throw new Exception("Para Emissão do Relatório é necessário informar a Matricula.");
        }
        if (matricula.getAluno() == null || matricula.getAluno().getCodigo().intValue() == 0) {
            throw new Exception("Para Emissão do Relatório é necessário informar o Aluno.");
        }
    }

    public static String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getDesignIReportRelatorio() {
        return (caminhoBaseRelatorio() + getIdEntidade() + ".jrxml");
    }

    public static String getIdEntidade() {
        return ("CartaAlunoRel");
    }
}
