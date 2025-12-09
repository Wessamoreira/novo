package relatorio.negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.InscricaoVO;
import relatorio.negocio.comuns.processosel.FiltroRelatorioProcessoSeletivoVO;
import relatorio.negocio.comuns.processosel.enumeradores.TipoRelatorioEstatisticoProcessoSeletivoEnum;


public interface EstatisticaProcessoSeletivoRelInterfaceFacade {
    
    @SuppressWarnings("rawtypes")
    public List consultarDadosGeracaoEstatistica(TipoRelatorioEstatisticoProcessoSeletivoEnum tipoRelatorio, Integer processoSeletivo, Integer dataProva, Integer sala, Integer unidadeEnsinoCurso, String ano, String semestre, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO, String ordenarPor, Integer qtdeDiasNotificarDataProva, Integer chamada, Boolean apresentarNomeCandidatoCaixaAlta, Integer quantidadeCasaDecimalAposVirgula, UsuarioVO usuarioVO, Integer numeroChamada) throws Exception;    
    String getDesignIReportRelatorio(TipoRelatorioEstatisticoProcessoSeletivoEnum tipoRelatorio);      
    
    String caminhoBaseIReportRelatorio();
    
    public Integer verificarClassificacaoCandidado(InscricaoVO inscricao) throws Exception;

}