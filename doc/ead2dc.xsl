<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ead="urn:isbn:1-931666-22-9">
  <xsl:output encoding="UTF-8" indent="yes" method="xml"/>
  <xsl:template match="/">
    <oai_dc:dc xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/">
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:abstract">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:descgrp/ead:acqinfo">
        <dc:provenance>
          <xsl:value-of select="."/>
        </dc:provenance>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt/ead:address">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:repository/ead:address">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt/ead:address/ead:addressline">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:repository/ead:address/ead:addressline">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:descgrp/ead:altformavail">
        <dc:relation>
          <xsl:value-of select="."/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:arrangement">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:arrangement">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:titlestmt/ead:author">
        <dc:contributor>
          <xsl:value-of select="."/>
        </dc:contributor>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:bioghist">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:revisiondesc/ead:change">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:revisiondesc/ead:change">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:revisiondesc/ead:change">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01">
        <dc:relation>
          <xsl:value-of select="."/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:c02">
        <dc:relation>
          <xsl:value-of select="."/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:descgrp/ead:userestrict">
        <dc:rights>
          <xsl:value-of select="."/>
        </dc:rights>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:did/ead:container">
        <dc:format>
          <xsl:value-of select="."/>
        </dc:format>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:controlaccess">
        <dc:subject>
          <xsl:value-of select="."/>
        </dc:subject>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:controlaccess/ead:controlaccess">
        <dc:subject>
          <xsl:value-of select="."/>
        </dc:subject>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:repository/ead:corpname">
        <dc:contributor>
          <xsl:value-of select="."/>
        </dc:contributor>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:controlaccess/ead:controlaccess/ead:corpname">
        <dc:contributor>
          <xsl:value-of select="."/>
        </dc:contributor>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:profiledesc/ead:creation">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt/ead:date">
        <dc:date>
          <xsl:value-of select="."/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:profiledesc/ead:creation/ead:date">
        <dc:date>
          <xsl:value-of select="."/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:revisiondesc/ead:change/ead:date">
        <dc:date>
          <xsl:value-of select="."/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:unittitle/ead:unitdate">
        <dc:date>
          <xsl:value-of select="."/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:did/ead:unittitle/ead:unitdate">
        <dc:date>
          <xsl:value-of select="."/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:c02/ead:did/ead:unittitle/ead:unitdate">
        <dc:date>
          <xsl:value-of select="."/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:unittitle/ead:unitdate">
        <dc:date>
          <xsl:value-of select="."/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:did/ead:unittitle/ead:unitdate">
        <dc:date>
          <xsl:value-of select="."/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:c02/ead:did/ead:unittitle/ead:unitdate">
        <dc:date>
          <xsl:value-of select="."/>
        </dc:date>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:descgrp">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:did">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:c02/ead:did">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader">
        <dc:identifier>
          <xsl:value-of select="."/>
        </dc:identifier>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:eadid">
        <dc:identifier>
          <xsl:value-of select="."/>
        </dc:identifier>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead">
        <dc:identifier>
          <xsl:value-of select="."/>
        </dc:identifier>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:notestmt/ead:note/ead:p/ead:extref">
        <dc:relation>
          <xsl:value-of select="."/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:note/ead:p/ead:extref">
        <dc:relation>
          <xsl:value-of select="."/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:descgrp/ead:userestrict/ead:p/ead:extref">
        <dc:relation>
          <xsl:value-of select="."/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:descgrp/ead:altformavail/ead:p/ead:extref">
        <dc:relation>
          <xsl:value-of select="."/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:otherfindaid/ead:extref">
        <dc:relation>
          <xsl:value-of select="."/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:physdesc/ead:extent">
        <dc:format>
          <xsl:value-of select="."/>
        </dc:format>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:controlaccess/ead:controlaccess/ead:genreform">
        <dc:type>
          <xsl:value-of select="."/>
        </dc:type>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:c02/ead:did/ead:unittitle/ead:geogname">
        <dc:subject>
          <xsl:value-of select="."/>
        </dc:subject>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:c02/ead:did/ead:unittitle/ead:geogname">
        <dc:subject>
          <xsl:value-of select="."/>
        </dc:subject>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:unitid">
        <dc:identifier>
          <xsl:value-of select="."/>
        </dc:identifier>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:did/ead:unitid">
        <dc:identifier>
          <xsl:value-of select="."/>
        </dc:identifier>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:unitid">
        <dc:identifier>
          <xsl:value-of select="."/>
        </dc:identifier>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:did/ead:unitid">
        <dc:identifier>
          <xsl:value-of select="."/>
        </dc:identifier>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:index">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:index/ead:indexentry">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:index/ead:indexentry">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:index/ead:indexentry">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:langmaterial/ead:language">
        <dc:language>
          <xsl:value-of select="."/>
        </dc:language>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:langmaterial">
        <dc:language>
          <xsl:value-of select="."/>
        </dc:language>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:index/ead:indexentry/ead:name">
        <dc:title>
          <xsl:value-of select="."/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:notestmt/ead:note">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:note">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:controlaccess/ead:note">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:did/ead:note">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:c02/ead:did/ead:note">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:notestmt">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:notestmt">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:otherfindaid">
        <dc:relation>
          <xsl:value-of select="."/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:otherfindaid">
        <dc:relation>
          <xsl:value-of select="."/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:controlaccess/ead:controlaccess/ead:persname">
        <dc:subject>
          <xsl:value-of select="."/>
        </dc:subject>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:physdesc">
        <dc:format>
          <xsl:value-of select="."/>
        </dc:format>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:c02/ead:did/ead:physdesc">
        <dc:format>
          <xsl:value-of select="."/>
        </dc:format>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:descgrp/ead:prefercite">
        <dc:identifier>
          <xsl:value-of select="."/>
        </dc:identifier>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:profiledesc">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt">
        <dc:publisher>
          <xsl:value-of select="."/>
        </dc:publisher>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt/ead:publisher">
        <dc:publisher>
          <xsl:value-of select="."/>
        </dc:publisher>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt/ead:publisher">
        <dc:publisher>
          <xsl:value-of select="."/>
        </dc:publisher>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt/ead:publisher">
        <dc:publisher>
          <xsl:value-of select="."/>
        </dc:publisher>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:repository">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:repository">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:revisiondesc">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:revisiondesc">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:scopecontent">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:scopecontent">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:scopecontent">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:scopecontent">
        <dc:description>
          <xsl:value-of select="."/>
        </dc:description>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:controlaccess/ead:controlaccess/ead:subject">
        <dc:subject>
          <xsl:value-of select="."/>
        </dc:subject>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:repository/ead:corpname/ead:subarea">
        <dc:relation>
          <xsl:value-of select="."/>
        </dc:relation>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:unittitle">
        <dc:title>
          <xsl:value-of select="."/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:did/ead:unittitle">
        <dc:title>
          <xsl:value-of select="."/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:c02/ead:did/ead:unittitle">
        <dc:title>
          <xsl:value-of select="."/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:unittitle">
        <dc:title>
          <xsl:value-of select="."/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:did/ead:unittitle">
        <dc:title>
          <xsl:value-of select="."/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc/ead:c01/ead:c02/ead:did/ead:unittitle">
        <dc:title>
          <xsl:value-of select="."/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:titlestmt/ead:titleproper">
        <dc:title>
          <xsl:value-of select="."/>
        </dc:title>
      </xsl:for-each>
      <xsl:for-each select="/ead:ead/ead:eadheader/ead:filedesc/ead:titlestmt/ead:titleproper">
        <dc:title>
          <xsl:value-of select="."/>
        </dc:title>
      </xsl:for-each>
    </oai_dc:dc>
  </xsl:template>
</xsl:stylesheet>
