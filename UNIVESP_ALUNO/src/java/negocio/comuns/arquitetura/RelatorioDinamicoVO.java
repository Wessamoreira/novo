package negocio.comuns.arquitetura;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;

/**
 * Classe de atributos do relatorio dinâmico do {@link RelatorioSEIDecidirVO}
 * 
 * @author Renato Borges
 *
 */
public class RelatorioDinamicoVO {

	private Map<String, Object> objetos;
	private Boolean utilizarComoSumario;
	private static final String CAMPO_1= "campo1";
	private static final String CAMPO_2= "campo2";
	private static final String CAMPO_3= "campo3";
	private static final String CAMPO_4= "campo4";
	private static final String CAMPO_5= "campo5";
	private static final String CAMPO_6= "campo6";
	private static final String CAMPO_7= "campo7";
	private static final String CAMPO_8= "campo8";
	private static final String CAMPO_9= "campo9";
	private static final String CAMPO_10= "campo10";
	private static final String CAMPO_11= "campo11";
	private static final String CAMPO_12= "campo12";
	private static final String CAMPO_13= "campo13";
	private static final String CAMPO_14= "campo14";
	private static final String CAMPO_15= "campo15";
	private static final String CAMPO_16= "campo16";
	private static final String CAMPO_17= "campo17";
	private static final String CAMPO_18= "campo18";
	private static final String CAMPO_19= "campo19";
	private static final String CAMPO_20= "campo20";
	private static final String CAMPO_21= "campo21";
	private static final String CAMPO_22= "campo22";
	private static final String CAMPO_23= "campo23";
	private static final String CAMPO_24= "campo24";
	private static final String CAMPO_25= "campo25";
	private static final String CAMPO_26= "campo26";
	private static final String CAMPO_27= "campo27";
	private static final String CAMPO_28= "campo28";
	private static final String CAMPO_29= "campo29";
	private static final String CAMPO_30= "campo30";
	private static final String CAMPO_31= "campo31";
	private static final String CAMPO_32= "campo32";
	private static final String CAMPO_33= "campo33";
	private static final String CAMPO_34= "campo34";
	private static final String CAMPO_35= "campo35";
	private static final String CAMPO_36= "campo36";
	private static final String CAMPO_37= "campo37";
	private static final String CAMPO_38= "campo38";
	private static final String CAMPO_39= "campo39";
	private static final String CAMPO_40= "campo40";
	private static final String CAMPO_41= "campo41";
	private static final String CAMPO_42= "campo42";
	private static final String CAMPO_43= "campo43";
	private static final String CAMPO_44= "campo44";
	private static final String CAMPO_45= "campo45";
	private static final String CAMPO_46= "campo46";
	private static final String CAMPO_47= "campo47";
	private static final String CAMPO_48= "campo48";
	private static final String CAMPO_49= "campo49";
	private static final String CAMPO_50= "campo50";
	private static final String CAMPO_51= "campo51";
	private static final String CAMPO_52= "campo52";
	private static final String CAMPO_53= "campo53";
	private static final String CAMPO_54= "campo54";
	private static final String CAMPO_55= "campo55";
	private static final String CAMPO_56= "campo56";
	private static final String CAMPO_57= "campo57";
	private static final String CAMPO_58= "campo58";
	private static final String CAMPO_59= "campo59";
	private static final String CAMPO_60= "campo60";
	private static final String CAMPO_61= "campo61";
	private static final String CAMPO_62= "campo62";
	private static final String CAMPO_63= "campo63";
	private static final String CAMPO_64= "campo64";
	private static final String CAMPO_65= "campo65";
	private static final String CAMPO_66= "campo66";
	private static final String CAMPO_67= "campo67";
	private static final String CAMPO_68= "campo68";
	private static final String CAMPO_69= "campo69";
	private static final String CAMPO_70= "campo70";
	private static final String CAMPO_71= "campo71";
	private static final String CAMPO_72= "campo72";
	private static final String CAMPO_73= "campo73";
	private static final String CAMPO_74= "campo74";
	private static final String CAMPO_75= "campo75";
	private static final String CAMPO_76= "campo76";
	private static final String CAMPO_77= "campo77";
	private static final String CAMPO_78= "campo78";
	private static final String CAMPO_79= "campo79";
	private static final String CAMPO_80= "campo80";
	private static final String CAMPO_81= "campo81";
	private static final String CAMPO_82= "campo82";
	private static final String CAMPO_83= "campo83";
	private static final String CAMPO_84= "campo84";
	private static final String CAMPO_85= "campo85";
	private static final String CAMPO_86= "campo86";
	private static final String CAMPO_87= "campo87";
	private static final String CAMPO_88= "campo88";
	private static final String CAMPO_89= "campo89";
	private static final String CAMPO_90= "campo90";
	private static final String CAMPO_91= "campo91";
	private static final String CAMPO_92= "campo92";
	private static final String CAMPO_93= "campo93";
	private static final String CAMPO_94= "campo94";
	private static final String CAMPO_95= "campo95";
	private static final String CAMPO_96= "campo96";
	private static final String CAMPO_97= "campo97";
	private static final String CAMPO_98= "campo98";
	private static final String CAMPO_99= "campo99";
	private static final String CAMPO_100= "campo100";
	private static final String CAMPO_101= "campo101";
	private static final String CAMPO_102= "campo102";
	private static final String CAMPO_103= "campo103";
	private static final String CAMPO_104= "campo104";
	private static final String CAMPO_105= "campo105";
	private static final String CAMPO_106= "campo106";
	private static final String CAMPO_107= "campo107";
	private static final String CAMPO_108= "campo108";
	private static final String CAMPO_109= "campo109";
	private static final String CAMPO_110= "campo110";
	private static final String CAMPO_111= "campo111";
	private static final String CAMPO_112= "campo112";
	private static final String CAMPO_113= "campo113";
	private static final String CAMPO_114= "campo114";
	private static final String CAMPO_115= "campo115";
	private static final String CAMPO_116= "campo116";
	private static final String CAMPO_117= "campo117";
	private static final String CAMPO_118= "campo118";
	private static final String CAMPO_119= "campo119";
	private static final String CAMPO_120= "campo120";
	private static final String CAMPO_121= "campo121";
	private static final String CAMPO_122= "campo122";
	private static final String CAMPO_123= "campo123";
	private static final String CAMPO_124= "campo124";
	private static final String CAMPO_125= "campo125";
	private static final String CAMPO_126= "campo126";
	private static final String CAMPO_127= "campo127";
	private static final String CAMPO_128= "campo128";
	private static final String CAMPO_129= "campo129";
	private static final String CAMPO_130= "campo130";
	private static final String CAMPO_131= "campo131";
	private static final String CAMPO_132= "campo132";
	private static final String CAMPO_133= "campo133";
	private static final String CAMPO_134= "campo134";
	private static final String CAMPO_135= "campo135";
	private static final String CAMPO_136= "campo136";
	private static final String CAMPO_137= "campo137";
	private static final String CAMPO_138= "campo138";
	private static final String CAMPO_139= "campo139";
	private static final String CAMPO_140= "campo140";
	private static final String CAMPO_141= "campo141";
	private static final String CAMPO_142= "campo142";
	private static final String CAMPO_143= "campo143";
	private static final String CAMPO_144= "campo144";
	private static final String CAMPO_145= "campo145";
	private static final String CAMPO_146= "campo146";
	private static final String CAMPO_147= "campo147";
	private static final String CAMPO_148= "campo148";
	private static final String CAMPO_149= "campo149";
	private static final String CAMPO_150= "campo150";
	private static final String CAMPO_151= "campo151";
	private static final String CAMPO_152= "campo152";
	private static final String CAMPO_153= "campo153";
	private static final String CAMPO_154= "campo154";
	private static final String CAMPO_155= "campo155";
	private static final String CAMPO_156= "campo156";
	private static final String CAMPO_157= "campo157";
	private static final String CAMPO_158= "campo158";
	private static final String CAMPO_159= "campo159";
	private static final String CAMPO_160= "campo160";
	private static final String CAMPO_161= "campo161";
	private static final String CAMPO_162= "campo162";
	private static final String CAMPO_163= "campo163";
	private static final String CAMPO_164= "campo164";
	private static final String CAMPO_165= "campo165";
	private static final String CAMPO_166= "campo166";
	private static final String CAMPO_167= "campo167";
	private static final String CAMPO_168= "campo168";
	private static final String CAMPO_169= "campo169";
	private static final String CAMPO_170= "campo170";
	private static final String CAMPO_171= "campo171";
	private static final String CAMPO_172= "campo172";
	private static final String CAMPO_173= "campo173";
	private static final String CAMPO_174= "campo174";
	private static final String CAMPO_175= "campo175";
	private static final String CAMPO_176= "campo176";
	private static final String CAMPO_177= "campo177";
	private static final String CAMPO_178= "campo178";
	private static final String CAMPO_179= "campo179";
	private static final String CAMPO_180= "campo180";
	private static final String CAMPO_181= "campo181";
	private static final String CAMPO_182= "campo182";
	private static final String CAMPO_183= "campo183";
	private static final String CAMPO_184= "campo184";
	private static final String CAMPO_185= "campo185";
	private static final String CAMPO_186= "campo186";
	private static final String CAMPO_187= "campo187";
	private static final String CAMPO_188= "campo188";
	private static final String CAMPO_189= "campo189";
	private static final String CAMPO_190= "campo190";
	private static final String CAMPO_191= "campo191";
	private static final String CAMPO_192= "campo192";
	private static final String CAMPO_193= "campo193";
	private static final String CAMPO_194= "campo194";
	private static final String CAMPO_195= "campo195";
	private static final String CAMPO_196= "campo196";
	private static final String CAMPO_197= "campo197";
	private static final String CAMPO_198= "campo198";
	private static final String CAMPO_199= "campo199";
	private static final String CAMPO_200= "campo200";
	private static final String CROSS_TAB= "crossTab";
	
