package negocio.facade.jdbc.arquitetura;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.comuns.arquitetura.MonitoramentoVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PerfilAcessoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>PerfilAcessoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see PerfilAcessoVO
 * @see ControleAcesso
 */
@Repository
@Scope("application")
@Lazy
public class Monitoramento {

    private static List<MonitoramentoVO> listaUsuario;

    public Monitoramento() {
        listaUsuario = new ArrayList(0);
    }

    public static void imprimirObjeto() throws Exception {
        Iterator i = getListaUsuario().iterator();
        while (i.hasNext()) {
            MonitoramentoVO m = (MonitoramentoVO) i.next();
//            //System.out.print("====================MONITORAMENTO========================");
//            //System.out.print("Codigo Usuario => " + m.getCodigoUsuario());
//            //System.out.print("Nome Usuario => " + m.getNomeUsuario());
//            //System.out.print("<<<<<<<<<<< ACOES >>>>>>>>>>>>> ");
            Iterator j = m.getAcoes().iterator();
            while (j.hasNext()) {
                String acao = (String)j.next();
//                //System.out.print("AÇÃO => " + acao);
            }
//            //System.out.print("Nome Usuario => " + m.getNomeUsuario());
//            //System.out.print("=========================================================");
        }
    }

    public static void adicionarMonitoramento(MonitoramentoVO monitoramento) throws Exception {
        MonitoramentoVO m = obterUsuarioListaUsuario(monitoramento);
        if (m == null) {
            getListaUsuario().add(monitoramento);
        } else {
            Iterator i = monitoramento.getAcoes().iterator();
            while (i.hasNext()) {
                String acao = (String)i.next();
                m.getAcoes().add(acao);
            }
        }
    }

	public static MonitoramentoVO obterUsuarioListaUsuario(MonitoramentoVO monitoramento) throws Exception {
		Iterator i = getListaUsuario().iterator();
		while (i.hasNext()) {
			MonitoramentoVO m = (MonitoramentoVO) i.next();
			if (i != null && m != null && monitoramento != null) {
				if (m.getCodigoUsuario().intValue() == monitoramento.getCodigoUsuario().intValue()) {
					return m;
				}
			}
		}
		return null;
    }

    public static void criarMonitoramentoVO(String entidade, String acao, UsuarioVO usuario) throws Exception {
        if (usuario == null) {
            usuario = new UsuarioVO();
        }
        MonitoramentoVO monitoramento = new MonitoramentoVO();
        monitoramento.setCodigoUsuario(usuario.getCodigo());
        monitoramento.setDataUltimoAcesso(new Date());
        monitoramento.setNomeUsuario(usuario.getNome());
        monitoramento.getAcoes().add(acao + " - " + entidade + " - DATA HORA - " + Uteis.getDataAtual() + " - HORA -" + Uteis.getHoraAtual());
        adicionarMonitoramento(monitoramento);
    }

    /**
     * @return the listaUsuario
     */
    public static List getListaUsuario() {
        if (listaUsuario == null) {
            listaUsuario = new ArrayList();
        }
        return listaUsuario;
    }

    /**
     * @param listaUsuario the listaUsuario to set
     */
    public void setListaUsuario(List listaUsuario) {
        this.listaUsuario = listaUsuario;
    }
}
