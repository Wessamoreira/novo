package relatorio.negocio.interfaces.biblioteca;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.TipoCatalogoVO;
import relatorio.negocio.comuns.biblioteca.ExemplaresRelVO;

public interface ExemplaresPorCursoRelInterfaceFacade {

    void validarDados(CursoVO curso, PeriodoLetivoVO periodo, DisciplinaVO disciplina, Date dataInicioCompraExemplar ,Date dataFimCompraExemplar , Date dataInicioAquisicaoExemplar ,Date dataFimAquisicaoExemplar) throws Exception;

    List<ExemplaresRelVO> criarObjeto(BibliotecaVO biblioteca, CursoVO curso, DisciplinaVO disciplina,Date dataInicioCompraExemplar,Date dataFimCompraExemplar, TipoCatalogoVO tipoCatalogo, String tipoCatalogoPeriodico , Date dataInicioAquisicaoExemplar ,Date dataFimAquisicaoExemplar, Boolean considerarSubTiposCatalogo, Boolean considerarPlanoEnsinoCursoVinculadoAoCatalogo) throws Exception;
    
    public String designIReportRelatorio();

    public String caminhoBaseRelatorio();

}
