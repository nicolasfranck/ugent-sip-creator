#!/usr/bin/env perl
use Catmandu::Sane;
use Catmandu::Util qw(:is);
use XML::XPath;
use XML::XPath::Node;
use XML::XPath::XMLParser;
use Data::Dumper;
use List::MoreUtils qw(distinct);

sub usage {
    say STDERR "usage: $0 <file> <tag>";
    exit;
}
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

my $file = shift;
is_string($file) && -f $file || usage();
my @elements = @ARGV;
scalar(@elements) || usage();

my $xpath = XML::XPath->new(filename => $file);
foreach my $element(@elements){
    my $nodeset = $xpath->find($element);
    my @paths = ();
    if($nodeset){
        for my $node($nodeset->get_nodelist){
            push @paths,to_path($node);
        }
    }
    say "$element $_" foreach(distinct(@paths));
}
