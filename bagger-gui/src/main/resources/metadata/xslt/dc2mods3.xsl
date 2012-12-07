<?xml version="1.0" encoding="UTF-8"?>
<!--
    Author: Nicolas Franck
    University Library of Ghent
    7 december 2012

    Mapping from http://www.openarchives.org/OAI/2.0/oai_dc/ or http://purl.org/dc/elements/1.1/
    to MODS version 3.3

    For more information, see mapping available at http://www.loc.gov/standards/mods/dcsimple-mods.html
-->
<xsl:stylesheet 
    version="1.0" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xmlns:dc="http://purl.org/dc/elements/1.1/" 
    xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:mods="http://www.loc.gov/mods/v3"
    exclude-result-prefixes="dc oai_dc xsi mods"
>   
    <xsl:output method="xml" indent="yes" encoding="UTF-8" />
    <xsl:strip-space elements="*"/>

    <xsl:variable name="dcTypeList" select="'Collection InteractiveResource PhysicalObject Service StillImage Software Text Sound Image Dataset MovingImage'" />

    <xsl:template name="mappingDCType2ModsTypeOfResource">
        <xsl:variable name="type" />
        <xsl:choose>
            <xsl:when test="$type = 'Collection'">
                <mods:typeOfResource collection="yes" />
            </xsl:when>
            <xsl:when test="$type = 'Dataset'">
                <mods:typeOfResource>software, multimedia</mods:typeOfResource>
                <mods:genre>database</mods:genre>
            </xsl:when>
            <xsl:when test="$type = 'Image'">
                <mods:typeOfResource>still image</mods:typeOfResource>
            </xsl:when>
            <xsl:when test="$type = 'InteractiveResource'">
                <mods:typeOfResource>software, multimedia</mods:typeOfResource>
            </xsl:when>
            <xsl:when test="$type = 'MovingImage'">
                <mods:typeOfResource>moving image</mods:typeOfResource>
            </xsl:when>
            <xsl:when test="$type = 'PhysicalObject'">
                <mods:typeOfResource>three dimensional object</mods:typeOfResource>
            </xsl:when>
            <xsl:when test="$type = 'Service'">
                <mods:typeOfResource>software, multimedia</mods:typeOfResource>
                <mods:genre>online system of service</mods:genre>
            </xsl:when>
            <xsl:when test="$type = 'Sound'">
                <mods:typeOfResource>sound recording</mods:typeOfResource>
            </xsl:when>
            <xsl:when test="$type = 'StillImage'">
                <mods:typeOfResource>still image</mods:typeOfResource>
            </xsl:when>
            <xsl:when test="$type = 'Software'">
                <mods:typeOfResource>software, multimedia</mods:typeOfResource>
            </xsl:when>
            <xsl:when test="$type = 'Text'">
                <mods:typeOfResource>text</mods:typeOfResource>
            </xsl:when>
            <xsl:otherwise>
                <mods:typeOfResource>text</mods:typeOfResource>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="/">
        <xsl:for-each select="dc:dc|oai_dc:dc">
            <xsl:apply-templates select="." />
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="dc:dc|oai_dc:dc">
        <mods:mods version="3.3">
            <xsl:attribute name="ID" >
                <xsl:value-of select="generate-id()" />
            </xsl:attribute>
            <xsl:for-each select="dc:title">
                <mods:titleInfo>
                    <mods:title><xsl:value-of select="."/></mods:title>
                </mods:titleInfo>
            </xsl:for-each>
            <xsl:for-each select="dc:creator">
                <mods:name>
                    <mods:namePart><xsl:value-of select="." /></mods:namePart>
                    <mods:role>
                        <mods:roleTerm type="text">creator</mods:roleTerm>
                    </mods:role>
                </mods:name>
            </xsl:for-each>
            <xsl:for-each select="dc:subject">
                <mods:subject>
                    <mods:topic><xsl:value-of select="." /></mods:topic>
                </mods:subject>
            </xsl:for-each>
            <xsl:for-each select="dc:description">
                <mods:note><xsl:value-of select="." /></mods:note>      
            </xsl:for-each>
            <xsl:for-each select="dc:publisher">
                <mods:originInfo>
                    <mods:publisher><xsl:value-of select="." /></mods:publisher>
                </mods:originInfo>
            </xsl:for-each>
            <xsl:for-each select="dc:contributor">
                <mods:name>
                    <mods:namePart><xsl:value-of select="." /></mods:namePart>
                </mods:name>
                <mods:role>
                    <mods:roleTerm type="text">contributor</mods:roleTerm>
                </mods:role>
            </xsl:for-each>
            <xsl:for-each select="dc:date">
                <mods:originInfo>
                    <mods:dateOther><xsl:value-of select="." /></mods:dateOther>
                </mods:originInfo>
            </xsl:for-each>
            <xsl:for-each select="dc:type">
                <xsl:variable name="type" select="." />            
                <xsl:choose>
                    <xsl:when test="contains($dcTypeList,$type)">
                        <mods:genre authority="dct"><xsl:value-of select="$type" /></mods:genre>
                        <xsl:call-template name="mappingDCType2ModsTypeOfResource">
                            <xsl:with-param name="type">
                                <xsl:value-of select="$type" />
                            </xsl:with-param>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <mods:genre><xsl:value-of select="$type" /></mods:genre>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
            <xsl:for-each select="dc:format">
                <mods:physicalDescription>
                    <mods:form><xsl:value-of select="." /></mods:form>
                </mods:physicalDescription>
            </xsl:for-each>
            <xsl:for-each select="dc:identifier">
                <xsl:variable name="identifier" select="." />
                <xsl:choose>
                    <xsl:when test="starts-with($identifier,'http://')">
                        <mods:location>
                            <mods:url><xsl:value-of select="$identifier" /></mods:url>
                        </mods:location>
                    </xsl:when>
                    <xsl:otherwise>
                        <mods:identifier><xsl:value-of select="$identifier" /></mods:identifier>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
            <xsl:for-each select="dc:source">
                <xsl:variable name="source" select="." />
                <mods:relatedItem type="original">
                    <xsl:choose>
                        <xsl:when test="starts-with($source,'http://')">
                            <mods:location>
                                <mods:url><xsl:value-of select="$source" /></mods:url>
                            </mods:location>
                        </xsl:when>
                        <xsl:otherwise>
                            <mods:titleInfo>
                                <mods:title><xsl:value-of select="$source" /></mods:title>
                            </mods:titleInfo>
                        </xsl:otherwise>
                    </xsl:choose>
                </mods:relatedItem>
            </xsl:for-each>
            <xsl:for-each select="dc:language">
                <xsl:variable name="language" select="." />
                <mods:language>
                <xsl:choose>    
                    <xsl:when test="string-length($language) &lt;= 3">
                        <mods:languageTerm type="code"><xsl:value-of select="$language" /></mods:languageTerm>
                    </xsl:when>
                    <xsl:otherwise>
                        <mods:languageTerm type="text"><xsl:value-of select="$language" /></mods:languageTerm>
                    </xsl:otherwise>
                </xsl:choose>
                </mods:language>
            </xsl:for-each>
            <xsl:for-each select="dc:relation">
                <xsl:variable name="relation" select="." />
                <mods:relatedItem>
                    <xsl:choose>
                        <xsl:when test="starts-with($relation,'http://')">
                            <mods:location>
                                <mods:url><xsl:value-of select="$relation" /></mods:url>
                            </mods:location>
                        </xsl:when>
                        <xsl:otherwise>
                            <mods:titleInfo>
                                <mods:title><xsl:value-of select="$relation" /></mods:title>
                            </mods:titleInfo>
                        </xsl:otherwise>
                    </xsl:choose>
                </mods:relatedItem>
            </xsl:for-each>
            <xsl:for-each select="dc:coverage">
                <mods:subject>
                    <mods:geographic><xsl:value-of select="." /></mods:geographic>
                </mods:subject>
            </xsl:for-each>
            <xsl:for-each select="dc:rights">
                <mods:accessCondition><xsl:value-of select="." /></mods:accessCondition>
            </xsl:for-each>
        </mods:mods>
    </xsl:template>
</xsl:stylesheet>
