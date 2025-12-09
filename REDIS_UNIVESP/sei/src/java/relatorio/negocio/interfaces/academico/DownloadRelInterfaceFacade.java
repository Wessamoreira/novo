package relatorio.negocio.interfaces.academico;

import negocio.comuns.academico.DownloadVO;
import relatorio.negocio.comuns.academico.DownloadRelVO;
import java.util.List;

public interface DownloadRelInterfaceFacade {

    public List<DownloadRelVO> criarObjeto(DownloadVO downloadVO, String ano, String semestre) throws Exception;
}
