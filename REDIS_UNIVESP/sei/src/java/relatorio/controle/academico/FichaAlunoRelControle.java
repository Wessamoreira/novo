package relatorio.controle.academico;




import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.FichaAlunoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FichaAlunoRel;

@SuppressWarnings("unchecked")
@Controller("FichaAlunoRelControle")
@Scope("viewScope")
@Lazy
public class FichaAlunoRelControle extends SuperControleRelatorio {

    private FichaAlunoRel fichaAlunoRel;
    private String matricula;

    public FichaAlunoRelControle() throws Exception {        
        
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {    
        try {
            String titulo = "FICHA DO ALUNO";
            String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
            String design = FichaAlunoRel.getDesignIReportRelatorio();
            List<FichaAlunoRelVO> fichaAlunoRelVOs = getFichaAlunoRel().criarObjeto(getMatricula(),getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (!fichaAlunoRelVOs.isEmpty()) {
            	getSuperParametroRelVO().setTituloRelatorio(titulo);
            	getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(FichaAlunoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());				
				getSuperParametroRelVO().setListaObjetos(fichaAlunoRelVOs);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(FichaAlunoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());				
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);				
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}         
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {  removerObjetoMemoria(this);
        }
    }

    public FichaAlunoRel getFichaAlunoRel() {
        if(fichaAlunoRel == null){
            fichaAlunoRel = new FichaAlunoRel();
        }
        return fichaAlunoRel;
    }

    public void setFichaAlunoRel(FichaAlunoRel fichaAlunoRel) {
        this.fichaAlunoRel = fichaAlunoRel;
    }



    public void imprimirPDF(String matricula) {
        setMatricula(matricula);
        imprimirPDF();
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
}
