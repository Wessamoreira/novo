package relatorio.negocio.interfaces.academico;

import java.util.List;

import relatorio.negocio.comuns.academico.OcorrenciasAlunosVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface OcorrenciasAlunosRelInterfaceFacade {

	public List<OcorrenciasAlunosVO> criarObjeto(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, OcorrenciasAlunosVO obj, String tipoOcorrencia, String ordenarPor) throws Exception;

    public void setDescricaoFiltros(String string);

    public String getDescricaoFiltros();
    
    public void validarDados(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception;

}