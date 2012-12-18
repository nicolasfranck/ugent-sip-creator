<?xml version="1.0" encoding="UTF-8"?>
<!--
    Author: Nicolas Franck
    University Library of Ghent
    18 december 2012

    http://dlib.ionio.gr/standards/EAD_to_MODS_v1.pdf
-->
<xsl:stylesheet 
    version="1.0" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ead="urn:isbn:1-931666-22-9"
    xmlns:mods="http://www.loc.gov/mods/v3"
    exclude-result-prefixes="mods xsi ead"
>   
    <xsl:output method="xml" indent="yes" encoding="UTF-8" />
    <xsl:strip-space elements="*"/>

    <xsl:variable name="levels">
        <xsl:text>collection fonds class recordgrp series subfonds subgrp subseries file item</xsl:text>
    </xsl:variable>

    <xsl:template match="/">
        <xsl:apply-templates select="ead:ead" />
    </xsl:template>

    <xsl:template match="ead:ead">
        <mods:mods>
            <xsl:for-each select="(
                ead:archdesc/@level |
                ead:archdesc/ead:dsc/ead:c/@level |
                ead:archdesc/ead:dsc/ead:c01/@level |
                ead:archdesc/ead:dsc/ead:c02/@level |
                ead:archdesc/ead:dsc/ead:c03/@level |
                ead:archdesc/ead:dsc/ead:c04/@level |
                ead:archdesc/ead:dsc/ead:c05/@level |
                ead:archdesc/ead:dsc/ead:c06/@level |
                ead:archdesc/ead:dsc/ead:c07/@level |
                ead:archdesc/ead:dsc/ead:c08/@level |
                ead:archdesc/ead:dsc/ead:c09/@level |
                ead:archdesc/ead:dsc/ead:c10/@level |
                ead:archdesc/ead:dsc/ead:c11/@level |
                ead:archdesc/ead:dsc/ead:c12/@level
            )">
                <mods:physicalDescription>
                    <xsl:variable name="level" select="." />
                    <mods:note>
                        <xsl:choose>
                            <xsl:when test="contains($levels,$level)">
                                <xsl:attribute name="level" >
                                    <xsl:value-of select="$level" />
                                </xsl:attribute>                                
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:attribute name="level">
                                    <xsl:text>otherlevel</xsl:text>
                                </xsl:attribute>
                                <xsl:attribute name="otherlevel" >
                                    <xsl:value-of select="$level" />
                                </xsl:attribute>
                            </xsl:otherwise>
                        </xsl:choose>                        
                    </mods:note>
                </mods:physicalDescription>
            </xsl:for-each>
            <xsl:for-each select="//ead:did/ead:unittitle">
                <mods:titleInfo>
                    <mods:title><xsl:value-of select="." /></mods:title>
                </mods:titleInfo>
            </xsl:for-each>
            <xsl:for-each select="//ead:did/ead:unitdate">
                <mods:originInfo>
                    <mods:dateCreated>
                        <xsl:value-of select="." />
                    </mods:dateCreated>
                </mods:originInfo>
            </xsl:for-each>
            <xsl:for-each select="//ead:did/ead:unitid">
                <mods:identifier>
                    <xsl:value-of select="." />
                </mods:identifier>
            </xsl:for-each>
            <xsl:for-each select="//ead:did/ead:physdesc">
                <mods:physicalDescription>
                    <xsl:for-each select="ead:extent">
                        <mods:extent><xsl:value-of select="." /></mods:extent>
                    </xsl:for-each>
                    <xsl:for-each select="ead:dimensions">
                        <mods:extent><xsl:value-of select="." /></mods:extent>
                    </xsl:for-each>
                </mods:physicalDescription>
            </xsl:for-each>
            <xsl:for-each select="//ead:did/ead:note">
                <mods:note><xsl:value-of select="." /></mods:note>
            </xsl:for-each>
            <xsl:for-each select="//ead:did/ead:origination">
                <mods:name><mods:namePart><xsl:value-of select="." /></mods:namePart></mods:name>
            </xsl:for-each>
            <xsl:for-each select="//ead:did/ead:repository">
                <mods:location><mods:physicalLocation><xsl:value-of select="." /></mods:physicalLocation></mods:location>
            </xsl:for-each>
            <xsl:for-each select="//ead:did/ead:langmaterial/ead:language">
                <mods:language><mods:languageTerm><xsl:value-of select="." /></mods:languageTerm></mods:language>
            </xsl:for-each>
            <xsl:for-each select="//ead:did/ead:abstract">
                <mods:abstract><xsl:value-of select="." /></mods:abstract>
            </xsl:for-each>
            <xsl:for-each select="//ead:did/ead:dao/@href">
                <mods:location><mods:url><xsl:value-of select="." /></mods:url></mods:location>
            </xsl:for-each>
            <xsl:for-each select="//ead:did/ead:materialspec">
                <mods:note type="MaterialSpecificDetails"><xsl:value-of select="." /></mods:note>
            </xsl:for-each>
            <xsl:for-each select="//ead:did/ead:physloc">
                <mods:location><mods:physicalLocation><xsl:value-of select="." /></mods:physicalLocation></mods:location>
            </xsl:for-each>
            <xsl:for-each select="(
                //ead:controlaccess/text() |
                //ead:controlaccess/ead:controlaccess
            )">
                <mods:subject><xsl:value-of select="." /></mods:subject>
            </xsl:for-each>
        </mods:mods>
    </xsl:template>
</xsl:stylesheet>
