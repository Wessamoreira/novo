package relatorio.negocio.interfaces.basico;

import java.util.Date;
import java.util.List;

import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.basico.AlunosAtivosRelVO;

public interface AlunosAtivosRelInterfaceFacade {

	String caminhoBaseRelatorio();

	public List<AlunosAtivosRelVO> criarObjeto(Date data, String tipoRelatorio, Integer unidadeEnsino) throws Exception;

	public String designIReportRelatorio();

	public String designIReportRelatorioSintetico();
	
	public int executarConsultaQuantidadeAlunosAtivos(Date data) throws Exception;

    void validarDados(Integer codigoUnidadeEnsino) throws ConsistirException;


}