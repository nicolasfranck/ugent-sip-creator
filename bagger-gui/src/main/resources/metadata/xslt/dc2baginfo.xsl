<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    version="1.0" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xmlns:dc="http://purl.org/dc/elements/1.1/" 
    xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>
    <xsl:output method="text" indent="no" encoding="UTF-8"/>
    <xsl:strip-space elements="*"/>

    <xsl:variable name="br">
        <xsl:text>&#10;</xsl:text>
    </xsl:variable>
    <xsl:template match="/">
        <xsl:for-each select="dc:dc|oai_dc:dc">
            <xsl:apply-templates select="." />
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="dc:dc|oai_dc:dc">
        <xsl:for-each select="dc:title">
            <xsl:text>DC-Title: </xsl:text>
            <xsl:value-of select="."/>
            <xsl:value-of select="$br"/>
        </xsl:for-each>
        <xsl:for-each select="dc:subject">
            <xsl:text>DC-Subject: </xsl:text>
            <xsl:value-of select="."/>
            <xsl:value-of select="$br"/>
        </xsl:for-each>
        <xsl:for-each select="dc:rights">
            <xsl:text>DC-Rights: </xsl:text>
            <xsl:value-of select="."/>
            <xsl:value-of select="$br"/>
        </xsl:for-each>
        <xsl:for-each select="dc:identifier">
            <xsl:text>DC-Identifier: </xsl:text>
            <xsl:value-of select="."/>
            <xsl:value-of select="$br"/>
        </xsl:for-each>
        <xsl:for-each select="dc:relation">
            <xsl:text>DC-Relation: </xsl:text>
            <xsl:value-of select="."/>
            <xsl:value-of select="$br"/>
        </xsl:for-each>
        <xsl:for-each select="dc:coverage">
            <xsl:text>DC-Coverage: </xsl:text>
            <xsl:value-of select="."/>
            <xsl:value-of select="$br"/>
        </xsl:for-each>
        <xsl:for-each select="dc:description">
            <xsl:text>DC-Description: </xsl:text>
            <xsl:value-of select="."/>
            <xsl:value-of select="$br"/>
        </xsl:for-each>
        <xsl:for-each select="dc:format">
            <xsl:text>DC-Format: </xsl:text>
            <xsl:value-of select="."/>
            <xsl:value-of select="$br"/>
        </xsl:for-each>
        <xsl:for-each select="dc:date">
            <xsl:text>DC-Date: </xsl:text>
            <xsl:value-of select="."/>
            <xsl:value-of select="$br"/>
        </xsl:for-each>
        <xsl:for-each select="dc:language">
            <xsl:text>DC-Language: </xsl:text>
            <xsl:value-of select="."/>
            <xsl:value-of select="$br"/>
        </xsl:for-each>
        <xsl:for-each select="dc:type">
            <xsl:text>DC-Type: </xsl:text>
            <xsl:value-of select="."/>
            <xsl:value-of select="$br"/>
        </xsl:for-each>
        <xsl:for-each select="dc:creator">
            <xsl:text>DC-Creator: </xsl:text>
            <xsl:value-of select="."/>
            <xsl:value-of select="$br"/>
        </xsl:for-each>
        <xsl:for-each select="dc:publisher">
            <xsl:text>DC-Publisher: </xsl:text>
            <xsl:value-of select="."/>
            <xsl:value-of select="$br"/>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
