<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    version="1.0" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xmlns:dc="http://purl.org/dc/elements/1.1/" 
    xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="dc oai_dc xsi"
>
    <xsl:output method="xml" indent="yes" encoding="UTF-8" />
    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <xsl:for-each select="dc:dc|oai_dc:dc">
            <xsl:apply-templates select="." />
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="dc:dc|oai_dc:dc">
        <dublin_core>
            <xsl:for-each select="dc:title">
                <dcvalue element="title" qualifier="none">
                    <xsl:value-of select="."/>
                </dcvalue>
            </xsl:for-each>
            <xsl:for-each select="dc:subject">
                <dcvalue element="subject" qualifier="none">
                    <xsl:value-of select="."/>
                </dcvalue>
            </xsl:for-each>
            <xsl:for-each select="dc:rights">
                <dcvalue element="rights" qualifier="none">
                    <xsl:value-of select="."/>
                </dcvalue>
            </xsl:for-each>
            <xsl:for-each select="dc:identifier">
                <dcvalue element="identifier" qualifier="none">
                    <xsl:value-of select="."/>
                </dcvalue>
            </xsl:for-each>
            <xsl:for-each select="dc:relation">
                <dcvalue element="relation" qualifier="none">
                    <xsl:value-of select="."/>
                </dcvalue>
            </xsl:for-each>
            <xsl:for-each select="dc:coverage">
                <dcvalue element="coverage" qualifier="none">
                    <xsl:value-of select="."/>
                </dcvalue>
            </xsl:for-each>
            <xsl:for-each select="dc:description">
                <dcvalue element="description" qualifier="none">
                    <xsl:value-of select="."/>
                </dcvalue>
            </xsl:for-each>
            <xsl:for-each select="dc:format">
                <dcvalue element="format" qualifier="none">
                    <xsl:value-of select="."/>
                </dcvalue>
            </xsl:for-each>
            <xsl:for-each select="dc:date">
                <dcvalue element="date" qualifier="none">
                    <xsl:value-of select="."/>
                </dcvalue>
            </xsl:for-each>
            <xsl:for-each select="dc:language">
                <dcvalue element="language" qualifier="none">
                    <xsl:value-of select="."/>
                </dcvalue>
            </xsl:for-each>
            <xsl:for-each select="dc:type">
                <dcvalue element="type" qualifier="none">
                    <xsl:value-of select="."/>
                </dcvalue>
            </xsl:for-each>
            <xsl:for-each select="dc:creator">
                <dcvalue element="creator" qualifier="none">
                    <xsl:value-of select="."/>
                </dcvalue>
            </xsl:for-each>
            <xsl:for-each select="dc:publisher">
                <dcvalue element="publisher" qualifier="none">
                    <xsl:value-of select="."/>
                </dcvalue>
            </xsl:for-each>
        </dublin_core>
    </xsl:template>
</xsl:stylesheet>
