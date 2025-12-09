package relatorio.negocio.interfaces.crm;

import java.io.File;
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.crm.PosVendaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface PosVendaRelInterfaceFacade {

	List<PosVendaRelVO> criarObjeto(PosVendaRelVO filotrPosVendaRelVO, FiltroRelatorioAcademicoVO filtroAcademicoVO, UsuarioVO usuario);

	void montarExcel(List<PosVendaRelVO> listaPosVendaRelVOs, PosVendaRelVO filotrPosVendaRelVO, File arquivo);

}
