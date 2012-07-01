<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    version="2.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"     
>
<xsl:output method="text" indent="yes" encoding="UTF-8"/>
<xsl:template match="/">
<oai_dc:dc xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/" xmlns:dc="http://purl.org/dc/elements/1.1/">
<xsl:for-each select="/ead/archdesc/did/abstract">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/descgrp/acqinfo">
    <dc:provenance>
        <xsl:value-of select="." />
    </dc:provenance>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/filedesc/publicationstmt/address">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/repository/address">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/filedesc/publicationstmt/address/addressline">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/repository/address/addressline">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/descgrp/altformavail">
    <dc:relation>
        <xsl:value-of select="." />
    </dc:relation>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/arrangement">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/arrangement">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/filedesc/titlestmt/author">
    <dc:contributor>
        <xsl:value-of select="." />
    </dc:contributor>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/bioghist">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/revisiondesc/change">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/revisiondesc/change">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/revisiondesc/change">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01">
    <dc:relation>
        <xsl:value-of select="." />
    </dc:relation>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/c02">
    <dc:relation>
        <xsl:value-of select="." />
    </dc:relation>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/descgrp/userestrict">
    <dc:rights>
        <xsl:value-of select="." />
    </dc:rights>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/did/container">
    <dc:format>
        <xsl:value-of select="." />
    </dc:format>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/controlaccess">
    <dc:subject>
        <xsl:value-of select="." />
    </dc:subject>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/controlaccess/controlaccess">
    <dc:subject>
        <xsl:value-of select="." />
    </dc:subject>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/repository/corpname">
    <dc:contributor>
        <xsl:value-of select="." />
    </dc:contributor>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/controlaccess/controlaccess/corpname">
    <dc:contributor>
        <xsl:value-of select="." />
    </dc:contributor>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/profiledesc/creation">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/filedesc/publicationstmt/date">
    <dc:date>
        <xsl:value-of select="." />
    </dc:date>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/profiledesc/creation/date">
    <dc:date>
        <xsl:value-of select="." />
    </dc:date>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/revisiondesc/change/date">
    <dc:date>
        <xsl:value-of select="." />
    </dc:date>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/unittitle/unitdate">
    <dc:date>
        <xsl:value-of select="." />
    </dc:date>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/did/unittitle/unitdate">
    <dc:date>
        <xsl:value-of select="." />
    </dc:date>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/c02/did/unittitle/unitdate">
    <dc:date>
        <xsl:value-of select="." />
    </dc:date>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/unittitle/unitdate">
    <dc:date>
        <xsl:value-of select="." />
    </dc:date>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/did/unittitle/unitdate">
    <dc:date>
        <xsl:value-of select="." />
    </dc:date>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/c02/did/unittitle/unitdate">
    <dc:date>
        <xsl:value-of select="." />
    </dc:date>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/descgrp">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/did">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/c02/did">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader">
    <dc:identifier>
        <xsl:value-of select="." />
    </dc:identifier>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/eadid">
    <dc:identifier>
        <xsl:value-of select="." />
    </dc:identifier>
</xsl:for-each>

