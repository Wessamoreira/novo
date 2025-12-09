<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>UNIVESP_ALUNO</groupId>
	<artifactId>UNIVESP_ALUNO</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>war</packaging>
	<properties>
		<org.bouncycastle>1.70</org.bouncycastle>
		<net.sf.jasperreports>6.20.0</net.sf.jasperreports>
		<org.apache.poi>5.2.3</org.apache.poi>
		<com.documents4j>1.1.12</com.documents4j>
		<java.version>21</java.version>
		<maven.compiler.release>21</maven.compiler.release>
        <maven.compiler.target>21</maven.compiler.target>
        <maven.compiler.source>21</maven.compiler.source>
		<project.build.sourceEncoding>ISO-8859-1</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<jersey.version>3.1.7</jersey.version>
	</properties>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.5.6</version>
		<relativePath />
		<!--  lookup parent from repository  -->
	</parent>
	<repositories>
		<repository>
			<id>sei-repo</id>
			<name>Sei Repository</name>
			<url>file:/C:/Users/Wessl/Documents/biblioteca/SEI_J17/</url>
		</repository>
		<repository>
			<id>com.e-iceblue</id>
			<name>e-iceblue</name>
			<url>https://repo.e-iceblue.com/nexus/content/groups/public/</url>
		</repository>
	</repositories>
	<dependencies>
		<dependency>
			<groupId>com.human</groupId>
			<artifactId>HumanGatewayClient</artifactId>
			<version>1.0.0</version>
		</dependency>
		<dependency>
			<groupId>org.marc4j</groupId>
			<artifactId>marc4j</artifactId>
			<version>2.6.3</version>
		</dependency>

		<dependency>
			<groupId>com.github.fernandospr</groupId>
			<artifactId>javapns-jdk16</artifactId>
			<version>2.4.0</version>
		</dependency>

		<dependency>
			<groupId>jakarta.ws.rs</groupId>
			<artifactId>jakarta.ws.rs-api</artifactId>
			<version>3.1.0</version>
			<scope>provided</scope>
		</dependency>

		<dependency>
			<groupId>org.codehaus.jettison</groupId>
			<artifactId>jettison</artifactId>
			<version>1.5.4</version>
		</dependency>

		<dependency>
			<groupId>org.joinfaces</groupId>
			<artifactId>weld-spring-boot-starter</artifactId>
			<version>5.5.6</version>
		</dependency>

		<dependency>
			<groupId>org.joinfaces</groupId>
			<artifactId>cdi-spring-boot-starter</artifactId>
			<version>5.5.6</version>
		</dependency>

		<dependency>
			<groupId>org.jboss.weld.servlet</groupId>
			<artifactId>weld-servlet-core</artifactId>
			<version>6.0.3.Final</version>
		</dependency>


		

		<dependency>
			<groupId>commons-fileupload</groupId>
			<artifactId>commons-fileupload</artifactId>
			<version>1.5</version>
		</dependency>
		

	

		<dependency>
			<groupId>jakarta.servlet</groupId>
			<artifactId>jakarta.servlet-api</artifactId>
			<version>6.0.0</version>
			<scope>provided</scope>
		</dependency>

		<dependency>
			<groupId>com.sun.mail</groupId>
			<artifactId>javax.mail</artifactId>
			<version>1.6.2</version>
		</dependency>

		<dependency>
			<groupId>org.eclipse.angus</groupId>
			<artifactId>angus-mail</artifactId>
			<version>2.0.3</version>
		</dependency>


		<!-- pom.xml -->
		<dependency>
			<groupId>javax.xml.bind</groupId>
			<artifactId>jaxb-api</artifactId>
			<version>2.3.1</version>
		</dependency>
		<dependency>
			<groupId>org.glassfish.jaxb</groupId>
			<artifactId>jaxb-runtime</artifactId>
			<version>2.3.1</version>
		</dependency>
		

		<dependency>
			<groupId>com.auth0</groupId>
			<artifactId>java-jwt</artifactId>
			<version>4.4.0</version>
		</dependency>

		<dependency>
			<groupId>com.googlecode.json-simple</groupId>
			<artifactId>json-simple</artifactId>
			<version>1.1.1</version>
		</dependency>

		<dependency>
			<groupId>org.bouncycastle</groupId>
			<artifactId>bcprov-jdk18on</artifactId>
			<version>1.78</version>
		</dependency>


		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.17.1</version>
		</dependency>


		<!-- Validação PDF/A -->
		<dependency>
			<groupId>org.verapdf</groupId>
			<artifactId>validation-model</artifactId>
			<version>1.26.1</version>
		</dependency>

		<!-- Core -->
		<dependency>
			<groupId>org.verapdf</groupId>
			<artifactId>core</artifactId>
			<version>1.26.1</version>
		</dependency>

		<!-- Parser -->
		<dependency>
			<groupId>org.verapdf</groupId>
			<artifactId>parser</artifactId>
			<version>1.26.1</version>
		</dependency>

		<dependency>
			<groupId>org.glassfish.jersey.core</groupId>
			<artifactId>jersey-client</artifactId>
			<version>${jersey.version}</version>
		</dependency>

		<dependency>
			<groupId>org.glassfish.jersey.inject</groupId>
			<artifactId>jersey-hk2</artifactId>
			<version>${jersey.version}</version>
		</dependency>

		<dependency>
			<groupId>org.glassfish.jersey.media</groupId>
			<artifactId>jersey-media-json-jackson</artifactId>
			<version>${jersey.version}</version>
		</dependency>

		<dependency>
			<groupId>com.github.mwiede</groupId>
			<artifactId>jsch</artifactId>
			<version>0.2.19</version>
		</dependency>

		<dependency>
			<groupId>org.aspectj</groupId>
			<artifactId>aspectjweaver</artifactId>
			<version>1.9.25</version>
		</dependency>

		<dependency>
			<groupId>org.primefaces</groupId>
			<artifactId>primefaces</artifactId>
			<version>14.0.4</version>
			<classifier>jakarta</classifier>
		</dependency>
		<dependency>
			<groupId>org.primefaces.extensions</groupId>
			<artifactId>primefaces-extensions</artifactId>
			<version>14.0.4</version>
			<classifier>jakarta</classifier>
		</dependency>
		<dependency>
			<groupId>org.primefaces.extensions</groupId>
			<artifactId>resources-ckeditor</artifactId>
			<version>12.0.4</version>
		</dependency>
		<!-- Apache Poi atulizado-->
		<dependency>
			<groupId>org.apache.poi</groupId>
			<artifactId>poi</artifactId>
			<version>5.3.0</version>
		</dependency>
		<dependency>
			<groupId>org.apache.poi</groupId>
			<artifactId>poi-ooxml</artifactId>
			<version>5.3.0</version>
		</dependency>


		<dependency>
			<groupId>com.konghq</groupId>
			<artifactId>unirest-java</artifactId>
			<version>3.14.5</version>
		</dependency>


		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-text</artifactId>
			<version>1.12.0</version>
		</dependency>

		<!--		JasperReport - Ireport INICIO-->
		<dependency>
			<groupId>net.sf.jasperreports</groupId>
			<artifactId>jasperreports</artifactId>
			<version>${net.sf.jasperreports}</version>
			<exclusions>
				<exclusion>
					<groupId>commons-logging</groupId>
					<artifactId>commons-logging</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/net.sf.jasperreports/jasperreports-fonts -->
		<dependency>
			<groupId>net.sf.jasperreports</groupId>
			<artifactId>jasperreports-fonts</artifactId>
			<version>${net.sf.jasperreports}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/net.sf.jasperreports/jasperreports-functions -->
		<dependency>
			<groupId>net.sf.jasperreports</groupId>
			<artifactId>jasperreports-functions</artifactId>
			<version>${net.sf.jasperreports}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/net.sf.jasperreports/jasperreports-metadata -->
		<dependency>
			<groupId>net.sf.jasperreports</groupId>
			<artifactId>jasperreports-metadata</artifactId>
			<version>${net.sf.jasperreports}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/net.sf.jasperreports/jasperreports-annotation-processors -->
		<dependency>
			<groupId>net.sf.jasperreports</groupId>
			<artifactId>jasperreports-annotation-processors</artifactId>
			<version>${net.sf.jasperreports}</version>
		</dependency>


		<dependency>
			<groupId>commons-fileupload</groupId>
			<artifactId>commons-fileupload</artifactId>
			<version>1.5</version>
		</dependency>


		<!-- Commons IO -->
		<dependency>
			<groupId>commons-io</groupId>
			<artifactId>commons-io</artifactId>
			<version>2.15.1</version>
		</dependency>

		<!--
		https://mvnrepository.com/artifact/net.sf.jasperreports/jasperreports-chart-themes -->
		<dependency>
			<groupId>net.sf.jasperreports</groupId>
			<artifactId>jasperreports-chart-themes</artifactId>
			<version>${net.sf.jasperreports}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/net.sf.jasperreports/jasperreports-chart-customizers -->
		<dependency>
			<groupId>net.sf.jasperreports</groupId>
			<artifactId>jasperreports-chart-customizers</artifactId>
			<version>${net.sf.jasperreports}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/net.sourceforge.barbecue/barbecue -->
		<dependency>
			<groupId>net.sourceforge.barbecue</groupId>
			<artifactId>barbecue</artifactId>
			<version>1.5-beta1</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/net.sf.barcode4j/barcode4j -->
		<dependency>
			<groupId>net.sf.barcode4j</groupId>
			<artifactId>barcode4j</artifactId>
			<version>2.1</version>
		</dependency>
		<!--		JasperReport - Ireport FIM-->

		<!-- https://mvnrepository.com/artifact/commons-cli/commons-cli -->
		<dependency>
			<groupId>commons-cli</groupId>
			<artifactId>commons-cli</artifactId>
			<version>1.5.0</version>
		</dependency>


		<!-- https://mvnrepository.com/artifact/com.google.guava/guava -->
		<dependency>
			<groupId>com.google.guava</groupId>
			<artifactId>guava</artifactId>
			<version>31.1-jre</version>
		</dependency>
		<dependency>
			<groupId>com.itextpdf</groupId>
			<artifactId>itextpdf</artifactId>
			<version>5.5.13.3</version>
		</dependency>
		<dependency>
			<groupId>com.itextpdf.tool</groupId>
			<artifactId>xmlworker</artifactId>
			<version>5.5.13.3</version>
		</dependency>
		<dependency>
			<groupId>com.itextpdf</groupId>
			<artifactId>html2pdf</artifactId>
			<version>4.0.3</version>
		</dependency>
		<dependency>
			<groupId>com.itextpdf</groupId>
			<artifactId>barcodes</artifactId>
			<version>7.2.3</version>
		</dependency>


		<dependency>
			<groupId>com.amazonaws</groupId>
			<artifactId>aws-java-sdk-s3</artifactId>
			<version>1.12.781</version>
			<exclusions>
				<exclusion>
					<groupId>commons-logging</groupId>
					<artifactId>commons-logging</artifactId>
				</exclusion>
			</exclusions>
		</dependency>

		<dependency>
			<groupId>com.konghq</groupId>
			<artifactId>unirest-java</artifactId>
			<version>3.13.11</version>
			<exclusions>
				<exclusion>
					<groupId>commons-logging</groupId>
					<artifactId>commons-logging</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<dependency>
			<groupId>com.google.code.gson</groupId>
			<artifactId>gson</artifactId>
			<version>2.12.1</version>
		</dependency>
		<dependency>
			<groupId>commons-codec</groupId>
			<artifactId>commons-codec</artifactId>
			<version>1.15</version>
		</dependency>
		<dependency>
			<groupId>org.bouncycastle</groupId>
			<artifactId>bcprov-jdk15on</artifactId>
			<version>${org.bouncycastle}</version>
		</dependency>
		<dependency>
			<groupId>org.bouncycastle</groupId>
			<artifactId>bcpkix-jdk15on</artifactId>
			<version>${org.bouncycastle}</version>
		</dependency>
		<dependency>
			<groupId>org.bouncycastle</groupId>
			<artifactId>bcutil-jdk15on</artifactId>
			<version>${org.bouncycastle}</version>
		</dependency>
		<dependency>
			<groupId>com.thoughtworks.xstream</groupId>
			<artifactId>xstream</artifactId>
			<version>1.4.19</version>
		</dependency>
		<dependency>
			<groupId>io.github.x-stream</groupId>
			<artifactId>mxparser</artifactId>
			<version>1.2.2</version>
		</dependency>

		<dependency>
			<groupId>org.apache.poi</groupId>
			<artifactId>poi</artifactId>
			<version>${org.apache.poi}</version>
		</dependency>
		<dependency>
			<groupId>org.apache.poi</groupId>
			<artifactId>poi-ooxml</artifactId>
			<version>${org.apache.poi}</version>
		</dependency>
		<dependency>
			<groupId>org.apache.poi</groupId>
			<artifactId>poi-scratchpad</artifactId>
			<version>${org.apache.poi}</version>
		</dependency>
		<dependency>
			<groupId>org.apache.poi</groupId>
			<artifactId>poi-excelant</artifactId>
			<version>${org.apache.poi}</version>
		</dependency>
		<dependency>
			<groupId>com.zaxxer</groupId>
			<artifactId>SparseBitSet</artifactId>
			<version>1.2</version>
		</dependency>
		<dependency>
			<groupId>org.apache.xmlbeans</groupId>
			<artifactId>xmlbeans</artifactId>
			<version>5.1.1</version>
		</dependency>
		<dependency>
			<groupId>org.quartz-scheduler</groupId>
			<artifactId>quartz</artifactId>
			<version>2.5.0</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.quartz-scheduler/quartz-jobs -->
		<dependency>
			<groupId>org.quartz-scheduler</groupId>
			<artifactId>quartz-jobs</artifactId>
			<version>2.5.0</version>
		</dependency>


		<dependency>
			<groupId>org.docx4j</groupId>
			<artifactId>docx4j</artifactId>
			<version>6.1.2</version>
			<exclusions>
				<exclusion>
					<groupId>org.slf4j</groupId>
					<artifactId>slf4j-api</artifactId>
				</exclusion>
				<exclusion>
					<groupId>org.slf4j</groupId>
					<artifactId>jcl-over-slf4j</artifactId>
				</exclusion>
				<exclusion>
					<groupId>org.slf4j</groupId>
					<artifactId>slf4j-log4j12</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.codelibs/jcifs -->
		<dependency>
			<groupId>org.codelibs</groupId>
			<artifactId>jcifs</artifactId>
			<version>2.1.39</version>
		</dependency>

		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<version>42.5.0</version>
		</dependency>
		<dependency>
			<groupId>com.opencsv</groupId>
			<artifactId>opencsv</artifactId>
			<version>5.7.0</version>
		</dependency>
		<dependency>
			<groupId>com.unboundid</groupId>
			<artifactId>unboundid-ldapsdk</artifactId>
			<version>6.0.6</version>
		</dependency>
		<dependency>
			<groupId>com.webcohesion.ofx4j</groupId>
			<artifactId>ofx4j</artifactId>
			<version>1.38</version>
			<exclusions>
				<exclusion>
					<groupId>org.javassist</groupId>
					<artifactId>javassist</artifactId>
				</exclusion>
				<exclusion>
					<groupId>org.reflections</groupId>
					<artifactId>reflections</artifactId>
				</exclusion>
				<exclusion>
					<groupId>jakarta.xml.bind</groupId>
					<artifactId>jakarta.xml.bind-api</artifactId>
				</exclusion>
				<exclusion>
					<groupId>commons-logging</groupId>
					<artifactId>commons-logging</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<dependency>
			<groupId>org.junit.jupiter</groupId>
			<artifactId>junit-jupiter-api</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.13.2</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.apache.pdfbox</groupId>
			<artifactId>pdfbox</artifactId>
			<version>2.0.27</version>
			<exclusions>
				<exclusion>
					<groupId>commons-logging</groupId>
					<artifactId>commons-logging</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<dependency>
			<groupId>org.apache.pdfbox</groupId>
			<artifactId>preflight</artifactId>
			<version>2.0.27</version>
			<exclusions>
				<exclusion>
					<groupId>commons-logging</groupId>
					<artifactId>commons-logging</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<dependency>
			<groupId>e-iceblue</groupId>
			<artifactId>spire.pdf.free</artifactId>
			<version>9.12.3</version>
		</dependency>
		<dependency>
			<groupId>org.graalvm.js</groupId>
			<artifactId>js</artifactId>
			<version>22.0.0</version>
		</dependency>
		<dependency>
			<groupId>org.graalvm.js</groupId>
			<artifactId>js-scriptengine</artifactId>
			<version>22.0.0</version>
		</dependency>

		<dependency>
			<groupId>org.apache.tika</groupId>
			<artifactId>tika-core</artifactId>
			<version>2.9.1</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/com.documents4j/documents4j-api -->
		<dependency>
			<groupId>com.documents4j</groupId>
			<artifactId>documents4j-api</artifactId>
			<version>${com.documents4j}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/com.documents4j/documents4j-transformer-msoffice-word -->
		<dependency>
			<groupId>com.documents4j</groupId>
			<artifactId>documents4j-transformer-msoffice-word</artifactId>
			<version>${com.documents4j}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/com.documents4j/documents4j-local -->
		<dependency>
			<groupId>com.documents4j</groupId>
			<artifactId>documents4j-local</artifactId>
			<version>${com.documents4j}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/com.documents4j/documents4j-transformer-msoffice-excel -->
		<dependency>
			<groupId>com.documents4j</groupId>
			<artifactId>documents4j-transformer-msoffice-excel</artifactId>
			<version>${com.documents4j}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/com.documents4j/documents4j-util-conversion -->
		<dependency>
			<groupId>com.documents4j</groupId>
			<artifactId>documents4j-util-conversion</artifactId>
			<version>${com.documents4j}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/com.documents4j/documents4j-transformer-msoffice-base -->
		<dependency>
			<groupId>com.documents4j</groupId>
			<artifactId>documents4j-transformer-msoffice-base</artifactId>
			<version>${com.documents4j}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/com.documents4j/documents4j-transformer-api -->
		<dependency>
			<groupId>com.documents4j</groupId>
			<artifactId>documents4j-transformer-api</artifactId>
			<version>${com.documents4j}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/com.documents4j/documents4j-util-all -->
		<dependency>
			<groupId>com.documents4j</groupId>
			<artifactId>documents4j-util-all</artifactId>
			<version>${com.documents4j}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/com.documents4j/documents4j-util-transformer-process -->
		<dependency>
			<groupId>com.documents4j</groupId>
			<artifactId>documents4j-util-transformer-process</artifactId>
			<version>${com.documents4j}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/com.documents4j/documents4j-transformer -->
		<dependency>
			<groupId>com.documents4j</groupId>
			<artifactId>documents4j-transformer</artifactId>
			<version>${com.documents4j}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/com.documents4j/documents4j-parent -->
		<dependency>
			<groupId>com.documents4j</groupId>
			<artifactId>documents4j-parent</artifactId>
			<version>${com.documents4j}</version>
			<type>pom</type>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/com.documents4j/documents4j-aggregation -->
		<dependency>
			<groupId>com.documents4j</groupId>
			<artifactId>documents4j-aggregation</artifactId>
			<version>${com.documents4j}</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/com.documents4j/documents4j-transformer-msoffice -->
		<dependency>
			<groupId>com.documents4j</groupId>
			<artifactId>documents4j-transformer-msoffice</artifactId>
			<version>${com.documents4j}</version>
			<type>pom</type>
		</dependency>

		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-fileupload2-jakarta</artifactId>
			<version>2.0.0-M1</version>
		</dependency>
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-fileupload2-core</artifactId>
			<version>2.0.0-M2</version>
		</dependency>

		<!-- https://mvnrepository.com/artifact/org.jfree/jfreechart -->
		<dependency>
			<groupId>org.jfree</groupId>
			<artifactId>jfreechart</artifactId>
			<version>1.5.5</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.jfree/jcommon -->
		<dependency>
			<groupId>org.jfree</groupId>
			<artifactId>jcommon</artifactId>
			<version>1.0.24</version>
		</dependency>

		<!-- https://mvnrepository.com/artifact/jakarta.mail/jakarta.mail-api -->
		<dependency>
			<groupId>jakarta.mail</groupId>
			<artifactId>jakarta.mail-api</artifactId>
			<version>2.1.3</version>
		</dependency>

		<!--
		https://mvnrepository.com/artifact/jakarta.xml.soap/jakarta.xml.soap-api -->
		<dependency>
			<groupId>jakarta.xml.soap</groupId>
			<artifactId>jakarta.xml.soap-api</artifactId>
			<version>3.0.2</version>
		</dependency>

		<!--		<dependency>-->
		<!--			<groupId>jakarta.platform</groupId>-->
		<!--			<artifactId>jakarta.jakartaee-web-api</artifactId>-->
		<!--			<version>10.0.0</version>-->
		<!--			<scope>provided</scope>-->
		<!--		</dependency>-->

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-autoconfigure -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-autoconfigure</artifactId>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-json -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-json</artifactId>
		</dependency>

		<!--
		https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-jdbc -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>


		<!-- https://mvnrepository.com/artifact/com.zaxxer/HikariCP -->
		<dependency>
			<groupId>com.zaxxer</groupId>
			<artifactId>HikariCP</artifactId>
			<version>6.2.1</version>
		</dependency>

		<dependency>
			<groupId>commons-net</groupId>
			<artifactId>commons-net</artifactId>
			<version>3.8.0</version>
		</dependency>
		<dependency>
			<groupId>org.json</groupId>
			<artifactId>json</artifactId>
			<version>20220924</version>
		</dependency>
		<dependency>
			<groupId>com.google.zxing</groupId>
			<artifactId>core</artifactId>
			<version>3.3.3</version>
		</dependency>
		<dependency>
			<groupId>org.jsoup</groupId>
			<artifactId>jsoup</artifactId>
			<version>1.16.2</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload -->
		<dependency>
			<groupId>commons-fileupload</groupId>
			<artifactId>commons-fileupload</artifactId>
			<version>1.4</version>
		</dependency>

		<dependency>
			<groupId>org.joinfaces</groupId>
			<artifactId>joinfaces</artifactId>
			<version>5.5.6</version>
		</dependency>

		<dependency>
			<groupId>org.joinfaces</groupId>
			<artifactId>joinfaces-autoconfigure</artifactId>
			<version>5.5.6</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/org.joinfaces/faces-spring-boot-starter -->
		<dependency>
			<groupId>org.joinfaces</groupId>
			<artifactId>faces-spring-boot-starter</artifactId>
			<version>5.5.6</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/org.joinfaces/primefaces-spring-boot-starter -->
		<dependency>
			<groupId>org.joinfaces</groupId>
			<artifactId>primefaces-spring-boot-starter</artifactId>
			<version>5.5.6</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/org.joinfaces/cdi-spring-boot-starter -->
		<dependency>
			<groupId>org.joinfaces</groupId>
			<artifactId>cdi-spring-boot-starter</artifactId>
			<version>5.5.6</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/org.joinfaces/weld-spring-boot-starter -->
		<dependency>
			<groupId>org.joinfaces</groupId>
			<artifactId>weld-spring-boot-starter</artifactId>
			<version>5.5.6</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/org.jboss.weld.servlet/weld-servlet-core -->
		<dependency>
			<groupId>org.jboss.weld.servlet</groupId>
			<artifactId>weld-servlet-core</artifactId>
			<version>6.0.3.Final</version>
		</dependency>
		<!--		 https://mvnrepository.com/artifact/org.glassfish.jaxb/jaxb-runtime -->
		<dependency>
			<groupId>org.glassfish.jaxb</groupId>
			<artifactId>jaxb-runtime</artifactId>
			<version>4.0.5</version>
		</dependency>

		<!-- https://mvnrepository.com/artifact/commons-io/commons-io -->
		<dependency>
			<groupId>commons-io</groupId>
			<artifactId>commons-io</artifactId>
			<version>2.18.0</version>
		</dependency>

		<dependency>
			<groupId>com.nimbusds</groupId>
			<artifactId>nimbus-jose-jwt</artifactId>
			<version>9.23</version>
		</dependency>


		<!--
		https://mvnrepository.com/artifact/jakarta.xml.bind/jakarta.xml.bind-api -->
		<dependency>
			<groupId>jakarta.xml.bind</groupId>
			<artifactId>jakarta.xml.bind-api</artifactId>
			<version>4.0.2</version>
		</dependency>

		<!-- https://mvnrepository.com/artifact/jakarta.ws.rs/jakarta.ws.rs-api -->
		<dependency>
			<groupId>jakarta.ws.rs</groupId>
			<artifactId>jakarta.ws.rs-api</artifactId>
			<version>4.0.0</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/nl.basjes.parse.useragent/yauaa -->
		<dependency>
			<groupId>nl.basjes.parse.useragent</groupId>
			<artifactId>yauaa</artifactId>
			<version>7.30.0</version>
		</dependency>
		<dependency>
			<groupId>io.sentry</groupId>
			<artifactId>sentry</artifactId>
			<version>8.5.0</version>
		</dependency>
		<dependency>
			<groupId>io.sentry</groupId>
			<artifactId>sentry-jdbc</artifactId>
			<version>8.5.0</version>
		</dependency>
		<dependency>
			<groupId>org.glassfish.web</groupId>
			<artifactId>jakarta.servlet.jsp.jstl</artifactId>
			<version>2.0.0</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/net.snowflake/snowflake-jdbc -->
		<dependency>
			<groupId>net.snowflake</groupId>
			<artifactId>snowflake-jdbc</artifactId>
			<version>3.11.1</version>
		</dependency>
		<dependency>
			<groupId>com.itextpdf</groupId>
			<artifactId>commons</artifactId>
			<version>9.2.0</version>
		</dependency>
		<dependency>
			<groupId>com.itextpdf</groupId>
			<artifactId>font-asian</artifactId>
			<version>9.2.0</version>
		</dependency>
		<dependency>
			<groupId>com.itextpdf</groupId>
			<artifactId>forms</artifactId>
			<version>9.2.0</version>
		</dependency>
		<dependency>
			<groupId>com.itextpdf</groupId>
			<artifactId>io</artifactId>
			<version>9.2.0</version>
		</dependency>
		<dependency>
			<groupId>com.itextpdf</groupId>
			<artifactId>kernel</artifactId>
			<version>9.2.0</version>
		</dependency>
		<dependency>
			<groupId>com.itextpdf</groupId>
			<artifactId>layout</artifactId>
			<version>9.2.0</version>
		</dependency>
		<dependency>
			<groupId>com.itextpdf</groupId>
			<artifactId>pdfa</artifactId>
			<version>9.2.0</version>
		</dependency>
		<dependency>
			<groupId>com.itextpdf</groupId>
			<artifactId>sign</artifactId>
			<version>9.2.0</version>
		</dependency>
		<dependency>
			<groupId>org.mozilla</groupId>
			<artifactId>rhino</artifactId>
			<version>1.7.13</version>
		</dependency>
		<dependency>
			<groupId>org.verapdf</groupId>
			<artifactId>validation-model-jakarta</artifactId>
			<version>1.28.1</version>
		</dependency>
		<dependency>
			<groupId>org.verapdf</groupId>
			<artifactId>pdfbox-validation-model-jakarta</artifactId>
			<version>1.28.1</version>
		</dependency>
		<dependency>
			<groupId>org.verapdf</groupId>
			<artifactId>parser</artifactId>
			<version>1.28.1</version>
		</dependency>
		<dependency>
			<groupId>org.verapdf</groupId>
			<artifactId>pdf-model</artifactId>
			<version>1.28.1</version>
		</dependency>
		<dependency>
			<groupId>org.verapdf</groupId>
			<artifactId>core-jakarta</artifactId>
			<version>1.28.1</version>
		</dependency>
		<dependency>
			<groupId>org.verapdf</groupId>
			<artifactId>verapdf-xmp-core-jakarta</artifactId>
			<version>1.28.1</version>
		</dependency>
		<dependency>
			<groupId>org.verapdf</groupId>
			<artifactId>feature-reporting-jakarta</artifactId>
			<version>1.28.1</version>
		</dependency>
		<dependency>
			<groupId>org.verapdf</groupId>
			<artifactId>metadata-fixer-jakarta</artifactId>
			<version>1.28.1</version>
		</dependency>
		<!--		<dependency>-->
		<!--		    <groupId>org.verapdf</groupId>-->
		<!--		    <artifactId>pdfbox-feature-reporting-jakarta</artifactId>-->
		<!--		    <version>1.28.1</version>-->
		<!--		</dependency>-->
		<!--		<dependency>-->
		<!--		    <groupId>org.verapdf</groupId>-->
		<!--		    <artifactId>pdfbox-metadata-fixer-jakarta</artifactId>-->
		<!--		    <version>1.28.1</version>-->
		<!--		</dependency>-->
		<!--		<dependency>-->
		<!--		    <groupId>org.verapdf.pdfbox</groupId>-->
		<!--		    <artifactId>pdfbox</artifactId>-->
		<!--		    <version>2.0.73</version>-->
		<!--		</dependency>-->
		<dependency>
			<groupId>org.verapdf</groupId>
			<artifactId>org.verapdf</artifactId>
			<version>1.28.1</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/io.rest-assured/rest-assured -->
		<dependency>
			<groupId>io.rest-assured</groupId>
			<artifactId>rest-assured</artifactId>
			<version>5.5.6</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/io.rest-assured/json-path -->
		<dependency>
			<groupId>io.rest-assured</groupId>
			<artifactId>json-path</artifactId>
			<version>5.5.6</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/io.rest-assured/rest-assured-common -->
		<dependency>
			<groupId>io.rest-assured</groupId>
			<artifactId>rest-assured-common</artifactId>
			<version>5.5.6</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.apache.commons/commons-lang3 -->
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-lang3</artifactId>
			<version>3.18.0</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.apache.groovy/groovy -->
		<dependency>
			<groupId>org.apache.groovy</groupId>
			<artifactId>groovy</artifactId>
			<version>4.0.22</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.apache.groovy/groovy-json -->
		<dependency>
			<groupId>org.apache.groovy</groupId>
			<artifactId>groovy-json</artifactId>
			<version>4.0.22</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/io.rest-assured/xml-path -->
		<dependency>
			<groupId>io.rest-assured</groupId>
			<artifactId>xml-path</artifactId>
			<version>5.5.6</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.apache.groovy/groovy-xml -->
		<dependency>
			<groupId>org.apache.groovy</groupId>
			<artifactId>groovy-xml</artifactId>
			<version>4.0.22</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.ccil.cowan.tagsoup/tagsoup -->
		<dependency>
			<groupId>org.ccil.cowan.tagsoup</groupId>
			<artifactId>tagsoup</artifactId>
			<version>1.2.1</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.hamcrest/hamcrest -->
		<dependency>
			<groupId>org.hamcrest</groupId>
			<artifactId>hamcrest</artifactId>
			<version>2.2</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/com.auth0/java-jwt -->
		<dependency>
			<groupId>com.auth0</groupId>
			<artifactId>java-jwt</artifactId>
			<version>3.3.0</version>
		</dependency>
		<!--
		https://mvnrepository.com/artifact/io.rest-assured/json-schema-validator -->
		<dependency>
			<groupId>io.rest-assured</groupId>
			<artifactId>json-schema-validator</artifactId>
			<version>5.5.6</version>
		</dependency>

	</dependencies>
	<build>
		<sourceDirectory>src/java</sourceDirectory>
		<resources>
			<resource>
				<directory>src/java</directory>
				<excludes>
					<exclude>**/*.java</exclude>
				</excludes>
			</resource>
			<resource>
				<directory>src/resources</directory>
			</resource>
		</resources>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-war-plugin</artifactId>
				<configuration>
					<warSourceDirectory>web</warSourceDirectory>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>