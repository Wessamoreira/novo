package relatorio.negocio.jdbc.biblioteca;

import java.util.ArrayList;
import java.util.List;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.utilitarias.Uteis;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import relatorio.negocio.interfaces.biblioteca.EtiquetaCodigoBarraRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 *
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class EtiquetaCodigoBarraRel extends SuperRelatorio implements EtiquetaCodigoBarraRelInterfaceFacade {

    public EtiquetaCodigoBarraRel() {
    }

    public List<CatalogoVO> consultarCatalogo(String campoConsultarCatalogo, String valorConsultarCatalogo, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        if (campoConsultarCatalogo.equals("codigo")) {
            if (valorConsultarCatalogo.equals("")) {
                valorConsultarCatalogo = "0";
            }
            int valorInt = Integer.parseInt(valorConsultarCatalogo);
            return getFacadeFactory().getCatalogoFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, 0, unidadeEnsino, usuario);
        }
        if (campoConsultarCatalogo.equals("titulo")) {
            return getFacadeFactory().getCatalogoFacade().consultarPorTitulo(valorConsultarCatalogo, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, 0, unidadeEnsino, 0, usuario);
        }
        return new ArrayList(0);
    }

    public List<ExemplarVO> consultarExemplar(String campoConsultarExemplar, String valorConsultarExemplar, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception {
        if (campoConsultarExemplar.equals("codigo")) {
            if (valorConsultarExemplar.equals("")) {
                valorConsultarExemplar = "0";
            }
            int valorInt = Integer.parseInt(valorConsultarExemplar);
            return getFacadeFactory().getExemplarFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, unidadeEnsino, usuarioLogado);
        }
        if (campoConsultarExemplar.equals("nomeBiblioteca")) {
            return getFacadeFactory().getExemplarFacade().consultarPorNomeBiblioteca(valorConsultarExemplar, false, Uteis.NIVELMONTARDADOS_TODOS, unidadeEnsino,  usuarioLogado);
        }
        if (campoConsultarExemplar.equals("obra")) {
            if (valorConsultarExemplar.equals("")) {
                valorConsultarExemplar = "0";
            }
            int valorInt = Integer.parseInt(valorConsultarExemplar);
            return getFacadeFactory().getExemplarFacade().consultarPorCatalogo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, unidadeEnsino, usuarioLogado);
        }
        if (campoConsultarExemplar.equals("codigoBarra")) {
            return getFacadeFactory().getExemplarFacade().consultarPorCodigoBarraDisponivel(valorConsultarExemplar, "", true, Uteis.NIVELMONTARDADOS_TODOS, unidadeEnsino,  usuarioLogado);
        }
        if (campoConsultarExemplar.equals("situacaoAtual")) {
            return getFacadeFactory().getExemplarFacade().consultarPorSituacaoAtual(valorConsultarExemplar, true, Uteis.NIVELMONTARDADOS_TODOS, unidadeEnsino,  usuarioLogado);
        }
        return new ArrayList(0);
    }
}
