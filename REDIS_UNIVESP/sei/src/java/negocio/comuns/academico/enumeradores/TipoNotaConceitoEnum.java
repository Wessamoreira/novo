package negocio.comuns.academico.enumeradores;


public enum TipoNotaConceitoEnum {
    
    NOTA_1(1), NOTA_2(2), NOTA_3(3), NOTA_4(4), NOTA_5(5), NOTA_6(6), NOTA_7(7), NOTA_8(8), NOTA_9(9), NOTA_10(10), 
    NOTA_11(11), NOTA_12(12), NOTA_13(13), NOTA_14(14), NOTA_15(15), NOTA_16(16), NOTA_17(17), NOTA_18(18), NOTA_19(19), NOTA_20(20), 
    NOTA_21(21), NOTA_22(22), NOTA_23(23), NOTA_24(24), NOTA_25(25), NOTA_26(26), NOTA_27(27), NOTA_28(28), NOTA_29(29), NOTA_30(30),
    NOTA_31(31), NOTA_32(32), NOTA_33(33), NOTA_34(34), NOTA_35(35), NOTA_36(36), NOTA_37(37), NOTA_38(38), NOTA_39(39), NOTA_40(40);
    
    private Integer numeroNota;
    
	private TipoNotaConceitoEnum(Integer numeroNota) {
		this.numeroNota = numeroNota;
	}

	public Integer getNumeroNota() {
		if (numeroNota == null) {
			numeroNota = 0;
		}
		return numeroNota;
	}

	public void setNumeroNota(Integer numeroNota) {
		this.numeroNota = numeroNota;
	}
    
	public static TipoNotaConceitoEnum getTipoNota(Integer numeroNota){
		for(TipoNotaConceitoEnum tipoNotaConceitoEnum: TipoNotaConceitoEnum.values()){
			if(tipoNotaConceitoEnum.getNumeroNota().equals(numeroNota)){
				return tipoNotaConceitoEnum;
			}
		}
		return null;
	}
    
}
