#!/usr/bin/env perl
use Catmandu::Sane;
use Catmandu::Util qw(:is);
use XML::XPath;
use XML::XPath::Node;
use XML::XPath::XMLParser;
use Data::Dumper;
use List::MoreUtils qw(distinct);

sub usage {
    say STDERR "usage: $0 <file> <paths>";
    exit;
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
            say $node->toString();
        }
    }
}
