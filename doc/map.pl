#!/usr/bin/env perl
use Catmandu::Sane;
use Catmandu::Util qw(:is);

use XML::XPath;
use XML::XPath::Node;
use XML::XPath::XMLParser;
use Data::Dumper;
use List::MoreUtils qw(distinct);

sub to_path {
    my($node) = @_;
    my @paths = ($node->getName());
    my $parent = $node->getParentNode();

    while($parent){
        next if $parent->getNodeType != XML::XPath::Node::ELEMENT_NODE();        
        unshift @paths,$parent->getName() if defined($parent->getName());
        $parent = $parent->getParentNode();
    }
    return "/".join('/',@paths);    
}

# cat map_ead_dc | ./map.pl ead.xml 

my $file = shift;
my $xpath = XML::XPath->new(filename => $file);

my $header = <<EOF;
<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
>
<xsl:output method="text" indent="yes" encoding="UTF-8"/>
<xsl:template match="/">
<oai_dc:dc xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/" xmlns:dc="http://purl.org/dc/elements/1.1/">
EOF
my $footer = <<EOF;
</oai_dc:dc>
</xsl:template>
</xsl:stylesheet>
EOF
my $content = <<EOF;
<xsl:for-each select="%1\$s">
    <%2\$s>
        <xsl:value-of select="." />
    </%2\$s>
</xsl:for-each>
EOF

printf $header;
while(my $line = <STDIN>){
    chomp($line);
    my($path,$dc) = split(' ',$line);
    my @paths = ();
    my $nodeset = $xpath->find($path);
    if($nodeset){
        for my $node($nodeset->get_nodelist){
            next if $node->getNodeType != XML::XPath::Node::ELEMENT_NODE;
            push @paths,to_path($node);
        }
    }

    say sprintf($content,$_,$dc) foreach(distinct(@paths));
}
print $footer;
