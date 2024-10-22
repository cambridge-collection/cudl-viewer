# Systems used by CUDL Viewer:

- [CUDL Viewer UI](https://github.com/cambridge-collection/cudl-viewer-ui) - This library is used to compile the JS and CSS used in the viewer.
- [Cudl Services API](https://github.com/cambridge-collection/cudl-services) - This provides access to transcription HTML and TEI metadata.
- [CUDL Solr API](https://github.com/cambridge-collection/cudl-solr) - This provides search functionality
- CUDL IIIF Image Servers - These use [IIPImage](https://iipimage.sourceforge.io/)
- [Content Editor](https://github.com/cambridge-collection/dl-loading-ui) - This provdes a nice GUI for editing the data, adding collections / items.
- Data Processing Pipeline - This is on AWS and controls automatically converting TEI for the viewer/platform.

All of this is controlled through Terraform and available here:
- [Terraform Infrastructure](https://github.com/cambridge-collection/cudl-terraform)

## The Data Structure
![Simple CUDL Dependency Graph Example.svg](Simple%20CUDL%20Dependency%20Graph%20Example.svg)

## The Data Processing
![CDCP Intro data.svg](CDCP%20Intro%20data.svg)

## The CUDL Viewer
![CDCP Intro applications.svg](CDCP%20Intro%20applications.svg)

Full information at https://cambridge-collection.github.io/


