<?xml version="1.0" encoding="UTF-8"?>
<!--
    Author: Nicolas Franck
    University Library of Ghent
    7 december 2012

    http://artefactual.com/wiki/index.php?title=Crosswalks:_Dublin_Core
-->
<xsl:stylesheet 
    version="1.0" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xmlns:dc="http://purl.org/dc/elements/1.1/" 
    xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ead="urn:isbn:1-931666-22-9"
    exclude-result-prefixes="dc oai_dc xsi ead"
>   
    <xsl:output method="xml" indent="yes" encoding="UTF-8" />
    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <xsl:apply-templates select="ead:ead" />
    </xsl:template>

    <xsl:template match="ead:ead">
        <dc:dc>
            <xsl:for-each select="ead:archdesc/ead:did/ead:unittitle">
                <dc:title><xsl:value-of select="." /></dc:title>
            </xsl:for-each>
            <xsl:for-each select="(
                ead:archdesc/ead:did/ead:origination/ead:persname |
                ead:archdesc/ead:did/ead:origination/ead:corpname |
                ead:archdesc/ead:did/ead:origination/ead:famname |
                ead:archdesc/ead:did/ead:origination/ead:name
            )">
                <dc:creator><xsl:value-of select="." /></dc:creator>
            </xsl:for-each>
            <xsl:for-each select="ead:archdesc/ead:controlaccess/ead:subject">
                <dc:subject><xsl:value-of select="." /></dc:subject>
            </xsl:for-each>
            <xsl:for-each select="(ead:archdesc/ead:scopecontent | ead:archdesc/ead:did/ead:abstract)">
                <dc:description><xsl:value-of select="." /></dc:description>
            </xsl:for-each>
            <xsl:for-each select="ead:archdesc/ead:did/ead:unittitle/ead:imprint/ead:publisher">
                <dc:publisher><xsl:value-of select="." /></dc:publisher>
            </xsl:for-each>
            <xsl:for-each select="ead:archdesc/ead:did/ead:unitdate">
                <dc:date><xsl:value-of select="." /></dc:date>
            </xsl:for-each>
            <xsl:for-each select="ead:archdesc/ead:did/ead:physdesc/ead:genreform">
                <dc:type><xsl:value-of select="." /></dc:type>
            </xsl:for-each>
            <xsl:for-each select="(
                ead:archdesc/ead:did/ead:physdesc/ead:extent | 
                ead:archdesc/ead:did/ead:physdesc/ead:dimensions | 
                ead:archdesc/ead:did/ead:physdesc/ead:genreform |
                ead:archdesc/ead:did/ead:physdesc/ead:physfacet
            )">
                <dc:format><xsl:value-of select="." /></dc:format>
            </xsl:for-each>
            <xsl:for-each select="(ead:eadheader/ead:eadid | ead:archdesc/ead:did/ead:unitid)">
                <dc:identifier><xsl:value-of select="." /></dc:identifier>
            </xsl:for-each>
            <xsl:for-each select="ead:archdesc/ead:did/ead:langmaterial/ead:language">
                <dc:language><xsl:value-of select="." /></dc:language>
            </xsl:for-each>
            <xsl:for-each select="(//ead:relatedmaterial | //ead:separatedmaterial)">
                <dc:relation><xsl:value-of select="." /></dc:relation>
            </xsl:for-each>
            <xsl:for-each select="(//controlaccess | //geogname)">
                <dc:coverage><xsl:value-of select="." /></dc:coverage>
            </xsl:for-each>
            <xsl:for-each select="(ead:archdesc/ead:accessrestrict | ead:archdesc/ead:userestrict)">
                <dc:rights><xsl:value-of select="." /></dc:rights>
            </xsl:for-each>
        </dc:dc>
    </xsl:template>
</xsl:stylesheet>