	public Object getCampo1(){return objetos.get(CAMPO_1);}
	public Object getCampo2(){return objetos.get(CAMPO_2);}
	public Object getCampo3(){return objetos.get(CAMPO_3);}
	public Object getCampo4(){return objetos.get(CAMPO_4);}
	public Object getCampo5(){return objetos.get(CAMPO_5);}
	public Object getCampo6(){return objetos.get(CAMPO_6);}
	public Object getCampo7(){return objetos.get(CAMPO_7);}
	public Object getCampo8(){return objetos.get(CAMPO_8);}
	public Object getCampo9(){return objetos.get(CAMPO_9);}
	public Object getCampo10(){return objetos.get(CAMPO_10);}
	public Object getCampo11(){return objetos.get(CAMPO_11);}
	public Object getCampo12(){return objetos.get(CAMPO_12);}
	public Object getCampo13(){return objetos.get(CAMPO_13);}
	public Object getCampo14(){return objetos.get(CAMPO_14);}
	public Object getCampo15(){return objetos.get(CAMPO_15);}
	public Object getCampo16(){return objetos.get(CAMPO_16);}
	public Object getCampo17(){return objetos.get(CAMPO_17);}
	public Object getCampo18(){return objetos.get(CAMPO_18);}
	public Object getCampo19(){return objetos.get(CAMPO_19);}
	public Object getCampo20(){return objetos.get(CAMPO_20);}
	public Object getCampo21(){return objetos.get(CAMPO_21);}
	public Object getCampo22(){return objetos.get(CAMPO_22);}
	public Object getCampo23(){return objetos.get(CAMPO_23);}
	public Object getCampo24(){return objetos.get(CAMPO_24);}
	public Object getCampo25(){return objetos.get(CAMPO_25);}
	public Object getCampo26(){return objetos.get(CAMPO_26);}
	public Object getCampo27(){return objetos.get(CAMPO_27);}
	public Object getCampo28(){return objetos.get(CAMPO_28);}
	public Object getCampo29(){return objetos.get(CAMPO_29);}
	public Object getCampo30(){return objetos.get(CAMPO_30);}
	public Object getCampo31(){return objetos.get(CAMPO_31);}
	public Object getCampo32(){return objetos.get(CAMPO_32);}
	public Object getCampo33(){return objetos.get(CAMPO_33);}
	public Object getCampo34(){return objetos.get(CAMPO_34);}
	public Object getCampo35(){return objetos.get(CAMPO_35);}
	public Object getCampo36(){return objetos.get(CAMPO_36);}
	public Object getCampo37(){return objetos.get(CAMPO_37);}
	public Object getCampo38(){return objetos.get(CAMPO_38);}
	public Object getCampo39(){return objetos.get(CAMPO_39);}
	public Object getCampo40(){return objetos.get(CAMPO_40);}
	public Object getCampo41(){return objetos.get(CAMPO_41);}
	public Object getCampo42(){return objetos.get(CAMPO_42);}
	public Object getCampo43(){return objetos.get(CAMPO_43);}
	public Object getCampo44(){return objetos.get(CAMPO_44);}
	public Object getCampo45(){return objetos.get(CAMPO_45);}
	public Object getCampo46(){return objetos.get(CAMPO_46);}
	public Object getCampo47(){return objetos.get(CAMPO_47);}
	public Object getCampo48(){return objetos.get(CAMPO_48);}
	public Object getCampo49(){return objetos.get(CAMPO_49);}
	public Object getCampo50(){return objetos.get(CAMPO_50);}
	public Object getCampo51(){return objetos.get(CAMPO_51);}
	public Object getCampo52(){return objetos.get(CAMPO_52);}
	public Object getCampo53(){return objetos.get(CAMPO_53);}
	public Object getCampo54(){return objetos.get(CAMPO_54);}
	public Object getCampo55(){return objetos.get(CAMPO_55);}
	public Object getCampo56(){return objetos.get(CAMPO_56);}
	public Object getCampo57(){return objetos.get(CAMPO_57);}
	public Object getCampo58(){return objetos.get(CAMPO_58);}
	public Object getCampo59(){return objetos.get(CAMPO_59);}
	public Object getCampo60(){return objetos.get(CAMPO_60);}
	public Object getCampo61(){return objetos.get(CAMPO_61);}
	public Object getCampo62(){return objetos.get(CAMPO_62);}
	public Object getCampo63(){return objetos.get(CAMPO_63);}
	public Object getCampo64(){return objetos.get(CAMPO_64);}
	public Object getCampo65(){return objetos.get(CAMPO_65);}
	public Object getCampo66(){return objetos.get(CAMPO_66);}
	public Object getCampo67(){return objetos.get(CAMPO_67);}
	public Object getCampo68(){return objetos.get(CAMPO_68);}
	public Object getCampo69(){return objetos.get(CAMPO_69);}
	public Object getCampo70(){return objetos.get(CAMPO_70);}
	public Object getCampo71(){return objetos.get(CAMPO_71);}
	public Object getCampo72(){return objetos.get(CAMPO_72);}
	public Object getCampo73(){return objetos.get(CAMPO_73);}
	public Object getCampo74(){return objetos.get(CAMPO_74);}
	public Object getCampo75(){return objetos.get(CAMPO_75);}
	public Object getCampo76(){return objetos.get(CAMPO_76);}
	public Object getCampo77(){return objetos.get(CAMPO_77);}
	public Object getCampo78(){return objetos.get(CAMPO_78);}
	public Object getCampo79(){return objetos.get(CAMPO_79);}
	public Object getCampo80(){return objetos.get(CAMPO_80);}
	public Object getCampo81(){return objetos.get(CAMPO_81);}
	public Object getCampo82(){return objetos.get(CAMPO_82);}
	public Object getCampo83(){return objetos.get(CAMPO_83);}
	public Object getCampo84(){return objetos.get(CAMPO_84);}
	public Object getCampo85(){return objetos.get(CAMPO_85);}
	public Object getCampo86(){return objetos.get(CAMPO_86);}
	public Object getCampo87(){return objetos.get(CAMPO_87);}
	public Object getCampo88(){return objetos.get(CAMPO_88);}
	public Object getCampo89(){return objetos.get(CAMPO_89);}
	public Object getCampo90(){return objetos.get(CAMPO_90);}
	public Object getCampo91(){return objetos.get(CAMPO_91);}
	public Object getCampo92(){return objetos.get(CAMPO_92);}
	public Object getCampo93(){return objetos.get(CAMPO_93);}
	public Object getCampo94(){return objetos.get(CAMPO_94);}
	public Object getCampo95(){return objetos.get(CAMPO_95);}
	public Object getCampo96(){return objetos.get(CAMPO_96);}
	public Object getCampo97(){return objetos.get(CAMPO_97);}
	public Object getCampo98(){return objetos.get(CAMPO_98);}
	public Object getCampo99(){return objetos.get(CAMPO_99);}
	public Object getCampo100(){return objetos.get(CAMPO_100);}
	public Object getCampo101(){return objetos.get(CAMPO_101);}
	public Object getCampo102(){return objetos.get(CAMPO_102);}
	public Object getCampo103(){return objetos.get(CAMPO_103);}
	public Object getCampo104(){return objetos.get(CAMPO_104);}
	public Object getCampo105(){return objetos.get(CAMPO_105);}
	public Object getCampo106(){return objetos.get(CAMPO_106);}
	public Object getCampo107(){return objetos.get(CAMPO_107);}
	public Object getCampo108(){return objetos.get(CAMPO_108);}
	public Object getCampo109(){return objetos.get(CAMPO_109);}
	public Object getCampo110(){return objetos.get(CAMPO_110);}
	public Object getCampo111(){return objetos.get(CAMPO_111);}
	public Object getCampo112(){return objetos.get(CAMPO_112);}
	public Object getCampo113(){return objetos.get(CAMPO_113);}
	public Object getCampo114(){return objetos.get(CAMPO_114);}
	public Object getCampo115(){return objetos.get(CAMPO_115);}
	public Object getCampo116(){return objetos.get(CAMPO_116);}
	public Object getCampo117(){return objetos.get(CAMPO_117);}
	public Object getCampo118(){return objetos.get(CAMPO_118);}
	public Object getCampo119(){return objetos.get(CAMPO_119);}
	public Object getCampo120(){return objetos.get(CAMPO_120);}
	public Object getCampo121(){return objetos.get(CAMPO_121);}
	public Object getCampo122(){return objetos.get(CAMPO_122);}
	public Object getCampo123(){return objetos.get(CAMPO_123);}
	public Object getCampo124(){return objetos.get(CAMPO_124);}
	public Object getCampo125(){return objetos.get(CAMPO_125);}
	public Object getCampo126(){return objetos.get(CAMPO_126);}
	public Object getCampo127(){return objetos.get(CAMPO_127);}
	public Object getCampo128(){return objetos.get(CAMPO_128);}
	public Object getCampo129(){return objetos.get(CAMPO_129);}
	public Object getCampo130(){return objetos.get(CAMPO_130);}
	public Object getCampo131(){return objetos.get(CAMPO_131);}
	public Object getCampo132(){return objetos.get(CAMPO_132);}
	public Object getCampo133(){return objetos.get(CAMPO_133);}
	public Object getCampo134(){return objetos.get(CAMPO_134);}
	public Object getCampo135(){return objetos.get(CAMPO_135);}
	public Object getCampo136(){return objetos.get(CAMPO_136);}
	public Object getCampo137(){return objetos.get(CAMPO_137);}
	public Object getCampo138(){return objetos.get(CAMPO_138);}
	public Object getCampo139(){return objetos.get(CAMPO_139);}
	public Object getCampo140(){return objetos.get(CAMPO_140);}
	public Object getCampo141(){return objetos.get(CAMPO_141);}
	public Object getCampo142(){return objetos.get(CAMPO_142);}
	public Object getCampo143(){return objetos.get(CAMPO_143);}
	public Object getCampo144(){return objetos.get(CAMPO_144);}
	public Object getCampo145(){return objetos.get(CAMPO_145);}
	public Object getCampo146(){return objetos.get(CAMPO_146);}
	public Object getCampo147(){return objetos.get(CAMPO_147);}
	public Object getCampo148(){return objetos.get(CAMPO_148);}
	public Object getCampo149(){return objetos.get(CAMPO_149);}
	public Object getCampo150(){return objetos.get(CAMPO_150);}
	public Object getCampo151(){return objetos.get(CAMPO_151);}
	public Object getCampo152(){return objetos.get(CAMPO_152);}
	public Object getCampo153(){return objetos.get(CAMPO_153);}
	public Object getCampo154(){return objetos.get(CAMPO_154);}
	public Object getCampo155(){return objetos.get(CAMPO_155);}
	public Object getCampo156(){return objetos.get(CAMPO_156);}
	public Object getCampo157(){return objetos.get(CAMPO_157);}
	public Object getCampo158(){return objetos.get(CAMPO_158);}
	public Object getCampo159(){return objetos.get(CAMPO_159);}
	public Object getCampo160(){return objetos.get(CAMPO_160);}
	public Object getCampo161(){return objetos.get(CAMPO_161);}
	public Object getCampo162(){return objetos.get(CAMPO_162);}
	public Object getCampo163(){return objetos.get(CAMPO_163);}
	public Object getCampo164(){return objetos.get(CAMPO_164);}
	public Object getCampo165(){return objetos.get(CAMPO_165);}
	public Object getCampo166(){return objetos.get(CAMPO_166);}
	public Object getCampo167(){return objetos.get(CAMPO_167);}
	public Object getCampo168(){return objetos.get(CAMPO_168);}
	public Object getCampo169(){return objetos.get(CAMPO_169);}
	public Object getCampo170(){return objetos.get(CAMPO_170);}
	public Object getCampo171(){return objetos.get(CAMPO_171);}
	public Object getCampo172(){return objetos.get(CAMPO_172);}
	public Object getCampo173(){return objetos.get(CAMPO_173);}
	public Object getCampo174(){return objetos.get(CAMPO_174);}
	public Object getCampo175(){return objetos.get(CAMPO_175);}
	public Object getCampo176(){return objetos.get(CAMPO_176);}
	public Object getCampo177(){return objetos.get(CAMPO_177);}
	public Object getCampo178(){return objetos.get(CAMPO_178);}
	public Object getCampo179(){return objetos.get(CAMPO_179);}
	public Object getCampo180(){return objetos.get(CAMPO_180);}
	public Object getCampo181(){return objetos.get(CAMPO_181);}
	public Object getCampo182(){return objetos.get(CAMPO_182);}
	public Object getCampo183(){return objetos.get(CAMPO_183);}
	public Object getCampo184(){return objetos.get(CAMPO_184);}
	public Object getCampo185(){return objetos.get(CAMPO_185);}
	public Object getCampo186(){return objetos.get(CAMPO_186);}
	public Object getCampo187(){return objetos.get(CAMPO_187);}
	public Object getCampo188(){return objetos.get(CAMPO_188);}
	public Object getCampo189(){return objetos.get(CAMPO_189);}
	public Object getCampo190(){return objetos.get(CAMPO_190);}
	public Object getCampo191(){return objetos.get(CAMPO_191);}
	public Object getCampo192(){return objetos.get(CAMPO_192);}
	public Object getCampo193(){return objetos.get(CAMPO_193);}
	public Object getCampo194(){return objetos.get(CAMPO_194);}
	public Object getCampo195(){return objetos.get(CAMPO_195);}
	public Object getCampo196(){return objetos.get(CAMPO_196);}
	public Object getCampo197(){return objetos.get(CAMPO_197);}
	public Object getCampo198(){return objetos.get(CAMPO_198);}
	public Object getCampo199(){return objetos.get(CAMPO_199);}
	public Object getCampo200(){return objetos.get(CAMPO_200);}
	
