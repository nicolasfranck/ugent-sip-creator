<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:marc="http://www.loc.gov/MARC21/slim" xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="marc">
    
    <xsl:output method="xml" indent="no" encoding="UTF-8"/>
    <xsl:strip-space elements="*"/>

     <!-- 08/08/08: tmee added corrected chopPunctuation templates for 260c -->
    <!-- 08/19/04: ntra added "marc:" prefix to datafield element -->
    <!-- 12/14/07: ntra added url encoding template -->
    <!-- url encoding -->

    <xsl:variable name="ascii">
        <xsl:text> !"#$%&amp;'()*+,-./0123456789:;&lt;=&gt;?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~</xsl:text>
    </xsl:variable>

    <xsl:variable name="latin1">
        <xsl:text> ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ</xsl:text>
    </xsl:variable>
    <!-- Characters that usually don't need to be escaped -->
    <xsl:variable name="safe">
        <xsl:text>!'()*-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz~</xsl:text>
    </xsl:variable>

    <xsl:variable name="hex">0123456789ABCDEF</xsl:variable>


    <xsl:template name="datafield">
        <xsl:param name="tag"/>
        <xsl:param name="ind1">
            <xsl:text> </xsl:text>
        </xsl:param>
        <xsl:param name="ind2">
            <xsl:text> </xsl:text>
        </xsl:param>
        <xsl:param name="subfields"/>
        <xsl:element name="marc:datafield">
            <xsl:attribute name="tag">
                <xsl:value-of select="$tag"/>
            </xsl:attribute>
            <xsl:attribute name="ind1">
                <xsl:value-of select="$ind1"/>
            </xsl:attribute>
            <xsl:attribute name="ind2">
                <xsl:value-of select="$ind2"/>
            </xsl:attribute>
            <xsl:copy-of select="$subfields"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="subfieldSelect">
        <xsl:param name="codes">abcdefghijklmnopqrstuvwxyz</xsl:param>
        <xsl:param name="delimeter">
            <xsl:text> </xsl:text>
        </xsl:param>
        <xsl:variable name="str">
            <xsl:for-each select="marc:subfield">
                <xsl:if test="contains($codes, @code)">
                    <xsl:value-of select="text()"/>
                    <xsl:value-of select="$delimeter"/>
                </xsl:if>
            </xsl:for-each>
        </xsl:variable>
        <xsl:value-of select="substring($str,1,string-length($str)-string-length($delimeter))"/>
    </xsl:template>

    <xsl:template name="buildSpaces">
        <xsl:param name="spaces"/>
        <xsl:param name="char">
            <xsl:text> </xsl:text>
        </xsl:param>
        <xsl:if test="$spaces&gt;0">
            <xsl:value-of select="$char"/>
            <xsl:call-template name="buildSpaces">
                <xsl:with-param name="spaces" select="$spaces - 1"/>
                <xsl:with-param name="char" select="$char"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template name="chopPunctuation">
        <xsl:param name="chopString"/>
        <xsl:param name="punctuation">
            <xsl:text>.:,;/ </xsl:text>
        </xsl:param>
        <xsl:variable name="length" select="string-length($chopString)"/>
        <xsl:choose>
            <xsl:when test="$length=0"/>
            <xsl:when test="contains($punctuation, substring($chopString,$length,1))">
                <xsl:call-template name="chopPunctuation">
                    <xsl:with-param name="chopString" select="substring($chopString,1,$length - 1)"/>
                    <xsl:with-param name="punctuation" select="$punctuation"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="not($chopString)"/>
            <xsl:otherwise>
                <xsl:value-of select="$chopString"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="chopPunctuationFront">
        <xsl:param name="chopString"/>
        <xsl:variable name="length" select="string-length($chopString)"/>
        <xsl:choose>
            <xsl:when test="$length=0"/>
            <xsl:when test="contains('.:,;/[ ', substring($chopString,1,1))">
                <xsl:call-template name="chopPunctuationFront">
                    <xsl:with-param name="chopString" select="substring($chopString,2,$length - 1)"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="not($chopString)"/>
            <xsl:otherwise>
                <xsl:value-of select="$chopString"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="chopPunctuationBack">
        <xsl:param name="chopString"/>
        <xsl:param name="punctuation">
            <xsl:text>.:,;/] </xsl:text>
        </xsl:param>
        <xsl:variable name="length" select="string-length($chopString)"/>
        <xsl:choose>
            <xsl:when test="$length=0"/>
            <xsl:when test="contains($punctuation, substring($chopString,$length,1))">
                <xsl:call-template name="chopPunctuation">
                    <xsl:with-param name="chopString" select="substring($chopString,1,$length - 1)"/>
                    <xsl:with-param name="punctuation" select="$punctuation"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="not($chopString)"/>
            <xsl:otherwise>
                <xsl:value-of select="$chopString"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- nate added 12/14/2007 for lccn.loc.gov: url encode ampersand, etc. -->
    <xsl:template name="url-encode">

        <xsl:param name="str"/>

        <xsl:if test="$str">
            <xsl:variable name="first-char" select="substring($str,1,1)"/>
            <xsl:choose>
                <xsl:when test="contains($safe,$first-char)">
                    <xsl:value-of select="$first-char"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:variable name="codepoint">
                        <xsl:choose>
                            <xsl:when test="contains($ascii,$first-char)">
                                <xsl:value-of select="string-length(substring-before($ascii,$first-char)) + 32"/>
                            </xsl:when>
                            <xsl:when test="contains($latin1,$first-char)">
                                <xsl:value-of select="string-length(substring-before($latin1,$first-char)) + 160"/>
                                <!-- was 160 -->
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:message terminate="no">Warning: string contains a character
                                    that is out of range! Substituting "?".</xsl:message>
                                <xsl:text>63</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <xsl:variable name="hex-digit1" select="substring($hex,floor($codepoint div 16) + 1,1)"/>
                    <xsl:variable name="hex-digit2" select="substring($hex,$codepoint mod 16 + 1,1)"/>
                    <!-- <xsl:value-of select="concat('%',$hex-digit2)"/> -->
                    <xsl:value-of select="concat('%',$hex-digit1,$hex-digit2)"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="string-length($str) &gt; 1">
                <xsl:call-template name="url-encode">
                    <xsl:with-param name="str" select="substring($str,2)"/>
                </xsl:call-template>
            </xsl:if>
        </xsl:if>
    </xsl:template>


    <!--
    Fixed 530 Removed type="original" from dc:relation 2010-11-19 tmee
    Fixed 500 fields. 2006-12-11 ntra
    Added ISBN and deleted attributes 6/04 jer
    -->
    <xsl:template match="/">
        <xsl:if test="marc:collection">
            <oai_dc:dcCollection xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd">
                <xsl:for-each select="marc:collection">
                    <xsl:for-each select="marc:record">
                        <oai_dc:dc>
                            <xsl:apply-templates select="."/>
                        </oai_dc:dc>
                    </xsl:for-each>
                </xsl:for-each>
            </oai_dc:dcCollection>
        </xsl:if>
        <xsl:if test="marc:record">
            <oai_dc:dc xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd">
                <xsl:apply-templates/>
            </oai_dc:dc>
        </xsl:if>
    </xsl:template>
    <xsl:template match="marc:record">
        <xsl:variable name="leader" select="marc:leader"/>
        <xsl:variable name="leader6" select="substring($leader,7,1)"/>
        <xsl:variable name="leader7" select="substring($leader,8,1)"/>
        <xsl:variable name="controlField008" select="marc:controlfield[@tag=008]"/>
        <xsl:for-each select="marc:datafield[@tag=245]">
            <dc:title>
                <xsl:call-template name="subfieldSelect">
                    <xsl:with-param name="codes">abfghk</xsl:with-param>
                </xsl:call-template>
            </dc:title>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=100]|marc:datafield[@tag=110]|marc:datafield[@tag=111]|marc:datafield[@tag=700]|marc:datafield[@tag=710]|marc:datafield[@tag=711]|marc:datafield[@tag=720]">
            <dc:creator>
                <xsl:value-of select="."/>
            </dc:creator>
        </xsl:for-each>
        <dc:type>
            <xsl:if test="$leader7='c'">
                <!--Remove attribute 6/04 jer-->
                <!--<xsl:attribute name="collection">yes</xsl:attribute>-->
                <xsl:text>collection</xsl:text>
            </xsl:if>
            <xsl:if test="$leader6='d' or $leader6='f' or $leader6='p' or $leader6='t'">
                <!--Remove attribute 6/04 jer-->
                <!--<xsl:attribute name="manuscript">yes</xsl:attribute>-->
                <xsl:text>manuscript</xsl:text>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="$leader6='a' or $leader6='t'">text</xsl:when>
                <xsl:when test="$leader6='e' or $leader6='f'">cartographic</xsl:when>
                <xsl:when test="$leader6='c' or $leader6='d'">notated music</xsl:when>
                <xsl:when test="$leader6='i' or $leader6='j'">sound recording</xsl:when>
                <xsl:when test="$leader6='k'">still image</xsl:when>
                <xsl:when test="$leader6='g'">moving image</xsl:when>
                <xsl:when test="$leader6='r'">three dimensional object</xsl:when>
                <xsl:when test="$leader6='m'">software, multimedia</xsl:when>
                <xsl:when test="$leader6='p'">mixed material</xsl:when>
            </xsl:choose>
        </dc:type>
        <xsl:for-each select="marc:datafield[@tag=655]">
            <dc:type>
                <xsl:value-of select="."/>
            </dc:type>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=260]">
            <dc:publisher>
                <xsl:call-template name="subfieldSelect">
                    <xsl:with-param name="codes">ab</xsl:with-param>
                </xsl:call-template>
            </dc:publisher>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=260]/marc:subfield[@code='c']">
            <dc:date>
                <xsl:value-of select="."/>
            </dc:date>
        </xsl:for-each>
        <dc:language>
            <xsl:value-of select="substring($controlField008,36,3)"/>
        </dc:language>
        <xsl:for-each select="marc:datafield[@tag=856]/marc:subfield[@code='q']">
            <dc:format>
                <xsl:value-of select="."/>
            </dc:format>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=520]">
            <dc:description>
                <xsl:value-of select="marc:subfield[@code='a']"/>
            </dc:description>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=521]">
            <dc:description>
                <xsl:value-of select="marc:subfield[@code='a']"/>
            </dc:description>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[500&lt;= @tag and @tag&lt;= 599 ][not(@tag=506 or @tag=530 or @tag=540 or @tag=546)]">
            <dc:description>
                <xsl:value-of select="marc:subfield[@code='a']"/>
            </dc:description>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=600]">
            <dc:subject>
                <xsl:call-template name="subfieldSelect">
                    <xsl:with-param name="codes">abcdq</xsl:with-param>
                </xsl:call-template>
            </dc:subject>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=610]">
            <dc:subject>
                <xsl:call-template name="subfieldSelect">
                    <xsl:with-param name="codes">abcdq</xsl:with-param>
                </xsl:call-template>
            </dc:subject>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=611]">
            <dc:subject>
                <xsl:call-template name="subfieldSelect">
                    <xsl:with-param name="codes">abcdq</xsl:with-param>
                </xsl:call-template>
            </dc:subject>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=630]">
            <dc:subject>
                <xsl:call-template name="subfieldSelect">
                    <xsl:with-param name="codes">abcdq</xsl:with-param>
                </xsl:call-template>
            </dc:subject>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=650]">
            <dc:subject>
                <xsl:call-template name="subfieldSelect">
                    <xsl:with-param name="codes">abcdq</xsl:with-param>
                </xsl:call-template>
            </dc:subject>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=653]">
            <dc:subject>
                <xsl:call-template name="subfieldSelect">
                    <xsl:with-param name="codes">abcdq</xsl:with-param>
                </xsl:call-template>
            </dc:subject>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=752]">
            <dc:coverage>
                <xsl:call-template name="subfieldSelect">
                    <xsl:with-param name="codes">abcd</xsl:with-param>
                </xsl:call-template>
            </dc:coverage>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=530]">
            <dc:relation>
                <xsl:call-template name="subfieldSelect">
                    <xsl:with-param name="codes">abcdu</xsl:with-param>
                </xsl:call-template>
            </dc:relation>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=760]|marc:datafield[@tag=762]|marc:datafield[@tag=765]|marc:datafield[@tag=767]|marc:datafield[@tag=770]|marc:datafield[@tag=772]|marc:datafield[@tag=773]|marc:datafield[@tag=774]|marc:datafield[@tag=775]|marc:datafield[@tag=776]|marc:datafield[@tag=777]|marc:datafield[@tag=780]|marc:datafield[@tag=785]|marc:datafield[@tag=786]|marc:datafield[@tag=787]">
            <dc:relation>
                <xsl:call-template name="subfieldSelect">
                    <xsl:with-param name="codes">ot</xsl:with-param>
                </xsl:call-template>
            </dc:relation>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=856]">
            <dc:identifier>
                <xsl:value-of select="marc:subfield[@code='u']"/>
            </dc:identifier>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=020]">
            <dc:identifier>
                <xsl:text>URN:ISBN:</xsl:text>
                <xsl:value-of select="marc:subfield[@code='a']"/>
            </dc:identifier>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=506]">
            <dc:rights>
                <xsl:value-of select="marc:subfield[@code='a']"/>
            </dc:rights>
        </xsl:for-each>
        <xsl:for-each select="marc:datafield[@tag=540]">
            <dc:rights>
                <xsl:value-of select="marc:subfield[@code='a']"/>
            </dc:rights>
        </xsl:for-each>
        <!--</oai_dc:dc>-->
    </xsl:template>
</xsl:stylesheet>

<!-- Stylus Studio meta-information - (c) 2004-2005. Progress Software Corporation. All rights reserved.
<metaInformation>
<scenarios ><scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" url="..\..\..\..\..\..\..\..\..\..\javadev4\testsets\diacriticu8.xml" htmlbaseurl="" outputurl="" processortype="internal" useresolver="yes" profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" validateoutput="no" validator="internal" customvalidator=""/></scenarios><MapperMetaTag><MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/><MapperBlockPosition></MapperBlockPosition><TemplateContext></TemplateContext><MapperFilter side="source"></MapperFilter></MapperMetaTag>
</metaInformation>
-->
