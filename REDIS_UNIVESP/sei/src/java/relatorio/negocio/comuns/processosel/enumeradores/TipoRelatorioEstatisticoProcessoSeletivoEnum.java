package relatorio.negocio.comuns.processosel.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum TipoRelatorioEstatisticoProcessoSeletivoEnum {

    DADOS_CANDIDATOS,
    INSCRITOS_BAIRRO,
    INSCRITOS_CURSO,
    LISTAGEM_FREQUENCIA,
    LISTAGEM_PRESENTES,
    LISTAGEM_APROVADOS,
    LISTAGEM_REPROVADOS,
    LISTAGEM_NAO_MATRICULADOS,
    LISTAGEM_MATRICULADOS,
    LISTAGEM_AUSENTES,
    LISTAGEM_CLASSIFICADOS,
    LISTAGEM_PRESENTE_AUSENTES_CURSO_TURNO_DATA,
    LISTAGEM_MURAL_CANDIDATO,
    APROVADO_PROCESSO_SELETIVO,
    CONFIRMACAO_MATRICULA,
    RENOVACAO_MATRICULA,
    LISTAGEM_CANDIDATOS_CHAMADOS,
    LEMBRETE_DATA_PROVA;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoRelatorioEstatisticoProcessoSeletivoEnum_"+this.name());
    }
    
}
