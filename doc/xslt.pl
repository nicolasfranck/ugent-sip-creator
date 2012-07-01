#!/usr/bin/env perl
use Catmandu::Sane;
use XML::LibXSLT;
use XML::LibXML;

my $xslt = XML::LibXSLT->new;
my $st = $xslt->parse_stylesheet(
    XML::LibXML->load_xml(location => shift)
);
my $result = $st->transform(
    XML::LibXML->load_xml(location => shift)
);
print $st->output_as_chars($result);
