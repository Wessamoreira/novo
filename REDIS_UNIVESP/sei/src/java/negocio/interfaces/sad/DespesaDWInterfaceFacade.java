package negocio.interfaces.sad;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.sad.DespesaDWVO;
import negocio.comuns.sad.LegendaGraficoVO;
import negocio.comuns.sad.NivelGraficoDWVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface DespesaDWInterfaceFacade {
	

    public DespesaDWVO novo(UsuarioVO usuario) throws Exception;
    public void incluir(DespesaDWVO obj, UsuarioVO usuario) throws Exception;
    public void gerarRelatorioGrafico(NivelGraficoDWVO obj, NivelGraficoDWVO obj2, String nivel, Date dataInicio, Date dataFim, String origem, List<UnidadeEnsinoVO> unidadeEnsinoVOs, UsuarioVO usuario) throws Exception ;
    public void setIdEntidade(String aIdEntidade);
    Object realizarMontagemGraficoPizzaJSON(List<LegendaGraficoVO> listaDadosAcesso) throws Exception;
}