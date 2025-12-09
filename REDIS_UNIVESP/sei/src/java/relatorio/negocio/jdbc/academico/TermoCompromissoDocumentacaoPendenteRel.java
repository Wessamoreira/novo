package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.TermoCompromissoDocumentacaoPendenteRelVO;
import relatorio.negocio.interfaces.academico.TermoCompromissoDocumentacaoPendenteRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class TermoCompromissoDocumentacaoPendenteRel extends SuperRelatorio implements TermoCompromissoDocumentacaoPendenteRelInterfaceFacade {

    public TermoCompromissoDocumentacaoPendenteRel() {

    }

    public List<TermoCompromissoDocumentacaoPendenteRelVO> criarObjeto(MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
        List<TermoCompromissoDocumentacaoPendenteRelVO> listaRelatorio = new ArrayList<TermoCompromissoDocumentacaoPendenteRelVO>(0);
        if (matriculaVO.getDocumetacaoMatriculaVOs().isEmpty()) {
            matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matriculaVO.getMatricula(), 0, NivelMontarDados.TODOS, usuarioVO);
        }
        getFacadeFactory().getPessoaFacade().carregarDados(matriculaVO.getAluno(), usuarioVO);
        listaRelatorio.add(montarDados(matriculaVO, configuracaoGeralSistemaVO, usuarioVO));
        return listaRelatorio;
    }

    private TermoCompromissoDocumentacaoPendenteRelVO montarDados(MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
        TermoCompromissoDocumentacaoPendenteRelVO termoCompromissoDocumentacaoPendenteRelVO = new TermoCompromissoDocumentacaoPendenteRelVO();
        termoCompromissoDocumentacaoPendenteRelVO.setNomeAluno(matriculaVO.getAluno().getNome());
        termoCompromissoDocumentacaoPendenteRelVO.setMatricula(matriculaVO.getMatricula());
        termoCompromissoDocumentacaoPendenteRelVO.setEndereco(matriculaVO.getAluno().getEndereco() + "  Nº: " + matriculaVO.getAluno().getNumero());
        termoCompromissoDocumentacaoPendenteRelVO.setComplemento(matriculaVO.getAluno().getComplemento());
        termoCompromissoDocumentacaoPendenteRelVO.setBairro(matriculaVO.getAluno().getSetor());
        termoCompromissoDocumentacaoPendenteRelVO.setCidade(matriculaVO.getAluno().getCidade().getNome());
        termoCompromissoDocumentacaoPendenteRelVO.setEstado(matriculaVO.getAluno().getCidade().getEstado().getSigla());
        termoCompromissoDocumentacaoPendenteRelVO.setCEP(matriculaVO.getAluno().getCEP());
        termoCompromissoDocumentacaoPendenteRelVO.setEmail(matriculaVO.getAluno().getEmail());
        termoCompromissoDocumentacaoPendenteRelVO.setTelefoneRes(matriculaVO.getAluno().getTelefoneRes());
        termoCompromissoDocumentacaoPendenteRelVO.setCelular(matriculaVO.getAluno().getCelular());
        termoCompromissoDocumentacaoPendenteRelVO.setRG(matriculaVO.getAluno().getRG());
        termoCompromissoDocumentacaoPendenteRelVO.setOrgaoExpedidor(matriculaVO.getAluno().getOrgaoEmissor());
        termoCompromissoDocumentacaoPendenteRelVO.setEstadoExpedidor(matriculaVO.getAluno().getEstadoEmissaoRG());
        termoCompromissoDocumentacaoPendenteRelVO.setCPF(matriculaVO.getAluno().getCPF());
        termoCompromissoDocumentacaoPendenteRelVO.setNrDiasLimiteEntregaDocumento(configuracaoGeralSistemaVO.getNrDiasLimiteEntregaDocumento().toString());
        for (FiliacaoVO filiacaoVO : matriculaVO.getAluno().getFiliacaoVOs()) {
            if (filiacaoVO.getTipo().equals("MA")) {
                termoCompromissoDocumentacaoPendenteRelVO.setNomeMae(filiacaoVO.getNome());
            } else if (filiacaoVO.getTipo().equals("PA")) {
                termoCompromissoDocumentacaoPendenteRelVO.setNomePai(filiacaoVO.getNome());
            }
        }
        termoCompromissoDocumentacaoPendenteRelVO.setCurso(matriculaVO.getCurso().getNome());
        termoCompromissoDocumentacaoPendenteRelVO.setTurno(matriculaVO.getTurno().getNome());
        for (DocumetacaoMatriculaVO documentacaoMatriculaVO : matriculaVO.getDocumetacaoMatriculaVOs()) {
            if (!documentacaoMatriculaVO.getEntregue()) {
                termoCompromissoDocumentacaoPendenteRelVO.getDocumentacaoMatriculaVOs().add(documentacaoMatriculaVO);
            }
        }
        termoCompromissoDocumentacaoPendenteRelVO.setUnidadeEnsino(matriculaVO.getUnidadeEnsino().getNome());
        if (usuarioVO.getUnidadeEnsinoLogado().getCodigo() == 0) {
            if (matriculaVO.getUnidadeEnsino().getCidade().getNome().equals("")) {
            	matriculaVO.setUnidadeEnsino(getAplicacaoControle().getUnidadeEnsinoVO(matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO));            	
            }
        	termoCompromissoDocumentacaoPendenteRelVO.setLocalData(matriculaVO.getUnidadeEnsino().getCidade().getNome() + ", " + Uteis.getDataAtual());
        } else {
            termoCompromissoDocumentacaoPendenteRelVO.setLocalData(usuarioVO.getUnidadeEnsinoLogado().getCidade().getNome() + ", " + Uteis.getDataAtual());
        }
        return termoCompromissoDocumentacaoPendenteRelVO;
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("TermoCompromissoDocumentacaoPendenteRel");
    }

    @Override
    public void ValidarDados(MatriculaVO matriculaVO) throws ConsistirException {
        if (matriculaVO == null || matriculaVO.getAluno() == null || matriculaVO.getAluno().getCodigo() == 0 || matriculaVO.getAluno().getNome() == null
            || matriculaVO.getAluno().getNome().equals("")) {
            throw new ConsistirException("A Matrícula deve ser informada para a geração do relatório.");
        }
    }
}
