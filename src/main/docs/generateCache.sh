#!/usr/bin/perl

#
# Simple script to make a lot of requests to a specified URL to force the generation of a cache for all
# the normalised and diplomatic transcriptions specified in the JSON data. 
#

use LWP::Simple;
use URI::Escape;

if (!($ARGV[0] =~ m/http.*/)) {
                  print "first parameter should be a JSON URL to cache external resources e.g.http://cudl-dev.lib.cam.ac.uk/json/MS-ADD-03958.json \n";
                  exit 0;
}

if (!($ARGV[1] =~ m/http.*/)) {
                  print "second parameter should be the base URL for the site you want the cache generated on, e.g. http://cudl-dev.lib.cam.ac.uk \n";
                  exit 0;
}

  $jsonURL = $ARGV[0];
  #$jsonURL ="http://localhost:1111/json/MS-ADD-03958.json";

  $cacheBaseURL = $ARGV[1];
  # $cacheBaseURL = "http://localhost:1111";

# Get the docID from the JSON url. 
  $docId="";
  if ($jsonURL=~ m/http.*\/(.*)\.json$/) {
    $docId = $1;
  } 


# Get external resources from json page content. 
  #print "docId : $docId";

  $jsonhtml = get($jsonURL);

  foreach $line (split("\n", $jsonhtml)) {

      # cache normaised/diplomatic  transcriptions
      if ($line =~ m/transcriptionNormalisedURL\":\"(.*)\"/ || 
          $line =~ m/transcriptionDiplomaticURL\":\"(.*)\"/) {

        $url = $1;	
        $urlParam = "/transcription?url=".uri_escape($url);
        $html = get($cacheBaseURL."/externalresource?url=".uri_escape($urlParam)."&doc=$docId");

        print "request: ".$cacheBaseURL."/externalresource?url=".uri_escape($urlParam)."&doc=$docId\n";

        unless (length($html)) {
	   warn "FAILED: Unable to load page for '$url'\n";
        }
      }

   }

exit 0;