<xsl:for-each select="/ead">
    <dc:identifier>
        <xsl:value-of select="." />
    </dc:identifier>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/filedesc/notestmt/note/p/extref">
    <dc:relation>
        <xsl:value-of select="." />
    </dc:relation>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/note/p/extref">
    <dc:relation>
        <xsl:value-of select="." />
    </dc:relation>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/descgrp/userestrict/p/extref">
    <dc:relation>
        <xsl:value-of select="." />
    </dc:relation>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/descgrp/altformavail/p/extref">
    <dc:relation>
        <xsl:value-of select="." />
    </dc:relation>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/otherfindaid/extref">
    <dc:relation>
        <xsl:value-of select="." />
    </dc:relation>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/physdesc/extent">
    <dc:format>
        <xsl:value-of select="." />
    </dc:format>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/controlaccess/controlaccess/genreform">
    <dc:type>
        <xsl:value-of select="." />
    </dc:type>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/c02/did/unittitle/geogname">
    <dc:subject>
        <xsl:value-of select="." />
    </dc:subject>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/c02/did/unittitle/geogname">
    <dc:subject>
        <xsl:value-of select="." />
    </dc:subject>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/unitid">
    <dc:identifier>
        <xsl:value-of select="." />
    </dc:identifier>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/did/unitid">
    <dc:identifier>
        <xsl:value-of select="." />
    </dc:identifier>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/unitid">
    <dc:identifier>
        <xsl:value-of select="." />
    </dc:identifier>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/did/unitid">
    <dc:identifier>
        <xsl:value-of select="." />
    </dc:identifier>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/index">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/index/indexentry">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/index/indexentry">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/index/indexentry">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/langmaterial/language">
    <dc:language>
        <xsl:value-of select="." />
    </dc:language>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/langmaterial">
    <dc:language>
        <xsl:value-of select="." />
    </dc:language>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/index/indexentry/name">
    <dc:title>
        <xsl:value-of select="." />
    </dc:title>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/filedesc/notestmt/note">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/note">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/controlaccess/note">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/did/note">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/c02/did/note">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/filedesc/notestmt">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/filedesc/notestmt">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/otherfindaid">
    <dc:relation>
        <xsl:value-of select="." />
    </dc:relation>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/otherfindaid">
    <dc:relation>
        <xsl:value-of select="." />
    </dc:relation>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/controlaccess/controlaccess/persname">
    <dc:subject>
        <xsl:value-of select="." />
    </dc:subject>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/physdesc">
    <dc:format>
        <xsl:value-of select="." />
    </dc:format>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/c02/did/physdesc">
    <dc:format>
        <xsl:value-of select="." />
    </dc:format>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/descgrp/prefercite">
    <dc:identifier>
        <xsl:value-of select="." />
    </dc:identifier>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/profiledesc">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/filedesc/publicationstmt">
    <dc:publisher>
        <xsl:value-of select="." />
    </dc:publisher>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/filedesc/publicationstmt/publisher">
    <dc:publisher>
        <xsl:value-of select="." />
    </dc:publisher>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/filedesc/publicationstmt/publisher">
    <dc:publisher>
        <xsl:value-of select="." />
    </dc:publisher>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/filedesc/publicationstmt/publisher">
    <dc:publisher>
        <xsl:value-of select="." />
    </dc:publisher>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/repository">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/repository">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/revisiondesc">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/revisiondesc">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/scopecontent">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/scopecontent">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/scopecontent">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/scopecontent">
    <dc:description>
        <xsl:value-of select="." />
    </dc:description>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/controlaccess/controlaccess/subject">
    <dc:subject>
        <xsl:value-of select="." />
    </dc:subject>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/repository/corpname/subarea">
    <dc:relation>
        <xsl:value-of select="." />
    </dc:relation>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/unittitle">
    <dc:title>
        <xsl:value-of select="." />
    </dc:title>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/did/unittitle">
    <dc:title>
        <xsl:value-of select="." />
    </dc:title>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/c02/did/unittitle">
    <dc:title>
        <xsl:value-of select="." />
    </dc:title>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/did/unittitle">
    <dc:title>
        <xsl:value-of select="." />
    </dc:title>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/did/unittitle">
    <dc:title>
        <xsl:value-of select="." />
    </dc:title>
</xsl:for-each>

<xsl:for-each select="/ead/archdesc/dsc/c01/c02/did/unittitle">
    <dc:title>
        <xsl:value-of select="." />
    </dc:title>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/filedesc/titlestmt/titleproper">
    <dc:title>
        <xsl:value-of select="." />
    </dc:title>
</xsl:for-each>

<xsl:for-each select="/ead/eadheader/filedesc/titlestmt/titleproper">
    <dc:title>
        <xsl:value-of select="." />
    </dc:title>
</xsl:for-each>

</oai_dc:dc>
</xsl:template>
</xsl:stylesheet>
