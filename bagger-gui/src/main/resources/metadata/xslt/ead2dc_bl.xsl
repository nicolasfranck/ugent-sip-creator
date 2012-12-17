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
    xmlns:dcterms="http://purl.org/dc/terms/"
    xmlns:dcmitype="http://purl.org/dc/dcmitype/"
    xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ead="urn:isbn:1-931666-22-9"
    exclude-result-prefixes="#all"
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
            <!-- bert: ead:abstract kan specifieker gemapt worden naar dcterms:abstract -->
            <xsl:for-each select="ead:archdesc/ead:did/ead:abstract">
                <dcterms:abstract><xsl:value-of select="." /></dcterms:abstract>
            </xsl:for-each>
            <xsl:for-each select="ead:archdesc/ead:scopecontent">
                <dc:description><xsl:value-of select="." /></dc:description>
            </xsl:for-each>
            <!-- bert: Als je <imprint> gebruikt, zou ik als best practice aannemen dat je <title><publisher><geoname><date> als child elementen gebruikt. De mapping van unittitle/imprint/publisher vereist dan een bijkomende mapping van /unittitle/title naar dc:title -->

            <!-- Nicolas: klopt dit nu? Over dat laatste was ik niet wat je ermee bedoelde-->
            <xsl:for-each select="(
                ead:archdesc/ead:did/ead:unittitle/ead:imprint/ead:geogname |
                ead:archdesc/ead:did/ead:unittitle/ead:imprint/ead:date |
                ead:archdesc/ead:did/ead:unittitle/ead:imprint/ead:publisher |
                ead:archdesc/ead:did/ead:unittitle/ead:imprint/ead:title
            )">
                <dc:publisher><xsl:value-of select="." /></dc:publisher>
            </xsl:for-each>
            <xsl:for-each select="ead:archdesc/ead:did/ead:unittitle/ead:imprint/ead:publisher">
                <dc:title><xsl:value-of select="." /></dc:title>
            </xsl:for-each>
            <xsl:for-each select="ead:archdesc/ead:did/ead:unitdate">
                <dc:date><xsl:value-of select="." /></dc:date>
            </xsl:for-each>
            <!-- bert: aangezien je een grote groep archiefdocumenten, publicaties of objecten beschrijft zou ik hier de defqult waarde "collection" gebruiken. <genreform> is doorgaans vrije tekst en mapt beter op dcterms:medium.-->
            <!--
                Nicolas: geldt dat voor vele (kleine) archiefinstellingen? Of enkel voor AMVB? Mijns inziens is AMVB toch geen kleine instelling?
            -->
            <dc:type>Collection</dc:type>
            <xsl:for-each select="ead:archdesc/ead:did/ead:physdesc/ead:genreform">
                <dcterms:medium><xsl:value-of select="." /></dcterms:medium>
            </xsl:for-each>
            
            <!-- bert: dc:format opsplitsen in dcterms:medium (genreform & physfacet) en dcterms:extent (extent & dimensions) -->
            <xsl:for-each select="(
                ead:archdesc/ead:did/ead:physdesc/ead:genreform |
                ead:archdesc/ead:did/ead:physdesc/ead:physfacet
            )">
                <dcterms:medium><xsl:value-of select="." /></dcterms:medium>
            </xsl:for-each>
            <xsl:for-each select="(
                ead:archdesc/ead:did/ead:physdesc/ead:extent | 
                ead:archdesc/ead:did/ead:physdesc/ead:dimensions
            )">
                <dcterms:extent><xsl:value-of select="." /></dcterms:extent>
            </xsl:for-each>

            <!-- bert ead/eadid is het identificatienummer van de inventaris. Dat wordt beter gemapt naar dcterms:isReferencedBy. -->
            <xsl:for-each select="ead:eadheader/ead:eadid">
                <dcterms:isReferencedBy><xsl:value-of select="." /></dcterms:isReferencedBy>
            </xsl:for-each>
            <xsl:for-each select="ead:archdesc/ead:did/ead:unitid">
                <dc:identifier><xsl:value-of select="." /></dc:identifier>
            </xsl:for-each>
            <xsl:for-each select="ead:archdesc/ead:did/ead:langmaterial/ead:language">
                <dc:language><xsl:value-of select="." /></dc:language>
            </xsl:for-each>
            <!-- bert: dc:relation uitsplitsen als:
            dcterms:hasFormat >>> ead:altformavailable
            dcterms:hasPart  >>> ead:relatedmaterial ead:separatedmaterial
            dcterms:isReferencedBy   >>> ead:eadid@identifier en alles onder ead:filedesc
            -->
            <xsl:for-each select="//ead:altformavail">
                <dcterms:hasFormat><xsl:value-of select="." /></dcterms:hasFormat>
            </xsl:for-each>
            <xsl:for-each select="(//ead:relatedmaterial | //ead:separatedmaterial)">
                <dcterms:hasPart><xsl:value-of select="." /></dcterms:hasPart>
            </xsl:for-each>
            <!-- Nicolas: alle text onder 'filedesc' wordt in apart veld gestopt? Is gewoon veiliger om vreemde concatenaties tegen te gaan-->
            <xsl:for-each select="(
                ead:eadheader/ead:eadid/@identifier |
                ead:eadheader/ead:filedesc//text()
            )">
                <dcterms:isReferencedBy><xsl:value-of select="." /></dcterms:isReferencedBy>
            </xsl:for-each>
            <!-- dc:coverage uitsplitsen als:
            dcterms:spatial >>> ead:controlaccess/geogname
            dcterms:temporal  >>>  ead:unitdate
            -->
            <xsl:for-each select="//ead:unitdate">
                <dcterms:temporal><xsl:value-of select="." /></dcterms:temporal>
            </xsl:for-each>
            <xsl:for-each select="//ead:controlaccess/ead:geogname">
                <dcterms:spatial><xsl:value-of select="." /></dcterms:spatial>
            </xsl:for-each>
            <!-- bert: beperk de mapping tot de aanduiding van de rechtenstatus van het archief.  dcterms:accessRights >>> accessrestrict/legalstatus
            -->
                
            <xsl:for-each select="ead:archdesc/ead:accessrestrict/ead:legalstatus">
                <dcterms:accessRights><xsl:value-of select="." /></dcterms:accessRights>
            </xsl:for-each>
            <!-- 
            dcterms:rightsHolder  >>> ead/archdesc/userestrict/userestrict
            dcterms:provenance >>>  ead/archdesc/acqinfo/acqinfo
            -->
            <xsl:for-each select="ead:archdesc/ead:userestrict/ead:userestrict">
                <dcterms:rightsHolder><xsl:value-of select="." /></dcterms:rightsHolder>
            </xsl:for-each>
            <xsl:for-each select="ead:archdesc/ead:acqinfo/ead:acqinfo">
                <dcterms:provenance><xsl:value-of select="." /></dcterms:provenance>
            </xsl:for-each>
           
        </dc:dc>
    </xsl:template>
</xsl:stylesheet>
