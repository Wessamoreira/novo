package negocio.comuns.utilitarias.dominios;

import java.util.function.Predicate;

public enum TipoNivelEducacional {

	
	INFANTIL("IN", "Educação Infantil", "Infantil"),
    BASICO("BA", "Ensino Fundamental", "Fundamental"),
    MEDIO("ME", "Ensino Médio", "Médio"),
    EXTENSAO("EX", "Extensão", "Qualificação"),
    SEQUENCIAL("SE", "Sequencial", "Superior"),
    GRADUACAO_TECNOLOGICA("GT", "Graduação Tecnológica", "Superior"),
    SUPERIOR("SU", "Graduação", "Superior"),
    POS_GRADUACAO("PO", "Pós-graduação", "Pós-graduação"),
    MESTRADO("MT", "Pós-graduação(Stricto Sensu) - Mestrado", "Pós-graduação(Stricto Sensu) - Mestrado"),
    PROFISSIONALIZANTE("PR", "Técnico/Profissionalizante", "Profissionalizante");
    
    String valor;
    String descricao;
    String nivel;

    
    TipoNivelEducacional(String valor, String descricao, String nivel) {
        this.valor = valor;
        this.descricao = descricao;
        this.nivel = nivel;
    }

    public static TipoNivelEducacional getEnum(String valor) {
        TipoNivelEducacional[] valores = values();
        for (TipoNivelEducacional obj : valores) {
            if (obj.getValor().equals(valor) || obj.getDescricao().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoNivelEducacional obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNivel() {
        return nivel;
    }

	public static final Predicate<TipoNivelEducacional> IS_BASICO = BASICO::equals;

	public static final Predicate<TipoNivelEducacional> IS_SEQUENCIAL = SEQUENCIAL::equals;

	public static final Predicate<TipoNivelEducacional> IS_EXTENSAO = EXTENSAO::equals;
	
	
}
