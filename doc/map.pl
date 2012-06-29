#!/usr/bin/env perl
use Catmandu::Sane;
use Catmandu::Util qw(:is);
use XML::XPath;
use XML::XPath::Node;
use XML::XPath::XMLParser;
use Data::Dumper;
use List::MoreUtils qw(distinct);

sub to_path {
    my $node = shift;
    my @paths = ($node->getLocalName());
    my $parent = $node->getParentNode();

    while($parent){
        next if $parent->getNodeType != XML::XPath::Node::ELEMENT_NODE();
        unshift @paths,$parent->getLocalName() if defined($parent->getLocalName());
        $parent = $parent->getParentNode();
    }
    return "/".join('/',@paths);    
}

# cat map_ead_dc | ./map.pl ead.xml 

my $file = shift;
my $xpath = XML::XPath->new(filename => $file);

my $header = <<EOF;
<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="marc" xmlns:bxf="http://bitflux.org/functions" xmlns:func="http://exslt.org/functions" extension-element-prefixes="func">

    <xsl:output method="text" indent="yes" encoding="UTF-8"/>
EOF
my $footer = <<EOF;
</xsl:stylesheet>
EOF
my $content = <<EOF;
    <xsl:template match="%s">
        <xsl:value-of select="%s" />
    </xsl:template>
EOF

print $header;
while(my $line = <STDIN>){
    chomp($line);
    my($path,$dc) = split(' ',$line);
    my @paths = ();
    my $nodeset = $xpath->find($path);
    if($nodeset){
        for my $node($nodeset->get_nodelist){
            push @paths,to_path($node);
        }
    }

    say sprintf($content,$_,$dc) foreach(distinct(@paths));
}
print $footer;
