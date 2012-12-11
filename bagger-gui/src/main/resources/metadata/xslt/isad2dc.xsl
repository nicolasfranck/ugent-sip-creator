<?xml version="1.0" encoding="UTF-8"?>
<!--
    Author: Nicolas Franck
            nicolas.franck@ugent.be
    University Library of Ghent
    Mapping ISAD(G) to Dublin Core
-->
<xsl:stylesheet 
    version="1.0" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xmlns:dc="http://purl.org/dc/elements/1.1/" 
    xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:isad="ISADG"
    exclude-result-prefixes="dc oai_dc xsi isad"
>   
    <xsl:output method="xml" indent="yes" encoding="UTF-8" />
    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <xsl:apply-templates select="isad:archivaldescription" />
    </xsl:template>

    <xsl:template match="isad:archivaldescription">

        <xsl:for-each select="isad:identity/isad:reference">
            <dc:identifier><xsl:value-of select="." /></dc:identifier>
        </xsl:for-each>

        <xsl:for-each select="isad:identity/isad:title">
            <dc:title><xsl:value-of select="." /></dc:title>
        </xsl:for-each>

        <xsl:for-each select="(
            isad:identity/isad:date | 
            isad:description_control/isad:descriptionsdate
        )">
            <dc:date><xsl:value-of select="." /></dc:date>
        </xsl:for-each>

        <xsl:for-each select="(
            isad:identity/isad:descriptionlevel | 
            isad:description_control/isad:rulesconventions
        )">
            <dc:type><xsl:value-of select="." /></dc:type>
        </xsl:for-each>

        <xsl:for-each select="(
            isad:identity/isad:extent | 
            isad:conditions_access_use/isad:phystech
        )">
            <dc:format><xsl:value-of select="." /></dc:format>
        </xsl:for-each>

        <xsl:for-each select="isad:context/isad:creator">
            <dc:creator><xsl:value-of select="." /></dc:creator>
        </xsl:for-each>

        <xsl:for-each select="(
            isad:context/isad:adminbiohistory |
            isad:content_structure/isad:arrangement |
            isad:content_structure/isad:appraisaldestruction |
            isad:conditions_access_use/isad:findingaids |
            isad:notes/isad:note
        )">
            <dc:description><xsl:value-of select="." /></dc:description>
        </xsl:for-each>

        <xsl:for-each select="(
            isad:context/isad:archivalhistory |
            isad:context/isad:acqinfo
        )">
            <dc:provenance><xsl:value-of select="." /></dc:provenance>
        </xsl:for-each>
        <xsl:for-each select="isad:content_structure/isad:scopecontent">
            <dc:subject><xsl:value-of select="." /></dc:subject>
        </xsl:for-each>

        <xsl:for-each select="isad:content_structure/isad:accruals">
            <dc:accrualPolicy><xsl:value-of select="." /></dc:accrualPolicy>
        </xsl:for-each>

        <xsl:for-each select="(
            isad:conditions_access_use/isad:accesrestrictions |
            isad:conditions_access_use/isad:reprorestrictions
        )">
            <dc:rights><xsl:value-of select="." /></dc:rights>
        </xsl:for-each>

        <xsl:for-each select="isad:conditions_access_use/isad:languagescripts">
            <dc:language><xsl:value-of select="." /></dc:language>
        </xsl:for-each>

        <xsl:for-each select="(
            isad:allied_materials/isad:existencelocation_originals |
            isad:allied_materials/isad:existencelocation_copies |
            isad:allied_materials/isad:relatedunits |
            isad:allied_materials/isad:publication
        )">
            <dc:relation><xsl:value-of select="." /></dc:relation>
        </xsl:for-each>

    </xsl:template>
</xsl:stylesheet>
