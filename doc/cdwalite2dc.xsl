<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:default="http://www.getty.edu/CDWA/CDWALite"
>
  <xsl:output encoding="UTF-8" indent="yes" method="xml"/>
  <xsl:template match="/">
    <oai_dc:dc 
        xmlns:dc="http://purl.org/dc/elements/1.1/" 
        xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
        xmlns:dcterms="http://purl.org/dc/terms/"
    >
      <xsl:for-each select="/default:cdwalite/default:administrativeMetadata/default:recordWrap/default:recordType">
        <dc:type>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:type>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:descriptiveMetadata/default:objectWorkTypeWrap/default:objectWorkType">
        <dc:type>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:type>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:descriptiveMetadata/default:classWrap/default:classification">
        <dc:subject>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:subject>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:descriptiveMetadata/default:titleWrap/default:titleSet/default:title">
        <dc:title>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:descriptiveMetadata/default:displayCreator">
        <dc:creator>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:creator>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:descriptiveMetadata/default:displayCreationDate">
        <dcterms:created>
          <xsl:value-of select="normalize-space(.)"/>
        </dcterms:created>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:descriptiveMetadata/default:styleWrap/default:style">
        <dcterms:temporal>
          <xsl:value-of select="normalize-space(.)"/>
        </dcterms:temporal>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:descriptiveMetadata/default:displayMeasurements">
        <dcterms:extent>
          <xsl:value-of select="normalize-space(.)"/>
        </dcterms:extent>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:descriptiveMetadata/default:indexingMeasurementsWrap/default:indexingMeasurementsSet/default:scaleMeasurements">
        <dcterms:extent>
          <xsl:value-of select="normalize-space(.)"/>
        </dcterms:extent>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:descriptiveMetadata/default:inscriptionsWrap/default:inscriptions">
        <dcterms:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dcterms:description>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:descriptiveMetadata/default:indexingSubjectWrap/default:indexingSubjectSet/default:subjectTerm">
        <dc:subject>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:subject>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:descriptiveMetadata/default:descriptiveNoteWrap/default:descriptiveNoteSet/default:descriptiveNote">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:descriptiveMetadata/default:relatedWorksWrap/default:relatedWorkSet/default:labelRelatedWork">
        <dc:relation>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:descriptiveMetadata/default:relatedWorksWrap/default:relatedWorkSet/default:locRelatedWork">
        <dc:relation>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:descriptiveMetadata/default:relatedWorksWrap/default:relatedWorkSet/default:relatedWorkRelType">
        <dc:relation>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:descriptiveMetadata/default:locationWrap/default:locationSet/default:workID">
        <dc:identifier>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:identifier>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:administrativeMetadata/default:rightsWork">
        <dc:rights>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:rights>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:administrativeMetadata/default:resourceWrap/default:resourceSet/default:resourceViewType">
        <dc:type>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:type>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:administrativeMetadata/default:resourceWrap/default:resourceSet/default:resourceViewSubjectTerm">
        <dc:subject>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:subject>
      </xsl:for-each>
      <xsl:for-each select="/default:cdwalite/default:administrativeMetadata/default:recordWrap/default:recordSource">
        <dcterms:isReferencedBy>
          <xsl:value-of select="normalize-space(.)"/>
        </dcterms:isReferencedBy>
      </xsl:for-each>
    </oai_dc:dc>
  </xsl:template>
</xsl:stylesheet>
