# Systems used by CUDL Viewer:

- [CUDL Viewer UI](https://github.com/cambridge-collection/cudl-viewer-ui) - This library is used to compile the JS and CSS used in the viewer.
- [Cudl Services](https://github.com/cambridge-collection/cudl-services) - This provides access to transcription HTML and TEI metadata.
- [CUDL Solr](https://github.com/cambridge-collection/cudl-solr) - This provides search functionality
- CUDL IIIF Image Servers - These use [IIPImage](https://iipimage.sourceforge.io/)
- [Content Editor](https://github.com/cambridge-collection/dl-loading-ui) - This provdes a nice GUI for editing the data, adding collections / items.
- Data Processing Pipeline - This is on AWS and controls automatically converting TEI for the viewer/platform.

All of this is controlled through Terraform and available here:
- [Terraform Infrastructure](https://github.com/cambridge-collection/cudl-terraform)
