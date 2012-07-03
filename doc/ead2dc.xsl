<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:default="urn:isbn:1-931666-22-9">
  <xsl:output encoding="UTF-8" indent="yes" method="xml"/>
  <xsl:template match="/">
    <oai_dc:dc xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/">
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:abstract">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:descgrp/default:acqinfo">
        <dc:provenance>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:provenance>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:filedesc/default:publicationstmt/default:address">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:repository/default:address">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:filedesc/default:publicationstmt/default:address/default:addressline">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:repository/default:address/default:addressline">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:descgrp/default:altformavail">
        <dc:relation>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:arrangement">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:arrangement">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:filedesc/default:titlestmt/default:author">
        <dc:contributor>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:contributor>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:bioghist">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:revisiondesc/default:change">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:revisiondesc/default:change">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:revisiondesc/default:change">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01">
        <dc:relation>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:c02">
        <dc:relation>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:descgrp/default:userestrict">
        <dc:rights>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:rights>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:did/default:container">
        <dc:format>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:format>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:controlaccess">
        <dc:subject>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:subject>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:controlaccess/default:controlaccess">
        <dc:subject>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:subject>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:repository/default:corpname">
        <dc:contributor>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:contributor>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:controlaccess/default:controlaccess/default:corpname">
        <dc:contributor>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:contributor>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:profiledesc/default:creation">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:filedesc/default:publicationstmt/default:date">
        <dc:date>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:profiledesc/default:creation/default:date">
        <dc:date>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:revisiondesc/default:change/default:date">
        <dc:date>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:unittitle/default:unitdate">
        <dc:date>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:did/default:unittitle/default:unitdate">
        <dc:date>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:c02/default:did/default:unittitle/default:unitdate">
        <dc:date>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:unittitle/default:unitdate">
        <dc:date>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:did/default:unittitle/default:unitdate">
        <dc:date>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:c02/default:did/default:unittitle/default:unitdate">
        <dc:date>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:descgrp">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:did">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:c02/default:did">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader">
        <dc:identifier>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:identifier>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:eadid">
        <dc:identifier>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:identifier>
      </xsl:for-each>
      <!--
      <xsl:for-each select="/default:ead">
        <dc:identifier>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:identifier>
      </xsl:for-each>
      -->
      <xsl:for-each select="/default:ead/default:eadheader/default:filedesc/default:notestmt/default:note/default:p/default:extref">
        <dc:relation>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:note/default:p/default:extref">
        <dc:relation>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:descgrp/default:userestrict/default:p/default:extref">
        <dc:relation>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:descgrp/default:altformavail/default:p/default:extref">
        <dc:relation>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:otherfindaid/default:extref">
        <dc:relation>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:physdesc/default:extent">
        <dc:format>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:format>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:controlaccess/default:controlaccess/default:genreform">
        <dc:type>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:type>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:c02/default:did/default:unittitle/default:geogname">
        <dc:subject>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:subject>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:c02/default:did/default:unittitle/default:geogname">
        <dc:subject>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:subject>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:unitid">
        <dc:identifier>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:identifier>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:did/default:unitid">
        <dc:identifier>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:identifier>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:unitid">
        <dc:identifier>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:identifier>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:did/default:unitid">
        <dc:identifier>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:identifier>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:index">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:index/default:indexentry">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:index/default:indexentry">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:index/default:indexentry">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:langmaterial/default:language">
        <dc:language>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:language>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:langmaterial">
        <dc:language>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:language>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:index/default:indexentry/default:name">
        <dc:title>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:filedesc/default:notestmt/default:note">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:note">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:controlaccess/default:note">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:did/default:note">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:c02/default:did/default:note">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:filedesc/default:notestmt">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:filedesc/default:notestmt">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:otherfindaid">
        <dc:relation>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:otherfindaid">
        <dc:relation>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:controlaccess/default:controlaccess/default:persname">
        <dc:subject>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:subject>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:physdesc">
        <dc:format>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:format>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:c02/default:did/default:physdesc">
        <dc:format>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:format>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:descgrp/default:prefercite">
        <dc:identifier>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:identifier>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:profiledesc">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:filedesc/default:publicationstmt">
        <dc:publisher>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:publisher>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:filedesc/default:publicationstmt/default:publisher">
        <dc:publisher>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:publisher>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:filedesc/default:publicationstmt/default:publisher">
        <dc:publisher>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:publisher>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:filedesc/default:publicationstmt/default:publisher">
        <dc:publisher>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:publisher>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:repository">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:repository">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:revisiondesc">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:revisiondesc">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:scopecontent">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:scopecontent">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:scopecontent">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:scopecontent">
        <dc:description>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:controlaccess/default:controlaccess/default:subject">
        <dc:subject>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:subject>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:repository/default:corpname/default:subarea">
        <dc:relation>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:unittitle">
        <dc:title>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:did/default:unittitle">
        <dc:title>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:c02/default:did/default:unittitle">
        <dc:title>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:did/default:unittitle">
        <dc:title>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:did/default:unittitle">
        <dc:title>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:archdesc/default:dsc/default:c01/default:c02/default:did/default:unittitle">
        <dc:title>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:filedesc/default:titlestmt/default:titleproper">
        <dc:title>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/default:ead/default:eadheader/default:filedesc/default:titlestmt/default:titleproper">
        <dc:title>
          <xsl:value-of select="normalize-space(.)"/>
        </dc:title>
      </xsl:for-each>
    </oai_dc:dc>
  </xsl:template>
</xsl:stylesheet>
