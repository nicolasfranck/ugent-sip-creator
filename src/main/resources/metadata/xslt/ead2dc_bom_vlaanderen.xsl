<?xml version="1.0" encoding="UTF-8"?>
<!--
    Author: Nicolas Franck
    University Library of Ghent
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
            <!-- ead:name en ead:namegrp horen die er wel bij? -->
            <xsl:for-each select="(
                //ead:unittitle |
                //ead:titleproper |
                //ead:title |
                //ead:subtitle
            )">
                <dc:title><xsl:value-of select="." /></dc:title>
            </xsl:for-each>
            <!-- dc:creator not found in mapping -->
            <xsl:for-each select="(
                //ead:controlaccess//text() |
                //ead:famname | 
                //ead:function |   
                //ead:geogname |
                //ead:subject
            )">
                <dc:subject><xsl:value-of select="." /></dc:subject>
            </xsl:for-each>
            <!-- archdescgrp not found in ead -->

            <!--
                ead:did already in ead:archdesc!
            -->
            <xsl:for-each select="(
                //ead:abbr | 
                //ead:abstract | 
                //ead:address/ead:addressline |
                //ead:appraisal//text() |
                //ead:arc |
                //ead:archdesc//text() |
                //ead:arrangement//text() |
                //ead:bioghist//text() |
                //ead:blockquote//text() |
                //ead:change//text() |
                //ead:descgrp |
                //ead:dsc//text() |
                //ead:dscgrp//text() |
                //ead:descrules//text() |
                //ead:event |
                //ead:eventgrp//text() |
                //ead:index//text() |
                //ead:legalstatus |
                //ead:originalsloc//text() |
                //ead:note//text() |
                //ead:odd//text() |
                //ead:physloc |
                //ead:processinfo//text() |
                //ead:profiledesc//text() |
                //ead:repository//text() |
                //ead:revisiondesc//text() |
                //ead:scopecontent//text() |
                //ead:sponsor
            )">
                <dc:description><xsl:value-of select="." /></dc:description>
            </xsl:for-each>
            <xsl:for-each select="(
                //ead:publicationstmt//text() |
                //ead:publisher
            )">
                <dc:publisher><xsl:value-of select="." /></dc:publisher>
            </xsl:for-each>
            <xsl:for-each select="(
                //ead:date |
                //ead:unitdate
            )">
                <dc:date><xsl:value-of select="." /></dc:date>
            </xsl:for-each>
            <xsl:for-each select="(
                //ead:genreform |
                //ead:fileplan//text()
            )">
                <dc:type><xsl:value-of select="." /></dc:type>
            </xsl:for-each>
            <xsl:for-each select="(
                ead:archdesc/ead:did/ead:physdesc/ead:extent | 
                ead:archdesc/ead:did/ead:physdesc/ead:dimensions | 
                //ead:materialspec |
                //ead:phystech//text() |
                //ead:physdesc |
                ead:archdesc/ead:did/ead:physdesc/ead:physfacet
            )">
                <dc:format><xsl:value-of select="." /></dc:format>
            </xsl:for-each>
            <xsl:for-each select="(
                //ead:dao/@href |
                //ead:daogrp/ead:daodesc//text() |
                //ead:daogrp/ead:daoloc/@entityref |
                ead:eadheader/ead:eadid | 
                ead:archdesc/ead:did/ead:unitid |
                //ead:prefercite//text()
            )">
                <dc:identifier><xsl:value-of select="." /></dc:identifier>
            </xsl:for-each>
            <xsl:for-each select="(
                ead:archdesc/ead:did/ead:langmaterial/ead:language |
                ead:eadheader/ead:profiledesc/ead:langusage/ead:language
            )">
                <dc:language><xsl:value-of select="." /></dc:language>
            </xsl:for-each>
            <xsl:for-each select="(
                //ead:altformavail//text() |
                //ead:archref//text() |
                //ead:bibref |
                //ead:bibseries |
                //ead:bibliography//text() |
                //ead:c//text() |
                //ead:c01//text() |
                //ead:c02//text() |
                //ead:c03//text() |
                //ead:c04//text() |
                //ead:c05//text() |
                //ead:c06//text() |
                //ead:c07//text() |
                //ead:c08//text() |
                //ead:c09//text() |
                //ead:c10//text() |
                //ead:c11//text() |
                //ead:c12//text() |
                ead:eadheader/ead:filedesc/ead:editionstmt//text() |
                //ead:extref |
                //ead:relatedmaterial//text() | 
                //ead:relatedmaterial//ead:extrefloc/@href |
                //ead:otherfindaid/text() |
                ead:eadheader/ead:filedesc/ead:seriesstmt/text() |
                //ead:subarea |
                //ead:separatedmaterial
            )">
                <dc:relation><xsl:value-of select="." /></dc:relation>
            </xsl:for-each>
            <xsl:for-each select="(
                ead:archdesc/ead:accessrestrict | 
                ead:archdesc/ead:userestrict
            )">
                <dc:rights><xsl:value-of select="." /></dc:rights>
            </xsl:for-each>
            <xsl:for-each select="ead:eadheader/ead:filedesc/ead:titlestmt/ead:author">
                <dc:contributor><xsl:value-of select="." /></dc:contributor>
            </xsl:for-each>           
        </dc:dc>
    </xsl:template>
</xsl:stylesheet>
