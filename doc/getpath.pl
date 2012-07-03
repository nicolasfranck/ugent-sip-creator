#!/usr/bin/env perl
use Catmandu::Sane;
use Catmandu::Util qw(:is);
use XML::XPath;
use XML::XPath::Node;
use XML::XPath::XMLParser;
use Data::Dumper;
use List::MoreUtils qw(distinct);

sub usage {
    say STDERR "usage: $0 <xpath-file> <mapping-file>";
    exit;
}
sub to_path {
    my $node = shift;
    my @paths = ($node->getLocalName());
    my $parent = $node->getParentNode();

    while($parent){
        #next if $parent->getNodeType != XML::XPath::Node::ELEMENT_NODE();
        unshift @paths,$parent->getLocalName() if defined($parent->getLocalName());
        $parent = $parent->getParentNode();
    }
    return "/".join('/',@paths);    
}

my $file_xpath = shift;
is_string($file_xpath) && -f $file_xpath || usage();
my $file_mapping = shift;
is_string($file_mapping) && -f $file_mapping || usage();

my $xpath = XML::XPath->new(filename => $file_xpath);
local(*FILE);
open FILE,$file_mapping or die($!);
while(my $line = <FILE>){
    chomp($line);
    my(@elements) = split(/\s+/o,$line);
    my $p = shift(@elements);
    my $nodeset = $xpath->find($p);
    my @paths = ();
    if($nodeset){
        for my $node($nodeset->get_nodelist){
            push @paths,to_path($node);
        }
    }
    say "$_ ".join(' ',@elements) foreach(distinct(@paths));
}
close FILE;