	public Object getCrossTab() {
		return objetos.get(CROSS_TAB);		
	}

	public Map<String, Object> getObjetos() {
		if(objetos == null) {
			objetos =  new HashMap<String, Object>();
		} 
		return objetos;
	}

	public void setObjetos(Map<String, Object> objetos) {
		this.objetos = objetos;
	}

	/**
	 * Metodo responsavel por montar {@link JRDataSource} para o CrossTab do relatório.
	 * 
	 * @param listaCrosstabVO
	 * @return
	 */
	public JRDataSource getDataSourceListaCrossTab(List<CrosstabVO> listaCrosstabVO) {
		return new JRBeanArrayDataSource(listaCrosstabVO.toArray());
	}
	
	public JRDataSource getDataSourceCrossTab() {
		if(getCrossTab() != null && getCrossTab() instanceof List) {
			return new JRBeanArrayDataSource(((List<CrosstabVO>) getCrossTab()).toArray());
		}
		if(getCrossTab() != null && getCrossTab() instanceof JRBeanArrayDataSource) {
			return (JRBeanArrayDataSource) getCrossTab();
		}
		return null;	
	}
	public Boolean getUtilizarComoSumario() {
		if(utilizarComoSumario == null) {
			utilizarComoSumario = false;
		}
		return utilizarComoSumario;
	}
	public void setUtilizarComoSumario(Boolean utilizarComoSumario) {
		this.utilizarComoSumario = utilizarComoSumario;
	}
	
	
}
