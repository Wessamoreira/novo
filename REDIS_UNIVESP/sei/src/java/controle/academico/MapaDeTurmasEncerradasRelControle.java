package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MapaDeTurmasEncerradasRelVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.Turma;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas turmaForm.jsp turmaCons.jsp) com
 * as funcionalidades da classe <code>Turma</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Turma
 * @see TurmaVO
 */

@Controller("MapaDeTurmasEncerradasRelControle")
@Scope("viewScope")
@Lazy
public class MapaDeTurmasEncerradasRelControle extends SuperControle implements Serializable {

 
	private MapaDeTurmasEncerradasRelVO mapaDeTurmasEncerradasRelVO;
	private List<MapaDeTurmasEncerradasRelVO> listaMapaDeTurmasEncerradasRelVO;
	private static final long serialVersionUID = 1L;




	public MapaDeTurmasEncerradasRelControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        consultar();
       // setMensagemID("msg_entre_prmconsulta");
    }

 
    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP TurmaCons.jsp. Define o tipo de consulta a ser
     * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List<MapaDeTurmasEncerradasRelVO> objs = getFacadeFactory().getMapaDeTurmasEncerradasRelFacade().consultar(getUnidadeEnsinoLogado(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("mapaDeTurmasEncerradasRelCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList<MapaDeTurmasEncerradasRelVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("mapaDeTurmasEncerradasRelCons.xhtml");
        }

    }

    public String selecionarTurma() {
        try {
            setMapaDeTurmasEncerradasRelVO((MapaDeTurmasEncerradasRelVO) context().getExternalContext().getRequestMap().get("mapaItens"));
            getListaMapaDeTurmasEncerradasRelVO().clear();
            getListaMapaDeTurmasEncerradasRelVO().addAll(getFacadeFactory().getMapaDeTurmasEncerradasRelFacade().
            consultarPorSituacaoMatriculaCodigoDaTurma(getUnidadeEnsinoLogado(), getMapaDeTurmasEncerradasRelVO(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTARTODOS, getUsuarioLogado()));
            
        	setMensagemID("msg_dados_adicionados");
        	return Uteis.getCaminhoRedirecionamentoNavegacao("mapaDeTurmasEncerradasRelForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("mapaDeTurmasEncerradasRelCons.xhtml");
        }
    }


	public MapaDeTurmasEncerradasRelVO getMapaDeTurmasEncerradasRelVO() {
		if (mapaDeTurmasEncerradasRelVO==null) {
			mapaDeTurmasEncerradasRelVO = new MapaDeTurmasEncerradasRelVO();
		}
		return mapaDeTurmasEncerradasRelVO;
	}

	public void setMapaDeTurmasEncerradasRelVO(
			MapaDeTurmasEncerradasRelVO mapaDeTurmasEncerradasRelVO) {
		this.mapaDeTurmasEncerradasRelVO = mapaDeTurmasEncerradasRelVO;
	}


	public List<MapaDeTurmasEncerradasRelVO> getListaMapaDeTurmasEncerradasRelVO() {
		if (listaMapaDeTurmasEncerradasRelVO==null) {
			listaMapaDeTurmasEncerradasRelVO = new ArrayList<MapaDeTurmasEncerradasRelVO>();
		}
		return listaMapaDeTurmasEncerradasRelVO;
	}


	public void setListaMapaDeTurmasEncerradasRelVO(
			List<MapaDeTurmasEncerradasRelVO> listaMapaDeTurmasEncerradasRelVO) {
		this.listaMapaDeTurmasEncerradasRelVO = listaMapaDeTurmasEncerradasRelVO;
	}




}
