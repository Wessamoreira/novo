package relatorio.negocio.interfaces.biblioteca;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;

public interface ReservaCatalogoRelInterfaceFacade {
	
	public List criarObjeto(Integer catalogo, Integer biblioteca, Date dataInicio, Date dataFim, Boolean prazoEncerrado, Boolean dentroPrazo, Boolean aguardandoExemplar, Boolean reservaConcluida, Boolean reservaCancelada) throws Exception;
	public List criarObjetoAnalitico(Integer catalogo, Integer biblioteca, Date dataInicio, Date dataFim, Boolean prazoEncerrado, Boolean dentroPrazo, Boolean aguardandoExemplar, Boolean reservaConcluida, Boolean reservaCancelada, UsuarioVO usuario) throws Exception;
	public void validarDados(Integer biblioteca, Date dataInicio, Date dataFim) throws Exception;
	public String designIReportRelatorio();
	public String caminhoBaseRelatorio();
	public String designIReportRelatorioAnalitico();

}
